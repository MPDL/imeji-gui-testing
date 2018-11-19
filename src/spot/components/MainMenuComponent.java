package spot.components;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import spot.pages.BrowseItemsPage;
import spot.pages.CollectionsPage;
import spot.pages.StartPage;
import spot.pages.admin.AdminHomepage;
import spot.pages.admin.AdministrationPage;
import spot.pages.registered.Homepage;

public class MainMenuComponent {

	private WebDriver driver;
	protected WebDriverWait wait;
	
	public MainMenuComponent(WebDriver driver) {
		this.driver = driver;
		
		//TODO: use a global static wait (like in BasePage)
		wait = new WebDriverWait(this.driver, 20);
		
		PageFactory.initElements(driver, this);
	}
	
	// TODO: old method, refactor
	public <T> T navigateTo(Class<T> expectedPage) {
		if (expectedPage == CollectionsPage.class) {
		    WebElement collectionsButton = driver.findElement(By.id("lnkCollections"));
			collectionsButton.click();
			wait.until(ExpectedConditions.stalenessOf(collectionsButton));
		} else if (expectedPage == StartPage.class) {
		    WebElement startButton =  driver.findElement(By.id("lnkHome"));
			startButton.click();
			wait.until(ExpectedConditions.stalenessOf(startButton));
		} else if (expectedPage == AdministrationPage.class) {
		    WebElement adminButton =  driver.findElement(By.id("lnkAdmin"));
			adminButton.click();
			wait.until(ExpectedConditions.stalenessOf(adminButton));
		} else if (expectedPage == Homepage.class || expectedPage == AdminHomepage.class) {
		    WebElement startButton =  driver.findElement(By.id("lnkHome"));
			startButton.click();
			wait.until(ExpectedConditions.stalenessOf(startButton));
		} else if (expectedPage == BrowseItemsPage.class) {
		    WebElement itemsButton =  driver.findElement(By.id("lnkItems"));
			itemsButton.click();
			wait.until(ExpectedConditions.stalenessOf(itemsButton));
		}
		
		return PageFactory.initElements(driver, expectedPage);
	}
	
}
