package com.test.vendorapplication;

import com.ajayc20.pages.vendorapplication.DashboardTablePage;
import com.ajayc20.pages.vendorapplication.DashboardWidgetsPage;
import com.ajayc20.pages.vendorapplication.LoginPage;
import com.ajayc20.pages.vendorapplication.LogoutSessionPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class VendorApplicationTest {

    WebDriver driver;

    @BeforeTest
    public void initWebDriver() {
        this.driver = new ChromeDriver();
        driver.manage().window().maximize();
    }

    @Test
    public void loginPage() {
        driver.get("https://d1uh9e7cu07ukd.cloudfront.net/selenium-docker/vendor-app/index.html");
        LoginPage loginPage = new LoginPage(driver);
        Assert.assertTrue(loginPage.isDataVisible());
        loginPage.loginIntoPage("mike", "mike");
    }

    @Test(dependsOnMethods = "loginPage")
    public void checkDashboardWidgets() {
        DashboardWidgetsPage widgetsPage = new DashboardWidgetsPage(driver);
        Assert.assertTrue(widgetsPage.isDataVisible());
        widgetsPage.validateMonthlyEarningCard("$55,000");
        widgetsPage.validateAnnualEarningCard("$563,300");
        widgetsPage.validateProfitMargin("80%");
        widgetsPage.validateAvailableInventory("45");
    }

    @Test(dependsOnMethods = "checkDashboardWidgets")
    public void checkDashboardTable() {
        DashboardTablePage tablePage = new DashboardTablePage(driver);
        Assert.assertTrue(tablePage.isDataVisible());
        tablePage.validateSearchBox("miami");
        tablePage.validateFinalCount(10);
    }

    @Test(dependsOnMethods = "checkDashboardTable")
    public void logOutSession() {
        LogoutSessionPage logoutSessionPage = new LogoutSessionPage(driver);
        Assert.assertTrue(logoutSessionPage.isDataVisible());
        logoutSessionPage.logoutSession();

    }

}
