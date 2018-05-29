package test.scripts.basicEdmond;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.google.common.util.concurrent.SimpleTimeLimiter;

import spot.util.MailAccountManager;
import test.base.BaseSelenium;

public class MailTest extends BaseSelenium {
	
	private MailAccountManager mailAccountMngr;
	
  @Test(enabled=false)
  public synchronized void mailTest() {
      mailAccountMngr = new MailAccountManager(getProperties());
      mailAccountMngr.accessInboxFolder();
      
//      TODO: Update the code and run this test-method       
//      SimpleTimeLimiter timeLimiter = new SimpleTimeLimiter();

		String notificationMail = "";
		int predefinedTimeOutInSeconds = 30;
		try {
//			notificationMail = timeLimiter.callWithTimeout(new Callable<String>() {
//				public String call() {
//					return mailAccountMngr.checkForNewMessage();
//				}
//			}, predefinedTimeOutInSeconds, TimeUnit.SECONDS, false);
		} catch (Exception e) {
			Assert.assertTrue(!notificationMail.equals(""), "Time out after " + predefinedTimeOutInSeconds + "! Couldn't share collection with user. System didn't send notification mail.");			
		} 
		
		Assert.assertTrue(notificationMail.contains("share") && notificationMail.contains("collection"), "Message does not refer to shared collection.");
  }
}
