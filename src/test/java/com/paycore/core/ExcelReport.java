package com.paycore.core;

import java.io.File;
import java.io.FileInputStream;

import jxl.Sheet;
import jxl.Workbook;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WriteException;


public class ExcelReport {

    static Workbook wbook;
    static WritableWorkbook wwbCopy;
    static String ExecutedTestCasesSheet;
    static WritableSheet shSheet;
    public static int usedRow =0;
    
    public static void readExcel()
    {
    try{
    wbook = Workbook.getWorkbook(new File(".\\Template\\Timecards_Reports.xls"));
    wwbCopy = Workbook.createWorkbook(new File("target\\Timecards_Reports.xls"), wbook);
    shSheet = wwbCopy.getSheet(0);
    }
    catch(Exception e)
    {
        e.printStackTrace();
    }
    }
    
    public static void setValueIntoCell(int iColumnNumber, String strData) throws WriteException
    {
    	
        WritableSheet wshTemp = wwbCopy.getSheet("Timecrad_Details");
        WritableFont cellFont = new WritableFont(WritableFont.TIMES, 9);
        WritableCellFormat cellFormat = new WritableCellFormat(cellFont);
        cellFormat.setBorder(Border.ALL, BorderLineStyle.THIN);
        Label labTemp = new Label(iColumnNumber, usedRow, strData, cellFormat);
        
        
        try {
            wshTemp.addCell(labTemp);
             } 
            catch (Exception e) 
            {
                e.printStackTrace();
            }
    }
    
    public static void closeFile()
    {
        try {
            // Closing the writable work book
            wwbCopy.write();
            wwbCopy.close();

            // Closing the original work book
            wbook.close();
        } catch (Exception e)

        {
            e.printStackTrace();
        }
    }
    
    public Object[][] getResourceNames(){
		String sheetName = "Resource_Details";
		String fileName = "."+"\\TestData\\"+"Resources.xls";
		try {
			FileInputStream fs = new FileInputStream(fileName);
			Workbook wb = Workbook.getWorkbook(fs);
			Sheet sh = wb.getSheet(sheetName); int j=0, No_Of_Rows=0;
			int totalNoOfCols = sh.getColumns(); int totalNoOfRows = sh.getRows();

			for (int rCnt= 1 ; rCnt < totalNoOfRows; rCnt++) {
				String Run_Flag_Count = sh.getCell(3,rCnt).getContents();
				if(Run_Flag_Count.equalsIgnoreCase("YES")){
					No_Of_Rows = No_Of_Rows+1;
				}
			}

			Object[][] Data = new Object[No_Of_Rows][2];

			for (int r= 1 ; r < totalNoOfRows; r++) {
				String Run_Flag = sh.getCell(3,r).getContents();
				if(Run_Flag.equalsIgnoreCase("YES")){
					for (int c=0; c < totalNoOfCols-2; c++) {
						Data[j][c] = sh.getCell(c+1, r).getContents(); 
					}
					j=j+1;
				}

			}
			wb.close();
			return Data;
		} catch (Exception e) {
			e.printStackTrace();
			return null;

		}
    } 
}