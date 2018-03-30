import com.mongodb.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bson.*;

public class dbInterface {

    DB db=null;
    static List<BasicDBObject> wordsFirstinserted;
    dbInterface(){
        wordsFirstinserted=new ArrayList<>();
    }



    public void initDB(String DBname,String DBCollection, Map<String,Map<String,DatabaseComm>> interConnection,String url){
        try {

            MongoClient mongoClient = new MongoClient("localhost", 27017);
            db = mongoClient.getDB(DBname);
            System.out.println("Connected to Database");

        } catch (Exception e) {
            System.out.println(e);
        }
        System.out.println("Server is ready ");
        DBCollection collection = db.getCollection(DBCollection);
        Map<String,DatabaseComm> insertInURLs=new HashMap<>();

        for (Map.Entry<String,Map<String,DatabaseComm>> stemmedword:interConnection.entrySet()){
            BasicDBObject theWord = new BasicDBObject();
            theWord.put("word",stemmedword.getKey());

            DBCursor dbCursor = collection.find(theWord);
            if (dbCursor.hasNext()) {
                //TODO: check for the word to be inserted
            }else{
                for (Map.Entry<String,DatabaseComm> originalWords:stemmedword.getValue().entrySet()){
                    BasicDBObject toInsert=new BasicDBObject();

                    theWord.put("idf", 1);

                    List<BasicDBObject> URLs = new ArrayList<>();
                    BasicDBObject wordObject = new BasicDBObject();
                    wordObject.put("url", url);
                    wordObject.put("theword", originalWords.getKey());
                    wordObject.put("tf", originalWords.getValue().getOccurence());
                    wordObject.put("tag",originalWords.getValue().getTag());
                    wordObject.put("positions",originalWords.getValue().getPositions());
                    URLs.add(wordObject);
                    theWord.put("words", URLs);
                    wordsFirstinserted.add(theWord);
                }
            }

        }


    }



}
