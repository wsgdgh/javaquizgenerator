/**
 * Score.java
 * (c) 2007 Ann Marie Steichmann
 */
package quizgen;

/**
 * <code>Score</code> tracks the score.
 * @author Ann Marie Steichmann
 *
 */
public class Score {

	private int total;
	private int numCorrect;
	private int numIncorrect;
	
	/**
	 * Constructor for Score
	 * @param total The total possible points
	 */
	public Score( int total ) {
		
		this.total = total;
		numCorrect = 0;
		numIncorrect = 0;
	}
	
	/**
	 * Increment correct answers
	 */
	public void correct() {
		numCorrect++;
	}
	
	/**
	 * Increment incorrect answers
	 */
	public void incorrect() {
		numIncorrect++;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return numCorrect + "/" + numIncorrect + "/" + total;
	}
}
