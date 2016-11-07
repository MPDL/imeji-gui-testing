package spot.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class MetadataOverviewPage extends BasePage {

	@FindBy(id = "editMenu")
	public WebElement editMenu;
	
	@FindBy(id = "actionMenu")
	public WebElement actionMenu;
	
	public MetadataOverviewPage(WebDriver driver) {
		super(driver);
	}

	public int getNumberOfAvailableMetaDataFields() {
		List<WebElement> metaDataProfileItems = driver.findElements(By.className("imj_metadataProfileItem"));
		
		return metaDataProfileItems.size();
	}
	
	public boolean containsType(String wantedType) {
		List<WebElement> metadataBlocks = driver.findElements(By.className("imj_metadataProfileItem"));
		
		for (WebElement block : metadataBlocks) {
			String description = block.findElement(By.className(".imj_metadataValue")).getText();
			String type = description.split(" ")[0];
			if (type.equals(wantedType))
				return true;
		}
		
		return false;
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
	
	public EditMetadataPage editMetadata() {
		editMenu.click();
		driver.findElement(By.id("action:lnkSelectEdit3")).click();
		
		return PageFactory.initElements(driver, EditMetadataPage.class);
	}
	
	public MetadataTransitionPage changeMetadata() {
		actionMenu.click();
		actionMenu.findElement(By.cssSelector(".imj_menuBody li:nth-of-type(2)>a")).click();
		
		return PageFactory.initElements(driver, MetadataTransitionPage.class);
	}
}
