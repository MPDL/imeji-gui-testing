package spot.scripts;

import java.awt.AWTException;
import java.io.IOException;

import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;
import org.testng.Assert;
import org.testng.annotations.*;

import spot.BaseSelenium;
import spot.pages.DetailedFileView;
import spot.pages.LoginPage;
import spot.pages.SingleUploadPage;
import spot.pages.admin.AdminHomePage;

public class SingleUploadTest extends BaseSelenium{

	/** name of file that is uploaded */
	String fileName = "spotguitesting.png";
	
	private AdminHomePage adminHomePage;
	private SingleUploadPage singleUploadPage;
	
	@BeforeClass
	public void loginAsAdmin() {
		LoginPage loginPage = getStartPage().openLoginForm();
		adminHomePage = loginPage.loginAsAdmin(getPropertyAttribute("aSpotUserName"), getPropertyAttribute("aSpotPassword"));
	}
	
	@Test (priority = 1)
	public void testLoadFile() {
		
		singleUploadPage = adminHomePage.goToUploadPage();
		try {
			
			
			String absolutePathToFile = "C:\\Users\\kocar\\Pictures\\" + fileName;
			String uploadedFile = singleUploadPage.upload(absolutePathToFile);
			
			Assert.assertTrue(uploadedFile.equalsIgnoreCase(fileName), "Name of uploaded file doesn't match with selected file's name");
			
		} catch (AWTException e) {
			e.printStackTrace();
		}
		
	}
	
	@Test (priority = 2)
	public void testSelectCollectionToUploadFile() {
		String collectionName = "Default first collection of Kocar, Gülüsan"; 
		String selectedComboText = singleUploadPage.selectCollectionToUploadFile(collectionName);
		
		Assert.assertTrue(collectionName.equalsIgnoreCase(selectedComboText), "Name of currently selected combobox value doesn't match with expected value");
	}
	
	@Test (priority = 3) 
	public void testSaveFile() {
		DetailedFileView detailedFileView = singleUploadPage.saveFile();
		String fileTitle = detailedFileView.getFileTitle();
		
		Assert.assertTrue(fileName.equals(fileTitle), "Name of uploaded file doesn't match with priorly selected file's name");
	}
	
	
	@AfterClass
	public void logout() {
		logout(PageFactory.initElements(getDriver(), AdminHomePage.class));	
	}
}
