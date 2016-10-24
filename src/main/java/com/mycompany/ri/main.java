/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.ri;

import java.io.IOException;

/**
 *
 * @author joaoa
 */
public class main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException{
        CorpusReader cp = new CorpusReader();
        DocProcessor dp = new DocProcessor();
        Tokeneizer to = new Tokeneizer();
        Indexer in = new Indexer();
        
        cp.readToMemory();
       
        dp.DivideLine(cp.getMenDocs());
        
        to.FromDocProcessor(dp.getToTokeneizer());

    }
}