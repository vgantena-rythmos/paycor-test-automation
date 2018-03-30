package org.selenium.core;

import java.util.Locale;
import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;


public class WebDriverFactory {
	
	Properties configFile;
	
	public enum Browser {
		FIREFOX, IE, CHROME
	}
	
	
    @SuppressWarnings("unused")
	public static WebDriver createInstance() {
        WebDriver driver = null;
        String browserName = System.getProperty("browser");
		if(browserName==null){
			browserName="chrome";  // default browser for application
		}
		System.out.println("Browser initialized with **** " + browserName + " ****");
		Browser browser = Browser.valueOf(Browser.class, System.getProperty("browser", browserName).toUpperCase(usingLocale()));
        switch (browser) {
		case FIREFOX:
			return createFirefoxDriver();
		case IE:
			return createInternetExplorerDriver();
		case CHROME:
			return createChromeDriver();
		default:
			return createFirefoxDriver();
		}
    }
    
    
    
    protected static WebDriver createFirefoxDriver() {
		System.setProperty("webdriver.firefox.marionette", System.getProperty("java.io.tmpdir")+"geckodriver.exe");
		WebDriver driver = new FirefoxDriver();
		return driver;
	}

	protected static WebDriver createChromeDriver(){
		System.setProperty("webdriver.chrome.driver", System.getProperty("java.io.tmpdir")+"chromedriver.exe");
		ChromeDriver driver = new ChromeDriver();
		return driver;
	}

	@SuppressWarnings("deprecation")
	protected static WebDriver createInternetExplorerDriver() {
		DesiredCapabilities capabilities = DesiredCapabilities.internetExplorer();
		capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS,true);
		capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
		capabilities.setCapability(InternetExplorerDriver.NATIVE_EVENTS, false);
		capabilities.setCapability(InternetExplorerDriver.REQUIRE_WINDOW_FOCUS, true);
		return new InternetExplorerDriver(capabilities);
	}
	
	
	protected static Locale usingLocale() {
		return Locale.getDefault();
	}
	
	
	public void LoadConfigOption()
	{
		configFile = new java.util.Properties();
		try {
			configFile.load(this.getClass().getClassLoader().
					getResourceAsStream("config/config.cfg"));
		}catch(Exception eta){
			eta.printStackTrace();
		}
	}

	public String getProperty(String key)
	{
		String value = this.configFile.getProperty(key);
		return value;
	}
    
    
}