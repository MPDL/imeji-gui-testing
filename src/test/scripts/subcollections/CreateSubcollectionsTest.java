package test.scripts.subcollections;

import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import spot.pages.CollectionEntryPage;
import spot.pages.LoginPage;
import spot.pages.StartPage;
import spot.pages.admin.AdminHomepage;
import spot.pages.registered.Homepage;
import spot.pages.registered.NewCollectionPage;
import spot.util.TimeStamp;
import test.base.BaseSelenium;

/**
 * Testcase #28
 * 
 * @author helk
 *
 */
public class CreateSubcollectionsTest extends BaseSelenium {

  private AdminHomepage adminHomepage;
  private Homepage homepage;
  private CollectionEntryPage currentOpenedCollection;

  private String collectionTitle = TimeStamp.getTimeStamp() + " Collection containing Subcollections";
  private String collectionDescription = "default description 123 äüö ? (ß) μ å";

  private String subcollectionDefaultTitle = TimeStamp.getTimeStamp() + " Subcollection";
  private String subcollectionTitle = subcollectionDefaultTitle + " Single creation";

  private List<String> subcollectionHierachyNames = new ArrayList<>();

  /**
   * IMJ-21, IMJ-226
   */
  @Test(priority = 1)
  public void disablePrivateMode() {
    LoginPage loginPage = new StartPage(driver).openLoginForm();
    adminHomepage = loginPage.loginAsAdmin(getPropertyAttribute(adminUsername), getPropertyAttribute(adminPassword));
    adminHomepage.goToAdminPage().disablePrivateMode();
  }

  @Test(priority = 2)
  public void logoutAdmin() {
    adminHomepage.logout();
  }

  /**
   * IMJ-1, IMJ-112
   */
  @Test(priority = 3)
  public void loginUser() {
    StartPage startPage = new StartPage(driver);
    LoginPage loginPage = startPage.openLoginForm();
    homepage = loginPage.loginAsNotAdmin(getPropertyAttribute(ruUsername), getPropertyAttribute(ruPassword));
  }

  /**
   * IMJ-112, IMJ-113, IMJ-83
   */
  @Test(priority = 4)
  public void createDefaultCollection() {
    NewCollectionPage newCollectionPage = homepage.goToCreateNewCollectionPage();
    currentOpenedCollection = newCollectionPage.createCollection(collectionTitle, collectionDescription, getPropertyAttribute(ruGivenName),
        getPropertyAttribute(ruFamilyName), getPropertyAttribute(ruOrganizationName));
  }

  /**
   * IMJ-56
   */
  @Test(priority = 5)
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

  /**
   * IMJ-301
   */
  @Test(priority = 6)
  public void createSubcollection() {
    currentOpenedCollection = currentOpenedCollection.createSubcollection(subcollectionTitle);

    currentOpenedCollection = homepage.goToCollectionPage().openCollectionByTitle(collectionTitle);

    boolean collectionContainsSubcollection = currentOpenedCollection.findItem(subcollectionTitle);
    Assert.assertTrue(collectionContainsSubcollection, "Subcollection is not displayed in collection.");
  }

  /**
   * IMJ-302
   */
  @Test(priority = 7)
  public void editSubcollection() {
    currentOpenedCollection = homepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
    currentOpenedCollection = currentOpenedCollection.openSubcollection(subcollectionTitle);

    String subcollectionNewTitle = subcollectionDefaultTitle + " With new name";
    currentOpenedCollection = currentOpenedCollection.editSubcollectionName(subcollectionNewTitle);

    uploadItem("SampleXLSXFile.xlsx");
    uploadItem("SampleWordFile.docx");

    currentOpenedCollection = homepage.goToCollectionPage().openCollectionByTitle(collectionTitle);

    boolean collectionContainsSubcollection = currentOpenedCollection.findItem(subcollectionNewTitle);
    Assert.assertTrue(collectionContainsSubcollection, "Subcollection is not displayed in collection.");
  }

  @Test(priority = 8)
  public void createSubcollections() {
    currentOpenedCollection = homepage.goToCollectionPage().openCollectionByTitle(collectionTitle);

    int subcollectionCount = 7;
    this.createSubcollections(subcollectionCount);
  }

  private List<String> createSubcollections(int numberOfSubcollectionsToCreate) {
    List<String> subcollestionNames = new ArrayList<>();

    for (int i = 0; i < numberOfSubcollectionsToCreate; i++) {
      String subcollectionSiblingTitle = this.subcollectionDefaultTitle + " sibling number " + i;
      subcollestionNames.add(subcollectionSiblingTitle);
      currentOpenedCollection.createSubcollection(subcollectionSiblingTitle);
      uploadItem("SampleXLSXFile.xlsx");
      uploadItem("SampleWordFile.docx");
      currentOpenedCollection = homepage.goToCollectionPage().openCollectionByTitle(collectionTitle);

      boolean collectionContainsSubcollection = currentOpenedCollection.findItem(subcollectionSiblingTitle);
      Assert.assertTrue(collectionContainsSubcollection, "Subcollection is not displayed in collection.");
    }

    return subcollestionNames;
  }

  @Test(priority = 9)
  public void createSubcollectionsHierachy() {
    currentOpenedCollection = homepage.goToCollectionPage().openCollectionByTitle(collectionTitle);

    int subcollectionCount = 7;
    this.subcollectionHierachyNames = this.createCollectionsHierachy(subcollectionCount);
  }

  private List<String> createCollectionsHierachy(int numberOfSubcollectionsToCreate) {
    String currentCollectionTitle = currentOpenedCollection.getTitle();
    List<String> collectionHierachyNames = new ArrayList<>();
    collectionHierachyNames.add(currentCollectionTitle);

    for (int i = 0; i < numberOfSubcollectionsToCreate; i++) {
      String subcollectionDescendantTitle = this.subcollectionDefaultTitle + " on hierachy level " + i;
      collectionHierachyNames.add(subcollectionDescendantTitle);
      currentOpenedCollection = currentOpenedCollection.createSubcollection(subcollectionDescendantTitle);
      uploadItem("SampleWordFile.docx");
      uploadItem("SampleJPGFile.jpg");
    }

    int numberOfAncestors = currentOpenedCollection.readCollectionAncestorsHierachy().size();
    Assert.assertEquals(numberOfAncestors, numberOfSubcollectionsToCreate);

    return collectionHierachyNames;
  }

  /**
   * IMJ-303
   */
  @Test(priority = 10)
  public void moveSubcollectionInHierachy() {
    int moveStartLevel = 4;
    int moveDestinationLevel = 2;
    String subcollectionToMove = this.subcollectionHierachyNames.get(moveStartLevel);
    String nameOfNewParentCollection = this.subcollectionHierachyNames.get(moveDestinationLevel);

    currentOpenedCollection =
        currentOpenedCollection.getSearchComponent().searchForCollectionsByExactTitle(subcollectionToMove).openFirstCollection();

    //TODO: Check (Assert) the move dialog

    currentOpenedCollection = currentOpenedCollection.moveSubcollection(nameOfNewParentCollection);

    List<String> ancestors = currentOpenedCollection.readCollectionAncestorsHierachy();
    for (int i = 0; i < moveDestinationLevel; i++) {
      Assert.assertEquals(ancestors.get(i), this.subcollectionHierachyNames.get(i));
    }
  }

  @Test(priority = 11)
  public void publishCollection() {
    currentOpenedCollection = homepage.goToCollectionPage().openCollectionByTitle(collectionTitle);

    currentOpenedCollection.releaseCollection();

    boolean releaseDisplayed = currentOpenedCollection.releasedIconVisible();
    Assert.assertTrue(releaseDisplayed, "Released icon is not displayed.");
  }

  @Test(priority = 12)
  public void createSubcollectionsInPublishCollection() {
    int subcollectionCount = 3;
    this.createSubcollections(subcollectionCount);
  }

  @Test(priority = 13)
  public void createSubcollectionsHierachyInPublishCollection() {
    int subcollectionCount = 2;
    this.createCollectionsHierachy(subcollectionCount);
  }

  @Test(priority = 14)
  public void discardCollection() {
    currentOpenedCollection = homepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
    currentOpenedCollection.discardCollection();
  }

  /**
   * IMJ-2
   */
  @AfterClass
  public void afterClass() {
    homepage = new StartPage(driver).goToHomepage(homepage);
    homepage.logout();
  }

}
