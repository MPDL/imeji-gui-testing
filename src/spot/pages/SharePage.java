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
	
	/**
	 * Share method for items
	 */
	public SharePage share(String email, boolean read) {
		emailTextField.sendKeys(email);
		checkGrantIfNecessary(read, readGrantCheckBox);
		
		shareButton.click();
		return PageFactory.initElements(driver, SharePage.class);
	}
	
	/**
	 * Share method for albums
	 */
	public SharePage share(String email, boolean read, boolean createItems, boolean editAlbumInformation, boolean administrate) {
		emailTextField.sendKeys(email);
		
		if (administrate) {
			// if administrate is true, each checkbox is automatically selected
			checkGrantIfNecessary(administrate, administrateGrantCheckBox);
		} 
		else if (!read) {
			// if read is false, each checkbox is automatically deselected
			checkGrantIfNecessary(read, readGrantCheckBox);
		}
		else {
			checkGrantIfNecessary(createItems, createItemsGrantCheckBox);
			checkGrantIfNecessary(editAlbumInformation, editInformationGrantCheckBox);
		}
		
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
			checkGrantIfNecessary(administrate, administrateGrantCheckBox);
		} 
		else if (!read) {
			// if read is false, each checkbox is automatically deselected
			checkGrantIfNecessary(read, readGrantCheckBox);
		}
		else {
			checkGrantIfNecessary(createItems, createItemsGrantCheckBox);
			checkGrantIfNecessary(editItems, editItemsGrantCheckBox);
			checkGrantIfNecessary(deleteItems, deleteItemsGrantCheckBox);
			checkGrantIfNecessary(editCollectionInformation, editInformationGrantCheckBox);
			if (released)
				checkGrantIfNecessary(editProfile, editProfileGrantCheckBox);
		}
		
		wait.until(ExpectedConditions.elementSelectionStateToBe(By.cssSelector("input[value='EDIT']"), editCollectionInformation));
		wait.until(ExpectedConditions.visibilityOf(shareButton));
		((JavascriptExecutor)driver).executeScript("arguments[0].click();", shareButton);
		//shareButton.click();
			
		return PageFactory.initElements(driver, SharePage.class);
	}
	
	private void checkGrantIfNecessary(boolean grantedTo, WebElement checkBox) {
		try {
			if (grantedTo) {
				if (!checkBox.isSelected())
					checkBox.click();
			} 
			else { 
				if (checkBox.isSelected())
					checkBox.click();
			}
		}
		catch (StaleElementReferenceException exc) {
			PageFactory.initElements(driver, this);
			checkGrantIfNecessary(grantedTo, checkBox);
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
		
		boolean personFound = false;
		
		for (WebElement sharedPerson : sharedPersons) {
			String spName = sharedPerson.getText();
			if (spName.equals(sharedPersonName)) {
				personFound = true;
				break;
			}
		}
		
		return personFound;
	}

	/**
	 * Check method for collections
	 * @param released TODO
	 * @param wantedSharedPersonName - user's name in the form [surname, first]
	 * @return allGrantsCorrect - user permissions are exactly as specified in method signature
	 */
	public boolean checkGrantSelections(boolean released, String wantedSharedPersonName, boolean read, boolean createItems, boolean editItems,
			boolean deleteItems, boolean editCollectionInformation, boolean editProfile, boolean administrate) {

		WebElement wantedSharedPerson = findWantedPerson(wantedSharedPersonName);
		
		boolean allGrantsCorrect = true;
		List<WebElement> wantedSharedPersonCheckBoxes = wantedSharedPerson.findElements(By.cssSelector("td:nth-of-type(2) td>input"));
		
		if (administrate) {
			
			// since grant for administration is given, all the other grants are given too
			Iterator<WebElement> iterator = wantedSharedPersonCheckBoxes.iterator();
			
			while (iterator.hasNext() && allGrantsCorrect) {
				
				WebElement grantCheckBox = iterator.next();
				if (!grantCheckBox.isSelected())
					allGrantsCorrect = false;
			}
						
		} else {			
			
			// order as in html dom: read, create items, edit items, delete items, edit collection info, edit profile, administrate
			Map<WebElement, Boolean> checkBoxes = new HashMap<WebElement, Boolean>();
			checkBoxes.put(wantedSharedPersonCheckBoxes.get(0), read);
			checkBoxes.put(wantedSharedPersonCheckBoxes.get(1), createItems);
			checkBoxes.put(wantedSharedPersonCheckBoxes.get(2), editItems);
			checkBoxes.put(wantedSharedPersonCheckBoxes.get(3), deleteItems);
			checkBoxes.put(wantedSharedPersonCheckBoxes.get(4), editCollectionInformation);
			if (released) {
				checkBoxes.put(wantedSharedPersonCheckBoxes.get(5), editProfile);
				checkBoxes.put(wantedSharedPersonCheckBoxes.get(6), administrate);
			}
			else {
				checkBoxes.put(wantedSharedPersonCheckBoxes.get(5), administrate);
			}
							
			Iterator iterator = checkBoxes.entrySet().iterator();
			
			while (iterator.hasNext() && allGrantsCorrect) {
	
				Map.Entry<WebElement, Boolean> checkBox = (Map.Entry<WebElement, Boolean>) iterator.next();
				
				allGrantsCorrect = checkCorrectnessOfGrant(checkBox.getValue(), checkBox.getKey());
			} 
		}
		return allGrantsCorrect;
	}
	
	/**
	 * Check method for albums
	 */
	public boolean checkGrantSelections(String wantedSharedPersonName, boolean read, boolean addItems, boolean editAlbumInformation, boolean administrate) {
		WebElement wantedSharedPerson = findWantedPerson(wantedSharedPersonName);
		WebElement readGrant = wantedSharedPerson.findElement(By.id("share:shareList:grantListForm:list:1:role:0"));
		WebElement addGrant = wantedSharedPerson.findElement(By.id("share:shareList:grantListForm:list:1:role:1"));
		WebElement editGrant = wantedSharedPerson.findElement(By.id("share:shareList:grantListForm:list:1:role:2"));
		WebElement administrateGrant = wantedSharedPerson.findElement(By.id("share:shareList:grantListForm:list:1:role:3"));
		
		return checkCorrectnessOfGrant(read, readGrant) && checkCorrectnessOfGrant(addItems, addGrant) & checkCorrectnessOfGrant(editAlbumInformation, editGrant)
				& checkCorrectnessOfGrant(administrate, administrateGrant);
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
