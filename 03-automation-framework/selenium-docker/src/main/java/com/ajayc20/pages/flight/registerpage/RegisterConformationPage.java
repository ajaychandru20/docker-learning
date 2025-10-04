package com.ajayc20.pages.flight.registerpage;

import com.ajayc20.pages.AbstractPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class RegisterConformationPage extends AbstractPage {

    @FindBy(id = "go-to-flights-search")
    private WebElement goToButton;

    public WebElement getGoToButton() {
        return goToButton;
    }

    public RegisterConformationPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isDataVisible() {
        this.wait.until(ExpectedConditions.visibilityOf(getGoToButton()));
        return getGoToButton().isDisplayed();
    }

    public void clickGoToButton() {

        this.getGoToButton().click();

    }


}
