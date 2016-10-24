/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.ri;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 *
 * @author joaoa
 */
public class Indexer {
    /* HashMap<String, HashMap<HashMap<Integer,String>, Integer>> hashindex = new HashMap<String, HashMap<HashMap<Integer,String>, Integer>>();
    HashMap<Integer, Integer> hashFrequencia = new HashMap<Integer, Integer>();
    HashMap<Integer, String> hashIdDoc = new HashMap<Integer, String>();*/
    private HashMap<String,HashSet<String>> hi= new HashMap<String,HashSet<String>>(); //Hashmap que ira conter o termo e o iddoc
    private HashSet<String> idsLista = new HashSet<>();  
   
    //Para usar a hashmap do indexer
    public HashMap<String,HashSet<String>> getHashIndex() {
        return hi;
    }

    public void setHi(String key, String DocId) {
         if (hi.get(key) == null || !hi.containsKey(key)) { //caso a hashmap nao tiver o valor inserido na key (talvez nao seja preciso o hi.get(key) == null)
            hi.put(key, new HashSet<>()); // Caso na hashmap nao exista a key (primeira ocorrencia), cria a key com o value nulo
            hi.get(key).add(DocId); //Uma vez criada a key na linha anterior, irá povoar o value com o DocID actual
        }else if(hi.containsKey(key)){ //Caso que a key ja exista (nao a primeira ocorrencia)
            if(!idsLista.contains(DocId) ){ //talvez nao seja necessario... need to check
                hi.get(key).add(DocId); //caso que a key exista e que ja tenha valores acrescenta o valor actual com o antigo
            }
        }
    }
    
    //Função para imprimir com o intiuto de testar
    public  void imprimir(){
        for(Map.Entry<String, HashSet<String>> entrySet : hi.entrySet()) {
            String key = entrySet.getKey();
            System.out.println(key + ": " + hi.get(key));
        }
    }
    
    //dunno
    public void saveDisc(HashMap<Integer, String> hasksave){
    	
    }
}
