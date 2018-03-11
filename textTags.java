

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
   public static boolean isRecrawling=true;




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

    public static void indexing(Document doc) throws IOException {

        checkStopWord=new stopwords();
        textTags teTags=new textTags();
        String file="test2.html"; //get from url
        //unique for the check on the whole txt afterwards
        final String[] neededTags={"p","pre","span","li","h1","h2", "h3", "h4", "h5", "h6"};

//        BufferedReader reader = new BufferedReader(new FileReader (file));
        String url="";

        FileWriter outstream= new FileWriter ("outb2a.txt");

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





        /*
         *
         * Recrawling
         * */


        if (isRecrawling==true){

            BasicDBObject q1 = new BasicDBObject();
            BasicDBObject q2 = new BasicDBObject();
            BasicDBObject q3 = new BasicDBObject();
            BasicDBObject q4 = new BasicDBObject();


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
            BasicDBObject b6 = new BasicDBObject();
            b2.put("url",url );
            b3.put("urls",b2);
            b4.put("$pull",b3);
            b5.put("multi","true");


            collection.update(b1,b4,false,true);

            ///////////////////////////////////////////////////
            ////////////////////////////////////////////////


//
//                    BasicDBObject idfinc = new  BasicDBObject().append("$inc",
//                            new BasicDBObject().append("idf", -1));
//
//                    collection.update(new BasicDBObject().append("word",insert.getKey()),idfinc);
//
//
//                    BasicDBObject pullURL = new  BasicDBObject().append("$pullAll",
//                            new BasicDBObject().append("idf", 1));
//
//                    BasicDBObject urlObject = new BasicDBObject();
//                    urlObject.put("url", url);
//
//                   // BasicDBObject urls = new BasicDBObject();
//                    List<BasicDBObject> URLs = new ArrayList<>();
//                    URLs.add(urlObject);
//
//
//
//
//                    BasicDBObject tempisa = new BasicDBObject();
//
//                    tempisa.put("$pullAll", new BasicDBObject().append("urls", URLs));
//                    collection.update(new BasicDBObject().append("urls",url),tempisa);


        }


        /**********************************************************************************/


        String innerBody=doc.select("body").text();
        /////////////////////////////////////////////////
        for (String tag:neededTags) {
            outstream.write("USED TAG: "+tag+'\n');
            for (Element element : doc.select(tag)) {

                String[] words = element.text().split(" ");

                for (String word : words) {

                    word=teTags.modifyWord(word,tag);
                    if(word==null)
                        continue;
                    if(word.length()==1&&word!="a")
                        continue;

                    if(word.length()<=1&& word!="a")
                        continue;

                    if (! objToInsert.containsKey(word))
                        objToInsert.put(word,new DatabaseComm());

                    objToInsert.get(word).insertWord(tag);

                    outstream.write(word + " ");

                }

            }
        }


        String[] words = innerBody.split(" ");
        for (String word : words){
            if (objToInsert.containsKey(word))
                continue;

            word=teTags.modifyWord(word,"p");
            if(word==null)
                continue;
            if(word.length()==1&&word!="a")
                continue;

            outstream.write(word + " ");
            if (! objToInsert.containsKey(word))
                objToInsert.put(word,new DatabaseComm());

            objToInsert.get(word).insertWord("p");

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


                List<BasicDBObject> occurence = new ArrayList<>();
                for (Map.Entry<String, Integer> tagsOccur : insert.getValue().getWordtags().entrySet()) {

                    BasicDBObject occurenceTag = new BasicDBObject();
                    occurenceTag.put("tagName", tagsOccur.getKey());
                    occurenceTag.put("numOccur", tagsOccur.getValue());
                    occurence.add(occurenceTag);

                }


                BasicDBObject urlObject = new BasicDBObject();
                urlObject.put("url", url);
                urlObject.put("tf", insert.getValue().getOccurence());
                urlObject.put("occurence", occurence);

                BasicDBObject tempisa = new BasicDBObject();
                tempisa.put("$addToSet", new BasicDBObject().append("urls", urlObject));
                collection.update(new BasicDBObject().append("word",insert.getKey()),tempisa);

            }
            else {
                // the word isnot inserted yeeeeet
                // lets insert it b2a
                if (insert.getKey()=="")
                    continue;
                System.out.println("ana awl mra ashof l kelma d");
                theWord.put("idf", 1);
                List<BasicDBObject> occurence = new ArrayList<>();
                for (Map.Entry<String, Integer> tagsOccur : insert.getValue().getWordtags().entrySet()) {

                    BasicDBObject occurenceTag = new BasicDBObject();
                    occurenceTag.put("tagName", tagsOccur.getKey());
                    occurenceTag.put("numOccur", tagsOccur.getValue());
                    occurence.add(occurenceTag);

                }

                List<BasicDBObject> URLs = new ArrayList<>();
                BasicDBObject urlObject = new BasicDBObject();
                urlObject.put("url", url);
                urlObject.put("tf", insert.getValue().getOccurence());
                urlObject.put("occurence", occurence);

                URLs.add(urlObject);
                theWord.put("urls", URLs);

                collection.insert(theWord);
            }

            }


            outstream.close();
    }
}
//    void insert_map_in_db()
//    {
//        DBCollection collection = database.getCollection("url");
//        System.out.println("collec " + collection);
//
//        for (String key : links.keySet()) {
//
//            for(String value: links.get(key)) {
//
//                if(!(value.equals("no parent") ||  value.equals("same parent"))) {
//
//                    //get the id of the parent url by its name and selects only the field url_name to return
//                    DBCursor cursor = collection.find(new BasicDBObject("url_name", value),new BasicDBObject("url_name",1));
//
//                    //---?? leeh de while
//                    while(cursor.hasNext()) {
//                        //System.out.println("only one parent at a time for "+ key);
//                        BasicDBObject object = (BasicDBObject) cursor.next();
//                        //  String parenturl_id = object.getString("url_name");
//
//
//
//                        //append the parenturl_id  to the url which is the key in the map (and it already exists in the DB)
//                        BasicDBObject newDocument = new BasicDBObject();
//                        newDocument.append("$push", new BasicDBObject().append("in_links_id", object.getObjectId("_id") ));  //to be added to in_links_id
//
//                        BasicDBObject searchQuery = new BasicDBObject().append("url_name", key);
//
//                        collection.update(searchQuery, newDocument);
//                    }
//                }
//            }
//        }
//    }
//    void insert_url_in_db(String url_name, String d,int out_links)
//    {
//        DBCollection collection = database.getCollection("url");
//        BasicDBObject url = new BasicDBObject();
//
//        url.put("url_name", url_name);
//        url.put("pr", priority);
//        priority++;
//        if(url_name.contains("/watch?v=")||(url_name.contains("youtube")&&url_name.contains("embed")))
//            url.put("is_video", true);
//        else
//            url.put("is_video", false);
//
//        url.put("document", d);
//        url.put("out_links_no", out_links);
//
//        collection.insert(url);
//    }