/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pr2metah;

/**
 *
 * @author Juan Carlos
 */
public class Pair {

    private int lugar;
    private int cubre;

    /**
     * Constructor parametrizado
     *
     * @param lugar
     * @param cubre
     */
    public Pair(int lugar, int cubre) {
        super();
        this.lugar = lugar;
        this.cubre = cubre;
    }

    /**
     * @return Numero de la comisaria
     */
    public int getLugar() {
        return lugar;
    }

    /**
     * Modifica el numero de comisaria
     *
     * @param lugar Numero de comisaria
     */
    public void setLugar(int lugar) {
        this.lugar = lugar;
    }

    /**
     * @return Numero de de elementos que cubre la comisaria
     */
    public int getCubre() {
        return cubre;
    }

    /**
     * Modifica el nmero de de elementos que cubre la comisaria
     *
     * @param cubre Numero de de elementos que cubre la comisaria
     */
    public void setCubre(int cubre) {
        this.cubre = cubre;
    }

    /**
     * Aumenta el numero de elementos que cubre una comisaria
     */
    public void aumentaCubre() {
        ++cubre;
    }
}
