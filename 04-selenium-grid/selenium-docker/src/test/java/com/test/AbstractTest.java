package com.test;

import com.google.common.util.concurrent.Uninterruptibles;
import com.utils.Config;
import com.utils.Constant;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

public class AbstractTest {
    protected WebDriver driver;

    private static Logger logger = LoggerFactory.getLogger(AbstractTest.class);

    @BeforeSuite
    public void configInit() {
        Config.initConfigProperties();
    }

    @BeforeTest
    public void setDriver() throws MalformedURLException {

        this.driver = Boolean.parseBoolean(Config.get(Constant.SELENIUM_GRID_ENABLED)) ? launchRemoteBrowser() : launchLocalBrowser();
        this.driver.manage().window().maximize();
    }


    private WebDriver launchRemoteBrowser() throws MalformedURLException {
        Capabilities capabilities = new ChromeOptions();

        if (Constant.FIREFOX_BROWSER.equalsIgnoreCase(Config.get(Constant.BROWSER))) {
            capabilities = new FirefoxOptions();
        }

        String urlFormat = Config.get(Constant.SELENIUM_GRID_URL_FORMAT);
        String urlHub = Config.get(Constant.SELENIUM_HUB_FORMAT);
        String completeURL = String.format(urlFormat, urlHub);
        logger.info(completeURL);
        return new RemoteWebDriver(new URL(completeURL), capabilities);
    }
    private WebDriver launchLocalBrowser() {
        return this.driver = new ChromeDriver();
    }

    @AfterTest
    public void quitBrowsers() {
        this.driver.quit();
    }
    @AfterMethod
    public void waitForTestMethods(){
        Uninterruptibles.sleepUninterruptibly(Duration.ofSeconds(2));
    }

}
