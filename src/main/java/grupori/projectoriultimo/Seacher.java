package grupori.projectoriultimo;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class Seacher {
    Map<String, HashMap<Integer, Float>> map = new HashMap<String, HashMap<Integer, Float>>();
    String regexNumbers = "^[0-9]";
    
    public boolean seacher(String termo){
        BufferedReader br = null;
        
        String letraStart = Character.toString(termo.charAt(0));
        
        System.out.println("letra inicill: " + letraStart);
        
        System.out.println("A inserir o index " + letraStart + " para memoria!");
        
        try {
            if(letraStart.matches(regexNumbers)){
                br = Files.newBufferedReader(java.nio.file.Paths.get("src\\main\\java\\grupori\\projectoriultimo\\indexs\\0.txt"));
            }else{
                br = Files.newBufferedReader(java.nio.file.Paths.get("src\\main\\java\\grupori\\projectoriultimo\\indexs\\" + letraStart + ".txt"));    
            }
            for (String line; (line = br.readLine()) != null;) {
                //  System.out.println("------------------");
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

    public void getMap() {
        for(Map.Entry<String, HashMap<Integer, Float>> parent : map.entrySet()){
            String key = parent.getKey();
            for(Map.Entry<Integer, Float> child : map.get(key).entrySet()){
                int subKey = child.getKey();
                System.out.println(subKey + ": " + map.get(key).get(subKey));
            }
        }
    }    
}
