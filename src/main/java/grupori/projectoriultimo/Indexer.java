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
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Map;
import java.util.Map.Entry;
import grupori.projectoriultimo.groups;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.charset.StandardCharsets;
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
    static Map<String, ArrayList<String>> tm = new TreeMap<>(); //TreeMap para os termos nao escritos imediatamente para ficheiro
    static StringBuilder mergeSaveDisc = null; //Para escrever para o ficheiro
    static boolean aux = true; //Para verificar se deve quebrar a linha ou nao!
    static List<String> ar2 = null; //É explicado mais abaixo
    static List<String> ar3 = null; //É explicado mais abaixo
 
    public void addTM(ArrayList<String> ar, int DocId) {
        int IDfromDoc = 0;
        if(!docMap.containsKey(DocId)){
            cont5++;
            docMap.put(DocId, cont5);
            IDfromDoc = cont5;
        }else{
            IDfromDoc = docMap.get(DocId);
        }
        
       // System.out.println(docMap);
        for (int i = 0; i < ar.size(); i++) {
            String key = ar.get(i);
            
            /*if(!hm.containsKey(key)){
                hm.put(key, new Posting(DocId));
            }else{*/
            if(hm.containsKey(key)){
                hm.get(key).updatePosting(DocId);
            }else if(hm.containsKey(key) && !hm.get(key).getTermFrequencies().containsKey(DocId)){
                hm.get(key).addToPosting(DocId);
            }else{
                hm.put(key, new Posting(DocId));
            }
            
            
          
            /*         
            
            if (!hm.containsKey(key)) {
                hm.put(key, new HashMap<>());
                //hm.get(key).put(DocId, 1.0f);
                hm.get(key).put(IDfromDoc, 1.0f);
            } else if (!hm.get(key).containsKey(IDfromDoc)) {
                //hm.get(key).put(DocId, 1.0f);
                hm.get(key).put(IDfromDoc, 1.0f);
            } else {
                //float frequencia = hm.get(key).get(DocId);
                float frequencia = hm.get(key).get(IDfromDoc);
                frequencia++;
                //hm.get(key).put(DocId, frequencia);
                hm.get(key).put(IDfromDoc,frequencia);
            }*/
            
        }
    }

    public void saveDisc() throws IOException {
        ArrayList<String> termos = new ArrayList<>(hm.keySet());
        String auxGroup = "";
        BufferedWriter bw = null;
        BufferedWriter bw2 = null;
        
        //PARA DESCOMENTAR
       /* File file2 = new File("src\\main\\java\\grupori\\projectoriultimo\\indexs\\_idMap"+ cont + ".txt");
        file2.createNewFile();
        FileWriter fw2 = new FileWriter(file2.getAbsoluteFile());
        bw2 = new BufferedWriter(fw2);
        StringBuilder ola = new StringBuilder();
        for(Map.Entry<Integer, Integer> parent : docMap.entrySet()){
            int key = parent.getKey();
            ola.append(key).append(":").append(docMap.get(key)).append(System.lineSeparator());
        }
        bw2.write(ola.toString());
        bw2.close();*/
        
        //sort array
        Collections.sort(termos, new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                return s1.compareToIgnoreCase(s2);
            }
        });
        
        //System.out.println(termos);
        //Save hm com o array como objectivo
        for (int i=0;i<termos.size();i++) {
            //System.out.println(termos.get(i));
            if(!termos.get(i).isEmpty()){ //Para evitar o maldito termo que é NULL!!! #$%&/
                /*char[] InicialTerm = termos.get(i).toCharArray();
                String iTerm = "" + InicialTerm[0];*/
                String grupo = getGroup(termos.get(i));
                if (!grupo.equals(auxGroup)) {
                    if (bw != null) {
                        bw.close();
                    }
                    File file = new File("src\\main\\java\\grupori\\projectoriultimo\\index\\" + getGroup(grupo) +"\\"+ cont + ".txt");
                    file.createNewFile();
                    FileWriter fw = new FileWriter(file.getAbsoluteFile());
                    bw = new BufferedWriter(fw);
                }

                StringBuilder sbSaveDisc = new StringBuilder();
                sbSaveDisc.append(termos.get(i));
                //for (Map.Entry<Integer, Float> child : hm.get(termos.get(i)).entrySet()) {
                for(Map.Entry<Integer, Float> child : hm.get(i).getTermFrequencies().entrySet()){
                    Integer cKey = child.getKey();
                    float cValue = child.getValue();

                    sbSaveDisc.append(",").append(cKey).append(":").append(cValue);
                }
                sbSaveDisc.append(System.lineSeparator());
                bw.write(sbSaveDisc.toString());
                auxGroup = grupo;
            }
        }
        bw.close();
        cont++;
    }
    
    private void mapReduce(String fileUrl, String letra) throws FileNotFoundException, IOException{
        List<BufferedReader> ar = new ArrayList<>(); //Array list para armazenar os ficheiros abertos
        String line = null; 
        
        BufferedReader br = null;
        BufferedWriter bw = null;
        
        //File folder = new File("C:\\Users\\joaoa\\OneDrive\\Público\\src\\main\\java\\grupori\\projectoriultimo\\index");
        File folder = new File(fileUrl);
        File[] ListOfFiles = folder.listFiles();
        //Carregar ficheiros para um arraylist
        for(int i=0;i<ListOfFiles.length;i++){
            //System.out.println(ListOfFiles[i]); //Le os ficheiros percentes no folder
            br = new BufferedReader(new FileReader(ListOfFiles[i]));
            ar.add(br);
        }
        
        //Criar ficheiro pronto para gravar
        File file = new File("src\\main\\java\\grupori\\projectoriultimo\\temp\\" + letra + ".txt");
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
            
            mergeSaveDisc = new StringBuilder();  //Para criar a string que vai para o ficheiro e para apagar o que foi inserido antes
            
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
                mergeSaveDisc.append(menor.split(",")[0]);
            }
            
            //Correr os termos de todos os documentos percentes na linha que se encontrar na altura
            for(String ar_2: ar2){ 
                String termo = ar_2.split(",", 2)[0];
                String values = ar_2.split(",", 2)[1];
                
                int resultado = termo.compareTo(menor.split(",")[0]);
               
                if(resultado == 0){ //Se o termo for igual ao valor de "menor" ira fazer append dos values
                    mergeSaveDisc.append(",").append(values);
                }else if(resultado > 0){ //Se o termo for igual ao calor de "menor" assim vai para a treemap
                    if(!tm.containsKey(termo)){  //Se o termo nao existir na treemap
                        tm.put(termo, new ArrayList<>());
                        tm.get(termo).add(values);
                    }else{ //Caso o termo existir na treemap (irá acrescentar o value)
                        tm.get(termo).add(values);
                    }
                }
            }
            mergeSaveDisc.append(System.lineSeparator()); //Criar a quebra de linha
            bw.write(mergeSaveDisc.toString().replaceAll("\\[|\\]", "").replaceAll(" ", "")); //Escreer paro ficheiro
        }
        writeLastTerms(); //Escrever a ultima parte da treemap caso exista termos nao inseridos
        bw.write(mergeSaveDisc.toString().replaceAll("\\[|\\]", "").replaceAll(" ", ""));
        bw.close(); //Fechar a escrita
    }
    
    private static ArrayList<String> checkTermSmallThanMenor(String termo){ //Verificação se um termo menor existe na treemap
        ArrayList<String> ar = new ArrayList<String>();

        for (Map.Entry<String, ArrayList<String>> entry : tm.entrySet()) {
            String key = entry.getKey();
            int resultado = key.compareTo(termo);
            if(resultado <0){ //se a key é menor que o termo 
                mergeSaveDisc.append(key).append(",").append(tm.get(key)).append(System.lineSeparator());
                ar.add(key);
            }else if(resultado == 0){ // se a key é igual ao termo
                mergeSaveDisc.append(key).append(",").append(tm.get(key));
                ar.add(key);
                aux = true; //Neste caso é para impedir que a se escreva o termo na proxima linha.
            }        
        }
        return ar;
    }
    
    private static void writeLastTerms(){
        for(Map.Entry<String, ArrayList<String>> parent : tm.entrySet()){
            String key = parent.getKey();
            
            mergeSaveDisc.append(key).append(",").append(tm.get(key)).append(System.lineSeparator());
        }
    }
    
    public String getGroup(String term) {
        for (groups group : groups.values()) {
            if (group.matchesGroup(group, term)) {
                return group.getGroupInitial(group);
            }
        }
        return null;
    }
     
    //PARA APAGAR
    public static TreeMap<String, HashMap<Integer, Float>> carregarFicheiro(String ficheiro) throws IOException {
        TreeMap<String, HashMap<Integer, Float>> tm = new TreeMap<>(); //tree temporaria para carregar o primeiro ficheiro de cada letra
        
        BufferedReader br = Files.newBufferedReader(java.nio.file.Paths.get(ficheiro), StandardCharsets.ISO_8859_1);
        for (String line; (line = br.readLine()) != null;) {
          //  System.out.println("------------------");
            String[] termInfo = line.split(",");
            String term = termInfo[0];
            for(int i=1;i<termInfo.length;i++){
                String[] docInfo = termInfo[i].split(":");
                int docId = Integer.parseInt(docInfo[0]);
                float docFreq = Float.parseFloat(docInfo[1]);
                
                if(!tm.containsKey(term)){
                    tm.put(term,new HashMap<>());
                    tm.get(term).put(docId,docFreq);
                }else if(!tm.get(term).containsKey(docId)){
                    tm.get(term).put(docId,docFreq);
                }else{
                    tm.get(term).put(docId,tm.get(term).get(docId)+docFreq);
                }
            }   
        }
        br.close();
        return tm;
    }
    //PARA APAGAR
    private void fusaoIndex(TreeMap<String, HashMap<Integer, Float>> termMapToMerge) {
        //System.out.println("entrou na fusao de index");
        for(Map.Entry<String, HashMap<Integer, Float>> parent : termMapToMerge.entrySet()){
            String key = parent.getKey();
            System.out.println(key);
            for(Entry<Integer, Float> child : termMapToMerge.get(key).entrySet()){
                int subKey = child.getKey();   
                if(!hm2.containsKey(key)){
                    hm2.put(key, new HashMap<>());
                    hm2.get(key).put(subKey, termMapToMerge.get(key).get(subKey));
                }else if(hm2.get(key).containsKey(subKey)){
                    hm2.get(key).put(subKey, hm2.get(key).get(subKey)+termMapToMerge.get(key).get(subKey));
                }else{
                    hm2.get(key).put(subKey, termMapToMerge.get(key).get(subKey));
                }
            }
        }
    }
    
    //Para adaptar!
    public void reduçãoIndex() throws IOException {
        //System.out.println("entrou no redução index");
        for (groups group : groups.values()) {
           // for(int i=0;i<cont;i++){
                mapReduce("src\\main\\java\\grupori\\projectoriultimo\\indexs\\" + group.getGroupInitial(group),group.toString());
           // }
            System.out.println("Index " + group + " escrito!");
           // System.out.println("a efectuar calculos!");
           // calculos();
            //System.out.println(hm2);
            /*System.out.println("A escrever index final");
            System.out.println(group);
            EscreverGrupos(group,0);
            System.out.println("done");*/
            System.out.println("A remover os subindex " + group);
            for (int i = 0; i < cont; i++) {
                Files.delete(java.nio.file.Paths.get("src\\main\\java\\grupori\\projectoriultimo\\indexs\\" + group.getGroupInitial(group)+ "\\" + i + ".txt"));
            }
        }  
    }
    //Para apagar!!
    private void EscreverGrupos(groups group, int SubIndexcont) { //Em base de multiplos subs-indexs.
        System.out.println("SubIndexcont: "+SubIndexcont);
     
                System.out.println("entrou!");
                System.out.println("1");
                File file = new File("src\\main\\java\\grupori\\projectoriultimo\\indexs\\" + group.getGroupInitial(group) + ".txt");
                //File file = new File("src\\main\\java\\grupori\\projectoriultimo\\indexs\\" + group.getGroupInitial(group) + "_" + SubIndexcont +".txt");
                testesb = new StringBuilder();
        try {
                file.createNewFile();
                FileWriter fw = new FileWriter(file.getAbsoluteFile());
                BufferedWriter bw = new BufferedWriter(fw);
                System.out.println("entrou!3");
                for (Map.Entry<String, HashMap<Integer, Float>> parent : hm2.entrySet()) {
                    String Key = parent.getKey();
                    //String linha = Key;
                    testesb.append(Key);
                    
                    for (Map.Entry<Integer, Float> child : hm2.get(Key).entrySet()) {
                        Integer subKey = child.getKey();
                        float subValue = child.getValue();
                        
                        testesb.append(",").append(subKey).append(":").append(subValue);
                        //linha += "," + subKey + ":" + subValue;
                    }
                    //linha += System.lineSeparator();
                    testesb.append(System.lineSeparator());
                    //bw.write(linha);
                    bw.write(testesb.toString());
                }
                
                System.out.println("entrou!4");
                bw.close();
                
          //  }     
        } catch (IOException e) {
            System.err.println("Erro ao criar o ficheiro do index!");
        }
        hm2.clear();
        System.gc();
    }
    //Para adaptar
    public void calculos(){
        for(Map.Entry<String,HashMap<Integer,Float>> parent: hm2.entrySet()){
            float wTotal = 0;
            float somatorioRaizQuadrada;
            float calculo = 0;
            String key = parent.getKey();
            
            for(Map.Entry<Integer, Float> child: hm2.get(key).entrySet()){
                int subKey = child.getKey();
                
                if(hm2.get(key).get(subKey) != null){
                    wTotal += Math.pow(hm2.get(key).get(subKey),2);
                }
            }
            somatorioRaizQuadrada = (float) Math.sqrt(wTotal);
            for(Map.Entry<Integer, Float> child: hm2.get(key).entrySet()){
                int subKey = child.getKey();
                calculo = (float) ((1 + Math.log(hm2.get(key).get(subKey)))/somatorioRaizQuadrada);
                hm2.get(key).put(subKey,calculo); 
            }
        }

    }
    
    public void finalwhipe(int contas){
        System.out.println("Final Wipe!");
        System.out.println("numero de doc lidos: " + contas);
        hm.clear();
        docMap.clear();
        Runtime.getRuntime().gc();
    }

    public void memory(int contas) throws IOException {
          if ( 100.0 * (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / Runtime.getRuntime().maxMemory() > 70){
            System.out.println("A gravar os ficheiros...");
            System.out.println("numero de doc lidos: " + contas);
            saveDisc();
            System.out.println("Limpar memoria");
            hm.clear();
            docMap.clear();
            Runtime.getRuntime().gc();
            if ( 100.0 * (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / Runtime.getRuntime().maxMemory() > 20){
                System.gc();
            }
        }
    }

    private HashMap<Integer, Posting> Posting() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
