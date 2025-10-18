package com.utils.listener;

import com.utils.Constant;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.Reporter;

public class ListenerTestNG implements ITestListener {

    @Override
    public void onTestFailure(ITestResult result) {
        TakesScreenshot driver = (TakesScreenshot) result.getTestContext().getAttribute(Constant.DRIVER);
        String screenshot = driver.getScreenshotAs(OutputType.BASE64);
        String htmlFormat = "<img width=700px src='data:image/png;base64,%s' />";
        String htmlImage = String.format(htmlFormat, screenshot);
        Reporter.log(htmlImage);
    }
}
