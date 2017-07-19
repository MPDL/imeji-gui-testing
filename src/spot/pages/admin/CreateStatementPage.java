package spot.pages.admin;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

import spot.pages.BasePage;

public class CreateStatementPage extends BasePage {

	@FindBy(id="j_idt102:j_idt103:inputStatementName")
	private WebElement nameBox;
	
	@FindBy(id="j_idt102:j_idt103:selStatementType")
	private WebElement typeDropdown;
	
	@FindBy(id="j_idt102:j_idt103:inputStatementNamespace")
	private WebElement namespaceBox;
	
	public CreateStatementPage(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}
	
	// check return type
//	public CreateStatementPage defaultStatementNumber(String name, int id) {
//		nameBox.sendKeys(name);
//		Select typeSelect = new Select(typeDropdown);
//		typeSelect.selectByValue("NUMBER");
//		namespaceBox.sendKeys("");
//		// click "Submit"
//	}
}
