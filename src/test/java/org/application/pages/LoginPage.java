package org.application.pages;

import org.openqa.selenium.WebDriver;

public class LoginPage extends BasePage{
	WebDriver driver;
	
	public LoginPage(){
		this.driver = getDriver();
	}
	
	
	public void applicationLogin(){
		getDriver().get("https://"+ System.getProperty("username") + ":" + System.getProperty("password") + "@rythmosinc.attendanceondemand.com/operator/");
	}

}
