package org.example;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class ScopusScraper {
    public static void main(String[] args) {
        WebDriver driver = new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        try {
            // 1. Open ORCID search page
            driver.get("https://www.scopus.com/freelookup/form/author.uri");

            // 2. Enter ORCID ID
            WebElement orcidInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("orcidId")));
            orcidInput.sendKeys("0000-0001-8483-865X"); // <- yaha apna ORCID id daalna

            // 3. Click ORCID Submit button
            WebElement searchBtn = wait.until(ExpectedConditions.elementToBeClickable(By.id("orcidSubmitBtn")));
            searchBtn.click();

            // 4. Toggle button click to show first article
            WebElement toggleBtn = wait.until(ExpectedConditions.elementToBeClickable(By.id("toggleTitle57215613908")));
            toggleBtn.click();
            System.out.println("before artcile extraction");

            // 5. Extract first article
            WebElement firstArticle = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("recentTitleData57215613908")));
            WebElement titleElement = firstArticle.findElement(By.cssSelector("span.titleText"));
            String articleName = titleElement.getText(); // Extract "Excel Modeling and Performance Evaluation of Solar Still: Comprehensive Analysis"

            System.out.println("First Article: " + articleName);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }
}
//<div id="recentTitleData57215613908" class="dropdownContentWrap lastTitle">…</div>
// this part is working fine with rishika ma'am id