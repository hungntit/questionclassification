import junit.framework.TestCase;


public class ClassifierTest extends TestCase {

	public void testCheckPatternQuestion() {
		Classifier.loadPattern();
		String actual = Classifier.checkPatternQuestion("What soft drink is the most  ");		
		System.out.println(actual);
		String expected = "What\\sis\\s([a-z]+)";
		assertEquals(expected, actual);
	}

}
