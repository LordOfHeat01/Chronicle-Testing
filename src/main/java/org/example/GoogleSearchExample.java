package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.Duration;
import java.util.List;

public class GoogleSearchExample {
    public static void main(String[] args) {
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();

        try {
            driver.get("https://www.google.com");

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement searchBox = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(By.name("q"))
            );

            searchBox.sendKeys("Tapendra");
            searchBox.submit();

            // Wait until results load
            wait.until(ExpectedConditions.titleContains("Tapendra"));

            System.out.println("Search Results Page Title: " + driver.getTitle());

            // Print top 5 results
            List<WebElement> results = driver.findElements(By.cssSelector("h3"));
            for (int i = 0; i < Math.min(results.size(), 5); i++) {
                System.out.println((i+1) + ". " + results.get(i).getText());
            }

            Thread.sleep(5000); // just to see results

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }
}
