package test;

import com.orangeHRM.base.BaseClass;
import com.orangeHRM.utilities.ExtentManager;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.Test;

public class DummyTest extends BaseClass {
    @Test
    public void dummyTest(){
       // ExtentManager.startTest("Dummy Test");->HANDLED BY TEST LISTNER
       String title =  getDriver().getTitle();
       ExtentManager.logStep("verifying the title");
        Assert.assertTrue(title.equalsIgnoreCase("orangeHRM"), "its not equal");
        System.out.println("test passed");
        ExtentManager.logSkip("this is skipped");
        throw new SkipException("Skipping the test as part of testing");
    }
}
