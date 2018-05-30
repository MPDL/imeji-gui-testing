package spot.components;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import spot.pages.BasePage;

public class UploadWindow extends BasePage {

	@FindBy(className = "moxie-shim-html5")
	private WebElement divWrapper;
	
	@FindBy(xpath = "//input[contains(@id, 'html5_')]")
	private WebElement selectFiles;
	
	public UploadWindow(WebDriver driver) {
		super(driver);
		
		PageFactory.initElements(driver,  this);
	}
	
	public void uploadFile(String filepath) {		
		JavascriptExecutor jse = (JavascriptExecutor)driver;
		//Set the visibility of the upload element so that the sendKeys() method works
		jse.executeScript("arguments[0].style.visibility = 'visible';", selectFiles);
		//Wait for the visibility of the upload element
		wait.until(ExpectedConditions.attributeToBe(selectFiles, "visibility", "visible"));
		
		selectFiles.sendKeys(filepath);
	}
}
