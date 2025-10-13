package com.test.flightreserve;

import com.ajayc20.pages.flightreservation.*;
import com.test.AbstractTest;
import com.test.flightreserve.resourceloader.BookFlightTestDataParameters;
import com.utils.Config;
import com.utils.Constant;
import com.utils.JsonLoader;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class BookFlightTicket extends AbstractTest {

    private RegisterPage registerPage;
    private RegisterConformationPage registerConformationPage;
    private FlightSearchPage flightSearchPage;
    private FlightSelectPage flightSelectPage;
    private FlightBookedConformationPage flightBookedConformationPage;
    private BookFlightTestDataParameters bookFlightTestDataParameters;


    @BeforeClass
    @Parameters("testDataPath")
    private void initPagesDriver(String testDataPath) {
        this.bookFlightTestDataParameters = JsonLoader.getTestData(testDataPath, BookFlightTestDataParameters.class);
        this.registerPage = new RegisterPage(this.driver);
        this.registerConformationPage = new RegisterConformationPage(this.driver);
        this.flightSearchPage = new FlightSearchPage(this.driver);
        this.flightSelectPage = new FlightSelectPage(this.driver);
        this.flightBookedConformationPage = new FlightBookedConformationPage(this.driver);
    }

    @Test
    public void registrationPage() {

//        RegisterPage registerPage = new RegisterPage(driver);
        registerPage.getURL(Config.get(Constant.FLIGHT_BOOKING_URL));
        Assert.assertTrue(registerPage.isDataVisible());
        registerPage.enterUsername(bookFlightTestDataParameters.username(), bookFlightTestDataParameters.lastname());
        registerPage.enterUserEmailPassword(bookFlightTestDataParameters.email(), bookFlightTestDataParameters.password());
        registerPage.enterAddressDetails(bookFlightTestDataParameters.streetname(), bookFlightTestDataParameters.cityname(), bookFlightTestDataParameters.pincode());
        registerPage.selectState(bookFlightTestDataParameters.state());
        registerPage.clickRegButton();

    }

    @Test(dependsOnMethods = "registrationPage")
    public void registrationPageConformation() {
//        RegisterConformationPage registerConformationPage = new RegisterConformationPage(driver);
        Assert.assertTrue(registerConformationPage.isDataVisible());
        registerConformationPage.clickGoToButton();


    }

    @Test(dependsOnMethods = "registrationPageConformation")
    public void flightSearch() {
//        FlightSearchPage flightSearchPage = new FlightSearchPage(driver);
        Assert.assertTrue(flightSearchPage.isDataVisible());
        flightSearchPage.selectDropDownPassanger(bookFlightTestDataParameters.noOfPassanger());
        flightSearchPage.selectRouteAndDepaturs();
        flightSearchPage.selectServiceClass();
        flightSearchPage.clickSelectFlightButton();
    }

    @Test(dependsOnMethods = "flightSearch")
    public void flightSelect() {
//        FlightSelectPage flightSelectPage = new FlightSelectPage(driver);
        Assert.assertTrue(flightSelectPage.isDataVisible());
        flightSelectPage.selectEmiratesEconomy();
        flightSelectPage.clickConformButton();
    }

    @Test(dependsOnMethods = "flightSelect")
    public void flightConformation() {
//        FlightBookedConformationPage conformationPage = new FlightBookedConformationPage(driver);
        flightBookedConformationPage.isDataVisible();
        flightBookedConformationPage.conformationNumber();
        Assert.assertEquals(flightBookedConformationPage.conformationAmount(), bookFlightTestDataParameters.totalamount()); // 1 - "$584 USD"

    }
}
