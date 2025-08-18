package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

public class Scrapper {
    public static void main(String[] args) {
        WebDriver driver = new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        // ORCID IDs list
        List<String> orcids = Arrays.asList(
                "0000-0001-5384-6606" // <-- aur bhi daal sakta hai
        );

        try {
            for (String orcid : orcids) {
                driver.get("https://www.scopus.com/freelookup/form/author.uri");

                // Enter ORCID ID
                WebElement orcidInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("orcidId")));
                orcidInput.clear();
                orcidInput.sendKeys(orcid);

                // Click submit
                WebElement searchBtn = wait.until(ExpectedConditions.elementToBeClickable(By.id("orcidSubmitBtn")));
                searchBtn.click();

                try {
                    // Toggle button (first "View last title")
                    WebElement toggleBtn = wait.until(ExpectedConditions.elementToBeClickable(
                            By.xpath("//button[starts-with(@id,'toggleTitle')]")
                    ));
                    toggleBtn.click();
                    System.out.println("Clicked toggle for ORCID: " + orcid);

                    // Wait for article div
                    WebElement articleDiv = wait.until(ExpectedConditions.visibilityOfElementLocated(
                            By.xpath("//div[starts-with(@id,'recentTitleData')]")
                    ));
                    wait.until(d -> !articleDiv.getText().trim().isEmpty());

                    String articleName = articleDiv.getText();
                    System.out.println("First Article for " + orcid + ": " + articleName);

                } catch (Exception e) {
                    System.out.println("No article found for ORCID: " + orcid);
                }

                System.out.println("------------------------------------");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }
}
