package spot.pages;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.IOException;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;
import org.openqa.selenium.support.ui.Select;

public class SingleUploadPage extends BasePage {

	@FindBy(xpath=".//*[@id='pickfiles']")
	private WebElement singleUploadButton;
	
	@FindBy(xpath=".//*[@id='singleUpload:techmetadata']/div[1]/h3")
	private WebElement selectedFileLabel;
	
	@FindBy(xpath=".//*[@id='singleUpload:collections']")
	private WebElement collectionComboBoxWebElement;
	
	@FindBy(xpath=".//*[@id='singleUpload:submitTop']")
	private WebElement saveTopButton;

	private Select collectionComboBox;
	
//	@FindBy(xpath=".//*[@id='singleUpload:submitBottom']")
//	private WebElement saveBottomButton;
	
	public SingleUploadPage(WebDriver driver) {
		super(driver);
		
		PageFactory.initElements(driver, this);
	}

	public String upload(String pathToFile) throws AWTException {
				
		singleUploadButton.click();		
		
		selectFile(pathToFile);
		
		return getSelectedFileName();
	}

	private String getSelectedFileName() {
		ElementLocatorFactory elementLocatorFactory =  new AjaxElementLocatorFactory(driver, 5);
		PageFactory.initElements(elementLocatorFactory, this);
		String selectedFileName = "";
		
		String tmp = selectedFileLabel.getText();
		
		int indexOf = tmp.indexOf(':');
		selectedFileName = tmp.substring(indexOf + 2);
		
		return selectedFileName;
	}

	private void selectFile(String pathToFile) throws AWTException {
		StringSelection stringSelection = new StringSelection(pathToFile);
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
		Robot robot = new Robot();
//		robot.delay(1000);
	    robot.keyPress(KeyEvent.VK_CONTROL);
	    robot.keyPress(KeyEvent.VK_V);
	    robot.keyRelease(KeyEvent.VK_V);
	    robot.keyRelease(KeyEvent.VK_CONTROL);
	    robot.keyPress(KeyEvent.VK_ENTER);
	    robot.keyRelease(KeyEvent.VK_ENTER);
//	    robot.delay(1000);
	}

	public String selectCollectionToUploadFile(String collectionName) {
		
		collectionComboBox = new Select(collectionComboBoxWebElement);
		
		collectionComboBox.selectByVisibleText(collectionName);
		
		String selectedComboText = collectionComboBox.getFirstSelectedOption().getText();		
		return selectedComboText;
//		return "";
	}

	public DetailedFileView saveFile() {
		ElementLocatorFactory elementLocatorFactory =  new AjaxElementLocatorFactory(driver, 5);
		PageFactory.initElements(elementLocatorFactory, this);
		
		saveTopButton.click();
		
		return PageFactory.initElements(driver, DetailedFileView.class);
	}
}
