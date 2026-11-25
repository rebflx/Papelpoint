package mx.desarrollo.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

public final class ConfigBotonesProductos {

    private static final String CLAVE = "productosRapidos";

    private ConfigBotonesProductos() {
    }

    private static Preferences prefs() {
        return Preferences.userNodeForPackage(ConfigBotonesProductos.class);
    }

    public static List<Integer> cargarIds() {
        String s = prefs().get(CLAVE, null);
        if (s == null) {
            return null;
        }
        List<Integer> ids = new ArrayList<>();
        for (String t : s.split(",")) {
            t = t.trim();
            if (t.isEmpty()) {
                continue;
            }
            try {
                ids.add(Integer.valueOf(t));
            } catch (NumberFormatException ignorar) {
            }
        }
        return ids;
    }

    public static void guardarIds(List<Integer> ids) {
        StringBuilder sb = new StringBuilder();
        for (Integer id : ids) {
            if (sb.length() > 0) {
                sb.append(',');
            }
            sb.append(id);
        }
        prefs().put(CLAVE, sb.toString());
    }
}