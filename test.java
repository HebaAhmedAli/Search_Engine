

import com.mongodb.*;
import com.mongodb.util.JSON;
import sun.security.pkcs11.Secmod;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class test {


    public static void main (String[] args)throws Exception {

        DB db=null;
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        try {


            MongoClient mongoClient = new MongoClient("localhost", 27017);
            db = mongoClient.getDB("indexerTest");
            System.out.println("Connected to Database");



        } catch (Exception e) {
            System.out.println(e);
        }


       System.out.println("Server is ready ");


        BasicDBObject theWord = new BasicDBObject();

        theWord.put("word","german");


        DBCollection collection = db.getCollection("wordsIndex");

        BasicDBObject q1 = new BasicDBObject();
        BasicDBObject q2 = new BasicDBObject();
        BasicDBObject q3 = new BasicDBObject();
        BasicDBObject q4 = new BasicDBObject();


        String SURL = "url nfsak fih";

        q1.put("url",SURL);
        q2.put("",q1);
        q3.put("urls",q2);




        DBCursor result = collection.find(q3);
       while (result.hasNext()) {
           BasicDBObject querry = (BasicDBObject) result.next();//new BasicDBObject("url",SURL);
           DBObject update_idf = new BasicDBObject();
           update_idf.put("$inc", new BasicDBObject("idf", -1));
           collection.updateMulti(querry, update_idf);
//
//           DBObject update_urls = new BasicDBObject();
//           update_urls.put("$pull", new BasicDBObject("url", SURL));
//           collection.updateMulti(querry, update_urls);
       }


        BasicDBObject b1 = new BasicDBObject();
        BasicDBObject b2 = new BasicDBObject();
        BasicDBObject b3 = new BasicDBObject();
        BasicDBObject b4 = new BasicDBObject();
        BasicDBObject b5 = new BasicDBObject();
        BasicDBObject b6 = new BasicDBObject();
        b2.put("url","url nfsak fih" );
        b3.put("urls",b2);
        b4.put("$pull",b3);
        b5.put("multi","true");


        collection.update(b1,b4,false,true);


//        DBCursor dbCursor = collection.find(theWord);
//        if (dbCursor.hasNext()){
//
//        }
//        BasicDBObject idfinc = new  BasicDBObject().append("$inc",
//                new BasicDBObject().append("idf", 1));
//
//        collection.update(new BasicDBObject().append("word","german"),idfinc);
//
//
//        List<BasicDBObject> occurence = new ArrayList<>();
//
//
//            BasicDBObject occurenceTag = new BasicDBObject();
//            occurenceTag.put("tagName", "h1");
//            occurenceTag.put("numOccur", 16);
//            occurence.add(occurenceTag);
//
//        BasicDBObject occurenceTag2 = new BasicDBObject();
//        occurenceTag2.put("tagName", "pre");
//        occurenceTag2.put("numOccur", 29);
//        occurence.add(occurenceTag2);
//
//
//
//        BasicDBObject urlObject = new BasicDBObject();
//        urlObject.put("url", "https://bos_b2a_3l4an_tb2a_3arf ");
//        urlObject.put("tf", 55);
//        urlObject.put("occurence", occurence);
//
//        BasicDBObject tempisa = new BasicDBObject();
//        tempisa.put("$push", new BasicDBObject().append("urls", urlObject));
//        collection.update(new BasicDBObject().append("word","german"),tempisa);
//
/*

        BasicDBObject object = ( BasicDBObject) collection.find().next();

        DBCursor cursor = collection.find();

        db.wordsIndex.update({word:"toz"}, {$push:{urls:{url:"lk"} }})





        BasicDBObject idfinc = new  BasicDBObject().append("$inc",
                new BasicDBObject().append("idf", -1));

        collection.update(new BasicDBObject().append("word",insert.getKey()),idfinc);




        BasicDBObject pullURL = new  BasicDBObject().append("$pull",
                new BasicDBObject().append("idf", 1));

        BasicDBObject urlObject = new BasicDBObject();
        urlObject.put("url", "https://bos_b2a_3l4an_tb2a_3arf ");

        // BasicDBObject urls = new BasicDBObject();
        List<BasicDBObject> URLs = new ArrayList<>();
        URLs.add(urlObject);




        BasicDBObject tempisa = new BasicDBObject();
        tempisa.put("","");
        tempisa.put("$pull", new BasicDBObject().append("urls", URLs));
        tempisa.put("multi","true");




        System.out.println(tempisa);
        collection.update(new BasicDBObject().append("url","https://bos_b2a_3l4an_tb2a_3arf "),tempisa);


        BasicDBObject b1 = new BasicDBObject();
        BasicDBObject b2 = new BasicDBObject();
        BasicDBObject b3 = new BasicDBObject();
        BasicDBObject b4 = new BasicDBObject();
        BasicDBObject b5 = new BasicDBObject();
        BasicDBObject b6 = new BasicDBObject();
        b2.put("url","https://bos_b2a_3l4an_tb2a_3arf " );
        b3.put("urls",b2);
        b4.put("$pull",b3);
        b5.put("multi","true");


        collection.update(b1,b4,false,true);









*/




//        DBCollection collection = db.getCollection("collection");
//        BasicDBObject document = new BasicDBObject();
//        document.put("hosting", "hostA");
//        document.put("type", "vps");
//        document.put("clients", 1000);
//        collection.insert(document);
//
//        System.out.println("after insertion");
//        DBCursor dbCursor = collection.find();
//        while(dbCursor.hasNext()){
//            System.out.println(dbCursor.next());
//        }



    }




}

