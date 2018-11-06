package spot.pages.admin;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import spot.pages.BasePage;
import test.base.StatementType;

public class NewStatementPage extends BasePage {

	@FindBy(xpath="//input[contains(@id, 'inputStatementName')]")
	private WebElement statementName;
	
	@FindBy(xpath="//select[contains(@id, 'selStatementType')]")
	private WebElement statementTypeDropdown;
	
	@FindBy(xpath="//input[contains(@id, 'btnCreateStatement')]")
	private WebElement saveButton;
	
	public NewStatementPage(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}
	
	public BrowseStatementsPage createNewStatement(String name, StatementType type) {
		statementName.sendKeys(name);
		new Select(statementTypeDropdown).selectByValue(type.toString());

		return submit();
	}
	
	public BrowseStatementsPage createNewStatement(String name, StatementType type, final List<String> predefinedValues) {
		statementName.sendKeys(name);
		new Select(statementTypeDropdown).selectByValue(type.toString());
		
		int predefinedCount = predefinedValues.size();
		for (int i = 0; i < predefinedCount; i++) {
			driver.findElement(By.className("fa-plus-square-o")).click();
			
			By xpathNewInput = By.xpath("//input[contains(@id, '" + i + ":inputPredefined')]");
			wait.until(ExpectedConditions.visibilityOfElementLocated(xpathNewInput));
			driver.findElement(xpathNewInput).sendKeys(predefinedValues.get(i));
		}
		
		return submit();
	}
	
	public BrowseStatementsPage submit(){
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[contains(@id, 'btnCreateStatement')]")));
		saveButton.click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("createStatement")));
		
		return PageFactory.initElements(driver, BrowseStatementsPage.class);
	}
}
