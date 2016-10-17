/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.ri;

/**
 *
 * @author joaoa
 */
public class DocProcessor{
    String data; //todo o documento do buffer.
    int id;
    String texto;
    Tokeneizer toke = new Tokeneizer();
           
    public String bufferReader(String documentoURL){
        return data;
    }
    
     
    public static void checkLanguage(String data){
        
    }
    
    public static void checkCharacterSet(String data){
        
    }
    
    public int getDocID(String data){
        return id;
    }
    
    
    
    public String getTexto(String data){
        return texto;
    }

   
}
