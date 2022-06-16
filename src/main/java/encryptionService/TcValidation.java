package encryptionService;

public class TcValidation {
    /*
   1 – TC Kimlik Numaralari 11 karakter olmak zorundadir.

   2 – Her hanesi bir rakam olmaldir.

   3 – Ilk hanesi 0 (sifir) olamaz

   4 – 1, 3, 5, 7, 9 basamaklarinin toplaminin 7 katindan, 2, 4, 6, 8 basamaklarinin toplamini cikarttigimizda elde ettigimiz
   sonucun 10’a bolumunden kalan sayi (MOD10)  10. basamaktaki sayiyi vermelidir.

   5 – İlk 10 hanenin toplamindan elde edilen sonucun 10’a bolumunden kalan sayi (MOD10) 11. basamaktaki sayiyi vermelidir.
    */
    public boolean TcNoCheck(String TcNo) {
        if (TcNo == null || TcNo.length() != 11) {
            System.out.println("Tc No 11 karakter olmali");
            return false;
        }
        int[] arr = split(TcNo);

        if (arr[0] == 0) {
            System.out.println("Tc No ilk rakami 0 olamaz");
            return false;
        }

        int odd = 0, even = 0, result = 0;
        odd = (arr[0] + arr[2] + arr[4] + arr[6] + arr[8]) * 7;
        even = arr[1] + arr[3] + arr[5] + arr[7];
        result = (odd - even) % 10;
        if (result != arr[9]) {
            System.out.println("Method 4 saglanmiyor.");
            return false;
        }

        result = 0;
        for (int i = 0; i < 10; i++) {
            result += arr[i];
        }
        result = result % 10;
        if (result != arr[10]) {
            System.out.println("Method 5 saglanmiyor.");
            return false;
        }
        return true;
    }

    public int[] split(String TcNo) {
        int[] numbers = new int[11];
        try {
            for (int i = 0; i < 11; i++) {
                numbers[i] = Integer.valueOf(TcNo.substring(i, (i + 1)));
            }
        } catch (Exception e) {
            System.out.println("Tc No icinde string olamaz!");
        }
        return numbers;
    }
}
