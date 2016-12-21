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
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

/**
 * @author João Amaral
 * @author Mafalda Rodrigues
 */
public class Indexer {

    Runtime runtime = Runtime.getRuntime();
    int cont = 0;
    int generateID = 0; //para gerar doc ids para postiormente referenciar os docId com os caminhos

    //Para o indexar
    Map<String, Posting> Indexhm = new HashMap<>();

    //DocID map
    HashMap<Integer, String> docMap = new HashMap<>();

    //Sub-Indexs Merge
    static StringBuilder mergeSaveDisc; //Para escrever para o ficheiro
    static List<String> ArrayDeleteTree; //Array para limpar a hahs do termos ja gravados

    //Calculo
    static DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.ENGLISH);
    private static final DecimalFormat df = new DecimalFormat("#.######", otherSymbols);

    public void addIndexHM(ArrayList<String> arFromTokeneizer, int DocId, String fileName) {
        if (!docMap.containsKey(DocId)) {
            docMap.put(DocId, (generateID++) + ":" + fileName);
        }

        for (int i = 0; i < arFromTokeneizer.size(); i++) {
            String key = arFromTokeneizer.get(i);

            if (!Indexhm.containsKey(key)) {
                Indexhm.put(key, new Posting(generateID));
            } else if (Indexhm.containsKey(key) && !Indexhm.get(key).getTermFrequencies().containsKey(generateID)) {
                Indexhm.get(key).addToPosting(generateID);
            } else {
                Indexhm.get(key).updatePosting(generateID);
            }
        }
    }

    public void saveToDisc() throws IOException {
        ArrayList<String> termsFromIndexhm = new ArrayList<>(Indexhm.keySet());
        String auxGroup = "";
        BufferedWriter bwIndexToSave = null;
        BufferedWriter bwIdMap;

        

        File fileIdMap = new File("subindex\\idMap\\" + cont + ".txt");
        fileIdMap.createNewFile();
        FileWriter fw2 = new FileWriter(fileIdMap.getAbsoluteFile());
        bwIdMap = new BufferedWriter(fw2);
        for (Map.Entry<Integer, String> parent : docMap.entrySet()) {
            StringBuilder escritaFichDocMap = new StringBuilder();
            int key = parent.getKey();
            String values = docMap.get(key);
            escritaFichDocMap.append(values.split(":")[0]).append(",").append(key).append(":").append(values.split(":")[1]).append(System.lineSeparator());
            bwIdMap.write(escritaFichDocMap.toString());
        }
        bwIdMap.close();

        //sort array
        Collections.sort(termsFromIndexhm, new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                return s1.compareToIgnoreCase(s2);
            }
        });

        //Save hm com o array como objectivo
        for (int i = 0; i < termsFromIndexhm.size(); i++) {
            String grupo = getGroup(termsFromIndexhm.get(i));
            if (!grupo.equals(auxGroup)) {
                if (bwIndexToSave != null) {
                    bwIndexToSave.close();
                }

                File fileSubIndex = new File("subindex\\" + getGroup(grupo) + "\\" + cont + ".txt");
                fileSubIndex.createNewFile();
                FileWriter fw = new FileWriter(fileSubIndex.getAbsoluteFile());
                bwIndexToSave = new BufferedWriter(fw);
            }

            StringBuilder sbSaveDisc = new StringBuilder();
            sbSaveDisc.append(termsFromIndexhm.get(i));
            for (Map.Entry<Integer, Integer> child : Indexhm.get(termsFromIndexhm.get(i)).getTermFrequencies().entrySet()) {
                Integer cKey = child.getKey();
                float cValue = child.getValue();

                sbSaveDisc.append(",").append(cKey).append(":").append(cValue);
            }
            sbSaveDisc.append(System.lineSeparator());
            bwIndexToSave.write(sbSaveDisc.toString());
            auxGroup = grupo;
        }
        bwIndexToSave.close();
        cont++;
    }

    private static void mapReduce(String fileUrl, String letter) throws FileNotFoundException, IOException {
        List<BufferedReader> armapReduce = new ArrayList<>(); //Array list para armazenar os ficheiros abertos
        String line;
        mergeSaveDisc = null;  //Para criar a string que vai para o ficheiro e para apagar o que foi inserido antes
        BufferedReader brmapReduce;
        BufferedWriter bwmapReduce;

        File folder = new File(fileUrl);
        File[] ListOfFiles = folder.listFiles();
        //Carregar ficheiros para um arraylist
        for (int i = 0; i < ListOfFiles.length; i++) {
            brmapReduce = new BufferedReader(new FileReader(ListOfFiles[i]));
            armapReduce.add(brmapReduce);
        }

        //Criar ficheiro pronto para gravar
        File file = new File("Index\\" + letter + ".txt");
        file.createNewFile();
        FileWriter fw = new FileWriter(file.getAbsoluteFile());
        bwmapReduce = new BufferedWriter(fw);
        TreeMap<String, ArrayList<String>> auxMap = new TreeMap<>();

        while (!armapReduce.isEmpty()) {
            mergeSaveDisc = new StringBuilder();
            ArrayDeleteTree = new ArrayList<>(); //array para limpar a hahs do termos ja gravados
            for (int i = 0; i < armapReduce.size(); i++) { //correr os termos de cada linha de cada ficheiro aberto!
                line = armapReduce.get(i).readLine(); //proxima linha

                if (line == null) {
                    armapReduce.get(i).close();
                    armapReduce.remove(i);
                    i--; //Como o ficheiro em posição i é fechado e removido, decrementa-se no contador i para voltar a ler o documento i na posição do documento que foi retirado
                    continue; //Passa para o proximo iterador do for sem sair do ciclo
                }

                String[] elements = line.split(",", 2);

                if (!auxMap.containsKey(elements[0])) {
                    auxMap.put(elements[0], new ArrayList<>());
                    auxMap.get(elements[0]).add(elements[1]);
                } else {
                    auxMap.get(elements[0]).add(elements[1]);
                }
            }
            /*System.out.println("firstkey: " + auxMap.firstKey());
            System.out.println("firstkey values: " " + auxMap.firstEntry().getValue());*/
            /*for(Map.Entry<String, ArrayList<String>> parent : auxMap.entrySet()){
                String key = parent.getKey();
                
                
                System.out.println("key: " + key + " values: " + auxMap.get(key));
            }   */        //System.out.println(auxMap.firstKey());
            mergeSaveDisc.append(auxMap.firstKey()).append(",").append(auxMap.firstEntry().getValue()).append(System.lineSeparator());
            auxMap.remove(auxMap.firstEntry().getKey());

            bwmapReduce.write(calcWeightTerm(mergeSaveDisc.toString()).replaceAll("\\[|\\]", "").replaceAll(" ", ""));
        }

        for (Map.Entry<String, ArrayList<String>> parent : auxMap.entrySet()) {
            mergeSaveDisc = new StringBuilder();
            String key = parent.getKey();

            mergeSaveDisc.append(key).append(",").append(auxMap.get(key)).append(System.lineSeparator());
            bwmapReduce.write(calcWeightTerm(mergeSaveDisc.toString().replaceAll("\\[|\\]", "").replaceAll(" ", "")));
            ArrayDeleteTree.add(key);
        }
        for (String array3 : ArrayDeleteTree) {
            auxMap.remove(array3);
        }
        ArrayDeleteTree.clear();
        System.gc();
        bwmapReduce.close();
    }

    public String getGroup(String term) {
        for (groups group : groups.values()) {
            if (group.matchesGroup(group, term)) {
                return group.getGroupInitial(group);
            }
        }
        return null;
    }

    public void MergeDocMapping(String folderIndexDocs) throws IOException {
        Map<String, String> auxHmDoc = new HashMap<>();

        BufferedReader brDocid;
        File file2 = new File("Index\\docindex.txt");
        file2.createNewFile();
        FileWriter fw2 = new FileWriter(file2.getAbsoluteFile());
        BufferedWriter bwDocId = new BufferedWriter(fw2);

        File folder = new File(folderIndexDocs);
        File[] ListOfFiles = folder.listFiles();
        for (int i = 0; i < ListOfFiles.length; i++) {
            brDocid = new BufferedReader(new FileReader(ListOfFiles[i]));
            for (String line; (line = brDocid.readLine()) != null;) {
                String[] termInfo = line.split(",");

                auxHmDoc.put(termInfo[0], termInfo[1]);
            }
            brDocid.close();
        }

        for (Map.Entry<String, String> parent : auxHmDoc.entrySet()) {
            StringBuilder sbMergeDocIds = new StringBuilder();
            String docid = parent.getKey();

            sbMergeDocIds.append(docid).append(",").append(auxHmDoc.get(docid)).append(System.lineSeparator());
            bwDocId.write(sbMergeDocIds.toString());
        }

        bwDocId.close();

        auxHmDoc.clear();
        System.gc();
    }

    public void reductionIndex() throws IOException {
        System.out.println("A gravar o docIdMapping:");
        MergeDocMapping("subindex\\idMap");
        for (groups alfabeto : groups.values()) {
            String grupo = groups.getGroupInitial(alfabeto);
            System.out.println("A escrever o index: " + grupo);
            mapReduce("subindex\\" + grupo, grupo);
            System.out.println("Index " + grupo + " escrito!");
            System.out.println("A remover os subindex " + grupo);
            System.out.println("Numero maximo do contador (cont): " + cont);
            
            File dir = new File("subindex\\" + grupo);
            for (File file: dir.listFiles()) {
 
                file.delete();
            }
        }
    }

    public void finalwhipe(int counter) {
        System.out.println("Final Wipe!");
        System.out.println("numero de doc lidos: " + counter);
        Indexhm.clear();
        docMap.clear();
        Runtime.getRuntime().gc();
    }

    public void memoryStore(int counter) throws IOException {
        if (100.0 * (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / Runtime.getRuntime().maxMemory() > 70) {
            System.out.println("A gravar os ficheiros...");
            System.out.println("numero de doc lidos: " + counter);
            saveToDisc();
            System.out.println("Limpar memoria");
            Indexhm.clear();
            docMap.clear();
            Runtime.getRuntime().gc();
            if (100.0 * (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / Runtime.getRuntime().maxMemory() > 20) {
                System.gc();
            }
        }
    }

    public static String calcWeightTerm(String line) {
        float wTotal = 0;
        float sunSquareRoot;
        float calc;

        String[] lineElemnets = line.split(",");

        for (int i = 1; i < lineElemnets.length; i++) {
            String[] valoresLinha = lineElemnets[i].replaceAll("\\[|\\]", "").split(":");
            String tf = valoresLinha[1].replace("\\[|\\]", "");

            wTotal += Math.pow(Float.parseFloat(tf), 2);
        }

        sunSquareRoot = (float) Math.sqrt(wTotal);

        StringBuilder sbCalcs = new StringBuilder();
        sbCalcs.append(lineElemnets[0]);
        for (int i = 1; i < lineElemnets.length; i++) {
            String[] valoresLinha = lineElemnets[i].replaceAll("\\[|\\]", "").split(":");
            calc = (float) ((1 + Math.log(Float.parseFloat(valoresLinha[1]))) / sunSquareRoot);
            String arredondado = df.format(calc);
            sbCalcs.append(",").append(valoresLinha[0]).append(":").append(arredondado);
        }
        sbCalcs.append(System.lineSeparator());
        return sbCalcs.toString();
    }
}
