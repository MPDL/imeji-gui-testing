package spot.pages;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class CreateMetaDataPage extends BasePage {

	@FindBy (xpath=".//*[@id='mdProfileType']")
	private List<WebElement> list;
	
	@FindBy (xpath=".//*[@id='profileForm:profile:0:j_idt248']")
	private WebElement createMetaDataBlock;
	
	public CreateMetaDataPage(WebDriver driver) {
		super(driver);
		
		PageFactory.initElements(driver, this);
	}
	
	public void createMetaDataProfile() {
		


		createMetaDataBlocks(7);
		
		
	}

	private void createMetaDataBlocks(int number) {
		
		// 8 metadata fields are needed; one already exists, create seven more
//		o	Datentyp Text
//		o	Datentyp Person
//		o	Datentyp Number
//		o	Datentyp Date
//		o	Datentyp Geolocation
//		o	Datentyp License
//		o	Datentyp Link
//		o	Datentyp Publication
		
		for (int i=0; i<number; i++) {
			createMetaDataBlock.click();
			try{
				Thread.sleep(2000);
			} catch (Exception e) {
				
			}
		}
	}

}
