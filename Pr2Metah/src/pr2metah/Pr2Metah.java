/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pr2metah;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author alumno
 */
public class Pr2Metah {

    static int cubre[];
    static int matriz[][];
    static int y, x;

    public static final int SEMILLA1 = 77383426;
    public static final int SEMILLA2 = 77368737;
    public static final int SEMILLA3 = 34267738;
    public static final int SEMILLA4 = 87377736;
    public static final int SEMILLA5 = 12482498;

    /**
     * Funcion para leer n fichero
     *
     * @param fich Ruta del fichero a leer
     * @throws FicheroNoEncontrado Excepcion en caso de no encontrar el fichero
     * @throws java.io.FileNotFoundException
     */
    public static void leerFichero(String fich) throws FicheroNoEncontrado, FileNotFoundException, IOException {
        if (!(new File(fich)).exists()) {
            throw new FicheroNoEncontrado("Fichero no encontrado \n");
        }
        File archivo;
        FileReader fr = null;
        BufferedReader br;
        try {
            archivo = new File(fich);
            fr = new FileReader(archivo);
            br = new BufferedReader(fr);
            String texto;
            String[] datos;
            texto = br.readLine();
            datos = texto.split(" ");
            x = Integer.parseInt(datos[1]) + 1;
            y = Integer.parseInt(datos[2]) + 1;

            matriz = new int[x][y];
            cubre = new int[y];

            for (int i = 0; i < y; i++) {
                cubre[i] = 0;
            }
            for (int i = 1; i < x; i++) {
                for (int j = 0; j < y; j++) {
                    matriz[i][j] = 0;
                }
            }
            matriz[0][0] = 0;
            int comisariasV = 1;
            while (y != comisariasV) {
                texto = br.readLine();
                datos = texto.split(" ");
                for (int i = 1; i < datos.length; i++) {
                    matriz[0][comisariasV] = Integer.parseInt(datos[i]);
                    ++comisariasV;
                }
            }
            int cont;
            for (int i = 1; i < x; i++) {
                texto = br.readLine();
                datos = texto.split(" ");
                cont = Integer.parseInt(datos[1]);
                while (cont != 0) {
                    texto = br.readLine();
                    datos = texto.split(" ");
                    for (int j = 1; j < datos.length; j++) {
                        matriz[i][Integer.parseInt(datos[j])] = 1;
                        ++cubre[Integer.parseInt(datos[j])];
                        --cont;
                    }
                }
            }
        } catch (IOException | NumberFormatException e) {
        } finally {
            try {
                if (null != fr) {
                    fr.close();
                }
            } catch (Exception e2) {
            }
        }
    }

    public static boolean esSolucion(int solucion[]) {
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
                //System.out.println("-----------------------> NO ES SOLUCION <-------------------");
                return false;
            }
        }
        //System.out.println("SI ES SOLUCION");
        return true;
    }

    public static void mostrarCromosoma(int cromo[]) {
        System.out.print("\n");
        for (int i = 1; i < y; i++) {
            System.out.print(cromo[i] + " ");
        }
        System.out.print("\n");
    }

    public static int[] generarCromosoma() {
        int cromo[] = new int[y];
        Random rnd = new Random();
        int nR, n;
        for (int i = 1; i < y; i++) {
            cromo[i] = 0;
        }
        ArrayList<Integer> array = new ArrayList<>();
        for (int i = 1; i < y; i++) {
            array.add(i);
        }

        while (!esSolucion(cromo)) {
            nR = (int) (rnd.nextDouble() * array.size());
            n = array.remove(nR);
            ++cromo[n];
        }
        return cromo;
    }

    public static void mostrarMatriz() {
        for (int i = 1; i < x; i++) {
            for (int j = 1; j < y; j++) {
                System.out.print(matriz[i][j] + " ");
            }
            System.out.println();
        }
    }

    /**
     * @param args the command line arguments
     * @throws pr2metah.FicheroNoEncontrado
     * @throws java.lang.InterruptedException
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws FicheroNoEncontrado, InterruptedException, IOException {
        String ficheros[] = {"scpe1.txt", "scp41.txt", "scpd1.txt", "scpnrf1.txt", "scpa1.txt"};
        int n = 5;
        leerFichero(ficheros[0]);
        generarCromosoma();
    }

}
