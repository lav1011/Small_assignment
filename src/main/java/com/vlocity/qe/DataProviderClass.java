package com.vlocity.qe;

import org.testng.ITestContext;
import org.testng.annotations.DataProvider;

/**
 * * Created by Lav Sharma
 */
public class DataProviderClass {


    @DataProvider(name = "Language_Data_set")
    public Object[] Languagedataset() {

        return new Object[]{
                "English", "日本語", "Español", "Deutsch", "Русский", "Français", "Italiano", "中文", "Português", "Polski",
        };
    }
}
