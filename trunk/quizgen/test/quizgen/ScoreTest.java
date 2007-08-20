/**
 * ScoreTest.java
 * (c) 2007 Ann Marie Steichmann
 */
package quizgen;

import junit.framework.TestCase;

/**
 * <code>ScoreTest</code> tests the functionality of <code>Score</code>
 * @author Ann Marie Steichmann
 *
 */
public class ScoreTest extends TestCase {
	
	public void testScore() {
		
		Score score = new Score( 20 );
		score.correct();
		assertEquals( "1/0/20", score.toString() );
		
		score.correct();
		assertEquals( "2/0/20", score.toString() );
		
		score.incorrect();
		assertEquals( "2/1/20", score.toString() );
	}

}
