/**
 * QuizGeneratorTest.java
 * (c) 2007 Ann Marie Steichmann
 */
package quizgen;

import java.util.List;

import junit.framework.TestCase;

/**
 * <code>QuizGeneratorTest</code>
 * 
 * @author Ann Marie Steichmann
 *
 */
public class QuizGeneratorTest extends TestCase {

	private QuizGenerator generator;
	
	public void testListingQuestions() {
		
		List<Question> questions = generator.listAll();
		assertTrue( questions.size() > 0 );
		
		StringBuffer expected = new StringBuffer();
		expected.append( "Q1_2.\tDescartes suggested that the mind and body interact through the flow of fluid from the mind to the nerves.  The idea that all reality can be divided into the physical or mental (body or mind) is called\n" );
		expected.append( "\ta.\tdualism [X]\n" );
		expected.append( "\tb.\tmonism []\n" );
		expected.append( "\tc.\tmaterialism []\n" );
		expected.append( "\td.\tempiricism []\n" );
		
		assertEquals( expected.toString(), questions.get(0).toString() );
	}
	
	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		
		super.setUp();
		
		generator = new QuizGenerator( "quizgen" ); 
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	@Override
	protected void tearDown() throws Exception {
		
		generator.freeResources();
		
		super.tearDown();
	}
}
