   HashMap<ZipEntry, HashMap<String, String>> table = new HashMap<>();
        HashMap<String, HashSet<String>> rabodeboi = new HashMap<>(); //Uso so HashSet em vez do ArrayList foi devido que o hashset nao insere duplicados.
        /* MUDAME A PUTA DA VARIAVEL */
        ZipFile zipFile = new ZipFile("C:\\Users\\Mafalda Rodrigues\\Desktop\\Mestrado\\RI\\RI\\src\\main\\java\\com\\mycompany\\ri\\corpus-RI.zip");
        //ZipFile zipFile = new ZipFile("D:\\Dropbox\\MEI\\RI\\RI\\src\\main\\java\\com\\mycompany\\ri\\corpus-RI.zip");

        Pattern pattern = Pattern.compile("\\\"([^\\\"]*)\\\""); //Para ler os conteudos dentro das " "
        
        Enumeration<? extends ZipEntry> entries = zipFile.entries(); //Para ler ficheiros zipados

        //Para ler cada ficheiro dentro do ficheiro zipado
        while (entries.hasMoreElements()) {
            ZipEntry entry = entries.nextElement();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(zipFile.getInputStream(entry)));

            String line;
            
            //Primeira passagem pelos ficheiros
            while ((line = bufferedReader.readLine()) != null) { 
                String[] id_aux = null;
                Matcher matcher = pattern.matcher(line); //Para activar o regex
        
                HashSet<String> idsLista = new HashSet<>(); //é para inserir os valores dos ids para colocar nos values por key
                String ID = null;
                
                if (!line.startsWith("@")) { //ler todas as linhas que nao tenham um @
                     if (line.length() != 0) { //let todas as linha que nao estejam vazias
                        //GET IDS no inicio de cada linha
                        id_aux = line.split(",");
                        ID = id_aux[0];
                    }
                    
                    
                    //GET text que se enzntra no meio das ""
                    while (matcher.find()) {
                        
                        StringTokenizer st = new StringTokenizer(matcher.group(1));  //Separa cada palavra pelo espaço
                
                        while (st.hasMoreElements()) {
                            String i = st.nextToken();
                            //System.out.println(i);
                            //Encher a has map SÓ com os termos mas com nenhum value por key
                            if (rabodeboi.get(i) == null || !rabodeboi.containsKey(i)) {
                                rabodeboi.put(i, new HashSet<String>());
                                if(!idsLista.contains(ID) ){ //Insere só o hashset nao conter nada
                                    rabodeboi.get(i).add(ID); //Inserir na hashmap de key i (.get(i)) 
                                }
                            }else {
                               if(!idsLista.contains(ID) ){ //Insere só o hashset nao conter nada
                                    rabodeboi.get(i).add(ID); //Inserir na hashmap de key i (.get(i)) 
                                }
                            }
                        }
                    }
                }
            }
            
            //Segunda passagem pelos ficheiros (TENTAR FAZER ISTO TUDO NUMA PASSAGEM)
            
                /*for(int i=0; i<idsLista.size();i++){
                    System.out.print(idsLista.get(i) + " ");
                }*/
            
        }
        /*TESTE*/
        for (Map.Entry<String, HashSet<String>> entrySet : rabodeboi.entrySet()) {
            String key = entrySet.getKey();
            System.out.println(key + ": " + rabodeboi.get(key));
        }
//        System.out.println(table);
    }/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.ri;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 *
 * @author joaoa
 */
public class lixop {
    
}
