package com.ajayc20.pages.flight.registerpage;

import com.ajayc20.pages.AbstractPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

public class RegisterPage extends AbstractPage {


    @FindBy(id = "firstName")
    private WebElement firstName;

    @FindBy(id = "lastName")
    private WebElement lastName;

    @FindBy(id = "email")
    private WebElement emailInput;

    @FindBy(id = "password")
    private WebElement passwordInput;

    @FindBy(name = "street")
    private WebElement streetName;


    @FindBy(name = "city")
    private WebElement cityInput;

    @FindBy(name = "zip")
    private WebElement zipInput;

    @FindBy(id = "inputState")
    private WebElement selectState;


    public WebElement getFirstName() {
        return firstName;
    }

    public WebElement getLastName() {
        return lastName;
    }

    public WebElement getEmailInput() {
        return emailInput;
    }

    public WebElement getPasswordInput() {
        return passwordInput;
    }

    public WebElement getStreetName() {
        return streetName;
    }

    public WebElement getCityInput() {
        return cityInput;
    }

    public WebElement getZipInput() {
        return zipInput;
    }

    public WebElement getSelectState() {
        return selectState;
    }

    public RegisterPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isDataVisible() {
        this.wait.until(ExpectedConditions.visibilityOf(getFirstName()));
        return this.getFirstName().isDisplayed();
    }

    public void getURL(String url) {
        this.driver.get(url);
    }

    public void enterUsername(String firstNAme, String lastNAme) {
        this.getFirstName().sendKeys(firstNAme);
        this.getLastName().sendKeys(lastNAme);
    }

    public void enterUserEmailPassword(String email, String password) {
        this.getEmailInput().sendKeys(email);
        this.getPasswordInput().sendKeys(password);
    }

    public void enterAddressDetails(String street_name, String city_name, String pincode) {

        this.getStreetName().sendKeys(street_name);
        this.getCityInput().sendKeys(city_name);
        this.getZipInput().sendKeys(pincode);
    }

    public void selectState(String textValue) {
        Select select = new Select(this.getSelectState());
        select.selectByValue(textValue);
    }

    public void clickRegButton() {
        this.driver.findElement(By.id("register-btn")).click();
    }

}
