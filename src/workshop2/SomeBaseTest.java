package workshop2;

import org.testng.annotations.Factory;

public abstract class SomeBaseTest {

	
	protected String username;
	protected String password;
	
	public SomeBaseTest(String u, String p) {
		username = u;
		password = p;
	}
	
//	 @Factory(dataProviderClass=workshop2.SampleDataProvider.class,dataProvider="loginDataProvider")
//	 public void factoring(String userN, String pw) {
//		 this.username = userN;
//	     this.password = pw;
//	 }
}
