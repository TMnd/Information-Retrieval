package grupori.projectoriultimo;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Seacher {
    public static boolean ASC = true;
    public static boolean DESC = false;
        
    public Map<String, HashMap<Integer, Float>> map = new HashMap<String, HashMap<Integer, Float>>();
    String regexNumbers = "^[0-9]";
    
    public boolean seacher(String termo){
        BufferedReader br = null;
        
        String letraStart = Character.toString(termo.charAt(0));
        
        System.out.println("letra inicill: " + letraStart);
        
        
        
        try {
            for (groups group : groups.values()) {
                if (!group.matchesGroup(group, letraStart)) {
                    continue;
                }
                br = Files.newBufferedReader(java.nio.file.Paths.get("src\\main\\java\\grupori\\projectoriultimo\\indexs\\" + groups.getGroupInitial(group) + ".txt"));    
                /*if(letraStart.matches(regexNumbers)){
                    br = Files.newBufferedReader(java.nio.file.Paths.get("src\\main\\java\\grupori\\projectoriultimo\\indexs\\0.txt"));
                }else{
                    br = Files.newBufferedReader(java.nio.file.Paths.get("src\\main\\java\\grupori\\projectoriultimo\\indexs\\" + letraStart + ".txt"));    
                }*/
                System.out.println("A inserir o index " + groups.getGroupInitial(group) + " para memoria!");
            }
            for (String line; (line = br.readLine()) != null;) {
                String[] termInfo = line.split(",");
                String term = termInfo[0];
                for(int i=1;i<termInfo.length;i++){
                    String[] docInfo = termInfo[i].split(":");
                    int docId = Integer.parseInt(docInfo[0]);
                    float docFreq = Float.parseFloat(docInfo[1]);
                    
                    if(!map.containsKey(term)){
                        map.put(term,new HashMap<>());
                        map.get(term).put(docId,docFreq);
                    }else if(!map.get(term).containsKey(docId)){
                        map.get(term).put(docId,docFreq);
                    }else{
                        map.get(term).put(docId,docFreq);
                    }
                }
            }  
            br.close();
        } catch (IOException ex) {
            System.out.println("O ficheiro de index nao detectado.");
        }
        
        for(Map.Entry<String, HashMap<Integer, Float>> parent : map.entrySet()){
            String key = parent.getKey();
            if(map.containsKey(key)){
                return true;
            }
        }
        return false;
        
    }
    
    public void tfidf(){
        //Formula => W(t,d) = (1 + log(tf)) * log[base 10]((1/math(w^2+w^2...) / df))
        //tf = termo frequency   |  df = document frequency
        
    }

    public Map<String, HashMap<Integer, Float>> getMap() {
        return map;
    }
    

    //Mostrar o top 5
    public void get1Map() {

        ArrayList<String> teste = new ArrayList<String>();
        Map<Integer, Float> a = new HashMap<Integer, Float>();
        //int aux = 0
        
        for(Map.Entry<String, HashMap<Integer, Float>> parent : map.entrySet()){
            String key = parent.getKey();
            for(Map.Entry<Integer, Float> child : map.get(key).entrySet()){
                int subKey = child.getKey();
                float subValue = child.getValue();
                a.put(subKey,subValue);
            }
        }
        System.out.println("ola: " + sortByComparator(a, DESC));
    }
    
    
    
    private static Map<Integer, Float> sortByComparator(Map<Integer, Float> unsortMap, final boolean order){
        List<Entry<Integer, Float>> list = new LinkedList<Entry<Integer, Float>>(unsortMap.entrySet());

        // Sorting the list based on values
        Collections.sort(list, new Comparator<Entry<Integer, Float>>(){
            public int compare(Entry<Integer, Float> o1,Entry<Integer, Float> o2){
                if (order){
                    return o1.getValue().compareTo(o2.getValue());
                }
                else{
                    return o2.getValue().compareTo(o1.getValue());

                }
            }
        });

        // Maintaining insertion order with the help of LinkedList
        Map<Integer, Float> sortedMap = new LinkedHashMap<Integer, Float>();
        for (Entry<Integer, Float> entry : list)
        {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }
}
