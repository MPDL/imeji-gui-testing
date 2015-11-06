package spot.components;

import java.util.NoSuchElementException;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class SortingComponent {

	private WebDriver driver;
	
	public enum SortBy {NAME, DATEOFCREATION, DATEOFMODIFICATION, AUTHOR, STATUS}
	
	public enum Order {ASCENDING, DESCENDING}
	
	@FindBy(xpath=".//*[@id='j_idt76:txtSort']")
	private WebElement sortButton;
	
	@FindBy(xpath=".//*[@id='j_idt76:j_idt153:lnkSortAsc']")
	private WebElement ascendingButton;
	
	@FindBy(xpath=".//*[@id='j_idt76:j_idt153:lnkSortDesc']")
	private WebElement descendingButton;
	
	@FindBy(xpath=".//*[@id='j_idt76:j_idt153']/ul/li[1]/a")
	private WebElement sortByNameButton;
	
	@FindBy(xpath=".//*[@id='j_idt76:j_idt153']/ul/li[2]/a")
	private WebElement sortByDateOfCreationButton;
	
	@FindBy(xpath=".//*[@id='j_idt76:j_idt153']/ul/li[3]/a")
	private WebElement sortByDateOfModificationButton;
	
	@FindBy(xpath=".//*[@id='j_idt76:j_idt153']/ul/li[4]/a")
	private WebElement sortByAuthor;
	
	@FindBy(xpath=".//*[@id='j_idt76:j_idt153']/ul/li[5]/a")
	private WebElement sortByStatus;
	
	public SortingComponent(WebDriver driver) {
		this.driver = driver;
		
		PageFactory.initElements(driver, this);
	}
	
	public void sortBy(SortBy sortBy, Order order) {
		
		sortButton.click();
		
		descendingAscendingSwitch(order);
		
		switch(sortBy) {
		case AUTHOR:
			sortByAuthor.click();
			break;
		case DATEOFCREATION:
			sortByDateOfCreationButton.click();
			break;
		case DATEOFMODIFICATION:
			sortByDateOfModificationButton.click();
			break;
		case NAME:
			sortByNameButton.click();
			break;
		case STATUS:
			sortByStatus.click();
			break;
		}
	}

	private void descendingAscendingSwitch(Order order) {
		
		switch(order) {
		case ASCENDING:
			try {
				ascendingButton.isDisplayed();
			} catch (NoSuchElementException nse) {
				descendingButton.click();
			}			
			break;
		case DESCENDING:
			try {
				descendingButton.isDisplayed();
			} catch (NoSuchElementException nse) {
				ascendingButton.click();
			}
			break;
		}
	}
}
