package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import io.github.bonigarcia.wdm.WebDriverManager;

public class LoginFormExample {
    public static void main(String[] args) {
        // ✅ Setup ChromeDriver automatically
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();

        // 1️⃣ Open login page
        driver.get("https://practicetestautomation.com/practice-test-login/");

        // 2️⃣ Enter username
        WebElement username = driver.findElement(By.id("username"));
        username.sendKeys("student");

        // 3️⃣ Enter password
        WebElement password = driver.findElement(By.id("password"));
        password.sendKeys("Password123");

        // 4️⃣ Click login button
        WebElement loginButton = driver.findElement(By.id("submit"));
        loginButton.click();

        // 5️⃣ Print the new page title (after login)
        System.out.println("After Login Page Title: " + driver.getTitle());

        // Close browser
        driver.quit();
    }
}
