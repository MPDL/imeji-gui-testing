package spot.components;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import spot.pages.BasePage;
import spot.pages.CollectionsPage;
import spot.pages.KindOfSharePage;

public class ActionComponent {

	private WebDriver driver;
	
	public enum ActionType {SHARE, DELETE, PUBLISH};
	
	@FindBy(xpath="html/body/div[1]/div[3]/div[3]/div[1]/span")
	private WebElement actionButton;
	
	@FindBy(xpath="html/body/div[1]/div[3]/div[3]/div[2]/ul/li[1]/a")
	private WebElement shareButton;
	
	@FindBy(xpath="html/body/div[1]/div[3]/div[3]/div[2]/ul/li[2]/a")
	private WebElement publishButton;
	
	@FindBy(xpath="html/body/div[1]/div[3]/div[3]/div[2]/ul/li[3]/a")
	private WebElement deleteButton;
	
	@FindBy(xpath=".//*[@id='j_idt390:j_idt397']/input[2]")
	private WebElement confirmDeletionButton;
	
	public ActionComponent (WebDriver driver) {
		this.driver = driver;
		
		PageFactory.initElements(driver, this);
	}
	
	public BasePage doAction(ActionType actionType){
		BasePage returnPage = null;
		
		actionButton.click();
		
		switch(actionType) {
		
		case DELETE: 
			
		deleteButton.click();
		confirmDeletionButton.click();
		returnPage = new CollectionsPage(driver);
		break;
		
		case SHARE: 
			
		shareButton.click();
		returnPage = new KindOfSharePage(driver);
		break;
		
		case PUBLISH: publishButton.click();
		break;
		}
		
		return returnPage;
	}
	
}
