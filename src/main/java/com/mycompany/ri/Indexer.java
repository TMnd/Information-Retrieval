package com.mycompany.ri;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author João Amaral
 * @author Mafalda Rodrigues
 */
public class Indexer {
    Runtime runtime = Runtime.getRuntime();
    Tokeneizer to = new Tokeneizer();
    static int cont5 = 0;
    
    static TreeMap<String, HashMap<Integer, Float>> hm = new TreeMap<String, HashMap<Integer, Float>>();

    /**
     * Esta função tem com como o objectivo de receber do tokenizer um arraylist
     * de tokens que sao tratados e devolvidos pelo tokenizer
     * 
     * @param content
     * @param DocId 
     */
    public void SendTokenizer(String content, int DocId){
        try {
            System.out.println("a enviar...");
           setHM(to.FromDocProcessor(content.toLowerCase()), DocId);
        } catch (IOException ex) {
            System.out.println("Falhou a receber os tokens");
        }
    }
    
    /**
     * Esta função tem o intuito de inserir na hashmap os dados enviados pelo tokeneizer
     * através da leitura do arraylist que foi enviado pelo mesmo.
     *
     * @param chave
     * @param DocId
     */
    public void setHM(ArrayList<String> chave, int DocId){ 
        for(int i=0; i<chave.size();i++){
            String key = chave.get(i);
            if (!hm.containsKey(key)) {
                hm.put(key, new HashMap<Integer, Float>());
                hm.get(key).put(DocId, 1.0f);
            } else if (!hm.get(key).containsKey(DocId)) {
                hm.get(key).put(DocId, 1.0f);
            } else {
                float frequencia = hm.get(key).get(DocId);
                frequencia++;
                hm.get(key).put(DocId, frequencia);
            }
        }
    }

    /**
     * Esta função tem como objectivo de verificar a memoria.
     * Se a memoria utilizada atingir os 70% o fragmento da Map em memoria
     * irá ser gravado em disco sendo seguido por uma lipeza da Map e o chamamento
     * do garbage collector.
     * Chamou-se o garbage collector duas vezes para a eventualidade do primeiro
     * chamamento nao limpar memoria necessaria.
     * 
     * @throws IOException 
     */
    public void checkMemoryAndStore() throws IOException {
       // System.out.println("Checking memory...");
        long usedMem = (100*(runtime.totalMemory() - runtime.freeMemory()))/runtime.totalMemory();
        
        if(usedMem > 70){
            saveDisc();
            hm.clear();
            System.gc();
            System.gc();
        }
    }
    
    /**
    * Esta função tem como objectivo de salvar o fragmento do index que se
    * encontra em memoria em diferentes subindex.
    *
    * @throws java.io.IOException
    */
    public void saveDisc() throws IOException{        
        int cont=0;
        File f = new File("D:\\OwnCloud\\Documents\\Universidade\\Recuperação de Informação\\RI\\src\\main\\java\\com\\mycompany\\ri\\teste\\subindex" + /*groups.getGroupInitiasl(group) +*/ (cont5++) + ".txt");
        f.createNewFile();
        FileWriter fw = new FileWriter(f.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fw);
       
        
        for(Map.Entry<String,HashMap<Integer, Float>> parent: hm.entrySet()){
            String key = parent.getKey();
                
            String content = key;
                
            for(Map.Entry<Integer, Float> child : hm.get(key).entrySet()){
                int subKey = child.getKey();
                float subvalue = child.getValue();
                    
                content += "," + subKey + ":" + subvalue;
            }
            cont++;
            if(hm.size() != cont){
                content += System.lineSeparator();
            }
            bw.write(content);
                
        }
        bw.close();
    }
    
    
    /**
     * O intuito desta função é para carregar cada ficheiro subIndex para memoria,
     * com o objectivo de juntar todos os subIndexs
     * 
     * @param ficheiro
     * @return
     * @throws IOException 
     */
    public static TreeMap<String, HashMap<Integer, Float>> carregarFicheiro(String ficheiro) throws IOException {
        TreeMap<String, HashMap<Integer, Float>> tm = new TreeMap<>();
        
        BufferedReader br = Files.newBufferedReader(java.nio.file.Paths.get(ficheiro), StandardCharsets.ISO_8859_1);
        for (String line; (line = br.readLine()) != null;) {
            String[] termInfo = line.split(",");
            String[] docInfo = termInfo[1].split(":");

            String term = termInfo[0];
            int docId = Integer.parseInt(docInfo[0]);
            float docFreq = Float.parseFloat(docInfo[1]);
            tm.put(term, new HashMap<>());
            tm.get(term).put(docId, docFreq);
        }
        br.close();
        return tm;
    }
    
    /**
     * Esta função irá utilizar uma Map já em memoria e fundi-la a Map principal 
     * ao adcionar termos (e os respectivos values) quando não existem e acrescentar 
     * values quando os termos ja existirem
     * 
     * @param termMapToMerge 
     */
    private static void fusaoIndex(TreeMap<String, HashMap<Integer, Float>> termMapToMerge) {
        for(Map.Entry<String, HashMap<Integer, Float>> parent : termMapToMerge.entrySet()){
            String key = parent.getKey();
            for(Entry<Integer, Float> child : termMapToMerge.get(key).entrySet()){
                int subKey = child.getKey();   
                if(!hm.containsKey(key)){
                    hm.put(key, new HashMap<Integer, Float>());
                    hm.get(key).put(subKey, termMapToMerge.get(key).get(subKey));
                }else{
                    hm.get(key).put(subKey, termMapToMerge.get(key).get(subKey));
                }
            }
        }
    }
    
    /**
     * A função tem como obectivo de iniciar a função dos ficheiros subIndex e 
     * eliminar os mesmos.
     * 
     * @throws IOException 
     */
    public void reduçãoIndex() throws IOException {
        for (int i = 0; i < cont5 ; i++) {
            fusaoIndex(carregarFicheiro("D:\\OwnCloud\\Documents\\Universidade\\Recuperação de Informação\\RI\\src\\main\\java\\com\\mycompany\\ri\\teste\\subindex" + i + ".txt"));
        }    
        for (int i = 0; i < cont5; i++) {
            Files.delete(java.nio.file.Paths.get("D:\\OwnCloud\\Documents\\Universidade\\Recuperação de Informação\\RI\\src\\main\\java\\com\\mycompany\\ri\\teste\\subindex" + i + ".txt"));
        }
        //Efectuar os calculos
        System.out.println("a calcular");
        calculos();
        //criar ficheiros
        System.out.println("merging time");
        EscreverGrupos();
    }
    
    /**
     * A função calculos tem como objectivo de calcular o lnc.
     * Para realizar este calculo é necessario duas passagens dos valores de 
     * cada termo.
     * A primeira passagem serve para calcular os pesos de cada termos.
     * A segunda passagem serve para efectuar os restantes calculos usando a 
     * formula 1 + log(base 2)[Frequencia do termo nos documentos] / peso de cada termos
     */
    public void calculos(){
        for(Map.Entry<String,HashMap<Integer,Float>> parent: hm.entrySet()){
            float wTotal = 0;
            float somatorioRaizQuadrada;
            float calculo = 0;
            String key = parent.getKey();
            
            for(Map.Entry<Integer, Float> child: hm.get(key).entrySet()){
                int subKey = child.getKey();
                
                if(hm.get(key).get(subKey) != null){
                    wTotal += Math.pow(hm.get(key).get(subKey),2);
                }
            }
            somatorioRaizQuadrada = (float) Math.sqrt(wTotal);
            for(Map.Entry<Integer, Float> child: hm.get(key).entrySet()){
                int subKey = child.getKey();
                calculo = (float) ((1 + Math.log(hm.get(key).get(subKey)))/somatorioRaizQuadrada);
                hm.get(key).put(subKey,calculo); 
            }
        }
    }
   
    
  
    private void EscreverGrupos() {
        System.out.println("Creating final index...");
        try {
            for (groups group : groups.values()) {
                File file = new File("D:\\OwnCloud\\Documents\\Universidade\\Recuperação de Informação\\RI\\src\\main\\java\\com\\mycompany\\ri\\teste\\" + group.getGroupInitial(group) + ".txt");
                file.createNewFile();
                FileWriter fw = new FileWriter(file.getAbsoluteFile());
                BufferedWriter bw = new BufferedWriter(fw);

                for (Map.Entry<String, HashMap<Integer, Float>> parent : hm.entrySet()) {
                    String Key = parent.getKey();

                    if (!group.matchesGroup(group, Key)) {
                        continue;
                    }

                    String linha = Key;

                    for (Map.Entry<Integer, Float> child : hm.get(Key).entrySet()) {
                        Integer subKey = child.getKey();
                        float subValue = child.getValue();

                        linha += "," + subKey + ":" + subValue;
                    }
                    linha += System.lineSeparator();
                    bw.write(linha);
                }
                bw.close();
            }
            hm.clear();
            System.gc();
        } catch (IOException e) {
            System.err.println("Erro ao criar o ficheiro do index!");
        }
    }  
}               
   
