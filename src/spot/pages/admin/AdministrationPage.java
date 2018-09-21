package spot.pages.admin;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import spot.pages.BasePage;
import test.base.StatementType;

public class AdministrationPage extends BasePage {

	@FindBy(css=".imj_listBody>div:nth-of-type(1) li:nth-of-type(1)>a")
	private WebElement createNewUser;
	
	@FindBy(css=".imj_listBody>.imj_adminPanel:nth-of-type(2)>.imj_content li>a")
	private WebElement createNewUserGroup;
	
	@FindBy(css=".imj_listBody>div:nth-of-type(1) li:nth-of-type(2)>a")
	private WebElement viewAllUsers;
	
	@FindBy(css=".imj_listBody>.imj_adminPanel:nth-of-type(2)>.imj_content li:nth-of-type(2)>a")
	private WebElement viewAllUserGroups;
	
	@FindBy(css=".imj_administrationTiledList .imj_userConfig:nth-of-type(5) a")
	private WebElement configurationEdit;
	
	@FindBy(css = ".imj_listBody>.imj_adminPanel:nth-of-type(3)>.imj_content li:nth-of-type(1)>a")
	private WebElement createStatement;
	
	@FindBy(css = ".imj_listBody>.imj_adminPanel:nth-of-type(3)>.imj_content li:nth-of-type(2)>a")
	private WebElement browseStatement;
	
	@FindBy(css = ".imj_listBody>.imj_adminPanel:nth-of-type(4)>.imj_content li:nth-of-type(1)>a")
	private WebElement createFacet;
	
	@FindBy(css = ".imj_listBody>.imj_adminPanel:nth-of-type(4)>.imj_content li:nth-of-type(2)>a")
	private WebElement browseFacet;
	
	public AdministrationPage(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}

	public UserProfilePage createNewUser(String newUserName) {
		createNewUser.click();
		NewUserPage createNewUserPage = new NewUserPage(driver);
		return createNewUserPage.createNewUser(newUserName);
	}
	
	public UserProfilePage createNewRestrictedUser(String newUserName) {
		createNewUser.click();
		NewUserPage createNewUserPage = new NewUserPage(driver);
		return createNewUserPage.createdNewRestrictedUser(newUserName);
	}
	
	public UsersOverviewPage viewAllUsers() {
		viewAllUsers.click();
		return PageFactory.initElements(driver, UsersOverviewPage.class);
	}

	// IMJ-38
	public UserGroupPage createNewUserGroup(String newUserGroupName) {
		createNewUserGroup.click();
		NewUserGroupPage createNewUserGroupPage = new NewUserGroupPage(driver);
		return createNewUserGroupPage.createNewUserGroup(newUserGroupName);
	}
	
	public UserGroupsOverviewPage viewAllUserGroups() {
		viewAllUserGroups.click();
		return PageFactory.initElements(driver, UserGroupsOverviewPage.class);
	}
	
	// IMJ-188
	public ConfigurationPage enablePrivateMode() {
		configurationEdit.click();
		ConfigurationPage configurationEditPage = new ConfigurationPage(driver);
		return configurationEditPage.enablePrivateMode();
	}
	
	public ConfigurationPage disablePrivateMode() {
		configurationEdit.click();
		ConfigurationPage configurationEditPage = new ConfigurationPage(driver);
		return configurationEditPage.disablePrivateMode();
	}
	
	public ConfigurationPage enableThumbnailView() {
		configurationEdit.click();
		ConfigurationPage configurationEditPage = new ConfigurationPage(driver);
		return configurationEditPage.enableThumbnailView();
	}
	
	// IMJ-240
	public ConfigurationPage enableListView() {
		configurationEdit.click();
		ConfigurationPage configurationEditPage = new ConfigurationPage(driver);
		return configurationEditPage.enableListView();
	}
	
	public ConfigurationPage setMaintenanceMessage(String message) {
		configurationEdit.click();
		ConfigurationPage configurationEditPage = new ConfigurationPage(driver);
		return configurationEditPage.setMaintenanceMessage(message);
	}
	
	public ConfigurationPage setTermsOfUse(String termsOfUse) {
		configurationEdit.click();
		ConfigurationPage configurationEditPage = new ConfigurationPage(driver);
		return configurationEditPage.setTermsOfUse(termsOfUse);
	}
	
	public ConfigurationPage setLicense(String license) {
		configurationEdit.click();
		ConfigurationPage configurationEditPage = new ConfigurationPage(driver);
		return configurationEditPage.setLicense(license);
	}
	
	// IMJ-191
	public ConfigurationPage setAutosuggestionMP() {
		configurationEdit.click();
		ConfigurationPage configurationEditPage = new ConfigurationPage(driver);
		return configurationEditPage.setAutosuggestionMP();
	}
	
	public ConfigurationPage enableRegistration(boolean usersCanRegister) {
		configurationEdit.click();
		ConfigurationPage configurationEditPage = new ConfigurationPage(driver);
		return configurationEditPage.enableRegistration();
	}
	
	public ConfigurationPage restrictRegistrationDomains(String domains) {
		configurationEdit.click();
		ConfigurationPage configurationEditPage = new ConfigurationPage(driver);
		return configurationEditPage.restrictRegistrationDomains(domains);
	}
	
	// IMJ-271
	public BrowseStatementsPage createStatement(String name, StatementType type) {
		createStatement.click();
		NewStatementPage newStatementPage = new NewStatementPage(driver);
		return newStatementPage.createNewStatement(name, type);
	}
	
	// IMJ-271
	public BrowseStatementsPage createStatement(String name, StatementType type, List<String> predefinedValues) {
		createStatement.click();
		NewStatementPage newStatementPage = new NewStatementPage(driver);
		return newStatementPage.createNewStatement(name, type, predefinedValues);
	}
	
	public BrowseStatementsPage deleteStatement(String name) {
		browseStatement.click();
		BrowseStatementsPage browseStatementsPage = new BrowseStatementsPage(driver);
		return browseStatementsPage.deleteStatement(name);
	}
	
	// IMJ-298
	public BrowseFacetsPage createSystemFacet(String facetTitle, String type) {
		createFacet.click();
		CreateFacetPage createFacet = new CreateFacetPage(driver);
		return createFacet.createSystemFacet(facetTitle, type);
	}
	
	// IMJ-282
	public BrowseFacetsPage createFacet(String facetTitle, String metadata) {
		createFacet.click();
		CreateFacetPage createFacet = new CreateFacetPage(driver);
		return createFacet.createFacet(facetTitle, metadata);
	}
	
	public CreateFacetPage goToCreateFacetPage() {
		createFacet.click();
		return PageFactory.initElements(driver, CreateFacetPage.class);
	}
	
	public BrowseFacetsPage changeSystemFacetSelection(String facetTitle, String newSelection) {
		browseFacet.click();
		BrowseFacetsPage browseFacets = new BrowseFacetsPage(driver);
		return browseFacets.changeSystemFacetSelection(facetTitle, newSelection);
	}
	
	public BrowseFacetsPage changeMetadataFacetSelection(String facetTitle, String newSelection) {
		browseFacet.click();
		BrowseFacetsPage browseFacets = new BrowseFacetsPage(driver);
		return browseFacets.changeMetadataFacetSelection(facetTitle, newSelection);
	}
	
	public BrowseFacetsPage renameFacet(String facetTitle, String newFacetTitle) {
		browseFacet.click();
		BrowseFacetsPage browseFacets = new BrowseFacetsPage(driver);
		return browseFacets.renameFacet(facetTitle, newFacetTitle);
	}
	
	public BrowseFacetsPage deleteFacet(String facetTitle) {
		browseFacet.click();
		BrowseFacetsPage browseFacets = new BrowseFacetsPage(driver);
		return browseFacets.deleteFacet(facetTitle);
	}
	
	public boolean areAllComponentsDisplayed() {
		try {
			driver.findElement(By.className("imj_userConfig"));
			driver.findElement(By.className("imj_config"));
			driver.findElement(By.className("imj_spaceConfig"));
			driver.findElement(By.className("imj_storageConfig"));
			return true;
		}
		catch (NoSuchElementException exc) {
			return false;
		}
	}
}
