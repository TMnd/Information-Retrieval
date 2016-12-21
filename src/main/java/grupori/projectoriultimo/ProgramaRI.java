package grupori.projectoriultimo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author João Amaral / Mafalda Rodrigues
 */
public class ProgramaRI {

    public static void main(String[] args) throws IOException {
        DocProcessor dp = new DocProcessor();
        Tokeneizer tk = new Tokeneizer();
        Seacher sea = new Seacher();
        Ranked rnk = new Ranked();
        Scanner sc = new Scanner(System.in);

        String urlStopwords;

        //Criar paginas na source do projecto
        File checkFilesubIndexFolder = new File("subindex");
        if (!checkFilesubIndexFolder.exists()) {
            checkFilesubIndexFolder.mkdir();
        }
        File checkIndexFolder = new File("Index");
        if (!checkIndexFolder.exists()) {
            checkIndexFolder.mkdir();
        }
        File checkFileidMapFolder = new File("subindex\\idmap");
        if (!checkFileidMapFolder.exists()) {
            checkFileidMapFolder.mkdir();
        }
        for (groups grupo : groups.values()) {
            File checkGroupFolder = new File("subindex\\" + groups.getGroupInitial(grupo));
            if (!checkGroupFolder.exists()) {
                System.out.println(checkGroupFolder.toString());
                checkGroupFolder.mkdir();
            }
        }
        String optionFirst = "", option = "", optionStemmer = "";
        boolean stemmerCheck = false;
        //File file = null;
        while (!(optionFirst.equals("1") || optionFirst.equals("2"))) {
            System.out.println("\n1. Indexar");
            System.out.println("2. Searcher");
            System.out.println("3. Exit");
            System.out.print("\nChoose an option: ");
            optionFirst = sc.next();

            if (optionFirst.equals("1")) {
                System.out.println("Insira o caminho para o ficheiro ler:");
                String folderPath = "src\\main\\java\\grupori\\projectoriultimo\\teste";

                while (!(option.equals("yes") || option.equals("no"))) {
                    System.out.println("Quem inserir algum ficheiro de stopwords?(yes\\no)");
                    option = sc.next();

                }
                if (option.equals("yes")) {
                    String collectionPath = "";
                    File file = null;
                    while (file == null || !file.exists()) {
                        System.out.print("Enter the collection path: ");
                        collectionPath = sc.next();
                        file = new File(collectionPath);
                        if (!file.exists()) {
                            System.err.println("Error reading the file/collection!");
                        }
                    }

                }
                if (option.equals("no")) {
                    System.out.println("A utilizar o ficheiro default: stopwords_en.txt");
                    urlStopwords = "src\\main\\java\\grupori\\projectoriultimo\\stopwords_en.txt";
                    tk.LoadStopWords(urlStopwords);
                }
                while (!(optionStemmer.equals("yes") || optionStemmer.equals("no"))) {
                    System.out.println("Deseja utilizar o Stemmer?(yes\\no)");
                    optionStemmer = sc.next();

                }
                if (optionStemmer.equals("yes")) {
                    stemmerCheck = true;
                }
                dp.readPath(folderPath, stemmerCheck);

            } else if (optionFirst.equals("2")) {
                File checkFile = new File("Index");
                if (checkFile.isDirectory() && checkFile.list().length == 0) {
                    System.out.println("Index nao existente.");
                } else {
                    System.out.println("Insira os termos para pesqueisar:");
                    ArrayList<String> resultados = new ArrayList<>(rnk.score(rnk.calcScore(sea.seacher(sc.nextLine()))));
                    for (String results : resultados) {
                        System.out.println(results);
                    }
                    System.out.println("Sair, acabou o programa");
                }

            } else {
                //(optionFirst.equals("3")) {
                System.out.println("Sair");
                break;
            }
        }
    }
}
