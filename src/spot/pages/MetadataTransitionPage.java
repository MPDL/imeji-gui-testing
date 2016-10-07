package spot.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

public class MetadataTransitionPage extends BasePage {

	@FindBy(css="input[value='Create a new metadata profile']")
	private WebElement createNewMetaDataProfileButton;
	
	@FindBy(css="input[value='Select an existing metadata profile']")
	private WebElement selectMetaDataProfileButton;
	
	public MetadataTransitionPage(WebDriver driver) {
		super(driver);
	}
	
	public NewMetadataProfilePage selectNewIndividualMetaDataProfile() {
		createNewMetaDataProfileButton.click();
		
		return PageFactory.initElements(driver, NewMetadataProfilePage.class);
	}
	
	public MetadataOverviewPage selectExistingMetadata(String profileName) {
		chooseProfile(profileName);
		driver.findElement(By.className("imj_submitButton")).click();
		
		return PageFactory.initElements(driver, MetadataOverviewPage.class);
	}
	
	public MetadataOverviewPage selectExistingMetadataTemplate(String profileName) {
		chooseProfile(profileName);
		driver.findElement(By.id("j_idt146:j_idt148:copyProfile")).click();
		driver.findElement(By.className("imj_submitButton")).click();
		
		return PageFactory.initElements(driver, MetadataOverviewPage.class);
	}
	
	private void chooseProfile(String profileName) {
		selectMetaDataProfileButton.click();
		
		WebElement metadataDropdown = driver.findElement(By.id("j_idt146:j_idt148:profileTemplates"));
		Select metadataSelect = new Select(metadataDropdown);
		metadataSelect.selectByVisibleText(profileName);
	}

}
