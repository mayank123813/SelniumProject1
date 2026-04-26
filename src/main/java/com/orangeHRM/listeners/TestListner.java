package com.orangeHRM.listeners;

import com.orangeHRM.base.BaseClass;
import com.orangeHRM.utilities.ExtentManager;
import com.orangeHRM.utilities.RetryAnalyzer;
import org.testng.*;
import org.testng.annotations.ITestAnnotation;
import org.testng.annotations.Test;
import org.testng.internal.annotations.IAnnotationTransformer;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class TestListner implements ITestListener, IAnnotationTransformer {

    @Override
    public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
               annotation.setRetryAnalyzer(RetryAnalyzer.class);
    }



    //TRIGGERED WHEN TEST SUCCEED
    @Override
    public void onTestSuccess(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        try {
            if(!result.getTestClass().getName().toLowerCase().contains("api")){
                ExtentManager.loginStepWithScreenshot(BaseClass.getActionDriver().getDriver(), "test passed successfully:"+testName,"test name:"+testName+" "+"test passed");
            }
            else{
                ExtentManager.loginStepValidationForAPI("test name:"+testName+" "+"test passed");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // trigger when suite ends
    @Override
    public void onFinish(ITestContext context) {
        //flush the report
        ExtentManager.endTest();
    }
     // this will trigger when suite start
    @Override
    public void onStart(ITestContext context) {
        // initialize the extent reports
        ExtentManager.getReporter();
    }
// triggered when test fails
    @Override
    public void onTestFailure(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String message = result.getThrowable().getMessage();
        ExtentManager.logStep(message);
        try {
            if(!result.getTestClass().getName().toLowerCase().contains("api")){
               ExtentManager.logFailure(BaseClass.getActionDriver().getDriver(),"test is not passed","test name:"+testName+" "+"test failed");
           }
           else {
                ExtentManager.logFailureAPI("test name:"+testName+" "+"test failed");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    // trigger when test start
    @Override
    public void onTestStart(ITestResult result) {
        String testName=result.getMethod().getMethodName();
        // start logging in extent reports
        ExtentManager.startTest(testName);

    }
    // when test skips
    @Override
    public void onTestSkipped(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        ExtentManager.logSkip("test skipped:"+testName);
    }

}
