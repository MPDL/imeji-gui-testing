package workshop2;

import org.testng.annotations.Factory;

public class SomeEdmondTestFactory {

	@Factory(dataProviderClass=workshop2.SomeLoginDataProvider.class,dataProvider="loginDataProvider")
    public Object[] edmondTestFactory(String u, String p) {
		return new Object[] { new SampleTest3(u,p), new UseLessTest(u,p) };
	}
}
