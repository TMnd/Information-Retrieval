/*
    LOGICA!!
    -Abrir todos os ficheiros e inseri-los num arraylist para que se consiga ler
ao mesmo tempo linha por linha.
    1º linha:
    -Lê a fila (de todos ficheiros) para determinar qual é o termo "menor" da linha
    -Uma vez com o "menor" termo identificado, le-se novamente a linha, só que desta
vez guarda-se para ficheiro os termos iguais (ao termo menor) guardando os restantes
para uma treemap (para ficarem ordenados).
    2º linha até ao fim:
    -Lê a fila (de todos ficheiros) para determinar qual é o termo "menor" da linha
    -Verifica se a TreeMap contém termos mais pequenos que o termo "menor":
        -Se houver termos menores entao esses termos são escritos para disco 
removendo a seguir esses termos.
        -Quando atinguir o termo igual ao termo "menor" entao o conteudo desse 
termo é acrescentado ao do ficheiro e escrevendo para disco
    No fim:
    -Quando todos os ficheiros fecharem poderam existir alguns termos na treemap map,
nesse caso escreve-se o que tiver na tree para ficheiro.
*/

package grupori.projectoriultimo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


public class NewMain{
    static Map<String, ArrayList<String>> tm = new TreeMap<>(); //TreeMap para os termos nao escritos imediatamente para ficheiro
    static StringBuilder sbSaveDisc = null; //Para escrever para o ficheiro
    static boolean aux = true; //Para verificar se deve quebrar a linha ou nao!
    static List<String> ar2 = null; //É explicado mais abaixo
    static List<String> ar3 = null; //É explicado mais abaixo

    public static void main(String[] args) throws FileNotFoundException, IOException {
        List<BufferedReader> ar = new ArrayList<>(); //Array list para armazenar os ficheiros abertos
        BufferedReader br = null; 
        String line = null; 
        
        BufferedWriter bw = null;
        
        File folder = new File("C:\\Users\\joaoa\\OneDrive\\Público\\src\\main\\java\\grupori\\projectoriultimo\\index");
        File[] ListOfFiles = folder.listFiles();
        //Carregar ficheiros para um arraylist
        for(int i=0;i<ListOfFiles.length;i++){
            //System.out.println(ListOfFiles[i]); //Le os ficheiros percentes no folder
            br = new BufferedReader(new FileReader(ListOfFiles[i]));
            ar.add(br);
        }
        
        //Criar ficheiro pronto para gravar
        File file = new File("src\\main\\java\\grupori\\projectoriultimo\\temp\\a.txt");
        file.createNewFile();
        FileWriter fw = new FileWriter(file.getAbsoluteFile());
        bw = new BufferedWriter(fw);
        
        while(true){ //Ciclo para estar sempre a correr enquanto haver documentos abertos no array "ar"
            aux = false; 
            ar2 = new ArrayList<>(); //array para inserir os termos por linha por cada sub-index
            ar3 = new ArrayList<>(); //array para limpar a hahs do termos ja gravados

            for(int i=0;i<ar.size();i++){ //correr os termos de cada linha de cada ficheiro aberto!
                line = ar.get(i).readLine(); //proxima linha

                if (line == null){
                    ar.get(i).close();
                    ar.remove(i);
                    i--; //Como o ficheiro em posição i é fechado e removido, decrementa-se no contador i para voltar a ler o documento i na posição do documento que foi retirado
                    continue; //Passa para o proximo iterador do for sem sair do ciclo
                }
                
                ar2.add(line);  //Adcionar o termo para o ar2 que é o arraylist que contem os termos por linha
            }
            if(ar.isEmpty()){ //Pare terminar o ciclo infinito, quando todos os ficheiros forem fechados e removidos do array
                break;
            }
            
            String menor = ar2.get(0).split(",")[0]; // Inicializar o menor
            
            sbSaveDisc = new StringBuilder();  //Para criar a string que vai para o ficheiro e para apagar o que foi inserido antes
            
            //Determinar o termo "mais baixo" da linha em contexto de todos os ficheiro
            for(String ar_2 : ar2){
                String ar_2_aux = ar_2.split(",")[0];
                int resultado = ar_2_aux.compareTo(menor);
                //<0 se a primeira string é menor que a segunda
                //>0 se a primeira string for maior que a segunda
                //=0 se ambas foram no mesmo tamanho
                //Referencia: http://beginnersbook.com/2013/12/java-string-compareto-method-example/
                if(resultado < 0){
                    menor = ar_2; //Velor mais pequeno 
                }
            }   

            //Verificação se UM TERMO MENOR EXISTE na treemap
            ar3.addAll(checkTermSmallThanMenor(menor.split(",")[0]));
            //Limpar treemap!
            if(!ar3.isEmpty()){ //Se o array 3 nao estiver vazio
                for(String ar_3: ar3){
                    tm.remove(ar_3); //Limpa os termos correspondentes aos valores do array 3
                    System.gc(); //Limpar memoria
                }
            }
            
            if(!aux){ //Caso que o termo em no momento nao existir na treemap para iniciar a linha
                sbSaveDisc.append(menor.split(",")[0]);
            }
            
            //Correr os termos de todos os documentos percentes na linha que se encontrar na altura
            for(String ar_2: ar2){ 
                String termo = ar_2.split(",", 2)[0];
                String values = ar_2.split(",", 2)[1];
                
                int resultado = termo.compareTo(menor.split(",")[0]);
               
                if(resultado == 0){ //Se o termo for igual ao valor de "menor" ira fazer append dos values
                    sbSaveDisc.append(",").append(values);
                }else if(resultado > 0){ //Se o termo for igual ao calor de "menor" assim vai para a treemap
                    if(!tm.containsKey(termo)){  //Se o termo nao existir na treemap
                        tm.put(termo, new ArrayList<>());
                        tm.get(termo).add(values);
                    }else{ //Caso o termo existir na treemap (irá acrescentar o value)
                        tm.get(termo).add(values);
                    }
                }
            }
            sbSaveDisc.append(System.lineSeparator()); //Criar a quebra de linha
            bw.write(sbSaveDisc.toString().replaceAll("\\[|\\]", "").replaceAll(" ", "")); //Escreer paro ficheiro
        }
        writeLastTerms(); //Escrever a ultima parte da treemap caso exista termos nao inseridos
        bw.write(sbSaveDisc.toString().replaceAll("\\[|\\]", "").replaceAll(" ", ""));
        bw.close(); //Fechar a escrita
    }
    
    private static ArrayList<String> checkTermSmallThanMenor(String termo){ //Verificação se um termo menor existe na treemap
        ArrayList<String> ar = new ArrayList<String>();

        for (Map.Entry<String, ArrayList<String>> entry : tm.entrySet()) {
            String key = entry.getKey();
            int resultado = key.compareTo(termo);
            if(resultado <0){ //se a key é menor que o termo 
                sbSaveDisc.append(key).append(",").append(tm.get(key)).append(System.lineSeparator());
                ar.add(key);
            }else if(resultado == 0){ // se a key é igual ao termo
                sbSaveDisc.append(key).append(",").append(tm.get(key));
                ar.add(key);
                aux = true; //Neste caso é para impedir que a se escreva o termo na proxima linha.
            }        
        }
        return ar;
    }
    
    private static void writeLastTerms(){
        for(Map.Entry<String, ArrayList<String>> parent : tm.entrySet()){
            String key = parent.getKey();
            
            sbSaveDisc.append(key).append(",").append(tm.get(key)).append(System.lineSeparator());
        }
    }
}
