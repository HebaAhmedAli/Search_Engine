

import com.mongodb.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.tartarus.snowball.SnowballStemmer;
import org.tartarus.snowball.ext.englishStemmer;


import java.io.*;
import java.net.UnknownHostException;
import java.util.*;

public class textTags {

    public static stopwords checkStopWord;
//    public static boolean isRecrawling=false;
    static List<DBObject> newWords=new ArrayList<DBObject>();

    //sam3???
    //Ah ana sam3ak 7elw gedan kman




    public String modifyWord(String word,String tag){

        Stemmer porterStemmer = new Stemmer();
        word=word.toLowerCase();
        //Check if Stop word
         if(tag=="p"||tag=="span"||tag=="pre"||tag=="li")
            if(checkStopWord.ifStopWords(word))
               return null ;

        if(checkStopWord.ifCitation(word))
            return null ;
            //Check if special character

         word=word.replaceAll("[^a-zA-Z0-9]", "");

        //else that: Stem the word
        porterStemmer.add(word);
        porterStemmer.stem();
        return porterStemmer.toString();

    }




    public static void indexing(Document doc,String url,boolean isRecrawling) throws IOException {

        checkStopWord=new stopwords();
        textTags teTags=new textTags();
        //unique for the check on the whole txt afterwards
        final String[] neededTags={"h1","h2", "h3", "h4", "h5", "h6"};//"p","pre","span","li",
//        FileWriter outstream= new FileWriter ("outb2a.txt");
        Map<String,DatabaseComm> objToInsert=new HashMap<String,DatabaseComm>();
        DB db=null;

        try {

            MongoClient mongoClient = new MongoClient("localhost", 27017);
            db = mongoClient.getDB("indexerTest");
            System.out.println("Connected to Database");


        } catch (Exception e) {
            System.out.println(e);
        }
        System.out.println("Server is ready ");
        DBCollection collection = db.getCollection("wordsIndex");
        BasicDBObject indexWord = new BasicDBObject();
        indexWord.put("word","a");
        collection.createIndex(indexWord);

        /*
         *
         * Recrawling
         * */


        if (isRecrawling==true){

            BasicDBObject q1 = new BasicDBObject();
            BasicDBObject q2 = new BasicDBObject();
            BasicDBObject q3 = new BasicDBObject();

            q1.put("url",url);
            q2.put("",q1);
            q3.put("urls",q2);

            DBCursor result = collection.find(q3);
            while (result.hasNext()) {
                BasicDBObject querry = (BasicDBObject) result.next();//new BasicDBObject("url",SURL);
                DBObject update_idf = new BasicDBObject();
                update_idf.put("$inc", new BasicDBObject("idf", -1));
                collection.updateMulti(querry, update_idf);

            }
            BasicDBObject b1 = new BasicDBObject();
            BasicDBObject b2 = new BasicDBObject();
            BasicDBObject b3 = new BasicDBObject();
            BasicDBObject b4 = new BasicDBObject();
            BasicDBObject b5 = new BasicDBObject();
        //subdoc content -> uongodrl w byms7o 4la tool
            b2.put("url",url );
            b3.put("urls",b2);
            b4.put("$pull",b3);
            b5.put("multi","true");


            collection.update(b1,b4,false,true);

        }


        /**********************************************************************************/


        String innerBody=doc.select("body").text();
        /////////////////////////////////////////////////
//        for (String tag:neededTags) {
////            outstream.write("USED TAG: "+tag+'\n');
//            for (Element element : doc.select(tag)) {
//
//                String[] words = element.text().split(" ");
//
//                for (String word : words) {
//
//                    word=teTags.modifyWord(word,tag);
//                    if(word==null)
//                        continue;
//                    if(word.length()==1&&word!="a")
//                        continue;
//
//
//                    if (! objToInsert.containsKey(word)){
//                        objToInsert.put(word,new DatabaseComm());
//                        objToInsert.get(word).changeTag();
//
//
//                    }
//
//
////                    outstream.write(word + " ");
//
//                }
//
//            }
//        }


        String[] words = innerBody.split(" ");
        for (int i = 0; i < words.length; i++) {
            String word=words[i];
            if (objToInsert.containsKey(word))
                continue;

            word=teTags.modifyWord(word,"p");
            if(word==null)
                continue;
            if(word.length()==1&&word!="a")
                continue;

//            outstream.write(word + " ");
            if (! objToInsert.containsKey(word))
                objToInsert.put(word,new DatabaseComm());

            //positions of the word in inner body
            objToInsert.get(word).addPosition(i);

        }


        for (Map.Entry<String,DatabaseComm> insert:objToInsert.entrySet()){

            BasicDBObject theWord = new BasicDBObject();
            theWord.put("word",insert.getKey());

            DBCursor dbCursor = collection.find(theWord);
            if (dbCursor.hasNext()){
                // the word is already exists in our db

                // function (true if recreawl ya3ni htms7 mn 2l database 2l url dh mn kol 2l words 2l true dh heba w feryal homa 2lli ba3tinholna
                BasicDBObject idfinc = new  BasicDBObject().append("$inc",
                        new BasicDBObject().append("idf", 1));
                collection.update(new BasicDBObject().append("word",insert.getKey()),idfinc);

                //da ele gwa l array ele gwa
                BasicDBObject urlObject = new BasicDBObject();
                urlObject.put("url", url);
                urlObject.put("tf", insert.getValue().getOccurence());
                urlObject.put("tag",insert.getValue().getTag());
                urlObject.put("positions",insert.getValue().getPositions());

                //b7oto fe array b2a
                BasicDBObject tempisa = new BasicDBObject();

                BasicDBObject llfind = new BasicDBObject();llfind.put("positions",urlObject);
                tempisa.put("$addToSet", new BasicDBObject().append("docs", llfind));
                collection.update(new BasicDBObject().append("word",insert.getKey()),tempisa);

            }
            else {
                // the word is not inserted yeeeeet
                // lets insert it b2a
                if (insert.getKey()=="")
                    continue;

                theWord.put("idf", 1);

                List<BasicDBObject> URLs = new ArrayList<>();
                BasicDBObject urlObject = new BasicDBObject();
                urlObject.put("url", url);
                urlObject.put("tf", insert.getValue().getOccurence());
                urlObject.put("tag",insert.getValue().getTag());
                urlObject.put("positions",insert.getValue().getPositions());

                URLs.add(urlObject);
                theWord.put("urls", URLs);
                newWords. add(theWord);
//                collection.insert(theWord);
            }

        }

        collection.insert(newWords);
//        outstream.close();

    }
}