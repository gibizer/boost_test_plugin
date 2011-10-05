package boost_unit_test_plugin;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;

public class Test_OutputParser {

	private OutputParser parser;
	private TestSuiteListener listener;

	@Before
	public void setUp() throws Exception {
		parser = new OutputParser();
		listener = mock(TestSuiteListener.class);
		parser.addTestSuiteListener(listener);
	}

	@Test
	public void emptyOutputContainsNotSuitesOrTestCases() {
		parser.parse("");

		verifyZeroInteractions(listener);
	}

	@Test
	public void enteringTestSuiteIsParsed() {
		parser.parse("Entering test suite \"MyTest\"");

		verify(listener).testSuiteStarted("MyTest");
	}

	@Test
	public void leavingTestSuiteIsParsed() {
		parser.parse("Leaving test suite \"MyTest\"");

		verify(listener).testSuiteFinished("MyTest");
	}

	@Test
	public void enteringTestCaseIsParsed() {
		parser.parse("Entering test case \"MyTestCasePass\"");

		verify(listener).testCaseStarted("MyTestCasePass");
	}

	@Test
	public void leavingTestCaseWithoutErrorMakesTheCasePassed() {
		parser.parse("Leaving test case \"MyTestCasePass\"");

		verify(listener).testCasePassed("MyTestCasePass");
	}

	@Test
	public void emptyTestSuite() {
		parser.parse("Entering test suite \"MyTest\"" + "\n"
				+ "Leaving test suite \"MyTest\"");

		verify(listener).testSuiteStarted("MyTest");
		verify(listener).testSuiteFinished("MyTest");
	}

	@Test
	public void multipleTestSuiteWithMultiplePassingTestCases() {
		parser.parse("" 
				+ "Entering test suite \"MyTest\"" + "\n"
				+ "Entering test case \"MyTestCasePass\"" + "\n"
				+ "Leaving test case \"MyTestCasePass\"" + "\n"
				+ "Entering test case \"OtherTestCasePass\"" + "\n"
				+ "Leaving test case \"OtherTestCasePass\"" + "\n"
				+ "Leaving test suite \"MyTest\"" + "\n"
				+ "Entering test suite \"Suite2\"" + "\n"
				+ "Entering test case \"MyTestCasePass\"" + "\n"
				+ "Leaving test case \"MyTestCasePass\"" + "\n"
				+ "Entering test suite \"InnerSuite\"" + "\n"				
				+ "Entering test case \"MyTestCasePass\"" + "\n"
				+ "Leaving test case \"MyTestCasePass\"" + "\n"
				+ "Leaving test suite \"InnerSuite\"" + "\n"				
				+ "Leaving test suite \"Suite2\"" + "\n"
				);
		
		InOrder inOrder = inOrder(listener);
		inOrder.verify(listener).testSuiteStarted("MyTest");
			inOrder.verify(listener).testCaseStarted("MyTestCasePass");
			inOrder.verify(listener).testCasePassed("MyTestCasePass");
			inOrder.verify(listener).testCaseStarted("OtherTestCasePass");
			inOrder.verify(listener).testCasePassed("OtherTestCasePass");
		inOrder.verify(listener).testSuiteFinished("MyTest");
		inOrder.verify(listener).testSuiteStarted("Suite2");
			inOrder.verify(listener).testCaseStarted("MyTestCasePass");
			inOrder.verify(listener).testCasePassed("MyTestCasePass");
			inOrder.verify(listener).testSuiteStarted("InnerSuite");
				inOrder.verify(listener).testCaseStarted("MyTestCasePass");
				inOrder.verify(listener).testCasePassed("MyTestCasePass");
			inOrder.verify(listener).testSuiteFinished("InnerSuite");
		inOrder.verify(listener).testSuiteFinished("Suite2");
	}

	@Test
	public void errorMessageMakesTheTestCaseFail() {
		parser.parse("" 
				+ "Entering test case \"MyTestCaseFail\"" + "\n"
				+ "../src/TestExample.cpp(14): error in \"MyTestCaseFail\": check 1 == 2 failed [1 != 2]" + "\n"
				+ "Leaving test case \"MyTestCaseFail\"" + "\n"
				);

		verify(listener).testCaseStarted("MyTestCaseFail");
		verify(listener).testCaseFailed("MyTestCaseFail", "../src/TestExample.cpp(14): error in \"MyTestCaseFail\": check 1 == 2 failed [1 != 2]" + "\n");
	}

	@Test
	public void multipleTestSuiteWithMultipleTestCases() {
		parser.parse("" 
				+ "Entering test suite \"MyTest\"" + "\n"
				+ "Entering test case \"MyTestCasePass\"" + "\n"
				+ "Leaving test case \"MyTestCasePass\"" + "\n"
				+ "Entering test case \"MyTestCaseFail\"" + "\n"
				+ "../src/TestExample.cpp(14): error in \"MyTestCaseFail\": check 1 == 2 failed [1 != 2]" + "\n"
				+ "../src/TestExample.cpp(15): error in \"MyTestCaseFail\": check 3 == 2 failed [3 != 2]" + "\n"
				+ "Leaving test case \"MyTestCaseFail\"" + "\n"
				+ "Entering test case \"MyTestCaseFailException\"" + "\n"
				+ "../src/TestExample.cpp(21): error in \"MyTestCaseFailException\": exception std::bad_cast is expected" +"\n"
				+ "../src/TestExample.cpp(22): error in \"MyTestCaseFailException\": check 3 == 2 failed [3 != 2]" + "\n"
				+ "Leaving test case \"MyTestCaseFailException\"" + "\n" 
				+ "Entering test case \"MyTestCaseThrowException\"" + "\n"
				+ "unknown location(0): fatal error in \"MyTestCaseThrowException\": std::bad_cast: std::bad_cast" + "\n"
				+ "..\\src\\TestExample.cpp(27): last checkpoint" + "\n"
				+ "Leaving test case \"MyTestCaseThrowException\"; testing time: 1999mks" + "\n"
				+ "Entering test suite \"test1_suite\"" + "\n"
				+ "Entering test case \"Test1\"" + "\n"
				+ "../src/TestExample.cpp(41): error in \"Test1\": check 2 < 1 failed" + "\n"
				+ "Leaving test case \"Test1\"" + "\n"
				+ "Leaving test suite \"test1_suite\"" + "\n"
				+ "Entering test suite \"test2_suite\"" + "\n"
				+ "Entering test case \"Test2\"" + "\n" 
				+ "../src/TestExample.cpp(51): error in \"Test2\": check 2 < 1 failed" + "\n"
				+ "Leaving test case \"Test2\"" + "\n"
				+ "Leaving test suite \"test2_suite\"" + "\n"
				+ "Leaving test suite \"MyTest\"" + "\n"
				+ "\n"
				+ "*** 7 failures detected in test suite \"MyTest\""
				);
		
		InOrder inOrder = inOrder(listener);
		inOrder.verify(listener).testSuiteStarted("MyTest");
			inOrder.verify(listener).testCaseStarted("MyTestCasePass");
			inOrder.verify(listener).testCasePassed("MyTestCasePass");
			inOrder.verify(listener).testCaseStarted("MyTestCaseFail");
			inOrder.verify(listener).testCaseFailed(eq("MyTestCaseFail"), eq(
					"../src/TestExample.cpp(14): error in \"MyTestCaseFail\": check 1 == 2 failed [1 != 2]" + "\n"
					+ "../src/TestExample.cpp(15): error in \"MyTestCaseFail\": check 3 == 2 failed [3 != 2]" + "\n"));
			inOrder.verify(listener).testCaseStarted("MyTestCaseFailException");
			inOrder.verify(listener).testCaseFailed(eq("MyTestCaseFailException"), eq(
					"../src/TestExample.cpp(21): error in \"MyTestCaseFailException\": exception std::bad_cast is expected" + "\n"
					+ "../src/TestExample.cpp(22): error in \"MyTestCaseFailException\": check 3 == 2 failed [3 != 2]" + "\n"));
			inOrder.verify(listener).testCaseStarted("MyTestCaseThrowException");
			inOrder.verify(listener).testCaseFailed(eq("MyTestCaseThrowException"), eq(
					"unknown location(0): fatal error in \"MyTestCaseThrowException\": std::bad_cast: std::bad_cast" + "\n"
					+ "..\\src\\TestExample.cpp(27): last checkpoint" + "\n"));
			inOrder.verify(listener).testSuiteStarted("test1_suite");
				inOrder.verify(listener).testCaseStarted("Test1");
				inOrder.verify(listener).testCaseFailed(eq("Test1"), eq(
						"../src/TestExample.cpp(41): error in \"Test1\": check 2 < 1 failed" + "\n"));
			inOrder.verify(listener).testSuiteFinished("test1_suite");
			inOrder.verify(listener).testSuiteStarted("test2_suite");
				inOrder.verify(listener).testCaseStarted("Test2");
				inOrder.verify(listener).testCaseFailed(eq("Test2"), (String)any());
			inOrder.verify(listener).testSuiteFinished("test2_suite");
		inOrder.verify(listener).testSuiteFinished("MyTest");
	}
	
	@Test
	public void TestFailsWithException(){
		parser.parse(
				"Entering test case \"MyTestCaseFailException\"" + "\n"
				+ "../src/TestExample.cpp(21): error in \"MyTestCaseFailException\": exception std::bad_cast is expected" +"\n"
				+ "../src/TestExample.cpp(22): error in \"MyTestCaseFailException\": check 3 == 2 failed [3 != 2]" + "\n"
				+ "Leaving test case \"MyTestCaseFailException\"" + "\n" );
		verify(listener).testCaseStarted("MyTestCaseFailException");
		verify(listener).testCaseFailed(eq("MyTestCaseFailException"), eq(
				"../src/TestExample.cpp(21): error in \"MyTestCaseFailException\": exception std::bad_cast is expected" +"\n"
				+ "../src/TestExample.cpp(22): error in \"MyTestCaseFailException\": check 3 == 2 failed [3 != 2]" + "\n"));
	}

}
