package spot.components;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import spot.pages.BasePage;
import spot.pages.CollectionEntryPage;

public class AddSubcollectionDialog extends BasePage {

  @FindBy(xpath = "//div[@id='addSubCollection']//input[@id='cr:subColForm:name']")
  private WebElement nameInputField;

  @FindBy(xpath = "//div[@id='addSubCollection']//a[@class='imj_cancelButton']")
  private WebElement cancelButton;

  @FindBy(xpath = "//div[@id='addSubCollection']//input[@class='imj_submitButton']")
  private WebElement addSubcollectionButton;

  public AddSubcollectionDialog(WebDriver driver) {
    super(driver);

    PageFactory.initElements(driver, this);
  }

  public CollectionEntryPage addSubcollection(String subcollectionName) {
    this.nameInputField.sendKeys(subcollectionName);

    this.addSubcollectionButton.click();
    wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//h1//span[text()='" + subcollectionName + "']")));

    return PageFactory.initElements(driver, CollectionEntryPage.class);
  }

}
