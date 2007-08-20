/**
 * Question.java
 * (c) 2007 Ann Marie Steichmann
 */
package quizgen;

import java.util.List;

/**
 * <code>Question</code> contains the text and choices for a single
 * question.
 * 
 * @author Ann Marie Steichmann
 *
 */
public class Question {

	private String id;
	private String text;
	private List<Choice> choices;
	
	/**
	 * Constructor for Question
	 * @param id The question id
	 * @param text The question text
	 * @param choices The available answer choices
	 */
	public Question( String id, String text, List<Choice> choices ) {
		
		this.id = id;
		this.text = text;
		this.choices = choices;
	}
	
	/**
	 * @return the question id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @return the question text
	 */
	public String getText() {
		return text;
	}

	/**
	 * @return the available answer choices
	 */
	public List<Choice> getChoices() {
		return choices;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		
		StringBuffer retVal = new StringBuffer();
		retVal.append( id + ".\t" + text + "\n" );
		for ( Choice choice : choices ) {
			retVal.append( choice + "\n" );
		}
		return retVal.toString();
	}
}
