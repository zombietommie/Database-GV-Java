/**
 * Josh Tran
 * Jenny Le
 * Tommy Tran
 */

package main;
import java.awt.print.Printable;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.sql.Date;


public class QueryGV {

	Connection conn; 
	final String host = "dbsvcs.cs.uno.edu"; 
	final int port = 1521;
	final String sID = "orcl";

	// Three database connection link constructors
	public QueryGV(	String host, 
			int port, 
			String sID, 
			String username, 
			String passwd) throws SQLException { 
				conn = new DatabaseConnection(host, port, sID).getDatabaseConnection(username, passwd); 
			}
	public QueryGV(String username, String passwd) throws SQLException { 
		this.conn = new DatabaseConnection(host, port, sID).getDatabaseConnection(username, passwd); 
	}

	public QueryGV(Connection conn) throws SQLException { 
		this.conn = conn; 
	}
	// END OF connection constructors
	

	
	/**
	 * Query 13 setup
	 * @param per_id
	 * @return 
	 * @throws SQLException
	 */
	public ArrayList<String[]> workerJob(String per_id) throws SQLException {
		String str = "SELECT first_name, last_name, job_title " + 
				"FROM Person NATURAL JOIN works " + 
				"WHERE per_id = ? ";
		ArrayList<String[]> al = new ArrayList<String[]>();
		PreparedStatement pStmt = conn.prepareStatement(str);
		pStmt.setString(1, per_id);
		ResultSet rs = pStmt.executeQuery();
		while(rs.next()) {
			String[] line = new String[3];
			line[0] = rs.getString("first_name");
			line[1] = rs.getString("last_name");
			line[2] = rs.getString("job_title");
			al.add(line);
		}
		return al;
	}
	
	/**
	 * Query 14 setup
	 * @param pos_code
	 * @return 
	 * @throws SQLException
	 */
	public ArrayList<String[]> onceHeldPosition(String pos_code) throws SQLException {
		String str = "SELECT per_id, first_name, job_title, start_date, end_date " + 
				"FROM person natural join works " + 
				"WHERE pos_code = ? ";
		ArrayList<String[]> al = new ArrayList<String[]>();
		PreparedStatement pStmt = conn.prepareStatement(str);
		pStmt.setString(1, pos_code);
		ResultSet rs = pStmt.executeQuery();
		while (rs.next()) {
			String[] line = new String[5];
			line[0] = rs.getString("per_id");
			line[1] = rs.getString("first_name");
			line[2] = rs.getString("job_title");
			line[3] = rs.getString("start_date");
			line[4] = rs.getString("end_date");
			al.add(line);
		}
		return al;
	}
	
	
	
	
	
	
	/** tester
	 * Run Query here!!!
	 * WARNING THESE ARE HARDCODED NEED APP
	 */
	public static void main(String[] args) throws SQLException {
		
		// Start of the connection to database by asking for username and password
		if (args.length == 1) {
			System.out.println("usage: java SampleQuery db-IP dp-SID"); 
			System.exit(1);
		} 
		DatabaseConnection dbc; 
		if (args.length == 0)
			dbc = new DatabaseConnection("dbsvcs.cs.uno.edu", 1521, "orcl"); 
		else 
			dbc = new DatabaseConnection(args[0], 1521, args[1]); 
		Scanner scanner = new Scanner(System.in);
		System.out.println("User Name: ");
		String username = scanner.nextLine();
		System.out.println("passcode: ");
		String dbpassword = scanner.nextLine(); 
		Connection conn = dbc.getDatabaseConnection(username, dbpassword); 
		QueryGV sqObj = new QueryGV(conn);
		// END OF database connection process
		
		// Variables in MAIN
		Boolean quit = false;
		
		
		/**
		 * Loop to run the program 
		 */
		while(!quit) {
			System.out.println("\n\n*****JJT LD Database Java Query Runner*****\n\n");
			System.out.println("Please enter a query number (13-28) or 0 to QUIT: ");
			// give user choice option
			try {
				int choice = scanner.nextInt();
				
				if (choice > 0 && choice < 13) {
					System.out.println("ERROR>>>>> You have entered the value below the given range!");
				}
				else if (choice == 13) {
					// Query 13 runner
					System.out.println("Running Query 13:");
					System.out.println("Given a person’s identifier, find all the jobs this person is currently holding and worked\n" + 
							"in the past.");
					System.out.print("Enter a person's ID: ");
					String ans = getAnswerString();
					ArrayList<String[]> str = sqObj.workerJob(ans);
					for (String[] line : str) {
						System.out.printf("first_name\tlast_name\tjob_title\n%s\t\t%s\t\t%s\n\n", line[0], line[1], line[2]);
					}
				}
				else if (choice == 14) {
					// Query 14 runner 
					System.out.println("Running Query 14:");
					System.out.println("In a local or national crisis, we need to find all the people who once held a position of the given pos_code. List\n" + 
							"per_id, name, job title and the years the person worked in (starting year and ending year).");
					System.out.println("Enter a position code: ");
					String ans = getAnswerString();
					ArrayList<String[]> str2 = sqObj.onceHeldPosition(ans);
					for (String[] line : str2) {
						System.out.printf("pos_code\tfirst_name\tjob_title\t\tstart_date\t\tend_date\n%s\t\t%s\t\t%s\t%s\t%s\n\n ", 
								line[0], line[1], line[2], line[3], line[4]);
					}
				}
				// This else is to check if use want to QUIT
				else if (choice == 0) {
					System.out.println("Quiting program...");
					quit = true;
				}
				
			} catch (InputMismatchException e) {
				System.out.println("ERROR>>>> the value must be an integer\n");
				e.printStackTrace();
				quit = true;
			}
		}
		// Closes the Scanner
		scanner.close();
	}
	
	/**
	 * 
	 * @return answer: which is the user input to the query as String
	 */
	public static String getAnswerString() {
		// Create new Scanner 
		Scanner sc = new Scanner(System.in);
		String answer = sc.nextLine();
//		sc.close();
		return answer;
	}
	
	/**
	 * 
	 * @return answer: which is the user input to the query as integer
	 */
	public static int getAnswerInt() {
		// Create new Scanner 
		Scanner sc = new Scanner(System.in);
		int answer = sc.nextInt();
//		sc.close();
		return answer;
	}
}
