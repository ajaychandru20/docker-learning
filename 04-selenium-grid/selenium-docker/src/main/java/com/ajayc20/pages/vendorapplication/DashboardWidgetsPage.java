package com.ajayc20.pages.vendorapplication;

import com.ajayc20.pages.AbstractPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;

public class DashboardWidgetsPage extends AbstractPage {

    @FindBy(css = "#content h1")
    private WebElement dashboardHeader;

    @FindBy(id = "monthly-earning")
    private WebElement monthlyEarningValue;

    @FindBy(id = "annual-earning")
    private WebElement annualEarningValue;

    @FindBy(id = "profit-margin")
    private WebElement profitMarginValue;

    @FindBy(id = "available-inventory")
    private WebElement availableInventoryValue;

    @Override
    public boolean isDataVisible() {
        this.wait.until(ExpectedConditions.textToBe(By.cssSelector("#content h1"), "Dashboard"));
        return dashboardHeader.isDisplayed();
    }

    public DashboardWidgetsPage(WebDriver driver) {
        super(driver);
    }

    public void validateMonthlyEarningCard(String expMonthlyEarAmount) {
        String monthlyHeader = monthlyEarningValue.findElement(By.xpath("../div[1]")).getText().trim();
        Assert.assertEquals(monthlyHeader, "EARNINGS (MONTHLY)");
        String monthlyEarningsAmount = monthlyEarningValue.getText().trim();
        Assert.assertEquals(monthlyEarningsAmount, expMonthlyEarAmount);
    }

    public void validateAnnualEarningCard(String expAnnualEarAmount) {
        String annualHeader = annualEarningValue.findElement(By.xpath("../div[1]")).getText().trim();
        Assert.assertEquals(annualHeader, "EARNINGS (ANNUAL)");
        String annualEarningsAmount = annualEarningValue.getText().trim();
        Assert.assertEquals(annualEarningsAmount, expAnnualEarAmount);

    }

    public void validateProfitMargin(String expProfitMar) {
        String profitMarginHeader = profitMarginValue.findElement(By.xpath("./ancestor::div[@class='row no-gutters align-items-center']/preceding-sibling::div")).getText().trim();
        Assert.assertEquals(profitMarginHeader, "PROFIT MARGIN");
        String profitMarginPercent = profitMarginValue.getText().trim();
        Assert.assertEquals(profitMarginPercent, expProfitMar);
    }

    public void validateAvailableInventory(String expAvailableInventory) {
        String availableInventoryHeader = availableInventoryValue.findElement(By.xpath("../div[1]")).getText().trim();
        Assert.assertEquals(availableInventoryHeader, "AVAILABLE INVENTORY");
        String actualAvailableInventoryValue = availableInventoryValue.getText().trim();
        Assert.assertEquals(actualAvailableInventoryValue, expAvailableInventory);
    }


}
