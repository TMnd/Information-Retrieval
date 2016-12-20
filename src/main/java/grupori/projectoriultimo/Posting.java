/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grupori.projectoriultimo;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author joaoa
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
    
    //mergingPosting = caso exista 2 termos iguais no doc
    public Posting mergePosting(Posting posting) {
        HashMap<Integer,Integer> tFrequencies = posting.getTermFrequencies();

        for (Integer docId : tFrequencies.keySet()) {
            if (!this.hm.containsKey(docId)) {
                this.hm.put(docId, tFrequencies.get(docId));
                this.docfreq++;
            } else {
                this.hm.put(docId, this.hm.get(docId) + tFrequencies.get(docId));
            }
        }
        return this;
    }

    //Para devolver a sub-hashmap!
    public HashMap<Integer, Integer> getTermFrequencies() {
        return this.hm;
    }

}
