package grupori.projectoriultimo;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Seacher {
    Tokeneizer tk = new Tokeneizer();

    String regexNumbers = "^[0-9]";

    public Map<String, HashMap<Integer, Float>> seacher(String termo) throws IOException {
        Map<String, HashMap<Integer, Float>> map = new HashMap<>();
        BufferedReader br = null;
        
        ArrayList<String> arrayQuery = new ArrayList<>(tk.receberDocumento(termo));
        
        for(String arQuery: arrayQuery){
            try {
                for (groups group : groups.values()) {
                    if (!group.matchesGroup(group, arQuery)) {
                        continue;
                    }
                    //br = Files.newBufferedReader(java.nio.file.Paths.get("src\\main\\java\\grupori\\projectoriultimo\\temp\\" + groups.getGroupInitial(group) + ".txt"));
                    if(arQuery.matches(regexNumbers)){
                        br = Files.newBufferedReader(java.nio.file.Paths.get("src\\main\\java\\grupori\\projectoriultimo\\temp\\0.txt"));
                    }else{
                        br = Files.newBufferedReader(java.nio.file.Paths.get("src\\main\\java\\grupori\\projectoriultimo\\temp\\" + groups.getGroupInitial(group) + ".txt"));    
                    }
                }
                for (String line; (line = br.readLine()) != null;) {
                    String[] termInfo = line.split(",");
                    String term = termInfo[0];
                    int result = term.compareTo(arQuery);
                    if (result == 0) {
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
                //System.out.println("map size: " + map.size());
                System.out.println(termo + ": " + map);
               // System.out.println(termo + ": (com calculos)" + map);
            } catch (IOException ex) {
                System.out.println("O ficheiro de index nao detectado.");
            }
        }
        return map;
    }
}
