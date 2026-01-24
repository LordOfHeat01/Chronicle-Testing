package org.example;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.Duration;
import java.util.*;
import java.util.List;

public class Swing1 {

    private static JFrame mainFrame;
    private static JTextArea outputArea;
    private static JProgressBar progressBar;
    private static JButton startButton;
    private static DefaultListModel<String> articleListModel;
    private static JList<String> articleList;
    private static JLabel statusLabel;
    private static JTextField scopusIdField;
    private static JTextField emailField;
    private static JPasswordField passwordField;
    private static JCheckBox headlessCheckbox;

    public static void main(String[] args) {
        // Create and show the UI
        SwingUtilities.invokeLater(() -> createAndShowGUI());
    }

    private static void createAndShowGUI() {
        // Create main frame
        mainFrame = new JFrame("Scopus Article Fetcher");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLayout(new BorderLayout(10, 10));
        mainFrame.getContentPane().setBackground(Color.WHITE);

        // Header panel
        JPanel headerPanel = createHeaderPanel();
        mainFrame.add(headerPanel, BorderLayout.NORTH);

        // Input panel
        JPanel inputPanel = createInputPanel();
        mainFrame.add(inputPanel, BorderLayout.WEST);

        // Center panel with output and articles
        JPanel centerPanel = createCenterPanel();
        mainFrame.add(centerPanel, BorderLayout.CENTER);

        // Progress panel
        JPanel progressPanel = createProgressPanel();
        mainFrame.add(progressPanel, BorderLayout.SOUTH);

        // Set frame properties
        mainFrame.setSize(1000, 750);
        mainFrame.setLocationRelativeTo(null); // Center on screen
        mainFrame.setVisible(true);
    }

    private static JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(0, 115, 152)); // Scopus blue
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel titleLabel = new JLabel("🔍 Scopus Article Fetcher", JLabel.LEFT);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);

        JLabel subtitleLabel = new JLabel("Fetch all publications by author Scopus ID", JLabel.LEFT);
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(200, 230, 255));

        JPanel textPanel = new JPanel(new GridLayout(2, 1));
        textPanel.setBackground(new Color(0, 115, 152));
        textPanel.add(titleLabel);
        textPanel.add(subtitleLabel);

        statusLabel = new JLabel("Ready", JLabel.RIGHT);
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        statusLabel.setForeground(Color.WHITE);

        headerPanel.add(textPanel, BorderLayout.CENTER);
        headerPanel.add(statusLabel, BorderLayout.EAST);

        return headerPanel;
    }

    private static JPanel createInputPanel() {
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder("Login Information"));
        inputPanel.setBackground(Color.WHITE);
        inputPanel.setPreferredSize(new Dimension(300, 0));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Scopus ID
        gbc.gridx = 0; gbc.gridy = 0;
        inputPanel.add(new JLabel("Scopus Author ID:"), gbc);

        gbc.gridx = 1; gbc.gridy = 0;
        scopusIdField = new JTextField("56251578000");
        inputPanel.add(scopusIdField, gbc);

        // Email
        gbc.gridx = 0; gbc.gridy = 1;
        inputPanel.add(new JLabel("Email:"), gbc);

        gbc.gridx = 1; gbc.gridy = 1;
        emailField = new JTextField("vidhisingh7985@gmail.com");
        inputPanel.add(emailField, gbc);

        // Password
        gbc.gridx = 0; gbc.gridy = 2;
        inputPanel.add(new JLabel("Password:"), gbc);

        gbc.gridx = 1; gbc.gridy = 2;
        passwordField = new JPasswordField("Stella10@");
        inputPanel.add(passwordField, gbc);

        // Headless checkbox
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        headlessCheckbox = new JCheckBox("Run browser in background (headless)");
        headlessCheckbox.setSelected(false); // Default: show browser
        headlessCheckbox.setBackground(Color.WHITE);
        inputPanel.add(headlessCheckbox, gbc);

        // Info label
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        JLabel infoLabel = new JLabel("<html><small>Note: Internet connection required</small></html>");
        infoLabel.setForeground(Color.GRAY);
        inputPanel.add(infoLabel, gbc);

        return inputPanel;
    }

    private static JPanel createCenterPanel() {
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        centerPanel.setBackground(Color.WHITE);

        // Output area for progress messages
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Consolas", Font.PLAIN, 12));
        outputArea.setBackground(new Color(248, 248, 248));
        outputArea.setBorder(BorderFactory.createTitledBorder("Progress Log"));

        JScrollPane outputScroll = new JScrollPane(outputArea);
        outputScroll.setPreferredSize(new Dimension(400, 200));

        // Articles list
        articleListModel = new DefaultListModel<>();
        articleList = new JList<>(articleListModel);
        articleList.setFont(new Font("Arial", Font.PLAIN, 12));
        articleList.setBackground(new Color(255, 255, 255));
        articleList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane articleScroll = new JScrollPane(articleList);
        articleScroll.setBorder(BorderFactory.createTitledBorder("Articles Found (0)"));

        // Split pane to show both output and articles
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, outputScroll, articleScroll);
        splitPane.setResizeWeight(0.4); // Give 40% space to output, 60% to articles

        centerPanel.add(splitPane, BorderLayout.CENTER);

        return centerPanel;
    }

    private static JPanel createProgressPanel() {
        JPanel progressPanel = new JPanel(new BorderLayout(10, 10));
        progressPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        progressPanel.setBackground(Color.WHITE);

        // Progress bar
        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        progressBar.setString("Ready to start...");
        progressBar.setBackground(Color.WHITE);

        // Start button
        startButton = new JButton("🚀 Start Fetching Articles");
        startButton.setFont(new Font("Arial", Font.BOLD, 14));
        startButton.setBackground(new Color(0, 115, 152));
        startButton.setForeground(Color.WHITE);
        startButton.setFocusPainted(false);
        startButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startButton.setEnabled(false);
                startButton.setText("⏳ Fetching...");
                new Thread(() -> startScrapingProcess()).start();
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(startButton);

        progressPanel.add(progressBar, BorderLayout.CENTER);
        progressPanel.add(buttonPanel, BorderLayout.EAST);

        return progressPanel;
    }

    private static void startScrapingProcess() {
        // Get input values
        String scopusId = scopusIdField.getText().trim();
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());
        boolean headless = headlessCheckbox.isSelected();

        if (scopusId.isEmpty() || email.isEmpty() || password.isEmpty()) {
            updateOutput("❌ Please fill in all fields!\n");
            enableStartButton();
            return;
        }

        // Run the scraping in a separate thread to keep UI responsive
        SwingUtilities.invokeLater(() -> {
            outputArea.setText(""); // Clear previous output
            articleListModel.clear(); // Clear previous articles
            progressBar.setValue(0);
            statusLabel.setText("Starting...");
        });

        // Setup WebDriver - FIXED: Remove headless mode by default
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--disable-blink-features=AutomationControlled");

        // ONLY add headless argument if checkbox is selected
        if (headless) {
            options.addArguments("--headless");
            updateOutput("🔧 Running in HEADLESS mode (browser hidden)\n");
        } else {
            updateOutput("🔧 Running in VISIBLE mode (browser will open)\n");
        }

        WebDriver driver = null;
        try {
            updateStatus("Opening browser...");
            updateOutput("Initializing Chrome browser...\n");
            updateProgress(5);

            driver = new ChromeDriver(options);
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

            updateStatus("Opening author profile...");
            updateOutput("Step 1: Opening author profile page...\n");
            updateProgress(10);

            String authorUrl = "https://www.scopus.com/authid/detail.uri?authorId=" + scopusId;
            updateOutput("URL: " + authorUrl + "\n");

            driver.get(authorUrl);
            Thread.sleep(5000);

            // Extract author info
            updateStatus("Extracting author information...");
            updateOutput("\n--- EXTRACTING AUTHOR INFORMATION ---\n");
            String authorName = extractAuthorName(driver);
            int totalDocuments = extractTotalDocuments(driver);
            String[] name = authorName.split(", ");
            String formattedName = name.length > 1 ? name[1] + " " + name[0] : authorName;

            updateOutput("Author Name: " + formattedName + "\n");
            updateOutput("Total Documents: " + totalDocuments + "\n");
            updateProgress(15);

            // Continue with the rest of the scraping process...
            performFullScraping(driver, wait, email, password, formattedName, totalDocuments);

            // Enable button when done
            SwingUtilities.invokeLater(() -> {
                startButton.setEnabled(true);
                startButton.setText("🚀 Start Fetching Articles");
                progressBar.setValue(100);
                progressBar.setString("Completed!");
                statusLabel.setText("Completed");
            });

        } catch (Exception e) {
            updateOutput("❌ Error: " + e.getMessage() + "\n");
            e.printStackTrace();
            enableStartButton();
        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
    }

    private static void performFullScraping(WebDriver driver, WebDriverWait wait, String email,
                                            String password, String authorName, int totalDocuments) {
        try {
            // Step 2: Click Sign in button
            updateStatus("Signing in...");
            updateOutput("\nStep 2: Looking for Sign in button...\n");
            updateProgress(20);

            // Handle "Maybe later" popup
            updateOutput("Checking for 'Maybe later' popup...\n");
            try {
                WebElement maybeLaterButton = wait.until(ExpectedConditions.elementToBeClickable(
                        By.xpath("//button[contains(text(), 'Maybe later')]")
                ));
                updateOutput("Found 'Maybe later' popup. Clicking to dismiss...\n");
                maybeLaterButton.click();
                Thread.sleep(2000);
            } catch (Exception e) {
                updateOutput("No 'Maybe later' popup found.\n");
            }

            WebElement signInButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//span[contains(@class, 'Typography-module__lVnit') and text()='Sign in']")
            ));
            updateOutput("Found Sign in button. Clicking...\n");
            signInButton.click();
            Thread.sleep(3000);

            // Handle cookie popup
            updateOutput("\nStep 3: Checking for cookie popup...\n");
            try {
                WebElement acceptCookiesButton = wait.until(ExpectedConditions.elementToBeClickable(
                        By.id("onetrust-accept-btn-handler")
                ));
                updateOutput("Found cookie popup. Clicking 'Accept all cookies'...\n");
                acceptCookiesButton.click();
                updateOutput("Cookie popup accepted.\n");
                Thread.sleep(2000);
            } catch (Exception e) {
                updateOutput("No cookie popup found or already handled.\n");
            }

            // Email entry
            updateStatus("Entering credentials...");
            updateOutput("\nStep 4: Entering email...\n");
            updateProgress(30);

            WebElement emailFieldElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("input[id*='email'], input[name='email'], #username")
            ));
            emailFieldElement.clear();
            emailFieldElement.sendKeys(email);
            updateOutput("Email entered.\n");

            // Continue button
            updateOutput("\nStep 5: Clicking Continue...\n");
            WebElement continueButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[@id='bdd-elsPrimaryBtn' and @type='submit']")
            ));
            continueButton.click();
            Thread.sleep(5000);

            // Password entry
            updateOutput("\nStep 6: Entering password...\n");
            updateProgress(40);

            WebElement passwordFieldElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.id("bdd-password")
            ));
            passwordFieldElement.clear();
            passwordFieldElement.sendKeys(password);
            updateOutput("Password entered.\n");

            // Sign in
            updateOutput("\nStep 7: Signing in...\n");
            WebElement signInBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.cssSelector("button[id='bdd-elsPrimaryBtn'], button[type='submit']")
            ));
            signInBtn.click();
            Thread.sleep(5000);

            updateStatus("Logged in, navigating...");
            updateOutput("\nStep 8: Login completed!\n");
            updateProgress(50);

            // Edit Profile
            updateOutput("\nStep 9: Clicking Edit Profile...\n");
            WebElement editProfileButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//span[contains(@class, 'Typography-module__lVnit') and text()='Edit profile']")
            ));
            editProfileButton.click();
            Thread.sleep(3000);

            // Continue confirmation
            updateOutput("\nStep 10: Confirming...\n");
            WebElement continueConfirmButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//span[contains(@class, 'Typography-module__lVnit') and text()='Continue']")
            ));
            continueConfirmButton.click();
            Thread.sleep(5000);

            // Documents
            updateStatus("Extracting articles...");
            updateOutput("\nStep 11: Clicking Documents...\n");
            updateProgress(60);

            WebElement documentsButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//span[contains(@class, 'Typography-module__lVnit') and text()='Documents']")
            ));
            documentsButton.click();
            Thread.sleep(5000);

            // Extract articles
            updateOutput("\nStep 12: Starting article extraction...\n");
            updateOutput("Based on document count: " + totalDocuments + " documents\n");

            Set<String> allArticles = extractAllArticlesWithSmartPagination(driver, wait, totalDocuments);

            // Display results
            updateOutput("\n" + "=".repeat(60) + "\n");
            updateOutput("=== EXTRACTION COMPLETED ===\n");
            updateOutput("=".repeat(60) + "\n");
            updateOutput("Author: " + authorName + "\n");
            updateOutput("Expected Documents: " + totalDocuments + "\n");
            updateOutput("Actual Articles Found: " + allArticles.size() + "\n");

            // Add articles to UI list
            int count = 1;
            for (String title : allArticles) {
                addArticleToList(count + ". " + title);
                count++;
            }

            updateStatus("Extraction completed!");
            updateProgress(100);

        } catch (Exception e) {
            updateOutput("❌ Error during scraping: " + e.getMessage() + "\n");
            throw new RuntimeException(e);
        }
    }

    // [Keep all the other methods exactly the same as previous version]
    // UI update methods, scraping methods, etc.

    // ... (include all the other methods from the previous version)

    // UI update methods
    private static void updateOutput(String text) {
        SwingUtilities.invokeLater(() -> {
            outputArea.append(text);
            outputArea.setCaretPosition(outputArea.getDocument().getLength());
        });
    }

    private static void updateStatus(String status) {
        SwingUtilities.invokeLater(() -> {
            statusLabel.setText(status);
        });
    }

    private static void updateProgress(int value) {
        SwingUtilities.invokeLater(() -> {
            progressBar.setValue(value);
        });
    }

    private static void addArticleToList(String articleTitle) {
        SwingUtilities.invokeLater(() -> {
            articleListModel.addElement(articleTitle);
            updateArticlesCount(articleListModel.size());
        });
    }

    private static void updateArticlesCount(int count) {
        SwingUtilities.invokeLater(() -> {
            JViewport viewport = (JViewport) articleList.getParent().getParent();
            JScrollPane scrollPane = (JScrollPane) viewport.getParent();
            scrollPane.setBorder(BorderFactory.createTitledBorder("Articles Found (" + count + ")"));
        });
    }

    private static void enableStartButton() {
        SwingUtilities.invokeLater(() -> {
            startButton.setEnabled(true);
            startButton.setText("🚀 Start Fetching Articles");
            statusLabel.setText("Error occurred");
        });
    }

    // ========== EXISTING SCRAPING METHODS ==========

    private static String extractAuthorName(WebDriver driver) {
        try {
            WebElement authorNameElement = driver.findElement(
                    By.cssSelector("h1[data-testid='author-profile-name']")
            );
            return authorNameElement.getText().trim();
        } catch (Exception e) {
            updateOutput("Could not extract author name: " + e.getMessage() + "\n");
            return "Unknown Author";
        }
    }

    private static int extractTotalDocuments(WebDriver driver) {
        try {
            WebElement documentCountElement = driver.findElement(
                    By.cssSelector("span[data-testid='uniclkable-count']")
            );
            String countText = documentCountElement.getText().trim();
            updateOutput("Document count found: " + countText + "\n");
            return Integer.parseInt(countText);
        } catch (Exception e) {
            updateOutput("Could not extract document count: " + e.getMessage() + "\n");
            return 10;
        }
    }

    private static Set<String> extractAllArticlesWithSmartPagination(WebDriver driver, WebDriverWait wait, int totalDocuments) {
        Set<String> allArticles = new LinkedHashSet<>();

        try {
            updateOutput("Starting SMART pagination extraction...\n");
            updateOutput("Total documents to extract: " + totalDocuments + "\n");

            int articlesPerPage = 10;
            int pagesNeeded = (int) Math.ceil((double) totalDocuments / articlesPerPage);
            updateOutput("Pages needed: " + pagesNeeded + " (based on " + articlesPerPage + " articles per page)\n");

            int maxPages = Math.min(pagesNeeded, 20);
            updateOutput("Safety limit: Processing maximum " + maxPages + " pages\n");

            int progressStep = 40 / maxPages; // Remaining 40% progress distributed across pages

            for (int pageNumber = 1; pageNumber <= maxPages; pageNumber++) {
                updateOutput("\n--- Processing Page " + pageNumber + " of " + maxPages + " ---\n");
                updateProgress(60 + (pageNumber * progressStep));

                wait.until(ExpectedConditions.presenceOfElementLocated(
                        By.cssSelector(".document-title, .Table-module__oZx3T, .ReviewProfileDetails-module__vxcvN")
                ));

                Set<String> currentPageArticles = extractArticlesFromCurrentPage(driver);
                int previousSize = allArticles.size();
                allArticles.addAll(currentPageArticles);
                int newArticles = allArticles.size() - previousSize;

                if (newArticles == 0 && pageNumber > 1) {
                    updateOutput("No new articles found. Stopping.\n");
                    break;
                }

                updateOutput("Page " + pageNumber + ": Found " + currentPageArticles.size() + " articles (" + newArticles + " new)\n");
                updateOutput("Total so far: " + allArticles.size() + " / " + totalDocuments + " articles\n");

                if (allArticles.size() >= totalDocuments) {
                    updateOutput("Reached expected document count. Stopping.\n");
                    break;
                }

                if (pageNumber < maxPages) {
                    WebElement nextButton = findNextButton(driver);
                    if (nextButton != null && nextButton.isEnabled() && nextButton.isDisplayed()) {
                        updateOutput("Clicking Next to go to page " + (pageNumber + 1) + "...\n");
                        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", nextButton);
                        Thread.sleep(3000);
                    } else {
                        updateOutput("Next button not available. Stopping early.\n");
                        break;
                    }
                }
            }

            updateOutput("\nSMART pagination completed! Processed pages\n");
            updateOutput("Expected: " + totalDocuments + " documents, Actual: " + allArticles.size() + " articles\n");

        } catch (Exception e) {
            updateOutput("Error in pagination: " + e.getMessage() + "\n");
        }

        return allArticles;
    }

    private static Set<String> extractArticlesFromCurrentPage(WebDriver driver) {
        Set<String> articles = new LinkedHashSet<>();

        try {
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

            if (titleElements.isEmpty()) {
                titleElements = driver.findElements(By.tagName("h5"));
            }

            for (WebElement element : titleElements) {
                String title = element.getText().trim().replaceAll("^\"|\"$", "").trim();
                if (!title.isEmpty() && title.length() > 20 &&
                        !title.equals("Next") && !title.equals("Documents") &&
                        !title.equals("Edit profile") && !title.contains("Sign in")) {
                    articles.add(title);
                }
            }

        } catch (Exception e) {
            updateOutput("Error extracting articles: " + e.getMessage() + "\n");
        }

        return articles;
    }

    private static WebElement findNextButton(WebDriver driver) {
        try {
            return driver.findElement(
                    By.xpath("//span[contains(@class, 'Typography-module__lVnit') and text()='Next']")
            );
        } catch (Exception e) {
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