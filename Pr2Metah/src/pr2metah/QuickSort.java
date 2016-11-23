/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pr2metah;

import java.util.ArrayList;

/**
 *
 * @author Juan Carlos
 */
public class QuickSort {

    private int costes[];
    ArrayList<int[]> poblacion;
    private int length;

    /**
     * Funcion para ordenar de mayor a menor un vector de pair en funcion del
     * elemento cubre
     *
     * @param inputArr Array de costes
     * @param poblacion de cromosomas
     */
    public void sort(int inputArr[], ArrayList<int[]> poblacion) {
        if (inputArr == null || inputArr.length == 0) {
            return;
        }
        this.costes = inputArr;
        this.poblacion = poblacion;
        length = inputArr.length;
        quickSort(0, length - 1);
    }

    private void quickSort(int lowerIndex, int higherIndex) {
        int i = lowerIndex;
        int j = higherIndex;

        int pivot = costes[lowerIndex + (higherIndex - lowerIndex) / 2];

        while (i <= j) {
            while (costes[i] > pivot) {
                ++i;
            }
            while (costes[j] < pivot) {
                --j;
            }
            if (i <= j) {
                exchangeNumbers(i, j);
                ++i;
                --j;
            }
        }

        if (lowerIndex < j) {
            quickSort(lowerIndex, j);
        }
        if (i < higherIndex) {
            quickSort(i, higherIndex);
        }
    }

    private void exchangeNumbers(int i, int j) {
        int temp = costes[i];
        int atemp[] = poblacion.get(i);
        costes[i] = costes[j];
        poblacion.set(i, poblacion.get(j));
        costes[j] = temp;
        poblacion.set(j, atemp);
    }

}
