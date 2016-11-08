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

    int tabu;
    
    
    public void mainGenetico(int x, int y, int matriz[][], int cubre[], Pair cubreOrdenado[]) {
        ArrayList<int[]> poblacion= generarPoblacion(x, y, 50, cubreOrdenado, matriz);
        tabu=-1;
        int padre1=torneoBinario(y,poblacion,matriz);
        int padre2=torneoBinario(y,poblacion,matriz);
        
        System.out.println("\nEl padre 1 es el "+padre1);
        System.out.println("El padre 2 es el "+padre2);
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
        do {
            nR = Math.abs(rnd.nextInt() % array.size());
            n = array.remove(nR);
            ++cromo[n];
        } while (!esSolucion(x, y, matriz, cromo));
        eliminaRedundancias(y, x, cromo, cubreOrdenado, matriz);
        esSolucion(x, y, matriz, cromo); //SOBRA
        return cromo;
    }

    //EL TORNEO BINARIO ME DEBE DAR SOLO 1 PADRE
    public int torneoBinario(int y,ArrayList<int[]> poblacion,int mat[][]) {
        Random rnd = new Random();
        int n1 = Math.abs(rnd.nextInt() % poblacion.size());
        System.out.println(n1);
        int n2 = Math.abs(rnd.nextInt() % poblacion.size());
        System.out.println(n2);
        while (n1 == n2 || n1 == tabu || n2 == tabu) {
            n1 = Math.abs(rnd.nextInt() % poblacion.size());
            n2 = Math.abs(rnd.nextInt() % poblacion.size());
        }
        int n=torneoCoste(y,poblacion.get(n1),poblacion.get(n2),mat);
        tabu=n;
        System.out.println("Ha ganado el "+n);
        return (n == 1) ? (n1) : (n2);
    }

    public int torneoCoste(int y, int cromosoma1[], int cromosoma2[], int mat[][]) {
        int coste1 = calculaSolucion(y, cromosoma1, mat);
        System.out.println("n1 tiene coste "+coste1);
        int coste2 = calculaSolucion(y, cromosoma2, mat);
        System.out.println("n2 tiene coste "+coste2);
        return (coste1 <= coste2) ? (1) : (2);
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
