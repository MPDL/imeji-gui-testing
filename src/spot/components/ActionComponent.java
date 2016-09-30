package spot.components;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import spot.pages.BasePage;
import spot.pages.CollectionContentPage;
import spot.pages.CollectionEntryPage;
import spot.pages.CollectionsPage;
import spot.pages.KindOfSharePage;

public class ActionComponent extends BasePage {

	public enum ActionType {SHARE, DELETE, PUBLISH, DISCARD};
	
	@FindBy(css="#actionMenu .imj_headerEntry")
	private WebElement actionButton;
	
	@FindBy(xpath="/html/body/div[1]/div[4]/div[2]/div[5]/div[1]/span[2]")
	private WebElement actionButton_2;
	
	@FindBy(css=".fa-share")
	private WebElement shareMenueLabel;
	
	@FindBy(css="#action\\:actionMenuShareCollection")
	private WebElement shareButton;
	
	@FindBy(id="action:actionMenuRelease")
	private WebElement publishButton;
	
	@FindBy(id="releaseMenuItemDialog")
	private WebElement releaseMenuItemDialog;
	
	@FindBy(id="action:actionMenuDelete")
	private WebElement deleteButton;
	
	@FindBy(id="action:actionMenuDelete")
	private WebElement deleteButton2;
	
	@FindBy(id="action:actionMenuDiscard")
	private WebElement discardButton;
	
	@FindBy(id="withdrawMenuItemDialog")
	private WebElement withDrawMenuItemDialog;
	
	@FindBy(xpath=".//*[@class='imj_confirmationReasonTextarea']/label")
	private WebElement discardCommentTextArea;
	
	@FindBy(css="#deleteMenuItemDialog .imj_submitButton")	
	private WebElement confirmDeletionButton;
	
	public ActionComponent (WebDriver driver) {
		super(driver);

		PageFactory.initElements(driver, this);
	}
	
	public BasePage doAction(ActionType actionType){
		BasePage returnPage = null;
		
		boolean isActionButtonDisplayed = false;
		try{
			retryingFindClick(By.cssSelector("#actionMenu .imj_headerEntry"));
			isActionButtonDisplayed = actionButton.isDisplayed();
		} catch (NoSuchElementException nse) {
			
			
			switch(actionType) {
			
				case DELETE: 
				actionButton_2.click();		
				deleteButton2.click();
				confirmDeletionButton.click();
				returnPage = new CollectionsPage(driver);
				break;
				
				case SHARE: 
					
				shareMenueLabel.click();
				shareButton.click();
				returnPage = new KindOfSharePage(driver);
				break;
				
				case PUBLISH:
				actionButton_2.click();
				wait.until(ExpectedConditions.elementToBeClickable(By.xpath("html/body/div[1]/div[3]/div[3]/div[2]/ul/li[2]/a")));
				retryingFindClick(By.xpath("html/body/div[1]/div[3]/div[3]/div[2]/ul/li[2]/a"));
				publishButton.click();
				
				try {
					
					releaseMenuItemDialog.findElement(By.className("imj_submitButton")).click();;
					
				} catch(NoSuchElementException e) {}
				
				break;
				
				case DISCARD:
				actionButton_2.click();
				discardButton.click();
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@class='imj_confirmationReasonTextarea']/label")));
				discardCommentTextArea.sendKeys("Discarding due to test automation purposes _ case 2");
								
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("withdrawMenuItemDialog")));
				WebElement confirmDiscard = withDrawMenuItemDialog.findElement(By.id("j_idt402:discardForm:btnDiscardContainer"));
				confirmDiscard.click();
				break;
			}
		}
		
		if (isActionButtonDisplayed) {
			
			
			switch(actionType) {
				
				case DELETE: 
				actionButton.click();		
				deleteButton.click();
				confirmDeletionButton.click();
				returnPage = new CollectionsPage(driver);
				break;
				
				case SHARE: 
				
				try {
				shareMenueLabel.click();
				} catch (NoSuchElementException e) {
					wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".fa-share")));
					shareMenueLabel.click();
				}
				shareButton.click();
				returnPage = new KindOfSharePage(driver);
				break;
				
				case PUBLISH:
				actionButton.click();
				publishButton.click();

				try {
					wait.until(ExpectedConditions.visibilityOf(releaseMenuItemDialog));
					
					releaseMenuItemDialog.findElement(By.className("imj_submitButton")).click();;
					
				} catch(NoSuchElementException e) {}
				returnPage = new CollectionContentPage(driver);
				break;
				
				case DISCARD:
				actionButton.click();
				discardButton.click();
				
				discardCommentTextArea.sendKeys("Discarding due to test automation purposes_ case 1");

				wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#withdrawMenuItemDialog .imj_submitButton")));
				
				WebElement confirmDiscardButton = driver.findElement(By.cssSelector("#withdrawMenuItemDialog .imj_submitButton"));
				confirmDiscardButton.click();
				
				returnPage = new CollectionEntryPage(driver);
				break;
			}
		}
		
		
		return returnPage;
	}
	
}
