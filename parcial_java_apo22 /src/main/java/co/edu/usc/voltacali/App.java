package co.edu.usc.voltacali;

public class App {
    public static void main(String[] args) {
        System.out.println("======================================");
        System.out.println("  PARCIAL 1 - VOLTACALI S.A.S.");
        System.out.println("  Sistema de Gestión de Cargadores");
        System.out.println("======================================\n");
        
        // ========== PASO 1: CREAR LA FLOTA ==========
        System.out.println("--- PASO 1: Crear flota ---");
        CargadorVE.setLimiteRed(50);
        
        // C1
        CargadorVE c1 = new CargadorVE("ABB", 2022, 400,
            CargadorVE.TipoConector.CCS2,
            CargadorVE.TipoCargador.RAPIDO_DC,
            3, 2, 220,
            CargadorVE.Ubicacion.CENTRO_COMERCIAL);
        c1.setPotenciaActual(55.0);
        
        // C2
        CargadorVE c2 = new CargadorVE("Siemens", 2022, 400,
            CargadorVE.TipoConector.TIPO_2,
            CargadorVE.TipoCargador.MURAL,
            2, 1, 22,
            CargadorVE.Ubicacion.RESIDENCIAL);
        
        // C3
        CargadorVE c3 = new CargadorVE(c1); // copia
        c3.setFabricante("Delta");
        c3.setUbicacion(CargadorVE.Ubicacion.ESTACION_SERVICIO);
        c3.setPotenciaMaxima(800);
        
        System.out.println("Total cargadores: " + CargadorVE.getTotalCargadores());
        
        // ========== PASO 2: SESIÓN DE CARGA SOBRE C1 ==========
        System.out.println("\n--- PASO 2: Sesión de carga C1 ---");
        System.out.println("Potencia inicial: " + c1.getPotenciaActual() + " kW");
        
        ejecutarAccion(c1, "setPotenciaActual(49)", () -> c1.setPotenciaActual(49));
        ejecutarAccion(c1, "aumentarPotencia(15)", () -> c1.aumentarPotencia(15));
        ejecutarAccion(c1, "tiempoEstimadoCarga(66)", () -> c1.tiempoEstimadoCarga(66));
        ejecutarAccion(c1, "aumentarPotencia(19)", () -> c1.aumentarPotencia(19));
        ejecutarAccion(c1, "tiempoEstimadoCarga(50, 2)", () -> c1.tiempoEstimadoCarga(50, 2));
        ejecutarAccion(c1, "reducirPotencia(19)", () -> c1.reducirPotencia(19));
        ejecutarAccion(c1, "aumentarPotencia(5, 3)", () -> c1.aumentarPotencia(5, 3));
        
        System.out.println("Potencia final C1: " + c1.getPotenciaActual() + " kW");
        
        // ========== PASO 3: OPERACIONES RESTO DE FLOTA ==========
        System.out.println("\n--- PASO 3: Resto de la flota ---");
        ejecutarAccion(c2, "setPotenciaActual(22)", () -> c2.setPotenciaActual(22));
        ejecutarAccion(c3, "setPotenciaActual(120)", () -> c3.setPotenciaActual(120));
        ejecutarAccion(c4, "aumentarPotencia(7.4)", () -> c4.aumentarPotencia(7.4));
        ejecutarAccion(c5, "aumentarPotencia(30)", () -> c5.aumentarPotencia(30));
        
        System.out.println("Potencia total flota: " + CargadorVE.getPotenciaTotalFlota() + " kW");
        
        // ========== PASO 4: ESTADÍSTICAS ==========
        System.out.println("\n--- PASO 4: Estadísticas ---");
        System.out.println("Cargadores por tipo:");
        for (CargadorVE.TipoCargador t : CargadorVE.TipoCargador.values()) {
            System.out.println("  " + t + ": " + CargadorVE.contarPorTipo(t));
        }
        
        // ========== MOSTRAR BITÁCORA ==========
        CargadorVE.mostrarBitacora();
        
        System.out.println("\n✅ PROGRAMA FINALIZADO");
    }
    
    // Método auxiliar para mostrar cada acción
    private static void ejecutarAccion(CargadorVE c, String desc, Runnable accion) {
        System.out.print(desc + " → ");
        accion.run();
        System.out.println("Potencia: " + c.getPotenciaActual() + " kW");
    }
}
