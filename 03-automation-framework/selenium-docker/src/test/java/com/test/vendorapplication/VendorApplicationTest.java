package com.test.vendorapplication;

import com.ajayc20.pages.vendorapplication.LoginPage;
import com.ajayc20.pages.vendorapplication.DashboardWidgetsPage;
import com.ajayc20.pages.vendorapplication.DashboardTablePage;
import com.ajayc20.pages.vendorapplication.LogoutSessionPage;

import static com.ajayc20.pages.vendorapplication.HandelPassConformWindowsAlert.*;

import com.test.AbstractTest;
import com.test.vendorapplication.resourceloader.VendorTestDataParameters;
import com.utils.JsonLoader;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;


public class VendorApplicationTest extends AbstractTest {

    private LoginPage loginPage;
    private DashboardWidgetsPage dashboardWidgetsPage;
    private DashboardTablePage dashboardTablePage;
    private LogoutSessionPage logoutSessionPage;
    private VendorTestDataParameters testData;

    @BeforeClass
    @Parameters("testDataPath")
    public void initWebDriverOnPages(String testDataPath) {
        this.testData = JsonLoader.getTestData(testDataPath, VendorTestDataParameters.class);
        this.loginPage = new LoginPage(this.driver);
        this.dashboardWidgetsPage = new DashboardWidgetsPage(this.driver);
        this.dashboardTablePage = new DashboardTablePage(this.driver);
        this.logoutSessionPage = new LogoutSessionPage(this.driver);
    }

    @Test
    public void loginPage() {
        loginPage.visitApplication("https://d1uh9e7cu07ukd.cloudfront.net/selenium-docker/vendor-app/index.html");
        Assert.assertTrue(loginPage.isDataVisible());
        loginPage.loginIntoPage(testData.username(), testData.password());
    }

    @Test(dependsOnMethods = "loginPage")
    public void checkDashboardWidgets() {
        Assert.assertTrue(dashboardWidgetsPage.isDataVisible());
        dashboardWidgetsPage.validateMonthlyEarningCard(testData.monthlyEarning());
        dashboardWidgetsPage.validateAnnualEarningCard(testData.annualEarning());
        dashboardWidgetsPage.validateProfitMargin(testData.profitMargin());
        dashboardWidgetsPage.validateAvailableInventory(testData.availableInventory());
    }

    @Test(dependsOnMethods = "checkDashboardWidgets")
    public void checkDashboardTable() {
        Assert.assertTrue(dashboardTablePage.isDataVisible());
        dashboardTablePage.validateSearchBox(testData.searchKeyword());
        dashboardTablePage.validateFinalCount(testData.searchResultsCount());
    }

    @Test(dependsOnMethods = "checkDashboardTable")
    public void logOutSession() {
        handelPasswordWindowAlert(this.driver);
        Assert.assertTrue(logoutSessionPage.isDataVisible());
        logoutSessionPage.logoutSession();

    }

}
