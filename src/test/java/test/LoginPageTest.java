package test;

import com.orangeHRM.base.BaseClass;
import com.orangeHRM.utilities.DataProviders;
import com.orangeHRM.utilities.ExtentManager;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import com.orangeHRM.pages.HomePage;
import com.orangeHRM.pages.LoginPage;

import java.io.IOException;
import java.lang.reflect.Method;

public class LoginPageTest extends BaseClass {
    private LoginPage loginPage;
    private HomePage homePage;
    @BeforeMethod
    public void setup(Method method) {
        ExtentManager.startTest(method.getName());  // 🔥 ADD HERE
    }
    @BeforeMethod
    public void setupPages(){
        loginPage=new LoginPage(getDriver());
        homePage=new HomePage(getDriver());
    }
    @Test(dataProvider="validLoginData",dataProviderClass = DataProviders.class)
    public void verifyValidLoginTest(String username,String password) throws IOException {
       // ExtentManager.startTest("Valid login test");->HANDLED BY TEST LISTNER
        ExtentManager.logStep("Navigating to login page entering username and password");
        loginPage.login(username,password);


        ExtentManager.logStep("verifying admin tab is visible or not");
        Assert.assertTrue(homePage.isAdminTabVisible(),"admin tab should be visible");
        ExtentManager.logStep("Validatation successful");

        homePage.logout();
        ExtentManager.logStep("logged out successfully");
    }
    // invalid login test
    @Test(dataProvider = "invalidLoginData",dataProviderClass = DataProviders.class)
    public void invalidLoginTest(String username,String password) throws IOException {
//        ExtentManager.startTest("Invalid Login Test");->HANDLED BY TEST LISTNER
        ExtentManager.logStep("Navigating to login page entering username and password");
        loginPage.login(username,password);
        ExtentManager.logStep("verifying admin tab is visible or not");
        Assert.assertTrue(loginPage.errorMessageDisplayed(),"error message should be displayed");
        ExtentManager.logStep("Validation Successful");
    }
    @Test
    public void verifyErrorMessage() throws IOException {
       // ExtentManager.startTest("Verifying Error Message");->HANDLED BY TEST LISTNER
        ExtentManager.logStep("Navigating to login page entering username and password");
        loginPage.login("admin","admin");
        ExtentManager.logStep("verifying admin tab is visible or not");
        String expected="Error message";
        Assert.assertTrue(loginPage.verifyErrorMessage(expected),"it should be equal");
        ExtentManager.logStep("Validation successful");
    }

}
