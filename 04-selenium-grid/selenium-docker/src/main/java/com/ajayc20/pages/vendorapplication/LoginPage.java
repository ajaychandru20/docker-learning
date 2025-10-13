package com.ajayc20.pages.vendorapplication;

import com.ajayc20.pages.AbstractPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class LoginPage extends AbstractPage {

    @FindBy(id = "username")
    private WebElement userNameField;

    @FindBy(id = "password")
    private WebElement passwordField;

    @FindBy(id = "login")
    private WebElement loginButton;

    public LoginPage(WebDriver driver) {
        super(driver);
    }


    public void visitApplication(String url) {
        this.driver.get(url);
    }

    public void loginIntoPage(String userName, String passWord) {
        userNameField.sendKeys(userName);
        passwordField.sendKeys(passWord);
        loginButton.click();
    }

    @Override
    public boolean isDataVisible() {
        this.wait.until(ExpectedConditions.visibilityOf(loginButton));
        return loginButton.isDisplayed();
    }
}
