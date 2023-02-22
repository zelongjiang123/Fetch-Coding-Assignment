/*
 * author: Zelong Jiang
 */

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;


public class Fetch {
	/*
	 * Transaction class store the information of each transaction
	 * Transaction is comparable based on each date
	 */
	class Transaction implements Comparable<Transaction>{
		String name;
		int points;
		String date;
		public Transaction(String thename, int thepoints, String thedate) {
			name = thename;
			points = thepoints;
			date = thedate;
		}
		@Override
		public int compareTo(Fetch.Transaction o) {
			return date.compareTo(o.date);
		}
	}
	public void transaction(int points, String filepath) throws FileNotFoundException
	{
		String line; // String used to parse the csv file line by line
        BufferedReader read = new BufferedReader(new FileReader(filepath)); // read csv to the buffer
        PriorityQueue<Transaction> pq = new PriorityQueue<>();  // used to store each transaction
        Map<String, Integer> scoreMap = new HashMap<>(); // store the score of each payer
        try {
                line = read.readLine(); // read the first row
                
                while ((line = read.readLine()) != null) {
                	//attribute[0] is customer_id, attribute[1] is the date, attribute[2] is amount
                	String[] attributes = line.split(","); //split the line by comma
                	
                	// wrong input format
                	if(attributes[0].length() == 0 || attributes[1].length() == 0 || attributes[2].length() != 22)
                		continue;
                	int score = Integer.valueOf(attributes[1]);
                	Transaction newtran = new Transaction(attributes[0], score, attributes[2]);
                	pq.add(newtran);
                	if(!scoreMap.containsKey(attributes[0]))
                		scoreMap.put(attributes[0], 0);
                	scoreMap.put(attributes[0], scoreMap.get(attributes[0]) + score);
                }
                
                read.close(); // close the reader
                
                int spent = 0; // used to calculate the points spent
                while(spent < points)
                {
                	Transaction current = pq.poll(); //get the earliest transaction
                	int currentpoints = scoreMap.get(current.name);
                	if(currentpoints - current.points >= 0) // check whether the player has a nonnegative balance
                	{
                		currentpoints -= current.points;
                		spent += current.points;
                		if(spent > points)
                		{
                			currentpoints += spent - points;
                			spent = points;
                		}
                		scoreMap.put(current.name, currentpoints);
                	}
                }
                
                // output the balance of each payer
                int size = 1;
                for(Map.Entry<String, Integer> e: scoreMap.entrySet())
                {
                	if(size < scoreMap.size())
                		System.out.println(e.getKey() + ": " + e.getValue() + ",");
                	else {
                		System.out.println(e.getKey() + ": " + e.getValue());
					}
                	size++;
                }    
        } catch (Exception e) {
                System.out.println(e.getMessage()); // catch IOException from readLine()
        }
	}
	/*
	 * used to test the method
	 */
	public static void main(String[] args)
	{
		try {
			Fetch fetch = new Fetch();
			/* IMPORTANT
			 * change the argument below to the path of input tansaction.csv file
			 */
			fetch.transaction(5000, "D:\\eclipse-workspace\\cs577\\transactions.csv");
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
	}
}
