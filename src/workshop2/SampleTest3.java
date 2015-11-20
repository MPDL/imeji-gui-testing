package workshop2;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;
 
public class SampleTest3 extends SomeBaseTest {
 
//    private  String username = null;
//    private String password = null;
    
    
    //Constructor: test data in this case String line can be utilized by all the @Test methods.
//    @Factory(dataProviderClass=workshop2.SampleDataProvider.class,dataProvider="loginDataProvider")
//    public SampleTest3(String userN, String pw) {
    public SampleTest3(String u, String p) {
    	super(u, p);
        System.out.println("SampleTest3 Constructor : This is to demonstrate how testng initializes the test class while using dataprovider and @Factory annotation.");
//        this.username = userN;
//        this.password = pw;
        System.out.println("SampleTest3:Constructor: testData got initialized by the @Factory annotation (via constructor): " + username + " and " + password);
        System.out.println("===============================");
    }
     
    @Test
    public void testMethodONE() {
        //This should print each of the file content one after the other
        //testng calls this method for each line.
        System.out.println("SampleTest3:testDataProvider(): testData got initialized by the @Factory annotation (via constructor): " + username);
//        Assert.assertFalse(line.equals(""), "Name was empty");
        Assert.assertEquals(username.equals(""), false, "Name was empty");
    }   
    
    @Test
    public void testMethodTWO() {
        //This should print each of the file content one after the other
        //testng calls this method for each line.
        System.out.println("SampleTest3:testDataProvider(): testData got initialized by the @Factory annotation (via constructor): " + username);
        Assert.assertFalse(username.equals(""), "Name was empty");
    }
    
    @Test
    public void testMethodTHREE() {
    	System.out.println("Method three");
    	Assert.assertFalse(username.equals(""), "Name was empty");
    }
     
    @BeforeClass
    public void beforeClass() {
        System.out.println("In a method which has beforeClass annotation");
    }
     
    @AfterClass
    public void afterClass() {
        System.out.println("In a method which has afterClass annotation");
        System.out.println("===============================");
 
    }
}
