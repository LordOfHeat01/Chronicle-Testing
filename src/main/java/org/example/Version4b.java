package org.example;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.*;

public class Version4b {
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
//56251578000->pp 55367393200->mk 57203375935 ->aks 36550140000->anjula arora jiit
            driver.get(authorUrl);
            Thread.sleep(5000);

            // NEW: Extract author name and document count BEFORE login
            System.out.println("\n--- EXTRACTING AUTHOR INFORMATION ---");
            String authorName = extractAuthorName(driver);
            int totalDocuments = extractTotalDocuments(driver);
            String [] name = authorName.split(", ");
            System.out.println("Author Name: " + name[1] + " " + name[0]);
            System.out.println("Total Documents: " + totalDocuments);

            // Step 2: Click Sign in button
            System.out.println("\nStep 2: Looking for Sign in button...");

            // NEW: Handle "Maybe later" popup before clicking Sign in
            System.out.println("Checking for 'Maybe later' popup...");
            try {
                WebElement maybeLaterButton = wait.until(ExpectedConditions.elementToBeClickable(
                        By.xpath("//button[contains(text(), 'Maybe later')]")
                ));
                System.out.println("Found 'Maybe later' popup. Clicking to dismiss...");
                maybeLaterButton.click();
                Thread.sleep(2000);
            } catch (Exception e) {
                System.out.println("No 'Maybe later' popup found.");
            }

            WebElement signInButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//span[contains(@class, 'Typography-module__lVnit') and text()='Sign in']")
            ));

            System.out.println("Found Sign in button. Clicking...");
            signInButton.click();
            Thread.sleep(3000);

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

                // STEP 12: Extract ALL articles with SMART pagination
                System.out.println("\nStep 12: Starting SMART pagination-based extraction...");
                System.out.println("Based on document count: " + totalDocuments + " documents");
                Set<String> allArticles = extractAllArticlesWithSmartPagination(driver, wait, totalDocuments);

                // Display final results
                System.out.println("\n" + "=".repeat(60));
                System.out.println("=== EXTRACTION COMPLETED ===");
                System.out.println("=".repeat(60));
                System.out.println("Author Name: " + name[1] + " " + name[0]);
                System.out.println("Expected Documents: " + totalDocuments);
                System.out.println("Actual Articles Found: " + allArticles.size());
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

    // NEW METHOD: Extract author name
    private static String extractAuthorName(WebDriver driver) {
        try {
            WebElement authorNameElement = driver.findElement(
                    By.cssSelector("h1[data-testid='author-profile-name']")
            );
            return authorNameElement.getText().trim();
        } catch (Exception e) {
            System.out.println("Could not extract author name: " + e.getMessage());
            return "Unknown Author";
        }
    }

    // NEW METHOD: Extract total document count
    // UPDATED METHOD: Extract correct document count
    private static int extractTotalDocuments(WebDriver driver) {
        try {
            // Try the correct selector with the typo 'uniclkable-count'
            WebElement documentCountElement = driver.findElement(
                    By.cssSelector("span[data-testid='uniclkable-count']")
            );
            String countText = documentCountElement.getText().trim();
            System.out.println("Document count found: " + countText);

            int totalDocuments = Integer.parseInt(countText);
            return totalDocuments;

        } catch (Exception e) {
            System.out.println("Could not extract document count from 'uniclkable-count': " + e.getMessage());

            // Fallback: Try to find any element containing "Documents" and a number
            try {
                String pageText = driver.findElement(By.tagName("body")).getText();
                java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("Documents.*?(\\d+)");
                java.util.regex.Matcher matcher = pattern.matcher(pageText);

                if (matcher.find()) {
                    String countStr = matcher.group(1);
                    int count = Integer.parseInt(countStr);
                    System.out.println("Found document count from text: " + count);
                    return count;
                }
            } catch (Exception e2) {
                System.out.println("Fallback method also failed: " + e2.getMessage());
            }

            return 10; // Safe default
        }
    }

    private static int getDocumentCountFromPagination(WebDriver driver) {
        try {
            // Look for pagination text like "1-10 of 43"
            String pageText = driver.findElement(By.tagName("body")).getText();

            // Pattern for "X of Y" or "X-Y of Z"
            java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("of\\s+(\\d+,?\\d+)");
            java.util.regex.Matcher matcher = pattern.matcher(pageText);

            if (matcher.find()) {
                String countStr = matcher.group(1).replace(",", "");
                int count = Integer.parseInt(countStr);
                System.out.println("Found document count from pagination: " + count);
                return count;
            }

            // If no pagination text, count articles on first page and estimate
            Set<String> firstPageArticles = extractArticlesFromCurrentPage(driver);
            int firstPageCount = firstPageArticles.size();

            if (firstPageCount == 10) {
                // If first page has 10, there are probably more pages
                System.out.println("First page has 10 articles. Total count unknown, will discover during pagination.");
                return 20; // Conservative estimate
            } else {
                // If first page has less than 10, that's probably the total
                System.out.println("First page has " + firstPageCount + " articles. Assuming this is the total.");
                return firstPageCount;
            }

        } catch (Exception e) {
            System.out.println("Could not determine document count from pagination: " + e.getMessage());
            return 20; // Safe default
        }
    }

    // UPDATED METHOD: Smart pagination with known total
    private static Set<String> extractAllArticlesWithSmartPagination(WebDriver driver, WebDriverWait wait, int totalDocuments) {
        Set<String> allArticles = new LinkedHashSet<>();

        try {
            System.out.println("Starting SMART pagination extraction...");
            System.out.println("Total documents to extract: " + totalDocuments);

            // Scopus shows 20 articles per page (confirmed from logs)
            int articlesPerPage = 20;
            int pagesNeeded = (int) Math.ceil((double) totalDocuments / articlesPerPage);
            System.out.println("Pages needed: " + pagesNeeded + " (20 articles per page)");

            int maxPages = Math.min(pagesNeeded, 20); // safety cap
            System.out.println("Processing maximum " + maxPages + " pages");

            for (int pageNumber = 1; pageNumber <= maxPages; pageNumber++) {
                System.out.println("\n--- Processing Page " + pageNumber + " of " + maxPages + " ---");

                // Wait up to 10s for title divs to appear
                try {
                    new WebDriverWait(driver, Duration.ofSeconds(10)).until(d ->
                            !((JavascriptExecutor) d)
                                    .executeScript("return Array.from(document.querySelectorAll(\"div[class*='woWlS']\")).map(e => e.innerText);")
                                    .toString().equals("[]")
                    );
                } catch (Exception e) {
                    System.out.println("  Timed out waiting for titles, scraping anyway...");
                }

                // Scrape current page
                Set<String> currentPageArticles = extractArticlesFromCurrentPage(driver);

                int previousSize = allArticles.size();
                allArticles.addAll(currentPageArticles);
                int newArticles = allArticles.size() - previousSize;

                System.out.println("Page " + pageNumber + ": Found " + currentPageArticles.size() + " articles (" + newArticles + " new)");
                System.out.println("Total so far: " + allArticles.size() + " / " + totalDocuments);

                // Stop if we have everything
                if (allArticles.size() >= totalDocuments) {
                    System.out.println("Reached expected document count. Done.");
                    break;
                }

                // Stop if no Next needed
                if (pageNumber >= maxPages) {
                    System.out.println("Reached last page.");
                    break;
                }

                // --- Click Next button ---
                WebElement nextButton = findNextButton(driver);
                if (nextButton == null) {
                    System.out.println("Next button not found. Stopping.");
                    break;
                }

                // Take a snapshot of ALL current titles to detect page change
                Set<String> titlesBeforeClick = new LinkedHashSet<>(currentPageArticles);
                System.out.println("Clicking Next → page " + (pageNumber + 1) + "...");

                JavascriptExecutor js = (JavascriptExecutor) driver;

                // Scroll Next button into view then click
                js.executeScript("arguments[0].scrollIntoView({block:'center'});", nextButton);
                Thread.sleep(500);
                js.executeScript("arguments[0].click();", nextButton);

                // Poll every second for up to 20 seconds until titles change
                boolean pageChanged = false;
                for (int attempt = 1; attempt <= 20; attempt++) {
                    Thread.sleep(1000);
                    Set<String> titlesNow = extractArticlesFromCurrentPage(driver);

                    // Page has changed when we get different titles
                    if (!titlesNow.isEmpty() && !titlesNow.equals(titlesBeforeClick)) {
                        System.out.println("  Page changed confirmed at " + attempt + "s.");
                        pageChanged = true;
                        break;
                    }
                    System.out.println("  Waiting for page to change... (" + attempt + "s)");
                }

                if (!pageChanged) {
                    // One last retry — scroll to top, re-find button, click again
                    System.out.println("  Page didn't change. Scrolling to top and retrying click...");
                    js.executeScript("window.scrollTo(0,0);");
                    Thread.sleep(1000);
                    nextButton = findNextButton(driver);
                    if (nextButton != null) {
                        js.executeScript("arguments[0].scrollIntoView({block:'center'});", nextButton);
                        Thread.sleep(500);
                        js.executeScript("arguments[0].click();", nextButton);
                        Thread.sleep(6000); // give extra time
                        Set<String> retryTitles = extractArticlesFromCurrentPage(driver);
                        if (!retryTitles.isEmpty() && !retryTitles.equals(titlesBeforeClick)) {
                            System.out.println("  Retry click successful!");
                        } else {
                            System.out.println("  Retry failed. Stopping pagination.");
                            break;
                        }
                    } else {
                        System.out.println("  Next button gone. Stopping.");
                        break;
                    }
                }
            }

            System.out.println("\nPagination completed!");
            System.out.println("Expected: " + totalDocuments + " | Actual fetched: " + allArticles.size());

        } catch (Exception e) {
            System.out.println("Error in pagination: " + e.getMessage());
            e.printStackTrace();
        }

        return allArticles;
    }

    // =====================================================================
    // Targets ONLY the title div: ReviewProfileDetails-module__woWlS
    // This div contains ONLY the article title text — no source/journal info.
    // Uses JS innerText to handle lazy-rendered elements.
    // Falls back to class*= wildcard in case the suffix varies slightly.
    // =====================================================================
    private static Set<String> extractArticlesFromCurrentPage(WebDriver driver) {
        Set<String> articles = new LinkedHashSet<>();

        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;

            // Use JavaScript to get all elements matching the exact title div class.
            // class*='woWlS' is a wildcard match on the unique suffix of the title class,
            // which avoids any single/double underscore ambiguity in the prefix.
            @SuppressWarnings("unchecked")
            List<WebElement> titleElements = (List<WebElement>) js.executeScript(
                    "return Array.from(document.querySelectorAll(\"div[class*='woWlS']\"));"
            );

            if (titleElements == null || titleElements.isEmpty()) {
                System.out.println("  WARNING: No title elements found on this page.");
                return articles;
            }

            System.out.println("  Found " + titleElements.size() + " title elements on this page.");

            for (WebElement element : titleElements) {
                // Use innerText — getText() can return empty for off-screen elements
                Object raw = js.executeScript("return arguments[0].innerText;", element);
                if (raw == null) continue;

                String title = raw.toString().trim();
                // Strip surrounding quotes if present
                title = title.replaceAll("^\"|\"$", "").trim();

                if (!title.isEmpty()) {
                    articles.add(title);
                }
            }

        } catch (Exception e) {
            System.out.println("Error extracting articles from current page: " + e.getMessage());
        }

        System.out.println("  Articles extracted from this page: " + articles.size());
        return articles;
    }

    private static WebElement findNextButton(WebDriver driver) {
        // Attempt 1: Exact match from actual Scopus paginator HTML
        // <li class="page-item"><button ...><span ...>Next</span></button></li>
        try {
            return driver.findElement(
                    By.xpath("//li[@class='page-item']//button[.//span[normalize-space(text())='Next']]")
            );
        } catch (Exception e1) {
            // Attempt 2: page-item class anywhere (in case extra classes added)
            try {
                return driver.findElement(
                        By.xpath("//li[contains(@class,'page-item')]//button[.//span[normalize-space(text())='Next']]")
                );
            } catch (Exception e2) {
                // Attempt 3: any button with a Next span anywhere on page
                try {
                    return driver.findElement(
                            By.xpath("//button[.//span[normalize-space(text())='Next']]")
                    );
                } catch (Exception e3) {
                    return null;
                }
            }
        }
    }
}