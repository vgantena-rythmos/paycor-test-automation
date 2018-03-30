package org.application.pages;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.exceptions.AssertionException;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.selenium.core.WebDriverManager;

import com.google.common.base.Function;

import cucumber.api.Scenario;

public class BasePage {
	
	private static Scenario report;
	static Integer DEFAULT_WAIT_RETRY = 1000;
	static Integer DEFAULT_WAIT_LIMIT = 75;
	static Integer SHORT_WAIT = 50;
	
	public enum Closure {
		CONTAINS_STRING_VERIFICATION, EQUALS_VERIFICATION,NOT_EQUALS_VERIFICATION
    }
	
	
	public void setReporter(Scenario report){
		this.report = report;
	}
	
	private static WebDriver webDriver;
    
	public BasePage(){
		this.webDriver = WebDriverManager.getDriver();
	}
	
	 public WebDriver getDriver() {
		    return WebDriverManager.getDriver();
		}
	
	public void navigateUrl(String url){
    	getDriver().get(url);
    }
    
    public void verifyUrl(String url){
    	System.out.println(url);
    	System.out.println(webDriver.getCurrentUrl());
    	verify(Closure.EQUALS_VERIFICATION,"url as expected.","url not as expected.","vgantena", "vgantena");
    }
    
    public void switchToFrame(int index){
		WebDriverManager.setWebDriver(getDriver().switchTo().frame(index));
	}
    public void switchToDefaultContent(){
    	WebDriverManager.setWebDriver(getDriver().switchTo().defaultContent());
    }
	
	void highlightElement(WebElement webElement) {
		try {
			if (getDriver() instanceof JavascriptExecutor) {
				((JavascriptExecutor) getDriver()).executeScript("arguments[0].style.border='4px solid green';arguments[0].style.background='yellow'", webElement);
			}
		} catch (Exception e) {
			// If something is wrong with the element, do not highlight
		}
	}
    
	public void verify(Closure verification,  String successMessage,String errorMessage, Object expectedValue, Object actualValue) {
		boolean takeScreenshot = true; // always true for screenshot
		String message=null;
		String successMessageToAppend = "<b style='color:green'>PASSED: </b>";
		String htmlFailedMessageToAppend = "<b style='color:red'>FAILED: </b>";
		try {
			boolean result = closureVerification(verification,expectedValue, actualValue);
			if (result == false) {
				message = htmlFailedMessageToAppend + errorMessage;
				assertTrue(false);
				throw new AssertionException(message);
			} else {
				message = successMessageToAppend + successMessage;
			}
		} catch (AssertionException ae) {
			try {
				throw ae;
			} catch (AssertionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (Exception e) {
			message = htmlFailedMessageToAppend + errorMessage;
			try {
				throw new AssertionException(message);
			} catch (AssertionException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} finally {
			String valuesPrint = "<b>Expected: </b>" + expectedValue + "<b> Actual: </b>" + actualValue;
			message = message + " " + valuesPrint;
			if (takeScreenshot) {
				String fileName = getScreenshot();
				String screenshotHtml = " <a href='" + fileName + "'>[screenshot]</a>";
				reportEvent(message + screenshotHtml);
			} else {
				reportEvent(message);
			}
		}
	}
	
	
	/* method for taking screenshot at runtime	 */
	public String getScreenshot(){
		File screenshot;
		if (getDriver() instanceof TakesScreenshot) {
			screenshot = ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.FILE);
		} else {
			// for Selenium Grid
			WebDriver augmentedDriver = new Augmenter().augment(getDriver());
			screenshot = ((TakesScreenshot) augmentedDriver).getScreenshotAs(OutputType.FILE);
		}
		String fileName = UUID.randomUUID().toString().substring(0, 18).toUpperCase() + ".png";
		System.out.println(System.getProperty("user.dir"));
		String path = System.getProperty("user.dir")+ "/target/cucumber-reports/images/" + fileName;
		// Needs Commons IO library
		try {
			FileUtils.copyFile(screenshot, new File(path));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "images/" + fileName;
	}
	
	/* method for reporting logs to HTML file*/
	public static void reportEvent(String message){
		report.write(message);
	}
	
	public static boolean closureVerification(Closure closure, Object expectedValue, Object actualValue){
		boolean flag = false;
		switch (closure) {
        case CONTAINS_STRING_VERIFICATION:
        	flag = actualValue.toString().contains(expectedValue.toString());
        	break;
        case EQUALS_VERIFICATION:
        	 if(expectedValue == actualValue){
        		 flag = true;
        	}
        	break;
        case NOT_EQUALS_VERIFICATION:
        	flag = (expectedValue != actualValue);
        	break;
    }
		return flag;
		
	}
	
	
	/**
	 * Get a {@link WebElement} according to the {@link By} passed as parameter. It is waiting for any AJAX request to finish
	 * then is looking for the element. If something wrong (any {@link WebDriverException) or timeout) it is retrying after reach the retryInterval.
	 *
	 * @param byElement locator to find the wanted element
	 * @param parentElement if it is different that null then it would lookup byElement using that element as a parent
	 * @param throwExceptions If it is true is throwing according {@link Exception} to the caller.
	 * @param waitLimit Max wait limit time
	 * @param retryInterval Max retry interval time
	 * @param ensureDisplayed If it is true is checking that the element is displayed
	 * @param ensureAjax If it is true is checking for AJAX request before lookup the element.
	 * @return A webElement according to the byElement parameter
	 */
	public WebElement waitForElement(final By byElement){
		final WebElement parentElement = null;
		boolean throwExceptions = true;
		int waitLimit = DEFAULT_WAIT_LIMIT;
		int retryInterval = DEFAULT_WAIT_RETRY;
		boolean ensureAjax = true;
		final List<WebElement> resultElement = new ArrayList<WebElement>();
		int timeout = waitLimit;
		int retry = retryInterval;
		System.currentTimeMillis();
		WebDriverWait wdw = new WebDriverWait(getDriver(), timeout, retry);
		if (ensureAjax) {
			//waitForAjaxRequestToFinish()
		}
		try {
			wdw.until(new Function<WebDriver, WebElement>() {
				@SuppressWarnings("unused")
				public WebElement apply(WebDriver wd) {
					List<WebElement> elements;
					try {
						if (parentElement != null) {
							elements = parentElement.findElements(byElement);
						} else {
							elements = wd.findElements(byElement);
						}
					} catch (WebDriverException w) {
						System.out.println(" #################### RETRYING ####################" + " findElements(" + byElement.toString() + ")");
						if (parentElement != null) {
							elements = parentElement.findElements(byElement);
						} else {
							elements = wd.findElements(byElement);
						}
					}
					if (elements.size() > 0 && (false || elements.get(0).isDisplayed()==true)) {
						resultElement.add(0, elements.get(0));
					}
					return resultElement.get(0);
				}
			});
		} catch (WebDriverException wde) {
			if (throwExceptions) {
				//addScreenshot("App state at time of error")
				throw new WebDriverException(wde.getMessage() + " Page - Element '" + byElement + "' failed lookup through Selenium.");
			}
		}
		return resultElement.get(0);
	}
	
	
	public void checkElement(By element){
		WebDriverWait wait = new WebDriverWait(getDriver(), 30);
		wait.until(ExpectedConditions.visibilityOfElementLocated(element));
	}
	
}
