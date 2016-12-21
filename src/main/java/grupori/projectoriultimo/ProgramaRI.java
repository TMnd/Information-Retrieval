package grupori.projectoriultimo;

import java.io.IOException;
import java.util.Scanner;

 /*
 * @author joaoa
 */
public class ProgramaRI {

    public static void main(String[] args) throws IOException {
        DocProcessor dp = new DocProcessor();
        Tokeneizer tk = new Tokeneizer();
        Seacher sea = new Seacher();
        Ranked rnk = new Ranked();
        Indexer dasda = new Indexer();
        
        Scanner sc = new Scanner(System.in);
        
        String urlStopwords;
        
        System.out.println("Insira o caminho para o ficheiro ler:");
        String caminhoFicheiroComprimido = "src\\main\\java\\grupori\\projectoriultimo\\teste";
        
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
        //    try {
             // dp.readPath(caminhoFicheiroComprimido);
                System.out.println("Indexação Completa!");
                System.out.println("Insira os termos para pesqueisar:");
                dasda.reducaoIndex();
               /* Map<Integer, Float> scorehm = new HashMap<>(rnk.calculoScore(sea.seacher(sc.nextLine())));
                 rnk.calculoScore(sea.seacher(sc.nextLine())).toString();
                  int contadorTop5 = 0;
                    System.out.println(rnk.sortByValue(scorehm));
                  for(Map.Entry<Integer, Float> parent : rnk.sortByValue(scorehm).entrySet()){
                      contadorTop5++;
                      if(contadorTop5 > 5){
                          continue;
                      }
                      Integer key = parent.getKey();

                      System.out.println("Key: " + key + " Values: " + parent.getValue());
                  }*/
                  System.out.println("ACABOU!");
            //} catch (IOException ex) {
           //     System.out.println("Ficheiro nao detectado");
            //}
        }
    }
}
