package com.orangeHRM.pages;

import com.orangeHRM.actiondriver.ActionDriver;
import com.orangeHRM.base.BaseClass;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.io.IOException;

public class LoginPage {

    private ActionDriver actionDriver;
    //define locators using By
    private By userNameField = By.name("username");
    private By passwordField = By.name("password");
    private By loginButton = By.tagName("button");
    private By errorMessage = By.xpath("//p[text()='Invalid credentials']");

    public LoginPage(WebDriver driver){
       this.actionDriver =  BaseClass.getActionDriver();
    }

    //method to perfom login

    public void login(String userName,String password) throws IOException {
        actionDriver.enterText(userNameField,userName);
        actionDriver.enterText(passwordField,password);
        actionDriver.click(loginButton);

        actionDriver.waitForPageLoad(10);
    }

    //method to check if error message is diplayed
    public boolean errorMessageDisplayed() throws IOException {
       return actionDriver.isDisplayed(errorMessage);
    }
    // method to get the text from error message
    public String getErrorMessage(){
        return actionDriver.getText(errorMessage);
    }
    // verify if error is correct or not

    public boolean verifyErrorMessage(String expected){
        String actual = getErrorMessage();
        return actual != null && actual.contains(expected);
    }
}
