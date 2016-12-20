/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grupori.projectoriultimo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.Map;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Files;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 *
 * @author joaoa
 */
public class Indexer {

    Runtime runtime = Runtime.getRuntime();
    int cont = 0;
    int cont5 = 0; //para os subindex conjuntos!
    StringBuilder testesb;

    //Para o index
    //Map<String, HashMap<Integer, Float>> hm = new HashMap<>();
    Map<String, Posting> hm = new HashMap<>();
    Map<String, HashMap<Integer, Float>> hm2 = new HashMap<>();

    //DocID map
    Map<Integer, Integer> docMap = new HashMap<>();

    //Sub-Indexs Merge
    static TreeMap<String, ArrayList<String>> tm = new TreeMap<>(); //TreeMap para os termos nao escritos imediatamente para ficheiro
    static StringBuilder mergeSaveDisc = null; //Para escrever para o ficheiro
    static boolean aux = true; //Para verificar se deve quebrar a linha ou nao!
    static List<String> ar2 = null; //É explicado mais abaixo
    static List<String> ar3 = null; //É explicado mais abaixo
    static BufferedReader br = null;
    static BufferedWriter bw = null;

    public void addTM(ArrayList<String> ar, int DocId) {
        int IDfromDoc = 0;
        if (!docMap.containsKey(DocId)) {
            cont5++;
            docMap.put(DocId, cont5);
            IDfromDoc = cont5;
        } else {
            IDfromDoc = docMap.get(DocId);
        }

        for (int i = 0; i < ar.size(); i++) {
            String key = ar.get(i);

            if (!hm.containsKey(key)) {
                hm.put(key, new Posting(DocId));
            } else if (hm.containsKey(key) && !hm.get(key).getTermFrequencies().containsKey(DocId)) {
                hm.get(key).addToPosting(DocId);
            } else {
                hm.get(key).updatePosting(DocId);
            }
        }
    }

    public void saveDisc() throws IOException {
        ArrayList<String> termos = new ArrayList<>(hm.keySet());
        String auxGroup = "";
        BufferedWriter bw = null;
        BufferedWriter bw2 = null;

        File file2 = new File("src\\main\\java\\grupori\\projectoriultimo\\index\\idMap\\" + cont + ".txt");
        file2.createNewFile();
        FileWriter fw2 = new FileWriter(file2.getAbsoluteFile());
        bw2 = new BufferedWriter(fw2);
        StringBuilder ola = new StringBuilder();
        for (Map.Entry<Integer, Integer> parent : docMap.entrySet()) {
            int key = parent.getKey();
            ola.append(key).append(":").append(docMap.get(key)).append(System.lineSeparator());
        }
        bw2.write(ola.toString());
        bw2.close();

        //sort array
        Collections.sort(termos, new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                return s1.compareToIgnoreCase(s2);
            }
        });

        //Save hm com o array como objectivo
        for (int i = 0; i < termos.size(); i++) {
            String grupo = getGroup(termos.get(i));
            if (!grupo.equals(auxGroup)) {
                if (bw != null) {
                    bw.close();
                }
                File file = new File("src\\main\\java\\grupori\\projectoriultimo\\index\\" + getGroup(grupo) + "\\" + cont + ".txt");
                file.createNewFile();
                FileWriter fw = new FileWriter(file.getAbsoluteFile());
                bw = new BufferedWriter(fw);
            }

            StringBuilder sbSaveDisc = new StringBuilder();
            sbSaveDisc.append(termos.get(i));
            for (Map.Entry<Integer, Integer> child : hm.get(termos.get(i)).getTermFrequencies().entrySet()) {
                Integer cKey = child.getKey();
                float cValue = child.getValue();

                sbSaveDisc.append(",").append(cKey).append(":").append(cValue);
            }
            sbSaveDisc.append(System.lineSeparator());
            bw.write(sbSaveDisc.toString());
            auxGroup = grupo;
        }
        bw.close();
        cont++;
    }

    private static void mapReduce(String fileUrl, String letra) throws FileNotFoundException, IOException {
        List<BufferedReader> ar = new ArrayList<>(); //Array list para armazenar os ficheiros abertos
        String line = null;
        mergeSaveDisc = null;  //Para criar a string que vai para o ficheiro e para apagar o que foi inserido antes
       
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
        String referencia = "";
        int cont =0;
        while (true) { //Ciclo para estar sempre a correr enquanto haver documentos abertos no array "ar"
            //String referencia = "";
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
                
                if (i == ar.size() - 1 && cont==0) {
                    referencia = termo;
                    //System.out.println("LAST OF THE LINE: " + referencia);
                    //ultimo da tree é que é a referencia
                }else{    
                    if(termo.compareTo(referencia)<0){
                        referencia = termo;
                    //System.out.println("LAST OF THE LINE: " + referencia);
                    }
                }

                if (!tm.containsKey(termo)) {
                    tm.put(termo, new ArrayList<>());
                    tm.get(termo).add(value);
                } else {
                    //System.out.println("EXISTE!! - " + termo );
                    tm.get(termo).add(value);
                }
                //ar2.add(termo);
                cont++;
            }
            if (ar.isEmpty()) { //Pare terminar o ciclo infinito, quando todos os ficheiros forem fechados e removidos do array
                System.out.println("A SAIR");
                break;
            }

            for(Map.Entry<String, ArrayList<String>> parent : tm.entrySet()){
                String key = parent.getKey();
                //System.out.println("referencia: " + referencia);
                int result = key.compareTo(referencia);
                
                if (result<0) {
                    //System.out.println("dasdasdasdasda");
                    mergeSaveDisc.append(key).append(",").append(tm.get(key)).append(System.lineSeparator());
                    ar3.add(key);
                }
            }

            for (String array3 : ar3) {
                tm.remove(array3);
            }
            
        }
        
            for (Map.Entry<String, ArrayList<String>> parent : tm.entrySet()) {
                String key = parent.getKey();

                mergeSaveDisc.append(key).append(",").append(tm.get(key)).append(System.lineSeparator());
                ar3.add(key);
            }
            for (String array3 : ar3) {
                tm.remove(array3);
            }
        bw.write(mergeSaveDisc.toString().replaceAll("\\[|\\]", "").replaceAll(" ", "")); //Escrever paro ficheiro
        bw.close(); //Fechar a escrita
    }

        
    public String getGroup(String term) {
        for (groups group : groups.values()) {
            if (group.matchesGroup(group, term)) {
                return group.getGroupInitial(group);
            }
        }
        return null;
    }

    public void MergeDodMapping(String pastaIndexDocs) throws IOException {
        Map<String, Integer> mappingHM = new HashMap<>();
        StringBuilder sbMergeDocIds = new StringBuilder();
        BufferedReader brDocid = null;
        BufferedWriter bwDocId;
        File file2 = new File("src\\main\\java\\grupori\\projectoriultimo\\temp\\docindex.txt");
        file2.createNewFile();
        FileWriter fw2 = new FileWriter(file2.getAbsoluteFile());
        bwDocId = new BufferedWriter(fw2);   

        File folder = new File(pastaIndexDocs);
        File[] ListOfFiles = folder.listFiles();
        for (int i = 0; i < ListOfFiles.length; i++) {
            System.out.println(ListOfFiles[i]);
            brDocid = new BufferedReader(new FileReader(ListOfFiles[i]));
            for (String line; (line = brDocid.readLine()) != null;) {
                String[] termInfo = line.split(":");

                mappingHM.put(termInfo[0], Integer.parseInt(termInfo[1]));
            }
            brDocid.close();
        }  
       
        for (Map.Entry<String, Integer> parent : mappingHM.entrySet()) {
            String docid = parent.getKey();

            sbMergeDocIds.append(docid).append(":").append(mappingHM.get(docid)).append(System.lineSeparator());
        }
        bwDocId.write(sbMergeDocIds.toString());
        bwDocId.close();
        
         mappingHM.clear();
        System.gc();
    }

    public void reducaoIndex() throws IOException {
        
        System.out.println("A gravar o docIdMapping:");
        MergeDodMapping("src\\main\\java\\grupori\\projectoriultimo\\index\\idMap");
        for (groups alfabeto : groups.values()) {
            String grupo = groups.getGroupInitial(alfabeto);
            System.out.println("A escrever o index: " + grupo);
            mapReduce("src\\main\\java\\grupori\\projectoriultimo\\index\\" + grupo, grupo);
            System.out.println("Index " + grupo + " escrito!");
            System.out.println("A remover os subindex " + grupo);
            System.out.println("Numero maximo do contador (cont): " + cont);
            /*for (int i = 0; i < cont; i++) {
                Files.delete(java.nio.file.Paths.get("src\\main\\java\\grupori\\projectoriultimo\\index\\" + group.getGroupInitial(group) + "\\" + i + ".txt"));
            }*/
        }
    }

    public void finalwhipe(int contas) {
        System.out.println("Final Wipe!");
        System.out.println("numero de doc lidos: " + contas);
        hm.clear();
        docMap.clear();
        Runtime.getRuntime().gc();
    }

    public void memory(int contas) throws IOException {
        if (100.0 * (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / Runtime.getRuntime().maxMemory() > 70) {
            System.out.println("A gravar os ficheiros...");
            System.out.println("numero de doc lidos: " + contas);
            saveDisc();
            System.out.println("Limpar memoria");
            hm.clear();
            docMap.clear();
            Runtime.getRuntime().gc();
            if (100.0 * (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / Runtime.getRuntime().maxMemory() > 20) {
                System.gc();
            }
        }
    }

    public static String[] split_banthar(String s, char delimeter) {
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
