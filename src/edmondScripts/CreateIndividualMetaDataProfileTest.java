package edmondScripts;

import org.testng.annotations.BeforeClass;

import spot.BaseSelenium;
import spot.pages.LoginPage;
import spot.pages.StartPage;
import spot.pages.admin.AdminHomePage;

public class CreateIndividualMetaDataProfileTest extends BaseSelenium {

	private LoginPage loginPage;
	private AdminHomePage adminHomePage;
	
	/*
	 
	 * add a metadata profile:
	 html/body/div[1]/div[5]/div[2]/h2/a 
	 
	 * create a new metadata profile 
	 .//*[@id='j_idt111']/input[2]
	 
	 * + Metadata
	 .//*[@id='profileForm:profile:0:j_idt248']
	 
	 * Metadata type dropbox; dasselbe für jeden einzelnen block
	 .//*[@id='mdProfileType']

	 * metadata type label textfield
	.//*[@id='profileForm:profile:0:labels:0:inputLabel']
	.//*[@id='profileForm:profile:1:labels:0:inputLabel']	  
	 
	 */
	
	@BeforeClass
	public void beforeClass() {
		navigateToStartPage();
		
		loginPage = new StartPage(driver).openLoginForm();
	}
}
