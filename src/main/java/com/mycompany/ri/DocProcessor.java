package com.mycompany.ri;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * @author  João Amaral
 * @author  Mafalda Rofrigues
 */
public class DocProcessor {
    //Padrão para usar o regex para tira as "" que rodeiam o documento
    Pattern padrao = Pattern.compile("\\\"([^\\\"]*)\\\""); 
    //O arraylist serve para armazenar todas as linhas lidas e tratadas para serem usadas no tokeneizer
    ArrayList<String> DocumentoEmMemoria = new ArrayList<String>(); 
   
    
    //Para filtrar as linhas que começarem com um "@"
    private String regex_inicial = "@";

    /**
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
        Tokeneizer to = new Tokeneizer();
        
        String ficheiroOnly = null;
        
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
            for(int i=0;i<ficheiro.length;i++){
                if(ficheiro[i].endsWith(".arff")){
                    ficheiroOnly = ficheiro[i];
                }
            }
            /*Isto serve para filtrar a pasta __MACOSX*/         
            if(!ficheiro[0].contains("MACOSX")){
                //Percorre todas as linhas de cada ficherio
                while ((line = bufferedReader.readLine()) != null) {
                    //ler todas as linhas que nao tenham um @ (como foi descrito na variavel regex_inicial)
                    if (!line.startsWith(regex_inicial)) { 

                        //ler todas as linha que nao estejam vazias
                        if (line.length() != 0) { 
                            //Adciona para o arraylist (para ser usado no tokeneizer) os documento
                            DocumentoEmMemoria.add(DivideLine(line, ficheiroOnly));
                        }
                    }
                }
            }
        }
      //  System.out.println("DocProcessor: Leu o zip todo");
        //Enviar o arraylist para o tokeneizer
        to.FromDocProcessor(DocumentoEmMemoria);
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
    public String DivideLine(String line, String ficheiro){
            
            String rebuildString = null;
            //separar cada documento no primeiro ','
            String[] teste = line.split(",",2); 
            
            //Para activar o regex
            Matcher matcher = padrao.matcher(teste[1]); 

            while (matcher.find()) {
                //exemplo do resultado que a função devolve: 
                // - idDoc_NomeDoFicheiro, TextoDoDoc
                rebuildString = teste[0] + "_" + ficheiro + "," + matcher.group(1);
            }
        return rebuildString;
    }

    /**
     * Para chamar o arraylist caso seja necessario
     * 
     * @return
     */
    public ArrayList<String> getMenDocs2() {
        return DocumentoEmMemoria;
    }
}