/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grupori.projectoriultimo;

import com.univocity.parsers.common.processor.RowListProcessor;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author joaoa
 */
public class DocProcessor {
    int cont=0;
    ArrayList<String> ar = new ArrayList<>();
    Runtime runtime = Runtime.getRuntime();
    //Padrão para usar o regex para tira as "" que rodeiam o documento
    Pattern padrao = Pattern.compile("\\\"([^\\\"]*)\\\""); 

    //Comentario temporariamente
    //HashMap<Integer, String> hmap = new HashMap<Integer, String>();

    //Para filtrar as linhas que começarem com um "@"
    String regex_inicial = "@";
        
    //Para cada linha (ids e os documentos) que contém cada ficheiro 
    String line;
    
    public void readPath(String PastaUnziped) throws IOException{
        File folder = new File(PastaUnziped);
        File[] listOfFiles = folder.listFiles();
        if(listOfFiles.length >1){
            if (listOfFiles[0].isFile()) {
                   if(listOfFiles[0].getName().endsWith(".arff")){
                       //parse do arff
                       parseArff(listOfFiles[0]);
                   }else{
                       System.out.println("csv");
                       //parse do csv
                       parseCSV(listOfFiles[0]);
                   }
                }else if (listOfFiles[0].isDirectory()) {
                    if(listOfFiles[0].getName() != "__MACOSX"){
                        readPath(PastaUnziped+"\\" +listOfFiles[0].getName());
                    }
                }
        }else{
            for (int i = 0; i < listOfFiles.length; i++) {
                //System.out.println(listOfFiles[i]);
                if (listOfFiles[i].isFile()) {
                   if(listOfFiles[i].getName().endsWith(".arff")){
                       //parse do arff
                       parseArff(listOfFiles[i]);
                   }else{
                       System.out.println("csv");
                       //parse do csv
                       parseCSV(listOfFiles[i]);
                   }
                }else if (listOfFiles[i].isDirectory()) {
                    if(listOfFiles[i].getName() != "__MACOSX"){
                        readPath(PastaUnziped+"\\" +listOfFiles[i].getName());
                    }
                }
            }
        }
    }    
    
    private void parseArff(File caminhoFicheiro){
        System.out.println("entrou no csv");
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(caminhoFicheiro));
        } catch (FileNotFoundException ex) {
            System.out.println("Não conseguiu ler o ficheiro");
        }
            
        try {
            //Percorre todas as linhas de cada ficherio
            while ((line = bufferedReader.readLine()) != null) {
                //ler todas as linhas que nao tenham um @ (como foi descrito na variavel regex_inicial)
                if (!line.startsWith(regex_inicial)) {
                    //ler todas as linha que nao estejam vazias
                    if (line.length() != 0) {
                        String[] teste = line.split(",", 2);
                        Matcher matcher = padrao.matcher(teste[1]);
                        while (matcher.find()) {
                            int docID = Integer.parseInt(teste[0]);
                            String doc = matcher.group(1);
                            System.out.println(docID);
                            System.out.println(doc);
                            //enviar para o main OU enviar para o index
                        }
                    }
                }
            }
        } catch (IOException ex) {  
            Logger.getLogger(DocProcessor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void parseCSV(File caminhoFicheiro){
        System.out.println("entrou no parcecsv");
        CsvParserSettings parserSettings = new CsvParserSettings();
        //parserSettings.getFormat().setLineSeparator("\n");
        parserSettings.setLineSeparatorDetectionEnabled(true);
        parserSettings.setMaxCharsPerColumn(60000);
        RowListProcessor rowProcessor = new RowListProcessor();
        parserSettings.setRowProcessor(rowProcessor);
        parserSettings.setHeaderExtractionEnabled(true);
        parserSettings.setReadInputOnSeparateThread(true);
        parserSettings.selectFields("Id", "Body");
        //5000 em 5000 linhas grava ou faz algo    
       // parserSettings.setNumberOfRecordsToRead(500000);
        CsvParser parser = new CsvParser(parserSettings);
        
        try{
            System.out.println("grupori.projectoriultimo.DocProcessor.parseCSV()");
                parser.beginParsing(caminhoFicheiro);
                String[] line;
                while ((line = parser.parseNext()) != null)
                {
                    try{
                        int key = Integer.parseInt(line[0]);
                        //String value = line[1].replaceAll("(?s)<pre>.*?</pre>", " ").replaceAll("(?s)<code>.*?</code>", " ").replaceAll("\\<[^>]*>", " ").replaceAll("\t", " ").replaceAll("\n", " ").replaceAll(" +", " ");
                        String value = line[1].replaceAll("\\<[^>]*>", "").replaceAll("[^a-zA-Z0-9]", " ").replaceAll("\\s+", " ").trim();
                        
                        if(value.length() != 0){
                            ar.add(value);
                          //  System.out.println(key);
                            //System.out.println(value);
                        }
                        checkMemoryAndStore();
                        //System.out.println("LASTPART:" +lastPart);
                        //System.out.println("NEXT:" +value);
                        //System.out.println("ID:"+key);
                        //System.out.println("Valor:"+value);
                    }catch(Exception ex){
                        System.out.println("ex Parse Int: " + ex.toString());
                    }
                }
            }catch(Exception ex){
                System.out.println("EX: " + ex.toString());
            }
    }
    
    public void checkMemoryAndStore() throws IOException {
       // System.out.println("Checking memory...");
        long usedMem = (100*(runtime.totalMemory() - runtime.freeMemory()))/runtime.totalMemory();
        //System.out.println(usedMem);
        
        if(usedMem > 70){
            System.out.println("Antes: " + ar.size());
            cont++;
            System.out.println(cont + ": limpeza - 70");
            ar.clear();
            System.gc();
            System.out.println("Depois: " + ar.size());
            ar = new ArrayList<>();
//            System.gc();
            /*if(usedMem>40){
                System.out.println("limpeza - 40");
                System.gc();
            }*/
        }
    }
}


