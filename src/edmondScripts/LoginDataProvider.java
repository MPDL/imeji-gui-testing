package edmondScripts;

import org.testng.annotations.DataProvider;

public class LoginDataProvider {

	@DataProvider (name = "loginDataProvider")
	public static Object[][] loginDataProvider() {
		return new Object[][] { { "kocar@mpdl.mpg.de", "admin", "Gülüsan", "Kocar", "MPDL"}, { "spot-test@mpdl.mpg.de", "notadmin", "Max", "Mustermann", "MPDL" } };
	}
}
