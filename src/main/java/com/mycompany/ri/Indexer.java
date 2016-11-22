package com.mycompany.ri;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

/**
 * @author João Amaral
 * @author Mafalda Rofrigues
 */
public class Indexer {
    Runtime runtime = Runtime.getRuntime();
    StringBuilder content =new StringBuilder();
    int cont=0;
    int cont5 = 0;
    //HashMap que irá conter toda a informação que tem de se indexar
    //private HashMap<String,HashMap<Integer, HashSet<String>>> hm = new HashMap<String,HashMap<Integer, HashSet<String>>>(); //Hashmap que ira conter o termo e o iddoc
    //private HashMap<String, HashMap<String, Integer>> hm = new HashMap<String, HashMap<String, Integer>>();
    TreeMap<String, HashMap<Integer, Integer>> hm = new TreeMap<String, HashMap<Integer, Integer>>();
    
    public void setHM(ArrayList<String> chave, int DocId) throws IOException {
            boolean aux = false;//checkMemory(0);
            
            //System.out.println("Encher para a hash");
            for(int i=0; i<chave.size();i++){
                String key = chave.get(i);
                if (!hm.containsKey(key)) {
                    hm.put(key, new HashMap<Integer, Integer>());
                    hm.get(key).put(DocId, 1);
                } else if (!hm.get(key).containsKey(DocId)) {
                    hm.get(key).put(DocId, 1);
                } else {
                    int frequencia = hm.get(key).get(DocId);
                    frequencia++;
                    hm.get(key).put(DocId, frequencia);
                }
            }
           /* System.out.println("A escrever para o disco");
           saveDisc();*/
          
            //System.out.println(hm);
           /* if(checkMemory(1)){
                System.out.println("Criar um ficheiro novo");
                saveDisc();
                System.out.println("antes: " + ((runtime.totalMemory() / 1024) - (runtime.freeMemory() / 1024)));
                hm.clear();
                System.gc();
                System.gc();
                /*if(checkMemory(0)){
                    System.out.println("maior que 20% de memoria");
                    System.gc();
                    //aux = checkMemory(0);
                    System.out.println("depois: " + ((runtime.totalMemory() / 1024) - (runtime.freeMemory() / 1024)));
                }*/
            //}  
    }
    

    /**
     * Esta função tem o intuito de inserir na hashmap os dados recolhidos pelo
     * tokeneizer.
     *
     * @param key
     * @param DocId
     */
    /*public void setHM(String key, String DocId) {      
        if (hm.get(key) == null || !hm.containsKey(key)) { 
            hm.put(key, new HashMap<Integer, HashSet<String>>());
            hm.get(key).put(1, new HashSet<String>());
            hm.get(key).get(1).add(DocId);
        }else if(hm.containsKey(key)){ //Caso que a key ja exista (nao a primeira ocorrencia)
            hm.get(key).get(1).add(DocId);
        }
    }*/
    /**
     * A função ser ve para actualizar o valor das keys da sub-hashmap. Os
     * valores da key da sub-hashmap correspondem a frequencia dos docimentos
     */
    /*  public void updateDocFrequency(){
        Iterator<Map.Entry<String, HashMap<Integer, HashSet<String>>>> parent = hm.entrySet().iterator();
        //Percorre todas as key (termos) da hashmap
        while(parent.hasNext()){
            Map.Entry<String, HashMap<Integer, HashSet<String>>> key = parent.next();
            String key2 = key.getKey();
            /*
                key - é a key principal
                key2 - é a key da sub-hashmap
     */
 /* Iterator<Map.Entry<Integer,HashSet<String>>> child = key.getValue().entrySet().iterator();
            //Percorre a sub-hashmap
            while(child.hasNext()){
                //Recebe o valor da cada key da sub-hashmap
                Integer sub_key2 = child.next().getKey();
                //Verifica o tamanho de cada hashset que contem a lista dos DocIds
                //e guarda-os numa variavel
                int tamanho = hm.get(key2).get(sub_key2).size();
                //Efectua uma copia da hashset que se encontra na sub-hashmap
                HashSet<String> aux = new HashSet<>(hm.get(key2).get(sub_key2));
                //Remove o sub-hashmap
                hm.get(key2).remove(sub_key2);
                //Preenche com uma nova sub-hash map mas com a key com o valor actualizado
                hm.get(key2).put(tamanho,new HashSet<>());
                //Restaura os valores do hashset dentro da sub-hashmap
                hm.get(key2).put(tamanho, aux);
            }            
        }
    }*/
    /**
     * Esta função tem a função para verificar o que existe na hashmap
     */
    /* public  void imprimir(){
        for(Map.Entry<String,HashMap<Integer, HashSet<String>>> entrySet : hm.entrySet()) {
            String key = entrySet.getKey();
            System.out.println(key + ": " + hm.get(key));
        }
    }*/
    //verificar a memoria
    public void checkMemoryAndStore() throws IOException {
        
        /*
        NOTAS:
        - Free Memory: Devolve a quantidade de memoria livre no Java Virtual Machine
        - Max Memory: Devolve a maxima quantidade que a Java Virtual Machine irá usar (valores inseridos na maquina no terminal de linux ou no java em si no windows)
        - Total Memory: Mesmo que a Max Memory, mas poder]a variar ao logo do tempo dependendo as instancias criadas/usadas
        */
        
        final long maxmemory = runtime.maxMemory();
        long maxRecal = (long) (maxmemory*.7);
        long minRecal = (long) (maxmemory*.2);
        long usedMem = (100*(runtime.totalMemory() - runtime.freeMemory()))/runtime.totalMemory();
        
      
        //if(option == 1){
            
           // if(usedMem >= maxRecal){
           System.out.println(usedMem);
            if(usedMem > 60){
                System.out.println("escrever");
                cont++;
                File f = new File("D:\\OwnCloud\\Documents\\Universidade\\Recuperação de Informação\\RI\\src\\main\\java\\com\\mycompany\\ri\\teste\\index" + cont + ".txt");
                f.createNewFile();
                FileWriter fw = new FileWriter(f.getAbsoluteFile());
                BufferedWriter bw = new BufferedWriter(fw);
                for(Map.Entry<String,HashMap<Integer, Integer>> parent: hm.entrySet()){
                    String key = parent.getKey();
                    
                    String content = key;
                    //content.append(key);
                    
                    for (Entry<Integer, Integer> child : hm.get(key).entrySet()) {
                        int subkey = child.getKey();

                        int subvalue = child.getValue();
                        
                       /* content.append(",");
                        content.append(subvalue);
                        content.append(":");
                        content.append(subvalue);*/
                        content += "," + subkey + ":" + subvalue;
                    }
                    content += System.lineSeparator();
                    //content.append("\n");
                    bw.write(content.toString());
                }
                bw.close();
                
                hm.clear();
                System.gc();
                System.gc();
                System.out.println("limpei");
                //return true;
            }//else{
                //return false;
            //}
        /*}else{
            if(usedMem >= minRecal){
                return true;
            }else{
                return false;
            }
        }*/
    }

    /**
     * Esta função serve para imprimir num ficheiro txt a hashmap
     *
     */
    public void saveDisc() throws IOException{        
        for(groups group: groups.values()){
            //group -> Devolve Group_a, etc
            //groups.getGroupInitiasl(group) -> Devolve só a letra final tirando Group_
            File f = new File("D:\\OwnCloud\\Documents\\Universidade\\Recuperação de Informação\\RI\\src\\main\\java\\com\\mycompany\\ri\\teste\\" + groups.getGroupInitiasl(group) + cont5 + ".txt");
            f.createNewFile();
            FileWriter fw = new FileWriter(f.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
        
            for(Map.Entry<String,HashMap<Integer, Integer>> parent: hm.entrySet()){
                String key = parent.getKey();
                //HashMap value = parent.getValue();
                
                if(!group.matchesGroup(group, key)){
                    //Funciona!!!
                    continue;
                }
                
                String content = key;
                
                for(Map.Entry<Integer, Integer> child : hm.get(key).entrySet()){
                    int subKey = child.getKey();
                    int subvalue = child.getValue();
                    
                    content += "," + subKey + ":" + subvalue;
                }
                content += System.lineSeparator();
                bw.write(content);
            }
            bw.close();
           
        }
        cont5++;
    }
}               
                /*// Get length of file in bytes
                long fileSizeInBytes = f.length();
                // Convert the bytes to Kilobytes (1 KB = 1024 Bytes)
                long fileSizeInKB = fileSizeInBytes / 1024;
                // Convert the KB to MegaBytes (1 MB = 1024 KBytes)
                long fileSizeInMB = fileSizeInKB / 1024;*/
                    
               /* if(f.exists() && !f.isDirectory()) {
                   //if (fileSizeInMB > 100) {
                        FileWriter fileWritter = new FileWriter(f,true);
                    try (BufferedWriter bufferWritter = new BufferedWriter(fileWritter)) {
                        bufferWritter.write(key + " - " + hm.get(key) + "\n");
                        //  }
                    }
                }else{
                    filewriterstream = new FileWriter("D:\\OwnCloud\\Documents\\Universidade\\Recuperação de Informação\\RI\\src\\main\\java\\com\\mycompany\\ri\\teste\\" + caracter + ".txt");
                    filewriterstream.write(key + " - " + hm.get(key) + "\n");//appends the string to the file
                    filewriterstream.close();
                }*/
   
