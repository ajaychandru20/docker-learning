package com.ajayc20.pages.flightreservation;

import com.ajayc20.pages.AbstractPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

public class FlightSearchPage extends AbstractPage {

    @FindBy(id = "oneway")
    private WebElement selectRadioOneWay;

    @FindBy(id = "passengers")
    private WebElement selectPassengerDropDown;

    @FindBy(id = "depart-from")
    private WebElement selectDepartmentFrom;

    @FindBy(id = "arrive-in")
    private WebElement selectArriveIn;

    @FindBy(id = "service-class1")
    private WebElement serviceClassEconomy;

    @FindBy(id = "search-flights")
    private WebElement selectFlightsButton;


    public WebElement getSelectRadioOneWay() {
        return selectRadioOneWay;
    }

    public WebElement getSelectPassengerDropDown() {
        return selectPassengerDropDown;
    }

    public WebElement getSelectDepartmentFrom() {
        return selectDepartmentFrom;
    }

    public WebElement getSelectArriveIn() {
        return selectArriveIn;
    }

    public WebElement getServiceClassEconomy() {
        return serviceClassEconomy;
    }

    public WebElement getSelectFlightsButton() {
        return selectFlightsButton;
    }

    public FlightSearchPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isDataVisible() {
        this.wait.until(ExpectedConditions.visibilityOf(this.getSelectFlightsButton()));
        return getSelectFlightsButton().isDisplayed();
    }

    public void selectDropDownPassanger(String value) {
        Select select = new Select(this.getSelectPassengerDropDown());
        select.selectByValue(value);
    }

    public void selectRouteAndDepaturs() {
        getSelectRadioOneWay().click();
        Select selectDep = new Select(this.getSelectDepartmentFrom());
        selectDep.selectByValue("London");

        Select selectArr = new Select(this.getSelectArriveIn());
        selectArr.selectByValue("London");
    }

    public void selectServiceClass() {
        getServiceClassEconomy().click();
    }

    public void clickSelectFlightButton() {
        this.getSelectFlightsButton().click();
    }

}
