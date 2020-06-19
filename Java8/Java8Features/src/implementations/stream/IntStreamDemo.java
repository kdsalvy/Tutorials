package implementations.stream;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class IntStreamDemo {

    // Complete the checkMagazine function below.
    static void checkMagazine(String[] magazine, String[] note) {
        Map<String, Long> mMap = new HashMap<>();
        Map<String, Long> nMap = new HashMap<>();

        fillMap(mMap, magazine);
        fillMap(nMap, note);

        System.out.println(nMap);
        System.out.println(mMap);
        
        boolean isFeasible = true;
        for(Map.Entry<String, Long> entry: nMap.entrySet()) {
            if(!mMap.containsKey(entry.getKey()) || !(mMap.get(entry.getKey()) >= entry.getValue())){
                isFeasible = false;
                break;
            }
        }
        String value = isFeasible? "Yes" : "No";
        System.out.println(value);
    }

    static void fillMap(Map<String, Long> map, String[] arr)  {
        for(String word: arr){
            Long count = 0L;
            if(map.containsKey(word)){
                count = map.get(word) + 1;
            } else {
                count = 1L;
            }
            map.put(word, count);
        }
    }


    public static void main(String[] args) throws FileNotFoundException {
        final Scanner scanner = new Scanner(new FileInputStream(new File("C:\\Users\\saukedia1\\Desktop\\Inputs\\input14.txt")));
        String[] mn = scanner.nextLine().split(" ");

        int m = Integer.parseInt(mn[0]);

        int n = Integer.parseInt(mn[1]);

        String[] magazine = new String[m];

        String[] magazineItems = scanner.nextLine().split(" ");
        scanner.skip("(\r\n|[\n\r\u2028\u2029\u0085])?");

        for (int i = 0; i < m; i++) {
            String magazineItem = magazineItems[i];
            magazine[i] = magazineItem;
        }

        String[] note = new String[n];

        String[] noteItems = scanner.nextLine().split(" ");
        scanner.skip("(\r\n|[\n\r\u2028\u2029\u0085])?");

        for (int i = 0; i < n; i++) {
            String noteItem = noteItems[i];
            note[i] = noteItem;
        }

        checkMagazine(magazine, note);

        scanner.close();
    }
}
