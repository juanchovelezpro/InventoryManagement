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
	private JLabel labClase;

	/**
	 * Boton para hacer tests
	 */
	private JButton butTest;

	/**
	 * Boton para abrir la grafica del item correspondiente.
	 */
	private JButton butGrafica;

	private Application app;
	private JLabel lblTDemanda;
	private JTextField txtCVD;
	private JTextField txtPDemanda;
	private JTextField txtClase;
	private JButton butStock;

	public PanelInfo(Application app) {

		this.app = app;

		setLayout(null);
		settingComponents();

	}

	public void settingComponents() {

		// Label CVD
		labCVD = new JLabel("CVD:");
		labCVD.setToolTipText("Coeficiente de variaci\u00F3n de demanda.");
		labCVD.setFont(new Font("Garamond", Font.BOLD, 26));
		labCVD.setBounds(6, 250, 83, 38);
		add(labCVD);

		// Boton grafica
		butGrafica = new JButton("Gr\u00E1fica Lineal - Cantidad de Item vs Per\u00EDodo");
		butGrafica.setBackground(new Color(112, 128, 144));
		butGrafica.setFont(new Font("Garamond", Font.BOLD, 14));
		butGrafica.addActionListener(this);
		butGrafica.setBounds(0, 436, 328, 38);
		add(butGrafica);

		// Boton tester
		butTest = new JButton("Test");
		butTest.addActionListener(this);
		butTest.setBounds(409, 332, 121, 50);
//		add(butTest);

		// Donde se colocan los items. (Lista)
		items = new JComboBox<String>();
		items.setToolTipText("Lista de los items del inventario.");
		items.setMaximumRowCount(6);
		items.setBackground(new Color(25, 25, 112));
		items.setFont(new Font("Garamond", Font.BOLD, 16));
		items.setBounds(151, 133, 499, 38);
		items.addItem("Items");
		items.setSelectedIndex(0);
		items.addActionListener(this);
		add(items);

		// Boton para abrir el chooser del excel.
		butChooser = new JButton("Cargar Archivo");
		butChooser.setToolTipText("Carga el archivo excel donde se encuentran los datos del inventario.");
		butChooser.setBackground(new Color(119, 136, 153));
		butChooser.setFont(new Font("Tw Cen MT", Font.BOLD, 16));
		butChooser.setBounds(0, 108, 150, 63);
		butChooser.addActionListener(this);
		add(butChooser);

		labClase = new JLabel("Clase:");
		labClase.setFont(new Font("Garamond", Font.BOLD, 26));
		labClase.setBounds(6, 323, 83, 38);
		add(labClase);

		JComboBox comboBox = new JComboBox();
		comboBox.setBounds(291, 108, 359, 26);
		add(comboBox);

		JLabel labTitulo = new JLabel("Inventory Management");
		labTitulo.setHorizontalAlignment(SwingConstants.CENTER);
		labTitulo.setFont(new Font("Elephant", Font.PLAIN, 44));
		labTitulo.setBounds(115, 16, 514, 69);
		add(labTitulo);

		JLabel labIcono = new JLabel("");
		labIcono.setBounds(6, 6, 100, 96);
		add(labIcono);

		JLabel labOrdenar = new JLabel("Ordenar items por:");
		labOrdenar.setFont(new Font("Garamond", Font.BOLD, 16));
		labOrdenar.setBounds(156, 108, 160, 32);
		add(labOrdenar);

		JLabel labAnalisis = new JLabel("Informaci\u00F3n del Item seleccionado:");
		labAnalisis.setHorizontalAlignment(SwingConstants.CENTER);
		labAnalisis.setFont(new Font("Elephant", Font.PLAIN, 17));
		labAnalisis.setBounds(10, 193, 306, 38);
		add(labAnalisis);

		JSeparator separador = new JSeparator();
		separador.setBackground(new Color(0, 0, 0));
		separador.setOrientation(SwingConstants.VERTICAL);
		separador.setBounds(334, 187, 20, 287);
		add(separador);

		lblTDemanda = new JLabel("P. Demanda:");
		lblTDemanda.setFont(new Font("Garamond", Font.BOLD, 26));
		lblTDemanda.setBounds(6, 288, 150, 38);
		add(lblTDemanda);

		butStock = new JButton("");
		butStock.setToolTipText(
				"Indica si el item se encuentra en \"Stock\"(Se pone en VERDE el boton )o \"En Pedido\" (El bot\u00F3n se pone ROJO)");
		butStock.setFont(new Font("Garamond", Font.BOLD, 28));
		butStock.setBounds(6, 368, 322, 38);
		add(butStock);

		txtCVD = new JTextField();
		txtCVD.setFont(new Font("Garamond", Font.PLAIN, 24));
		txtCVD.setEditable(false);
		txtCVD.setHorizontalAlignment(SwingConstants.LEFT);
		txtCVD.setBounds(88, 250, 240, 36);
		add(txtCVD);
		txtCVD.setColumns(10);

		txtPDemanda = new JTextField();
		txtPDemanda.setEditable(false);
		txtPDemanda.setFont(new Font("Garamond", Font.PLAIN, 20));
		txtPDemanda.setColumns(10);
		txtPDemanda.setBounds(152, 288, 176, 36);
		add(txtPDemanda);

		txtClase = new JTextField();
		txtClase.setEditable(false);
		txtClase.setFont(new Font("Garamond", Font.PLAIN, 24));
		txtClase.setColumns(10);
		txtClase.setBounds(88, 327, 240, 36);
		add(txtClase);

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

				txtCVD.setText(" " + df.format(inv.getItems().get(items.getSelectedIndex() - 1).CVD()));
				txtClase.setText(inv.getItems().get(items.getSelectedIndex() - 1).getClase() + "");
				txtPDemanda.setText(inv.getItems().get(items.getSelectedIndex() - 1).getPatronDemanda());

				if (inv.getItems().get(items.getSelectedIndex() - 1).isStock()) {

					butStock.setText("EN STOCK");
					butStock.setBackground(Color.GREEN.brighter());

				} else {

					butStock.setText("BAJO PEDIDO");
					butStock.setBackground(Color.RED.brighter());

				}

			} else {

				txtCVD.setText("");
				txtClase.setText("");
				txtPDemanda.setText("");
				butStock.setText("");
				butStock.setBackground(Color.GRAY.brighter());

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

	public JTextField getTxtCVD() {
		return txtCVD;
	}

	public JTextField getTxtClase() {
		return txtClase;
	}

	public JTextField getTxtPDemanda() {
		return txtPDemanda;
	}

	public JButton getButStock() {
		return butStock;
	}
}
