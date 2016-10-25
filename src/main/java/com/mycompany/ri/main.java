/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.ri;

import java.io.IOException;
import java.util.Scanner;

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
        
        Scanner sc = new Scanner(System.in);
        
        System.out.println("Inserir o caminho para o ficheiro:");
        String insert = sc.nextLine();
        
        cp.AnaliseFile("C:\\Users\\Mafalda Rodrigues\\Desktop\\RIIII\\RI\\src\\main\\java\\com\\mycompany\\ri\\corpus-RI.zip");
       
        dp.DivideLine(dp.getMenDocs2());
        
        to.FromDocProcessor(dp.getMenDocs2());

    }
}