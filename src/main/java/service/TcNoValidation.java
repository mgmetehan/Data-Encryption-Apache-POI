package service;

/*
1 – TC Kimlik Numaraları 11 karakter olmak zorundadır.

2 – Her hanesi bir rakam olmaldır.

3 – İlk hanesi 0 (sıfır) olamaz

4 – 1, 3, 5, 7, 9 basamaklarının toplamının 7 katından, 2, 4, 6, 8 basamaklarının toplamını çıkarttığımızda elde ettiğimiz
sonucun 10’a bölümünden kalan sayı (MOD10)  10. basamaktaki sayıyı vermelidir.

5 – İlk 10 hanenin toplamından elde edilen sonucun 10’a bölümünden kalan sayı (MOD10) 11. basamaktaki sayıyı vermelidir.
 */
public class TcNoValidation {
    static TcNoValidation validation = new TcNoValidation();

    public static void main(String[] args) {
        String TcNo = "11111111110";
        System.out.println(validation.TcNoCheck(TcNo));
    }

    public boolean TcNoCheck(String TcNo) {
        if (TcNo == null || TcNo.length() != 11) {
            System.out.println("Tc No 11 karakter olmalı");
            return false;
        }
        int[] arr = validation.split(TcNo);

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
