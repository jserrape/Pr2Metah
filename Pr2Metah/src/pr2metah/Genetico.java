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

    int tamPoblacion = 50;
    int tabu;
    ArrayList<int[]> poblacion;
    ArrayList<int[]> descendencia;
    int costes[];
    int costesAux[];

    //FUSION
    public void mainGenetico(int x, int y, int matriz[][], int cubre[], Pair cubreOrdenado[]) {
        poblacion = new ArrayList<>();
        
        costes = new int[tamPoblacion];
        
        int hijoFusion[], peorCoste, mejorCoste, pos = 0, pos2 = 0;
        generarPoblacion(x, y, tamPoblacion, cubreOrdenado, matriz);
        generarCostes(y, matriz);
        /////////////////////////////////////////
        //Esto me crea la primera descendencia de 50 pimpollos

        //for (int z = 0; z < 400; z++) {
            Random rand = new Random();
            descendencia = new ArrayList<>();
            costesAux = new int[tamPoblacion];
            peorCoste = 0;
            for (int i = 0; i < tamPoblacion; i++) {
                tabu = -1;
                int padre1 = torneoBinario(y, poblacion, matriz);
                int padre2 = torneoBinario(y, poblacion, matriz);
                if (rand.nextDouble() < 0.69) {
                    hijoFusion = operadorFusion(y, poblacion.get(padre1), poblacion.get(padre2), matriz, padre1, padre2);
                    esSolucionPrint(x, y, matriz, hijoFusion);
                    descendencia.add(hijoFusion);
                    arreglaSolucion(matriz, i, x, y, cubreOrdenado);
                    esSolucionPrint(x, y, matriz, descendencia.get(i));
                } else {
                    if (costes[padre1] < costes[padre2]) {
                        descendencia.add(poblacion.get(padre1));
                    } else {
                        descendencia.add(poblacion.get(padre2));
                    }
                }   
                if (peorCoste < costesAux[i]) { //PARA GUARDAR EL ELITISMO
                    peorCoste = costesAux[i];
                    pos = i;
                    System.out.println("Actualizo el peor a coste " + peorCoste + " de la pos " + pos);
                }
            }
            mejorCoste = 99999999;
            for (int i = 0; i < tamPoblacion; i++) { //BUSCAR LA MEJOR DE LA POBLACION
                if (costes[i] < mejorCoste) {
                    mejorCoste = costes[i];
                    pos2 = i;
                }
            }

            for (int i = 0; i < tamPoblacion; i++) {
                System.out.println(i + ":\tpoblacion=" + costes[i] + "\tdescendientes=" + costesAux[i]);
            }

            System.out.println("El mejor de la poblacion es el nº " + pos2 + " con coste " + mejorCoste);
            System.out.println("El  peor  descendiente  es  el nº " + pos + " con coste " + peorCoste);
            //COPIO EL MEJOR DE LOA POBLACION SOBRE EL PEOR DE LOS DESCENDIENTES

            if (peorCoste > mejorCoste) {
                System.out.println("Intercambio elitismo");
                descendencia.set(pos, poblacion.get(pos2));
                costesAux[pos] = mejorCoste;
            } else {
                System.out.println("POLLAS");
            }

            for (int i = 0; i < tamPoblacion; i++) {
                System.out.println(i + ":\tpoblacion=" + costes[i] + "\tdescendientes=" + costesAux[i]);
            }

            //PASO LOS DESCENDIENTES A POBLACION
            poblacion = (ArrayList<int[]>) descendencia.clone();
            System.arraycopy(costesAux, 0, costes, 0, tamPoblacion);
            System.out.println("Sustituyo a la poblacion anterior");

            for (int i = 0; i < tamPoblacion; i++) {
                System.out.println(i + ":\tpoblacion=" + costes[i] + "\tdescendientes=" + costesAux[i]);
            }
            System.out.println("--------------------------------------");
            System.out.println("--------------------------------------");
            System.out.println("--------------------------------------");
        //}

    }

    public void generarCostes(int y, int mat[][]) {
        for (int i = 0; i < poblacion.size(); i++) {
            costes[i] = calculaSolucion(y, poblacion.get(i), mat);
        }
        for (int i = 0; i < poblacion.size(); i++) {
            //System.out.println(i + ": " + costes[i]);
        }
    }

    public void generarPoblacion(int x, int y, int nPoblacion, Pair cubreOrdenado[], int matriz[][]) {
        for (int i = 0; i < nPoblacion; i++) {
            poblacion.add(generarCromosoma(x, y, cubreOrdenado, matriz));
        }
        System.out.println("La poblacion tiene tamaño: " + poblacion.size() + "\n");
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

    public int torneoBinario(int y, ArrayList<int[]> poblacion, int mat[][]) {
        Random rnd = new Random();
        int n1 = Math.abs(rnd.nextInt() % poblacion.size());
        System.out.println(n1);
        int n2 = Math.abs(rnd.nextInt() % poblacion.size());
        System.out.println(n2);
        while (n1 == n2 || n1 == tabu || n2 == tabu) {
            n1 = Math.abs(rnd.nextInt() % poblacion.size());
            n2 = Math.abs(rnd.nextInt() % poblacion.size());
        }
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        
        System.out.println(n1+"     "+n2+"  <--------------------------------------------------------------------------------------------");
        
         //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        int n = torneoCoste(y, costes[n1], costes[n2], mat);
        tabu = n;
        //System.out.println("Ha ganado el " + n);
        return (n == 1) ? (n1) : (n2);
    }

    public int torneoCoste(int y, int coste1, int coste2, int mat[][]) {
        //System.out.println("n1 tiene coste " + coste1);
        //System.out.println("n2 tiene coste " + coste2);
        return (coste1 <= coste2) ? (1) : (2);
    }

    public int[] operadorFusion(int y, int padre1[], int padre2[], int mat[][], int Npadre1, int Npadre2) {        //SIN ACABAR <---------------------
        int hijo[] = new int[y];
        Random rnd = new Random();
        float aleatorio;
        int costePapi1 = costes[Npadre1];
        int costePapi2 = costes[Npadre2];
        float p = (float) costePapi2 / (costePapi1 + costePapi2);
        System.out.println("p: " + p);
        for (int i = 1; i < y; i++) {
            if (padre1[i] == padre2[i]) {   //Caso de ser iguales
                hijo[i] = padre1[i];
            } else {                      //Caso contrario
                aleatorio = (float) (Math.abs(rnd.nextInt() % 101)) / 100;
                //System.out.println("Aleatorio: "+aleatorio);
                if (aleatorio <= p) {
                    hijo[i] = padre1[i];
                } else {
                    hijo[i] = padre2[i];
                }
            }
        }

        return hijo;
    }

    public void hux(int padre[], int madre[], int hijo1[], int hijo2[], int tam) {
        Random rand = new Random();
        for (int i = 1; i < tam; ++i) {
            if (padre[i] != madre[i]) {
                if (rand.nextDouble() < 0.49) {
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
        System.out.println("-----------------------> si es solucion <-------------------");
        return true;
    }

    public void mostrarCromosoma(int y, int cromo[]) {
        for (int i = 1; i < y; i++) {
            System.out.print(cromo[i] + " ");
        }
        System.out.print("\n");
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

    public void arreglaSolucion(int matriz[][], int pos, int x, int y, Pair pair[]) {
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
                if (matriz[j][k] == 1 && zonas[j] == 0 && descendencia.get(pos)[k] == 1) {
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
            if (descendencia.get(pos)[i] == 0 && vecino[i] == 1) {
                if (i != pos) {
                    descendencia.get(pos)[i] = 1;
                    coste += matriz[0][i];
                }
            }
        }
        coste = eliminaRedundancias(x, y, descendencia.get(pos), pair, matriz, coste);
        costesAux[pos] = coste;
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
        for (int z = 0; z < z - 1; z++) {
            if (solucion[cubreOrdenado[z].getLugar()] == 1) {
                columnaRedundante = true;
                quito = cubreOrdenado[z].getLugar();
                sustituible = false;
                for (i = 1; i < x; i++) {
                    if (matriz[i][quito] == 1) {
                        sustituible = false;
                        for (int j = 1; j < z; j++) {
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
