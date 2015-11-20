package workshop;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Factory;


public class LoginDataProvider {

	protected String userName;
	protected String password;
	
	@DataProvider (name = "loginDataProvider")
    public static Object[][] dataMethod() {
		return new Object[][] { { "Catherina", "p1" }, { "Gulsan", "p2" } };
	} 
    
	
//	protected String userName;
//	protected String password;
//	
//	@Factory(dataProvider = "myDataProvider")
//	public UserFactory(String user, String p) {
//		this.userName = user;
//		this.password = p;
//	}
//	
//	@DataProvider(name = "myDataProvider")
//	public Object[][] dataMethod() {
//		return new Object[][] { { "Catherina", "p1" }, { "Gulsan", "p2" } };
//	}
	
//	@Factory(dataProvider = "myDataProvider")
//    public TestClass[] testFactory(String name, String password) {
//        return new TestClass[] { new TestClass(name, password) };
//    }
//	
//	@DataProvider (name = "myDataProvider")
//	public Object[][] dataMethod() {
//		return new Object[][] { { "Catherina", "p1" }, { "Gulsan", "p2" } };
//	}
}
