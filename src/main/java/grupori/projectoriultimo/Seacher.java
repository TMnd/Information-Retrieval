package grupori.projectoriultimo;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import sun.security.krb5.internal.tools.Ktab;

public class Seacher {

    static List<String> ar = null;

    public Map<String, HashMap<Integer, Float>> map = new HashMap<>();
    String regexNumbers = "^[0-9]";

    public boolean seacher(String termo) throws IOException {
        ar = new ArrayList<String>();
        BufferedReader br = null;
        char inicioLetra = 0;

        String[] letraStart = termo.split(" ");

        for (int i = 0; i < letraStart.length; i++) {
            inicioLetra = letraStart[i].charAt(0);
            System.out.println("letra iniciais: " + inicioLetra);

            try {
                for (groups group : groups.values()) {
                    if (!group.matchesGroup(group, letraStart[i])) {
                        continue;
                    }
                    br = Files.newBufferedReader(java.nio.file.Paths.get("src\\main\\java\\grupori\\projectoriultimo\\temp\\" + groups.getGroupInitial(group) + ".txt"));
                    /*if(letraStart.matches(regexNumbers)){
                        br = Files.newBufferedReader(java.nio.file.Paths.get("src\\main\\java\\grupori\\projectoriultimo\\indexs\\0.txt"));
                    }else{
                        br = Files.newBufferedReader(java.nio.file.Paths.get("src\\main\\java\\grupori\\projectoriultimo\\indexs\\" + letraStart + ".txt"));    
                    }*/
                    System.out.println("Inserir o index " + groups.getGroupInitial(group) + " para memoria!");
                }
                for (String line; (line = br.readLine()) != null;) {
                    String[] termInfo = line.split(",");
                    String term = termInfo[0];
                    int result = term.compareTo(letraStart[i]);
                    if (result == 0) {
                       // System.out.println("term: " + termo);
                        //System.out.println("encontrou: " + termInfo[0]);
                        for (int j = 1; j < termInfo.length; j++) { //o 0 é o termo
                            String[] docInfo = termInfo[j].split(":");
                            int docId = Integer.parseInt(docInfo[0]);
                            float docFreq = Float.parseFloat(docInfo[1]);
                            if (!map.containsKey(term)) {
                                map.put(term, new HashMap<>());
                                map.get(term).put(docId, docFreq);
                            } else if (map.containsKey(term) && !map.get(term).containsKey(docId)) {
                                map.get(term).put(docId, docFreq);
                            }
                        }
                    }
                }

                br.close();
                System.out.println("map size: " + map.size());
                System.out.println(termo + ": (sem calculos)" + map);
                calculos();
                System.out.println(termo + ": (com calculos)" + map);
            } catch (IOException ex) {
                System.out.println("O ficheiro de index nao detectado.");
            }

        }

        for (Map.Entry<String, HashMap<Integer, Float>> parent : map.entrySet()) {
            String key = parent.getKey();
            if (map.containsKey(key)) {
                return true;
            }
        }
        return false;

    }

    public void tfidf() {
        //Formula => W(t,d) = (1 + log(tf)) * log[base 10]((1/math(w^2+w^2...) / df))
        //tf = termo frequency   |  df = document frequency

    }

    public Map<String, HashMap<Integer, Float>> getMap() {
        return map;
    }

    //Para adaptar
    public void calculos() {
        for (Map.Entry<String, HashMap<Integer, Float>> parent : map.entrySet()) {
            float wTotal = 0;
            float somatorioRaizQuadrada;
            float calculo = 0;
            String key = parent.getKey();

            for (Map.Entry<Integer, Float> child : map.get(key).entrySet()) {
                int subKey = child.getKey();

                if (map.get(key).get(subKey) != null) {
                    wTotal += Math.pow(map.get(key).get(subKey), 2);
                }
            }
            somatorioRaizQuadrada = (float) Math.sqrt(wTotal);
            for (Map.Entry<Integer, Float> child : map.get(key).entrySet()) {
                int subKey = child.getKey();
                calculo = (float) ((1 + Math.log(map.get(key).get(subKey))) / somatorioRaizQuadrada);
                map.get(key).put(subKey, calculo);
            }
        }

    }
}
