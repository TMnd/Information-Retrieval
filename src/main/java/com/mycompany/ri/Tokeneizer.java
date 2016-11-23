package com.mycompany.ri;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.StringTokenizer;
import org.tartarus.snowball.ext.englishStemmer;
import org.tartarus.snowball.SnowballStemmer;

/**
 * @author  João Amaral
 * @author  Mafalda Rodrigues
 */
public class Tokeneizer {
    
    SnowballStemmer snowballStemmer = new englishStemmer();
  
    //Para guardar as StopWord que se encontram num ficheiro para um hashset
    private HashSet<String> StopWord = new HashSet<>();

    /**
     * Como existe um ficheiro no porjecto com as stopwords entao esta função
     * lê o ficheiro linha a linha e vai adcionando para o hashSet "StopWord"
     * as palavras.
     *
     * @throws FileNotFoundException
     * @throws IOException
     */
    public void loadStoppingwords() throws FileNotFoundException, IOException {
        //Carregar o ficheiro que contém as stopwords
        String StopWords = "D:\\OwnCloud\\Documents\\Universidade\\Recuperação de Informação\\RI\\src\\main\\java\\com\\mycompany\\ri\\stopwords_en.txt";
        //D:\OwnCloud\Documents\Universidade\Recuperação de Informação\RI\src\main\java\com\mycompany\ri\stopwords_en.txt
        
        //ler o ficheiro txt
        FileReader f = new FileReader(StopWords); 
        BufferedReader br = new BufferedReader(f);
        
        String sCurrentLine;

        //Percorre o ficheiro linha a linha 
        while ((sCurrentLine = br.readLine()) != null) { 
            //Adciona cada linha para o hashset
            StopWord.add(sCurrentLine); 
        }
        br.close();
    }

    /**
     * A função receve como parametro o documento que por sua vez é dividido em tokens.
     * Cada token é comparado a lista de stopwords com o intuito de "filtrar" a informação.
     * Cada token filtrado é passado pelo stemmer, este serve para reduzir as variações
     * morfológicas das palavras para um termo em comum (Stem).
     * 
     * @param line
     * @param array
     * @return 
     * @throws IOException
     */
    public ArrayList<String> FromDocProcessor(String line) throws IOException {
        //Coloca as StopWords em memoria
        loadStoppingwords(); 
        
        ArrayList<String> ar = new ArrayList<>();
        //Dividir por tokens o parte do documento
        StringTokenizer st = new StringTokenizer(line.replaceAll("\\<[^>]*>", " ").replaceAll("[^\\w'. ]", " ").
                replaceAll("(?!([0-9]+))([\\.\\,]+)(?!([0-9]+))", " ").replaceAll("\\.", " ").replaceAll(" +", " ").trim()); 
        //ID = teste[0]; //Para guardar em variavel o id do doc para que possa ser usado depois
               
        while (st.hasMoreElements()) { //Corre se a string tokeneizer tiver mais elementos
            String token = st.nextToken().trim();
                
            //Verifica se o token é uma stopword ou não    
            if (!StopWord.contains(token)) {
                //Passa o token pelo stemmer e pelo stemmer
                String tokentratado = CheckSpecialCases(token).replaceAll("\\'", " ").replaceAll(" +", " ");
                String tokenStemmer=stemming(tokentratado);                 
                    
                //Insere o token já tratado num arraylist
                ar.add(tokenStemmer);
            }
        }
        //Devolve o arraylist já preenchido de tokens
        return ar;
    }
    
    /*public char getCharacter(String token){
        char[] aux = token.toCharArray();
        
        return aux[0];
    }*/

    /**
     * Tratamento de palavras usando a biblioteca SnowballStemmer
     *
     * @param word
     * @return
     */
    public String stemming(String word) {
       
        snowballStemmer.setCurrent(word);

        snowballStemmer.stem();
        
        return snowballStemmer.getCurrent();

    }

    /**
     * Para "limpar" a string retirando todos os carecters especiais
     * 
     * @param text
     * @return
     */
    public String apagartudoMenosPontoApostrofe(String text) {
       // return text.replaceAll("[^\\w ]", " ").replaceAll(" +", " ");
        return text.replaceAll("[^\\w.'%]", " ").replaceAll(" +", " "); //remove tudo menos os . e ' e %
    }
    
    public String parseText2(String text) {
     //   return text.replaceAll("[.']", " ").replaceAll(" +", " ");
        return text.replaceAll("\\.$", " ").replaceAll(" +", " "); //

    }
    
    /**
     * Para "limpar" a string retirando todas as tags
     * 
     * @param text
     * @return
     */
    public String CheckSpecialCharacters(String text){
        return text.replaceAll("<\\d>|<\\\\\\d>", " ").replaceAll(" +", " ");
    }
    
    public String CheckSpecialCases(String text){
       //ESTA BOM return text.replaceAll("\\b'\\b", "");
      return text.replaceAll("\\b'\\b", "").replaceAll(" +", " "); ///DONE
    }
    
     
    /**
     * Para adquirir a lista de stopwords noutras classes
     * 
     * @return
     */
    public HashSet<String> getStopWord() {
        return StopWord;
    }
    
    public String lemmatization(){
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
