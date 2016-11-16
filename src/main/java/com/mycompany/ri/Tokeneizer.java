package com.mycompany.ri;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.StringTokenizer;
import org.tartarus.snowball.ext.englishStemmer;
import org.tartarus.snowball.SnowballStemmer;

/**
 * @author  João Amaral
 * @author  Mafalda Rofrigues
 */
public class Tokeneizer {
    
    SnowballStemmer snowballStemmer = new englishStemmer();
    Indexer in = new Indexer();
  
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
    }

    /**
     * As menssagens no meio do codigo têm o objectivo de informar ao utilizador
     * a etapa que se encontra durante a execução do programa.
     * Esta função recebe como parametro o arraylist (que é preenchido no DocProcessor
     * Antes de fazer alguma coisa o metodo "loadStoppingWords()" é percorrido.
     * Uma vez com as spotwords em memoria, o arraylist é lido para ser processado.
     * Cada elemento do arraylist contém o id do documento mais o documento que
     * serão divididos para que possam ser inseridos na hashmap(Indexer).
     * Agora falando o conteudo do documento. Cada documento irá passar por dois 
     * metodos que irão retirar retirar os caracteres especiais e tags. Uma vez
     * com os documentos tratados, estes são divididos em varios tokens.
     * Cada Token é colocado em letras pequenas e irá passar pelo stemmer que o
     * resultado que for recebido pelo metodo "stemming" irá ser adcionado em 
     * em conjunto com o docid correspondente na hashmap no indexer.
     * Uma vez que o ultimo elemento é lido é chamado o metodo updateDocFrequency()
     * que existe no indexer.
     * 
     * @param array
     * @throws IOException
     */
    public void FromDocProcessor(ArrayList<String> array) throws IOException {
        //Preenche o hashset primeiro antes desta função correr para nao criar problemas. 
        //Podia-se começar no principio do programa mas devido ao uso de arraylist no principio
        //penso que esta seja a melhor opção
        loadStoppingwords(); 
        Iterator leitorArray = array.iterator(); //Percorrer o arraly list
        while (leitorArray.hasNext()) {
            String ID = null;
            String line = (String) leitorArray.next(); 
            //Pra dividir os strings do arraylist pelo ',' 
            String[] teste = line.split(",", 2); 
            
            //Dividir por tokens o parte do documento
            StringTokenizer st = new StringTokenizer(teste[1].toLowerCase().replaceAll("\\<[^>]*>", " ").replaceAll("[^\\w'. ]", " ").replaceAll("(?!([0-9]+))([\\.\\,]+)(?!([0-9]+))", "").replaceAll("\\.", " ").replaceAll(" +", " ").trim());  //Elimina tudo menos os ' .
            ID = teste[0]; //Para guardar em variavel o id do doc para que possa ser usado depois
           
            while (st.hasMoreElements()) { //Corre se a string tokeneizer tiver mais elementos
                String token = st.nextToken().trim();
                if (!StopWord.contains(token)) {//Verifica se o token é uma stopword ou não
                    
                    String tokentratado = CheckSpecialCases(token).replaceAll("\\'", " ").replaceAll(" +", " ");//Elimina tags, o'connor para oconnor e I.B.M para ibm
                    String tokenStemmer=stemming(tokentratado);                 
  
                    //Insere a string que foi recebida pelo stemmer e o id 
                    //do decumento na hashmap que se encontra na class indexer
                    in.setHM(tokenStemmer, ID);
                }
            }
            //Esta condição irá correr sempre que o iterator chegar ao ultimo valor do array
            if (!leitorArray.hasNext()) {
                //Uma vez com a hashmap do indexer preenchida este metodo irá calcular a frequencia dos documentos para cada termo
                //in.updateDocFrequency();
                //Imprime a hashmap do indexer, para testes
                //in.imprimir();
                //in.checkMemory();
                in.saveDisc(1);
            }
        }
    }
    
    public char getCharacter(String token){
        char[] aux = token.toCharArray();
        
        return aux[0];
    }

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
