/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.ri;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class DocProcessor {
    Pattern pattern = Pattern.compile("\\\"([^\\\"]*)\\\""); //Padrão para o regex
    String data; //dunno
    int id; //dunno
    String texto; //dunno
    CorpusReader cp = new CorpusReader(); //Para poder buscar as funções da class CorpusReader
    private ArrayList<String> toTokeneizer = new ArrayList<String>(); //Arralist para que se possa guardar as strings completamente tratadas com regex e com o id e o nome do documento no sitio certo

    //Serve para dividir cada documento para poder ser tratado
    public void DivideLine(ArrayList<String> ola){
        //Percorrer o arraly list
        Iterator iter = ola.iterator();
        while(iter.hasNext()){
            String line = (String) iter.next(); //cada linha do documento que foi inserido na arraylist
           
            String[] teste= line.split(",", 3); //separar a string no ',' mas só os dois primeiros
            
            Matcher matcher = pattern.matcher(teste[2]); //Para activar o regex
            
            while (matcher.find()) { 
                String rebuildString = teste[1] + "_" + teste[0] + "," + matcher.group(1); //ex: idDoc_NomeDoFicheiro, TextoDoDoc
                toTokeneizer.add(rebuildString);// Insere na arraylist a string com o id do documento com o formato correcto e o conteudo do mesmo já tratado
            }                    
        }
    }
    
    //Serve para que a class tokeneizer consiga adquirir a arraylist da class DocProcessor para que possa trabalhar.
    public ArrayList<String> getToTokeneizer() {
        return toTokeneizer;
    }
    
    //dunno
    public HashMap<String, HashMap<String, Float>> putinTable(HashMap<String, HashMap<String, String>> conteudo) {
        return null;
    }

    public static void checkLanguage(String data) {

    }

    public static void checkCharacterSet(String data) {

    }

    public String getTexto(String data) {
        return texto;
    }

    public HashMap<String, HashMap<String, Float>> getHashMap() {
        return null;
    }
    
}
    
    /*Iterator iter = arrlist.iterator();
      while (iter.hasNext()) {
         System.out.println(iter.next());
      }*/
    
    
    
    /*  while (entries.hasMoreElements()) {
            ZipEntry entry = entries.nextElement();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(zipFile.getInputStream(entry)));

            String line;
            
            //Primeira passagem pelos ficheiros
            while ((line = bufferedReader.readLine()) != null) { 
                String[] id_aux = null;
                Matcher matcher = pattern.matcher(line); //Para activar o regex
        
                HashSet<String> idsLixo = new HashSet<>(); //Para dar valores nulos para cada key da hashmap
                
                if (!line.startsWith("@")) { //ler todas as linhas que nao tenham um @
                    //GET text que se enzntra no meio das ""
                    while (matcher.find()) {
                        
                        StringTokenizer st = new StringTokenizer(matcher.group(1));  //Separa cada palavra pelo espaço
                
                        while (st.hasMoreElements()) {
                            String i = st.nextToken();
                            //System.out.println(i);
                            //Encher a has map SÓ com os termos mas com nenhum value por key
                            if (rabodeboi.get(i) == null || !rabodeboi.containsKey(i)) {
                                rabodeboi.put(i, new HashSet<String>());
                            }
                        }
                    }
                }
            }*/

   
    
    
    
    
    
    
    
    
    
    
    

