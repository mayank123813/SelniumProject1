package com.orangeHRM.utilities;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer {

    private static int count=0;
    private final int maxCount=2;


    @Override
    public boolean retry(ITestResult iTestResult) {
        if(count<maxCount){
            count++;
            return true;
        }
        return false;
    }
}
