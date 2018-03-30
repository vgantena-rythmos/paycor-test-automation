package org.application.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import cucumber.api.Scenario;
import cucumber.api.java.Before;

public class DashboardPage extends BasePage {
	
	WebDriver driver;
	
	By IMG_UNAPPROVED_TIME_CARDS = By.xpath("//img[contains(@onclick,'aeE0_ID')]");
	By TBL_RESOURCE_INFO         = By.xpath("//table[@id='aeE0_ID']/tbody");
	
	public DashboardPage(){
		this.driver = getDriver();
	}


	public int unapprovedTimeCards(){
		switchToFrame(2);
		waitForElement(IMG_UNAPPROVED_TIME_CARDS).click();	    
		WebElement unapproved_table = waitForElement(TBL_RESOURCE_INFO);
		List<WebElement> rows_table=unapproved_table.findElements(By.tagName("tr"));
		int cards_count =0;
		int rows_count = rows_table.size();
		for (int row = 0; row < rows_count; row++) {  
			boolean data_flag = true;
			List < WebElement > Columns_row = rows_table.get(row).findElements(By.tagName("td"));    	    
			int columns_count = Columns_row.size();
			
			for (int column = 0; column < columns_count; column++) {   
				if(Columns_row.get(column).getAttribute("onclick")!=null){
					String celtext = Columns_row.get(column).getText();
					cards_count++;
					reportEvent("Unapproved Time Card Resource Name : " + celtext );
				}else{
					data_flag = false;
					break;
				}
			}
			if(!data_flag){
				break;
			}
			
		}
		
		return cards_count;
	}

}
