package com.ajayc20.pages.vendorapplication;

import com.ajayc20.pages.AbstractPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;

public class DashboardTablePage extends AbstractPage {


    @FindBy(css = ".card-header h6")
    private WebElement tableCardOrderHistoryHeader;

    @FindBy(css = "#dataTable_filter input")
    private WebElement tableSearchInput;

    @FindBy(id = "dataTable_info")
    private WebElement tableCountInfoData;

    @Override
    public boolean isDataVisible() {
        this.wait.until(ExpectedConditions.visibilityOf(tableSearchInput));
        return tableSearchInput.isDisplayed();
    }

    public DashboardTablePage(WebDriver driver) {
        super(driver);
    }

    public void validateSearchBox(String searchValue) {
        tableSearchInput.clear();
        tableSearchInput.sendKeys(searchValue);
    }

    public void validateFinalCount(Integer expFinalCount) {
        String listFinalCountRecords = tableCountInfoData.getText().trim();
        String[] arr = listFinalCountRecords.split(" ");
        if (arr[5].trim().matches("\\d+")) {
            Integer count = Integer.parseInt(arr[5]);
            Assert.assertEquals(count, expFinalCount);

        } else {
            System.out.println("this count contains non-integar: " + arr[5].trim());
        }
    }


}
