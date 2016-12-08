/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grupori.projectoriultimo;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.StringTokenizer;
/*import org.tartarus.snowball.ext.englishStemmer;
import org.tartarus.snowball.SnowballStemmer;*/

/**
 *
 * @author joaoa
 */
public class Tokeneizer {
    private final HashSet<String> StopWord = new HashSet<>();
    ArrayList<String> ar = new ArrayList<>();
    boolean aux = false;
    
    public void LoadStopWords(String url) throws IOException{
        //ler o ficheiro txt
        FileReader f = null; 
        try {
            f = new FileReader(url);
        } catch (FileNotFoundException ex) {
            System.out.println("Ficheiro nao encontrado");
        }
        BufferedReader br = new BufferedReader(f);
        
        String sCurrentLine;

        //Percorre o ficheiro linha a linha 
        while ((sCurrentLine = br.readLine()) != null) { 
            //Adciona cada linha para o hashset
            StopWord.add(sCurrentLine); 
        }
        br.close();
    }
    
    //Limpar o arraylist que ira conter os tokens
    public void clearAr(){
       // System.out.println("entrou no clearAR");
      //  System.out.println("antes de apagar: " + ar.size()); //a primeira passagem sera NULL!
        //System.out.println("11");
        if(aux){
            if(ar.size() >0){
                System.out.println("Limpar o arraylist!");
                ar.clear();
                System.gc();
                System.out.println("depois de apagar: " + ar.size());
            }else{
                System.out.println("nao tem nada para limpar");
            }
        }
    }
    
    public ArrayList<String> receberDocumento(String line) throws IOException {  
        //System.out.println("a tokenizar");
       // if(aux){
            //System.out.println("clear");
            //ar.clear();
         //   System.gc();
      //  }
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
                //String tokenStemmer=stemming(tokentratado);                 
                    
                //Insere o token já tratado num arraylist
                ar.add(tokentratado);
            }
        }
        //Devolve o arraylist já preenchido de tokens
        aux = true;
        return ar;
    }
    
    public String CheckSpecialCharacters(String text){
        return text.replaceAll("<\\d>|<\\\\\\d>", " ").replaceAll(" +", " ");
    }
    
    public String CheckSpecialCases(String text){
       //ESTA BOM return text.replaceAll("\\b'\\b", "");
      return text.replaceAll("\\b'\\b", "").replaceAll(" +", " "); ///DONE
    }
    
  /*  public String stemming(String word) {
       
        snowballStemmer.setCurrent(word);

        snowballStemmer.stem();
        
        return snowballStemmer.getCurrent();

    }*/
}
