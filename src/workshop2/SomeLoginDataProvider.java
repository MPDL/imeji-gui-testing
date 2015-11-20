package workshop2;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
 
import org.testng.ITestContext;
import org.testng.annotations.DataProvider;
 
public class SomeLoginDataProvider {
 
	/*
    @DataProvider (name = "loginDataProvider")
    public static Iterator<Object[]> loginDataProvider () {
        
        //Get a list of String file content (line items) from the test file.
        List<String> testData = new ArrayList<String>();
        
//        testData.add("Catherina");
        testData.add("Gulsan");
 
        //We will be returning an iterator of Object arrays so create that first.
        List<Object[]> dataToBeReturned = new ArrayList<Object[]>();
 
        //Populate our List of Object arrays with the file content.
        for (String userData : testData)
        {
            dataToBeReturned.add(new Object[] { userData } );
        }
        //return the iterator - testng will initialize the test class and calls the 
        //test method with each of the content of this iterator.
        return dataToBeReturned.iterator();
 
    }
    */
 
    @DataProvider (name = "loginDataProvider")
	public static Object[][] dataMethod() {
		return new Object[][] { { "Catherina", "p1"}, { "Gulsan", "p2" } };
	}
}
