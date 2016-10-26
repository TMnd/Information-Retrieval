
package com.mycompany.ri;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

/**
 * @author  João Amaral
 * @author  Mafalda Rofrigues
 */

public class Indexer {
    //HashMap que irá conter toda a informação que tem de se indexar
    private HashMap<String,HashMap<Integer, HashSet<String>>> hm = new HashMap<String,HashMap<Integer, HashSet<String>>>(); //Hashmap que ira conter o termo e o iddoc
   
    /**
     * Esta função tem o intuito de inserir na hashmap os dados recolhidos pelo
     * tokeneizer.
     * 
     * @param key
     * @param DocId
     */
    public void setHM(String key, String DocId) {       
        if (hm.get(key) == null || !hm.containsKey(key)) { //caso a hashmap nao tiver o valor inserido na key (talvez nao seja preciso o hi.get(key) == null)
            hm.put(key, new HashMap<Integer, HashSet<String>>());
            hm.get(key).put(1, new HashSet<String>());
            hm.get(key).get(1).add(DocId);
        }else if(hm.containsKey(key)){ //Caso que a key ja exista (nao a primeira ocorrencia)
            hm.get(key).get(1).add(DocId);
        }
    }
    
    /**
     * A função ser ve para actualizar o valor das keys da sub-hashmap.
     * Os valores da key da sub-hashmap correspondem a frequencia dos docimentos
     */
    public void updateDocFrequency(){
        Iterator<Map.Entry<String, HashMap<Integer, HashSet<String>>>> parent = hm.entrySet().iterator();
        //Percorre todas as key (termos) da hashmap
        while(parent.hasNext()){
            Map.Entry<String, HashMap<Integer, HashSet<String>>> key = parent.next();
            String key2 = key.getKey();
            /*
                key - é a key principal
                key2 - é a key da sub-hashmap
            */
            Iterator<Map.Entry<Integer,HashSet<String>>> child = key.getValue().entrySet().iterator();
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
    }
    
    /**
     * Esta função tem a função para verificar o que existe na hashmap
     */
    public  void imprimir(){
     //   System.out.println("A imprimir");
     int num = 0;
        for(Map.Entry<String,HashMap<Integer, HashSet<String>>> entrySet : hm.entrySet()) {
           /* String key = entrySet.getKey();
            System.out.println(key + ": " + hm.get(key));*/
           num++;
        }
        System.out.println("numero de linhsa: " + num);
    }
    
    /**
     * Esta função serve para imprimir num ficheiro txt a hashmap
     * 
     */
    public void saveDisc() throws IOException{
        FileWriter filewriterstream;
        BufferedWriter out;

       // System.out.println("teste");
        filewriterstream = new FileWriter("src\\main\\java\\com\\mycompany\\ri\\IndexOutput.txt");
        out = new BufferedWriter(filewriterstream);
        for(Map.Entry<String,HashMap<Integer, HashSet<String>>> entrySet : hm.entrySet()) {
            String key = entrySet.getKey();
            out.write("KEY: " + key + " - " + hm.get(key) + "\n");
        }
        out.close();
       // System.out.println("Indexer: Texto criado");
    }
}
