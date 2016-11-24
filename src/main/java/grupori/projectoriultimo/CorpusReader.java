/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grupori.projectoriultimo;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.core.ZipFile;

/**
 *
 * @author joaoa
 */
public class CorpusReader {  
    public String Unzip(String SourceZip){
        String detination = SourceZip.substring(0, SourceZip.lastIndexOf("."));
        try {    
            ZipFile zipFile = new ZipFile(SourceZip);
            zipFile.extractAll(detination);
        } catch (ZipException ex) {
            System.err.println("Falha na descompactação!");
        }
        return detination;
    }
}
