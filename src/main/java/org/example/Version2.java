package org.example;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class Version2 {
    public static void main(String[] args) {
        // Use WebDriverManager to avoid manual chromedriver path handling
        WebDriverManager.chromedriver().setup();

        // Start Chrome
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        WebDriver driver = new ChromeDriver(options);

        // Explicit wait helper
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(25));

        // ORCID(s) to test
        List<String> orcids = Arrays.asList("0000-0001-5384-6606");

        try {
            for (String orcid : orcids) {
                System.out.println("Processing ORCID: " + orcid);

                // 1) Open Scopus ORCID lookup page
                driver.get("https://www.scopus.com/freelookup/form/author.uri");

                // 2) Enter ORCID ID
                WebElement orcidInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("orcidId")));
                orcidInput.clear();
                orcidInput.sendKeys(orcid);

                // 3) Click submit
                WebElement searchBtn = wait.until(ExpectedConditions.elementToBeClickable(By.id("orcidSubmitBtn")));
                searchBtn.click();

                try {
                    // 4) Click the "View last title" toggle (id independent)
                    WebElement toggleBtn = wait.until(ExpectedConditions.elementToBeClickable(
                            By.xpath("//button[starts-with(@id,'toggleTitle') or .//span[text()='View last title']]")
                    ));
                    toggleBtn.click();
                    System.out.println("Clicked toggle for ORCID: " + orcid);

                    // 5) Wait for the recent title container to be populated (id starts with recentTitleData)
                    WebElement articleDiv = wait.until(ExpectedConditions.visibilityOfElementLocated(
                            By.xpath("//div[starts-with(@id,'recentTitleData')]")
                    ));

                    // Wait until that div has non-empty text (JS populates it)
                    wait.until(driver1 -> !articleDiv.getText().trim().isEmpty());
                    String articleName = articleDiv.getText().trim();
                    System.out.println("Latest article snippet: " + articleName);

                    // 6) Locate the author link (dynamic id — locate by href pattern / class)
                    // Primary locator: anchor that contains authorDetails.uri and has class 'docTitle'
                    By authorLinkLocatorPrimary = By.xpath("//a[contains(@href,'authorDetails.uri') and contains(@class,'docTitle')]");
                    By authorLinkLocatorFallback1 = By.xpath("//a[contains(@href,'authorDetails.uri')]");
                    By authorLinkLocatorFallback2 = By.cssSelector("a.docTitle");

                    WebElement authorLink = null;

                    // Try primary
                    try {
                        authorLink = wait.until(ExpectedConditions.elementToBeClickable(authorLinkLocatorPrimary));
                    } catch (Exception exPrimary) {
                        // try fallback 1
                        try {
                            authorLink = wait.until(ExpectedConditions.elementToBeClickable(authorLinkLocatorFallback1));
                        } catch (Exception exFallback1) {
                            // try fallback 2
                            try {
                                authorLink = wait.until(ExpectedConditions.elementToBeClickable(authorLinkLocatorFallback2));
                            } catch (Exception exFallback2) {
                                // no author link found
                                System.out.println("Could not locate author link for ORCID: " + orcid);
                            }
                        }
                    }

                    if (authorLink != null) {
                        String authorText = authorLink.getText();
                        String [] author= authorText.split(",");
                        System.out.println("Found author link text: " + author[1] +" "+ author[0]);
                        // Save current windows so we can detect a new one
                        String originalWindow = driver.getWindowHandle();
                        Set<String> handlesBefore = driver.getWindowHandles();

                        // Click the author link. Use JS click if normal click fails due to overlay.
                        try {
                            authorLink.click();
                        } catch (WebDriverException jsClickEx) {
                            // fallback to JS click
                            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", authorLink);
                        }

                        // 7) Wait for either new window OR URL change containing 'authorDetails.uri'
                        // Wait up to timeout for a new window handle
                        boolean switched = false;
                        long end = System.currentTimeMillis() + 15000; // 15s timeout
                        while (System.currentTimeMillis() < end) {
                            Set<String> handlesNow = driver.getWindowHandles();
                            // If a new window opened
                            if (handlesNow.size() > handlesBefore.size()) {
                                for (String handle : handlesNow) {
                                    if (!handlesBefore.contains(handle)) {
                                        // switch to the new window
                                        driver.switchTo().window(handle);
                                        switched = true;
                                        break;
                                    }
                                }
                                if (switched) break;
                            } else {
                                // maybe it navigated in the same window: check URL contains authorDetails.uri
                                if (driver.getCurrentUrl().contains("authorDetails.uri")) {
                                    switched = true;
                                    break;
                                }
                            }

                            try { Thread.sleep(300); } catch (InterruptedException ignored) {}
                        }

                        if (!switched) {
                            System.out.println("Author page did not open in a new tab nor navigate to authorDetails within timeout.");
                        } else {
                            // Wait for URL to contain authorDetails.uri to be safe
                            wait.until(ExpectedConditions.or(
                                    ExpectedConditions.urlContains("authorDetails.uri"),
                                    ExpectedConditions.titleContains("Author") // fallback
                            ));

                            System.out.println("Switched to author page. URL: " + driver.getCurrentUrl());
                            System.out.println("Page title: " + driver.getTitle());

                            // From here (Version 2) you will implement logic to scrape all publications/book chapters.
                            // For now we stop after verifying the author page opened.
                        }

                        // Optional: close the new tab and switch back to original
                        // If we switched to a new window, close it and return
                        if (!driver.getWindowHandle().equals(originalWindow)) {
                            driver.close();
                            driver.switchTo().window(originalWindow);
                        } else {
                            // If same window navigation happened, go back
                            try {
                                driver.navigate().back();
                            } catch (Exception ignore) {}
                        }

                    } // end if authorLink != null

                } catch (Exception e) {
                    System.out.println("No article / toggle / author link flow for ORCID: " + orcid);
                    e.printStackTrace();
                }

                System.out.println("------------------------------------");
            }
        } finally {
            // Clean up
            driver.quit();
        }
    }
}
