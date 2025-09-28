package org.example;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.*;

public class Version3b {
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
            String authorUrl = "https://www.scopus.com/authid/detail.uri?authorId=36550140000";
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
            String[] emailSelectors = {  "input[id*='email']",
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
            String password ="Nitya@11";
                    //scanner.nextLine();

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

                // STEP 12: Extract default articles (no dropdown change)
                System.out.println("\nStep 12: Extracting default articles...");
                Set<String> articleTitles = extractArticleTitlesSinglePage(driver, wait);

                // Display results
                System.out.println("\n" + "=".repeat(50));
                System.out.println("=== EXTRACTION COMPLETE ===");
                System.out.println("=".repeat(47));
                System.out.println("Total articles found: " + articleTitles.size());
                System.out.println("\n=== ARTICLE TITLES ===");

                int count = 1;
                for (String title : articleTitles) {
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

            // Print current URL and page source for debugging
            System.out.println("\n=== DEBUG INFO ===");
            System.out.println("Current URL: " + driver.getCurrentUrl());
            System.out.println("Page Title: " + driver.getTitle());
        } finally {
            // Close resources
            scanner.close();
            driver.quit();
            System.out.println("Browser closed.");
        }
    }

    private static Set<String> extractArticleTitlesSinglePage(WebDriver driver, WebDriverWait wait) {
        Set<String> articleTitles = new LinkedHashSet<>();

        try {
            System.out.println("Attempting to extract articles...");

            // Method 1: Try the specific CSS selector
            try {
                List<WebElement> elements = driver.findElements(
                        By.cssSelector("td.Table-module__oZx3T.ReviewProfileDetails-module__vxcvN h5.ReviewProfileDetails-module__woWIS")
                );
                System.out.println("Method 1 - Found " + elements.size() + " articles");

                for (WebElement element : elements) {
                    String title = element.getText().trim().replaceAll("^\"|\"$", "");
                    if (!title.isEmpty()) {
                        articleTitles.add(title);
                        System.out.println("• " + title);
                    }
                }
            } catch (Exception e) {
                System.out.println("Method 1 failed: " + e.getMessage());
            }

            // Method 2: If first method found nothing, try broader search
            if (articleTitles.isEmpty()) {
                try {
                    List<WebElement> allH5 = driver.findElements(By.tagName("h5"));
                    System.out.println("Method 2 - Found " + allH5.size() + " h5 elements");

                    for (WebElement h5 : allH5) {
                        String text = h5.getText().trim().replaceAll("^\"|\"$", "");
                        // Filter for likely article titles (long text that's not buttons/menus)
                        if (!text.isEmpty() && text.length() > 20 &&
                                !text.equals("Documents") &&
                                !text.equals("Edit profile") &&
                                !text.contains("Sign in")) {
                            articleTitles.add(text);
                            System.out.println("• " + text);
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Method 2 failed: " + e.getMessage());
                }
            }

            // Method 3: Look for any elements with document-title class
            if (articleTitles.isEmpty()) {
                try {
                    List<WebElement> docTitles = driver.findElements(By.cssSelector(".document-title"));
                    System.out.println("Method 3 - Found " + docTitles.size() + " elements with document-title class");

                    for (WebElement docTitle : docTitles) {
                        String text = docTitle.getText().trim().replaceAll("^\"|\"$", "");
                        // Remove the "×" button text if present
                        text = text.replace("×", "").trim();
                        if (!text.isEmpty() && text.length() > 20) {
                            articleTitles.add(text);
                            System.out.println("• " + text);
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Method 3 failed: " + e.getMessage());
                }
            }

            System.out.println("Final count: " + articleTitles.size() + " articles extracted");

        } catch (Exception e) {
            System.out.println("Error in extraction: " + e.getMessage());
        }

        return articleTitles;
    }
}