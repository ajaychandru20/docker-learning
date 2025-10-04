package com.ajayc20.pages.flight.registerpage;

import com.ajayc20.pages.AbstractPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class FlightSelectPage extends AbstractPage {

    @FindBy(id = "dep-emirates-economy")
    private WebElement depEmiratesEconomy;


    @FindBy(id = "arr-emirates-economy")
    private WebElement arrEmiratesEconomy;


    @FindBy(id = "confirm-flights")
    private WebElement conformFlightButton;

    public WebElement getDepEmiratesEconomy() {
        return depEmiratesEconomy;
    }

    public WebElement getArrEmiratesEconomy() {
        return arrEmiratesEconomy;
    }

    public WebElement getConformFlightButton() {
        return conformFlightButton;
    }


    @Override
    public boolean isDataVisible() {
        this.wait.until(ExpectedConditions.visibilityOf(getConformFlightButton()));
        return getConformFlightButton().isDisplayed();
    }

    public FlightSelectPage(WebDriver driver) {
        super(driver);
    }

    public void selectEmiratesEconomy() {
        this.getDepEmiratesEconomy().click();
        this.getArrEmiratesEconomy().click();
    }

    public void clickConformButton() {
        this.getConformFlightButton().click();
    }
}
