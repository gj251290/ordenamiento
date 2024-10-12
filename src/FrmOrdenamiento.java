import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;

import java.awt.Toolkit;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FrmOrdenamiento extends JFrame {

    private JButton btnOrdenarBurbuja;
    private JButton btnOrdenarRapido;
    private JButton btnOrdenarInsercion;
    private JButton btnOrdenarSeleccion;
    private JButton btnOrdenarMezcla;
    private JToolBar tbOrdenamiento;
    @SuppressWarnings("rawtypes")
    private JComboBox cmbCriterio;
    private JLabel txtTiempo;
    private JButton btnBuscar;
    private JTextField txtBuscar;

    private JProgressBar progressBar;

    private JTable tblDocumentos;

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public FrmOrdenamiento() {

        tbOrdenamiento = new JToolBar();
        btnOrdenarBurbuja = new JButton();
        btnOrdenarInsercion = new JButton();
        btnOrdenarRapido = new JButton();
        btnOrdenarSeleccion = new JButton();
        btnOrdenarMezcla = new JButton();
        cmbCriterio = new JComboBox();
        txtTiempo = new JLabel();

        btnBuscar = new JButton();
        txtBuscar = new JTextField();

        progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        progressBar.setVisible(false);

        tblDocumentos = new JTable();

        btnOrdenarBurbuja.setIcon(new ImageIcon(getClass().getResource("/iconos/burbuja.png")));
        btnOrdenarBurbuja.setToolTipText("Ordenar Burbuja");
        btnOrdenarBurbuja.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnOrdenarBurbujaClick(evt);
            }
        });
        tbOrdenamiento.add(btnOrdenarBurbuja);

        btnOrdenarRapido.setIcon(new ImageIcon(getClass().getResource("/iconos/rapido.png")));
        btnOrdenarRapido.setToolTipText("Ordenar Rapido");
        btnOrdenarRapido.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnOrdenarRapidoClick(evt);
            }
        });
        tbOrdenamiento.add(btnOrdenarRapido);

        btnOrdenarInsercion.setIcon(new ImageIcon(getClass().getResource("/iconos/insercion.png")));
        btnOrdenarInsercion.setToolTipText("Ordenar Inserción");
        btnOrdenarInsercion.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnOrdenarInsercionClick(evt);
            }
        });
        tbOrdenamiento.add(btnOrdenarInsercion);

        btnOrdenarSeleccion.setIcon(new ImageIcon(getClass().getResource("/iconos/seleccion.png")));
        btnOrdenarSeleccion.setToolTipText("Ordenar Selección");
        btnOrdenarSeleccion.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnOrdenarSeleccionClick(evt);
            }
        });
        tbOrdenamiento.add(btnOrdenarSeleccion);

        btnOrdenarMezcla.setIcon(new ImageIcon(getClass().getResource("/iconos/mezcla.png")));
        btnOrdenarMezcla.setToolTipText("Ordenar Mezcla");
        btnOrdenarMezcla.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnOrdenarMezclaClick(evt);
            }
        });
        tbOrdenamiento.add(btnOrdenarMezcla);

        txtTiempo.setPreferredSize(new Dimension(100, 40));
        txtTiempo.setHorizontalAlignment(SwingConstants.CENTER);

        cmbCriterio.setModel(new DefaultComboBoxModel(
                new String[] { "Nombre Completo, Tipo de Documento", "Tipo de Documento, Nombre Completo" }));
        cmbCriterio.setToolTipText("Eluja un criterio de ordenación");
        tbOrdenamiento.add(cmbCriterio);
        tbOrdenamiento.add(txtTiempo);

        txtBuscar.setHorizontalAlignment(SwingConstants.CENTER);
        txtBuscar.setToolTipText("Ingrese la busqueda");

        btnBuscar.setIcon(new ImageIcon(getClass().getResource("/iconos/Buscar.png")));
        btnBuscar.setToolTipText("Buscar");
        btnBuscar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnBuscar(evt);
            }
        });
        tbOrdenamiento.add(txtBuscar);
        tbOrdenamiento.add(btnBuscar);
        tbOrdenamiento.add(progressBar);

        getContentPane().add(tbOrdenamiento, BorderLayout.NORTH);

        JScrollPane spDocumentos = new JScrollPane(tblDocumentos);
        getContentPane().add(spDocumentos, BorderLayout.CENTER);

        String nombreArchivo = System.getProperty("user.dir")
                + "/src/datos/Datos.csv";
        Documento.obtenerDatosDesdeArchivo(nombreArchivo);
        Documento.mostrarDatos(tblDocumentos);

        setSize(800, 600);
        Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
        int anchoPantalla = pantalla.width;
        int altoPantalla = pantalla.height;

        int anchoVentana = getWidth();
        int altoVentana = getHeight();

        int posX = (anchoPantalla - anchoVentana) / 2;
        int posY = (altoPantalla - altoVentana) / 2;

        setLocation(posX, posY);
        setTitle("Ordenamiento Documentos");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private void estadoBotones(boolean enabled) {
        btnOrdenarBurbuja.setEnabled(enabled);
        btnOrdenarRapido.setEnabled(enabled);
        btnOrdenarInsercion.setEnabled(enabled);
        btnOrdenarSeleccion.setEnabled(enabled);
        btnOrdenarMezcla.setEnabled(enabled);

        btnBuscar.setVisible(enabled);
        txtBuscar.setVisible(enabled);
    }

    private void btnOrdenarBurbujaClick(ActionEvent evt) {
        if (cmbCriterio.getSelectedIndex() >= 0) {
            estadoBotones(false);
            progressBar.setVisible(true);
            Util.iniciarCronometro();

            SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                @Override
                protected Void doInBackground() {
                    Documento.ordenarBurbuja(cmbCriterio.getSelectedIndex());
                    return null;
                }

                @Override
                protected void done() {
                    txtTiempo.setText(Util.getTextoTiempoCronometro());
                    Documento.mostrarDatos(tblDocumentos);
                    progressBar.setVisible(false);
                    estadoBotones(true);
                }
            };

            worker.execute();
        }
    }

    private void btnOrdenarRapidoClick(ActionEvent evt) {
        if (cmbCriterio.getSelectedIndex() >= 0) {
            estadoBotones(false);
            progressBar.setVisible(true);
            Util.iniciarCronometro();

            SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                @Override
                protected Void doInBackground() {
                    Documento.ordenarRapido(0, Documento.documentos.size() - 1, cmbCriterio.getSelectedIndex());
                    return null;
                }

                @Override
                protected void done() {
                    txtTiempo.setText(Util.getTextoTiempoCronometro());
                    Documento.mostrarDatos(tblDocumentos);
                    progressBar.setVisible(false);
                    estadoBotones(true);
                }
            };

            worker.execute();
        }
    }

    private void btnOrdenarInsercionClick(ActionEvent evt) {
        if (cmbCriterio.getSelectedIndex() >= 0) {
            estadoBotones(false);
            progressBar.setVisible(true);
            Util.iniciarCronometro();

            SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                @Override
                protected Void doInBackground() {
                    Documento.ordenarInsercion(cmbCriterio.getSelectedIndex());
                    return null;
                }

                @Override
                protected void done() {
                    txtTiempo.setText(Util.getTextoTiempoCronometro());
                    Documento.mostrarDatos(tblDocumentos);
                    progressBar.setVisible(false);
                    estadoBotones(true);
                }
            };

            worker.execute();
        }
    }

    private void btnOrdenarSeleccionClick(ActionEvent evt) {
        if (cmbCriterio.getSelectedIndex() >= 0) {
            estadoBotones(false);
            progressBar.setVisible(true);
            Util.iniciarCronometro();

            SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                @Override
                protected Void doInBackground() {
                    Documento.ordenarSeleccion(cmbCriterio.getSelectedIndex());
                    return null;
                }

                @Override
                protected void done() {
                    txtTiempo.setText(Util.getTextoTiempoCronometro());
                    Documento.mostrarDatos(tblDocumentos);
                    progressBar.setVisible(false);
                    estadoBotones(true);
                }
            };

            worker.execute();
        }
    }

    private void btnOrdenarMezclaClick(ActionEvent evt) {
        if (cmbCriterio.getSelectedIndex() >= 0) {
            estadoBotones(false);
            progressBar.setVisible(true);
            Util.iniciarCronometro();

            SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                @Override
                protected Void doInBackground() {
                    Documento.ordenarMezcla(cmbCriterio.getSelectedIndex());
                    return null;
                }

                @Override
                protected void done() {
                    txtTiempo.setText(Util.getTextoTiempoCronometro());
                    Documento.mostrarDatos(tblDocumentos);
                    progressBar.setVisible(false);
                    estadoBotones(true);
                }
            };

            worker.execute();
        }
    }

    private void btnBuscar(ActionEvent evt) {
        String termino = txtBuscar.getText().trim();
        if (!termino.isEmpty() && cmbCriterio.getSelectedIndex() >= 0) {
            Documento resultado = Documento.buscar(termino, cmbCriterio.getSelectedIndex());
            if (resultado != null) {
                for (int i = 0; i < Documento.documentos.size(); i++) {
                    if (Documento.documentos.get(i).equals(resultado)) {
                        tblDocumentos.setRowSelectionInterval(i, i);
                        tblDocumentos.scrollRectToVisible(tblDocumentos.getCellRect(i, 0, true));
                        break;
                    }
                }
                JOptionPane.showMessageDialog(this,
                        "Documento encontrado:\n" + resultado.getNombreCompleto() + "\n" + resultado.getDocumento());
            } else {
                JOptionPane.showMessageDialog(this, "Documento no encontrado.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Por favor, ingrese un término de búsqueda y seleccione un criterio.");
        }
    }

}