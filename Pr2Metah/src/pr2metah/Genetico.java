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
        
        operadorFusion(y,poblacion.get(padre1),poblacion.get(padre2),matriz);
    }

    public ArrayList<int[]> generarPoblacion(int x, int y, int nPoblacion, Pair cubreOrdenado[], int matriz[][]) {
        ArrayList<int[]> poblacion = new ArrayList<>();
        for (int i = 0; i < nPoblacion; i++) {
            poblacion.add(generarCromosoma(x, y, cubreOrdenado, matriz));
        }
        System.out.println("La poblacion tiene tamaño: " + poblacion.size()+"\n");
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

    public int[] operadorFusion(int y, int padre1[], int padre2[], int mat[][]){        //SIN ACABAR <---------------------
        int hijo[]=new int[y];
        Random rnd = new Random();
        float aleatorio;
        int costePapi1 = calculaSolucion(y, padre1, mat);
        int costePapi2 = calculaSolucion(y, padre2, mat);
        float p= (float) costePapi2 / (costePapi1+costePapi2);
        System.out.println("p: "+p);
        for(int i=1;i<y;i++){
            if(padre1[i]==padre2[i]){   //Caso de ser iguales
                hijo[i]=padre1[i];
            }else{                      //Caso contrario
                aleatorio= (float) (Math.abs(rnd.nextInt() % 101))/100;
                //System.out.println("Aleatorio: "+aleatorio);
                if(aleatorio<=p){
                    hijo[i]=padre1[i];
                }else{
                    hijo[i]=padre2[i];
                }
            }
        }

        return hijo;
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
    
    public void hux(int padre[], int madre[], int hijo1[], int hijo2[], int tam) {
        Random rand = new Random();
        for (int i = 1; i < tam; ++i){
            if (padre[i] != madre [i]) {
                if(rand.nextDouble() < 0.5) {
                    hijo1[i] = padre[i];
                    hijo2[i] = madre[i];
                } else {
                    hijo1[i] = madre[i];
                    hijo2[i] = padre[i];
                }
            } else {
                hijo1[i] = hijo2[i] = padre[i];
            }
        }
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
    
    public void arreglaSolucion(ArrayList<int[]> poblacion, int matriz[][], int costes[], int pos, int x, int y, Pair pair[]) {


        //Se genera un vector con todos los candidatos que cubren alguna zona de las que me quedan por cubrir al eliminar esa ( sin incluirla )
        int vecino[] = new int[y];
        int zonas[] = new int[x];
        int zonasPendientes = 0;
        int coste = costes[pos];

        for (int i = 1; i < y; i++) {
            vecino[i] = 0;
            if (i < x) {
                zonas[i] = 0;
            }
        }

        //Se rellena el vector de zonas, las posiciones que quedan con 0, son las que faltan por cubrir
        for (int k = 1; k < y; k++) {
            for (int j = 1; j < x; j++) {
                if (matriz[j][k] == 1 && zonas[j] == 0 && poblacion.get(pos)[k] == 1) {
                    zonas[j] = 1;
                    ++zonasPendientes;
                }
            }
        }
        zonasPendientes = x - zonasPendientes - 1;
        for (int k = 1; (k < y && zonasPendientes > 0); k++) {
            for (int j = 1; j < x; j++) {
                if (k != pos) {
                    if ((matriz[j][k] == 1) && (zonas[j] == 0)) { //La zona esta sin cubrir
                        vecino[k] = 1;
                        zonas[j] = 1;
                        --zonasPendientes;
                    }
                }
            }
        }

        for (int i = 1; i < y; i++) {
            if (poblacion.get(pos)[i] == 0 && vecino[i] == 1) {
                if (i != pos) {
                    poblacion.get(pos)[i] = 1;
                    coste += matriz[0][i];
                }
            }
        }
        coste = eliminaRedundancias(x, y, poblacion.get(pos), pair, matriz, coste);
        costes[pos] = coste;
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
    
    public int eliminaRedundancias(int x, int y, int solucion[], Pair cubreOrdenado[], int matriz[][], int coste) {
        int factorizacion = coste;
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
                    solucion[quito] = 0;
                    factorizacion = factorizacion - matriz[0][quito];
                }
            }
        }
        return factorizacion;
    }

}
