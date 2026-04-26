package test;

import com.orangeHRM.base.BaseClass;
import com.orangeHRM.utilities.ExtentManager;
import org.testng.Assert;
import org.testng.annotations.Test;

public class Dummy2Test extends BaseClass {
    @Test
    public void dummyTest(){
//        ExtentManager.startTest("Dummy2 Test");->HANDLED BY LISTNER
        String title =  getDriver().getTitle();
        ExtentManager.logStep("Verifying the title");
        Assert.assertTrue(title.equalsIgnoreCase("orangeHRM"), "its not equal");
        System.out.println("test passed");

        ExtentManager.logStep("validation successful");
    }
}
