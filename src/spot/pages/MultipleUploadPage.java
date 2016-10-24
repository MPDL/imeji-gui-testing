package spot.pages;

import java.awt.AWTException;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import spot.components.ActionComponent;
import spot.components.ActionComponent.ActionType;

public class MultipleUploadPage extends BasePage {

	@FindBy(id="uploader_browse")
	private WebElement addFileButton;
	
	@FindBy(id="html5_1amijo7q41l2718uu19a7av8da33")
	private WebElement inputFileWindow;
	
	@FindBy(xpath=".//*[@id='uploader_container']/div/div[2]/div[2]/div[1]/div/a[2]")
	private WebElement startUploadButton;
	
	@FindBy(name="lnkSelectEdit")
	private WebElement editUploadedImagesButton;
	
	@FindBy(xpath="//input[contains(@id, 'selUniqueNames')]")
	private WebElement uniquenessCheckBox;
	
	@FindBy(xpath="//input[contains(@id, 'selUploadFile')]")
	private WebElement overwriteCheckBox;
	
	@FindBy(xpath="//input[contains(@id, 'selUploadThumb')]")
	private WebElement previewCheckBox;
	
	private ActionComponent actionComponent;
	
	public MultipleUploadPage(WebDriver driver) {
		super(driver);
		
		actionComponent = new ActionComponent(driver);
		
		PageFactory.initElements(driver, this);
	}

	public void addFile(String filePath) throws AWTException {
		JavascriptExecutor jse = (JavascriptExecutor)driver;
		String js = "arguments[0].style.visibility = 'visible';";
	    jse.executeScript(js, driver.findElement(By.xpath("/html/body/div[1]/div[5]/div[1]/div/div[1]/div/div[1]/div/div[2]/input")));
	    driver.findElement(By.xpath("/html/body/div[1]/div[5]/div[1]/div/div[1]/div/div[1]/div/div[2]/input")).sendKeys(filePath);
	    String jsa = "arguments[0].style.visibility = 'hidden';";
	    jse.executeScript(jsa, driver.findElement(By.xpath("/html/body/div[1]/div[5]/div[1]/div/div[1]/div/div[1]/div/div[2]/input")));
		
	}
	
	public void checkUniqueness(boolean check) {
		checkbox(uniquenessCheckBox, check);
	}
	
	public void overwriteFile(boolean overwrite) {
		checkbox(overwriteCheckBox, overwrite);
	}
	
	public void uploadPreview(boolean preview) {
		checkbox(previewCheckBox, preview);
	}
	
	private void checkbox(WebElement checkbox, boolean check) {
		if (check) {
			if (!checkbox.isSelected())
				checkbox.click();
		}
		else {
			if (checkbox.isSelected())
				checkbox.click();
		}
	}
	
	public void startUpload() {
		startUploadButton.click();	
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("imj_fileSuccessMessageArea")));
	}
	
	public void startFailedUpload() {
		startUploadButton.click();	
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("imj_fileErrorMessageArea")));
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
		sharePage.share(false, true, emailUser, true, true, true, false, false, false, false);
	}
	
	public void publishCollection() {
		actionComponent.doAction(ActionType.PUBLISH);
	}
	
	public ActionComponent getActionComponent() {
		return actionComponent;
	}
	
	public CollectiveEditOfDefaultMetadataPage editUploadedImages() {
		editUploadedImagesButton.click();
		
		return PageFactory.initElements(driver, CollectiveEditOfDefaultMetadataPage.class);
	}
}
