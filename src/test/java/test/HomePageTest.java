package test;

import com.orangeHRM.base.BaseClass;
import com.orangeHRM.utilities.DataProviders;
import com.orangeHRM.utilities.ExtentManager;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import com.orangeHRM.pages.HomePage;
import com.orangeHRM.pages.LoginPage;

import java.io.IOException;
import java.lang.reflect.Method;

public class HomePageTest extends BaseClass {
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
    public void verifyOrangeHRMLogo(String username,String password) throws IOException {
        //ExtentManager.startTest("Verify orangeHRM Logo");->HANDLED BY TEST LISTNER
        ExtentManager.logStep("Navigating to login page entering username and password");
        loginPage.login(username,password);
        ExtentManager.logStep("verifying logo is visible or not");
        Assert.assertTrue(homePage.verifyOrangeHRMLogo(),"logo is not visible");
        ExtentManager.logStep("validation successful");
        ExtentManager.logStep("logged out successfully");
    }
}
