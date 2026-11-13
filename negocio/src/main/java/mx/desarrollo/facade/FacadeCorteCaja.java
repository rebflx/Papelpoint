package mx.desarrollo.facade;

import mx.desarrollo.delegate.DelegateCorte;
import mx.desarrollo.delegate.ResumenCorte;
import mx.desarrollo.entity.CorteCaja;
import mx.desarrollo.entity.Usuario;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class FacadeCorteCaja {

    private final DelegateCorte delegateCorte = new DelegateCorte();

    public ResumenCorte consultarTurno(Usuario sesion, Usuario objetivo) {
        return delegateCorte.consultarTurno(sesion, objetivo);
    }

    public ResumenCorte cerrarCorte(Usuario sesion, Usuario objetivo, double efectivoContado, double fondoInicial) {
        return delegateCorte.cerrarCorte(sesion, objetivo, efectivoContado, fondoInicial);
    }

    public ResumenCorte corteFinal(Usuario sesion, LocalDate dia) {
        return delegateCorte.corteFinal(sesion, dia);
    }

    public List<CorteCaja> historial(Usuario sesion, LocalDate desde, LocalDate hasta, Usuario filtroUsuario) {
        return delegateCorte.historial(sesion, desde, hasta, filtroUsuario);
    }

    public ResumenCorte detalleCorte(Usuario sesion, Integer idCorte) {
        return delegateCorte.detalleCorte(sesion, idCorte);
    }

    public List<Usuario> usuariosParaCorte(Usuario sesion) {
        return delegateCorte.usuariosParaCorte(sesion);
    }

    public double totalDenominaciones(Map<Double, Integer> piezasPorDenominacion) {
        return delegateCorte.totalDenominaciones(piezasPorDenominacion);
    }

    public List<String> lineasImpresion(ResumenCorte resumen, int ancho) {
        return delegateCorte.lineasImpresion(resumen, ancho);
    }
}
