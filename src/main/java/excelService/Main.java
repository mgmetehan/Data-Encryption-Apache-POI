package excelService;

public class Main {
    //Bir Hucrede boşluk bırakıp 2 adet tc yazarsan çalışmıyor
    public static void main(String[] args) {
        String filePath = "C:/Users/mgmet/Desktop/deneme.xlsx";
        Reader r = new Reader();
        r.ReadCellData(filePath);
        //ör tc 22260299888 / 11118495268
    }
}
