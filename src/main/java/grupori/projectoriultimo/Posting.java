package grupori.projectoriultimo;

import java.util.HashMap;

/**
 * @author João Amaral
 * @author Mafalda Rodrigues
 */
class Posting {
    private HashMap<Integer, Integer> hm;
    private int docfreq = 0;
    
    public Posting(){
        
    }

    //Construtor criar uma sub map
    public Posting(int docid) { //inicializar variaveis e criar o objecto
        this.hm = new HashMap<>();
        this.hm.put(docid, 1);
        this.docfreq++;
    }
    
    //
    public void addToPosting(int docId) {
        this.hm.put(docId, 1);
        this.docfreq++;
    }

    public void addToPosting(int docId, int termFrequency) {
        this.hm.put(docId, docfreq);
        this.docfreq++;
    }
    
    public void updatePosting(int docId) {
        this.hm.put(docId, this.hm.get(docId) + 1);
    }

    //Para devolver a sub-hashmap!
    public HashMap<Integer, Integer> getTermFrequencies() {
        return this.hm;
    }

}
