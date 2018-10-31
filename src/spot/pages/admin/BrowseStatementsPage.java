package spot.pages.admin;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import spot.pages.BasePage;

public class BrowseStatementsPage extends BasePage {
	
	public BrowseStatementsPage(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}
	
	public BrowseStatementsPage deleteStatement(String name) {
		WebElement statement = findStatement(name);
		statement.findElement(By.className("fa-trash")).click();
		
		WebElement deleteButton = driver.findElement(By.cssSelector("#deleteStatement>form>.imj_submitPanel>.imj_submitButton"));		
		deleteButton.click();
		
		wait.until(ExpectedConditions.stalenessOf(statement));
		
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
	
	// IMJ-271
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
		// Find labels that contain the statement name
		List<WebElement> statementLabelsContainingTheName = driver.findElements(By.xpath("//div[@class='imj_admindataLabel' and contains(text()[1],'" + name + "')]"));
		
		// Return the statement that matches the statement name exactly
		for (WebElement statementLabel : statementLabelsContainingTheName) {
			String currentName = statementLabel.getText();
			if (currentName.equals(name)) {
				WebElement statement = statementLabel.findElement(By.xpath(".//.."));
				return statement;
			}
		}
		
		throw new NoSuchElementException("Statement with this name is not available on the page.");
	}
	
	public int statementCount() {
		final int defaultAdmindataSetCount = 2;
		List<WebElement> adminDataSets = driver.findElements(By.xpath("//div[@class='imj_admindataSet']"));
		
		int statementCount = adminDataSets.size() - defaultAdmindataSetCount;
		
		return statementCount;
	}
	
}
