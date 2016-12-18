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
import org.tartarus.snowball.ext.englishStemmer;
import org.tartarus.snowball.SnowballStemmer;

/**
 *
 * @author joaoa
 */
public class Tokeneizer {

    private final HashSet<String> StopWord = new HashSet<>();
    Indexer id = new Indexer();

    SnowballStemmer snowballStemmer = new englishStemmer();
    //ArrayList<String> ar = new ArrayList<String>();
    ArrayList<String> ar = null;
    boolean aux = false;

    public void LoadStopWords(String url) throws IOException {
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
    /*public void clearAr(ArrayList<String> ar){
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
     }*/

    /* public ArrayList<String> getAr() {
     return ar;
     }*/
    public ArrayList<String> receberDocumento(String line) throws IOException {

        ar = new ArrayList<>();
        
        

        //Dividir por tokens o parte do documento
      /*  StringTokenizer st = new StringTokenizer(line.replaceAll("\\<[^>]*>", " ").replaceAll("[^\\w'. ]", " ").
         replaceAll("(?!([0-9]+))([\\.\\,]+)(?!([0-9]+))", " ").replaceAll("\\.", " ").replaceAll(" +", " ").trim()); */
        //StringTokenizer st = new StringTokenizer(parseEndOfSentence(parseSpecialCharacters(parseAcronyms(parseDecimalNumbers(line)))));
        String[] st = split_banthar(parseEndOfSentence(parseSpecialCharacters(parseAcronyms(parseDecimalNumbers(line)))), ' ');

        //while (st.hasMoreElements()) { //Corre se a string tokeneizer tiver mais elementos
        for (String sts : st) {
           // String token = st.nextToken().trim()System.out.println("sts: " + sts);
            //Verifica se o token é uma stopword ou não

            if (!StopWord.contains(sts)) {

                //Passa o token pelo stemmer e pelo stemmer
                String tokentratado = CheckSpecialCases(sts).replaceAll("\\'", " ").replaceAll(" +", "").replace(" ", "").replaceAll(">", "").replace("<", "").toLowerCase();
                //String tokentratado = CheckSpecialCharacters(parseTags(parseDecimalNumbers(parseAcronyms(parseSpecialCharacters(parseEndOfSentence(token))))));
                if (!tokentratado.isEmpty()) {
                    String tokenStemmer = stemming(tokentratado);

                   // System.out.println("sts: " + tokenStemmer);
                    //Insere o token já tratado num arraylist
                    ar.add(tokenStemmer);
                }
            }
        }
        //Devolve o arraylist já preenchido de tokens
        return ar;

    }

    public String parseEndOfSentence(String text) {
        return text.replaceAll("(?!([a-z]{1}))([\\.\\,])(?=([A-Z]{1}))", " ");
    }

    public String parseSpecialCharacters(String text) {
        return text.replaceAll("[^0-9a-zA-Z'.<> ]+", " ");
    }

    public String parseAcronyms(String text) {
        return text.replaceAll("(?!([0-9]+))([\\.\\,]+)(?!([0-9]+))", " ");
    }

    public String parseDecimalNumbers(String text) {
        return text.replaceAll("\\.", " ");
    }

    public String parseTags(String text) {
        return text.replaceAll("<\\w*>?|<?\\w*>", " ");
    }

    public String CheckSpecialCharacters(String text) {
        return text.replaceAll("<\\d>|<\\\\\\d>", " ").replaceAll(" +", " ");
    }

    public String CheckSpecialCases(String text) {
        //ESTA BOM return text.replaceAll("\\b'\\b", "");
        return text.replaceAll("\\b'\\b", "").replaceAll(" +", " "); ///DONE
    }

    public String stemming(String word) {
        snowballStemmer.setCurrent(word);

        snowballStemmer.stem();

        return snowballStemmer.getCurrent();

    }

    //custom split
    private static String[] split_banthar(String s, char delimeter) {
        int count = 1;

        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == delimeter) {
                count++;
            }
        }
        String[] array = new String[count];

        int a = -1;
        int b = 0;

        for (int i = 0; i < count; i++) {

            while (b < s.length() && s.charAt(b) != delimeter) {
                b++;
            }
            array[i] = s.substring(a + 1, b);
            a = b;
            b++;
        }

        return array;
    }
}
