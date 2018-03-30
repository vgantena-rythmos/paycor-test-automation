package org.selenium.core;

import org.openqa.selenium.WebDriver;
import org.testng.IInvokedMethod;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public class WebDriverManager {
    private static ThreadLocal<WebDriver> webDriver = new ThreadLocal<WebDriver>();
 
    public static WebDriver getDriver() {
        return webDriver.get();
    }
 
    public static void setWebDriver(WebDriver driver) {
    	driver.manage().window().maximize();
        webDriver.set(driver);
    }
    
    public static void tearDownDriver(){
    	getDriver().close();
    }
    
    
}