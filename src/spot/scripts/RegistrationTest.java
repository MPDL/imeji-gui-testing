package spot.scripts;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.annotations.*;

import spot.BaseSelenium;
import spot.SeleniumTestSuite;
import spot.pages.RegistrationPage;
import spot.pages.StartPage;
import spot.pages.notAdmin.HomePage;
import spot.util.MailAccountManager;

public class RegistrationTest extends BaseSelenium {

	private static final Logger log4j = LogManager.getLogger(RegistrationTest.class.getName());
	
	private MailAccountManager mailAccountMngr;
	private String verificationURL;
	private String verificationMessage;

	@BeforeClass
	public void accessEmailAccount() {
		
		mailAccountMngr = new MailAccountManager(getProperties());
		mailAccountMngr.accessInboxFolder();
	}

	/**
	 * Test registration action.
	 * 
	 * @throws TestLinkAPIException
	 */
	@Test(priority = 2)
	public void testSubmitRegistrationForm() {

		log4j.info("Testing properties file: " + getPropertyAttribute("tuSpotPassword"));
		
		// getting hold of registrationPage
		RegistrationPage registrationPage = new StartPage(driver).goToRegistrationPage();
		registrationPage.register(getPropertyAttribute("tuEmailAddress"), getPropertyAttribute("tuFamilyName"),
				getPropertyAttribute("tuGivenName"), getPropertyAttribute("tuOrganization"));

		// verification message is going to be sent; Check mail once in "freq" MILLIseconds
		verificationMessage = mailAccountMngr.checkForNewMessage();
		Assert.assertTrue(!verificationMessage.equals(""), "Couln't retrieve verification message");
		
	}

	/**
	 * Access mail account. Look for recently received registration verification
	 * mail. Finish registration process by clicking verification URL.
	 * 
	 * This test method is called after registration form is submitted.
	 * 
	 */
	@Test (priority = 3)
	public void testVerificationMail() {

		// look for verification link
		ArrayList<String> links = new ArrayList<String>();
		String regex = "\\(?\\b(http://|www[.])[-A-Za-z0-9+&amp;@#/%?=~_()|!:,.;]*[-A-Za-z0-9+&amp;@#/%=~_()|]";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(verificationMessage);
		while (matcher.find()) {
			String urlStr = matcher.group();
			if (urlStr.startsWith("(") && urlStr.endsWith(")")) {
				urlStr = urlStr.substring(1, urlStr.length() - 1);
			}
			links.add(urlStr);
		}

		if (links.size() == 1) {

			verificationURL = links.get(0);

			Assert.assertTrue(verificationURL.startsWith("http://qa-imeji.mpdl.mpg.de/register") && verificationURL.endsWith(getPropertyAttribute("tuEmailAddress")),
					"Invalid URL for activating spot account");

			// at this point it seems the registration is going to be successful; edit the properties file (by adding spot user name & password)
			editDataProperties();
			
				
		}

	}
	
	@Test (priority = 4) 
	public void testActivateAccount() {
		navigateDriverTo(verificationURL);
		
		RegistrationPage  registrationPage = new RegistrationPage(driver);
		
		HomePage homePage = registrationPage.activateAccount(getPropertyAttribute("tuSpotPassword"));

		String userFullName = getPropertyAttribute("tuGivenName") + " " + getPropertyAttribute("tuFamilyName");
		
		Assert.assertEquals(homePage.getLoggedInUserFullName(), userFullName, "User name doesn't match");		
	}
	
	@AfterClass
	public void logout() {
		logout(PageFactory.initElements(driver, HomePage.class));
	}

	private void editDataProperties() {
		// write userName into properties file
		getProperties().setProperty("tuSpotUserName", getPropertyAttribute("tuEmailAddress"));
		// extract password from message and write into properties file
		getProperties().setProperty("tuSpotPassword", extractPassword(verificationMessage)); 
		
		FileOutputStream output;
		try {
			output = new FileOutputStream(SeleniumTestSuite.propertiesFileName);
			getProperties().store(output, "This description goes to the header of a file");
		    output.close();
		} catch (IOException e) {
			e.printStackTrace();
			log4j.error("Error in regards of editing data properties file");
		}
	    
	}

	/**
	 * Extracting password from mail message.
	 * Example:
	 * "....... Benutzerkonto: kocar@mpdl.mpg.de	  	   		   			"
	 * Passwort: ZaufTS<kK>4Y(o3 ............."
	 * 
	 * @param messageContent
	 * @return
	 */
	private String extractPassword(String messageContent) {
		String password = "";

		int tmpIndex = messageContent.lastIndexOf(getPropertyAttribute("tuEmailAddress"))
				+ getPropertyAttribute("tuEmailAddress").length();
		messageContent = messageContent.substring(tmpIndex).trim();
		password = messageContent.split("\\s+")[1];
		log4j.info("Password is: " + password);
		return password;
	}

}
