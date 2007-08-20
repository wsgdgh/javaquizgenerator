/**
 * QuizUpdaterTest.java
 * (c) 2007 Ann Marie Steichmann
 */
package quizgen;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

/**
 * <code>QuizUpdaterTest</code> TODO: What does this do???
 * @author Ann Marie Steichmann
 *
 */
public class QuizUpdaterTest extends TestCase {

	private TestDBConnection testConn;
	private QuizUpdater updater;
	
	class TestDBConnection extends QuizDBConnection {

		private Statement stmt;
		
		public String text;
		
		protected TestDBConnection(String dbName) {
			super(dbName);
		}

		@Override
		protected void initStatements() {
			
			try {
				if ( conn != null && !conn.isClosed() ) {
					stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
					        ResultSet.CONCUR_READ_ONLY);
					System.out.println( "Successfully created statements" );
				}
			} catch (SQLException e) {
				System.out.println( "Unable to create statements: " + e );
				e.printStackTrace();
				freeResources();
			}		
		}
		
		private ResultSet getChoiceResults() throws Exception {
			
			String sql = "select * from choice where qid='Q11_1'";
			ResultSet results = stmt.executeQuery( sql );
			return results;
		}
		
		private ResultSet getQuestionResults() throws Exception {
			
			String sql = "select * from question where id='Q11_1'";
			ResultSet results = stmt.executeQuery( sql );
			return results;
		}
		
		public void assertValueEquals( String text ) throws Exception {
			
			ResultSet results = getQuestionResults();
			results.next();
			String value = results.getString( "value" );
			assertEquals( text, value );
		}
		
		public void assertQuestionExists() throws Exception {
			
			ResultSet results = getQuestionResults();
			assertTrue( results.next() );
			results = getChoiceResults();
			assertTrue( results.next() );
			assertTrue( results.next() );
			assertTrue( results.next() );
			assertTrue( results.next() );
			assertFalse( results.next() );
		}
		
		public void assertQuestionDoesNotExist() throws Exception {
			
			ResultSet results = getQuestionResults();
			assertFalse( results.next() );
			results = getChoiceResults();
			assertFalse( results.next() );
		}
	}
	
	public void testAddingAndDeletingQuestion() throws Exception {
		
		testConn.assertQuestionDoesNotExist();
		
		updater.add( buildQuestion() );
		testConn.assertQuestionExists();
		testConn.assertValueEquals( "this isn't a \"test_run\"" );
		
		updater.delete( "Q11_1" );
		testConn.assertQuestionDoesNotExist();
	}
	
	public void testLoadingQuestion() throws Exception {
		
		Question question1 = buildQuestion();
		updater.add( question1 );
		Question question2 = updater.load( "Q11_1" );
		assertEquals( question1.getId(), question2.getId() );
		assertEquals( question1.getText(), question2.getText() );
		assertEquals( question1.getChoices().size(), question2.getChoices().size() );
		updater.delete( "Q11_1" );
	}
	
	private Question buildQuestion() {

		List<Choice> choices = new ArrayList<Choice>();
		choices.add( new Choice( 'a', "test 1", false ) );
		choices.add( new Choice( 'b', "test 2", true ) );
		choices.add( new Choice( 'c', "test 3", false ) );
		choices.add( new Choice( 'd', "test 4", false ) );
		return new Question( "Q11_1", "this isn't a \"test_run\"", choices );
	}
	
	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		
		super.setUp();
		
		testConn = new TestDBConnection("quizgen");
		updater = new QuizUpdater("quizgen");
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {

		testConn.freeResources();
		updater.freeResources();
		
		super.tearDown();
	}
}
