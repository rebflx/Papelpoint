package mx.desarrollo.delegate;

import mx.desarrollo.entity.Usuario;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public final class SesionCaja {

    public static final class Apertura {

        private final Integer idUsuario;
        private final double fondoInicial;
        private final LocalDateTime horaApertura;

        private Apertura(Integer idUsuario, double fondoInicial, LocalDateTime horaApertura) {
            this.idUsuario = idUsuario;
            this.fondoInicial = fondoInicial;
            this.horaApertura = horaApertura;
        }

        public Integer getIdUsuario() {
            return idUsuario;
        }

        public double getFondoInicial() {
            return fondoInicial;
        }

        public LocalDateTime getHoraApertura() {
            return horaApertura;
        }
    }

    private static final Map<Integer, Apertura> ABIERTAS = new ConcurrentHashMap<>();

    private SesionCaja() {
    }


    public static Apertura abrir(Usuario usuario, double fondoInicial) {
        validarUsuario(usuario);
        if (Double.isNaN(fondoInicial) || Double.isInfinite(fondoInicial)) {
            throw new IllegalArgumentException("El fondo inicial no es un numero valido");
        }
        if (fondoInicial < 0) {
            throw new IllegalArgumentException("El fondo inicial no puede ser negativo");
        }
        if (fondoInicial > DelegateCorte.MONTO_MAXIMO) {
            throw new IllegalArgumentException("El fondo inicial es demasiado grande");
        }
        double fondo = Math.round(fondoInicial * 100) / 100.0;
        Apertura nueva = new Apertura(usuario.getIdUsuario(), fondo, LocalDateTime.now());
        if (ABIERTAS.putIfAbsent(usuario.getIdUsuario(), nueva) != null) {
            throw new IllegalArgumentException("La caja ya esta abierta");
        }
        return nueva;
    }

    public static Apertura de(Usuario usuario) {
        if (usuario == null || usuario.getIdUsuario() == null) {
            return null;
        }
        return ABIERTAS.get(usuario.getIdUsuario());
    }

    public static boolean estaAbierta(Usuario usuario) {
        return de(usuario) != null;
    }

    public static void cerrar(Integer idUsuario) {
        if (idUsuario != null) {
            ABIERTAS.remove(idUsuario);
        }
    }

    private static void validarUsuario(Usuario usuario) {
        if (usuario == null || usuario.getIdUsuario() == null) {
            throw new IllegalArgumentException("No hay un usuario en sesion");
        }
    }
}
