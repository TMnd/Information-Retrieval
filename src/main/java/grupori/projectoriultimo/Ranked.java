package grupori.projectoriultimo;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * @author João Amaral
 * @author Mafalda Rodrigues
 */
public class Ranked {

    static Map<String, String> indexDoc = new TreeMap<>();

    private static boolean loadIndexMapping() throws IOException {
        BufferedReader brIndexList = Files.newBufferedReader(java.nio.file.Paths.get("Index\\docindex.txt"));

        for (String line; (line = brIndexList.readLine()) != null;) {
            String[] termInfo = line.split(",", 2);
            indexDoc.put(termInfo[0], termInfo[1]);
        }
        return true;
    }

    public ArrayList<String> score(Map<String, Float> scorehm) {
        ArrayList<String> arFinal = new ArrayList<>();
        int contTop5 = 0;
        for (Map.Entry<String, Float> parent : sortByValue(scorehm).entrySet()) {
            contTop5++;
            if (contTop5 > 5) {
                continue;
            }
            String key = parent.getKey();
            
            //Mudar para melhor output
            arFinal.add("Doc: " + indexDoc.get(key) + "\t Score: " + parent.getValue());
        }
        return arFinal;
    }

    public static Map<String, Float> calcScore(Map<String, HashMap<Integer, Float>> oldhm){
        Map<String, Float> scorehm = new HashMap<>();
        if (indexDoc.isEmpty()) {
            try {
                loadIndexMapping();
            } catch (IOException ex) {
                Logger.getLogger(Ranked.class.getName()).log(Level.SEVERE, null, ex);
            }
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
        return map.entrySet().stream().sorted(Map.Entry.comparingByValue(Collections.reverseOrder())).collect(Collectors.toMap(
                                Map.Entry::getKey,
                                Map.Entry::getValue,
                                (e1, e2) -> e1,
                                LinkedHashMap::new
        ));
    }
}
