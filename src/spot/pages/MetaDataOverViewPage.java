package spot.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class MetaDataOverViewPage extends BasePage {

	public MetaDataOverViewPage(WebDriver driver) {
		super(driver);
	}

	public int getNumberOfAvailableMetaDataFields() {
		List<WebElement> metaDataProfileItems = driver.findElements(By.className("imj_metadataProfileItem"));
		
		return metaDataProfileItems.size();
	}
}
