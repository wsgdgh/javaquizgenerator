/**
 * QuizUpdater.java
 * (c) 2007 Ann Marie Steichmann
 */
package quizgen;

import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import quizgen.util.StringConverter;

/**
 * <code>QuizUpdater</code> updates a given database.
 * @author Ann Marie Steichmann
 *
 */
public class QuizUpdater extends QuizDBConnection {

	private Statement stmt;
	
	/**
	 * Constructor for QuizUpdater
	 * @param dbName The name of the database to connect to
	 */
	public QuizUpdater( String dbName ) {
		super(dbName);
	}

	@Override
	protected void initStatements() {
		
		try {
			if ( conn != null && !conn.isClosed() ) {
				stmt = conn.createStatement();
				System.out.println( "Successfully created statements" );
			}
		} catch (SQLException e) {
			System.out.println( "Unable to create statements: " + e );
			e.printStackTrace();
			freeResources();
		}		
	}
	
	/* (non-Javadoc)
	 * @see quizgen.QuizDBConnection#freeResources()
	 */
	@Override
	public void freeResources() {

		super.freeResources();
		stmt = null;
	}

	/**
	 * Add the given question to the quiz database
	 * @param question The question to add to the quiz database
	 * @throws SQLException 
	 */
	public void add(Question question) throws SQLException {
		
		String sql = "insert into question values ('" +
			question.getId() + "','" + 
			StringConverter.escape( question.getText() ) +
			"')";
		stmt.executeUpdate( sql );
		
		for ( Choice choice : question.getChoices() ) {
			sql = "insert into choice values ('" +
			question.getId() + "','" +
			choice.getLetter() + "','" + 
			StringConverter.escape( choice.getText() ) + "'," +
			choice.isCorrect() + ")";
			stmt.execute( sql );
		}
	}

	/**
	 * Delete the given question from the quiz database
	 * @param id The id of the question to delete from the quiz database
	 * @throws SQLException 
	 */
	public void delete(String id) throws SQLException {
		
		String sql = "delete from choice where qid='" +
			id + "'";
		stmt.execute( sql );
		
		sql = "delete from question where id='" + 
			id + "'";
		stmt.execute( sql );
	}
	
	/**
	 * Load the given question
	 * @param id The id of the question to load
	 * @return A
	 * @throws SQLException
	 * @throws IOException
	 */
	public Question load(String id) throws SQLException, IOException {
		
		String questionSql = "select * from question where id='" + id + "'";
		ResultSet results = stmt.executeQuery( questionSql );
		if ( results.next() ) {
			String value = results.getString( "value" );
			String choiceSql = "select * from choice where qid='" + id + "'";
			results = stmt.executeQuery( choiceSql );
			List<Choice> choices = new ArrayList<Choice>();
			while ( results.next() ) {
				InputStream letterStream = results.getAsciiStream("letter");
				char letter = (char)letterStream.read();
				String cValue = results.getString("value");
				boolean correct = results.getBoolean("correct");
				choices.add( new Choice( letter, cValue, correct ) );
			}
			return new Question( id, value, choices );
		}
		return null;
	}
}
