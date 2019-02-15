package spot.components;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import spot.pages.BasePage;
import spot.pages.CollectionEntryPage;

public class MoveDialog extends BasePage {

  @FindBy(xpath = "//div[@id='moveCol']//a[@class='imj_cancelButton']")
  private WebElement cancelButton;

  public MoveDialog(WebDriver driver) {
    super(driver);

    PageFactory.initElements(driver, this);
  }

  public CollectionEntryPage moveToCollection(String nameOfNewParentCollection) {
    WebElement newParentCollectionElement = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@id='tree']//a[text()='" + nameOfNewParentCollection + "']")));
    newParentCollectionElement.click();
    wait.until(ExpectedConditions.stalenessOf(newParentCollectionElement));

    return PageFactory.initElements(driver, CollectionEntryPage.class);
  }

}
