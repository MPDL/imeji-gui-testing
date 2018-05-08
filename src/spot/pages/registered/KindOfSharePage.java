package spot.pages.registered;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import spot.pages.BasePage;
import spot.pages.CollectionEntryPage;

public class KindOfSharePage extends BasePage {

	@FindBy(id="share:userShare")
	private WebElement shareWithAUserButton;
	
	@FindBy(id="share:userGroupShare")
	private WebElement shareWithAUserGroup;
	
	@FindBy(css = ".imj_shareRightsOverview:nth-of-type(2) .imj_rightsTableUser")
	private WebElement pendingInvitations;
	
	@FindBy(className = "imj_backPanel")
	private WebElement backLink;
	
	public KindOfSharePage(WebDriver driver) {
		super(driver);
		
		PageFactory.initElements(driver, this);
	}

	public SharePage shareWithAUser() {
		shareWithAUserButton.click();
		
		return PageFactory.initElements(driver, SharePage.class);
	}
	
	public SharePage shareWithUserGroup() {
		shareWithAUserGroup.click();
		
		return PageFactory.initElements(driver, SharePage.class);
	}
	
	public CollectionEntryPage goBackToCollection() {
		backLink.click();
		return PageFactory.initElements(driver, CollectionEntryPage.class);
	}
	
	public boolean isSharedPersonInList(String sharedPersonName) {
		List<WebElement> sharedPersons = driver.findElements(By.className("imj_rightsTableUser"));
		
		for (WebElement sharedPerson : sharedPersons) {
			String spName = sharedPerson.getText();
			if (spName.equals(sharedPersonName)) {
				return true;
			}
		}
		return false;
	}

	public boolean isEmailPendingInvitation(String email) {
		return pendingInvitations.getText().equals(email);
	}
	
	/**
	 * Check method for collections
	 * @param released TODO
	 * @param wantedSharedPersonName - user's name in the form [surname, first]
	 * @return allGrantsCorrect - user permissions are exactly as specified in method signature
	 */
	public boolean checkGrantSelections(boolean released, String wantedSharedPersonName, boolean read, boolean editItems, boolean administrate) {

		WebElement wantedSharedPerson = findWantedPerson(wantedSharedPersonName);
		
		WebElement readRadioButton = wantedSharedPerson.findElement(By.xpath("//input[@value='READ']"));
		WebElement editRadioButton = wantedSharedPerson.findElement(By.xpath("//input[@value='EDIT']"));
		WebElement adminRadioButton = wantedSharedPerson.findElement(By.xpath("//input[@value='ADMIN']"));
		
		if (read)
			return readRadioButton.isSelected();
		if (editItems)
			return editRadioButton.isSelected();
		if (administrate)
			return adminRadioButton.isSelected();
		
		throw new IllegalArgumentException("Exactly one option should be set to true.");
	}
	
	private WebElement findWantedPerson(String wantedSharedPersonName) {
		List<WebElement> sharedPersons = driver.findElements(By.cssSelector("#history>tbody>tr"));
		
		for (WebElement sharedPerson : sharedPersons) {
			String sharedPersonName = sharedPerson.findElement(By.cssSelector("td:nth-of-type(1)")).getText();
			if (sharedPersonName.equals(wantedSharedPersonName)) {
				return sharedPerson;
			}
		}

		throw new NoSuchElementException("The wanted person's name was not found in the share page.");
	}
}
