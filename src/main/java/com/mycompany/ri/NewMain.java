/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.ri;

import java.text.NumberFormat;

/**
 *
 * @author joaoa
 */
public class NewMain {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
       Runtime runtime = Runtime.getRuntime();

        NumberFormat format = NumberFormat.getInstance();

        long maxMemory = runtime.maxMemory();
        long allocatedMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();
        
        float teste = Float.parseFloat(format.format((freeMemory + (maxMemory - allocatedMemory)) / 1024));
        float teste2 = (float) (teste - (teste * 0.6));
        
        System.out.println(teste);

        if(teste2 > teste){
           
            System.out.println("e maior ..... total free memory: " + (teste - (teste * 0.6)) + " mg");
        }else{
            
             System.out.println("e menor");
        }
        
    }
    
}
