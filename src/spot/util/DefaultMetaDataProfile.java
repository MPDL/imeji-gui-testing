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
	private String organization;
	private String id;
	private String publicationLink;
	private String date;
	private String geolocName;
	private String latitude;
	private String longitude;

	private DefaultMetaDataProfile() {
	
		title = "test title";
		author = "Petrova";
		organization = "MPDL";
		id = "";
		publicationLink = "http://qa-edmond.mpdl.mpg.de";
		date = "2016-01-01";
		geolocName = "Munich, Germany";
		latitude = "49";
		longitude = "12";
		
	}

	public String getTitle() {
		return title;
	}

	public String getAuthor() {
		return author;
	}
	
	public String getOrganization() {
		return organization;
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
	
	public String getGeolocName() {
		return geolocName;
	}
	
	public String getLatitude() {
		return latitude;
	}
	
	public String getLongitude() {
		return longitude;
	}
	
	public static DefaultMetaDataProfile getDefaultMetaDataProfileInstance() {
		return DMDP;
	}
	
}
