package spot.pages;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class SharePage extends BasePage {

	@FindBy(css="#share\\:emailInput")
	private WebElement emailTextField;
	
	// checkboxes for the various grants
	@FindBy(css = "input[value='READ']")
	private WebElement readGrantCheckBox;
	
	@FindBy(css = "input[value='CREATE']")
	private WebElement createItemsGrantCheckBox;
	
	@FindBy(css = "input[value='EDIT_ITEM']")
	private WebElement editItemsGrantCheckBox;
	
	@FindBy(css = "input[value='DELETE_ITEM']")
	private WebElement deleteItemsGrantCheckBox;
	
	@FindBy(css = "input[value='EDIT']")
	private WebElement editInformationGrantCheckBox;
	
	@FindBy(css = "input[value='EDIT_PROFILE']")
	private WebElement editProfileGrantCheckBox;
	
	@FindBy(css = "input[value='ADMIN']")
	private WebElement administrateGrantCheckBox;
	
	@FindBy(css = "#share\\:selSendEmail")
	private WebElement sendMailCheckBox;
	
	@FindBy(css = "input[value='Share']")
	private WebElement shareButton;	
	
	@FindBy(id = "share:unknownEmails")
	private WebElement unknownEmailPanel;
	
	@FindBy(css = "#share\\:unknownEmails form")
	private WebElement emailList;
	
	@FindBy(id = "shareGroup")
	private WebElement groupPanel;
	
	public SharePage(WebDriver driver) {
		super(driver);
		
		PageFactory.initElements(driver, this);
	}

	/**
	 *  Share method for collections
	 *  @param if released == true, editProfile checkbox can be interacted with
	 */
	public SharePage share(boolean released, boolean sendMail, String email, boolean read, boolean createItems, boolean editItems, boolean deleteItems, boolean editCollectionInformation, boolean editProfile, boolean administrate) {
		emailTextField.sendKeys(email);
		
		return checkCollectionGrants(released, sendMail, read, createItems, editItems, deleteItems, editCollectionInformation, editProfile, administrate);
	}
	
	public SharePage share(boolean released, boolean sendMail, String email, boolean read, boolean editItems, boolean administrate) {
		emailTextField.sendKeys(email);
		
		return checkCollectionGrants(released, sendMail, read, editItems, administrate);
	}
	
	/**
	 * Share method for items
	 */
	public SharePage share(String email, boolean read) {
		emailTextField.sendKeys(email);
		setGrant(read, readGrantCheckBox);
		
		shareButton.click();
		return PageFactory.initElements(driver, SharePage.class);
	}
	
	/**
	 * Share method for user groups
	 */
	public SharePage shareWithGroup(String groupName, boolean released, boolean sendMail, boolean read, boolean createItems, boolean editItems, boolean deleteItems, boolean editCollectionInformation, boolean editProfile, boolean administrate) {
		selectGroup(groupName);
		
		return checkCollectionGrants(released, sendMail, read, createItems, editItems, deleteItems, editCollectionInformation, editProfile, administrate);
		
	}
	
	private void selectGroup(String groupName) {
		List<WebElement> userGroups = groupPanel.findElements(By.tagName("tr"));
		for (WebElement group : userGroups) {
			String currentName = group.findElement(By.tagName("h2")).getText();
			if (currentName.equals(groupName)) {
				group.findElement(By.className("imj_editButton")).click();
				break;
			}
		}
	}

	private SharePage checkCollectionGrants(boolean released, boolean sendMail, boolean read, boolean createItems, boolean editItems, boolean deleteItems, boolean editCollectionInformation, boolean editProfile, boolean administrate) {
		// check whether email shall be sent to the person who the album is shared with
		checkSendMail(sendMail);
		
		// check grants as defined by parameters
		if (administrate) {
			// if administrate is true, each checkbox is automatically selected
			setGrant(administrate, administrateGrantCheckBox);
		} 
		else if (!read) {
			// if read is false, each checkbox is automatically deselected
			setGrant(read, readGrantCheckBox);
		}
		else {
			setGrant(createItems, createItemsGrantCheckBox);
			setGrant(editItems, editItemsGrantCheckBox);
			setGrant(deleteItems, deleteItemsGrantCheckBox);
			setGrant(editCollectionInformation, editInformationGrantCheckBox);
			if (released)
				setGrant(editProfile, editProfileGrantCheckBox);
		}
		
		wait.until(ExpectedConditions.elementSelectionStateToBe(By.cssSelector("input[value='EDIT']"), editCollectionInformation));
		wait.until(ExpectedConditions.visibilityOf(shareButton));
		((JavascriptExecutor)driver).executeScript("arguments[0].click();", shareButton);
		//shareButton.click();
			
		return PageFactory.initElements(driver, SharePage.class);
	}
	
	private SharePage checkCollectionGrants(boolean released, boolean sendMail, boolean read, boolean editItems, boolean administrate) {
		if (administrate) {
			setGrant(administrate, administrateGrantCheckBox);
		} 
		else if (read) {
			setGrant(read, readGrantCheckBox);
		}
		else {
			setGrant(editItems, editItemsGrantCheckBox);
		}
		
		shareButton.click();
		return PageFactory.initElements(driver, SharePage.class);
	}
	
	private void setGrant(boolean grantedTo, WebElement radioButton) {
		try {
			if (grantedTo) {
				if (!radioButton.isSelected())
					radioButton.click();
			} 
			else { 
				if (radioButton.isSelected())
					radioButton.click();
			}
		}
		catch (StaleElementReferenceException exc) {
			PageFactory.initElements(driver, this);
			setGrant(grantedTo, radioButton);
		}
	}

	private void checkSendMail(boolean sendMail) {
		if (sendMail)
			if (!sendMailCheckBox.isSelected())
				sendMailCheckBox.click();
		else 
			if (sendMailCheckBox.isSelected())
				sendMailCheckBox.click();
	}

	public boolean checkPresenceOfSharedPersonInList(String sharedPersonName) {
	
		List<WebElement> sharedPersons = driver.findElements(By.className("imj_rightsTableUser"));
		
		for (WebElement sharedPerson : sharedPersons) {
			String spName = sharedPerson.getText();
			if (spName.equals(sharedPersonName)) {
				return true;
			}
		}
		return false;
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
	
	/**
	 * Check method for items
	 * @param wantedSharedPersonName - user's name in the form [surname, first]
	 */
	public boolean checkGrantSelections(String wantedSharedPersonName, boolean read) {
		WebElement wantedSharedPerson = findWantedPerson(wantedSharedPersonName);
		WebElement readGrant = wantedSharedPerson.findElement(By.tagName("input"));
		
		return checkCorrectnessOfGrant(read, readGrant);
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
	
	public boolean revokeDisplayed(String personName) {
		WebElement grantedUser = findWantedPerson(personName);
		
		try {
			grantedUser.findElement(By.className("fa-trash"));
			return true;
		}
		catch (NoSuchElementException exc) {
			return false;
		}
	}

	private boolean checkCorrectnessOfGrant(boolean grantedTo, WebElement grantCheckBox) {
		
		boolean isGrantCorrect = true;
		
		if (grantedTo) {
			if (!grantCheckBox.isSelected())
				isGrantCorrect = false;
		} else {
			if (grantCheckBox.isSelected())
				isGrantCorrect = false;
		}

		return isGrantCorrect;
	}
	
	public KindOfSharePage invite() {
		WebElement inviteButton = unknownEmailPanel.findElement(By.className("imj_submitButton"));
		inviteButton.click();
		
		return PageFactory.initElements(driver, KindOfSharePage.class);
	}
	
	public boolean inviteButtonEnabled() {
		WebElement inviteButton = unknownEmailPanel.findElement(By.className("imj_submitButton"));
		return inviteButton.isDisplayed() && inviteButton.isEnabled();
	}
	
	public List<WebElement> getExternalEmails() {
		return emailList.findElements(By.tagName("li"));
	}
	
	public boolean messageDisplayed() {
		return unknownEmailPanel.isDisplayed();
	}
}
