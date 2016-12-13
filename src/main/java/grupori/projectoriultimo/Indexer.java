/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grupori.projectoriultimo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Map;
import java.util.Map.Entry;
import grupori.projectoriultimo.groups;
import java.io.BufferedReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Collections;
import java.util.Comparator;

/**
 *
 * @author joaoa
 */
public class Indexer {
   // Tokeneizer tk = new Tokeneizer();
    Runtime runtime = Runtime.getRuntime();
    int cont = 0;
    int cont5 = 0; //para os subindex conjuntos!
    ArrayList<String> teste = null;

    Map<String, HashMap<Integer, Float>> hm = new HashMap<String, HashMap<Integer, Float>>();
    Map<String, HashMap<Integer, Float>> hm2 = new HashMap<String, HashMap<Integer, Float>>();

    public void addTM(ArrayList<String> ar, int DocId) {
        for (int i = 0; i < ar.size(); i++) {
            String key = ar.get(i);
            if (!hm.containsKey(key)) {
                hm.put(key, new HashMap<>());
                hm.get(key).put(DocId, 1.0f);
            } else if (!hm.get(key).containsKey(DocId)) {
                hm.get(key).put(DocId, 1.0f);
            } else {
                float frequencia = hm.get(key).get(DocId);
                frequencia++;
                hm.get(key).put(DocId, frequencia);
            }
        }
       // System.out.println(hm);
    }

    public void saveDisc() throws IOException {
        ArrayList<String> terms = new ArrayList<>(hm.keySet());
        Collections.sort(terms, new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                return s1.compareToIgnoreCase(s2);
            }
        });
        
        //System.out.println(terms);
        
        String aux_group = "";
        BufferedWriter bw = null;
        
        for (String term : terms) {
            if(!term.isEmpty()){
                //System.out.println("termo: " + term);
                String group = getGroup(term);
               // System.out.println("group: " + group);
                if (!group.equals(aux_group)) {
                  //  System.out.println("Entrou no primeiro if(positico");
                    if (bw != null) {
                    //    System.out.println("Vai chegar o ficheiro");
                        bw.close();
                    }
                    File file = new File("src\\main\\java\\grupori\\projectoriultimo\\indexs\\" + group + cont + ".txt");
                    file.createNewFile();
                    FileWriter fw = new FileWriter(file.getAbsoluteFile());
                    bw = new BufferedWriter(fw);
                }

                StringBuilder sb = new StringBuilder();
                sb.append(term);
               // System.out.println("append antes do for: " + sb);
                for (Map.Entry<Integer, Float> child : hm.get(term).entrySet()) {
                    Integer cKey = child.getKey();
                    float cValue = child.getValue();

                    sb.append(",").append(cKey).append(":").append(cValue);
                }
               // System.out.println("appenc depois do for: " + sb);
                sb.append(System.lineSeparator());
                bw.write(sb.toString());
                aux_group = group;
            }
        }
        bw.close();
        cont++;
    }
    
    public String getGroup(String term) {
        char[] InicialTerm = term.toCharArray();
        String iTerm = "" + InicialTerm[0];
        for (groups group : groups.values()) {
            if (group.matchesGroup(group, iTerm)) {
                return group.getGroupInitial(group);
            }
        }
        return null;
    }

        /*Map<String, HashMap<Integer, Float>> tm = new TreeMap<String, HashMap<Integer, Float>>(hm);
       
        for (groups group : groups.values()) {
            try {
                File f = new File("src\\main\\java\\grupori\\projectoriultimo\\indexs\\" /*+ (cont5++) +*/  /*+ groups.getGroupInitial(group) + cont + ".txt");
                f.createNewFile();
                FileWriter fw = new FileWriter(f.getAbsoluteFile());
                BufferedWriter bw = new BufferedWriter(fw);

                for (Map.Entry<String, HashMap<Integer, Float>> parent : tm.entrySet()) {
                    String key = parent.getKey();
                    
                    if (!group.matchesGroup(group, key)) {
                        continue;
                    }

                    String content = key;

                    for (Map.Entry<Integer, Float> child : tm.get(key).entrySet()) {
                        int subKey = child.getKey();
                        float subvalue = child.getValue();

                        content += "," + subKey + ":" + subvalue;
                    }
                    
                    if (tm.size() != cont) {
                        content += System.lineSeparator();
                    }
                    bw.write(content);
                }
                bw.close();
                
            } catch (IOException ex) {
                System.out.println("Ficheiro já existe");
            }
        }*/
    
    
    public static TreeMap<String, HashMap<Integer, Float>> carregarFicheiro(String ficheiro) throws IOException {
        TreeMap<String, HashMap<Integer, Float>> tm = new TreeMap<>(); //tree temporaria para carregar o primeiro ficheiro de cada letra
        
        BufferedReader br = Files.newBufferedReader(java.nio.file.Paths.get(ficheiro), StandardCharsets.ISO_8859_1);
        for (String line; (line = br.readLine()) != null;) {
          //  System.out.println("------------------");
            String[] termInfo = line.split(",");
            String term = termInfo[0];
            for(int i=1;i<termInfo.length;i++){
                String[] docInfo = termInfo[i].split(":");
                int docId = Integer.parseInt(docInfo[0]);
                float docFreq = Float.parseFloat(docInfo[1]);
                
                if(!tm.containsKey(term)){
                    tm.put(term,new HashMap<>());
                    tm.get(term).put(docId,docFreq);
                }else if(!tm.get(term).containsKey(docId)){
                    tm.get(term).put(docId,docFreq);
                }else{
                    tm.get(term).put(docId,docFreq);
                }
            }   
        }
        br.close();
        return tm;
    }
        
    private void fusaoIndex(TreeMap<String, HashMap<Integer, Float>> termMapToMerge) {
        for(Map.Entry<String, HashMap<Integer, Float>> parent : termMapToMerge.entrySet()){
            String key = parent.getKey();
            for(Entry<Integer, Float> child : termMapToMerge.get(key).entrySet()){
                int subKey = child.getKey();   
                if(!hm2.containsKey(key)){
                    hm2.put(key, new HashMap<Integer, Float>());
                    hm2.get(key).put(subKey, termMapToMerge.get(key).get(subKey));
                }else{
                    hm2.get(key).put(subKey, termMapToMerge.get(key).get(subKey));
                }
            }
        }
    }

    public void reduçãoIndex() throws IOException {
        for (groups group : groups.values()) {
            for(int i=0;i<cont;i++){
                fusaoIndex(carregarFicheiro("src\\main\\java\\grupori\\projectoriultimo\\indexs\\" + group.getGroupInitial(group)+ i + ".txt"));
            }
            calculos();
            EscreverGrupos(group);
            for (int i = 0; i < cont; i++) {
                Files.delete(java.nio.file.Paths.get("src\\main\\java\\grupori\\projectoriultimo\\indexs\\" + group.getGroupInitial(group)+ i + ".txt"));
            }
        }  
    }
    
    private void EscreverGrupos(groups group) { //Em base de multiplos subs-indexs.
        try {
                File file = new File("src\\main\\java\\grupori\\projectoriultimo\\indexs\\" + group.getGroupInitial(group) + ".txt");
                file.createNewFile();
                FileWriter fw = new FileWriter(file.getAbsoluteFile());
                BufferedWriter bw = new BufferedWriter(fw);

                for (Map.Entry<String, HashMap<Integer, Float>> parent : hm2.entrySet()) {
                    String Key = parent.getKey();

                    String linha = Key;

                    for (Map.Entry<Integer, Float> child : hm2.get(Key).entrySet()) {
                        Integer subKey = child.getKey();
                        float subValue = child.getValue();

                        linha += "," + subKey + ":" + subValue;
                    }
                    linha += System.lineSeparator();
                    bw.write(linha);
                }
                bw.close();
          //  }
            
        } catch (IOException e) {
            System.err.println("Erro ao criar o ficheiro do index!");
        }
        hm2.clear();
        hm.clear();
        System.gc();
    }
    
    public void calculos(){
        for(Map.Entry<String,HashMap<Integer,Float>> parent: hm2.entrySet()){
            float wTotal = 0;
            float somatorioRaizQuadrada;
            float calculo = 0;
            String key = parent.getKey();
            
            for(Map.Entry<Integer, Float> child: hm2.get(key).entrySet()){
                int subKey = child.getKey();
                
                if(hm2.get(key).get(subKey) != null){
                    wTotal += Math.pow(hm2.get(key).get(subKey),2);
                }
            }
            somatorioRaizQuadrada = (float) Math.sqrt(wTotal);
            for(Map.Entry<Integer, Float> child: hm2.get(key).entrySet()){
                int subKey = child.getKey();
                calculo = (float) ((1 + Math.log(hm2.get(key).get(subKey)))/somatorioRaizQuadrada);
                hm2.get(key).put(subKey,calculo); 
            }
        }

    }
    
    

    public void memory(int contas) throws IOException {
        //tk.clearAr();
        

        /*long totalfreememory = runtime.freeMemory() + (runtime.maxMemory() - runtime.totalMemory());
        long usedMem = 100 * (runtime.maxMemory() - totalfreememory) / runtime.maxMemory();*/

        //if (usedMem > 70) {
          if ( 100.0 * (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / Runtime.getRuntime().maxMemory() > 70){
            System.out.println("A gravar os ficheiros...");
           // System.out.println("arraylist: " + tk.getAr());
            System.out.println("numero de doc lidos: " + contas);
            saveDisc();
            System.out.println("Limpar memoria");
            hm.clear();
            //tk.setAux(true);
            Runtime.getRuntime().gc();
            //if (usedMem > 20) {
            if ( 100.0 * (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / Runtime.getRuntime().maxMemory() > 20){
                System.gc();
            }
        }
    }
}
