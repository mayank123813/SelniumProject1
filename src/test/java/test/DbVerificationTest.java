package test;

import com.orangeHRM.base.BaseClass;
import com.orangeHRM.pages.HomePage;
import com.orangeHRM.pages.LoginPage;
import com.orangeHRM.utilities.DBConnection;
import com.orangeHRM.utilities.DataProviders;
import com.orangeHRM.utilities.ExtentManager;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Map;

public class DbVerificationTest extends BaseClass {
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
    @Test(dataProvider = "employeeVerification",dataProviderClass = DataProviders.class)
    public void verifyEmNameVerificationFromDb(String empId,String empName) throws IOException {
        SoftAssert softAssert=getSoftAssert();


        ExtentManager.logStep("loginh with admin credentials");
        loginPage.login(prop.getProperty("username"), prop.getProperty("password"));
        ExtentManager.logStep("click on pim tab");
        homePage.clickOnPIMTab();
        ExtentManager.logStep("search for employee");
        homePage.employeeSearch(empName);
        ExtentManager.logStep("get employee name from db");
//        String employee_id="1";
        //fetch the data into map

       Map<String,String> employeeDetails =  DBConnection.getEmployeeDetails(empId);
      String emp_firstName= employeeDetails.get("firstname");
      String emp_middleName= employeeDetails.get("middlename");
      String emp_lastName= employeeDetails.get("lastname");

      String emp_firstAndMiddleName = (emp_firstName+"test"+emp_middleName).trim();
      ExtentManager.logStep("verify first and middle name");
        softAssert.assertTrue(homePage.verifyEmpFirstAndMiddleName
                (emp_firstAndMiddleName),"first and middle name not matching");
      ExtentManager.logStep("verify last name");
      softAssert.assertTrue(homePage.verifyEmpLastName(emp_lastName),"last name not matching");
      ExtentManager.logStep("db validation complete");





    }
}
