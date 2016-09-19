# imeji-gui-testing
A framework for the automated testing of Imeji that uses Selenium and TestNG and implements the Page Object pattern.

# Setup instructions
Clone the repository on your local machine; set Maven goal to "clean test".

# Providing user data
An additional properties file with registered user and admin information should be provided. The keys are listed in the methods <i>setupAdmin()</i> and <i>setupRegisteredUser()</i> in class BaseSelenium.
In order for the file to be read, set the environment variable <i>testData.properties</i> (name can be changed by changing the value of the attribute propertiesFileName in the SeleniumTestSuite class) to the absolute path of your properties file.

# Setting up the test suites
New suites can be added in the Maven Surefire Plugin configuration in pom.xml; each of the XML suites should contain the parameter <i>browserType</i>.
