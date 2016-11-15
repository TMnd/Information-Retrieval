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
        
        
        Scanner sc = new Scanner(System.in);
        
        System.out.println("Inserir o caminho para o ficheiro:");
       // String insert = sc.nextLine();
        System.out.println("a ler");
        
        cp.AnaliseFile("D:\\OwnCloud\\Documents\\Universidade\\Recuperação de Informação\\corpus-RI.zip");
        //D:\OwnCloud\Documents\Universidade\Recuperação de Informação\corpus-RI.zip
    }
}