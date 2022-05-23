package service;

public class TcValidation {

    public boolean TcNoCheck(String TcNo) {
        if (TcNo == null || TcNo.length() != 11) {
            System.out.println("Tc No 11 karakter olmalı");
            return false;
        }
        int[] arr = split(TcNo);

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
