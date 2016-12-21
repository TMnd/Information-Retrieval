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
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

/**
 *
 * @author joaoa
 */
public class Indexer {

    Runtime runtime = Runtime.getRuntime();
    int cont = 0;
    int gerarID = 0; //para os subindex conjuntos!
    StringBuilder testesb;

    //Para o indexgerarID
    //Map<String, HashMap<Integer, Float>> hm = new HashMap<>();
    Map<String, Posting> hm = new HashMap<>();
    Map<String, HashMap<Integer, Float>> hm2 = new HashMap<>();

    //DocID map
    HashMap<Integer, String> docMap = new HashMap<>();
    /// Caminho - id: docid
    //Sub-Indexs Merge
    static StringBuilder mergeSaveDisc = null; //Para escrever para o ficheiro
    static boolean aux = true; //Para verificar se deve quebrar a linha ou nao!
    static List<String> ar2 = null; //É explicado mais abaixo
    static List<String> ArrayDeleteTree = null; //É explicado mais abaixo
    static BufferedReader br = null;
    static BufferedWriter bw = null;
    
    
    //Calculo
    static DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.ENGLISH);
    private static final DecimalFormat df = new DecimalFormat("#.###", otherSymbols);
    
    public void addTM(ArrayList<String> ar, int DocId, String nomeFicheiro) {
        if (!docMap.containsKey(DocId)) {
            docMap.put(DocId, (gerarID++) + ":" + nomeFicheiro);
        }

        for (int i = 0; i < ar.size(); i++) {
            String key = ar.get(i);

            if (!hm.containsKey(key)) {
                hm.put(key, new Posting(gerarID));
            } else if (hm.containsKey(key) && !hm.get(key).getTermFrequencies().containsKey(gerarID)) {
                hm.get(key).addToPosting(gerarID);
            } else {
                hm.get(key).updatePosting(gerarID);
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
        for (Map.Entry<Integer, String> parent : docMap.entrySet()) {
            StringBuilder escritaFichDocMap = new StringBuilder();
            int key = parent.getKey();
            //System.out.println("key: " + key);
            String values = docMap.get(key);
            //System.out.println("values: " + values);
            escritaFichDocMap.append(values.split(":")[0]).append(",").append(key).append(":").append(values.split(":")[1]).append(System.lineSeparator());
            bw2.write(escritaFichDocMap.toString());
        }
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

        TreeMap<String, ArrayList<String>> tm = new TreeMap<>();
        while (!ar.isEmpty()) {
            mergeSaveDisc = new StringBuilder();
            ArrayDeleteTree = new ArrayList<>(); //array para limpar a hahs do termos ja gravados
            for (int i = 0; i < ar.size(); i++) { //correr os termos de cada linha de cada ficheiro aberto!
                line = ar.get(i).readLine(); //proxima linha

                if (line == null) {
                    ar.get(i).close();
                    ar.remove(i);
                    i--; //Como o ficheiro em posição i é fechado e removido, decrementa-se no contador i para voltar a ler o documento i na posição do documento que foi retirado
                    continue; //Passa para o proximo iterador do for sem sair do ciclo
                }

                String[] Elementos = line.split(",", 2);

                if (!tm.containsKey(Elementos[0])) {
                    tm.put(Elementos[0], new ArrayList<>());
                    tm.get(Elementos[0]).add(Elementos[1]);
                } else {
                    tm.get(Elementos[0]).add(Elementos[1]);
                }
            }

            mergeSaveDisc.append(tm.firstKey()).append(",").append(tm.firstEntry().getValue()).append(System.lineSeparator());
            tm.remove(tm.firstEntry().getKey());

            //bw.write(mergeSaveDisc.toString().replaceAll("\\[|\\]", "")/*.replaceAll(" ", ""))*/); //Escrever paro ficheiro
            bw.write(calculoPesoTermo(mergeSaveDisc.toString()).replaceAll("\\[|\\]", "").replaceAll(" ", ""));
            //cont++;
        }

        for (Map.Entry<String, ArrayList<String>> parent : tm.entrySet()) {
            mergeSaveDisc = new StringBuilder();
            String key = parent.getKey();

            mergeSaveDisc.append(key).append(",").append(tm.get(key)).append(System.lineSeparator());
            //bw.write(mergeSaveDisc.toString().replaceAll("\\[|\\]", ""));
            bw.write(calculoPesoTermo(mergeSaveDisc.toString().replaceAll("\\[|\\]", "").replaceAll(" ", "")));
            ArrayDeleteTree.add(key);
        }
        for (String array3 : ArrayDeleteTree) {
            tm.remove(array3);
        }
        ArrayDeleteTree.clear();
        System.gc();
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

    public void MergeDocMapping(String pastaIndexDocs) throws IOException {
        Map<String, String> mappingHM = new HashMap<>();

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
                String[] termInfo = line.split(",");

                mappingHM.put(termInfo[0], termInfo[1]);
            }
            brDocid.close();
        }

        for (Map.Entry<String, String> parent : mappingHM.entrySet()) {
            StringBuilder sbMergeDocIds = new StringBuilder();
            String docid = parent.getKey();

            sbMergeDocIds.append(docid).append(",").append(mappingHM.get(docid)).append(System.lineSeparator());
            bwDocId.write(sbMergeDocIds.toString());
        }

        bwDocId.close();
        
        mappingHM.clear();
        System.gc();
    }

    public void reducaoIndex() throws IOException {
       // System.out.println("A gravar o docIdMapping:");
       // MergeDocMapping("src\\main\\java\\grupori\\projectoriultimo\\index\\idMap");
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

    public static String calculoPesoTermo(String Linha) {
        
        //ArrayList<String> addCalulo = new ArrayList<>();
        HashMap<String, HashMap<Integer, Float>> tfcalculo = new HashMap<>();
        float wTotal = 0;
        float somatorioRaizQuadrada;
        float calculo = 0;

        String[] elementosLinha = Linha.split(",");
        
        for (int i = 1; i < elementosLinha.length; i++) {
            String[] valoresLinha = elementosLinha[i].replaceAll("\\[|\\]", "").split(":");
           // System.out.println(Arrays.toString(valoresLinha));
            String tf = valoresLinha[1].replace("\\[|\\]", "");

            wTotal += Math.pow(Float.parseFloat(tf), 2);
        }

        somatorioRaizQuadrada = (float) Math.sqrt(wTotal);

        StringBuilder sbCalculos = new StringBuilder();
        sbCalculos.append(elementosLinha[0]);
        for (int i = 1; i < elementosLinha.length; i++) {
            String[] valoresLinha = elementosLinha[i].replaceAll("\\[|\\]", "").split(":");
            calculo = (float) ((1 + Math.log(Float.parseFloat(valoresLinha[1]))) / somatorioRaizQuadrada);
            String arredondado = df.format(calculo);
            sbCalculos.append(",").append(valoresLinha[0]).append(":").append(arredondado);
        }
        sbCalculos.append(System.lineSeparator());
        return sbCalculos.toString();
    }
}
