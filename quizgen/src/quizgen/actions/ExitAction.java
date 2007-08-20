/**
 * ExitAction.java
 * (c) 2007 Ann Marie Steichmann
 */
package quizgen.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.Action;

/**
 * <code>ExitAction</code> action for exiting the application.
 * @author Ann Marie Steichmann
 *
 */
public class ExitAction extends UIActionAdapter {

	/* (non-Javadoc)
	 * @see quizgen.ui.QuizGeneratorUI.UIActionAdapter#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		System.exit(0);
	}

	/* (non-Javadoc)
	 * @see quizgen.ui.QuizGeneratorUI.UIActionAdapter#getValue(java.lang.String)
	 */
	@Override
	public Object getValue(String key) {
		
		if ( key.equals( Action.NAME ) ) {
			return "Exit";
		} else if ( key.equals( Action.SHORT_DESCRIPTION) ) {
			return "Exit the application";
		} else if ( key.equals( Action.MNEMONIC_KEY ) ) {
			return new Integer( KeyEvent.VK_X );
		}
		return null;
	}
}
