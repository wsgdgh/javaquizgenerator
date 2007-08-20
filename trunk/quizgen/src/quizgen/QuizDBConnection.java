/**
 * QuizDBConnection.java
 * (c) 2007 Ann Marie Steichmann
 */
package quizgen;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * <code>QuizDBConnection</code> base class containing methods useful
 * for interacting with the database.
 * 
 * @author Ann Marie Steichmann
 *
 */
public abstract class QuizDBConnection {

	protected String dbName;
	protected Connection conn;
	
	class JDBCShutdownHook extends Thread {

		/* (non-Javadoc)
		 * @see java.lang.Thread#run()
		 */
		@Override
		public void run() {
			freeResources();
		}
	}
	
	protected QuizDBConnection( String dbName ) {
		
		this.dbName = dbName;
		initConnection();
		initStatements();
		initShutdownHook();
	}
	
	protected void initConnection() {
		
		try {
			Class.forName( "com.mysql.jdbc.Driver" );
			String url =
	            "jdbc:mysql:///" + dbName;
			conn =
                DriverManager.getConnection( url, "root", "admin");
			System.out.println( "Successfully connected to database: " + conn );
		} catch (ClassNotFoundException e) {
			System.out.println( "Could not load driver: " + e );
			e.printStackTrace();
			freeResources();
		} catch (SQLException e) {
			System.out.println( "Error creating connection: " + e );
			e.printStackTrace();
			freeResources();
		}
	}

	/**
	 * Close the connection and free up any DB resources.
	 * 
	 * Can be overriden.
	 */
	public void freeResources() {
		
		try {
			if (conn != null && !conn.isClosed()) {
				conn.close();
				System.out.println("Successfully closed connection");
			}
		} catch (Exception e) {
			System.out.println("Error freeing resources: " + e);
			e.printStackTrace();
		}
		conn = null;
	}
	
	protected void initShutdownHook() {
		
		Runtime.getRuntime().addShutdownHook( 
							new JDBCShutdownHook() );
	}
	
	protected abstract void initStatements();
}
