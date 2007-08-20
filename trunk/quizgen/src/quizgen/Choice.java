/**
 * Choice.java
 * (c) 2007 Ann Marie Steichmann
 */
package quizgen;

/**
 * <code>Choice</code> contains the letter, text, and validity for a
 * single choice to a question.
 * 
 * @author Ann Marie Steichmann
 *
 */
public class Choice {
	
	private char letter;
	private String text;
	private boolean correct;
	
	/**
	 * Constructor for Choice
	 * @param letter The letter of the choice
	 * @param text The text of the choice
	 * @param correct true if the choice is correct
	 */
	public Choice(char letter, String text, boolean correct) {
		this.letter = letter;
		this.text = text;
		this.correct = correct;
	}

	/**
	 * @return the letter of the choice
	 */
	public char getLetter() {
		return letter;
	}

	/**
	 * @return the text of the choice
	 */
	public String getText() {
		return text;
	}

	/**
	 * @return true if this choice is correct
	 */
	public boolean isCorrect() {
		return correct;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		
		return "\t" + letter + ".\t" + text + " [" +
		        (correct?"X":"") + "]";
	}
}
