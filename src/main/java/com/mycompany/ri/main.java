/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.ri;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 *
 * @author joaoa
 */
public class main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        HashMap<ZipEntry, HashMap<String, String>> table = new HashMap<>();
        HashMap<String, HashMap<String, Double>> docIds = new HashMap<>();
        ZipFile zipFile = new ZipFile("C:\\Users\\Mafalda Rodrigues\\Desktop\\Mestrado\\RI\\RI\\src\\main\\java\\com\\mycompany\\ri\\corpus-RI.zip");

        Enumeration<? extends ZipEntry> entries = zipFile.entries();

        while (entries.hasMoreElements()) {
            ZipEntry entry = entries.nextElement();
            InputStream stream = zipFile.getInputStream(entry);
            table.put(entries.nextElement(), new HashMap<>());
            //System.out.println(stream);
            // BufferedInputStream reader = new BufferedInputStream(stream);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(zipFile.getInputStream(entry)));

            String line;
            while ((line = bufferedReader.readLine()) != null) {

                if (!line.startsWith("@")) {
                   //System.out.println(line.trim().split("\"")[0]);
                    String[] namesList = line.split("\"");
                    for(String x:namesList ){
                        if(x.endsWith(",")){
                           System.out.println(x); 
                        }
                        
                    }
                    
                    
                   

                   

                    //Matcher m = Pattern.compile(".*[^0-9].*").matcher(line);
                    //while(m.find()){
                    //docIds.put(line,  new HashMap<>());
                       // ints.add(Integer.parseInt(m.group()));
                    //}
                    /*  StringTokenizer st = new StringTokenizer(line,",");
                     line.matches("\\D+");
                     String word=st.nextToken();
                     //Integer.parseInt(word)
                     if(){
                            
                     }
                     System.out.println(line);
                     */
                }

            }

//        System.out.println(table);
        }
    }
}


// StringTokenizer st = new StringTokenizer(line, ",");
//
//                    while (st.hasMoreElements()) {
//                        String i = st.nextToken();
//
//                        System.out.println(i);
//                    }
