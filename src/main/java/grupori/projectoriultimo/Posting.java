/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grupori.projectoriultimo;

import java.util.HashMap;
import java.util.TreeMap;

/**
 *
 * @author joaoa
 */
class Posting {
    private HashMap<Integer, Float> hm;
    private float docfreq = 0;
    
    //Para criar uma nova sub-hashmap
    public Posting(int docID){
        this.hm = new HashMap<>();
        this.hm.put(docID, 1.0f);
        this.docfreq++;
    }
    
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
    }
}
