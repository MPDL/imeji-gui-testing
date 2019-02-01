package spot.components;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import spot.pages.BasePage;
import spot.pages.CollectionEntryPage;

// TODO: Merge with class AddSubcollectionDialog
public class EditSubcollectionDialog extends BasePage {

  @FindBy(xpath = "//div[@id='editSubCollection']//input[@id='ed:subColForm:name']")
  private WebElement nameInputField;

  @FindBy(xpath = "//div[@id='editSubCollection']//a[@class='imj_cancelButton']")
  private WebElement cancelButton;

  @FindBy(xpath = "//div[@id='editSubCollection']//input[@class='imj_submitButton']")
  private WebElement changeCollectionNameButton;

  public EditSubcollectionDialog(WebDriver driver) {
    super(driver);

    PageFactory.initElements(driver, this);
  }

  public CollectionEntryPage changeCollectionName(String subcollectionName) {
    this.nameInputField.clear();
    this.nameInputField.sendKeys(subcollectionName);

    this.changeCollectionNameButton.click();
    //TODO: Readd the wait as soon as the bug that the page is not reloaded is fixed
    //    wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//h1//span[text()='" + subcollectionName + "']")));

    return PageFactory.initElements(driver, CollectionEntryPage.class);
  }

}
