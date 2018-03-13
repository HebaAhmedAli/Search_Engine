


import com.mongodb.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;

import mpi.*;

public class indexer implements Serializable{
	
	 static MongoClient mongoClient ;
	 static DB database ;
	 
	public indexer()
	{
		
	}
	
	public void start_indexer(URL url,Document d) throws ClassNotFoundException{
		
		try {
			mongoClient = new MongoClient();
		    database = mongoClient.getDB("search_engine");
		} catch (MongoException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

		while(true) {
			recv_from_crawler( url, d);
			try{
                textTags.indexing(d,url.toString());
            }catch (IOException e){
			    System.out.println(e);
            }

		}
		
	}
	public void recv_from_crawler(URL url,Document d) throws ClassNotFoundException
    {
		//i think hn7tag nbreak 
		
		byte[] yourBytes_url= new byte[10000];
		int document_size=0;
		URL url_from_crawler=null;
		Document doc_from_crawler=null;
		//10000 is assumed to be max url size
		MPI.COMM_WORLD.Recv(yourBytes_url,0,10000,MPI.BYTE,0,0);
	
		
		//Create object from bytes
		ByteArrayInputStream bis = new ByteArrayInputStream(yourBytes_url);
		ObjectInput in = null;
		try {
		  in = new ObjectInputStream(bis);
		  url_from_crawler = (URL) in.readObject(); 
		 // System.out.println("url_from_crawler ----> "+url_from_crawler.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
		  try {
		    if (in != null) {
		    	
		      in.close();
		    }
		  } catch (IOException ex) {
		    // ignore close exception
		  }
		}
		
		
	
		get_document_from_db(url_from_crawler.toString(),d);
		
    }

	private void get_document_from_db(String url, Document d) {
		// TODO Auto-generated method stub
		DBCollection collection = database.getCollection("url");
		DBCursor cursor = collection.find(new BasicDBObject("url_name", url),new BasicDBObject("document",1));
		
		//---?? leeh de while
		String doc_from_db = null;
		while(cursor.hasNext()) {
			//System.out.println("only one parent at a time for "+ key);
		     BasicDBObject object = (BasicDBObject) cursor.next();
		     doc_from_db = object.getString("document");
		    
		}
		//to do
	
		//Parse a document from a String
		d= Jsoup.parse(doc_from_db);
		
		//System.out.println("document at indexer---->\n"+d);
		
	}

	
	

}