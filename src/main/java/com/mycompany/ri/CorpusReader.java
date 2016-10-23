/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.ri;

import java.util.HashMap;
import java.util.zip.ZipEntry;

/**
 *
 * @author joaoa
 */
public class CorpusReader {
    String file;
    DocProcessor dp = new DocProcessor();
    
    private HashMap<ZipEntry, HashMap<String, String>> path = new HashMap<>();
    private HashMap<String, HashMap<String, String>> conteudo = new HashMap<>();
    
    public CorpusReader() {
        this.file = file;
    }    

    public String getFile() {
        return file;
    }
    
    public String checkFormat(String file){
        return null;
    }
}
