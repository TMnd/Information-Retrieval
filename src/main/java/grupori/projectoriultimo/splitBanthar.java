/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grupori.projectoriultimo;

/**
 *
 * @author Joao Amaral
 */
public class splitBanthar {
    //custom split
    public static String[] split_banthar(String s, char delimeter) {
        int count = 1;

        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == delimeter) {
                count++;
            }
        }
        String[] array = new String[count];

        int a = -1;
        int b = 0;

        for (int i = 0; i < count; i++) {

            while (b < s.length() && s.charAt(b) != delimeter) {
                b++;
            }
            array[i] = s.substring(a + 1, b);
            a = b;
            b++;
        }

        return array;
    }
}
