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


import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;


public class NewMain2{
 /*   private static List<SubIndexIterator> iterators;
    
    
    public static void merge () throws IOException {
        //Get iterators
        iterators = loadSubIndexIterators();

        String term;
        int numLinesAppended = 0;
        BufferedWriter bw = new BufferedWriter(new FileWriter("index.txt"));
        PrintWriter printWriter = new PrintWriter(bw);

        StringBuilder sb = new StringBuilder();
        while ((term = getMinimum()) != null) {

            // Store the term
            sb.append(term + "~");

            // Merge the term matches
            getMergedOccurrences(term, sb);

            numLinesAppended++;

            if (numLinesAppended % 5000 == 0) {
                // Print
                printWriter.append(sb);
                sb = new StringBuilder();
            }

        }

        printWriter.append(sb);
        printWriter.close();
    }
    
    private static List<SubIndexIterator> loadSubIndexIterators() throws IOException {

        //Open the current folder and search for files with extension .sidx
        File file = new File(".");
        String [] sidxFiles = file.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.contains(".sidx");
            }
        });

        List<SubIndexIterator> iterators = new ArrayList<>();

        for (String sidxFile: sidxFiles) {
            iterators.add(new SubIndexIterator(sidxFile));
        }

        //Create iterators for each files
        return iterators;
    }

    private static String getMinimum() {
        filterNulls();

        if (iterators.size() == 0) {
            return null;
        }

        Collections.sort(iterators);
        return iterators.get(0).getCurrent().getTerm();
    }
    
    private static void filterNulls () {

        int itrsSize = iterators.size();
        ArrayList<Integer> itrsToRemove = new ArrayList<>();

        for (int i = 0; i < itrsSize; i++) {
            if (iterators.get(i).getCurrent() == null) {
                iterators.set(i, null);
            }
        }

        for (int i = itrsSize - 1; i >=0 ; i--) {
            if (iterators.get(i) == null) {
                iterators.remove(i);
            }
        }
    }
    
    private static void getMergedOccurrences(String term, StringBuilder sb) {
        filterNulls();

        for (int i = 0; i < iterators.size(); i++) {

            if (iterators.get(i).getCurrent().getTerm().equals(term)) {
                sb.append(iterators.get(i).getCurrent().getPostings());
                iterators.get(i).next();
            }

        }

        sb.append("\n");
    }*/
}   
