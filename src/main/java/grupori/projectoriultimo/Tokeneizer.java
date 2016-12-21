package grupori.projectoriultimo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import org.tartarus.snowball.ext.englishStemmer;
import org.tartarus.snowball.SnowballStemmer;

/**
 * @author João Amaral
 * @author Mafalda Rodrigues
 */
public class Tokeneizer {

    static HashSet<String> StopWord = new HashSet<>();

    SnowballStemmer snowballStemmer = new englishStemmer();
    ArrayList<String> arTokens;
    
    public static void LoadStopWords(String url) throws IOException {
        //ler o ficheiro txt
        FileReader f = new FileReader(url);
       
        BufferedReader br = new BufferedReader(f);

        String sCurrentLine;

        //Percorre o ficheiro linha a linha 
        while ((sCurrentLine = br.readLine()) != null) {
            StopWord.add(sCurrentLine);
        }
        br.close();
    }

    public ArrayList<String> receiveDoc(String line, boolean stemmercheck) throws IOException {
        arTokens = new ArrayList<>();
      
        String[] st = splitBanthar.split_banthar(parseEndOfSentence(parseSpecialCharacters(parseAcronyms(parseDecimalNumbers(line)))), ' ');
   
        for (String sts : st) {
            if (!StopWord.contains(sts)) {
                String tokentratado = CheckSpecialCases(sts).replaceAll("\\'", " ").replaceAll(" +", "").replace(" ", "").replaceAll(">", "").replace("<", "").toLowerCase();
                //String tokentratado = CheckSpecialCharacters(parseTags(parseDecimalNumbers(parseAcronyms(parseSpecialCharacters(parseEndOfSentence(sts))))));
                
                if (!tokentratado.isEmpty()) {
                    String tokenStemmer;
                    if(stemmercheck){
                        tokenStemmer = stemming(tokentratado);
                    }else{
                        tokenStemmer = tokentratado;
                    }
                    arTokens.add(tokenStemmer);
                }
            }
        }
        //Devolve o arraylist já preenchido de tokens
        return arTokens;
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
}
