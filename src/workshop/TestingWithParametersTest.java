package workshop;

import org.testng.annotations.Test;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Factory;

public class TestingWithParametersTest {
	
	protected String userName;
	protected String password;
	
	@Factory(dataProviderClass=workshop.LoginDataProvider.class, dataProvider="loginDataProvider")
	public TestingWithParametersTest(String username, String pw) {
		this.userName = username;
		this.password = pw;
	}
	
	 @Test/*(dataProvider = "dp")*/
	 public void f() {
		 System.out.println("This is method f: String is " + password );
	 }
	 
	 @Test/*(dataProvider = "dp")*/
	 public void g() {
		 System.out.println("This is method g: String is " + userName );
	 }
}
