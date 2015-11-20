package edmondScripts;

import org.testng.annotations.Factory;

public class EdmondTestFactory {

	@Factory(dataProviderClass=edmondScripts.LoginDataProvider.class,dataProvider="loginDataProvider")
    public Object[] edmondTestFactory(String userName, String password, String givenName, String familyName, String organizationName) {
		return new Object[] { new DataUploadWithoutMetaDataProfileTest(userName,password, givenName, familyName, organizationName)};
	}
}
