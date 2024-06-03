package com.Cucumber.automation.framework.utils;

import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;

public class ExcelReader {
    protected String filePath;
    protected String sheetName;
    protected String valueColumnName;
    protected int valueColumn;
    protected HashMap<String, String> map;

    public ExcelReader(String sheetName) throws IOException {
        this.filePath = System.getProperty("user.dir") + File.separator + PropertyReader.getFieldValue("testDataFilePath");
        this.sheetName = sheetName;
        this.valueColumnName = null;
        this.setMap();
    }

    public ExcelReader(String sheetName, String valueColumnName) throws IOException {
        this.filePath = System.getProperty("user.dir") + File.separator + PropertyReader.getFieldValue("testDataFilePath");
        this.sheetName = sheetName;
        this.valueColumnName = valueColumnName;
        this.setMap();
    }

    public String getFilePath() {
        return this.filePath;
    }

    public String getSheetName() {
        return this.sheetName;
    }

    public String getValueColumnName() {
        return this.valueColumnName;
    }

    public String getURL(String environmentName) throws IOException {
        String url = null;
        if (environmentName == null) {
            return null;
        } else {
            FileInputStream file = new FileInputStream(this.filePath);

            try (Workbook book = WorkbookFactory.create(file)) {
                Sheet sheet = book.getSheet("EnvironmentInfo");
                DataFormatter fmt = new DataFormatter();

                for (int i = 1; i <= sheet.getLastRowNum(); ++i) {
                    String rowEnvironmentName;
                    String rowUrl;
                    try {
                        Row row = sheet.getRow(i);
                        rowEnvironmentName = fmt.formatCellValue(row.getCell(0));
                        rowUrl = fmt.formatCellValue(row.getCell(1));
                    } catch (NullPointerException npe) {
                        continue;
                    }
                    rowEnvironmentName = rowEnvironmentName.trim();
                    rowUrl = rowUrl.trim();
                    if (rowEnvironmentName.contentEquals(environmentName)) {
                        url = rowUrl;
                        break;
                    }
                }
            }

            file.close();

            return url;
        }
    }

    public String getUserName(String userType, String environmentName) throws IOException {
        String userName = null;

        if (userType != null && environmentName != null) {
            FileInputStream file = new FileInputStream(this.filePath);

            try (Workbook book = WorkbookFactory.create(file)) {
                Sheet sheet = book.getSheet("userInfo");
                DataFormatter fmt = new DataFormatter();

                for (int i = 1; i <= sheet.getLastRowNum(); ++i) {
                    String rowUserType;
                    String rowEnvironmentName;
                    String rowUserName;
                    try {
                        Row row = sheet.getRow(i);
                        rowUserType = fmt.formatCellValue(row.getCell(0));
                        rowEnvironmentName = fmt.formatCellValue(row.getCell(1));
                        rowUserName = fmt.formatCellValue(row.getCell(2));
                    } catch (NullPointerException npe) {
                        continue;
                    }

                    rowUserType = rowUserType.trim();
                    rowEnvironmentName = rowEnvironmentName.trim();
                    rowUserName = rowUserName.trim();

                    if (rowUserType.contentEquals(userType) && rowEnvironmentName.contentEquals(environmentName)) {
                        userName = rowUserName;
                        break;
                    }
                }
            }

            file.close();
            return userName;
        } else {
            return null;
        }
    }

    public String getPassword(String userType, String environmentName) throws IOException {
        String password = null;

        if (userType != null && environmentName != null) {
            FileInputStream file = new FileInputStream(this.filePath);

            try (Workbook book = WorkbookFactory.create(file)) {
                Sheet sheet = book.getSheet("userInfo");
                DataFormatter fmt = new DataFormatter();

                for (int i = 1; i <= sheet.getLastRowNum(); ++i) {
                    String rowUserType;
                    String rowEnvironmentName;
                    String rowPassword;

                    try {
                        Row row = sheet.getRow(i);
                        rowUserType = fmt.formatCellValue(row.getCell(0));
                        rowEnvironmentName = fmt.formatCellValue(row.getCell(1));
                        rowPassword = fmt.formatCellValue(row.getCell(3));
                    } catch (NullPointerException npe) {
                        continue;
                    }

                    rowUserType = rowUserType.trim();
                    rowEnvironmentName = rowEnvironmentName.trim();
                    rowPassword = rowPassword.trim();

                    if (rowUserType.contentEquals(userType) && rowEnvironmentName.contentEquals(environmentName)) {
                        password = rowPassword;
                        break;
                    }
                }
            }

            file.close();

            return password;
        } else {
            return null;
        }
    }

    public void setSheetName(String newSheetName, String valueColumnName) throws IOException {
        this.sheetName = newSheetName;
        this.valueColumnName = valueColumnName;
        this.setMap();
    }

    public void setValueColumnName(String valueColumnName) throws IOException {
        this.valueColumnName = valueColumnName;
        this.setMap();
    }

    private void setMap() throws IOException {
        this.map = new HashMap<>();
        FileInputStream file = new FileInputStream(this.filePath);

        try (Workbook book = WorkbookFactory.create(file)) {
            Sheet sheet = book.getSheet(this.sheetName);
            DataFormatter fmt = new DataFormatter();
            Row row = sheet.getRow(sheet.getFirstRowNum());
            int i;
            if (this.valueColumnName != null) {
                for (i = 1; i <= row.getFirstCellNum(); ++i) {
                    if (row.getCell(i).toString().contentEquals(this.valueColumnName)) {
                        this.valueColumn = i;
                        break;
                    }
                }
            } else {
                this.valueColumn = 1;
            }

            for (i = 0; i <= sheet.getLastRowNum(); ++i) {
                String v1;
                String v2;
                try {
                    row = sheet.getRow(i);
                    v1 = row.getCell(0).toString();
                    v2 = fmt.formatCellValue(row.getCell(this.valueColumn));
                } catch (NullPointerException npe) {
                    continue;
                }

                v1 = v1.trim();
                v2 = v2.trim();
                this.map.put(v1, v2);
            }
        }

        file.close();
    }

    public String getValue(String name) {
        return this.map.get(name);
    }

    public void clearAll() {
        this.map.clear();
    }
}
