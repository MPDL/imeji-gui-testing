package spot.pages;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import spot.components.ActionComponent;
import spot.components.ActionComponent.ActionType;

public class MultipleUploadPage extends BasePage {

	@FindBy(xpath=".//*[@id='uploader_browse']")
	private WebElement addFileButton;
	
	@FindBy(xpath=".//*[@id='uploader_container']/div/div[2]/div[2]/div[1]/div/a[2]")
	private WebElement startUploadButton;
	
	@FindBy(name="lnkSelectEdit")
	private WebElement editUploadedImagesButton;
	
	private ActionComponent actionComponent;
	
	public MultipleUploadPage(WebDriver driver) {
		super(driver);
		
		actionComponent = new ActionComponent(driver);
		
		PageFactory.initElements(driver, this);
	}

	public void addFile(String filePath) throws AWTException {
		waitForInitationPageElements(10);
		addFileButton.click();
//		addFileButton.sendKeys(filePath);
//		addFileButton.submit();
		selectFile(filePath);
	}
	
	public void startUpload() {
		startUploadButton.click();		
	}
	
	private void selectFile(String pathToFile) throws AWTException {
		StringSelection stringSelection = new StringSelection(pathToFile);
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
		Robot robot = new Robot();
//		robot.delay(5000);
		
	    robot.keyPress(KeyEvent.VK_CONTROL);
	    robot.keyPress(KeyEvent.VK_V);
	    robot.keyRelease(KeyEvent.VK_V);
	    robot.keyRelease(KeyEvent.VK_CONTROL);
	    
	    robot.delay(3000);
	    robot.keyPress(KeyEvent.VK_ENTER);
	    robot.keyRelease(KeyEvent.VK_ENTER);
//	    robot.delay(5000);
	}

	public boolean verifyUploadedFiles(List<String> fileNames) {
		boolean successfullVerification = true;

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("imj_fileSuccessMessageArea")));
		
		WebElement uploadFileList = driver.findElement(By.className("imj_fileSuccessMessageArea"));
		
		List<WebElement> uploadedFiles = uploadFileList.findElements(By.className("imj_messageInfo"));
		
		if (uploadedFiles.size() != fileNames.size()) {
			successfullVerification = false;
		} else {
		
			List<String> listedFilesNames = new ArrayList<String>();
			
			for (WebElement listedFile : uploadedFiles) {
				listedFilesNames.add(listedFile.getText());			
			}
			
			for (String fileName : fileNames) {
				if (!listedFilesNames.contains(fileName)) {
					successfullVerification = false;
					break;
				}				
			}
		}
		
		return successfullVerification;
	}

	public void shareCollectionWithUser(String emailUser) {
		KindOfSharePage kindOfSharePage = (KindOfSharePage)actionComponent.doAction(ActionType.SHARE);
		
		SharePage sharePage = kindOfSharePage.shareWithAUser();
		sharePage.share(true, emailUser, true, true, true, false, false, false, false);
	}
	
	public void publishCollection() {
		actionComponent.doAction(ActionType.PUBLISH);
	}
	
	public ActionComponent getActionComponent() {
		return actionComponent;
	}
	
	public CollectiveEditOfDefaultMetaDataPage editUploadedImages() {
		editUploadedImagesButton.click();
		
		return PageFactory.initElements(driver, CollectiveEditOfDefaultMetaDataPage.class);
	}
}
