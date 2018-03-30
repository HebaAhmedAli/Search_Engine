

import com.mongodb.*;

import java.util.ArrayList;
import java.util.List;


public class test {


    public static void main (String[] args)throws Exception {

        DB db=null;
        try {


            MongoClient mongoClient = new MongoClient("localhost", 27017);
            db = mongoClient.getDB("aabdotest");
            System.out.println("Connected to Database");



        } catch (Exception e) {
            System.out.println(e);
        }

        DBCollection collection = db.getCollection("boys");

        System.out.println("Server is ready ");




//        BasicDBObject document = new BasicDBObject();
//        document.put("word", "la");
//
//
//        BasicDBList docs = new BasicDBList();
//        BasicDBObject doc = new BasicDBObject();
//        doc.put("theword", "la2");
//
//        BasicDBList positions = new BasicDBList();
//
//        BasicDBObject position = new BasicDBObject();
//        position.put("docnum",2);
//        position.put("wordpos", 4);
//
//        positions.add(position);
//
//
//        doc.put("positions",positions);
//        docs.add(doc);
//
//        document.put("docs", docs);
//
//        collection.insert(document);


//        BasicDBObject theWord = new BasicDBObject();
//
//        theWord.put("word","german");
//

///*
//*
//* nm7 kol ele y7os l url
//* */
//        String SURL="https://www.geeksforgeeks.org/";
//        BasicDBObject q1 = new BasicDBObject();
//        BasicDBObject q2 = new BasicDBObject();
//        BasicDBObject q3 = new BasicDBObject();
//
//        q1.put("url",SURL);
//        q2.put("",q1);
//        q3.put("urls",q1);
//
//        DBObject update_idf = new BasicDBObject();
//        update_idf.put("$inc", new BasicDBObject("idf", -1));
//        collection.updateMulti(q3, update_idf);
//
//        DBObject update = new BasicDBObject();
//		update.put("$pull", q3);
//		collection.updateMulti(q3, update);
//
//
//         System.out.println("5alst ya bashr...");
//
/*DBCursor result = collection.find(q3);
        while (result.hasNext()) {
            BasicDBObject querry = (BasicDBObject) result.next();//new BasicDBObject("url",SURL);
            DBObject update_idf = new BasicDBObject();
            update_idf.put("$inc", new BasicDBObject("idf", -1));
            collection.updateMulti(querry, update_idf);

        }*/
      /*  BasicDBObject b1 = new BasicDBObject();
        BasicDBObject b2 = new BasicDBObject();
        BasicDBObject b3 = new BasicDBObject();
        BasicDBObject b4 = new BasicDBObject();
        BasicDBObject b5 = new BasicDBObject();

        b2.put("url",SURL );
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
*/
//        BasicDBObject docObject = new BasicDBObject();
//        docObject.put("docnum", 2);
//        docObject.put("wordpos", 4);
//
//        BasicDBObject tempisa = new BasicDBObject();
//        tempisa.put("$addToSet", new BasicDBObject().append("docs.0.positions", docObject));
//        //TODO:    "docs.<index l doc fel array>.positions" d lazem tgebha w t7oteha ya mnonet 2albe <3
//        collection.update(new BasicDBObject().append("word","la"),tempisa);



        BulkWriteOperation builder = collection.initializeUnorderedBulkOperation();


        BasicDBObject o1= new BasicDBObject();
        BasicDBObject o2= new BasicDBObject();
        BasicDBObject o3= new BasicDBObject();
        BasicDBObject o4= new BasicDBObject();
        BasicDBObject o5= new BasicDBObject();
        BasicDBObject o6= new BasicDBObject();
        BasicDBObject o7= new BasicDBObject();



        o1.put("name","samir");
        o1.put("age",23);


        o2.put("name","aabdo");
        o4.put("age",50);

        o3.put("$set",o4);


        builder.insert(o1);

        builder.find(o2).update(o3);

        BulkWriteResult result = builder.execute();
      System.out.println(result.isAcknowledged());







//        List<Integer> tags = new ArrayList<Integer>();
//        tags.add(11);
//        tags.add(6);
//        tags.add(569);
//        BasicDBObject doc = new BasicDBObject();
//        doc.append("url", tags);
//        collection.insert(doc);

    }




}

