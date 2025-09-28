package org.example;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.*;

public class Version3c {
    public static void main(String[] args) {
        // Setup WebDriver
        WebDriverManager.chromedriver().setup();

        // Configure Chrome options
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--disable-blink-features=AutomationControlled");

        // Create WebDriver instance
        WebDriver driver = new ChromeDriver(options);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        // Scanner for password input
        Scanner scanner = new Scanner(System.in);

        try {
            // Step 1: Open author profile page
            String authorUrl = "https://www.scopus.com/authid/detail.uri?authorId=56937836900";
            System.out.println("Step 1: Opening author profile page...");
            System.out.println("URL: " + authorUrl);

            driver.get(authorUrl);
            Thread.sleep(5000);

            // Step 2: Click Sign in button
            System.out.println("\nStep 2: Looking for Sign in button...");
            WebElement signInButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//span[contains(@class, 'Typography-module__lVnit') and text()='Sign in']")
            ));

            System.out.println("Found Sign in button. Clicking...");
            signInButton.click();
            Thread.sleep(5000);

            // Step 3: Handle cookie popup if it appears
            System.out.println("\nStep 3: Checking for cookie popup...");
            try {
                WebElement acceptCookiesButton = wait.until(ExpectedConditions.elementToBeClickable(
                        By.id("onetrust-accept-btn-handler")
                ));
                System.out.println("Found cookie popup. Clicking 'Accept all cookies'...");
                acceptCookiesButton.click();
                System.out.println("Cookie popup accepted.");
                Thread.sleep(2000);
            } catch (Exception e) {
                System.out.println("No cookie popup found or already handled.");
            }

            // Step 4: Wait for email page and enter email
            System.out.println("\nStep 4: Waiting for email input field...");

            // Try multiple selectors for email field
            WebElement emailField = null;
            String[] emailSelectors = {"input[id*='email']",
                    "input[type='email']",
                    "input[name='email']",
                    "input[placeholder*='email' i]",
                    "input[placeholder*='Email' i]",

                    "input[id*='Email']",
                    "#username",  // Common ID for email fields
                    "input[data-testid*='email']"
            };

            for (String selector : emailSelectors) {
                try {
                    emailField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(selector)));
                    System.out.println("Found email field using selector: " + selector);
                    break;
                } catch (Exception e) {
                    // Try next selector
                    continue;
                }
            }

            if (emailField == null) {
                // If CSS selectors fail, try XPath
                try {
                    emailField = wait.until(ExpectedConditions.visibilityOfElementLocated(
                            By.xpath("//input[contains(@type, 'email') or contains(@name, 'email') or contains(@placeholder, 'email') or contains(@id, 'email')]")
                    ));
                    System.out.println("Found email field using XPath");
                } catch (Exception e) {
                    System.out.println("Could not find email field with any selector");
                    throw e;
                }
            }

            System.out.println("Found email field. Entering email...");
            String email = "tapendraverma2012@gmail.com";
            emailField.clear();
            emailField.sendKeys(email);
            System.out.println("Email entered: " + email);

            // Step 5: Click Continue button
            System.out.println("\nStep 5: Looking for Continue button...");
            WebElement continueButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[@id='bdd-elsPrimaryBtn' and @type='submit']")
            ));

            // Check if button is enabled
            if (continueButton.isEnabled()) {
                System.out.println("Continue button is enabled. Clicking...");
                continueButton.click();
            } else {
                System.out.println("Continue button is disabled. Trying JavaScript click...");
                JavascriptExecutor js = (JavascriptExecutor) driver;
                js.executeScript("arguments[0].click();", continueButton);
            }

            // Step 6: Wait for password page and enter password
            System.out.println("\nStep 6: Waiting for password page to load...");
            Thread.sleep(5000);

            // Find password field
            System.out.println("Looking for password field...");
            WebElement passwordField = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.id("bdd-password")
            ));

            System.out.println("Found password field. Please enter your password:");
            String password = "Nitya@11";

            System.out.println("Entering password...");
            passwordField.clear();
            passwordField.sendKeys(password);
            System.out.println("Password entered.");

            // Step 7: Click Sign In button
            System.out.println("\nStep 7: Looking for Sign In button...");

            // Try multiple selectors for Sign In button
            WebElement signInBtn = null;
            String[] signInSelectors = {
                    "button[id='bdd-elsPrimaryBtn']",
                    "button[type='submit']",
                    "button[value*='signin']",
                    "button[value*='login']",
                    "input[type='submit']"
            };

            for (String selector : signInSelectors) {
                try {
                    signInBtn = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(selector)));
                    System.out.println("Found Sign In button using selector: " + selector);
                    break;
                } catch (Exception e) {
                    continue;
                }
            }

            if (signInBtn == null) {
                // Try XPath as fallback
                try {
                    signInBtn = wait.until(ExpectedConditions.elementToBeClickable(
                            By.xpath("//button[contains(text(), 'Sign In') or contains(text(), 'Sign in') or contains(@value, 'signin')]")
                    ));
                    System.out.println("Found Sign In button using XPath");
                } catch (Exception e) {
                    System.out.println("Could not find Sign In button");
                    throw e;
                }
            }

            // Check if button is enabled
            if (signInBtn.isEnabled()) {
                System.out.println("Sign In button is enabled. Clicking...");
                signInBtn.click();
            } else {
                System.out.println("Sign In button is disabled. Trying JavaScript click...");
                JavascriptExecutor js = (JavascriptExecutor) driver;
                js.executeScript("arguments[0].click();", signInBtn);
            }

            // Step 8: Wait for login to complete
            System.out.println("\nStep 8: Waiting for login to complete...");
            Thread.sleep(5000);

            // Check if we're redirected back to author profile
            String currentUrl = driver.getCurrentUrl();
            System.out.println("Current URL: " + currentUrl);

            if (currentUrl.contains("scopus.com") && !currentUrl.contains("login")) {
                System.out.println("=== SUCCESS: Login completed! ===");

                // STEP 9: Click Edit Profile button
                System.out.println("\nStep 9: Looking for Edit Profile button...");
                WebElement editProfileButton = wait.until(ExpectedConditions.elementToBeClickable(
                        By.xpath("//span[contains(@class, 'Typography-module__lVnit') and text()='Edit profile']")
                ));
                System.out.println("Found Edit Profile button. Clicking...");
                editProfileButton.click();
                Thread.sleep(3000);

                // STEP 10: Click Continue button (confirmation)
                System.out.println("\nStep 10: Looking for Continue button (confirmation)...");
                WebElement continueConfirmButton = wait.until(ExpectedConditions.elementToBeClickable(
                        By.xpath("//span[contains(@class, 'Typography-module__lVnit') and text()='Continue']")
                ));
                System.out.println("Found Continue button. Clicking...");
                continueConfirmButton.click();
                Thread.sleep(5000);

                // STEP 11: Click Documents button
                System.out.println("\nStep 11: Looking for Documents button...");
                WebElement documentsButton = wait.until(ExpectedConditions.elementToBeClickable(
                        By.xpath("//span[contains(@class, 'Typography-module__lVnit') and text()='Documents']")
                ));
                System.out.println("Found Documents button. Clicking...");
                documentsButton.click();
                Thread.sleep(5000);

                // STEP 12: Extract ALL articles with pagination
                System.out.println("\nStep 12: Starting pagination-based extraction...");
                Set<String> allArticles = extractAllArticlesWithPagination(driver, wait);

                // Display final results
                System.out.println("\n" + "=".repeat(60));
                System.out.println("=== EXTRACTION COMPLETED ===");
                System.out.println("=".repeat(60));
                System.out.println("Total unique articles found: " + allArticles.size());
                System.out.println("\n=== ALL ARTICLE TITLES ===");

                int count = 1;
                for (String title : allArticles) {
                    System.out.println(count + ". " + title);
                    count++;
                }

            } else {
                System.out.println("Login might have failed. Current URL: " + currentUrl);
            }

            System.out.println("\nKeeping browser open for 30 seconds...");
            Thread.sleep(30000);

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Close resources
            scanner.close();
            driver.quit();
            System.out.println("Browser closed.");
        }
    }

    private static Set<String> extractAllArticlesWithPagination(WebDriver driver, WebDriverWait wait) {
        Set<String> allArticles = new LinkedHashSet<>();
        int pageNumber = 1;
        boolean hasNextPage = true;

        System.out.println("Starting pagination extraction...");

        while (hasNextPage) {
            try {
                System.out.println("\n--- Processing Page " + pageNumber + " ---");

                // Wait for articles to load on current page
                wait.until(ExpectedConditions.presenceOfElementLocated(
                        By.cssSelector(".document-title, .Table-module__oZx3T, .ReviewProfileDetails-module__vxcvN")
                ));

                // Extract articles from current page
                Set<String> currentPageArticles = extractArticlesFromCurrentPage(driver);
                allArticles.addAll(currentPageArticles);

                System.out.println("Page " + pageNumber + ": Found " + currentPageArticles.size() + " articles");
                System.out.println("Total so far: " + allArticles.size() + " articles");

                // Check if Next button exists and is clickable
                WebElement nextButton = findNextButton(driver);
                if (nextButton != null && nextButton.isEnabled() && nextButton.isDisplayed()) {
                    System.out.println("Next button found. Clicking to go to page " + (pageNumber + 1) + "...");

                    // Click Next button using JavaScript for reliability
                    JavascriptExecutor js = (JavascriptExecutor) driver;
                    js.executeScript("arguments[0].click();", nextButton);

                    // Wait for next page to load
                    Thread.sleep(3000);
                    pageNumber++;

                } else {
                    System.out.println("No more pages available. Next button not found or disabled.");
                    hasNextPage = false;
                }

            } catch (Exception e) {
                System.out.println("Error on page " + pageNumber + ": " + e.getMessage());
                hasNextPage = false;
            }
        }

        System.out.println("\nPagination completed! Processed " + (pageNumber - 1) + " pages");
        return allArticles;
    }

    private static Set<String> extractArticlesFromCurrentPage(WebDriver driver) {
        Set<String> articles = new LinkedHashSet<>();

        try {
            // Try multiple selectors to find article titles
            String[] titleSelectors = {
                    "td.Table-module__oZx3T.ReviewProfileDetails-module__vxcvN h5.ReviewProfileDetails-module__woWIS",
                    ".document-title h5.ReviewProfileDetails-module__woWIS",
                    ".ReviewProfileDetails-module__woWIS",
                    "td.ReviewProfileDetails-module__vxcvN h5",
                    ".document-title h5"
            };

            List<WebElement> titleElements = new ArrayList<>();

            for (String selector : titleSelectors) {
                try {
                    List<WebElement> elements = driver.findElements(By.cssSelector(selector));
                    if (!elements.isEmpty()) {
                        titleElements = elements;
                        break;
                    }
                } catch (Exception e) {
                    continue;
                }
            }

            // Fallback: get all h5 elements
            if (titleElements.isEmpty()) {
                titleElements = driver.findElements(By.tagName("h5"));
            }

            // Extract text from each element
            for (WebElement element : titleElements) {
                String title = element.getText().trim();
                // Clean the title (remove quotes, etc.)
                title = title.replaceAll("^\"|\"$", "").trim();

                // Filter for actual article titles (not buttons/menus)
                if (!title.isEmpty() && title.length() > 20 &&
                        !title.equals("Next") &&
                        !title.equals("Documents") &&
                        !title.equals("Edit profile") &&
                        !title.contains("Sign in")) {
                    articles.add(title);
                }
            }

        } catch (Exception e) {
            System.out.println("Error extracting articles from current page: " + e.getMessage());
        }

        return articles;
    }

    private static WebElement findNextButton(WebDriver driver) {
        try {
            // Look for Next button with the specific class and text
            return driver.findElement(
                    By.xpath("//span[contains(@class, 'Typography-module__lVnit') and text()='Next']")
            );
        } catch (Exception e) {
            // Try alternative selectors if first one fails
            try {
                return driver.findElement(By.xpath("//button[contains(text(), 'Next')]"));
            } catch (Exception e2) {
                try {
                    return driver.findElement(By.xpath("//a[contains(text(), 'Next')]"));
                } catch (Exception e3) {
                    return null;
                }
            }
        }
    }
}