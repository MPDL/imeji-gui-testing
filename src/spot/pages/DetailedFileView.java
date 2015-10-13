package spot.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class DetailedFileView extends BasePage{

	@FindBy(xpath=".//*[@id='picWebResolutionInternalDigilib']")
	private WebElement fileResolution;
	
	protected DetailedFileView(WebDriver driver) {
		super(driver);
	}

	public String getFileTitle() {
		String fileTitle = fileResolution.getAttribute("title");
		return fileTitle;
	}
	
	

}
