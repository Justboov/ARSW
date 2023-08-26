/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.threads;

/**
 *
 * @author hcadavid
 */
public class CountThread extends Thread { 
    
    private int rango1;
    private int rango2;
    
    //Constructor que contiene los rangos de números a mostrar.
    public CountThread(int r1, int r2){
        rango1 = r1;
        rango2 = r2;
    }
    
    // Método Run() que determinará el código que se ejecutará en paralelo.
    @Override
    public void run() {
        for (int i = rango1; i <= rango2; i++){
            System.out.println(i);
        }
    }
}
