package grupori.projectoriultimo;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author João Amaral
 * @author Mafalda Rodrigues
 */
public class Seacher {
    Tokeneizer token = new Tokeneizer();

    String regexNumbers = "^[0-9]";

    public Map<String, HashMap<Integer, Float>> seacher(String term) throws IOException {
        Map<String, HashMap<Integer, Float>> mapSeacher = new HashMap<>();
        BufferedReader brSeacher = null;
        
        ArrayList<String> arrayQuery = new ArrayList<>(token.receiveDoc(term));
        
        for(String arQuery: arrayQuery){
            try {
                for (groups group : groups.values()) {
                    if (!group.matchesGroup(group, arQuery)) {
                        continue;
                    }
                    if(arQuery.matches(regexNumbers)){
                        brSeacher = Files.newBufferedReader(java.nio.file.Paths.get("src\\main\\java\\grupori\\projectoriultimo\\temp\\0.txt"));
                    }else{
                        brSeacher = Files.newBufferedReader(java.nio.file.Paths.get("src\\main\\java\\grupori\\projectoriultimo\\temp\\" + groups.getGroupInitial(group) + ".txt"));    
                    }
                }
                for (String line; (line = brSeacher.readLine()) != null;) {
                    String[] termInfo = line.split(",");
                    String termSplitted = termInfo[0];
                    int result = termSplitted.compareTo(arQuery);
                    if (result == 0) {
                        for (int j = 1; j < termInfo.length; j++) { //o 0 é o termo
                            String[] docInfo = termInfo[j].split(":");
                            int docId = Integer.parseInt(docInfo[0]);
                            float docFreq = Float.parseFloat(docInfo[1]);
                            if (!mapSeacher.containsKey(termSplitted)) {
                                mapSeacher.put(termSplitted, new HashMap<>());
                                mapSeacher.get(termSplitted).put(docId, docFreq);
                            } else if (mapSeacher.containsKey(termSplitted) && !mapSeacher.get(termSplitted).containsKey(docId)) {
                                mapSeacher.get(termSplitted).put(docId, docFreq);
                            }
                            
                        }
                    }
                }

                brSeacher.close();
                System.out.println(term + ": " + mapSeacher);
            } catch (IOException ex) {
                System.out.println("O ficheiro de index nao detectado.");
            }
        }
        return mapSeacher;
    }
}
