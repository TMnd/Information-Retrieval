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
    static StringBuilder mergeSaveDisc = null; //Para escrever para o ficheiro
    static boolean aux = true; //Para verificar se deve quebrar a linha ou nao!
    static List<String> ar2 = null; //É explicado mais abaixo
    static List<String> ar3 = null; //É explicado mais abaixo
    static BufferedReader br = null;
    static BufferedWriter bw = null;


    public static void main(String[] args) throws IOException{
        
        System.out.println("começar: ");
        for (groups group: groups.values()) {
            System.out.println("a escrever: " + group);
            mapReduce("src\\main\\java\\grupori\\projectoriultimo\\index\\" + group.getGroupInitial(group), /*"a");*/group.toString());
            System.out.println("Index " + group + " escrito!");
            //System.out.println("A remover os subindex " + group);
            /*for (int i = 0; i < cont; i++) {
                Files.delete(java.nio.file.Paths.get("src\\main\\java\\grupori\\projectoriultimo\\index\\" + group.getGroupInitial(group) + "\\" + i + ".txt"));
            }*/
        } 
        System.out.println("Acabou!");
    }
    
    private static void mapReduce(String fileUrl, String letra) throws FileNotFoundException, IOException {
        List<BufferedReader> ar = new ArrayList<>(); //Array list para armazenar os ficheiros abertos
        String line = null;
        mergeSaveDisc = null;  //Para criar a string que vai para o ficheiro e para apagar o que foi inserido antes

        //File folder = new File("C:\\Users\\joaoa\\OneDrive\\Público\\src\\main\\java\\grupori\\projectoriultimo\\index");
        File folder = new File(fileUrl);
        File[] ListOfFiles = folder.listFiles();
        //Carregar ficheiros para um arraylist
        for (int i = 0; i < ListOfFiles.length; i++) {
            //System.out.println(ListOfFiles[i]); //Le os ficheiros percentes no folder
            br = new BufferedReader(new FileReader(ListOfFiles[i]));
            ar.add(br);
        }

        //Criar ficheiro pronto para gravar
        File file = new File("src\\main\\java\\grupori\\projectoriultimo\\temp\\" + letra + ".txt");
        file.createNewFile();
        FileWriter fw = new FileWriter(file.getAbsoluteFile());
        bw = new BufferedWriter(fw);


        while (true) { //Ciclo para estar sempre a correr enquanto haver documentos abertos no array "ar"
            String menor = "";
            int cont11 = 0;
            mergeSaveDisc = new StringBuilder();
            aux = false;
            ar2 = new ArrayList<>(); //array para inserir os termos por linha por cada sub-index
            ar3 = new ArrayList<>(); //array para limpar a hahs do termos ja gravados
            for (int i = 0; i < ar.size(); i++) { //correr os termos de cada linha de cada ficheiro aberto!
                line = ar.get(i).readLine(); //proxima linha
              

                if (line == null) {
                    ar.get(i).close();
                    ar.remove(i);
                    i--; //Como o ficheiro em posição i é fechado e removido, decrementa-se no contador i para voltar a ler o documento i na posição do documento que foi retirado
                    continue; //Passa para o proximo iterador do for sem sair do ciclo
                }

                String termo = line.split(",",2)[0];
                String value = line.split(",",2)[1];
                
                if(menor.isEmpty()){
                    //menor = line.split(",")[0];
                    menor = split_banthar(line, ',')[0];
                    mergeSaveDisc.append(menor);
                }
                 
                int result = termo.compareTo(menor);

                if(result == 0){
                    //System.out.println(termo + " é igual que " + menor);
                    mergeSaveDisc.append(",").append(value);
                }else{
                    //System.out.println(termo + " é menor que " + menor);
                    if(!tm.containsKey(termo)){
                        tm.put(termo, new ArrayList<>());
                        tm.get(termo).add(value);
                    }else{
                        tm.get(termo).add(value);
                    }
                }
                
                cont11++;
            }

            if (ar.isEmpty()) { //Pare terminar o ciclo infinito, quando todos os ficheiros forem fechados e removidos do array
                break;
            }
          
            mergeSaveDisc.append(System.lineSeparator()); //Criar a quebra de linha
            
            for(Map.Entry<String, ArrayList<String>> parent : tm.entrySet()){
                String key = parent.getKey();
            
                int resultado = key.compareTo(menor);
                
                if(resultado < 0){
                    mergeSaveDisc.append(key).append(",").append(tm.get(key)).append(System.lineSeparator());
                    ar3.add(key);
                }
                
            }
            //System.out.println("tm size antes: " + tm.size());
            //System.out.println("ar3 antes: " + ar3.size());
            
            for(String ar3: ar3){
                tm.remove(ar3);
            }
            
            //System.out.println("ar3 depois: " + ar3.size());
            
            //System.out.println("tm size depois: " + tm.size());
         
            bw.write(mergeSaveDisc.toString().replaceAll("\\[|\\]", "").replaceAll(" ", "")); //Escreer paro ficheiro
        }
        
        for(Map.Entry<String, ArrayList<String>> parent : tm.entrySet()){
            String key = parent.getKey();
            
            //System.out.println("LAST: " + key + "," + tm.get(key));

            mergeSaveDisc.append(key).append(",").append(tm.get(key)).append(System.lineSeparator());
            ar3.add(key);
        }
        for(String ar3: ar3){
            tm.remove(ar3);
        }
        bw.write(mergeSaveDisc.toString().replaceAll("\\[|\\]", "").replaceAll(" ", "")); //Escreer paro ficheiro
        //System.out.println("tm size final: " + tm.size());
       // writeLastTerms(); //Escrever a ultima parte da treemap caso exista termos nao inseridos
       // bw.write(mergeSaveDisc.toString().replaceAll("\\[|\\]", "").replaceAll(" ", ""));
        bw.close(); //Fechar a escrita
    }
    
    private static void checkTermSmallThanMenor(String termo) throws IOException{ //Verificação se um termo menor existe na treemap
        //ArrayList<String> ar = new ArrayList<String>();
System.out.println("1");
        for (Map.Entry<String, ArrayList<String>> entry : tm.entrySet()) {
            System.out.println("1");
            String key = entry.getKey();
            int resultado = key.compareTo(termo);
            if(resultado < 0){ 
                System.out.println("key: " + key);
                mergeSaveDisc.append(key).append(",").append(tm.get(key)).append(System.lineSeparator());
                bw.write(mergeSaveDisc.toString());
                tm.remove(key);
               // System.out.println("key: " + key + " é menor que :menor:" + menor);
            }else {
                //System.out.println("2222222: " + key + " é menor que :menor:" + menor);
                /*if(resultado == 0){ // se a key é igual ao termo
                mergeSaveDisc.append(key).append(",").append(tm.get(key));
               // ar.add(key);
                aux = true; //Neste caso é para impedir que a se escreva o termo na proxima linha.
                tm.remove(key);*/
                System.out.println("2");
            }     
        }
      //  return ar;
    }
    
    private static void writeLastTerms(){
        for(Map.Entry<String, ArrayList<String>> parent : tm.entrySet()){
            String key = parent.getKey();
            
            mergeSaveDisc.append(key).append(",").append(tm.get(key)).append(System.lineSeparator());
        }
    }
    
    //custom split
    private static String[] split_banthar(String s, char delimeter) {
        int count = 1;

        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == delimeter) {
                count++;
            }
        }
        String[] array = new String[count];

        int a = -1;
        int b = 0;

        for (int i = 0; i < count; i++) {

            while (b < s.length() && s.charAt(b) != delimeter) {
                b++;
            }
            array[i] = s.substring(a + 1, b);
            a = b;
            b++;
        }

        return array;
    }
}
