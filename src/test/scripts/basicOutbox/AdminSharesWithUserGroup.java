package test.scripts.basicOutbox;

import java.util.LinkedList;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import spot.components.MessageComponent.MessageType;
import spot.pages.AdvancedSearchPage;
import spot.pages.CollectionEntryPage;
import spot.pages.EditCollectionPage;
import spot.pages.LoginPage;
import spot.pages.SearchResultsPage;
import spot.pages.SharePage;
import spot.pages.StartPage;
import spot.pages.admin.AdminHomepage;
import spot.pages.admin.UserGroupPage;
import spot.pages.admin.UserGroupsOverviewPage;
import spot.pages.registered.Homepage;
import spot.pages.registered.NewCollectionPage;
import spot.util.TimeStamp;
import test.base.BaseSelenium;

public class AdminSharesWithUserGroup extends BaseSelenium {

	private Homepage homepage;
	private AdminHomepage adminHomepage;
	private CollectionEntryPage collectionEntry;
	
	private String userGroupName = "Test User Group " + TimeStamp.getTimeStamp();
	private String collectionTitle = TimeStamp.getTimeStamp() + " Outbox private mode";
	private String collectionDescription = "default description 123 äüö ? (ß) μ å";

	/**
	 * IMJ-188
	 */
	@Test(priority = 1)
	public void switchPrivateMode() {
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		adminHomepage = loginPage.loginAsAdmin(getPropertyAttribute(adminUsername), getPropertyAttribute(adminPassword));
		adminHomepage.goToAdminPage().enablePrivateMode();
	}

	/**
	 * IMJ-38
	 */
	@Test(priority = 2)
	public void createUserGroup() {
		adminHomepage = (AdminHomepage) new StartPage(driver).goToHomepage(adminHomepage);
		UserGroupPage userGroup = adminHomepage.goToAdminPage().createNewUserGroup(userGroupName);
		UserGroupsOverviewPage allGroups = userGroup.goToAdminPage().viewAllUserGroups();
		Assert.assertTrue(allGroups.isNewUserGroupPresent(userGroupName), "User group is not in user group list.");
		Assert.assertTrue(allGroups.isNewUserGroupOnTop(userGroupName), "Newest user group is not on top of list.");
	}

	/**
	 * IMJ-41
	 */
	@Test(priority = 3)
	public void addUsersToGroup() {
		List<String> emailsNewUsers = new LinkedList<>();
		emailsNewUsers.add(getPropertyAttribute(ruUsername));
		emailsNewUsers.add(getPropertyAttribute(restrUsername));
		emailsNewUsers.add("test_temp1@mpdl.mpg.de");
		emailsNewUsers.add("test_temp2@mpdl.mpg.de");
		
		UserGroupsOverviewPage userGroups = adminHomepage.goToAdminPage().viewAllUserGroups();
		UserGroupPage groupPage = userGroups.viewUserGroupDetails(userGroupName);
		for (String email : emailsNewUsers)
			groupPage = groupPage.addNewUser(email);
		
		for (String email : emailsNewUsers)
			Assert.assertTrue(groupPage.isUserPresent(email), "User " + email + " was not added to user group.");
	}

	/**
	 * IMJ-40
	 */
	@Test(priority = 4)
	public void editGroupTitle() {
		UserGroupsOverviewPage userGroups = adminHomepage.goToAdminPage().viewAllUserGroups();
		UserGroupPage groupPage = userGroups.viewUserGroupDetails(userGroupName);
		userGroupName += " (revised)";
		groupPage = groupPage.changeTitle(userGroupName);
		Assert.assertEquals(groupPage.getUserGroupTitle(), userGroupName, "Title probably was not changed.");
	}

	/**
	 * IMJ-42
	 */
	@Test(priority = 5)
	public void deleteUserFromGroup() {
		UserGroupsOverviewPage userGroups = adminHomepage.goToAdminPage().viewAllUserGroups();
		UserGroupPage groupPage = userGroups.viewUserGroupDetails(userGroupName);
		groupPage = groupPage.deleteUser(0);
		Assert.assertFalse(groupPage.isUserPresent(getPropertyAttribute(restrUsername)), "User was not deleted from user group.");
	}

	/**
	 * IMJ-83
	 */
	@Test(priority = 6)
	public void createCollection() {
		NewCollectionPage newCollection = adminHomepage.goToCreateNewCollectionPage();
		collectionEntry = newCollection.createCollection(collectionTitle, collectionDescription,
				"", "QA Team MPDL", getPropertyAttribute(ruOrganizationName));
		
		MessageType messageType = collectionEntry.getPageMessageType();
		Assert.assertNotEquals(messageType, MessageType.NONE, "No message was displayed.");
		Assert.assertEquals(messageType, MessageType.INFO, "Success message was not displayed.");
	}

	/**
	 * IMJ-133
	 */
	@Test(priority = 7)
	public void uploadLogo() {
		collectionEntry = adminHomepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		EditCollectionPage editCollection = collectionEntry.editInformation();
		editCollection.addLogo(getFilepath("SampleJPGFile.jpg"));
		collectionEntry = editCollection.submitChanges();
		
		boolean hasLogo = collectionEntry.hasLogo();
		Assert.assertTrue(hasLogo, "Logo is not displayed.");
	}

	/**
	 * IMJ-123
	 */
	@Test(priority = 8)
	public void editTitle() {
		collectionEntry = adminHomepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		EditCollectionPage editCollection = collectionEntry.editInformation();
		collectionTitle += " (revised)";
		editCollection.editTitle(collectionTitle);
		collectionEntry = editCollection.submitChanges();
		
		String pageTitle = collectionEntry.getTitle();
		Assert.assertEquals(pageTitle, collectionTitle, "Title was not changed.");
	}
	
	private void uploadItem(String title) {
		collectionEntry = adminHomepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		collectionEntry = collectionEntry.uploadFile(getFilepath(title));
		// to avoid ElementNotVisibleException
		try { Thread.sleep(2500);} catch (InterruptedException e) {}
		
		boolean uploadSuccessful = collectionEntry.findItem(title);
		Assert.assertTrue(uploadSuccessful, "Item not among uploads.");
	}

	/**
	 * IMJ-56
	 */
	@Test(priority = 9)
	public void uploadCSV() {
		uploadItem("SampleCSVFile.csv");
	}

	/**
	 * IMJ-56
	 */
	@Test(priority = 10)
	public void uploadXLSX() {
		uploadItem("SampleXLSXFile.xlsx");
	}

	/**
	 * IMJ-241
	 */
	@Test(priority = 11)
	public void shareWithUserGroup() {
		collectionEntry = adminHomepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		SharePage sharePage = collectionEntry.share();
		sharePage = sharePage.shareWithGroup(userGroupName, false, false, true, false, false);
		
		boolean grantsCorrect = sharePage.checkGrantSelections(false, userGroupName, true, false, false);
		Assert.assertTrue(grantsCorrect, "Share grants are not correct.");
		
		boolean canRevoke = sharePage.revokeDisplayed(userGroupName);
		Assert.assertTrue(canRevoke, "Grants cannot be revoked: button not displayed.");
	}
	
	@Test(priority = 12)
	public void logout() {
		adminHomepage.logout();
	}

	/**
	 * IMJ-46
	 */
	@Test(priority = 13)
	public void openCollection() {
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		homepage = loginPage.loginAsNotAdmin(getPropertyAttribute(ruUsername), getPropertyAttribute(ruPassword));
		collectionEntry = homepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		
		Assert.assertTrue(collectionEntry.shareIconVisible(), "Share icon is not displayed.");
	}

	/**
	 * IMJ-183
	 */
	@Test(priority = 14)
	public void advancedSearch() {
		AdvancedSearchPage advancedSearch = homepage.goToAdvancedSearch();
		SearchResultsPage searchResults = advancedSearch.advancedSearch("*file*");
		Assert.assertNotEquals(searchResults.getResultCount(), 0, "Registered user cannot find shared content.");
	}

	/**
	 * IMJ-234
	 */
	@Test(priority = 15)
	public void downloadItems() {
		for (int i = 0; i <= 1; i++) {
			collectionEntry = homepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
			Assert.assertTrue(collectionEntry.openItem(i).isDownloadPossible(), "User cannot download item.");
		}
	}

	/**
	 * IMJ-2
	 */
	@Test(priority = 16)
	public void logoutRU() {
		homepage = new StartPage(driver).goToHomepage(homepage);
		homepage.logout();
	}

	/**
	 * IMJ-96
	 */
	@Test(priority = 17)
	public void deleteCollection() {
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		adminHomepage = loginPage.loginAsAdmin(getPropertyAttribute(adminUsername), getPropertyAttribute(adminPassword));
		collectionEntry = adminHomepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		collectionEntry.deleteCollection();
	}
	
	@Test(priority = 18)
	public void deleteGroup() {
		adminHomepage = (AdminHomepage) new StartPage(driver).goToHomepage(adminHomepage);
		UserGroupsOverviewPage userGroups = adminHomepage.goToAdminPage().viewAllUserGroups();
		userGroups = userGroups.deleteUserGroupByName(userGroupName);
		boolean groupDeleted = !userGroups.isNewUserGroupPresent(userGroupName);
		Assert.assertTrue(groupDeleted, "Group " + userGroupName + " was not deleted successfully.");
	}
	
	@AfterClass
	public void disablePrivateMode() {
		adminHomepage = (AdminHomepage) new StartPage(driver).goToHomepage(adminHomepage);
		adminHomepage.goToAdminPage().disablePrivateMode();
		adminHomepage.logout();
	}
 }
