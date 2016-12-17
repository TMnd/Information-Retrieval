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
    StringBuilder testesb;
            
    Map<String, HashMap<Integer, Float>> hm = new HashMap<>();
    Map<String, HashMap<Integer, Float>> hm2 = new HashMap<>();
    
    //DocID map
    Map<Integer, Integer> docMap = new HashMap<>();
 

    public void addTM(ArrayList<String> ar, int DocId) {
        int IDfromDoc = 0;
        if(!docMap.containsKey(DocId)){
            cont5++;
            docMap.put(DocId, cont5);
            IDfromDoc = cont5;
        }else{
            IDfromDoc = docMap.get(DocId);
        }
        
       // System.out.println(docMap);
        for (int i = 0; i < ar.size(); i++) {
            String key = ar.get(i);
            if (!hm.containsKey(key)) {
                hm.put(key, new HashMap<>());
                //hm.get(key).put(DocId, 1.0f);
                hm.get(key).put(IDfromDoc, 1.0f);
            } else if (!hm.get(key).containsKey(IDfromDoc)) {
                //hm.get(key).put(DocId, 1.0f);
                hm.get(key).put(IDfromDoc, 1.0f);
            } else {
                //float frequencia = hm.get(key).get(DocId);
                float frequencia = hm.get(key).get(IDfromDoc);
                frequencia++;
                //hm.get(key).put(DocId, frequencia);
                hm.get(key).put(IDfromDoc,frequencia);
            }
        }
    }

    public void saveDisc() throws IOException {
        ArrayList<String> termos = new ArrayList<>(hm.keySet());
        String auxGroup = "";
        BufferedWriter bw = null;
        BufferedWriter bw2 = null;
        // grupo = null;
        
        //PARA DESCOMENTAR
       /* File file2 = new File("src\\main\\java\\grupori\\projectoriultimo\\indexs\\_idMap"+ cont + ".txt");
        file2.createNewFile();
        FileWriter fw2 = new FileWriter(file2.getAbsoluteFile());
        bw2 = new BufferedWriter(fw2);
        StringBuilder ola = new StringBuilder();
        for(Map.Entry<Integer, Integer> parent : docMap.entrySet()){
            int key = parent.getKey();
            ola.append(key).append(":").append(docMap.get(key)).append(System.lineSeparator());
        }
        bw2.write(ola.toString());
        bw2.close();*/
        
        //sort array
        Collections.sort(termos, new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                return s1.compareToIgnoreCase(s2);
            }
        });
        
        //System.out.println(termos);
        //Save hm com o array como objectivo
        for (int i=0;i<termos.size();i++) {
            //System.out.println(termos.get(i));
            if(!termos.get(i).isEmpty()){ //Para evitar o maldito termo que é NULL!!! #$%&/
                /*char[] InicialTerm = termos.get(i).toCharArray();
                String iTerm = "" + InicialTerm[0];*/
                String grupo = getGroup(termos.get(i));
                if (!grupo.equals(auxGroup)) {
                    if (bw != null) {
                        bw.close();
                    }
                    File file = new File("src\\main\\java\\grupori\\projectoriultimo\\indexs\\" + grupo + cont + ".txt");
                    file.createNewFile();
                    FileWriter fw = new FileWriter(file.getAbsoluteFile());
                    bw = new BufferedWriter(fw);
                }

                StringBuilder sbSaveDisc = new StringBuilder();
                sbSaveDisc.append(termos.get(i));
                for (Map.Entry<Integer, Float> child : hm.get(termos.get(i)).entrySet()) {
                    Integer cKey = child.getKey();
                    float cValue = child.getValue();

                    sbSaveDisc.append(",").append(cKey).append(":").append(cValue);
                }
                sbSaveDisc.append(System.lineSeparator());
                bw.write(sbSaveDisc.toString());
                auxGroup = grupo;
            }
        }
        bw.close();
        cont++;
    }
    
    public String getGroup(String term) {
        for (groups group : groups.values()) {
            if (group.matchesGroup(group, term)) {
                return group.getGroupInitial(group);
            }
        }
        return null;
    }

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
                    tm.get(term).put(docId,tm.get(term).get(docId)+docFreq);
                }
            }   
        }
        br.close();
        return tm;
    }
        
    private void fusaoIndex(TreeMap<String, HashMap<Integer, Float>> termMapToMerge) {
        //System.out.println("entrou na fusao de index");
        for(Map.Entry<String, HashMap<Integer, Float>> parent : termMapToMerge.entrySet()){
            String key = parent.getKey();
            System.out.println(key);
            for(Entry<Integer, Float> child : termMapToMerge.get(key).entrySet()){
                int subKey = child.getKey();   
                if(!hm2.containsKey(key)){
                    hm2.put(key, new HashMap<>());
                    hm2.get(key).put(subKey, termMapToMerge.get(key).get(subKey));
                }else if(hm2.get(key).containsKey(subKey)){
                    hm2.get(key).put(subKey, hm2.get(key).get(subKey)+termMapToMerge.get(key).get(subKey));
                }else{
                    hm2.get(key).put(subKey, termMapToMerge.get(key).get(subKey));
                }
            }
        }
    }

    public void reduçãoIndex() throws IOException {
        //System.out.println("entrou no redução index");
        for (groups group : groups.values()) {
            int subindexcont = 0;
            for(int i=0;i<cont;i++){
                fusaoIndex(carregarFicheiro("src\\main\\java\\grupori\\projectoriultimo\\indexs\\" + group.getGroupInitial(group)+ i + ".txt"));
                System.out.println(group.getGroupInitial(group)+ i);
            }
           // System.out.println("a efectuar calculos!");
           // calculos();
            //System.out.println(hm2);
            /*System.out.println("A escrever index final");
            System.out.println(group);
            EscreverGrupos(group,0);
            System.out.println("done");*/
            for (int i = 0; i < cont; i++) {
                Files.delete(java.nio.file.Paths.get("src\\main\\java\\grupori\\projectoriultimo\\indexs\\" + group.getGroupInitial(group)+ i + ".txt"));
            }
        }  
    }
    
    private void EscreverGrupos(groups group, int SubIndexcont) { //Em base de multiplos subs-indexs.
        System.out.println("SubIndexcont: "+SubIndexcont);
     
                System.out.println("entrou!");
                System.out.println("1");
                File file = new File("src\\main\\java\\grupori\\projectoriultimo\\indexs\\" + group.getGroupInitial(group) + ".txt");
                //File file = new File("src\\main\\java\\grupori\\projectoriultimo\\indexs\\" + group.getGroupInitial(group) + "_" + SubIndexcont +".txt");
                testesb = new StringBuilder();
        try {
                file.createNewFile();
                FileWriter fw = new FileWriter(file.getAbsoluteFile());
                BufferedWriter bw = new BufferedWriter(fw);
                System.out.println("entrou!3");
                for (Map.Entry<String, HashMap<Integer, Float>> parent : hm2.entrySet()) {
                    String Key = parent.getKey();
                    //String linha = Key;
                    testesb.append(Key);
                    
                    for (Map.Entry<Integer, Float> child : hm2.get(Key).entrySet()) {
                        Integer subKey = child.getKey();
                        float subValue = child.getValue();
                        
                        testesb.append(",").append(subKey).append(":").append(subValue);
                        //linha += "," + subKey + ":" + subValue;
                    }
                    //linha += System.lineSeparator();
                    testesb.append(System.lineSeparator());
                    //bw.write(linha);
                    bw.write(testesb.toString());
                }
                
                System.out.println("entrou!4");
                bw.close();
                
          //  }     
        } catch (IOException e) {
            System.err.println("Erro ao criar o ficheiro do index!");
        }
        hm2.clear();
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
    
    public void finalwhipe(int contas){
        System.out.println("Final Wipe!");
        System.out.println("numero de doc lidos: " + contas);
        hm.clear();
        docMap.clear();
        Runtime.getRuntime().gc();
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
            docMap.clear();
            //tk.setAux(true);
            Runtime.getRuntime().gc();
            //if (usedMem > 20) {
            if ( 100.0 * (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / Runtime.getRuntime().maxMemory() > 20){
                System.gc();
            }
        }
    }
}
