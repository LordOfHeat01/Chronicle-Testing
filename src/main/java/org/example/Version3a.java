package org.example;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class Version3a {
    public static void main(String[] args) {
        // Setup WebDriver
        WebDriverManager.chromedriver().setup();

        // Configure Chrome options
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--disable-blink-features=AutomationControlled");

        // Create WebDriver instance
        WebDriver driver = new ChromeDriver(options);

        try {
            // Open direct Elsevier login page
            String loginUrl = "https://www.scopus.com/authid/detail.uri?authorId=36550140000";

            System.out.println("Opening Elsevier login page...");
            System.out.println("URL: " + loginUrl);

            driver.get(loginUrl);

            // Wait for page to load
            Thread.sleep(5000);

            // Print current page info
            System.out.println("Current URL: " + driver.getCurrentUrl());
            System.out.println("Page Title: " + driver.getTitle());
            System.out.println("Page Source Length: " + driver.getPageSource().length() + " characters");

            System.out.println("\n=== LOGIN PAGE SHOULD BE OPEN NOW ===");
            System.out.println("Please inspect the following elements and provide their HTML:");
            System.out.println("1. Email input field");
            System.out.println("2. Continue button");
            System.out.println("3. Any error messages (if visible)");

            System.out.println("\nKeeping browser open for 60 seconds for inspection...");

            // Keep browser open for inspection
            Thread.sleep(100000); // 60 seconds to inspect

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Close browser
            driver.quit();
            System.out.println("Browser closed.");
        }
    }
}