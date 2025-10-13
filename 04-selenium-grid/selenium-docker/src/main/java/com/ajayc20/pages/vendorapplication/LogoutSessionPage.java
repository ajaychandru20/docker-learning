package com.ajayc20.pages.vendorapplication;

import com.ajayc20.pages.AbstractPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class LogoutSessionPage extends AbstractPage {
    @FindBy(xpath = "//a[@id='userDropdown']/img")
    private WebElement profileIcon;

    @FindBy(linkText = "Logout")
    private WebElement logOutButton;

    @FindBy(css = "#logoutModal a")
    private WebElement logOutBox;

    @FindBy(xpath = "//*[button]/a")
    private WebElement logOutButtonConformation;


    public LogoutSessionPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isDataVisible() {
        this.wait.until(ExpectedConditions.visibilityOf(profileIcon));
        return profileIcon.isDisplayed();
    }

    public void logoutSession() {
        this.wait.until(ExpectedConditions.visibilityOf(profileIcon));
        this.wait.until(ExpectedConditions.elementToBeClickable(profileIcon));
        profileIcon.click();
        this.wait.until(ExpectedConditions.visibilityOf(logOutButton));
        this.wait.until(ExpectedConditions.elementToBeClickable(logOutButton));
        logOutButton.click();

        this.wait.until(ExpectedConditions.visibilityOf(logOutBox));
        this.wait.until(ExpectedConditions.elementToBeClickable(logOutBox));
        logOutBox.click();

    }
}
