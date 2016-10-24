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
    private ArrayList<String> menDocs2 = new ArrayList<String>(); //O arraylist server para armazenar todas as linhas como strings ja modificadas de todas as linhas.
    
    public void readToMemory() throws IOException{
        //Para a mafalda testar
        //ZipFile zipFile = new ZipFile("C:\\Users\\Mafalda Rodrigues\\Desktop\\Mestrado\\RI\\RI\\src\\main\\java\\com\\mycompany\\ri\\corpus-RI.zip");
        //Para o joao testar
        ZipFile zipFile = new ZipFile("D:\\OwnCloud\\Documents\\Universidade\\Recuperação de Informação\\RI\\src\\main\\java\\com\\mycompany\\ri\\corpus-RI.zip");
        
        Enumeration<? extends ZipEntry> entries = zipFile.entries();
 
        //Percorre todos os ficheiros do ficheiro zip
        while (entries.hasMoreElements()) {
            //O entry sera cada ficheiro que contem no ficheiro ziop
            ZipEntry entry = entries.nextElement();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(zipFile.getInputStream(entry)));
            String line; //Para cada linha (documentos, id e o texto em si incluidos) que contém cada ficheiro 
            String ficheiro = entry.getName(); //Para adquirir o nome de cada ficheiro ex: se o ficheiro tiver uma pasta sera: NomeDaPasta/NomeDoFicheiro. se for só o ficheiro aparecera só o ficheiro
            String line_rebuild = null; //Serve para reconstruir cada linha que sera inserida no arraylyst que ira acrescentar no principio o nome do ficheiro que esta a ser lido as linhas
            
            //Percorre todas as linhas de cada ficherio
            while ((line = bufferedReader.readLine()) != null) {
                if (!line.startsWith("@")) { //ler todas as linhas que nao tenham um @ (ISTO TEM DE SER MELHORADO)
                    if (line.length() != 0) { //let todas as linha que nao estejam vazias
                        line_rebuild = ' ' + ficheiro + ',' + line + ' '; //String modificadora que acrescenta o nome do ficheiro antes do conteudo de cada linhas
                        menDocs2.add(line_rebuild); //Acrscenta a linha modificada para o arraylist
                    }
                }
            }
        }
    }

    //Serve para adquirir o arraylist quando se é chamado por outra class
    public ArrayList<String> getMenDocs() {
        return menDocs2;
    }

    //não sei se preciso 
    public String getFile() {
        return null;

    }

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

   