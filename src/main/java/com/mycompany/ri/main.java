package com.mycompany.ri;

import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author joaoa
 */
public class main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args){
        CorpusReader cp = new CorpusReader();
        
        Scanner sc = new Scanner(System.in);
        
        System.out.println("Inserir o caminho para o ficheiro:");
        //String insert = sc.nextLine();
        System.out.println("a ler");
        
        try {
            cp.AnaliseFile("D:\\OwnCloud\\Documents\\Universidade\\Recuperação de Informação\\teste.zip");
            //D:\OwnCloud\Documents\Universidade\Recuperação de Informação\corpus-RI.zip
        } catch (IOException ex) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}