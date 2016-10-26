package com.mycompany.ri;

import java.io.IOException;

/**
 * @author  João Amaral
 * @author  Mafalda Rofrigues
 */

public class CorpusReader {
    /**
     * Esta função é chamada no main que tem como paramentro o caminho para o 
     * fiheiro que o utilizador terá de inserir.
     * Esta função serve para analisar a extansão do ficheiro com o objectivo
     * de aplicar a acção correspondente.
     * 
     * @param file
     * @throws java.io.IOException
     * 
     */
    public void AnaliseFile(String file) throws IOException {
        DocProcessor dc = new DocProcessor();

        //Verifica se a extensão do ficheiro é um ficheiro zip
        if (file.endsWith(".zip")) {  
           // System.out.println("CorpusReader: ENTROU NA FUNÇÃO ZIP");

            //Insere o ficheiro "file" na Classe DocProcessor
            dc.readFileZip(file); 
        } else {
            //Caso o ficheiro "file" nao seja um ficheiro zip o programa fechou
           // System.out.println("CorpusReader: NÃO ENTROU NA FUNÇÃO ZIP");
            System.exit(0);
        }
    }
}
