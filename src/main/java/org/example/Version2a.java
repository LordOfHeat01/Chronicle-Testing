package org.example;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class Version2a {
    public static void main(String[] args) {
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.setExperimentalOption("excludeSwitches", Arrays.asList("enable-automation"));

        WebDriver driver = new ChromeDriver(options);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        List<String> orcids = Arrays.asList("0000-0002-2279-9373","0000-0001-5215-1300");

        try {
            for (String orcid : orcids) {
                System.out.println("Processing ORCID: " + orcid);

                driver.get("https://www.scopus.com/freelookup/form/author.uri");

                // Enter ORCID
                WebElement orcidInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("orcidId")));
                orcidInput.clear();
                orcidInput.sendKeys(orcid);

                WebElement searchBtn = wait.until(ExpectedConditions.elementToBeClickable(By.id("orcidSubmitBtn")));
                searchBtn.click();

                try {
                    // Wait for search results to load
                    Thread.sleep(5000);

                    // DEBUG: Check what's on the page
                    System.out.println("Current URL after search: " + driver.getCurrentUrl());
                    System.out.println("Page title: " + driver.getTitle());

                    // Click the "View last title" toggle
                    WebElement toggleBtn = wait.until(ExpectedConditions.elementToBeClickable(
                            By.xpath("//button[starts-with(@id,'toggleTitle') or contains(text(),'View last title')]")
                    ));
                    toggleBtn.click();
                    System.out.println("Clicked toggle button");

                    // Wait for recent title data to load
                    WebElement articleDiv = wait.until(ExpectedConditions.visibilityOfElementLocated(
                            By.xpath("//div[starts-with(@id,'recentTitleData')]")
                    ));
                    wait.until(driver1 -> !articleDiv.getText().trim().isEmpty());
                    String articleName = articleDiv.getText().trim();
                    System.out.println("Latest article: " + articleName);

                    // --- FIND AND CLICK AUTHOR LINK GENERICALLY ---
                    WebElement authorLink = null;
                    String originalWindow = driver.getWindowHandle();
                    Set<String> handlesBefore = driver.getWindowHandles();

                    // Try multiple generic strategies to find author link
                    try {
                        // Strategy 1: Primary XPath with class and href (most reliable)
                        authorLink = wait.until(ExpectedConditions.elementToBeClickable(
                                By.xpath("//a[contains(@href,'authorDetails.uri') and contains(@class,'docTitle')]")
                        ));
                        System.out.println("Found author link with primary selector");
                    } catch (Exception e1) {
                        try {
                            // Strategy 2: Fallback - just href pattern
                            authorLink = wait.until(ExpectedConditions.elementToBeClickable(
                                    By.xpath("//a[contains(@href,'authorDetails.uri')]")
                            ));
                            System.out.println("Found author link with href selector");
                        } catch (Exception e2) {
                            try {
                                // Strategy 3: Look for the first author link in results
                                // This is generic - finds the first author link that's not empty
                                List<WebElement> potentialAuthorLinks = driver.findElements(
                                        By.xpath("//a[contains(@href,'authorDetails.uri')]")
                                );

                                for (WebElement link : potentialAuthorLinks) {
                                    String linkText = link.getText().trim();
                                    if (!linkText.isEmpty() && link.isDisplayed() && link.isEnabled()) {
                                        authorLink = link;
                                        System.out.println("Found author link from list: " + linkText);
                                        break;
                                    }
                                }
                            } catch (Exception e3) {
                                System.out.println("All author link strategies failed");
                            }
                        }
                    }

                    if (authorLink != null) {
                        String authorText = authorLink.getText();
                        String [] author = authorText.split(",");
                        System.out.println("Author name: " + author[1] + " " + author[0]);

                        // CLICK THE AUTHOR LINK
                        try {
                            // First try normal click
                            authorLink.click();
                            System.out.println("Normal click attempted");
                        } catch (Exception e) {
                            // If normal click fails, use JavaScript click
                            System.out.println("Normal click failed, using JavaScript click");
                            JavascriptExecutor js = (JavascriptExecutor) driver;
                            js.executeScript("arguments[0].scrollIntoView(true);", authorLink);
                            js.executeScript("arguments[0].click();", authorLink);
                        }

                        // Wait for navigation - either new tab or same page
                        boolean switched = false;
                        long endTime = System.currentTimeMillis() + 15000; // 15 second timeout

                        while (System.currentTimeMillis() < endTime) {
                            // Check if new window opened
                            Set<String> currentHandles = driver.getWindowHandles();
                            if (currentHandles.size() > handlesBefore.size()) {
                                // Switch to new window
                                for (String handle : currentHandles) {
                                    if (!handlesBefore.contains(handle)) {
                                        driver.switchTo().window(handle);
                                        switched = true;
                                        System.out.println("Switched to new tab");
                                        break;
                                    }
                                }
                                break;
                            }

                            // Check if URL changed in same window
                            String currentUrl = driver.getCurrentUrl();
                            if (currentUrl.contains("authorDetails.uri") || currentUrl.contains("authid/detail")) {
                                switched = true;
                                System.out.println("Navigation occurred in same window");
                                break;
                            }

                            Thread.sleep(300);
                        }

                        if (switched) {
                            // Wait for author page to fully load
                            Thread.sleep(5000);
                            System.out.println("Successfully reached author page: " + driver.getCurrentUrl());

                            // ---- NOW FETCH ARTICLE TITLES ----
                            System.out.println("\n=== ARTICLE TITLES ===");

                            try {
                                Set<String> uniqueTitles = new LinkedHashSet<>();

                                // Strategy 1: Most specific - span inside h4 inside list items
                                try {
                                    List<WebElement> titleSpans = driver.findElements(
                                            By.cssSelector("li[data-testid='results-list-item'] h4 span")
                                    );
                                    for (WebElement span : titleSpans) {
                                        String title = span.getText().trim();
                                        if (isValidArticleTitle(title)) {
                                            uniqueTitles.add(title);
                                        }
                                    }
                                } catch (Exception e) {
                                    System.out.println("Specific span selector failed");
                                }

                                // Strategy 2: Fallback - just h4 inside list items
                                if (uniqueTitles.isEmpty()) {
                                    try {
                                        List<WebElement> titleH4 = driver.findElements(
                                                By.cssSelector("li[data-testid='results-list-item'] h4")
                                        );
                                        for (WebElement h4 : titleH4) {
                                            String title = h4.getText().trim();
                                            if (isValidArticleTitle(title)) {
                                                uniqueTitles.add(title);
                                            }
                                        }
                                    } catch (Exception e) {
                                        System.out.println("H4 selector failed");
                                    }
                                }

                                // Strategy 3: Final fallback - all h4 elements
                                if (uniqueTitles.isEmpty()) {
                                    try {
                                        List<WebElement> allH4 = driver.findElements(By.tagName("h4"));
                                        for (WebElement h4 : allH4) {
                                            String title = h4.getText().trim();
                                            if (isValidArticleTitle(title)) {
                                                uniqueTitles.add(title);
                                            }
                                        }
                                    } catch (Exception e) {
                                        System.out.println("All H4 selector failed");
                                    }
                                }

                                System.out.println("Unique articles found: " + uniqueTitles.size());
                                for (String title : uniqueTitles) {
                                    System.out.println("• " + title);
                                }

                            } catch (Exception e) {
                                System.out.println("Error fetching articles: " + e.getMessage());
                            }

                        } else {
                            System.out.println("Failed to navigate to author page");
                            System.out.println("Current URL: " + driver.getCurrentUrl());
                        }

                    } else {
                        System.out.println("Could not find author link element");
                    }

                } catch (Exception e) {
                    System.out.println("Failed for ORCID: " + orcid);
                    System.out.println("Error: " + e.getMessage());
                    e.printStackTrace();
                }

                System.out.println("------------------------------------");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }
    private static boolean isValidArticleTitle(String title) {
        return !title.isEmpty() &&
                title.length() > 15 &&
                !title.equals("Export all") &&
                !title.equals("Save all to list") &&
                !title.equals("Full text") &&
                !title.matches(".*\\d{4}.*") && // exclude years
                !title.matches("^[A-Z],.*") && // exclude author lists
                !title.equals("Book Chapter") &&
                !title.equals("Article") &&
                !title.equals("Document") &&
                !title.contains("DOI:") &&
                !title.contains("Cited by");
    }
}