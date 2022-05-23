package service.Tc;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;

public class ReadCellAndTc {
    private static final String filePath = "C:\\Users\\mgmet\\Desktop\\deneme.xlsx";

    static ReadCellAndTc read = new ReadCellAndTc();

    public static void main(String[] args) {
        read.ReadCellData(1, 0);
    }

    public void ReadCellData(int vRow, int vColumn) {
        String value = null;
        String result = null;
        Workbook wb = null;

        try {
            FileInputStream fis = new FileInputStream(filePath);
            wb = new XSSFWorkbook(fis);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Sheet sheet = wb.getSheetAt(0);   //getting the XSSFSheet object at given index

        for (int rowLastIndex = 0; rowLastIndex < sheet.getLastRowNum(); rowLastIndex++) {
            result = "";
            Row row = sheet.getRow(vRow++); //returns the logical row
            Cell cell = row.getCell(vColumn); //getting the cell representing the given column
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
            }
            System.out.println(read.TcNoCheck(result));//Buraya write kodu gelecek
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

}

