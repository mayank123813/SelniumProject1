package test;

import com.orangeHRM.utilities.APIUtility;
import com.orangeHRM.utilities.ExtentManager;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;

public class APITest {
    @BeforeMethod
    public void setup(Method method) {
        ExtentManager.startTest(method.getName());  // 🔥 ADD HERE
    }
    @Test
    public void verifyGetRequest(){
        String endpoint = "https://jsonplaceholder.typicode.com/users/1";
        ExtentManager.logStep("API endpoint"+endpoint);

        //send get request
        ExtentManager.logStep("send get request to api endpoint");
       Response response =  APIUtility.sendGetRequest(endpoint);

        // validate status code from the response
        ExtentManager.logStep("validating the status code from response");
        boolean validateResponseStatusCode = APIUtility.validateResponseStatusCode(response,200);
        Assert.assertTrue(validateResponseStatusCode,"status code is not equal");
        if(validateResponseStatusCode){
            ExtentManager.loginStepValidationForAPI("status code validation successful");
        }
        else{
            ExtentManager.logFailureAPI("status code validation not successful");
        }

        //validate username
        ExtentManager.logStep("validating username from response");
        String userName = APIUtility.getJsonValue(response,"username");
        boolean ifUserNameEquals="Bret".equals(userName);
        Assert.assertTrue(ifUserNameEquals,"username not equal");

        if(ifUserNameEquals){
            ExtentManager.loginStepValidationForAPI("username is equal");
        }
        else{
            ExtentManager.logFailureAPI("username not equal");
        }
    }
}
