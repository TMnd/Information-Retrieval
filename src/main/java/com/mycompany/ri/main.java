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

        while (entries.hasMoreElements()) {
            ZipEntry entry = entries.nextElement();
            InputStream stream = zipFile.getInputStream(entry);
            table.put(entries.nextElement(), new HashMap<>());
            //System.out.println(stream);
            // BufferedInputStream reader = new BufferedInputStream(stream);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(zipFile.getInputStream(entry)));

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                char[] k = line.toCharArray();
                for (int i = 0; i < k.length; i++) {
                    if (!(k[0] == '@')) {
                        break;
                        //
                    }
                }
                System.out.println(line);

            }
        }

        System.out.println(table);

    }

}
