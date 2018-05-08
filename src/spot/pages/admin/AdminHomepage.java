package spot.pages.admin;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import spot.pages.registered.Homepage;

/**
 * HomePage for logged-in admins.
 * 
 * @author kocar
 *
 */
public class AdminHomepage extends Homepage {

	public AdminHomepage(WebDriver driver) {
		super(driver);
		
		PageFactory.initElements(driver, this);
	}

}
