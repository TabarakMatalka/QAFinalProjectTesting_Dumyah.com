package Dumyah_FinalProjectQA.Dumyah_FinalProject;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

public class ExcelReportListener implements ITestListener {

    Workbook workbook = new XSSFWorkbook();
    Sheet sheet = workbook.createSheet("Test Results");
    int rowNum = 0;

    public ExcelReportListener() {
        Row header = sheet.createRow(rowNum++);
        header.createCell(0).setCellValue("Test Name");
        header.createCell(1).setCellValue("Status");
        header.createCell(2).setCellValue("Time");
    }

    private void writeResult(ITestResult result, String status) {
        Row row = sheet.createRow(rowNum++);
        row.createCell(0).setCellValue(result.getName());
        row.createCell(1).setCellValue(status);
        row.createCell(2).setCellValue(new Date(result.getEndMillis()).toString());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        writeResult(result, "PASS");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        writeResult(result, "FAIL");
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        writeResult(result, "SKIPPED");
    }

    @Override
    public void onFinish(ITestContext context) {
        try {
            // Create the directory if it doesn't exist
            File directory = new File("test-output");
            if (!directory.exists()) {
                directory.mkdirs();
            }

            FileOutputStream outputStream = new FileOutputStream("test-output/TestResults.xlsx");
            workbook.write(outputStream);
            workbook.close();
            outputStream.close();

            System.out.println("✅ Excel report saved to test-output/TestResults.xlsx");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
