package com.test.flightreserve;

import com.ajayc20.pages.flight.registerpage.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class BookFlightTicket {

    WebDriver driver;

    @BeforeTest
    public void setDriver() {
        this.driver = new ChromeDriver();
        driver.manage().window().maximize();

    }

    @Test
    public void registrationPage() {

        RegisterPage registerPage = new RegisterPage(driver);
        registerPage.getURL("https://d1uh9e7cu07ukd.cloudfront.net/selenium-docker/reservation-app/index.html#");
        Assert.assertTrue(registerPage.isDataVisible());
        registerPage.enterUsername("Ajay", "C");
        registerPage.enterUserEmailPassword("selenium@docker.com", "Platform.1");
        registerPage.enterAddressDetails("No 01 Chennai", "Pattabiram", "600000");
        registerPage.selectState("Indiana");
        registerPage.clickRegButton();

    }

    @Test(dependsOnMethods = "registrationPage")
    public void registrationPageConformation() {
        RegisterConformationPage registerConformationPage = new RegisterConformationPage(driver);
        Assert.assertTrue(registerConformationPage.isDataVisible());
        registerConformationPage.clickGoToButton();


    }

    @Test(dependsOnMethods = "registrationPageConformation")
    public void flightSearch() {
        FlightSearchPage flightSearchPage = new FlightSearchPage(driver);
        Assert.assertTrue(flightSearchPage.isDataVisible());
        flightSearchPage.selectDropDownPassanger("1");
        flightSearchPage.selectRouteAndDepaturs();
        flightSearchPage.selectServiceClass();
        flightSearchPage.clickSelectFlightButton();
    }

    @Test(dependsOnMethods = "flightSearch")
    public void flightSelect() {
        FlightSelectPage flightSelectPage = new FlightSelectPage(driver);
        Assert.assertTrue(flightSelectPage.isDataVisible());
        flightSelectPage.selectEmiratesEconomy();
        flightSelectPage.clickConformButton();
    }

    @Test(dependsOnMethods = "flightSelect")
    public void flightConformation() {
        FlightBookedConformationPage conformationPage = new FlightBookedConformationPage(driver);
        conformationPage.isDataVisible();
        conformationPage.conformationNumber();
        Assert.assertEquals(conformationPage.conformationAmount(), "$584 USD");

    }
}
