package grupori.projectoriultimo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.jsoup.select.Elements;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

/**
 * @author João Amaral
 * @author Mafalda Rodrigues
 */
public class DocProcessor {
    Indexer index = new Indexer();
    Tokeneizer token = new Tokeneizer();
    
    HashMap<Integer, String> map = new HashMap<>();
    int contSendDoc = 0; //Para no metodo de verificar memoria seja possivel ver quantos documentos foram lidos até ao momento do save
    
    StringBuilder document = null;
    FileInputStream fis = null;
    CSVParser parser;
    
    private String Unzip(String SourceZip){
        String detination = SourceZip.substring(0, SourceZip.lastIndexOf("."));
        try {    
            ZipFile zipFile = new ZipFile(SourceZip);
            zipFile.extractAll(detination);
        } catch (ZipException ex) {
            System.err.println("Unpacking failed.");
        }
        return detination;
    }
    
    public void readPath(String SourceZip, boolean stemmercheck){
        try {
            File folder;
            if(SourceZip.endsWith(".zip")){
                folder = new File(Unzip(SourceZip));
            }else{
                folder = new File(SourceZip);
            }
            
            File[] listOfFiles = folder.listFiles();
            for(int i=0; i<listOfFiles.length;i++){
                if(listOfFiles[i].getName().endsWith(".arff")){
                    System.out.println("Indexing file: " + listOfFiles[i].getName());
                    parseArff(listOfFiles[i],listOfFiles[i].getName(),stemmercheck);
                }else if(listOfFiles[i].getName().endsWith(".csv")){
                    try {
                        System.out.println("Indexing file: " + listOfFiles[i].getName());
                        parseCSV(listOfFiles[i],listOfFiles[i].getName(),stemmercheck);
                    } catch (IOException ex) {
                        Logger.getLogger(DocProcessor.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }else{
                    System.out.println("File extention not supported.");
                }
            }
            index.saveToDisc();
            index.finalwhipe(contSendDoc);
            System.out.println("Merging all sub indexes");
            index.reductionIndex();
        } catch (IOException ex) {
            Logger.getLogger(DocProcessor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
    private void parseArff(File filePath,String fileName,boolean stemmercheck){
        BufferedReader bufferedReader = null;
        String line;
        Pattern padrao = Pattern.compile("\\\"([^\\\"]*)\\\""); 
        String regex_inicial = "@";
        
        try {
            bufferedReader = new BufferedReader(new FileReader(filePath));
        } catch (FileNotFoundException ex) {
            System.out.println("Wasn't enable to open the file");
        }
            
        try {
            while ((line = bufferedReader.readLine()) != null) {
                //ler todas as linhas que nao tenham um @ (como foi descrito na variavel regex_inicial)
                if (!line.startsWith(regex_inicial)) {
                    if (line.length() != 0) { //ler todas as linha que nao estejam vazias
                        String[] lineSplit = line.split(",", 2);
                        Matcher matcher = padrao.matcher(lineSplit[1]);
                        while (matcher.find()) {
                            int docID = Integer.parseInt(lineSplit[0]);
                            String doc = matcher.group(1).toLowerCase();
                            
                            index.addIndexHM(token.receiveDoc(doc,stemmercheck), docID, fileName);
                            index.memoryStore(contSendDoc);
                        }
                    }
                }
            }
        } catch (IOException ex) {  
            Logger.getLogger(DocProcessor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void parseCSV(File filePath, String fileName,boolean stemmercheck) throws FileNotFoundException, IOException{
        fis = new FileInputStream(filePath);
        BufferedReader br = new BufferedReader(new InputStreamReader(fis));
        parser = new CSVParser(br, CSVFormat.DEFAULT.withHeader());
        Iterator it = parser.iterator();

        while(it.hasNext()){
            document = new StringBuilder();
            contSendDoc++;
            CSVRecord record = (CSVRecord) it.next();
            int idDoc = Integer.parseInt(record.get("Id"));

            if(!fis.toString().contains("Tags")){

            String aux;
                if(record.isMapped("Title") && record.isMapped("Body")){
                    document.append(record.get("Title"));
                    document.append(" ");
                    //texto.append(CSVRegex(record.get("Body")));
                    document.append(record.get("Body").replaceAll("(?s)<code>.*?</code>", "").replaceAll("<[^>]*>","").trim()); //remove tags;
                }else {
                    //texto.append(CSVRegex(record.get("Body")));
                    
                    document.append(record.get("Body").replaceAll("(?s)<code>.*?</code>", "").replaceAll("<[^>]*>","").trim()); //remove tags;
                }

                aux = document.toString().toLowerCase();
        
                index.addIndexHM(token.receiveDoc(aux,stemmercheck), idDoc, fileName);
                index.memoryStore(contSendDoc);
            }
        }
    }
    
    public StringBuilder CSVRegex(String str) {
        
        Elements doc = Jsoup.parse(str).select("p,li,a");

        StringBuilder sb = new StringBuilder();
        for (Element x : doc) {
            sb.append(x.text()).append(" ");
        }

        return sb;
    }
  
}


