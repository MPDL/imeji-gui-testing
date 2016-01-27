package spot.util;

import java.io.IOException;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.FolderClosedException;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.event.MessageCountAdapter;
import javax.mail.event.MessageCountEvent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.IMAPStore;

/**
 * This class provides access to an email account.
 * 
 * @author kocar
 *
 */
public class MailAccountManager {

	private static final Logger log4j = LogManager.getLogger(MailAccountManager.class.getName());
	
	private Session emailSession;
	
	private IMAPFolder inboxFolder;
	
	private String message;

	private Properties data;
	
	/**
	 * constructor with the required email account access data
	 * @param properties
	 */
	public MailAccountManager(Properties properties) {
		data = properties;
		setupEmailSession();
	}
	
	/**
	 * To be able to access the email account, a session with the required data needs to be established.
	 */
	private void setupEmailSession() {
		Properties properties = new Properties();
		properties.put("mail.imap.host", data.getProperty("tuHost"));
		properties.setProperty("mail.imap.ssl.enable", "true");
		properties.put("mail.imap.auth", "true");
		Authenticator auth = new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(data.getProperty("tuEmailUserName"), data.getProperty("tuEmailPassword"));
			}
		};

		emailSession = Session.getDefaultInstance(properties, auth);
	}

	/**
	 * Accessing the inbox folder of the email account.
	 */
	public synchronized void accessInboxFolder() {				
		
		// create the imap store object and connect with the pop server
		try {
			IMAPStore emailStore = (IMAPStore) emailSession.getStore(data.getProperty("tuMailStoreType"));

			emailStore.connect(data.getProperty("tuEmailUserName"), data.getProperty("tuEmailPassword"));

			// 3) create the folder object and open it
			inboxFolder = (IMAPFolder) emailStore.getFolder("INBOX");

			inboxFolder.open(Folder.READ_WRITE);
			
			MessageCountAdapter mca = new MessageCountAdapter() {
				public void messagesAdded(MessageCountEvent ev) {
					Message[] msgs = ev.getMessages();
					log4j.info("Got " + msgs.length + " new messages");
					
					Message mostRecentUnseenRegistrationMsg = msgs[msgs.length-1];
					
					try {
						message = getContentText(mostRecentUnseenRegistrationMsg);
						
						mostRecentUnseenRegistrationMsg.setFlag(Flags.Flag.SEEN, true);
						inboxFolder.removeMessageCountListener(this);
						// close the store and folder objects
						inboxFolder.close(false);
						emailStore.close();
					} catch (MessagingException | IOException e1) {
						log4j.error("Message either couldn't be converted into text or some other failure occured.");
						e1.printStackTrace();
					}

				}
			};
			inboxFolder.addMessageCountListener(mca);
		} catch (MessagingException e) {
			e.printStackTrace();
		}		
	}
	
	/**
	 * Converting message from mail folder to plain string.
	 * 
	 * @param message
	 *            that shall be converted
	 * @return
	 * @throws MessagingException
	 * @throws IOException
	 */
	private String getContentText(Part message) throws MessagingException, IOException {
		if (message.isMimeType("text/*")) {
			String s = (String) message.getContent();

			return s;
		}
		if (message.isMimeType("multipart/alternative")) {
			// prefer html text over plain text
			Multipart mp = (Multipart) message.getContent();
			String text = null;
			for (int i = 0; i < mp.getCount(); i++) {
				Part bp = mp.getBodyPart(i);
				if (bp.isMimeType("text/plain")) {
					if (text == null)
						text = getContentText(bp);
					continue;
				} else if (bp.isMimeType("text/html")) {
					String s = getContentText(bp);
					if (s != null)
						return s;
				} else {
					return getContentText(bp);
				}
			}
			return text;
		} else if (message.isMimeType("multipart/*")) {
			Multipart mp = (Multipart) message.getContent();
			for (int i = 0; i < mp.getCount(); i++) {
				String s = getContentText(mp.getBodyPart(i));
				if (s != null)
					return s;
			}
		}
		return null;
	}
	
	/**
	 * This method checks for the the awaited mail every 5000 milliseconds.
	 * 
	 * @return The message in a string.
	 */
	public synchronized String checkForNewMessage() {
		
		try {
			int freq = 5000;
			boolean supportsIdle = false;
			try {
				if (inboxFolder instanceof IMAPFolder) {
					IMAPFolder f = (IMAPFolder) inboxFolder;
					f.idle();
					supportsIdle = true;
				}
			} catch (FolderClosedException fex) {
				throw fex;
			} catch (MessagingException mex) {
				supportsIdle = false;
			}
			
			while(inboxFolder.isOpen()){
				if (supportsIdle && inboxFolder instanceof IMAPFolder) {
					IMAPFolder f = (IMAPFolder) inboxFolder;
					if (inboxFolder.isOpen())
						f.idle(true);
					log4j.info("IDLE done");
				} else {
					Thread.sleep(freq); // sleep for freq milliseconds
	
					// This is to force the IMAP server to send us
					// EXISTS notifications.
					inboxFolder.getMessageCount();
				}
			}	
		} catch (Exception ex) {			
			log4j.error(ex.getMessage());
			ex.printStackTrace();
		}
		
		return message;
	}
}
