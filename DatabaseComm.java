
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.MongoClient;

import java.util.*;

public class DatabaseComm {

     int occurence;
    ArrayList<Integer> positions;
     Map<String, Integer > wordtags;


    public void addPosition(int pos) {
        positions.add(pos);
    }


    public ArrayList<Integer> getPositions() {
        return positions;
    }

    public DatabaseComm(){
        wordtags=new HashMap<String,Integer >();
        positions=new ArrayList<Integer>();
        occurence=0;
    }

    public int getOccurence() {
        return occurence;
    }

    public Map<String, Integer> getWordtags() {
        return wordtags;
    }

    public void insertWord(String tag){

            occurence++;
            if(wordtags.containsKey(tag)){
                wordtags.put(tag,wordtags.get(tag)+1);
            }
            else
                wordtags.put(tag,1);

    }
    public List<BasicDBObject> getTagOccurrences(){

        List<BasicDBObject> occurrence = new ArrayList<>();
        for (Map.Entry<String, Integer> tagsOccur : wordtags.entrySet()) {

            BasicDBObject occurenceTag = new BasicDBObject();
            occurenceTag.put("tagName", tagsOccur.getKey());
            occurenceTag.put("numOccur", tagsOccur.getValue());
            occurrence.add(occurenceTag);

        }
        return  occurrence;

    }


}
