package spot.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class MetaDataOverViewPage extends BasePage {

	@FindBy(id = "editMenu")
	public WebElement editMenu;
	
	public MetaDataOverViewPage(WebDriver driver) {
		super(driver);
	}

	public int getNumberOfAvailableMetaDataFields() {
		List<WebElement> metaDataProfileItems = driver.findElements(By.className("imj_metadataProfileItem"));
		
		return metaDataProfileItems.size();
	}
	
	public boolean profileCanBeModified() {
		editMenu.click();
		try {
			wait.until(ExpectedConditions.elementToBeClickable(By.id("action:lnkSelectEdit3")));
			return true;
		}
		catch (TimeoutException exc) {
			return false;
		}
	}
}
