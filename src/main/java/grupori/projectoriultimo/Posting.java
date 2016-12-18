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
    private HashMap<Integer, Float> hm;
    private float docfreq = 0;
    
    public Posting(){
        
    }

    //Construtor criar uma sub map
    public Posting(int docid) { //inicializar variaveis e criar o objecto
        this.hm = new HashMap<>();
        this.hm.put(docid, 1.0F);
        this.docfreq++;
    }
    
    //
    public void addToPosting(int docId) {
        this.hm.put(docId, 1.0F);
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
        HashMap<Integer,Float> tFrequencies = posting.getTermFrequencies();

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
    public HashMap<Integer, Float> getTermFrequencies() {
        return this.hm;
    }

  
    /*
    //Verificar se contem o documento na hashmap
    public boolean containsDoc(int docId) {
        return this.hm.containsKey(docId);
    }
    
    //Caso se exite o termo mas nao o documento
    public void addToPosting(int docId) {
        this.hm.put(docId, 1.0f);
        this.docfreq++;
    }
    
    //actualizar a freq do termo em cada doc
    public void updatePosting(int docId) {
        this.hm.put(docId, this.hm.get(docId)+1);
        
    }
    
    //Ir buscar os valores da hm correspondentes
    public HashMap<Integer, Float> getTermFrequencies() {
        return this.hm;
    }*/
}
