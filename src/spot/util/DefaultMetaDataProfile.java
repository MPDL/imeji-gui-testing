package spot.util;

/**
 * This class provides a singleton object by accessing the method 'getDefaultMetaDataProfileInstance'.
 * The instance represents a default meta data profile object.
 * 
 * @author kocar
 *
 */
public class DefaultMetaDataProfile {
	
	private static final DefaultMetaDataProfile DMDP = new DefaultMetaDataProfile();
	
	private String title;
	private String author;
	private String id;
	private String publicationLink;
	private String date;

	private DefaultMetaDataProfile() {
	
		title = "test title";
		author = "test author family name";
		id = "1234567890";
		publicationLink = "https://www.test-publication-link.de";
		date = "1999-01-01";
	}

	public String getTitle() {
		return title;
	}

	public String getAuthor() {
		return author;
	}

	public String getId() {
		return id;
	}

	public String getPublicationLink() {
		return publicationLink;
	}

	public String getDate() {
		return date;
	}
	
	public static DefaultMetaDataProfile getDefaultMetaDataProfileInstance() {
		return DMDP;
	}
	
}
