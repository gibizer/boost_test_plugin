package boost_unit_test_plugin;

public interface TestExecutionListener {
	
	public void executionStarted();
	public void executionFinished(String output);
}
