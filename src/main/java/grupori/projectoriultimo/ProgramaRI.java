package grupori.projectoriultimo;

import java.io.IOException;
import java.util.Scanner;

/**
 *
 * @author joaoa
 */
public class ProgramaRI {

    public static void main(String[] args) {
        CorpusReader cr = new CorpusReader();
        DocProcessor dp = new DocProcessor();
        
        Scanner sc = new Scanner(System.in);
        
        System.out.println("Insira o caminho para o ficheiro ler");
        String caminhoFicheiroComprimido = "D:\\OwnCloud\\Documents\\Universidade\\Recuperação de Informação\\ProjectoRIUltimo\\src\\main\\java\\grupori\\projectoriultimo\\teste";//"D:\\OwnCloud\\Documents\\Universidade\\Recuperação de Informação\\ProjectoRIUltimo\\src\\main\\java\\grupori\\projectoriultimo\\teste.zip";//sc.nextLine();
        
        
        if (caminhoFicheiroComprimido.endsWith(".zip")) {
            System.out.println("e zip");
            try {
                dp.readPath(cr.Unzip(caminhoFicheiroComprimido));
                System.out.println("O programa continua");
            } catch (IOException ex) {
                System.out.println("Ficheiro nao detectado");
            }
        }else{
            System.out.println("nao e zip");
            try {
                dp.readPath(caminhoFicheiroComprimido);
                //System.out.println(caminhoFicheiroComprimido);
                System.out.println("O programa continua");
            } catch (IOException ex) {
                System.out.println("Ficheiro nao detctado");
            }
        }
    }
}
