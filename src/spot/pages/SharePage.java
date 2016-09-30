package spot.pages;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

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
	
	@FindBy(name="share:j_idt182")
	private WebElement shareButton;	
	
	public SharePage(WebDriver driver) {
		super(driver);
		
		PageFactory.initElements(driver, this);
	}

	/**
	 *  Share method for collections
	 */
	public SharePage share(boolean sendMail, String email, boolean read, boolean createItems, boolean editItems, boolean deleteItems, boolean editCollectionInformation, boolean editProfile, boolean administrate) {
		emailTextField.sendKeys(email);
		
		// check whether email shall be sent to the person who the album is shared with
		checkSendMail(sendMail);
		
		// check grants as defined by parameters
		if (administrate) {
			// if administrate is true, each checkbox is automatically selected
			checkGrantIfNecessary(administrate, administrateGrantCheckBox);
		} else {
			checkGrantIfNecessary(read, readGrantCheckBox);
			checkGrantIfNecessary(createItems, createItemsGrantCheckBox);
			checkGrantIfNecessary(editItems, editItemsGrantCheckBox);
			checkGrantIfNecessary(deleteItems, deleteItemsGrantCheckBox);
			checkGrantIfNecessary(editCollectionInformation, editInformationGrantCheckBox);
			checkGrantIfNecessary(editProfile, editProfileGrantCheckBox);
			checkGrantIfNecessary(administrate, administrateGrantCheckBox);
		}
			
		shareButton.click();
		
		return PageFactory.initElements(driver, SharePage.class);
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
		} else {
			checkGrantIfNecessary(read, readGrantCheckBox);
			checkGrantIfNecessary(createItems, createItemsGrantCheckBox);
			checkGrantIfNecessary(editAlbumInformation, editInformationGrantCheckBox);
			checkGrantIfNecessary(administrate, administrateGrantCheckBox);
		}
		
		shareButton.click();
		return PageFactory.initElements(driver, SharePage.class);
	}

	private void checkGrantIfNecessary(boolean grantedTo, WebElement checkBox) {
		if (grantedTo) {
			if (!checkBox.isSelected())
				checkBox.click();
		} else { 
			if (checkBox.isSelected())
				checkBox.click();
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
	
		List<WebElement> sharedPersons = driver.findElements(By.cssSelector("table#history>tbody>tr>td:nth-of-type(1)"));
		
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

	public boolean checkGrantSelections(String wantedSharedPersonName, boolean read, boolean createItems, boolean editItems, boolean deleteItems,
			boolean editCollectionInformation, boolean editProfile, boolean administrate) {

		WebElement wantedSharedPerson = findWantedPerson(wantedSharedPersonName);
		
		boolean allGrantsCorrect = true;
		List<WebElement> wantedSharedPersonCheckBoxes = wantedSharedPerson.findElements(By.cssSelector("td:nth-of-type(2) td>input"));
		
		if (administrate) {
			
			// since grant for administration is given, all the other grants are given to
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
			checkBoxes.put(wantedSharedPersonCheckBoxes.get(5), editProfile);
			checkBoxes.put(wantedSharedPersonCheckBoxes.get(6), administrate);
							
			Iterator iterator = checkBoxes.entrySet().iterator();
			
			while (iterator.hasNext() && allGrantsCorrect) {
	
				Map.Entry<WebElement, Boolean> checkBox = (Map.Entry<WebElement, Boolean>) iterator.next();
				
				allGrantsCorrect = checkCorrectnessOfGrant(checkBox.getValue(), checkBox.getKey());
			} 
		}
		return allGrantsCorrect;
	}
	
	public boolean checkGrantSelections(String wantedSharedPersonName, boolean read) {
		WebElement wantedSharedPerson = findWantedPerson(wantedSharedPersonName);
		WebElement readGrant = wantedSharedPerson.findElement(By.tagName("input"));
		
		return checkCorrectnessOfGrant(true, readGrant);
	}
	
	private WebElement findWantedPerson(String wantedSharedPersonName) {
		List<WebElement> sharedPersons = driver.findElements(By.cssSelector("#history>tbody>tr"));
		
		for (WebElement sharedPerson : sharedPersons) {
			String sharedPersonName = sharedPerson.findElement(By.cssSelector("td:nth-of-type(1)")).getText();
			if (sharedPersonName.equals(wantedSharedPersonName)) {
				return sharedPerson;
			}
		}
		return null;
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
}
