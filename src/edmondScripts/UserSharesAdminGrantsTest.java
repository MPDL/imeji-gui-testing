package edmondScripts;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.google.common.util.concurrent.SimpleTimeLimiter;

import spot.BaseSelenium;
import spot.pages.CollectionContentPage;
import spot.pages.CollectionsPage;
import spot.pages.KindOfSharePage;
import spot.pages.LoginPage;
import spot.pages.SharePage;
import spot.pages.StartPage;
import spot.pages.admin.AdminHomePage;
import spot.util.MailAccountManager;

public class UserSharesAdminGrantsTest extends BaseSelenium {

	private MailAccountManager mailAccountMngr;
	
	private AdminHomePage adminHomePage;

	@BeforeClass
	public void beforeClass() {
		mailAccountMngr = new MailAccountManager(getProperties());
		mailAccountMngr.accessInboxFolder();
		
		navigateToStartPage();

		new StartPage(driver).selectLanguage(englishSetup);

		LoginPage loginPage = new StartPage(driver).openLoginForm();
		adminHomePage = loginPage.loginAsAdmin(getPropertyAttribute(spotAdminUserName),
				getPropertyAttribute(spotAdminPassWord));
	}

	@Test
	public void userSharesAdminGrantsForPublishedCollectionTest() {
		
		String sharedPersonName = getPropertyAttribute("tuFamilyName") + ", " + getPropertyAttribute("tuGivenName");
		
		boolean read=false, createItems=false, editItems=false, deleteItems=false, editCollectionInformation=false, editProfile=false;
		boolean administrate = true;
		
		CollectionsPage collectionPage = adminHomePage.goToCollectionPage();
		CollectionContentPage somePublishedCollection = collectionPage.openSomeNotPublishedCollection();
		KindOfSharePage kindOfSharePage = somePublishedCollection.share();
		SharePage sharePage = kindOfSharePage.shareWithAUser();
		sharePage.share(true, getPropertyAttribute(spotRUUserName), read, createItems, editItems, deleteItems, editCollectionInformation, editProfile , administrate);
		
		// 'shared with you' notification mail is going to be sent; Check mailBox		
		SimpleTimeLimiter timeLimiter = new SimpleTimeLimiter();

		String notificationMail="";
		int predefinedTimeOutInSeconds=10;
		try {
			notificationMail = timeLimiter.callWithTimeout(new Callable<String>() {
				public String call() {
					return mailAccountMngr.checkForNewMessage();
				}
			}, predefinedTimeOutInSeconds, TimeUnit.SECONDS, false);
		} catch (Exception e) {
			Assert.assertTrue(!notificationMail.equals(""), "Time out after " + predefinedTimeOutInSeconds + "! Couldn't share collection with user. System didn't send notification mail.");			
		} 
		
		boolean isSharedPersonListed = sharePage.checkPresenceOfSharedPersonInList(sharedPersonName);		
		Assert.assertTrue(isSharedPersonListed, "Person, who the published collection was shared with, isn't listed in the list of persons the collection is shared with.");
		
		boolean grantsOK = sharePage.checkGrantSelections(sharedPersonName, read, createItems, editItems, deleteItems, editCollectionInformation, editProfile, administrate);		
		Assert.assertTrue(grantsOK, "At least one of the grants is not correctly selected as it was intended in the first place.");
	}
	
	@AfterClass
	public void afterClass() {
		adminHomePage.logout();
	}
	
}
