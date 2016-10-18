/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.ri;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
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
        ZipFile zipFile = new ZipFile("C:\\Users\\Mafalda Rodrigues\\Desktop\\Mestrado\\RI\\corpus-RI.zip");

        Enumeration<? extends ZipEntry> entries = zipFile.entries();

        while(entries.hasMoreElements()){
            ZipEntry entry = entries.nextElement();
            InputStream stream = zipFile.getInputStream(entry);
            table.put(entries.nextElement(), new HashMap<>());
            //System.out.println(entries.nextElement().toString()); 
        }
        System.out.println(table);
        
        
        
        
    
               
                
    }
    
}
