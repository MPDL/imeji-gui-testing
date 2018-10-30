package test.scripts.highVolume;

import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import spot.pages.LoginPage;
import spot.pages.StartPage;
import spot.pages.admin.AdminHomepage;
import spot.pages.admin.AdministrationPage;
import spot.pages.admin.BrowseStatementsPage;
import spot.util.TimeStamp;
import test.base.BaseSelenium;
import test.base.StatementType;

public class CreateMultipleStatements extends BaseSelenium{

	private AdminHomepage adminHomepage;
	private AdministrationPage adminPage;
	private BrowseStatementsPage browseAllStatements;
	
	private String genericStatementName = TimeStamp.getTimeStamp() + "_statement_"; 
	
	private final int numberOfStatements = 2;
	
	/**
	 * IMJ-21
	 */
	@Test(priority = 1)
	public void loginAdmin() {
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		adminHomepage = loginPage.loginAsAdmin(getPropertyAttribute(adminUsername), getPropertyAttribute(adminPassword));
	}
	
	@Test(priority = 2)
	public void createStatements() {
		adminPage = adminHomepage.goToAdminPage();
		
		for(int i=0; i<numberOfStatements; i++) {
			this.createStatement(genericStatementName + i);
		}
	}
	
	public void createStatement(String newStatementName) {
		BrowseStatementsPage browseStatementsPage = adminPage.createStatement(newStatementName, StatementType.TEXT);
		adminPage = browseStatementsPage.goToAdminPage();
	}
	
	@Test(priority = 2)
	public void deleteStatements() {
		browseAllStatements = adminPage.browseAllStatements();
		
		for(int i=0; i<numberOfStatements; i++) {
			this.deleteStatement(genericStatementName + i);
		}
	}
	
	public void deleteStatement(String statementName) {
		browseAllStatements = browseAllStatements.deleteStatement(statementName);
	}
	
	/**
	 * IMJ-2
	 */
	@AfterClass
	public void afterClass() {
		adminHomepage = (AdminHomepage) new StartPage(driver).goToHomepage(adminHomepage);
		adminHomepage.logout();
	}
	
}
