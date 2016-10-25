/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.ri;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;


public class CorpusReader {
    /*private HashMap<ZipEntry, HashMap<String, String>> path = new HashMap<>();
     private HashMap<String, HashMap<String, String>> conteudo = new HashMap<>();
     //isto tem que ir para o DocProcessor, mas não consegui. Voltar a ver
     private HashMap<String, HashMap<String, Float>> docID = new HashMap<>();
     private HashMap<String, HashSet<String>> textMap = new HashMap<>();*/

    public String AnaliseFile(String file) throws IOException {
        DocProcessor dc = new DocProcessor();

        if (file.endsWith(".zip")) {
            System.out.println("CorpusReader: ENTROU NA FUNÇÃO ZIP");

            return dc.readFileZip(file);

        } else {
            System.out.println("CorpusReader: NÃO ENTROU NA FUNÇÃO ZIP");

        }
        return "Não entrou em nada";
    }

    //Serve para adquirir o arraylist quando se é chamado por outra class
   /* public ArrayList<String> getMenDocs() {
     return menDocs2;
     }*/


    public String checkFormat(String file) {
        return null;
    }
}

/*para depois ir buscar o conteudo todo--> tratar no docProcessor*/
/* public HashMap<String, HashMap<String, String>> getConteudo() {
 return conteudo;
 }

 public HashMap<ZipEntry, HashMap<String, String>> getPath() {
 return path;
 }*/

/*esta parte tem que ir para o DOCPROCESSOR */
   // public HashMap<String, HashSet<String>> getTextMap() {

/*for (Map.Entry<String, HashSet<String>> entrySet : textMap.entrySet()) {
 String key = entrySet.getKey();
            
 System.out.println(key + ": " + textMap.get(key));
 }*/
        //return textMap;
// }

/*public HashMap<String, HashMap<String, Float>> getDocID() {
 return docID;
 }*/
