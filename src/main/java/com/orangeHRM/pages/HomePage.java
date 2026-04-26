package com.orangeHRM.pages;

import com.orangeHRM.actiondriver.ActionDriver;
import com.orangeHRM.base.BaseClass;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.time.Duration;

public class HomePage {
    private ActionDriver actionDriver;
    private By adminTab = By.xpath("//a[contains(@href,'admin')]");
    private By userIdButton = By.cssSelector(".oxd-userdropdown-tab");
    private By logoutButton = By.xpath("//a[text()='Logout']");
    private By logoImage = By.xpath("//img[contains(@src,'orangehrm-logo')]");
    private final By pimTab = By.xpath("//span[text()='PIM']");
    private final By employeeSearch = By.xpath("//label[text()='Employee Name']//parent::div/following-sibling::div/div//input");
    private By searchBtn = By.xpath("//button[@type='submit']");
    private By empFirstAndMiddleName=By.xpath("//div[@class='oxd-table-card']/div/div[3]");
    private By empLastName=By.xpath("//div[@class='oxd-table-card']/div/div[4]");



    public HomePage(WebDriver driver){
        this.actionDriver= BaseClass.getActionDriver();
    }


    public void waitForSidebar() {
        WebDriverWait wait=new WebDriverWait(actionDriver.getDriver(), Duration.ofSeconds(2));
        wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//aside")
        ));
    }

    // to verify if admin in visible
    public boolean isAdminTabVisible() {
        try {
            actionDriver.waitForSidebar();   // 🔥 NEW
            return actionDriver.isPresent(adminTab); // 🔥 NEW
        } catch (Exception e) {
            return false;
        }
    }

    public boolean verifyOrangeHRMLogo() {
        try {
            actionDriver.waitForSidebar();
            return actionDriver.isPresent(logoImage);
        } catch (Exception e) {
            return false;
        }
    }

    //method for logout operation
    public void logout() throws IOException {
        actionDriver.click(userIdButton);
        actionDriver.click(logoutButton);
    }

    //method to navigate to pim tab
    public void clickOnPIMTab(){
        try {
            actionDriver.click(pimTab);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    //employee search
    public void employeeSearch(String value){
        actionDriver.enterText(employeeSearch,value);
        try {
            actionDriver.click(searchBtn);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        actionDriver.scrollToElement(empFirstAndMiddleName);
    }
    //verify employee first and middle name
    public boolean verifyEmpFirstAndMiddleName(String empFirstAndMiddleNameFromDb){
       return actionDriver.compareText(empFirstAndMiddleName,empFirstAndMiddleNameFromDb);
    }
    // verify employee last name
    public boolean verifyEmpLastName(String empLastNameFromDb){
        return actionDriver.compareText(empLastName,empLastNameFromDb);
    }
}
