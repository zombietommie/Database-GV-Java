/**
 * Josh Tran
 * Jenny Le
 * Tommy Tran
 */

package main;

import java.awt.print.Printable;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
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
	public QueryGV(String host, int port, String sID, String username, String passwd) throws SQLException {
		conn = new DatabaseConnection(host, port, sID).getDatabaseConnection(username, passwd);
	}

	public QueryGV(String username, String passwd) throws SQLException {
		this.conn = new DatabaseConnection(host, port, sID).getDatabaseConnection(username, passwd);
	}

	public QueryGV(Connection conn) throws SQLException {
		this.conn = conn;
	}
	// END OF connection constructors

	// Query Setup
	/**
	 * Query 1 Setup
	 * 
	 * @return
	 * @throws SQLException
	 */
	public ArrayList<String[]> queryOne() throws SQLException {
		String str = "SELECT first_name,last_name " + "FROM Person NATURAL JOIN Job " + "ORDER BY last_name ASC";
		ArrayList<String[]> al = new ArrayList<String[]>();
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(str);
		while (rs.next()) {
			String[] line = new String[2];
			line[0] = rs.getString("first_name");
			line[1] = rs.getString("last_name");
			al.add(line);
		}
		return al;
	}

	/**
	 * Query 2 setup
	 * 
	 * @return
	 * @throws SQLException
	 */
	public ArrayList<String[]> queryTwo() throws SQLException {
		String str = "SELECT first_name, last_name, pay_rate " + "FROM Person NATURAL JOIN Job "
				+ "WHERE pay_type='salary' " + "ORDER BY pay_rate DESC";
		ArrayList<String[]> al = new ArrayList<String[]>();
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(str);
		while (rs.next()) {
			String[] line = new String[3];
			line[0] = rs.getString("first_name");
			line[1] = rs.getString("last_name");
			line[2] = rs.getString("pay_rate");
			al.add(line);
		}
		return al;
	}

	/**
	 * Query 3 setup 
	 * 
	 * @return
	 * @throws SQLException
	 */
	public ArrayList<String[]> queryThree() throws SQLException {
		String str = "SELECT\n" + 
				"    \"A1\".\"FAC_ID\"     \"FAC_ID\",\n" + 
				"    \"A1\".\"FAC_NAME\"   \"FAC_NAME\",\n" + 
				"    \"A1\".\"AVG_PAY\"    \"AVG_PAY\"\n" + 
				"FROM\n" + 
				"    (\n" + 
				"        ( SELECT\n" + 
				"            \"A4\".\"FAC_ID\"     \"FAC_ID\",\n" + 
				"            \"A4\".\"FAC_NAME\"   \"FAC_NAME\",\n" + 
				"            \"A4\".\"AVG_PAY\"    \"AVG_PAY\"\n" + 
				"        FROM\n" + 
				"            (\n" + 
				"                SELECT\n" + 
				"                    \"A5\".\"FAC_ID\"     \"FAC_ID\",\n" + 
				"                    \"A5\".\"FAC_NAME\"   \"FAC_NAME\",\n" + 
				"                    AVG(\"A5\".\"PAY_RATE\") \"AVG_PAY\"\n" + 
				"                FROM\n" + 
				"                    (\n" + 
				"                        SELECT\n" + 
				"                            \"A6\".\"FAC_ID\"     \"FAC_ID\",\n" + 
				"                            \"A7\".\"PAY_RATE\"   \"PAY_RATE\",\n" + 
				"                            \"A7\".\"PAY_TYPE\"   \"PAY_TYPE\",\n" + 
				"                            \"A6\".\"FAC_NAME\"   \"FAC_NAME\"\n" + 
				"                        FROM\n" + 
				"                            \"JSLE1\".\"JOB\"       \"A7\",\n" + 
				"                            \"JSLE1\".\"FACTORY\"   \"A6\"\n" + 
				"                        WHERE\n" + 
				"                            \"A7\".\"FAC_ID\" = \"A6\".\"FAC_ID\"\n" + 
				"                    ) \"A5\"\n" + 
				"                WHERE\n" + 
				"                    \"A5\".\"PAY_TYPE\" = 'salary'\n" + 
				"                GROUP BY\n" + 
				"                    \"A5\".\"FAC_ID\",\n" + 
				"                    \"A5\".\"FAC_NAME\"\n" + 
				"            ) \"A4\"\n" + 
				"        )\n" + 
				"        UNION\n" + 
				"        ( SELECT\n" + 
				"            \"A3\".\"FAC_ID\"     \"FAC_ID\",\n" + 
				"            \"A3\".\"FAC_NAME\"   \"FAC_NAME\",\n" + 
				"            \"A3\".\"AVG_PAY\"    \"AVG_PAY\"\n" + 
				"        FROM\n" + 
				"            (\n" + 
				"                SELECT\n" + 
				"                    \"A8\".\"FAC_ID\"     \"FAC_ID\",\n" + 
				"                    \"A8\".\"FAC_NAME\"   \"FAC_NAME\",\n" + 
				"                    AVG(\"A8\".\"PAY_RATE\" * 1920) \"AVG_PAY\"\n" + 
				"                FROM\n" + 
				"                    (\n" + 
				"                        SELECT\n" + 
				"                            \"A9\".\"FAC_ID\"      \"FAC_ID\",\n" + 
				"                            \"A10\".\"PAY_RATE\"   \"PAY_RATE\",\n" + 
				"                            \"A10\".\"PAY_TYPE\"   \"PAY_TYPE\",\n" + 
				"                            \"A9\".\"FAC_NAME\"    \"FAC_NAME\"\n" + 
				"                        FROM\n" + 
				"                            \"JSLE1\".\"JOB\"       \"A10\",\n" + 
				"                            \"JSLE1\".\"FACTORY\"   \"A9\"\n" + 
				"                        WHERE\n" + 
				"                            \"A10\".\"FAC_ID\" = \"A9\".\"FAC_ID\"\n" + 
				"                    ) \"A8\"\n" + 
				"                WHERE\n" + 
				"                    \"A8\".\"PAY_TYPE\" = 'wage'\n" + 
				"                GROUP BY\n" + 
				"                    \"A8\".\"FAC_ID\",\n" + 
				"                    \"A8\".\"FAC_NAME\"\n" + 
				"            ) \"A3\"\n" + 
				"        )\n" + 
				"    ) \"A1\"\n" + 
				"GROUP BY\n" + 
				"    \"A1\".\"FAC_ID\",\n" + 
				"    \"A1\".\"FAC_NAME\",\n" + 
				"    \"A1\".\"AVG_PAY\"\n" + 
				"ORDER BY\n" + 
				"    \"A1\".\"AVG_PAY\" DESC";
		ArrayList<String[]> al = new ArrayList<String[]>();
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(str);
		while (rs.next()) {
			String[] line = new String[3];
			line[0] = rs.getString("fac_id");
			line[1] = rs.getString("fac_name");
			line[2] = Float.toString(rs.getFloat("avg_pay"));
			al.add(line);
		}
		return al;
	}

	/**
	 * Query 4 setup
	 * 
	 * @param pos_code
	 * @return
	 * @throws SQLException
	 */
	public ArrayList<String[]> queryFour(String pos_code) throws SQLException {
		String str = "SELECT pos_code, sk_code " + "FROM Requires " + "WHERE pos_code = ? "
				+ "GROUP BY pos_code, sk_code ";
		ArrayList<String[]> al = new ArrayList<String[]>();
		PreparedStatement pStmt = conn.prepareStatement(str);
		pStmt.setString(1, pos_code);
		ResultSet rs = pStmt.executeQuery();
		while (rs.next()) {
			String[] line = new String[2];
			line[0] = rs.getString("pos_code");
			line[1] = rs.getString("sk_code");
			al.add(line);
		}
		return al;
	}
	
	/**
	 * Query 5 setup
	 * @param per_id
	 * @return
	 * @throws SQLException
	 */
	public ArrayList<String[]> queryFive(String per_id) throws SQLException {
		String str = "SELECT title, sk_code " + 
				"FROM Has_skill NATURAL JOIN Skill " + 
				"WHERE per_id = ?";
		ArrayList<String[]> al = new ArrayList<String[]>();
		PreparedStatement pStmt = conn.prepareStatement(str);
		pStmt.setString(1, per_id);
		ResultSet rs = pStmt.executeQuery();
		while (rs.next()) {
			String[] line = new String[2];
			line[0] = rs.getString("title");
			line[1] = rs.getString("sk_code");
			al.add(line);
		}
		return al;
	}
	
	/**
	 * Query 6 setup
	 * @param pos_code
	 * @param per_id
	 * @return
	 * @throws SQLException
	 */
	public ArrayList<String[]> querySix(String pos_code, String per_id) throws SQLException {
		String str = "( SELECT\n" + 
				"    \"A3\".\"SK_CODE\"   \"SK_CODE\",\n" + 
				"    \"A3\".\"TITLE\"     \"TITLE\"\n" + 
				"FROM\n" + 
				"    (\n" + 
				"        SELECT\n" + 
				"            \"A4\".\"SK_CODE\"    \"SK_CODE\",\n" + 
				"            \"A5\".\"TITLE\"      \"TITLE\",\n" + 
				"            \"A4\".\"POS_CODE\"   \"POS_CODE\"\n" + 
				"        FROM\n" + 
				"            \"JSLE1\".\"SKILL\"      \"A5\",\n" + 
				"            \"JSLE1\".\"REQUIRES\"   \"A4\"\n" + 
				"        WHERE\n" + 
				"            \"A5\".\"SK_CODE\" = \"A4\".\"SK_CODE\"\n" + 
				"    ) \"A3\"\n" + 
				"WHERE\n" + 
				"    \"A3\".\"POS_CODE\" = ?\n" + 
				")\n" + 
				"MINUS\n" + 
				"( SELECT\n" + 
				"    \"A2\".\"SK_CODE\"   \"SK_CODE\",\n" + 
				"    \"A2\".\"TITLE\"     \"TITLE\"\n" + 
				"FROM\n" + 
				"    (\n" + 
				"        SELECT\n" + 
				"            \"A6\".\"SK_CODE\"   \"SK_CODE\",\n" + 
				"            \"A7\".\"PER_ID\"    \"PER_ID\",\n" + 
				"            \"A6\".\"TITLE\"     \"TITLE\"\n" + 
				"        FROM\n" + 
				"            \"JSLE1\".\"HAS_SKILL\"   \"A7\",\n" + 
				"            \"JSLE1\".\"SKILL\"       \"A6\"\n" + 
				"        WHERE\n" + 
				"            \"A7\".\"SK_CODE\" = \"A6\".\"SK_CODE\"\n" + 
				"    ) \"A2\"\n" + 
				"WHERE\n" + 
				"    \"A2\".\"PER_ID\" = ?\n" + 
				")" ;
		ArrayList<String[]> al = new ArrayList<String[]>();
		PreparedStatement pStmt = conn.prepareStatement(str);
		pStmt.setString(1, pos_code);
		pStmt.setString(2, per_id);
		ResultSet rs = pStmt.executeQuery();
		while (rs.next()) {
			String[] line = new String[2];
			line[0] = rs.getString("sk_code");
			line[1] = rs.getString("title");
			al.add(line);
		}
		return al;
	}
	
	/**
	 * Query 10 setup
	 * @return
	 * @throws SQLException
	 */
	public ArrayList<String[]> queryTen() throws SQLException {
		String str = "SELECT cus_id, SUM(sale_amount)\n" + 
				"FROM contract \n" + 
				"WHERE con_date BETWEEN '01-Jan-2018' AND '31-Dec-2018'\n" + 
				"GROUP BY cus_ID, sale_amount \n";
		ArrayList<String[]> al = new ArrayList<String[]>();
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(str);
		while (rs.next()) {
			String[] line = new String[2];
			line[0] = rs.getString("cus_id");
			line[1] = Float.toString(rs.getFloat("SUM(sale_amount)"));
			al.add(line);
		}
		return al;
	}
	
	/**
	 * Query 11 setup
	 * @return
	 * @throws SQLException
	 */
	public ArrayList<String[]> queryEleven() throws SQLException {
		String str = "SELECT DISTINCT\n" + 
				"    \"A1\".\"M_CODE\"     \"M_CODE\",\n" + 
				"    \"A1\".\"M_NAME\"     \"M_NAME\",\n" + 
				"    \"A1\".\"QUANTITY\"   \"QUANTITY\"\n" + 
				"FROM\n" + 
				"    (\n" + 
				"        SELECT\n" + 
				"            \"from$_subquery$_005\".\"M_CODE\"     \"M_CODE\",\n" + 
				"            \"from$_subquery$_005\".\"M_NAME\"     \"M_NAME\",\n" + 
				"            \"from$_subquery$_005\".\"QUANTITY\"   \"QUANTITY\",\n" + 
				"            \"from$_subquery$_005\".\"PAY_DATE\"   \"PAY_DATE\"\n" + 
				"        FROM\n" + 
				"            (\n" + 
				"                SELECT\n" + 
				"                    \"PURCHASELINE\".\"M_CODE\"            \"M_CODE\",\n" + 
				"                    \"PURCHASELINE\".\"QUANTITY\"          \"QUANTITY\",\n" + 
				"                    \"PURCHASELINE\".\"PURCHASE_NUM\"      \"PURCHASE_NUM\",\n" + 
				"                    \"from$_subquery$_003\".\"M_NAME\"     \"M_NAME\",\n" + 
				"                    \"from$_subquery$_003\".\"PAY_DATE\"   \"PAY_DATE\"\n" + 
				"                FROM\n" + 
				"                    (\n" + 
				"                        SELECT\n" + 
				"                            \"MATERIAL\".\"M_CODE\"         \"M_CODE\",\n" + 
				"                            \"MATERIAL\".\"M_NAME\"         \"M_NAME\",\n" + 
				"                            \"MATERIAL\".\"QUANTITY\"       \"QUANTITY\",\n" + 
				"                            \"PURCHASE\".\"PURCHASE_NUM\"   \"PURCHASE_NUM\",\n" + 
				"                            \"PURCHASE\".\"PAY_DATE\"       \"PAY_DATE\"\n" + 
				"                        FROM\n" + 
				"                            \"JSLE1\".\"MATERIAL\"   \"MATERIAL\",\n" + 
				"                            \"JSLE1\".\"PURCHASE\"   \"PURCHASE\"\n" + 
				"                    ) \"from$_subquery$_003\",\n" + 
				"                    \"JSLE1\".\"PURCHASELINE\"                                                                                                                                                                                                                 \"PURCHASELINE\"\n" + 
				"                WHERE\n" + 
				"                    \"from$_subquery$_003\".\"PURCHASE_NUM\" = \"PURCHASELINE\".\"PURCHASE_NUM\"\n" + 
				"                    AND \"from$_subquery$_003\".\"QUANTITY\" = \"PURCHASELINE\".\"QUANTITY\"\n" + 
				"                    AND \"from$_subquery$_003\".\"M_CODE\" = \"PURCHASELINE\".\"M_CODE\"\n" + 
				"            ) \"from$_subquery$_005\"\n" + 
				"        WHERE\n" + 
				"            \"from$_subquery$_005\".\"PAY_DATE\" >= '1-Oct-2018'\n" + 
				"            AND \"from$_subquery$_005\".\"PAY_DATE\" <= '31-Dec-2018'\n" + 
				"    ) \"A1\"\n" + 
				"WHERE\n" + 
				"    \"A1\".\"QUANTITY\" = (\n" + 
				"        SELECT\n" + 
				"            \"A2\".\"MAXQUANTITY\" \"MAXQUANTITY\"\n" + 
				"        FROM\n" + 
				"            (\n" + 
				"                SELECT\n" + 
				"                    MAX(\"A3\".\"QUANTITY\") \"MAXQUANTITY\"\n" + 
				"                FROM\n" + 
				"                    (\n" + 
				"                        SELECT\n" + 
				"                            \"from$_subquery$_005\".\"M_CODE\"     \"M_CODE\",\n" + 
				"                            \"from$_subquery$_005\".\"M_NAME\"     \"M_NAME\",\n" + 
				"                            \"from$_subquery$_005\".\"QUANTITY\"   \"QUANTITY\",\n" + 
				"                            \"from$_subquery$_005\".\"PAY_DATE\"   \"PAY_DATE\"\n" + 
				"                        FROM\n" + 
				"                            (\n" + 
				"                                SELECT\n" + 
				"                                    \"PURCHASELINE\".\"M_CODE\"            \"M_CODE\",\n" + 
				"                                    \"PURCHASELINE\".\"QUANTITY\"          \"QUANTITY\",\n" + 
				"                                    \"PURCHASELINE\".\"PURCHASE_NUM\"      \"PURCHASE_NUM\",\n" + 
				"                                    \"from$_subquery$_003\".\"M_NAME\"     \"M_NAME\",\n" + 
				"                                    \"from$_subquery$_003\".\"PAY_DATE\"   \"PAY_DATE\"\n" + 
				"                                FROM\n" + 
				"                                    (\n" + 
				"                                        SELECT\n" + 
				"                                            \"MATERIAL\".\"M_CODE\"         \"M_CODE\",\n" + 
				"                                            \"MATERIAL\".\"M_NAME\"         \"M_NAME\",\n" + 
				"                                            \"MATERIAL\".\"QUANTITY\"       \"QUANTITY\",\n" + 
				"                                            \"PURCHASE\".\"PURCHASE_NUM\"   \"PURCHASE_NUM\",\n" + 
				"                                            \"PURCHASE\".\"PAY_DATE\"       \"PAY_DATE\"\n" + 
				"                                        FROM\n" + 
				"                                            \"JSLE1\".\"MATERIAL\"   \"MATERIAL\",\n" + 
				"                                            \"JSLE1\".\"PURCHASE\"   \"PURCHASE\"\n" + 
				"                                    ) \"from$_subquery$_003\",\n" + 
				"                                    \"JSLE1\".\"PURCHASELINE\"                                                                                                                                                                                                                 \"PURCHASELINE\"\n" + 
				"                                WHERE\n" + 
				"                                    \"from$_subquery$_003\".\"PURCHASE_NUM\" = \"PURCHASELINE\".\"PURCHASE_NUM\"\n" + 
				"                                    AND \"from$_subquery$_003\".\"QUANTITY\" = \"PURCHASELINE\".\"QUANTITY\"\n" + 
				"                                    AND \"from$_subquery$_003\".\"M_CODE\" = \"PURCHASELINE\".\"M_CODE\"\n" + 
				"                            ) \"from$_subquery$_005\"\n" + 
				"                        WHERE\n" + 
				"                            \"from$_subquery$_005\".\"PAY_DATE\" >= '1-Oct-2018'\n" + 
				"                            AND \"from$_subquery$_005\".\"PAY_DATE\" <= '31-Dec-2018'\n" + 
				"                    ) \"A3\"\n" + 
				"            ) \"A2\"\n" + 
				"    )";
		ArrayList<String[]> al = new ArrayList<String[]>();
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(str);
		while (rs.next()) {
			String[] line = new String[3];
			line[0] = rs.getString("m_code");
			line[1] = rs.getString("m_name");
			line[2] = Integer.toString(rs.getInt("quantity"));
			al.add(line);
		}
		return al;
	}
	
	public ArrayList<String[]> queryTwelve() throws SQLException {
		String str = "SELECT\n" + 
				"    \"A1\".\"FAC_ID\"     \"FAC_ID\",\n" + 
				"    \"A1\".\"FAC_NAME\"   \"FAC_NAME\",\n" + 
				"    MAX(\"A1\".\"QUANTITY\") \"MAX(QUANTITY)\"\n" + 
				"FROM\n" + 
				"    (\n" + 
				"        SELECT\n" + 
				"            \"A2\".\"FAC_ID\"     \"FAC_ID\",\n" + 
				"            \"A3\".\"P_CODE\"     \"P_CODE\",\n" + 
				"            \"A3\".\"QUANTITY\"   \"QUANTITY\",\n" + 
				"            \"A2\".\"FAC_NAME\"   \"FAC_NAME\"\n" + 
				"        FROM\n" + 
				"            \"JSLE1\".\"MAKES\"     \"A3\",\n" + 
				"            \"JSLE1\".\"FACTORY\"   \"A2\"\n" + 
				"        WHERE\n" + 
				"            \"A3\".\"FAC_ID\" = \"A2\".\"FAC_ID\"\n" + 
				"    ) \"A1\"\n" + 
				"GROUP BY\n" + 
				"    \"A1\".\"FAC_ID\",\n" + 
				"    \"A1\".\"FAC_NAME\",\n" + 
				"    \"A1\".\"QUANTITY\"\n" + 
				"";
		ArrayList<String[]> al = new ArrayList<String[]>();
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(str);
		while (rs.next()) {
			String[] line = new String[3];
			line[0] = rs.getString("fac_id");
			line[1] = rs.getString("fac_name");
			line[2] = Integer.toString(rs.getInt("MAX(quantity)"));
			al.add(line);
		}
		return al;
	}

	/**
	 * tester Run Query here!!!
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
		while (!quit) {
			System.out.println("\n\n*****JJT GV Database Java Query Runner*****\n\n");
			System.out.println("Please enter a query number (1-12) or 0 to QUIT: ");
			// give user choice option
			try {
				int choice = scanner.nextInt();

				if (choice > 0 && choice > 13) {
					System.out.println("ERROR>>>>> You have entered the value that is not for GV queries!");
				} else if (choice == 1) {
					System.out.println("Running Query 1");
					System.out.println("List the workers by name in the alphabetical order of last names");
					System.out.println("first_name\t\tlast_name");
					ArrayList<String[]> str = sqObj.queryOne();
					for (String[] line : str) {
						System.out.printf("%s\t\t%s\n\n", line[0], line[1]);
					}
				} else if (choice == 2) {
					System.out.println("Running Query 2");
					System.out.println("List the staff (salary workers) by salary in descending order");
					System.out.println("frsit_name\t\tlast_name\t\tpay_rate");
					ArrayList<String[]> str = sqObj.queryTwo();
					for (String[] line : str) {
						System.out.printf("%s\t\t%s\t\t%s\n\n", line[0], line[1], line[2]);
					}
				} else if (choice == 3) {
					System.out.println("Running Query 3");
					System.out.println(
							"List the average annual pay (the salary or wage rates multiplying by 1920 hours) of each factory in descending order");
					System.out.println("fac_id\t\tfac_name\t\t\tavg_pay");
					ArrayList<String[]> str = sqObj.queryThree();
					for (String[] line : str) {
						System.out.printf("%s\t\t%s\t\t\t%s\n\n", line[0], line[1], line[2]);
					}
				} else if (choice == 4) {
					System.out.println("Running Query 4");
					System.out.println("List the required skills of a given pos_code in a readable format.");
					System.out.println("Enter a position code: ");
					String ans = getAnswerString();
					System.out.println("pos_code\t\tsk_code");
					ArrayList<String[]> str = sqObj.queryFour(ans);
					for (String[] line : str) {
						System.out.printf("%s\t\t\t%s\t\t", line[0], line[1]);
					}
				} else if (choice == 5) {
					System.out.println("Running Query 5");
					System.out.println("Given a person’s identifier, list this person’s skills in a readable format");
					System.out.println("Enter a person ID: ");
					String ans = getAnswerString();
					System.out.println("title\t\tsk_code");
					ArrayList<String[]> str = sqObj.queryFive(ans);
					for (String[] line : str) {
						System.out.printf("%s\t%s\n\n",line[0], line[1]);
					}
				} else if (choice == 6) {
					System.out.println("Running Query 6");
					System.out.println("Given a person’s identifier, list a person’s missing skills for a specific pos_code in a readable format.");
					System.out.println("Enter a position code: ");
					String ans1 = getAnswerString();
					System.out.println("Enter a persion ID: ");
					String ans2 = getAnswerString();
					System.out.println("sk_code\t\ttitle");
					ArrayList<String[]> str = sqObj.querySix(ans1, ans2);
					for (String[] line : str) {
						System.out.printf("%s\t\t%s\n\n", line[0], line[1]);
					}
				} else if (choice == 7) {
					System.out.println("This query is for AZ, select another.");
				} else if (choice == 8) {
					System.out.println("This query is for AZ, select another.");
				} else if (choice == 9) {
					System.out.println("This query is for AZ, select another.");
				} else if (choice == 10) {
					System.out.println("Running Query 10");
					System.out.println("List the total sales in dollar to each customer of GV in 2018.");
					System.out.println("cus_id\t\tSUM(sale_amount)");
					ArrayList<String[]> str = sqObj.queryTen();
					for (String[] line : str) {
						System.out.printf("%s\t\t%s\n\n", line[0],line[1]);
					}
				} else if (choice == 11) {
					System.out.println("Running Query 11");
					System.out.println("Show m_code, m_name of the material(s) that GV purchased the most (measured by quantity) in the fourth quarter of 2018.");
					System.out.println("m_code\t\tm_name\t\tquantity");
					ArrayList<String[]> str = sqObj.queryEleven();
					for (String[] line : str) {
						System.out.printf("%s\t\t%s\t\t%s\n\n", line[0],line[1],line[2]);
					}
				} else if (choice == 12) {
					System.out.println("Running Query 12");
					System.out.println("Show the factory name that made the most total quantity of the product that was sold the most in 2018. WHERE product amount was sold the most in 2018, return the factory name that made that product");
					System.out.println("fac_id\t\tfac_name\t\tMAX(quantity)");
					ArrayList<String[]> str = sqObj.queryTwelve();
					for (String[] line : str) {
						System.out.printf("%s\t\t%s\t\t%s\n\n", line[0],line[1],line[2]);
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
