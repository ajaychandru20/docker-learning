package com.ajayc20.pages.flight.registerpage;

import com.ajayc20.pages.AbstractPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FlightBookedConformationPage extends AbstractPage {

    private static final Logger logger = LoggerFactory.getLogger(FlightBookedConformationPage.class);

    @FindBy(css = "#flights-confirmation-section .card-body .row:nth-child(1) .col:nth-child(2) p")
    private WebElement conformationNumber;

    @FindBy(css = "#flights-confirmation-section .card-body .row:nth-child(3) .col:nth-child(2) p")
    private WebElement finalAmount;

    public WebElement getConformationNumber() {
        return conformationNumber;
    }

    public WebElement getFinalAmount() {
        return finalAmount;
    }


    @Override
    public boolean isDataVisible() {
        this.wait.until(ExpectedConditions.visibilityOf(getConformationNumber()));
        return getConformationNumber().isDisplayed();
    }

    public String conformationNumber() {
        logger.info("Conformation Number: {}", getConformationNumber().getText());
        return getConformationNumber().getText();
    }

    public String conformationAmount() {
        logger.info("Conformation Amount is: {}", getFinalAmount().getText());
        return getFinalAmount().getText();
    }

    public FlightBookedConformationPage(WebDriver driver) {

        super(driver);
    }

}
