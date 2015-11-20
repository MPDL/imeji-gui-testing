package edmondScripts;

import spot.BaseSelenium;

public abstract class BaseTest extends BaseSelenium{

	protected String username;
	protected String password;
	
	protected String givenName;
	protected String familyName;
	
	protected String organizationName;
	

	public BaseTest(String username, String password, String givenName, String familyName, String organizationName) {
		this.username = username;
		this.password = password;
		this.givenName = givenName;
		this.familyName = familyName;
		this.organizationName = organizationName;
	}
}
