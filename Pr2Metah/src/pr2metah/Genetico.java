/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pr2metah;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Xenahort
 */
public class Genetico {

    
    
    public void mainGenetico(int x, int y, int matriz[][], int cubre[], Pair cubreOrdenado[]) {
        generarPoblacion(x, y, 50, cubreOrdenado, matriz);
    }

    
    
    public ArrayList<int[]> generarPoblacion(int x, int y, int nPoblacion, Pair cubreOrdenado[], int matriz[][]) {
        ArrayList<int[]> poblacion = new ArrayList<>();
        for (int i = 0; i < nPoblacion; i++) {
            poblacion.add(generarCromosoma(x, y, cubreOrdenado, matriz));
        }
        System.out.println("La poblacion tiene tamaño: " + poblacion.size());
        return poblacion;
    }

    
    
    public int[] generarCromosoma(int x, int y, Pair cubreOrdenado[], int matriz[][]) {
        int cromo[] = new int[y];
        Random rnd = new Random();
        int nR, n;
        for (int i = 1; i < y; i++) {
            cromo[i] = 0;
        }
        ArrayList<Integer> array = new ArrayList<>(); //DEBERIA SACAR ESTO FUERA
        for (int i = 1; i < y; i++) {
            array.add(i);
        }
        while (!esSolucion(x, y, matriz, cromo)) {
            nR = (int) (rnd.nextDouble() * array.size());
            n = array.remove(nR);
            ++cromo[n];
        }
        eliminaRedundancias(y, x, cromo, cubreOrdenado, matriz);
        esSolucion(x, y, matriz, cromo); //SOBRA
        return cromo;
    }

    
    
    public int torneoBinario(int y, int cromosoma1[], int cromosoma2[], int mat[][]) {
        int coste1 = calculaSolucion(y, cromosoma1, mat);
        int coste2 = calculaSolucion(y, cromosoma2, mat);
        if (coste1 > coste2) {
            return 1;
        } else {
            return 2;
        }
    }

    
    
    public boolean esSolucion(int x, int y, int matriz[][], int solucion[]) {
        boolean ok;
        for (int i = 1; i < x; i++) {
            ok = false;
            for (int j = 1; j < y; j++) {
                if (solucion[j] == 1) {
                    if (matriz[i][j] == 1) {
                        j = y;
                        ok = true;
                    }
                }
            }
            if (!ok) {
                return false;
            }
        }
        return true;
    }

    
    
    public int calculaSolucion(int y, int solucion[], int mat[][]) {
        int coste = 0;
        for (int i = 1; i < y; i++) {
            if (solucion[i] == 1) {
                coste += mat[0][i];
            }
        }
        return coste;
    }

    
    
    public boolean esSolucionPrint(int x, int y, int matriz[][], int solucion[]) {
        boolean ok;
        for (int i = 1; i < x; i++) {
            ok = false;
            for (int j = 1; j < y; j++) {
                if (solucion[j] == 1) {
                    if (matriz[i][j] == 1) {
                        j = y;
                        ok = true;
                    }
                }
            }
            if (!ok) {
                System.out.println("-----------------------> NO ES SOLUCION <-------------------");
                return false;
            }
        }
        System.out.println("SI ES SOLUCION");
        return true;
    }

    
    
    public void mostrarCromosoma(int y, int cromo[]) {
        for (int i = 1; i < y; i++) {
            System.out.print(cromo[i] + " ");
        }
        System.out.print("\n");
    }

    
    
    public void eliminaRedundancias(int x, int y, int solucion[], Pair cubreOrdenado[], int matriz[][]) {
        int quito;
        int i;
        boolean columnaRedundante, sustituible;
        for (int z = 0; z < x - 1; z++) {
            if (solucion[cubreOrdenado[z].getLugar()] == 1) {
                columnaRedundante = true;
                quito = cubreOrdenado[z].getLugar();
                sustituible = false;
                for (i = 1; i < y; i++) {
                    if (matriz[i][quito] == 1) {
                        sustituible = false;
                        for (int j = 1; j < x; j++) {
                            if (matriz[i][j] == 1 && solucion[j] == 1 && quito != j) {
                                sustituible = true;
                            }
                        }
                        if (!sustituible) {
                            columnaRedundante = false;
                        }
                    }
                }
                if (columnaRedundante) {
                    //System.out.println("REDUNDANTEE");
                    solucion[quito] = 0;
                }
            }
        }
    }
    
    
    
}
