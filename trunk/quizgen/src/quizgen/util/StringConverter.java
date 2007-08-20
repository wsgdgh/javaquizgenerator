/**
 * StringConverter.java
 * (c) 2007 Ann Marie Steichmann
 */
package quizgen.util;

/**
 * <code>StringConverter</code> utility for manipulating text
 * @author Ann Marie Steichmann
 *
 */
public class StringConverter {

	/**
	 * Escape the given text so that it can be used in an insert
	 * statement
	 * @param text The original text
	 * @return text that has been properly escaped for the
	 * insert statement
	 */
	public static String escape( String text ) {
		
		String retval = text.replace( "\\", "\\\\" );
		retval = retval.replace( "\0", "\\0" );
		retval = retval.replace( "'", "\\'" );
		retval = retval.replace( "\"", "\\\"" );
		retval = retval.replace( "\b", "\\b" );
		retval = retval.replace( "\n", "\\n" );
		retval = retval.replace( "\r", "\\r" );
		retval = retval.replace( "\t", "\\t" );
		return retval;
	}
}
