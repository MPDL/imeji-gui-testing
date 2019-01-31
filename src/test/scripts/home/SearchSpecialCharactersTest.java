package test.scripts.home;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import spot.pages.CollectionEntryPage;
import spot.pages.ItemViewPage;
import spot.pages.LoginPage;
import spot.pages.SearchResultsPage;
import spot.pages.StartPage;
import spot.pages.admin.AdminHomepage;
import spot.pages.registered.Homepage;
import spot.pages.registered.NewCollectionPage;
import spot.util.TimeStamp;
import test.base.BaseSelenium;

/**
 * Testcase #13 (Simple Search, not private mode, NRU, special search terms)
 */
public class SearchSpecialCharactersTest extends BaseSelenium {

  private StartPage startPage;
  private Homepage homepage;
  private AdminHomepage adminHomepage;
  private CollectionEntryPage collectionEntry;

  private String collectionTitle = TimeStamp.getTimeStamp() + " Create Collection/Item with special Characters to Search for";
  private String collectionDescription = "default description 123 äüö ? (ß) μ å";

  private String itemName = "SamplePDFFile.pdf";
  private String testItemName = "ÄÜÖßñ";

  @BeforeMethod
  public void beforeMethodTest() {
    navigateToStartPage();
  }

  @BeforeClass
  public void beforeClassTest() {
    startPage = new StartPage(driver);
  }

  @Test(priority = 1)
  public void createItemToSearchFor() {
    this.disablePrivateMode();
    this.loginUser();
    this.createDefaultCollection();
    this.uploadItem();
    this.setNameOfItem();
    this.publishCollection();
    this.logout();
  }

  private void disablePrivateMode() {
    LoginPage loginPage = new StartPage(driver).openLoginForm();
    adminHomepage = loginPage.loginAsAdmin(getPropertyAttribute(adminUsername), getPropertyAttribute(adminPassword));

    adminHomepage.goToAdminPage().disablePrivateMode();

    adminHomepage = (AdminHomepage) new StartPage(driver).goToHomepage(adminHomepage);
    adminHomepage.logout();
  }

  public void loginUser() {
    LoginPage loginPage = new StartPage(driver).openLoginForm();
    homepage = loginPage.loginAsNotAdmin(getPropertyAttribute(ruUsername), getPropertyAttribute(ruPassword));
  }

  private void createDefaultCollection() {
    NewCollectionPage newCollectionPage = homepage.goToCreateNewCollectionPage();
    collectionEntry = newCollectionPage.createCollection(collectionTitle, collectionDescription, getPropertyAttribute(ruGivenName),
        getPropertyAttribute(ruFamilyName), getPropertyAttribute(ruOrganizationName));
  }

  private void uploadItem() {
    collectionEntry = homepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
    collectionEntry = collectionEntry.uploadFile(getFilepath(itemName));
    collectionEntry = collectionEntry.goToCollectionPage().openCollectionByTitle(collectionTitle);

    boolean uploadSuccessful = collectionEntry.findItem(itemName);
    Assert.assertTrue(uploadSuccessful, "Item " + itemName + " not among uploads.");
  }

  private void setNameOfItem() {
    ItemViewPage itemViewPage = collectionEntry.openItem(itemName).editItem().editFileName(testItemName);
    String valueOfMetaData = itemViewPage.getValueOfMetaData("File name");
    Assert.assertEquals(valueOfMetaData, testItemName, "File name was not changed.");
  }

  private void publishCollection() {
    collectionEntry = homepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
    collectionEntry.releaseCollection();
  }

  private void logout() {
    homepage = new StartPage(driver).goToHomepage(homepage);
    homepage.logout();
  }

  @Test(priority = 2)
  public void searchAUmlaut() {
    search("*ä*");
  }

  @Test(priority = 3)
  public void searchOUmlaut() {
    search("*ö*");
  }

  @Test(priority = 4)
  public void searchUUmlaut() {
    search("*ü*");
  }

  @Test(priority = 5)
  public void searchEszett() {
    search("*ß*");
  }

  @Test(priority = 6)
  public void searchNTilde() {
    search("*ñ*");
  }

  private void search(String searchQuery) {
    SearchResultsPage searchQueryPage = startPage.getSearchComponent().searchFor(searchQuery);
    String searchQueryDisplayText = searchQueryPage.getItemSearchQuery();
    Assert.assertEquals(searchQueryDisplayText, searchQuery);

    int numResults = searchQueryPage.getResultCount();
    Assert.assertTrue(numResults > 0, "Items were not successfully found.");
  }

  @Test(priority = 7)
  public void loginAdminUserAgain() {
    LoginPage loginPage = new StartPage(driver).openLoginForm();
    adminHomepage = loginPage.loginAsAdmin(getPropertyAttribute(adminUsername), getPropertyAttribute(adminPassword));
  }

  @Test(priority = 8)
  public void discardCollection() {
    collectionEntry = adminHomepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
    collectionEntry.discardCollection();
  }

  //TODO: Remove the revokeDiscardedCollection! It is only a workaround (for imeji-GitHub-BugTicket #1091) to keep the selenium tests running!
  //    @Test(priority = 9)
  //    public void revokeDiscardedCollection() {        
  //        UserProfilePage userProfilePage = adminHomepage.goToAdminPage().browseAllUsers().viewDetailsOfUser(getPropertyAttribute(ruUsername));
  //        userProfilePage.revokeUserGrants(collectionTitle);
  //    }

  /**
   * IMJ-2
   */
  @AfterClass
  public void afterClass() {
    adminHomepage.logout();
  }

}
