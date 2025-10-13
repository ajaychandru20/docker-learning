package com.test.vendorapplication.resourceloader;

public record VendorTestDataParameters(
        String username,
        String password,
        String monthlyEarning,
        String annualEarning,
        String profitMargin,
        String availableInventory,
        String searchKeyword,
        int searchResultsCount
) {
}
