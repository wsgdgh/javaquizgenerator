/**
 * StringConverterTest.java
 * (c) 2007 Ann Marie Steichmann
 */
package quizgen.util;

import junit.framework.TestCase;

/**
 * <code>StringConverterTest</code> tests the functionality of
 * <code>StringConverter</code>
 * @author Ann Marie Steichmann
 *
 */
public class StringConverterTest extends TestCase {

	public void testEscapeDB() {
		
		String text = "\\this\\ isn't a \"test\".\n";
		System.out.println("text: " + text);
		String expected = "\\\\this\\\\ isn\\'t a \\\"test\\\".\\n";
		System.out.println("expected: " + expected);
		assertEquals( expected, StringConverter.escape( text ) );
	}
}
