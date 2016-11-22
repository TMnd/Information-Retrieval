package com.mycompany.ri;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
/**
 * @author  João Amaral
 * @author  Mafalda Rofrigues
 */
public class DocProcessor {
    Tokeneizer to = new Tokeneizer();
    Indexer index23 = new Indexer();
    //Padrão para usar o regex para tira as "" que rodeiam o documento
    Pattern padrao = Pattern.compile("\\\"([^\\\"]*)\\\""); 
    //O arraylist serve para armazenar todas as linhas lidas e tratadas para serem usadas no tokeneizer
    ArrayList<String> ar = new ArrayList<String>(); 
  //  HashMap<Integer, String> hmap = new HashMap<Integer, String>();
    
    //Para filtrar as linhas que começarem com um "@"
    private String regex_inicial = "@";

    /**
     * Ler o ficheiro zip inserido pelo utilizador
     * 
     * Esta função é chamada na classe do CorpusReader.
     * Como parametro encontra-se uma string que corresponde ao caminho 
     * para o documento inserido pelo utilizador posteriormente para que depois
     * a o metodo "zipFile" possa processar o ficheiro zip e inserir todos os
     * documentos que cada ficheiro contém para um arraylist que posteriormente 
     * será usado pela classe Tokeneizer
     * 
     * @param file
     * @throws IOException
     */
    public void readFileZip(String file) throws IOException {
        CSVParser parser;
        String ficheiroOnly = null;
        StringBuilder merge=new StringBuilder();
        
        //Esta classe é usada para ler todos os ficheiro que se encontram no ficheiro zip
        ZipFile zipFile = new ZipFile(file);
        
        //Serve para percorrer todos os ficheriros dentro do ficheiro zip
        Enumeration<? extends ZipEntry> entries = zipFile.entries();

        //Percorre todos os ficheiros do ficheiro zip
        while (entries.hasMoreElements()) {

            ZipEntry entry = entries.nextElement();
            
            

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(zipFile.getInputStream(entry)));

            //Para cada linha (ids e os documentos) que contém cada ficheiro 
            String line;
             //Map<Integer, String> mapaNomes = new HashMap<Integer, String>();
            //Para adquirir o nome de cada ficheiro ex: se o ficheiro tiver uma pasta sera: NomeDaPasta/NomeDoFicheiro. se for só o ficheiro aparecera só o ficheiro
            String[] ficheiro = entry.getName().split("/");
            for (int i = 0; i < ficheiro.length; i++) {
                if (ficheiro[i].endsWith(".arff")) {
                    ficheiroOnly = ficheiro[i];
          
                    //System.out.println(ficheiroOnly);
                     /*Isto serve para filtrar a pasta __MACOSX*/
                    if (!ficheiro[0].contains("MACOSX")) {
                        
                        //Percorre todas as linhas de cada ficherio
                        while ((line = bufferedReader.readLine()) != null) {
                            //ler todas as linhas que nao tenham um @ (como foi descrito na variavel regex_inicial)
                            if (!line.startsWith(regex_inicial)) {
                                //ler todas as linha que nao estejam vazias
                                if (line.length() != 0) {
                                    
                                    //line = é o documento --- ficheiroOnly = nome do ficheiro
                                    DivideLine(line.toLowerCase(), ficheiroOnly);
                                    
         
                                    //System.out.println(hmap.size());
                                    
                                    //Adciona para o arraylist (para ser usado no tokeneizer) os documento
                                    //System.out.println(ficheiroOnly);
                                   // DocumentoEmMemoria.add(DivideLine(line, ficheiroOnly));
                                }
                            }
                        }
                        
                    }

                }else if (ficheiro[i].endsWith(".csv")) {
                    ficheiroOnly = ficheiro[i];
                    String all;
                    parser = new CSVParser(bufferedReader, CSVFormat.DEFAULT.withHeader());
                    Iterator it = parser.iterator();
                    int idDoc;
                   // String OwnId;
                    System.out.println(ficheiroOnly);
                    
                    while (it.hasNext()) {
                        CSVRecord record = (CSVRecord) it.next();
                        //System.out.println(record);
                        idDoc = Integer.parseInt(record.get("Id"));
                       
                        //System.out.println(idDoc);
                        //dc.setIdDoc(idDoc);
                       // hmap.put(idDoc, ficheiroOnly);
                        
                        
                        if (!ficheiro[0].contains("Tags")) {

                            //OwnId = Integer.parseInt(record.get("OwnerUserId"));
                            //OwnId = String.valueOf(record.get("OwnerUserId"));
                            //System.out.println(record.get("OwnerUserId"));
                            //dc.setOwnerUserId(OwnId);

                            //dc.setCreationDate(record.get("CreationDate"));

                           // dc.setClosedDate(record.get("ClosedDate"));
                            //dc.setScore(record.get("Score"));
                           // dc.setPath(ficheiroOnly);
                            String aux;
                           
                            if (record.isMapped("Title") && record.isMapped("Body")) {
                                
                                merge.append(record.get("Title"));
                                merge.append(" ");
                                merge.append(record.get("Body"));
                                //merge = record.get("Title") + record.get("Body");
    
                               // merge = merge.replaceAll("(?s)<pre>.*?</pre>", " ").replaceAll("(?s)<code>.*?</code>", " ").replaceAll("\\<[^>]*>", " ").replaceAll("\t", " ").replaceAll("\n", " ").replaceAll(" +", " ");

                            } else {
                                 merge.append(record.get("Body"));  
                            }
                            
                             aux = merge.toString().replaceAll("(?s)<code>.*?</code>", " ").replaceAll("\\<[^>]*>", " ").replaceAll("\t", " ").replaceAll("\n", " ").replaceAll(" +", " ");
                             //System.out.println(merge);
                             //return ;
                            index23.checkMemoryAndStore();
                            ar = to.FromDocProcessor(aux.toLowerCase(),idDoc);
                           // System.out.println("Arraylist para o indexer");
                            index23.setHM(ar,idDoc);
                        } else {
                            all = record.get("Tag");
                            to.FromDocProcessor(all.toLowerCase(),idDoc);
                        }
                    }
                }
            }
            
        }
        System.out.println("es 1 conas");
        
        
        
        //System.out.println(hmap);
        //System.out.println("DocProcessor: Leu o zip todo");
        //Enviar o arraylist para o tokeneizer
        //to.FromDocProcessor(DocumentoEmMemoria);
    }

    /**
     * Serve para dividir cada documento para poder ser tratado de modo que 
     * se possa acrescentar o nome do ficheiro ao id para evitar a possibilidade
     * de conter ids de decumentos iguais em ficheiros diferentes.
     * 
     * @param line
     * @param ficheiro
     * @return
     */
    public void DivideLine(String line, String ficheiro) throws IOException { 
        String rebuildString = null;

        //separar o id pelo documento
        String[] teste = line.split(",", 2);

        //Para activar o regex no documento
        Matcher matcher = padrao.matcher(teste[1]);
        int idDoc;
        while (matcher.find()) {
            idDoc = Integer.parseInt(teste[0]);
           // dc.setIdDoc(idDoc);
           // dc.setPath(ficheiro);
            //System.out.println(dc.getId());
            
            //hmap.put(idDoc, ficheiro);
            
            rebuildString = matcher.group(1);
         //   System.out.println(hmap.size());
            //enviar para o doc processor o documento e o id do proprio
            to.FromDocProcessor(rebuildString, idDoc);
        }
        
        
        //System.out.println(dc.getIdDoc());
       //return rebuildString;
    }
    
    //MERGING TIME!!!!
    /*public void merge(){
        HashMap<String,HashMap<Integer,Integer>> hm = new HashMap<>();
        char 
    }*/
    
    /**
     * Para chamar o arraylist caso seja necessario
     * 
     * @return
     */
    /*public ArrayList<String> getMenDocs2() {
        return DocumentoEmMemoria;
    }*/
}