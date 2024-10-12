import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JOptionPane;

public class Documento {

    private String apellido1;
    private String apellido2;
    private String nombre;
    private String documento;

    public Documento(String apellido1, String apellido2, String nombre, String documento) {
        this.apellido1 = apellido1;
        this.apellido2 = apellido2;
        this.nombre = nombre;
        this.documento = documento;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido1() {
        return apellido1;
    }

    public String getApellido2() {
        return apellido2;
    }

    public String getDocumento() {
        return documento;
    }

    public String getNombreCompleto() {
        return apellido1 + " " + apellido2 + " " + nombre;
    }

    // ********** Atributos y Metodos estaticos **********
    // Almacena la lista de documentos
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static List<Documento> documentos = new ArrayList();
    public static String[] encabezados;

    // Metodo que obtiene los datos desde el archivo CSV
    public static void obtenerDatosDesdeArchivo(String nombreArchivo) {
        documentos.clear();
        BufferedReader br = Archivo.abrirArchivo(nombreArchivo);
        if (br != null) {
            try {
                String linea = br.readLine();
                if (linea != null) {
                    encabezados = linea.split(";");
                }
                linea = br.readLine();
                while (linea != null) {
                    String[] textos = linea.split(";");
                    if (textos.length >= 4) {
                        Documento d = new Documento(textos[0], textos[1], textos[2], textos[3]);
                        documentos.add(d);
                    }
                    linea = br.readLine();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error al leer el archivo: " + ex.getMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "No se pudo abrir el archivo.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Metodo para mostrar los datos en una tabla
    public static void mostrarDatos(JTable tbl) {
        String[][] datos = null;
        if (documentos.size() > 0) {
            datos = new String[documentos.size()][encabezados.length];
            for (int i = 0; i < documentos.size(); i++) {
                datos[i][0] = documentos.get(i).apellido1;
                datos[i][1] = documentos.get(i).apellido2;
                datos[i][2] = documentos.get(i).nombre;
                datos[i][3] = documentos.get(i).documento;
            }
        }
        DefaultTableModel dtm = new DefaultTableModel(datos, encabezados);
        tbl.setModel(dtm);
    }

    // metodo para intercambiar elementos
    private static void intercambiar(int origen, int destino) {
        Documento temporal = documentos.get(origen);
        documentos.set(origen, documentos.get(destino));
        documentos.set(destino, temporal);
    }

    // metodo para verificar si un documento es mayor que otro
    public static boolean esMayor(Documento d1, Documento d2, int criterio) {
        if (criterio == 0) {
            // ordenar primero por Nombre Completo y luego por Tipo de Documento
            return ((d1.getNombreCompleto().compareTo(d2.getNombreCompleto()) > 0)
                    || (d1.getNombreCompleto().equals(d2.getNombreCompleto())
                            && d1.getDocumento().compareTo(d2.getDocumento()) > 0));
        } else {
            // ordenar primero por Tipo de Documento y luego por Nombre Completo
            return ((d1.getDocumento().compareTo(d2.getDocumento()) > 0)
                    || (d1.getDocumento().equals(d2.getDocumento())
                            && d1.getNombreCompleto().compareTo(d2.getNombreCompleto()) > 0));
        }
    }

    // Método que ordena los datos según el algoritmo de la BURBUJA recursivo
    public static void ordenarBurbujaRecursivo(int n, int criterio) {
        if (n == documentos.size() - 1) {
            return;
        } else {
            for (int i = n + 1; i < documentos.size(); i++) {
                // System.out.println(n+" vs "+i);
                if (esMayor(documentos.get(n), documentos.get(i), criterio)) {
                    intercambiar(n, i);
                }
            }
            ordenarBurbujaRecursivo(n + 1, criterio);
        }
    }

    // Método que ordena los datos según el algoritmo de la BURBUJA
    public static void ordenarBurbuja(int criterio) {
        for (int i = 0; i < documentos.size() - 1; i++) {

            // System.out.println("Vamos en " + i);
            for (int j = i + 1; j < documentos.size(); j++) {
                if (esMayor(documentos.get(i), documentos.get(j), criterio)) {
                    intercambiar(i, j);
                    // System.out.println("Intercambio " + documentos[i].getNombreCompleto() + " por
                    // " + documentos[j].getNombreCompleto());
                }
            }
        }
    }

    private static int localizarPivote(int inicio, int fin, int criterio) {
        int pivote = inicio;
        Documento documentoP = documentos.get(pivote);
        for (int i = inicio + 1; i <= fin; i++) {
            if (esMayor(documentoP, documentos.get(i), criterio)) {
                intercambiar(i, pivote);
                pivote++;
            }
        }

        return pivote;
    }

    public static void ordenarRapido(int inicio, int fin, int criterio) {
        if (inicio >= fin) {
            return;
        }
        int pivote = localizarPivote(inicio, fin, criterio);
        ordenarRapido(inicio, pivote - 1, criterio);
        ordenarRapido(pivote + 1, fin, criterio);
    }

    public static void ordenarInsercion(int criterio) {
        for (int i = 1; i < documentos.size(); i++) {
            Documento clave = documentos.get(i);
            int j = i - 1;
            while (j >= 0 && esMayor(documentos.get(j), clave, criterio)) {
                documentos.set(j + 1, documentos.get(j));
                j--;
            }
            documentos.set(j + 1, clave);
        }
    }

    public static void ordenarSeleccion(int criterio) {
        for (int i = 0; i < documentos.size() - 1; i++) {
            int indiceMin = i;
            for (int j = i + 1; j < documentos.size(); j++) {
                if (esMayor(documentos.get(indiceMin), documentos.get(j), criterio)) {
                    indiceMin = j;
                }
            }
            if (indiceMin != i) {
                intercambiar(i, indiceMin);
            }
        }
    }

    public static void ordenarMezcla(int criterio) {
        documentos = mergeSort(documentos, criterio);
    }

    private static List<Documento> mergeSort(List<Documento> lista, int criterio) {
        if (lista.size() <= 1) {
            return lista;
        }

        int medio = lista.size() / 2;
        List<Documento> izquierda = new ArrayList<>(lista.subList(0, medio));
        List<Documento> derecha = new ArrayList<>(lista.subList(medio, lista.size()));

        izquierda = mergeSort(izquierda, criterio);
        derecha = mergeSort(derecha, criterio);

        return merge(izquierda, derecha, criterio);
    }

    private static List<Documento> merge(List<Documento> izquierda, List<Documento> derecha, int criterio) {
        List<Documento> resultado = new ArrayList<>();
        int i = 0, j = 0;

        while (i < izquierda.size() && j < derecha.size()) {
            if (!esMayor(izquierda.get(i), derecha.get(j), criterio)) {
                resultado.add(izquierda.get(i));
                i++;
            } else {
                resultado.add(derecha.get(j));
                j++;
            }
        }

        while (i < izquierda.size()) {
            resultado.add(izquierda.get(i));
            i++;
        }

        while (j < derecha.size()) {
            resultado.add(derecha.get(j));
            j++;
        }

        return resultado;
    }

    public static Documento buscar(String termino, int criterio) {
        for (Documento doc : documentos) {
            if (criterio == 0) {
                if (doc.getNombreCompleto().toLowerCase().contains(termino.toLowerCase()) ||
                        doc.getDocumento().toLowerCase().contains(termino.toLowerCase())) {
                    return doc;
                }
            } else {
                if (doc.getDocumento().toLowerCase().contains(termino.toLowerCase()) ||
                        doc.getNombreCompleto().toLowerCase().contains(termino.toLowerCase())) {
                    return doc;
                }
            }
        }
        return null;
    }
}
