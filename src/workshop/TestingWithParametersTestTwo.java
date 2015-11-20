package workshop;

import org.testng.annotations.Test;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Factory;

public class TestingWithParametersTestTwo /*extends UserFactory*/ {

	@DataProvider (name = "myDataProvider")
	public Object[][] dataMethod() {
		return new Object[][] { { "Catherina" }, { "Gulsan" } };
	}
	
	@Factory(dataProvider = "myDataProvider")
    public TestClass2[] testFactory(String name) {
        return new TestClass2[] { new TestClass2(name) };
    }

	public class TestClass2 {

        private String name;

        public TestClass2(String name) {
            this.name = name;
        }

        @Test
        public void testclass_2_1() throws InterruptedException {
            System.out.println("testclass_2_1 " + name);
        }

        @Test
        public void testclass_2_2() throws InterruptedException {
            System.out.println("testclass_2_2 " + name);
        }
    }
}
