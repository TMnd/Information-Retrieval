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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

/**
 *
 * @author joaoa
 */
public class DocProcessor {
    Indexer id = new Indexer();
    Tokeneizer tk = new Tokeneizer();
    
    //HashSet<String> hs = new HashSet<String>();
    HashMap<Integer, String> map = new HashMap<>();
    int contEnviarDoc = 0;
    
    StringBuilder texto = null;
    FileInputStream fis = null;
    CSVParser parser;
            
    Runtime runtime = Runtime.getRuntime();
    
    //Padrão para usar o regex para tira as "" que rodeiam o documento
    
    //Comentario temporariamente
    //HashMap<Integer, String> hmap = new HashMap<Integer, String>();

    //Para filtrar as linhas que começarem com um "@"
    
        
    //Para cada linha (ids e os documentos) que contém cada ficheiro 
   
    
    private String Unzip(String SourceZip){
        System.out.println("entrou no unzip");
        System.out.println("SourceZip: " + SourceZip);
        String detination = SourceZip.substring(0, SourceZip.lastIndexOf("."));
        System.out.println("2 - " + detination);
        try {    
            ZipFile zipFile = new ZipFile(SourceZip);
            zipFile.extractAll(detination);
        } catch (ZipException ex) {
            System.err.println("Falha na descompactação!");
        }
        System.out.println("devolve");
        return detination;
    }
    
    public void readPath(String SourceZip) throws IOException{
        System.out.println("entrou");

        File folder;
        if(SourceZip.endsWith(".zip")){
            folder = new File(Unzip(SourceZip));
        }else{
            folder = new File(SourceZip);
        }
        System.out.println("entrou 2");
        File[] listOfFiles = folder.listFiles();
        for(int i=0; i<listOfFiles.length;i++){
           if(listOfFiles[i].getName().endsWith(".arff")){
               System.out.println("é um ficheiro .arff");
               //parse arff
               parseArff(listOfFiles[i]);
           }else{
                System.out.println("Ficheiro: " + listOfFiles[i].toString());
                parseCSV(listOfFiles[i]);
                //parseCSV_antigo(folder);
                /*System.out.println("Change file");
                id.saveDisc();
                id.finalwhipe(contEnviarDoc);*/
           }
        }
        System.out.println("Last save");
        id.saveDisc();
        System.out.println("Final Wipe!");
        id.finalwhipe(contEnviarDoc);
       /* System.out.println("Merge...");
        id.reduçãoIndex();*/
    }    
    
    private void parseArff(File caminhoFicheiro){
        System.out.println("entrou no csv");
        BufferedReader bufferedReader = null;
        String line;
        Pattern padrao = Pattern.compile("\\\"([^\\\"]*)\\\""); 
        String regex_inicial = "@";
        
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
    
    private void parseCSV(File caminhoFicheiro) throws FileNotFoundException, IOException{
        fis = new FileInputStream(caminhoFicheiro);
        BufferedReader br = new BufferedReader(new InputStreamReader(fis));
        parser = new CSVParser(br, CSVFormat.DEFAULT.withHeader());
        Iterator it = parser.iterator();

        while(it.hasNext()){
            texto = new StringBuilder();
            contEnviarDoc++;
            CSVRecord record = (CSVRecord) it.next();
            int idDoc = Integer.parseInt(record.get("Id"));

            if(!fis.toString().contains("Tags")){

            String aux;
                if(record.isMapped("Title") && record.isMapped("Body")){
                    texto.append(record.get("Title"));
                    texto.append(" ");
                    texto.append(record.get("Body").replaceAll("(?s)<code>.*?</code>", "").replaceAll("<[^>]*>","").trim()); //remove tags;
                }else {
                    texto.append(record.get("Body").replaceAll("(?s)<code>.*?</code>", "").replaceAll("<[^>]*>","").trim()); //remove tags;
                }

                aux = texto.toString().toLowerCase();

                id.addTM(tk.receberDocumento(aux), idDoc);
                id.memory(contEnviarDoc);
            }
        }
    }
    
    private String parseCSV_antigo(File caminhoFicheiro){
        /*System.out.println("entrou no parcecsv");
        
        StringBuilder sbb = new StringBuilder();
        
        CsvParserSettings settings =  new CsvParserSettings();
        settings.getFormat().setLineSeparator("\n");
        
        CsvParser parser = new CsvParser(settings);
        
        parser.beginParsing(caminhoFicheiro);
        
        String[] row;
        while((row = parser.parseNext()) != null){
            
        }*/
        
        
        
        CsvParserSettings parserSettings = new CsvParserSettings();
        parserSettings.getFormat().setLineSeparator("\n");
        //parserSettings.setLineSeparatorDetectionEnabled(true);
        parserSettings.setMaxCharsPerColumn(60000);
        RowListProcessor rowProcessor = new RowListProcessor();
        parserSettings.setRowProcessor(rowProcessor);
        parserSettings.setHeaderExtractionEnabled(true);
       //parserSettings.setReadInputOnSeparateThread(true);
        parserSettings.selectFields("Id","Title","Body");
        parserSettings.setSkipEmptyLines(true);
        //5000 em 5000 linhas grava ou faz algo    
        //parserSettings.setNumberOfRecordsToRead();//3000);
        CsvParser parser = new CsvParser(parserSettings);
        
        try{
  
                parser.beginParsing(caminhoFicheiro);
                String[] line;
                
                while ((line = parser.parseNext()) != null)
                {
                    //System.out.println("1");
                   
                    //System.out.println("1.5");
                    try{
                       // System.out.println("2");
                        int key = Integer.parseInt(line[0]);
                        String value = line[1].replaceAll("(?s)<pre>.*?</pre>", "").replaceAll("(?s)<code>.*?</code>", "").replaceAll("<[^>]*>","");
                        //String value = ola3(ola2(ola(line[1])));//line[1].replaceAll("\\<[^>]*>", "").replaceAll("[^a-zA-Z0-9]", " ").replaceAll("\\s+", " ").trim();
                        
                        
                        
                        if(value.length() != 0){
                          // System.out.println("3");
                          //map.put(key, value);
                //hs.put(value);
                            //System.out.println(key);
                         //  System.out.println(value);
                            contEnviarDoc++;
                          //  System.out.println("a enviar o documento: " + contEnviarDoc);
                            id.addTM(tk.receberDocumento(value),key);
                        }
                        
                       id.memory(contEnviarDoc);
                        //System.out.println("LASTPART:" +lastPart);
                        //System.out.println("NEXT:" +value);
                        //System.out.println("ID:"+key);
                        //System.out.println("Valor:"+value);
                    }catch(Exception ex){
                        System.out.println("ex Parse Int: " + ex.toString());
                    }
                }
                System.out.println("last save");
                id.saveDisc();
                System.out.println("merging...");
                id.reduçãoIndex();
            }catch(Exception ex){
                System.out.println("EX: " + ex.toString());
            }
        return null;
    }
    
    
    private String ola(String olaaa){
        return olaaa.replaceAll("\\<[^>]*>", "");
    }
    
    private String ola2(String olaaa){
        return olaaa.replaceAll("[^a-zA-Z0-9]", " ");
    }
    
    private String ola3(String olaaa){
        return olaaa.replaceAll("\\s+", " ");
    }
    
    /*public void checkMemoryAndStore() throws IOException {
        
        //System.out.println("Checking memory...");
        //long usedMem = (100*(runtime.maxMemory() - runtime.freeMemory()))/runtime.totalMemory();
        
        long totalfreememory = runtime.freeMemory() + (runtime.maxMemory() - runtime.totalMemory());  
        //System.out.println(totalfreememory);
        long usedMem = 100*(runtime.totalMemory()-totalfreememory)/runtime.totalMemory();
        //System.out.println(usedMem);

        if(usedMem > 70){
            map.clear();
            Runtime.getRuntime().gc();
            //System.gc();
            if(usedMem>20){
                System.gc();   
            }
        }
    }*/
}


