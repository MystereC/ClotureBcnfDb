package combination;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;



public class genarateCombination {
	
	
	private String givenAttribute[];
	
	
	public genarateCombination(String givenAttribute[]) {
		
		this.givenAttribute= givenAttribute;
		
	}
	
	
	 /* arr[]  ---> Input Array 
   data[] ---> Temporary array to store current combination 
   start & end ---> Staring and Ending indexes in arr[] 
   index  ---> Current index in data[] 
   r ---> Size of a combination to be printed */
	
   public void combinationUtil(String data[], int start, int end, int index, int r) 
   { 
       // Current combination is ready to be printed, print it 
       if (index == r) 
       { 
    	   
           for (int j=0; j<r; j++) 
               System.out.print(data[j]); 
           System.out.println("");;
           return; 
       } 
       // replace index with all possible elements. The condition 
       // "end-i+1 >= r-index" makes sure that including one element 
       // at index will make a combination with remaining elements 
       // at remaining positions 
       for (int i=start; i<=end && end-i+1 >= r-index; i++) 
       { 
           data[index] = this.givenAttribute[i]; 
                    
           combinationUtil(data, i+1, end, index+1, r); 
       } 
   } 
   
   
   
   // The main function that prints all combinations of size r 
   // in arr[] of size n. This function mainly uses combinationUtil() 
  public void printCombination(int n, int r) 
   { 
       // A temporary array to store all combination one by one 
       String data[]=new String[r]; 
 
       // Print all combination using temprary array 'data[]' 
       combinationUtil(data, 0, n-1, 0, r); 
   } 
  
  public void printAllCombination(int n) 
  {
     
      for(int i=1; i<n+1 ; i++) {
             printCombination( n, i);
          }
	  
  }
  

	
	  
	  
  
   
  /*Driver function to check for above function*/
  public static void main (String[] args) throws IOException { 
      String arr[] = {"A","B","C","D"}; 
      String name="Combination.txt";
      genarateCombination gC= new genarateCombination(arr);
      
      int n = arr.length;       
      
          
       // Creating a File object that represents the disk file. 
          PrintStream o = new PrintStream(new File(name)); 
          combinationList cb = new combinationList(name);
    
          // Store current System.out before assigning a new value 
          PrintStream console = System.out; 
    
          // Assign o to output stream 
          System.setOut(o); 
          gC.printAllCombination(n); 
       // Use stored value for output stream 
          System.setOut(console);
         cb.combinationFromFileToList(name);
         cb.printAllCombination();
          
          
          
         
          
          
          
         
     
         
         
      
      
  } 

}

