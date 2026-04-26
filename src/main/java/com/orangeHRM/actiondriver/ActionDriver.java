package com.orangeHRM.actiondriver;

import com.orangeHRM.base.BaseClass;
import com.orangeHRM.utilities.ExtentManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.time.Duration;

public class ActionDriver extends BaseClass {
    private WebDriver driver;
    private WebDriverWait wait;
    public static final Logger logger = BaseClass.logger;


    public ActionDriver(WebDriver driver){
        this.driver=driver;
       int explicitWait= Integer.parseInt(BaseClass.getProp().getProperty("explicitWait"));
        this.wait=new WebDriverWait(driver, Duration.ofSeconds(explicitWait));
    }

    public void waitForElementToBeClickable(By by){
        try {
            wait.until(ExpectedConditions.elementToBeClickable(by));
        } catch (Exception e) {
            logger.error("element not clickable:"+e.getMessage());
        }
    }

    // wait for element to be visiblity of element
    public void waitElementToBePresent(By by){
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(by));
        } catch (Exception e) {
            logger.error("element not visible:"+e.getMessage());
        }
    }

    //method to click an element
    public void click(By by) throws IOException {
        String elementDescription = getElementDescription(by);
        try {
            waitForElementToBeClickable(by);
            driver.findElement(by).click();
            ExtentManager.logStep("clicked an element:"+elementDescription);
            logger.info("clicked an element:"+elementDescription);
        } catch (Exception e) {
            logger.error("not able to click:"+e.getMessage());
            ExtentManager.logFailure(getDriver(),"unable to click",elementDescription+"unable to click");
        }
    }

    //method to enter text in input feild
    public void enterText(By by,String value){
        try {
            waitElementToBePresent(by);
            driver.findElement(by).clear();
            driver.findElement(by).sendKeys(value);
        } catch (Exception e) {
            logger.error("not able to enter:"+e.getMessage());
        }
    }
    //method to get text from input field
    public String getText(By by){
        try {
            waitElementToBePresent(by);
            return driver.findElement(by).getText();
        } catch (Exception e) {
            logger.error("unable to get text:"+e.getMessage());
            return "";
        }
    }

    public boolean compareText(By by,String expected){
        try {
            waitElementToBePresent(by);
            String actual = driver.findElement(by).getText();
            if(expected.equals(actual)){
                logger.info("actual text"+actual+"as well as"+expected+"are equal");
                ExtentManager.loginStepWithScreenshot(getDriver(),"compare text","test sucessfully verfied");
                return true;
            }
            else{
                logger.error("actual text"+actual+"as well as"+expected+"are not equal");
                ExtentManager.logFailure(getDriver(),"test comparision failed","both text not equal");
                return false;
            }
        } catch (Exception e) {
            logger.error("this string"+expected+"is not equal"+e.getMessage());

        }
        return false;
    }
    public boolean isDisplayed(By by) throws IOException {
       try {
           waitElementToBePresent(by);
           logger.info("element is displayed"+getElementDescription(by));
           ExtentManager.logStep("element is displayed:"+getElementDescription(by));
           ExtentManager.loginStepWithScreenshot(getDriver(),"element is displayed","element is displayed:"+getElementDescription(by));
            return driver.findElement(by).isDisplayed();
       } catch (Exception e) {
           logger.error("not displayed yet:"+e.getMessage());
           ExtentManager.logFailure(getDriver(),"element not displayed:"+getElementDescription(by),"Element is not displayed"+getElementDescription(by));
           return false;
       }
    }
    public void waitForPageLoad(int timeOutInSec){
        try {
            wait.withTimeout(Duration.ofSeconds(timeOutInSec)).until(WebDriver->((JavascriptExecutor)driver)
                    .executeScript("return document.readyState").equals("complete"));
        } catch (Exception e) {
            logger.error("page did not load within time:"+timeOutInSec);
        }
    }
    // method to get the description of element using By locator

    public String getElementDescription(By locator){
        try {
            if (driver == null) {
                return "driver is null";
            }
            if (locator == null) {
                return "locator is null";
            }

            // 🔥 SAFE CHECK (no exception)
            if (driver.findElements(locator).isEmpty()) {
                return "element not present: " + locator.toString();
            }

            WebElement element = driver.findElement(locator);

            String name = element.getDomAttribute("name");
            String id = element.getDomAttribute("id");
            String text = element.getText();
            String className = element.getDomAttribute("class");
            String placeholder = element.getDomAttribute("placeholder");

            if (isNotEmpty(name)) {
                return "element with name: " + name;
            } else if (isNotEmpty(id)) {
                return "element with id: " + id;
            } else if (isNotEmpty(text)) {
                return "element with text: " + truncate(text, 50);
            } else if (isNotEmpty(placeholder)) {
                return "element with placeholder: " + placeholder;
            } else if (isNotEmpty(className)) {
                return "element with class name: " + className;
            }

            return locator.toString();

        } catch (Exception e) {
            logger.error("unable to describe element: " + e.getMessage());
            return "unable to describe: " + locator.toString();
        }
    }
    // utility method to check strung is not null or empty
    private boolean isNotEmpty(String value){
        return value!=null && !value.isEmpty();
    }
    // utility method to truncate long string
    private String truncate(String value,int maxlength){
        if(value==null || value.length()<=maxlength){
            return value;
        }
        return value.substring(0,maxlength);
    }

    //scroll to an element
    public void scrollToElement(By by){
        try{
            JavascriptExecutor js = (JavascriptExecutor) driver;
            WebElement element= driver.findElement(by);
            js.executeScript("arguments[0].scrollIntoView(true);",element);
        } catch (Exception e) {
            logger.error("unable to locate element:"+e.getMessage());
        }
    }

    public void waitForSidebar() {
        logger.info("waiting for side bar to open");
        wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//aside")
        ));
    }

    public boolean isPresent(By by) {
        try {

            wait.until(ExpectedConditions.presenceOfElementLocated(by));

            return true;
        } catch (Exception e) {
            return false;
        }
    }



}
