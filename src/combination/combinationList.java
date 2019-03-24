package combination;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class combinationList {

	private List<String> listOfCobination;
	

	public combinationList(String fileName ) throws FileNotFoundException {
		this.listOfCobination = new ArrayList<String>();
		
	}
	
  public void combinationFromFileToList(String filePath) throws IOException {
	  InputStream in = new FileInputStream(new File(filePath));
	  BufferedReader reader = new BufferedReader(new InputStreamReader(in));
	  String line;
	  
	  while(((line = reader.readLine()) != null)){
		  
		  
		  this.listOfCobination.add(line);
	  }
			  
  } 
  

  public void printAllCombination() {
	  Iterator<String> it = this.listOfCobination.iterator();
      while(it.hasNext()){
         System.out.println(it.next());
      }
	  
  }
	
	
	
}

