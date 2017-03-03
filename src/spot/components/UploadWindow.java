package spot.components;

import org.openqa.selenium.By;
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
		
		jse.executeScript("arguments[0].style.visibility = 'visible';", divWrapper);
		jse.executeScript("arguments[0].style.width = '5px';", divWrapper);
		jse.executeScript("arguments[0].style.height = '5px';", divWrapper);
		jse.executeScript("arguments[0].style.display = 'block';", divWrapper);
		
		jse.executeScript("arguments[0].style.visibility = 'visible';", selectFiles);
		jse.executeScript("arguments[0].style.width = '5px';", selectFiles);
		jse.executeScript("arguments[0].style.height = '5px';", selectFiles);
		jse.executeScript("arguments[0].style.display = 'block';", selectFiles);
		
		selectFiles.sendKeys(filepath);
		
		//String title = extractTitle(filepath);
		//wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//img[@title='" + title + "']")));
	}
	
	private String extractTitle(String filepath) {
		String[] separated = filepath.split("/");
		return separated[separated.length - 1];
	}
}
