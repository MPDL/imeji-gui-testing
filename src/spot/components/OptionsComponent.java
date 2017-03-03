package spot.components;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import spot.pages.BasePage;
import spot.pages.EditLicensePage;
import spot.pages.registered.EditItemsPage;

public class OptionsComponent extends BasePage {

	@FindBy(id = "actionCollectionMore")
	private WebElement menuHeader;
	
	public OptionsComponent(WebDriver driver) {
		super(driver);
		
		PageFactory.initElements(driver, this);
	}
	
	public EditItemsPage editAllItems() {
		menuHeader.click();
		menuHeader.findElement(By.cssSelector("#actionCollectionMore li:nth-of-type(1)")).click();
		
		return PageFactory.initElements(driver, EditItemsPage.class);
	}
	
	public EditLicensePage assignLicense() {
		menuHeader.click();
		menuHeader.findElement(By.cssSelector("#actionCollectionMore li:nth-of-type(2)")).click();
		
		return PageFactory.initElements(driver, EditLicensePage.class);
	}
}
