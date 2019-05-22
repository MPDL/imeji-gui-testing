package test.scripts.collectionMetadata;

import java.util.Arrays;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import spot.pages.CollectionEntryPage;
import spot.pages.LoginPage;
import spot.pages.StartPage;
import spot.pages.admin.AdminHomepage;
import spot.pages.admin.CollectionsConfigurationPage;
import spot.pages.registered.EditCollectionPage;
import spot.pages.registered.Homepage;
import spot.pages.registered.NewCollectionPage;
import spot.util.TimeStamp;
import test.base.BaseSelenium;

/**
 * Test for CRUD metadata (former: additional information) of a collection.
 * 
 * @author helk
 *
 */
public class CollectionMetadataTest extends BaseSelenium{
	
  private AdminHomepage adminHomepage;
  private Homepage homepage;
  private CollectionEntryPage currentOpenedCollection;
  private CollectionsConfigurationPage collectionsConfigurationPage;

  private String studyType1 = "Test Study Type 1";
  private String studyType2 = "Test Study Type 2";
  private String studyType3 = "Test Study Type 3";
  private String metadataField1 = "Test Metadata Field 1";
  private String metadataField2 = "Test Metadata Field 2";
  private String metadataAutosuggest1 = "Test Metadata Autosuggest 1";
  private String metadataAutosuggest2 = "Test Metadata Autosuggest 2";
  
  private String metadataFieldValue1 = "Test Metadata Field value 1";
  private String metadataAutosuggestValue1 = "Test Metadata Autosuggest value 1";
  
  private String collectionTitle = TimeStamp.getTimeStamp() + " Collection with Collection Metadata";
  private String collectionDescription = "default description 123 äüö ? (ß) μ å";
  
  @Test(priority = 1)
  public void loginAdmin() {
    LoginPage loginPage = new StartPage(driver).openLoginForm();
    adminHomepage = loginPage.loginAsAdmin(getPropertyAttribute(adminUsername), getPropertyAttribute(adminPassword));
  }
  
  @Test(priority = 2)
  public void configureCollectionMetadata() {
	  collectionsConfigurationPage = adminHomepage.goToAdminPage().goToCollectionsConfigurationPage();
	  
	  collectionsConfigurationPage.setStudyTypes(studyType1 + "\n" + studyType2 + "\n"+ studyType3);
	  collectionsConfigurationPage.setMetadataFields(metadataField1 + "\n" + metadataField2);
	  collectionsConfigurationPage.setMetadataAutosuggests(metadataAutosuggest1 + "\n" + metadataAutosuggest2);
  }

  @Test(priority = 3)
  public void logoutAdmin() {
	  collectionsConfigurationPage.logout();
  }
  
  /**
   * IMJ-1, IMJ-112
   */
  @Test(priority = 4)
  public void loginUser() {
    StartPage startPage = new StartPage(driver);
    LoginPage loginPage = startPage.openLoginForm();
    homepage = loginPage.loginAsNotAdmin(getPropertyAttribute(ruUsername), getPropertyAttribute(ruPassword));
  }
  
  /**
   * IMJ-112, IMJ-113, IMJ-83
   */
  @Test(priority = 5)
  public void createCollectionWithStudyTypes() {
    NewCollectionPage newCollectionPage = homepage.goToCreateNewCollectionPage();
    currentOpenedCollection = newCollectionPage.createCollection(collectionTitle, collectionDescription, getPropertyAttribute(ruGivenName),
        getPropertyAttribute(ruFamilyName), getPropertyAttribute(ruOrganizationName), Arrays.asList(studyType1, studyType2));
  }
  
  /**
   * IMJ-56
   */
  @Test(priority = 6)
  public void uploadItems() {
    uploadItem("SamplePPTXFile.pptx");
    uploadItem("SamplePNGFile.png");
    uploadItem("SampleTIFFile.tif");
  }

  //IMJ-131, IMJ-56
  private void uploadItem(String title) {
    currentOpenedCollection = currentOpenedCollection.uploadFile(getFilepath(title));

    boolean uploadSuccessful = currentOpenedCollection.findItem(title);
    Assert.assertTrue(uploadSuccessful, "Item not among uploads.");
  }
  
  @Test(priority = 7)
  public void addStudyType() {
	  EditCollectionPage editCollection = currentOpenedCollection.editInformation();
	  editCollection.selectStudyType(studyType3);
	  currentOpenedCollection = editCollection.submitChanges();
  }
  
  @Test(priority = 8)
  public void addCollectionMetadata() {
	  EditCollectionPage editCollection = currentOpenedCollection.editInformation();
	  editCollection.editCollectionMetadata(metadataField1, metadataFieldValue1);
	  currentOpenedCollection = editCollection.submitChanges();
  }
  
  @Test(priority = 9)
  public void addStudyContext() {
	  EditCollectionPage editCollection = currentOpenedCollection.editInformation();
	  editCollection.addAutoSuggestedStudyContext(metadataAutosuggest1, metadataAutosuggestValue1);
	  currentOpenedCollection = editCollection.submitChanges();
  }
  
  @Test(priority = 10)
  public void checkMoreInformationsForChanges() {
	  currentOpenedCollection = currentOpenedCollection.openMoreInformation();
	  
	  boolean studyTypesPresent = currentOpenedCollection.areStudyTypesPresent(Arrays.asList(studyType1, studyType2, studyType3));
	  boolean collectionMetadataPresent = currentOpenedCollection.isCollectionMetadataPresent(metadataField1, metadataFieldValue1);
	  boolean studyContextPresent = currentOpenedCollection.isCollectionMetadataPresent(metadataAutosuggest1, metadataAutosuggestValue1);
	  
	  Assert.assertTrue(studyTypesPresent, "The study types are not displayed.");
	  Assert.assertTrue(collectionMetadataPresent, "The collection metadata are not displayed.");
	  Assert.assertTrue(studyContextPresent, "The study contexts are not displayed.");
  }
  
  @Test(priority = 11)
  public void deleteCollection() {
    currentOpenedCollection = homepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
    currentOpenedCollection.deleteCollection();
  }
  
  @Test(priority = 12)
  public void logoutUser() {
	  StartPage startpage = new StartPage(driver).goToStartPage();
	  startpage.logout();
  }
  
  @Test(priority = 13)
  public void loginAdminAgain() {
    LoginPage loginPage = new StartPage(driver).openLoginForm();
    adminHomepage = loginPage.loginAsAdmin(getPropertyAttribute(adminUsername), getPropertyAttribute(adminPassword));
  }
  
  @Test(priority = 14)
  public void clearCollectionMetadata() {
	  collectionsConfigurationPage = adminHomepage.goToAdminPage().goToCollectionsConfigurationPage();
	  
	  collectionsConfigurationPage.clearStudyTypes();
	  collectionsConfigurationPage.clearMetadataFields();
	  collectionsConfigurationPage.clearMetadataAutosuggests();
  }
  
  /**
   * IMJ-2
   */
  @AfterClass
  public void afterClass() {
    StartPage startpage = new StartPage(driver).goToStartPage();
    startpage.logout();
  }

}
