
import org.jsoup.nodes.Document;
import java.net.URL;
import mpi.*;

public class search_engine {
	
	public static void main(String[] args) throws InterruptedException, ClassNotFoundException {
		// TODO Auto-generated method stub

		MPI.Init(args);
		Integer rank=MPI.COMM_WORLD.Rank();
		Integer size=MPI.COMM_WORLD.Size();
		
		
		
		if(rank==0)
		{
			//crawler
			//handelo eh ? 
			WebCrawlerWithDepth my_webcrawl = new WebCrawlerWithDepth(5);
			my_webcrawl.start_crawler();
		}
		else if(rank==1)
		{
			//indexer
			indexer my_indexer= new indexer();
			URL dummy_url=null;
			Document dummy_doc=null;
			my_indexer.start_indexer(dummy_url,dummy_doc);

		}
		
		MPI.Finalize();
	}

}