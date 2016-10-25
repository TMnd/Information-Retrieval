/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.ri;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.StringTokenizer;

public class Tokeneizer {
    //private int lengthToken;
   // public StringTokenizer listTokens;
    Indexer in = new Indexer();
    private HashSet<String> sw = new HashSet<>();
   // public String data; //recebido pelo docprocessor
    //HashMap<String, HashSet<String>> hashFromIndexer = in.setHi(in.hi);
    
    /*public void setData(String data) {
        this.data = data;
    }*/
    
    public void FromDocProcessor(ArrayList<String> array) throws IOException{
        loadStoppingwords(); //Preenche o hashset primeiro antes desta função correr para nao criar problemas. Podia-se começar no principio do programa mas devido ao uso de arraylist no principio penso que esta seja a melhor opção
        //Percorrer o arraly list
        Iterator iter = array.iterator();
        System.out.println("Preencher a hashmap a imprimir");
        while(iter.hasNext()){
            String ID = null;
            String line = (String) iter.next(); //cada linha do documento que foi inserido na arraylist
            HashSet<String> idsLista = new HashSet<>(); //é para inserir os valores dos ids para colocar nos values por key 
            String[] teste= line.split(",", 2); //Pra dividir os strings do arraylist pelo ','
            
            
            StringTokenizer st = new StringTokenizer(teste[1]); //Dividir por tokens o parte do documento
            ID = teste[0]; //Para guardar em variavel o id do doc para que possa ser usado depois
            
            //Corre se a string tokeneizer tiver mais elementos
            while(st.hasMoreElements()){
               // int frequencia = 0;
                String i = st.nextToken(); //Torna cada token em string
                
                //in.setHi(i, ID); //Inser o valor de da token mas o id correspondte na hashmap que se encontra na class do indexer
                if(!sw.contains(i)){
                  //  frequencia++;
                    in.setHi(i,1,ID);
                }
            }
            //Quando o arraylist nao tiver mais elementos, o indexer imprime o que tem em memoria
            //Esta parte serve exclusivamente para testes
           
            if(!iter.hasNext()){
                System.out.println("A calucar a frequencia dos documentos em que o termo aparece");
                in.updateDocFrequency();
                System.out.println("A Imprimir");
                in.imprimir();
            }
        }
    }
    
    //Load das stopwords para um hashset (reason: LUDACRIS SPEED)
    public void loadStoppingwords() throws FileNotFoundException, IOException{
        System.out.println("A dar load das stoppingwords");
        FileReader f = new FileReader("src\\main\\java\\com\\mycompany\\ri\\stopwords_en.txt"); //ver o ficheiro que existe na source do programa
        BufferedReader br = new BufferedReader(f);;
	String sCurrentLine;

        while ((sCurrentLine = br.readLine()) != null) { //Percorre o ficheiro linha a linha 
            sw.add(sCurrentLine); //Adciona cada linha a um hashset
	}
    }
    
   /* public boolean stoppingWords(String word) throws FileNotFoundException, IOException{
        
        if(sw.contains(word))
     
    }*/
        
    
    //Dunno
    /*public StringTokenizer divideText(String data){
        return listTokens;
    }*/
    
    public String stemming(){
        return null;
    }
    
    public String lemmatization(){
        return null;
    }
    
    public String CheckSpecialCharacters(){
        return null;
    }
    
    public String lowercase(){
        return null;
    }
    
    public String normalization(){
        return null;
    }
    
    public String soundex(){
        return null;
    }
    
    public String removeCharacters(){
        return null;
    }
}