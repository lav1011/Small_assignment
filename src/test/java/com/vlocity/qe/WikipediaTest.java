package com.vlocity.qe;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.restassured.response.Response;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.given;

/**
 * This class verifies elements on the wikipedia homepage.
 * Created by Lav Sharma
 */
public class WikipediaTest {

    private Logger log = LoggerFactory.getLogger(WikipediaTest.class);

    private WebDriver driver;
    private ElementFinder finder;

    @BeforeClass
    public void setup() {

        /*
            WebDriverManager.chromedriver().setup() should automatically use the right
             driver for your Chrome version.  If it does not, you can choose a version manually
            see https://sites.google.com/a/chromium.org/chromedriver/downloads
            and update it as needed.

            WebDriverManager.chromedriver().version("74.0.3729.6").setup();
        */

        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        finder = new ElementFinder(driver);

        // adjust timeout as needed
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        driver.get("https://www.wikipedia.org/");
    }


    @Test
    public void sloganPresent() {

        String sloganClass = "localized-slogan";
        WebElement slogan = finder.findElement(By.className(sloganClass));

        Assert.assertNotNull(slogan, String.format("Unable to find slogan div by class: %s", sloganClass));

        log.info("Slogan text is {}", slogan.getText());

        Assert.assertEquals(slogan.getText(), "The Free Encyclopedia");
    }


    @Test(dataProvider = "Language_Data_set", dataProviderClass = DataProviderClass.class, description = "Verifies the languages present on wikipedia.org home page with data provider")
    public void verifylanguage(String data) {

        boolean checklanguage;
        checklanguage = driver.findElement(By.xpath("//div[@class='central-featured']/div/a/strong[text()='" + data + "']")).isDisplayed();
        Assert.assertTrue(checklanguage);
    }

    /*
       Below method will verify the response and Assert on the basis of broken URL or not.
     */
    @Test(description = " Verifies the hyperlinks for the Featured Languages ")
    public void getlinks() {
        Boolean status;
        int totalcount = driver.findElements(By.xpath("//div[@class='central-featured']/div")).size();
        for (int i = 1; i < totalcount + 1; i++) {
            String link = driver.findElement(By.xpath("//div[@class='central-featured']/div[" + i + "]/a")).getAttribute("href");
            status = getStatusOfLinks(link);
            Assert.assertTrue(status);

        }
    }

    /*
        Below method will fetch the response for an endpoint.
     */
    public boolean getStatusOfLinks(String url) {
        boolean flag = false;

        try {

            Response response = given().when().get(url);

            if (response.statusCode() == 200) {
                flag = true;
                System.out.println("For the given URL:- " + url + " Status code:- " + response.statusCode());
            } else {
                flag = false;
                System.out.println("For the given URL:- " + url + " Status code:- " + response.statusCode());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;

    }


    @AfterClass
    public void closeBrowser() {

        if (driver != null) {
            driver.close();
        }
    }
}
