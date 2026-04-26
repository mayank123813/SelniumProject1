package com.orangeHRM.utilities;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ExtentManager {
    private static ExtentReports extent;
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();
    private static Map<Long, WebDriver> driverMap = new HashMap<>();

    // initialize  the extent report
    public synchronized static ExtentReports getReporter(){
        if(extent==null){
            String reportPath = System.getProperty("user.dir")+"/src/test/resources/ExtentReports/ExtentReports.html";
          ExtentSparkReporter sparkReporter =   new ExtentSparkReporter(reportPath);
          sparkReporter.config().setReportName("Automation Test Report");
          sparkReporter.config().setDocumentTitle("orangeHRM Report");
          sparkReporter.config().setTheme(Theme.DARK);

          // object for initializing it
            extent=new ExtentReports();
            extent.attachReporter(sparkReporter);
            //add system info
            extent.setSystemInfo("Operating system",System.getProperty("os.name"));
            extent.setSystemInfo("Java version",System.getProperty("java.version"));
            extent.setSystemInfo("User Name",System.getProperty("user.name"));
        }

        return extent;

    }
    //start test
    public synchronized static  ExtentTest startTest(String testName){
        ExtentTest extentTest=getReporter().createTest(testName);
        test.set(extentTest);
        return extentTest;
    }
    // end a test
    public synchronized static void endTest(){
        getReporter().flush();
    }

    // get current threads test
    public synchronized static ExtentTest getTest(){
       return test.get();
    }

    // method to get the name of the current test
    public static String getTestName(){
       ExtentTest currentTest = getTest();
       if(currentTest!=null){
         return   currentTest.getModel().getName(); // in get model test info is stored (name,logs etc)
       }
       else{
           return "no test is currently active for test";
       }
    }
    // log a step
    public static void logStep(String logMessage){
        getTest().info(logMessage);
    }
    // log a step with screenshot
    public static void loginStepWithScreenshot(WebDriver driver,String logMessage,String ScreenShotMessage) throws IOException {
        getTest().pass(logMessage);
        // screenshot method
        attachScreenshot(driver,ScreenShotMessage);
    }
    public static void loginStepValidationForAPI(String logMessage) {
        getTest().pass(logMessage);
    }


    // log a failure
    public static void logFailure(WebDriver driver,String logMessage,String ScreenShotMessage) throws IOException {
        String colorMessage = String.format("<span style='color:red'>%s</span>",logMessage);
        getTest().fail(colorMessage);

        //screenshot method
        attachScreenshot(driver,ScreenShotMessage);

    }

    // log a failure of api
    public static void logFailureAPI(String logMessage) {
        String colorMessage = String.format("<span style='color:red'>%s</span>",logMessage);
        getTest().fail(colorMessage);
    }


    // log a skip
    public static void logSkip(String logMessage){
        String colorMessage = String.format("<span style='color:orange'>%s</span>",logMessage);
        getTest().skip(colorMessage);
    }
    // method for screenshot with date and time
    public synchronized static String takeScreenshot(WebDriver driver,String screenshotName) throws IOException {
        TakesScreenshot ts = (TakesScreenshot)driver;
       File src =  ts.getScreenshotAs(OutputType.FILE);
       // format date and time for file name
        String timeStamp=new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
        //Saving screenshot to file
        String desPath = System.getProperty("user.dir")+"/src/test/resources/screenshots/"
                +screenshotName+"_"+timeStamp+".png";
       File des =  new File(desPath);
        FileUtils.copyFile(src,des);
        //convert screenshot to base64 for embedding in report
       String base64Format = convertToBase64(src);
       return base64Format;

    }
    // method for conversion of screenshot to base64 format
    public static String convertToBase64(File screenshotFile) throws IOException {
        String base64Format = "";
        //read the file content into a byte array
        byte[] fileContent = FileUtils.readFileToByteArray(screenshotFile);
        base64Format= Base64.getEncoder().encodeToString(fileContent);
        return base64Format;
    }
    // attach screenshot to report using base64
    public synchronized static void attachScreenshot(WebDriver driver,String message) throws IOException {
        String screenshotBase64=takeScreenshot(driver,getTestName());
        getTest()
                .info(message,com.aventstack.extentreports.MediaEntityBuilder.createScreenCaptureFromBase64String(screenshotBase64).build());


    }

    //register WebDriver for current thread
    public static void registerDriver(WebDriver driver){

        driverMap.put(Thread.currentThread().threadId(),driver);
    }
}
