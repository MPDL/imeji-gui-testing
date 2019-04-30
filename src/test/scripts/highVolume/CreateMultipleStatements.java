package test.scripts.highVolume;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
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
	
	private String genericStatementName = TimeStamp.getTimeStamp() + " temp_test_statement_"; 
	
	private final int numberOfStatements = 550;
	
	//FIXME: Running this test can lead in a crash of Firefox (see Ticket #40 'Firefox memory leak').
	//Workaround: Use Chrome in stead of Firefox to run this tests class.
	
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
		
		int oldStatementCount = this.getStatementCount();
		
		for(int i=0; i<numberOfStatements; i++) {
			this.createStatement(genericStatementName + i);
		}
		
		int newStatementCount = this.getStatementCount();
		Assert.assertEquals(newStatementCount-oldStatementCount, numberOfStatements, "Not all statements have been created.");
	}
	
	public int getStatementCount() {
		BrowseStatementsPage browseStatementsPage = adminPage.browseAllStatements();
		int statementCount = browseStatementsPage.statementCount();
		adminPage = browseStatementsPage.goToAdminPage();
		
		return statementCount;
	}
	
	public void createStatement(String newStatementName) {
		BrowseStatementsPage browseStatementsPage = adminPage.createStatement(newStatementName, StatementType.TEXT);
		adminPage = browseStatementsPage.goToAdminPage();
	}
	
	@Test(priority = 3)
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
