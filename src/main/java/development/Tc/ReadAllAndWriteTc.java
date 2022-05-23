package development.Tc;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Iterator;

public class ReadAllAndWriteTc {
    private static final String filePath = "C:\\Users\\mgmet\\Desktop\\deneme.xlsx";

    static ReadAllAndWriteTc read = new ReadAllAndWriteTc();

    public static void main(String[] args) {
        read.ReadCellData();
    }

    public void ReadCellData() {
        String value = null, result = null;
        int vRow = 0, vColumn = 0;
        boolean check;

        try {
            FileInputStream file = new FileInputStream(new File(filePath));
            //Create Workbook instance holding reference to .xlsx file
            XSSFWorkbook workbook = new XSSFWorkbook(file);
            //Get first/desired sheet from the workbook
            XSSFSheet sheet = workbook.getSheetAt(0);
            //Iterate through each rows one by one
            Iterator<Row> rowIterator = sheet.iterator();
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                //For each row, iterate through all the columns
                Iterator<Cell> cellIterator = row.cellIterator();

                while (cellIterator.hasNext()) {
                    //o satirdaki siradi hücreyi okur
                    result = "";
                    Cell cell = cellIterator.next();
                    if (cell.getCellType() == CellType.STRING) {
                        value = cell.getStringCellValue();
                    } else if (cell.getCellType() == CellType.NUMERIC) {
                        value = String.valueOf(cell.getNumericCellValue());
                        for (int i = 0; i < value.length() - 3; i++) {
                            if (i == 1) {
                                continue;
                            }
                            result += String.valueOf(value.charAt(i));
                        }
                        vRow = cell.getRowIndex();
                        vColumn = cell.getColumnIndex();
                    }
                    check = read.TcNoCheck(result);
                    if (check) {
                        read.ExcelUpdateCell(vRow, vColumn);
                    }
                }
            }
            file.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean TcNoCheck(String TcNo) {
        if (TcNo == null || TcNo.length() != 11) {
            System.out.println("Tc No 11 karakter olmalı");
            return false;
        }
        int[] arr = read.split(TcNo);

        if (arr[0] == 0) {
            System.out.println("Tc No ilk rakamı 0 olamaz");
            return false;
        }

        int odd = 0, even = 0, result = 0;
        odd = (arr[0] + arr[2] + arr[4] + arr[6] + arr[8]) * 7;
        even = arr[1] + arr[3] + arr[5] + arr[7];
        result = (odd - even) % 10;
        if (result != arr[9]) {
            System.out.println("Method 4 sağlanmıyor.");
            return false;
        }

        result = 0;
        for (int i = 0; i < 10; i++) {
            result += arr[i];
        }
        result = result % 10;
        if (result != arr[10]) {
            System.out.println("Method 5 sağlanmıyor.");
            return false;
        }
        return true;
    }

    public int[] split(String TcNo) {
        int[] numbers = new int[11];
        for (int i = 0; i < 11; i++) {
            numbers[i] = Integer.valueOf(TcNo.substring(i, (i + 1)));
        }
        return numbers;
    }

    public void ExcelUpdateCell(int vRow, int vColumn) {
        try {

            // Create an object of FileInputStream class to read excel file
            FileInputStream fis = new FileInputStream(new File(filePath));

            // Create object of XSSFWorkbook class
            XSSFWorkbook workbook = new XSSFWorkbook(fis);

            // Read excel sheet by sheet name
            XSSFSheet sheet = workbook.getSheetAt(0);

            // Get the Cell at index 3 from the above row
            XSSFCell cell = sheet.getRow(vRow).getCell(vColumn);

            cell.setCellType(CellType.STRING);
            cell.setCellValue("*****");

            // Write the output to the file
            FileOutputStream fileOut = new FileOutputStream(new File(filePath));
            workbook.write(fileOut);

            System.out.println("Id column in Excel is updated successfully");
            fileOut.close();

            // Closing the workbook
            workbook.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

