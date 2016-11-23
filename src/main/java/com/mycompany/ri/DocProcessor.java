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
 * @author  Mafalda Rodrigues
 */
public class DocProcessor {
    Tokeneizer to = new Tokeneizer();
    Indexer indexer = new Indexer();
    
    //Padrão para usar o regex para tira as "" que rodeiam o documento
    Pattern padrao = Pattern.compile("\\\"([^\\\"]*)\\\""); 
    
    //Comentario temporariamente
    //HashMap<Integer, String> hmap = new HashMap<Integer, String>();
    
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
            
            //Para adquirir o nome de cada ficheiro ex: se o ficheiro tiver uma pasta sera: NomeDaPasta/NomeDoFicheiro. se for só o ficheiro aparecera só o ficheiro
            String[] ficheiro = entry.getName().split("/");
            for (int i = 0; i < ficheiro.length; i++) {
                if (ficheiro[i].endsWith(".arff")) {
                    ficheiroOnly = ficheiro[i];
          
                     /*Isto serve para filtrar a pasta __MACOSX*/
                    if (!ficheiro[0].contains("MACOSX")) {
                        //Percorre todas as linhas de cada ficherio
                        while ((line = bufferedReader.readLine()) != null) {
                            //ler todas as linhas que nao tenham um @ (como foi descrito na variavel regex_inicial)
                            if (!line.startsWith(regex_inicial)) {
                                //ler todas as linha que nao estejam vazias
                                if (line.length() != 0) {
                                    //line = é o documento --- ficheiroOnly = nome do ficheiro
                                    DivideLine(line, ficheiroOnly);
                                }
                            }
                        }
                    }
                }else if (ficheiro[i].endsWith(".csv")) {
                    ficheiroOnly = ficheiro[i];
                    parser = new CSVParser(bufferedReader, CSVFormat.DEFAULT.withHeader());
                    Iterator it = parser.iterator();
                    int idDoc;
                    
                    while (it.hasNext()) {
                        CSVRecord record = (CSVRecord) it.next();
                        idDoc = Integer.parseInt(record.get("Id"));
                       
                        //Temporariamente comentado
                        //hmap.put(idDoc, ficheiroOnly);
                        
                        
                        if (!ficheiro[0].contains("Tags")) {
                            //Informação
                            //OwnId = Integer.parseInt(record.get("OwnerUserId"));
                            //OwnId = String.valueOf(record.get("OwnerUserId"));
                            //System.out.println(record.get("OwnerUserId"));
                            //dc.setOwnerUserId(OwnId);

                            //dc.setCreationDate(record.get("CreationDate"));

                            //dc.setClosedDate(record.get("ClosedDate"));
                            //dc.setScore(record.get("Score"));
                            //dc.setPath(ficheiroOnly);
                            String aux;
                           
                            if (record.isMapped("Title") && record.isMapped("Body")) {
                                
                                merge.append(record.get("Title"));
                                merge.append(" ");
                                merge.append(record.get("Body"));
                                //merge = record.get("Title") + record.get("Body");
                                //merge = merge.replaceAll("(?s)<pre>.*?</pre>", " ").replaceAll("(?s)<code>.*?</code>", " ").replaceAll("\\<[^>]*>", " ").replaceAll("\t", " ").replaceAll("\n", " ").replaceAll(" +", " ");
                            } else {
                                 merge.append(record.get("Body"));  
                            }
                            
                            aux = merge.toString().replaceAll("(?s)<code>.*?</code>", " ").replaceAll("\\<[^>]*>", " ").replaceAll("\t", " ").replaceAll("\n", " ").replaceAll(" +", " ");
                       
                            indexer.checkMemoryAndStore();
                            indexer.SendTokenizer(aux, idDoc);                         
                        } 
                    }
                }
            }
           
        }
        indexer.reduçãoIndex();
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
        //separar o id pelo documento
        String[] teste = line.split(",", 2);

        //Para activar o regex no documento
        Matcher matcher = padrao.matcher(teste[1]);
        int idDoc;
        while (matcher.find()) {
            idDoc = Integer.parseInt(teste[0]);
            //Informação
            //dc.setIdDoc(idDoc);
            //dc.setPath(ficheiro);
            //System.out.println(dc.getId());
            
            //hmap.put(idDoc, ficheiro);
            
            //rebuildString = matcher.group(1);
     
            indexer.checkMemoryAndStore();
            indexer.SendTokenizer(matcher.group(1), idDoc);
        }
    }
}