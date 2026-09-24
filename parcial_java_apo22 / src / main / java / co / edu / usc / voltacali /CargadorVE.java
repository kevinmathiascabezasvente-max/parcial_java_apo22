package co.edu.usc.voltacali;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class CargadorVE {
    
    // ========== PARTE A: ATRIBUTOS ENCAPSULADOS ==========
    private String fabricante;
    private int anioInstalacion;
    private double voltajeNominal;
    private TipoConector tipoConector;
    private TipoCargador tipoCargador;
    private int numeroConectores;
    private int puestosParqueo;
    private double potenciaMaxima;
    private Ubicacion ubicacion;
    private double potenciaActual;
    
    // ========== PARTE D: ENUMS Y CLASE INTERNA ==========
    public enum TipoConector {
        TIPO_1, TIPO_2, CCS2, CHADEMO, GBT
    }
    
    public enum TipoCargador {
        MURAL, PEDESTAL, RAPIDO_DC, BIDIRECCIONAL_V2G
    }
    
    public enum Ubicacion {
        RESIDENCIAL, PARQUEADERO_PUBLICO, CENTRO_COMERCIAL,
        HOTEL, TERMINAL, FLOTA_CORPORATIVA, UNIVERSIDAD
    }
    
    private static class RegistroSesion {
        private String evento;
        private boolean valido;
        
        public RegistroSesion(String evento, boolean valido) {
            this.evento = evento;
            this.valido = valido;
        }
        
        @Override
        public String toString() {
            return evento + " → " + (valido ? "OK" : "RECHAZADO");
        }
    }
    
    // ========== PARTE D: ATRIBUTOS ESTÁTICOS ==========
    private static Vector<CargadorVE> flota = new Vector<>();
    private static List<RegistroSesion> bitacora = new ArrayList<>();
    private static int limiteRed = 50;
    private static int totalCargadores = 0;
    
    // ========== PARTE C: SOBRECARGA DE CONSTRUCTORES ==========
    // Constructor 1: por defecto
    public CargadorVE() {
        this("", 0, 0.0, null, null, 0, 0, 0.0, null);
        potenciaActual = 0.0;
        totalCargadores++;
        flota.add(this);
        registrarSesion("Constructor por defecto", true);
    }
    
    // Constructor 2: fabricante, tipoConector, potenciaMaxima, ubicacion
    public CargadorVE(String fabricante, TipoConector tc, double pm, Ubicacion u) {
        this(fabricante, 2022, 220.0, tc, null, 1, 1, pm, u);
        potenciaActual = 0.0;
        totalCargadores++;
        flota.add(this);
        registrarSesion("Constructor básico", true);
    }
    
    // Constructor 3: completo con incremento
    public CargadorVE(String fab, int anio, double volt, TipoConector tc,
                      TipoCargador tipC, int numCon, int puestos,
                      double pm, Ubicacion ubi) {
        this.fabricante = fab;
        this.anioInstalacion = anio;
        this.voltajeNominal = volt;
        this.tipoConector = tc;
        this.tipoCargador = tipC;
        this.numeroConectores = numCon;
        this.puestosParqueo = puestos;
        this.potenciaMaxima = pm;
        this.ubicacion = ubi;
        this.potenciaActual = 0.0;
        totalCargadores++;
        flota.add(this);
        registrarSesion("Constructor completo", true);
    }
    
    // Constructor 4: copia
    public CargadorVE(CargadorVE otro) {
        this.fabricante = otro.fabricante;
        this.anioInstalacion = otro.anioInstalacion;
        this.voltajeNominal = otro.voltajeNominal;
        this.tipoConector = otro.tipoConector;
        this.tipoCargador = otro.tipoCargador;
        this.numeroConectores = otro.numeroConectores;
        this.puestosParqueo = otro.puestosParqueo;
        this.potenciaMaxima = otro.potenciaMaxima;
        this.ubicacion = otro.ubicacion;
        this.potenciaActual = 0.0;
        totalCargadores++;
        flota.add(this);
        registrarSesion("Constructor copia", true);
    }
    
    // ========== PARTE B: COMPORTAMIENTO ==========
    public boolean aumentarPotencia(double cantidad) {
        if (cantidad < 0) {
            System.out.println("❌ Valor negativo no permitido");
            registrarSesion("Aumentar potencia: valor negativo", false);
            return false;
        }
        double nuevaPotencia = potenciaActual + cantidad;
        if (nuevaPotencia > potenciaMaxima) {
            System.out.println("Supera potencia máxima (" + potenciaMaxima + " kW)");
            registrarSesion("Aumentar potencia: supera máximo", false);
            return false;
        }
        if (getPotenciaTotalFlota() + cantidad > limiteRed) {
            System.out.println("Supera límite de red (" + limiteRed + " kW)");
            registrarSesion("Aumentar potencia: supera límite red", false);
            return false;
        }
        potenciaActual = nuevaPotencia;
        registrarSesion("Aumentar potencia: +" + cantidad + " kW", true);
        return true;
    }
    
    public boolean reducirPotencia(double cantidad) {
        if (cantidad < 0) {
            System.out.println("Valor negativo no permitido");
            registrarSesion("Reducir potencia: valor negativo", false);
            return false;
        }
        if (potenciaActual - cantidad < 0) {
            System.out.println("No se puede bajar de 0");
            registrarSesion("Reducir potencia: menor a cero", false);
            potenciaActual = 0;
            return false;
        }
        potenciaActual -= cantidad;
        registrarSesion("Reducir potencia: -" + cantidad + " kW", true);
        return true;
    }
    
    public int tiempoEstimadoCarga(double energiaKWh) {
        if (potenciaActual == 0) {
            System.out.println("Potencia en 0, no se puede calcular");
            registrarSesion("Calcular tiempo: potencia cero", false);
            return -1;
        }
        double horas = energiaKWh / potenciaActual;
        return (int) Math.round(horas * 60); // minutos
    }
    
    public void mostrar() {
        System.out.println("══════════════════════════════════");
        System.out.println("Fabricante:     " + fabricante);
        System.out.println("Año instalación:" + anioInstalacion);
        System.out.println("Voltaje:        " + voltajeNominal + " V");
        System.out.println("Conector:       " + tipoConector);
        System.out.println("Tipo:           " + tipoCargador);
        System.out.println("Conectores:     " + numeroConectores);
        System.out.println("Puestos:        " + puestosParqueo);
        System.out.println("Potencia máx:   " + potenciaMaxima + " kW");
        System.out.println("Potencia act:   " + potenciaActual + " kW");
        System.out.println("Ubicación:      " + ubicacion);
        System.out.println("══════════════════════════════════");
    }
    
    // ========== PARTE D: MÉTODOS ESTÁTICOS ==========
    private static void registrarSesion(String evento, boolean valido) {
        bitacora.add(new RegistroSesion(evento, valido));
    }
    
    public static double getPotenciaTotalFlota() {
        double total = 0;
        for (CargadorVE c : flota) {
            if (c != null) total += c.potenciaActual;
        }
        return total;
    }
    
    public static int contarPorTipo(TipoCargador tipo) {
        if (tipo == null) return 0;
        int cont = 0;
        for (CargadorVE c : flota) {
            if (c != null && c.tipoCargador == tipo) cont++;
        }
        return cont;
    }
    
    public static CargadorVE[] getFlotaCargadores() {
        return flota.toArray(new CargadorVE[0]);
    }
    
    public static void mostrarBitacora() {
        System.out.println("\n📋 BITÁCORA DE SESIONES:");
        for (RegistroSesion r : bitacora) {
            System.out.println("  " + r);
        }
    }
    
    public static double getLimiteRed() { return limiteRed; }
    public static void setLimiteRed(int nuevoLimite) { limiteRed = nuevoLimite; }
    public static int getTotalCargadores() { return totalCargadores; }
    
    // ========== GETTERS Y SETTERS ==========
    public String getFabricante() { return fabricante; }
    public void setFabricante(String fabricante) { this.fabricante = fabricante; }
    public int getAnioInstalacion() { return anioInstalacion; }
    public void setAnioInstalacion(int anioInstalacion) { this.anioInstalacion = anioInstalacion; }
    public double getVoltajeNominal() { return voltajeNominal; }
    public void setVoltajeNominal(double voltajeNominal) { this.voltajeNominal = voltajeNominal; }
    public TipoConector getTipoConector() { return tipoConector; }
    public void setTipoConector(TipoConector tipoConector) { this.tipoConector = tipoConector; }
    public TipoCargador getTipoCargador() { return tipoCargador; }
    public void setTipoCargador(TipoCargador tipoCargador) { this.tipoCargador = tipoCargador; }
    public int getNumeroConectores() { return numeroConectores; }
    public void setNumeroConectores(int numeroConectores) { this.numeroConectores = numeroConectores; }
    public int getPuestosParqueo() { return puestosParqueo; }
    public void setPuestosParqueo(int puestosParqueo) { this.puestosParqueo = puestosParqueo; }
    public double getPotenciaMaxima() { return potenciaMaxima; }
    public void setPotenciaMaxima(double potenciaMaxima) { this.potenciaMaxima = potenciaMaxima; }
    public Ubicacion getUbicacion() { return ubicacion; }
    public void setUbicacion(Ubicacion ubicacion) { this.ubicacion = ubicacion; }
    public double getPotenciaActual() { return potenciaActual; }
    public void setPotenciaActual(double potenciaActual) { this.potenciaActual = potenciaActual; }
}
