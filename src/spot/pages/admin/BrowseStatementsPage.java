package spot.pages.admin;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import spot.pages.BasePage;

public class BrowseStatementsPage extends BasePage {

	@FindBy(className="imj_itemContent")
	private List<WebElement> statements;
	
	private By deleteConfirmationLocator = By.className("imj_modalDialogBox");
	//private By deleteConfirmationLocator = By.xpath("//div[contains(@id, 'deleteStatement')]");
	
	public BrowseStatementsPage(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}
	
	public BrowseStatementsPage deleteStatement(String name) {
		WebElement statement = findStatement(name);
		statement.findElement(By.className("fa-trash")).click();
		wait.until(ExpectedConditions.visibilityOfNestedElementsLocatedBy(statement, deleteConfirmationLocator));
		WebElement deleteConfirmationDialog = statement.findElement(deleteConfirmationLocator);
		deleteConfirmationDialog.findElement(By.className("imj_submitButton")).click();
		try { Thread.sleep(2000); } catch(InterruptedException exc) {}
		
		return PageFactory.initElements(driver, BrowseStatementsPage.class);
	}
	
	public boolean isStatementPresent(String name) {
		try {
			findStatement(name);
			return true;
		}
		catch (NoSuchElementException exc) {
			return false;
		}
	}
	
	public BrowseStatementsPage makeDefault(String name) {
		WebElement statement = findStatement(name);
		statement.findElement(By.className("fa-star-o")).click();
		try { Thread.sleep(2000); } catch(InterruptedException exc) {}
		
		return PageFactory.initElements(driver, BrowseStatementsPage.class);
	}
	
	public boolean isDefault(String name) {
		WebElement statement = findStatement(name);
		try {
			statement.findElement(By.className("fa-star"));
			return true;
		}
		catch (NoSuchElementException exc) {
			return false;
		}
	}
	
	private WebElement findStatement(String name) {
		for (WebElement statement : statements) {
			String currentName = statement.findElement(By.className("imj_admindataLabel")).getText();
			if (currentName.equals(name)) {
				return statement;
			}
		}
		throw new NoSuchElementException("Statement with this name is not available on the page.");
	}
	
}
