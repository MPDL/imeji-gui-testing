package workshop;

import org.testng.Assert;
import org.testng.annotations.Test;

import testlink.api.java.client.TestLinkAPIClient;
import testlink.api.java.client.TestLinkAPIException;
import testlink.api.java.client.TestLinkAPIResults;

public class AutomatedUpdateExample {

	public static String DEVKEY = "55d98b5f4eb5139f57418541506f075c";

	public static String URL = "http://rd.mpdl.mpg.de/testlink/lib/api/xmlrpc/v1/xmlrpc.php";
	
	String testProject = "Imeji GUI testing";

	String testPlan = "Test of test";
	
	String build = "Build test of test";


	public static void reportResult(String TestProject, String TestPlan, String Testcase, String Build, String Notes,
			String Result) throws TestLinkAPIException {

		TestLinkAPIClient api = new TestLinkAPIClient(DEVKEY, URL);

		api.reportTestCaseResult(TestProject, TestPlan, Testcase, Build, Notes, Result);

	}

	@Test
	public void testCase() throws TestLinkAPIException {

		
		String testCase = "WorkshopTestCase_I";

		String notes = null;

		String result = TestLinkAPIResults.TEST_FAILED;

		// ....
		boolean myBoolean = true;

		try {
		
			Assert.assertEquals(myBoolean, false, "Not my expected value");
			
			notes = "Successfully executed.";
			result = TestLinkAPIResults.TEST_FAILED;
			
		} catch (AssertionError e) {
			notes = "Not successfully executed.";
			result = TestLinkAPIResults.TEST_FAILED;
			
		} finally {
			reportResult(testProject, testPlan, testCase, build, notes, result);
			
		}
		
	}
}
