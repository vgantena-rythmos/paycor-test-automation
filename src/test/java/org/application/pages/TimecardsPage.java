package org.application.pages;

import java.util.List;
import java.util.concurrent.TimeUnit;

import jxl.write.WriteException;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.paycore.core.ExcelReport;

public class TimecardsPage extends BasePage{
	WebDriver driver;
	int row_increment = 0 ;
	By LNK_TIMECARDS = By.xpath("//div[@title='Time Cards']");
	By IMG_APPROVED  = By.xpath("//img[@title='Approved']");
	By IMG_LOCKED    = By.xpath("//img[@title='Locked']");
	By TBL_TIME      = By.xpath("//table[@id='Spreadsheet0_ID']/tbody");
	By TBL_RESOURCE  = By.xpath("//table/tbody/tr[@id='PayDesSheet0R0_ID']/td[1]");
	By LNK_DETAIL    = By.xpath("//span[@selected='0' and text()='Detail']");
	By LNK_WORKSHEET    = By.xpath("//span[text()='Worksheet']");
	
	WebDriverWait wait;
	
	public TimecardsPage(){
		this.driver = getDriver();
	}

	
	public boolean verifyElement(By ele){
		List<WebElement> elements = getDriver().findElements(ele);
		if(elements.size()>0){
			return true;
		}else{
			return false;
		}
	}
	public void resorceTimeCardsDetails(String resourceName) throws WriteException{
		navigateTimeCard();
		switchToDefaultContent();		
		navigateResource(resourceName);		
		String approvals = null;
		String lock=null;

		if(verifyElement(IMG_APPROVED)){			
			ExcelReport.setValueIntoCell(3, "Yes");
		}else{
			ExcelReport.setValueIntoCell(3, "No");
		}
		if(verifyElement(IMG_LOCKED)){			
			ExcelReport.setValueIntoCell(4, "Yes");
		}else{
			ExcelReport.setValueIntoCell(4, "No");
		}

		//System.out.println("Resource Card is :" + approvals + "\t" + lock);

	}
	public void navigateTimeCard(){
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		switchToFrame(1);		
		waitForElement(LNK_TIMECARDS).click();
	}
	public void navigateResource(String resourceName){
		switchToFrame(2);
		try {
			Thread.sleep(9000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//waitForElement(TBL_RESOURCE).click();
		//getDriver().findElement(TBL_RESOURCE).click();
		getDriver().findElement(By.xpath("//td[contains(text(),'" + resourceName +"')]")).click();
		try {
			Thread.sleep(9000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		wait = new WebDriverWait(getDriver(), 60);
		//waitForElement(LNK_DETAIL).click();
		checkElement(LNK_DETAIL);
		wait.until(ExpectedConditions.elementToBeClickable(LNK_DETAIL)).click();
		
	}

	
	public void resourceHours(String resourceName, String supervisorName) throws WriteException{
		ExcelReport.usedRow= ExcelReport.usedRow+1 ;
		resorceTimeCardsDetails(resourceName);
		wait = new WebDriverWait(getDriver(), 20);		
		wait.until(ExpectedConditions.stalenessOf(getDriver().findElement(TBL_TIME)));
		highlightElement(getDriver().findElement(TBL_TIME));
		List<WebElement> time_table = getDriver().findElements(TBL_TIME);
		
		//insert serial number
		ExcelReport.setValueIntoCell(0, Integer.toString(ExcelReport.usedRow));
		//Set ResourceName
		ExcelReport.setValueIntoCell(1, resourceName);
		//Set supervisor Name
		ExcelReport.setValueIntoCell(2, supervisorName);
		
		
		for(int tCnt=1; tCnt<=time_table.size()-1; tCnt++){
			WebElement s_table = time_table.get(tCnt);				
			List<WebElement> rows_table=s_table.findElements(By.tagName("tr"));
			int rows_count = rows_table.size();
			for(int rCnt=0;rCnt<rows_count;rCnt++){
				List<WebElement> cols_table=rows_table.get(rCnt).findElements(By.tagName("td"));
				/*int cols_count = cols_table.size();
				for(int cCnt=0;cCnt<cols_count;cCnt++){
					cols_data=cols_table.get(cCnt).getText();
					System.out.print(cols_data + "\t");
					//reportEvent(cols_data + "\t");
				}*/
				String payDesig= cols_table.get(0).getText(); 
				
				switch(payDesig.toUpperCase()){
				case "REGULAR":
					ExcelReport.setValueIntoCell(5, cols_table.get(2).getText());
					break;
				case "PTO":
					ExcelReport.setValueIntoCell(6, cols_table.get(2).getText());
					break;
				case "UPT":
					ExcelReport.setValueIntoCell(7, cols_table.get(2).getText());
					break;
				case "FMLA":
					ExcelReport.setValueIntoCell(8, cols_table.get(2).getText());
					break;
				case "HOL":
					ExcelReport.setValueIntoCell(9, cols_table.get(2).getText());
					break;
				case "TOTALS":
					ExcelReport.setValueIntoCell(10, cols_table.get(2).getText());
					break;
				
				}
							

			}

		}
		switchToDefaultContent();
		
	}
	
	
}
