package grupori.projectoriultimo;

import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author joaoa
 */
public class ProgramaRI {

    public static void main(String[] args) {
        DocProcessor dp = new DocProcessor();
        Tokeneizer tk = new Tokeneizer();
        Seacher sea = new Seacher();
        
        Scanner sc = new Scanner(System.in);
        
        String urlStopwords;
        
        System.out.println("Insira o caminho para o ficheiro ler:");
        String caminhoFicheiroComprimido = "src\\main\\java\\grupori\\projectoriultimo\\teste";//"D:\\OwnCloud\\Documents\\Universidade\\Recuperação de Informação\\ProjectoRIUltimo\\src\\main\\java\\grupori\\projectoriultimo\\teste.zip";//sc.nextLine();
        //F:\Lixo\OneDrive\Público\src\main\java\grupori\projectoriultimo\teste
        
        System.out.println("Quem inserir algum ficheiro de stopwords?");
        //inserir verificações
        String option = sc.nextLine();
        if(option.equals(option)){
            try {
                System.out.println("Insira o ficheiro de stopwords pretendido: (y/n)");
                //urlStopwords = sc.nextLine();
                urlStopwords = "src\\main\\java\\grupori\\projectoriultimo\\stopwords_en.txt";
                tk.LoadStopWords(urlStopwords);
            } catch (IOException ex) {
                System.out.println("Opção invalida");
            }
        }else{
            try {
                System.err.println("A carregar ficheiro pré-definido");
                urlStopwords = "src\\main\\java\\grupori\\projectoriultimo\\stopwords_en.txt";
                tk.LoadStopWords(urlStopwords);
            } catch (IOException ex) {
                System.out.println("Opção invalida");
            }
        }
        
        if (caminhoFicheiroComprimido.endsWith(".zip")) {
            System.out.println("e zip");
            try {
                dp.readPath(caminhoFicheiroComprimido);
                System.out.println("O programa continua");
            } catch (IOException ex) {
                System.out.println("Ficheiro nao detectado");
            }
        }else{
            System.out.println("nao e zip");
            try {
                dp.readPath(caminhoFicheiroComprimido);
                System.out.println("Indexação Completa!");
                System.out.println("Insira o termo que deseja procurar:");
                if(sea.seacher(sc.nextLine())){
                    sea.getMap();
                }
            } catch (IOException ex) {
                System.out.println("Ficheiro nao detctado");
            }
        }
    }
}
