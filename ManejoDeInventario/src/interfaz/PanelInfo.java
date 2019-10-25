package interfaz;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.swing.*;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import modelo.*;

public class PanelInfo extends JPanel implements ActionListener {

	/**
	 * Lista de los items
	 */
	private JComboBox<String> items;

	/**
	 * Para seleccionar el archivo de excel
	 */
	private JFileChooser fileChooser;

	/**
	 * Boton para abrir el chooser del archivo.
	 */
	private JButton butChooser;

	/**
	 * Labels para el CVD
	 */
	private JLabel labCVD;
	private JLabel labCVDCalculado;
	private JLabel labClase;
	private JLabel labClaseRes;

	/**
	 * Boton para hacer tests
	 */
	private JButton butTest;

	/**
	 * Boton para abrir la grafica del item correspondiente.
	 */
	private JButton butGrafica;

	private Application app;

	public PanelInfo(Application app) {

		this.app = app;

		setLayout(null);
		settingComponents();

	}

	public void settingComponents() {

		// Label CVD
		labCVD = new JLabel("CVD:");
		labCVD.setFont(new Font("Garamond", Font.BOLD, 20));
		labCVD.setBounds(257, 165, 100, 20);
		add(labCVD);

		// Label CVD Calculado
		labCVDCalculado = new JLabel();
		labCVDCalculado.setBounds(344, 165, 100, 20);
		labCVDCalculado.setFont(new Font("Garamond", 1, 20));
		add(labCVDCalculado);

		// Boton grafica
		butGrafica = new JButton("Graficar");
		butGrafica.addActionListener(this);
		butGrafica.setBounds(290, 297, 140, 46);
		add(butGrafica);

		// Boton tester
		butTest = new JButton("Test");
		butTest.addActionListener(this);
		butTest.setBounds(297, 76, 121, 50);
		add(butTest);

		// Donde se colocan los items. (Lista)
		items = new JComboBox<String>();
		items.setFont(new Font("Garamond", Font.PLAIN, 18));
		items.setBounds(12, 13, 686, 38);
		items.addItem("Items");
		items.setSelectedIndex(0);
		items.addActionListener(this);
		add(items);

		// Boton para abrir el chooser del excel.
		butChooser = new JButton("Cargar Archivo");
		butChooser.setFont(new Font("Garamond", Font.BOLD, 18));
		butChooser.setBounds(0, 440, 753, 50);
		butChooser.addActionListener(this);
		add(butChooser);

		labClase = new JLabel("Clase:");
		labClase.setFont(new Font("Garamond", Font.BOLD, 20));
		labClase.setBounds(257, 226, 56, 16);
		add(labClase);

		labClaseRes = new JLabel("");
		labClaseRes.setFont(new Font("Garamond", Font.BOLD, 20));
		labClaseRes.setBounds(325, 215, 66, 27);
		add(labClaseRes);

	}

	public void actualizarItems() {

		for (int i = 0; i < app.getInventario().getItems().size(); i++) {

			items.addItem(app.getInventario().getItems().get(i).getCodigo() + " - "
					+ app.getInventario().getItems().get(i).getDescripcion());

		}

	}

	@Override
	public void paintComponent(Graphics g) {

		super.paintComponent(g);

		repaint();

	}

	// Obtiene la leyenda X para realizar la gráfica lineal
	public ArrayList<String> leyendaX() {

		ArrayList<String> leyenda = new ArrayList<>();

		int periodos = app.getInventario().getItems().get(0).getCantidades().size();

		for (int i = 1; i <= periodos; i++) {

			leyenda.add(i + "");

		}

		return leyenda;

	}

	// Para crear la grafica.
	public void crearGraficaLineal() {

		ArrayList<String> leyenda = leyendaX();

		DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		if (items.getSelectedIndex() > 0) {
			int selected = items.getSelectedIndex();
			Item item = app.getInventario().getItems().get(selected - 1);

			for (int i = 0; i < item.getCantidades().size(); i++) {
				dataset.addValue(item.getCantidades().get(i), "Cantidades", leyenda.get(i));
			}

			JFreeChart lineChart = ChartFactory.createLineChart(
					"Gráfica Lineal - Item: " + item.getCodigo() + "| Descripción: " + item.getDescripcion(), "Periodo",
					"Cantidad del Item", dataset, PlotOrientation.VERTICAL, true, true, false);

			ChartPanel panel = new ChartPanel(lineChart);

			JFrame grafica = new JFrame("Gráfica");
			grafica.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			grafica.setSize(600, 600);
			grafica.getContentPane().add(panel);

			grafica.setVisible(true);
		} else {

			JOptionPane.showMessageDialog(null, "Selecciona un item válido", "Error", JOptionPane.ERROR_MESSAGE);

		}

	}

	// Carga todos los procesos del programa para el analisis de los datos.
	public void analizarDatos(FileInputStream fs) throws IOException {

		app.getInventario().setWorkbook(new XSSFWorkbook(fs));
		app.getInventario().leerArchivo();
		app.getInventario().fillCantidadesItemPorPeriodo();
		app.getInventario().calcularPorcentajesDeVolumenes();
		app.getInventario().asignarClasesItems();
		app.getInventario().asignarVariablesDeAnalisis();

		actualizarItems();

	}

	@Override
	public void actionPerformed(ActionEvent e) {

		// Para cargar el archivo en el programa.
		if (e.getSource().equals(butChooser)) {

			fileChooser = new JFileChooser();
			fileChooser.showOpenDialog(null);
			fileChooser.isVisible();

			FileInputStream fs = null;

			try {
				if (fileChooser.getSelectedFile() != null) {
					fs = new FileInputStream(fileChooser.getSelectedFile());

				} else {

					JOptionPane.showMessageDialog(null, "No se ha seleccionado un archivo", "Error",
							JOptionPane.ERROR_MESSAGE);

				}

				if (fs != null) {

					// Cargar definitivamente el excel al programa y realizar todos los calculos y
					// procesos.

					analizarDatos(fs);

				}

			} catch (Exception ex) {

				ex.printStackTrace();
			}

		}

		// Para la seleccion de los items.
		if (e.getSource().equals(items)) {

			if (items.getSelectedIndex() != 0) {

				Inventario inv = app.getInventario();

				// Formato a dos decimales
				DecimalFormat df = new DecimalFormat("0.00");

				labCVDCalculado.setText(" " + df.format(inv.getItems().get(items.getSelectedIndex() - 1).CVD()));
				labClaseRes.setText(inv.getItems().get(items.getSelectedIndex() - 1).getClase() + "");

			} else {

				labCVDCalculado.setText("");

			}

		}

		if (e.getSource().equals(butGrafica)) {

			if (app.getInventario() != null && app.getInventario().getWorkbook() != null)
				crearGraficaLineal();

		}

		// Boton provisional para realizar pruebas.
		if (e.getSource().equals(butTest)) {

		}

	}

}
