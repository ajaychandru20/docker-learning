package com.test;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;

import java.net.MalformedURLException;
import java.net.URL;

public class AbstractTest {
    protected WebDriver driver;

    @BeforeTest
    public void setDriver() throws MalformedURLException {

        Boolean status = Boolean.getBoolean("selenium.grid.enabled");
        if (status) {
            this.driver = launchRemoteBrowser();
        } else {
            this.driver = launchLocalBrowser();
        }
        this.driver.manage().window().maximize();
    }

    private WebDriver launchLocalBrowser() {
        return this.driver = new ChromeDriver();
    }

    private WebDriver launchRemoteBrowser() throws MalformedURLException {
        Capabilities capabilities;
        if (System.getProperty("browser").equalsIgnoreCase("chrome")) {
            capabilities = new ChromeOptions();
        } else {
            capabilities = new FirefoxOptions();
        }
        return new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), capabilities);
    }

    @AfterTest
    public void quitBrowsers() {
        this.driver.quit();
    }

}
