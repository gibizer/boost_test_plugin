package boost_unit_test_plugin;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OutputParser {
	
	private TestSuiteListener listener;
	private static final String NAME = "\"([^\"]+)\"";
	private static final Pattern ENTER_SUITE = Pattern.compile("Entering test suite " + NAME + ".*"); 
	private static final Pattern EXIT_SUITE = Pattern.compile("Leaving test suite " + NAME + ".*"); 
	private static final Pattern ENTER_CASE = Pattern.compile("Entering test case " + NAME + ".*"); 
	private static final Pattern EXIT_CASE = Pattern.compile("Leaving test case " + NAME + ".*"); 
	private static final Pattern ERROR = Pattern.compile("([^:]+):([^:]+):?(.*)");
	private boolean failed = false;
	private String errorMessage = "";
	
	public void addTestSuiteListener(TestSuiteListener listener){
		this.listener = listener;
	}
	
	public void parse(String output){
		if (listener == null){
			//nobody listens pointless to parse
			return;
		}
		failed = false;
		errorMessage = "";
		for(String line : output.split("\n")){			
			parseLine(line);
		}
	}
	
	public void parseLine(String line){
		parseEnterSuite(line);
		parseExitSuite(line);
		parseEnterCase(line);
		parseExitCase(line);
		parseError(line);
	}
	
	private boolean parseError(String toBeParsed){
		Matcher matcher = ERROR.matcher(toBeParsed);
		if (matcher.matches()){
			failed = true;
			errorMessage = errorMessage + toBeParsed + "\n";
			return true;
		}
		return false;
	}
	
	private boolean parseEnterSuite(String toBeParsed){
		Matcher matcher = ENTER_SUITE.matcher(toBeParsed);
		if (matcher.matches()){
			onEnterSuite(matcher.group(1));
			return true;
		}		
		return false;
	}
	
	private void parseEnterCase(String toBeParsed){
		Matcher matcher = ENTER_CASE.matcher(toBeParsed);
		if (matcher.matches()){
			onEnterCase(matcher.group(1));
		}		
	}
	
	private void parseExitSuite(String toBeParsed){
		Matcher matcher = EXIT_SUITE.matcher(toBeParsed);
		if (matcher.matches()){
			onExitSuite(matcher.group(1));
		}
	}
	
	private void parseExitCase(String toBeParsed){
		Matcher matcher = EXIT_CASE.matcher(toBeParsed);
		if (matcher.matches()){
			onExitCase(matcher.group(1));
		}
	}
	
	private void onEnterSuite(String suiteName){
		listener.testSuiteStarted(suiteName);
	}

	private void onExitSuite(String suiteName){
		listener.testSuiteFinished(suiteName);
	}

	private void onEnterCase(String caseName){
		listener.testCaseStarted(caseName);
	}

	private void onExitCase(String caseName){
		if (failed){
			listener.testCaseFailed(caseName, errorMessage);
		}else{
			listener.testCasePassed(caseName);			
		}
		failed = false;
		errorMessage = "";
	}
}
