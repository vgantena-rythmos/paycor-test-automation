package com.paycore.core;

import org.application.pages.LoginPage;
import org.application.pages.TimecardsPage;
import org.openqa.selenium.WebDriver;
import org.selenium.core.WebDriverFactory;
import org.selenium.core.WebDriverManager;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import jxl.write.WriteException;

public class Timecards {
	ExcelReport dataMap;
	LoginPage loginPage;
	TimecardsPage timecardsPage;
	@BeforeClass
	public void init(){
		dataMap =new ExcelReport();
		dataMap.readExcel();
		final WebDriver driver = WebDriverFactory.createInstance();
        WebDriverManager.setWebDriver(driver);
	}
	
	@Test(priority = 1, dataProvider = "ResourceDetails")
	public void test(String resourceName, String supervisorName) throws WriteException{
		System.out.println("Resource Name: "+ resourceName +" Supervisor Name : "+ supervisorName);
		//Step-3
			timecardsPage= new TimecardsPage();
			timecardsPage.resourceHours(resourceName, supervisorName);
			System.out.println("Done...for : " + resourceName);
        
	}
	
	@AfterClass
	public void teardown(){
		ExcelReport.closeFile();
		WebDriverManager.tearDownDriver();
	}
		
	@Test(priority = 0)
	public void sample() throws WriteException{
		//Step-1
		loginPage = new LoginPage();
		loginPage.applicationLogin();
		
		/*//Step-2
		timecardsPage= new TimecardsPage();
		timecardsPage.resorceTimeCardsDetails();*/
		
	}


	@DataProvider(name = "ResourceDetails")
	public Object[][] getResourceDetails(){
		return dataMap.getResourceNames();

	}



}
