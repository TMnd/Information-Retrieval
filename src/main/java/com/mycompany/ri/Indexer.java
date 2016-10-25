/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.ri;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author joaoa
 */
public class Indexer {
    /* HashMap<String, HashMap<HashMap<Integer,String>, Integer>> hashindex = new HashMap<String, HashMap<HashMap<Integer,String>, Integer>>();
    HashMap<Integer, Integer> hashFrequencia = new HashMap<Integer, Integer>();
    HashMap<Integer, String> hashIdDoc = new HashMap<Integer, String>();*/
    
    private HashMap<String,HashMap<Integer, HashSet<String>>> hi= new HashMap<String,HashMap<Integer, HashSet<String>>>(); //Hashmap que ira conter o termo e o iddoc
    private HashMap<String,HashMap<Integer, HashSet<String>>> hi2 = new HashMap<String,HashMap<Integer, HashSet<String>>>();
  //  private HashMap<String,HashMap<Integer,HashSet<String>>> hi= new HashMap<String,HashMap<Integer,HashSet<String>>>(); //Hashmap que ira conter o termo e o iddoc
      
   
    //Para usar a hashmap do indexer
    //public HashMap<String,HashSet<String>> getHashIndex() {
   /* public HashMap<String,HashMap<Integer,HashSet<String>>> getHashIndex() {
        return hi;
    }*/

    public void setHi(String key,int frequencia, String DocId) {
        //HashSet<String> idsLista = new HashSet<>();
        if (hi2.get(key) == null || !hi2.containsKey(key)) { //caso a hashmap nao tiver o valor inserido na key (talvez nao seja preciso o hi.get(key) == null)
            hi2.put(key, new HashMap<Integer, HashSet<String>>());
            hi2.get(key).put(frequencia, new HashSet<String>());
            hi2.get(key).get(frequencia).add(DocId);
            
        }else if(hi2.containsKey(key)){ //Caso que a key ja exista (nao a primeira ocorrencia)
            hi2.get(key).get(frequencia).add(DocId);
        }
    }
    
    public void updateDocFrequency(){
        Iterator<Map.Entry<String, HashMap<Integer, HashSet<String>>>> parent = hi2.entrySet().iterator();
        while(parent.hasNext()){
           // System.out.println("start key princpal");
            Map.Entry<String, HashMap<Integer, HashSet<String>>> key = parent.next();
            String key2 = key.getKey();
            /*
                key - imprime a key e os respectivo valor
                key2 - imprime so os respectivos valores
            */
            Iterator<Map.Entry<Integer,HashSet<String>>> child = key.getValue().entrySet().iterator();
            while(child.hasNext()){
                Integer sub_key2 = child.next().getKey();
                int tamanho = hi2.get(key2).get(sub_key2).size();
                HashSet<String> aux = new HashSet<>(hi2.get(key2).get(sub_key2));
                hi2.get(key2).remove(sub_key2);
                hi2.get(key2).put(tamanho,new HashSet<>());
                hi2.get(key2).put(tamanho, aux);

            }            
            //System.out.println("Key: " + key2 + " - " + hi2.get(key2));
        }
    }
    
    //Função para imprimir com o intiuto de testar
    public  void imprimir(){
        for(Map.Entry<String,HashMap<Integer, HashSet<String>>> entrySet : hi2.entrySet()) {
        //for(Map.Entry<String, HashMap<Integer,String>> entrySet : hi.entrySet()) {
            String key = entrySet.getKey();
            System.out.println(key + ": " + hi2.get(key));
        }
    }
    
    //dunno
    public void saveDisc(HashMap<Integer, String> hasksave){
    	
    }
}
