package org.application.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LeftMenuPage extends BasePage{
	
	WebDriver driver;
	
	By LNK_TIMECARDS = By.xpath("//div[@title='Time Cards']");
	
	public LeftMenuPage(){
		this.driver = driver.switchTo().frame(1);
	}
	
	public void navigateTimeCards(){
		waitForElement(LNK_TIMECARDS).click();
	}
	

}
