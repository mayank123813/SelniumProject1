package com.orangeHRM.base;

import com.orangeHRM.actiondriver.ActionDriver;
import com.orangeHRM.utilities.ExtentManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.annotations.*;
import com.orangeHRM.utilities.LoggerManager;
import org.testng.asserts.SoftAssert;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

public class BaseClass {

    protected static Properties prop;
//   protected WebDriver driver;
//   private static ActionDriver actionDriver;

   private static ThreadLocal<WebDriver> driver=new ThreadLocal<>();
   private static ThreadLocal<ActionDriver> actionDriver = new ThreadLocal<>();
    public static final Logger logger =  LoggerManager.getLogger(BaseClass.class);
    ThreadLocal<SoftAssert> softAssert=ThreadLocal.withInitial(SoftAssert::new);
    public SoftAssert getSoftAssert(){
        return softAssert.get();
    }
    //load configuration file
    @BeforeSuite
   public void loadConfig() throws IOException{
       prop =  new Properties();
       FileInputStream fis =  new FileInputStream
               (System.getProperty("user.dir")+"/src/main/resources/config.properties");
       prop.load(fis);
       logger.info("config.properties file loaded");

       // start the extent report
//        ExtentManager.getReporter(); ->implemented in test listner
   }
    //initialize the webdriver based on browser defined in config.properties
    @Parameters("browser")
   public void launchBrowser(){
       String browser =  prop.getProperty("browser");
       if(browser.equalsIgnoreCase("chrome")){
//           driver=new ChromeDriver();
           ChromeOptions options = new ChromeOptions();
           options.addArguments("--start-maximized");
          options.addArguments("--headless=new");
           options.addArguments("--disable-gpu");
           options.addArguments("--window-size=1920,1080");
           options.addArguments("--disable-notifications");
           options.addArguments("--disable-shm-dev-usage");
           driver.set(new ChromeDriver(options));
           ExtentManager.registerDriver(getDriver());
           logger.info("chromdriver object created");
       }
       else if(browser.equalsIgnoreCase("firefox")){
           FirefoxOptions options = new FirefoxOptions();
           options.addArguments("--headless");
           options.addArguments("--disable-gpu");
           options.addArguments("--window-size=1920,1080");
           options.addArguments("--disable-notifications");
           options.addArguments("--disable-shm-dev-usage");
           driver.set(new FirefoxDriver(options));
           ExtentManager.registerDriver(getDriver());
           logger.info("firefoxdriver object created");
       }
       else if(browser.equalsIgnoreCase("edge")){
           EdgeOptions options = new EdgeOptions();
           options.addArguments("--headless");
           options.addArguments("--disable-gpu");
           options.addArguments("--window-size=1920,1080");
           options.addArguments("--disable-notifications");
           options.addArguments("--disable-shm-dev-usage");
           driver.set(new EdgeDriver(options));
           ExtentManager.registerDriver(getDriver());
           logger.info("edge driver instance created");
       }
       else{
           throw new IllegalArgumentException("browser not supported"+browser);
       }
   }

   public void loadProperties(){
       int implicitWait = Integer.parseInt(prop.getProperty("implicitWait"));
       getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));
       getDriver().manage().window().maximize();

       try {
           getDriver().get(prop.getProperty("url"));
       } catch (Exception e) {
           logger.error("url not correct:"+e.getMessage());

       }
   }
   public void staticWait(int sec){
       LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(sec));
   }

  public WebDriver getDriver(){
        return driver.get();
   }
//
//   public void setDriver(WebDriver driver){
//        this.driver=driver;
//   }
    // get action driver
    public static ActionDriver getActionDriver(){
        if(actionDriver==null){
            System.out.println("action driver not intialized");
            throw new IllegalStateException("driver not intislized");
        }
        return actionDriver.get();
    }

   public static Properties getProp(){
        return prop;
   }

   @BeforeMethod
    public void setup() throws IOException {
        launchBrowser();
        loadProperties();
        staticWait(3);
        logger.info("webdriver initialized and browser maximized");
        logger.trace("this i trace message");
        logger.error("this is error message");
        logger.debug("this is debug message");
        logger.fatal("thus is fatal message");
        logger.warn("this is a warn message");

//        if(actionDriver==null){
//            actionDriver=new ActionDriver(driver);
//            logger.info("action driver object created:"+Thread.currentThread().threadId());
//        }
       // initialize action driver for current thread
       actionDriver.set(new ActionDriver(getDriver()));
       logger.info("action driver  initialized for thread:"+Thread.currentThread().threadId());

    }
    //closing
    @AfterMethod
    public void tearDown(){
        try {
            if(driver!=null){
                getDriver().quit();
            }
        } catch (Exception e) {
            logger.error("unable to quit:"+e.getMessage());
        }
        driver.remove();
        actionDriver.remove();
//        ExtentManager.endTest();->implemented in test listner class
    }
}
