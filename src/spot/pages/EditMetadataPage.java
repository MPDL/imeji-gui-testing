package spot.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

public class EditMetadataPage extends BasePage {
	
	@FindBy(xpath="/html/body/div[1]/div[5]/div[1]/form/div/div[2]/div[2]/input[2]")
	private WebElement saveButton;

	public EditMetadataPage(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}
	
	public void changeBlockType(int index, String newType) {
		WebElement block = getBlockByIndex(index);
		WebElement typeDropdown = block.findElement(By.id("mdProfileType"));
		typeDropdown.click();
		
		Select typeSelect = new Select(typeDropdown);
		typeSelect.selectByValue(newType);
	}
	
	public MetadataOverviewPage saveChanges() {
		retryingFindClick(By.xpath("/html/body/div[1]/div[5]/div[1]/form/div/div[2]/div[2]/input[2]"));
		
		return PageFactory.initElements(driver, MetadataOverviewPage.class);
	}
	
	private WebElement getBlockByIndex(int index) {
		WebElement metadataBlock = driver.findElement(By.id("profileForm:profile:" + index + ":metadata"));
		return metadataBlock;
	}
}
