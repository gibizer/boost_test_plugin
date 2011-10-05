package boost_unit_test_plugin;

public interface TestSuiteListener {

	public void testSuiteStarted(String name);
	public void testSuiteFinished(String name);
	public void testCaseStarted(String name);
	public void testCasePassed(String name);
	public void testCaseFailed(String name, String message);
}
