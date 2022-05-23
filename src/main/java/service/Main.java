package service;

public class Main {
    public static void main(String[] args) {
        String filePath = "C:\\Users\\mgmet\\Desktop\\deneme.xlsx";
        Reader r=new Reader();
        r.ReadCellData(filePath);
    }
}
