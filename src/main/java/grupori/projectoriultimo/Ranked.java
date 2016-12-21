/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grupori.projectoriultimo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 *
 * @author joaoa
 */
public class Ranked {

    static Map<String, String> indexDoc = new TreeMap<>();

    private static boolean loadIndexMapping() throws IOException {
        BufferedReader brIndexList = Files.newBufferedReader(java.nio.file.Paths.get("src\\main\\java\\grupori\\projectoriultimo\\temp\\docindex.txt"));

        for (String line; (line = brIndexList.readLine()) != null;) {
            String[] termInfo = line.split(",", 2);
            indexDoc.put(termInfo[0], termInfo[1]);
        }
        return true;
    }

    public ArrayList<String> score(Map<String, Float> scorehm) {
        ArrayList<String> arFinal = new ArrayList<>();
        int contadorTop5 = 0;
        //System.out.println(rnk.sortByValue(scorehm));
        for (Map.Entry<String, Float> parent : sortByValue(scorehm).entrySet()) {
            contadorTop5++;
            if (contadorTop5 > 5) {
                continue;
            }
            String key = parent.getKey();

            //System.out.println("Key: " + key + " Values: " + parent.getValue());
            arFinal.add("Key: " + indexDoc.get(key) + " Values: " + parent.getValue());
        }
        return arFinal;
    }

    public static Map<String, Float> calculoScore(Map<String, HashMap<Integer, Float>> oldhm) throws IOException {
        Map<String, Float> scorehm = new HashMap<>();
        if (indexDoc.isEmpty()) {
            loadIndexMapping();
        }

        for (Map.Entry<String, HashMap<Integer, Float>> parent : oldhm.entrySet()) {
            String key = parent.getKey();

            for (Map.Entry<Integer, Float> child : oldhm.get(key).entrySet()) {
                int subKey = child.getKey();

                float idf = (float) (oldhm.get(key).get(subKey) * Math.log(indexDoc.size() / oldhm.get(key).size()));
                if (!scorehm.containsKey(subKey)) {
                    scorehm.put("" + subKey, idf);
                } else {
                    scorehm.put("" + subKey, scorehm.get(subKey) + idf);
                }
            }
        }

        return scorehm;
    }

    public static Map<String, String> getIndexDoc() {
        return indexDoc;
    }

    public static <Integer, Float extends Comparable<? super Float>> Map<Integer, Float> sortByValue(Map<Integer, Float> map) {
        return map.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Collections.reverseOrder()))
                .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                Map.Entry::getValue,
                                (e1, e2) -> e1,
                                LinkedHashMap::new
                        ));
    }
}
