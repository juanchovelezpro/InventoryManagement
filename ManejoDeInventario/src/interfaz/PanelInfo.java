package interfaz;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.*;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import comparadores.OrdenarItemsPorVolumenPorcentaje;
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

	private JFileChooser fileSaver;

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
	private JLabel labIcono;
	private JComboBox<String> comboOrdenarItems;
	private JComboBox<String> comboTablas;
	private JButton butVerTabla;
	private JLabel labTablas;
	private JLabel labVersion;
	private JButton butCreditos;
	private JButton butHelp;
	private JButton butArchivoPlantilla;

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
		butGrafica.setToolTipText(
				"Permite visualizar la gr\u00E1fica lineal de las cantidades de  un item en un periodo. (Esta gr\u00E1fica se realiza con la primera tabla en la lista de tablas)");
		butGrafica.setBackground(new Color(112, 128, 144));
		butGrafica.setFont(new Font("Garamond", Font.BOLD, 15));
		butGrafica.addActionListener(this);
		butGrafica.setBounds(6, 436, 328, 38);
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
		labClase.setToolTipText("Puede ser tipo A, B o C.");
		labClase.setFont(new Font("Garamond", Font.BOLD, 26));
		labClase.setBounds(6, 323, 83, 38);
		add(labClase);

		comboOrdenarItems = new JComboBox<>();
		comboOrdenarItems.setToolTipText("Criterios para ordenar la lista de items.");
		comboOrdenarItems.setBounds(291, 108, 359, 26);
		comboOrdenarItems.addItem("Clase");
		comboOrdenarItems.addItem("Código");
		comboOrdenarItems.addItem("CVD");
		comboOrdenarItems.addItem("Descripcion");
		comboOrdenarItems.setSelectedIndex(0);
		comboOrdenarItems.addActionListener(this);
		add(comboOrdenarItems);

		JLabel labTitulo = new JLabel("Inventory Management");
		labTitulo.setToolTipText("Software para la gesti\u00F3n de inventarios");
		labTitulo.setHorizontalAlignment(SwingConstants.CENTER);
		labTitulo.setFont(new Font("Elephant", Font.PLAIN, 44));
		labTitulo.setBounds(99, 20, 565, 69);
		add(labTitulo);

		labIcono = new JLabel("");
		labIcono.setBounds(10, 6, 100, 96);
		labIcono.setIcon(new ImageIcon(getClass().getClassLoader().getResource("imagenes/logoIcesi.jpg")));
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
		lblTDemanda.setToolTipText("Patr\u00F3n de demanda.");
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
		txtCVD.setHorizontalAlignment(SwingConstants.RIGHT);
		txtCVD.setBounds(88, 250, 240, 36);
		add(txtCVD);
		txtCVD.setColumns(10);

		txtPDemanda = new JTextField();
		txtPDemanda.setHorizontalAlignment(SwingConstants.RIGHT);
		txtPDemanda.setEditable(false);
		txtPDemanda.setFont(new Font("Garamond", Font.PLAIN, 20));
		txtPDemanda.setColumns(10);
		txtPDemanda.setBounds(152, 288, 176, 36);
		add(txtPDemanda);

		txtClase = new JTextField();
		txtClase.setHorizontalAlignment(SwingConstants.RIGHT);
		txtClase.setEditable(false);
		txtClase.setFont(new Font("Garamond", Font.PLAIN, 24));
		txtClase.setColumns(10);
		txtClase.setBounds(88, 327, 240, 36);
		add(txtClase);

		comboTablas = new JComboBox<>();
		comboTablas.setFont(new Font("SansSerif", Font.PLAIN, 12));
		comboTablas.setToolTipText(
				"Lista de las tablas que pueden ser \u00FAtiles para obtener informacion detallada del inventario.");
		comboTablas.setBounds(345, 250, 300, 35);
		comboTablas.addItem("Tabla de Inventario de Items por periodo");
		comboTablas.addItem("Tabla de Clasificacion ABC");
		comboTablas.addActionListener(this);
		add(comboTablas);

		butVerTabla = new JButton("Ver tabla");
		butVerTabla.setToolTipText("Permite ver la tabla seleccionada en la lista de tablas.");
		butVerTabla.setBackground(new Color(119, 136, 153));
		butVerTabla.setFont(new Font("Garamond", Font.BOLD, 18));
		butVerTabla.setBounds(345, 292, 300, 36);
		butVerTabla.addActionListener(this);
		add(butVerTabla);

		labTablas = new JLabel("Tablas");
		labTablas.setFont(new Font("Elephant", Font.PLAIN, 17));
		labTablas.setHorizontalAlignment(SwingConstants.CENTER);
		labTablas.setBounds(376, 204, 238, 16);
		add(labTablas);

		labVersion = new JLabel("Versi\u00F3n 1.0.0");
		labVersion.setToolTipText("(BETA)");
		labVersion.setFont(new Font("Elephant", Font.BOLD, 18));
		labVersion.setHorizontalAlignment(SwingConstants.CENTER);
		labVersion.setBounds(500, 437, 150, 53);
		add(labVersion);

		butCreditos = new JButton("Cr\u00E9ditos");
		butCreditos.setToolTipText("Muestra las personas que colaboraron en el proyecto y los cargos.");
		butCreditos.setFont(new Font("Garamond", Font.BOLD, 16));
		butCreditos.setBackground(new Color(128, 128, 128));
		butCreditos.setBounds(417, 451, 90, 28);
		butCreditos.addActionListener(this);
		add(butCreditos);

		butHelp = new JButton("Help");
		butHelp.setToolTipText("Instrucciones para el manejo correcto del programa.");
		butHelp.setBackground(new Color(128, 128, 128));
		butHelp.setFont(new Font("Garamond", Font.BOLD, 16));
		butHelp.setBounds(340, 451, 77, 28);
		butHelp.addActionListener(this);
		add(butHelp);

		butArchivoPlantilla = new JButton("Archivo Excel Plantilla");
		butArchivoPlantilla.setToolTipText(
				"Descarga la plantilla, que es un archivo excel que muestra como tal el archivo que lee el programa. (Archivos con diferente formato a la plantilla no los lee el programa)");
		butArchivoPlantilla.setBackground(new Color(0, 128, 0));
		butArchivoPlantilla.setFont(new Font("Ebrima", Font.BOLD, 12));
		butArchivoPlantilla.setBounds(349, 355, 301, 28);
		butArchivoPlantilla.addActionListener(this);
		add(butArchivoPlantilla);

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

	public void crearTablaInventarioItemPorPeriodo() {

		ArrayList<Item> itemsOrdenados = app.getInventario().getItems();

		int auxCol = 2;
		String[] columnas = new String[itemsOrdenados.get(0).getCantidades().size() + auxCol];
		Object[][] data = new Object[itemsOrdenados.size()][itemsOrdenados.get(0).getCantidades().size() + auxCol];

		for (int i = 0; i < columnas.length; i++) {

			if (i == 0) {

				columnas[i] = "Item";

			} else if (i == 1) {

				columnas[i] = "Desc. Item";

			} else {

				columnas[i] = i - 1 + "";

			}

		}

		for (int i = 0; i < data.length; i++) {

			for (int j = 0; j < data[0].length; j++) {

				if (j == 0) {
					data[i][j] = itemsOrdenados.get(i).getCodigo();
				} else if (j == 1) {
					data[i][j] = itemsOrdenados.get(i).getDescripcion();

				} else {

					data[i][j] = itemsOrdenados.get(i).getCantidades().get(j - 2);

				}
			}

		}

		JTable myTable = new JTable(data, columnas);
		myTable.setEnabled(true);
		myTable.getTableHeader().setResizingAllowed(true);
		myTable.getTableHeader().setReorderingAllowed(false);
		JScrollPane scrollPane = new JScrollPane(myTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		myTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		scrollPane.setSize(new Dimension(800, 500));
		myTable.setFont(new Font("Garamond", 1, 16));

		JFrame tabla = new JFrame();
		tabla.setTitle("Tabla de Inventario de items por periodo");
		tabla.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		tabla.getContentPane().add(scrollPane);
		tabla.setSize(scrollPane.getSize());
		tabla.setVisible(true);

	}

	public void crearTablaClasificacionABC() {

		ArrayList<Item> itemsPorClase = app.getInventario().getItems();

		Collections.sort(itemsPorClase, new OrdenarItemsPorVolumenPorcentaje());

		String[] columnas = { "Item", "Volumen", "Volumen %", "Vol. Acumulado", "Clase" };
		String[][] data = new String[itemsPorClase.size()][columnas.length];

		for (int i = 0; i < data.length; i++) {

			for (int j = 0; j < data[0].length; j++) {

				switch (j) {
				case 0:

					data[i][j] = itemsPorClase.get(i).getCodigo() + "";

					break;

				case 1:
					data[i][j] = String.format("%.2f", itemsPorClase.get(i).volumen());
					break;

				case 2:
					data[i][j] = String.format("%.2f", itemsPorClase.get(i).getVolumenPorcentaje());
					break;

				case 3:
					data[i][j] = String.format("%.2f", app.getInventario().getVolumenesAcumulados().get(i));
					break;

				case 4:
					data[i][j] = data[i][j] = itemsPorClase.get(i).getClase() + "";
					break;

				default:
					break;
				}

			}

		}

		JTable myTable = new JTable(data, columnas);
		myTable.setEnabled(true);
		myTable.getTableHeader().setResizingAllowed(true);
		myTable.getTableHeader().setReorderingAllowed(false);
		JScrollPane scrollPane = new JScrollPane(myTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		myTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		scrollPane.setSize(new Dimension(800, 500));
		myTable.setFont(new Font("Garamond", 1, 16));

		JFrame tabla = new JFrame();
		tabla.setTitle("Tabla de Clasificacion ABC");
		tabla.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		tabla.getContentPane().add(scrollPane);
		tabla.setSize(scrollPane.getSize());
		tabla.setVisible(true);

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

		if (items.getSelectedIndex() >= 0) {
			int selected = items.getSelectedIndex();
			Item item = app.getInventario().getItems().get(selected);

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

		app.setInventario(new Inventario());
		app.getInventario().setWorkbook(new XSSFWorkbook(fs));
		app.getInventario().leerArchivo();
		app.getInventario().fillCantidadesItemPorPeriodo();
		app.getInventario().calcularPorcentajesDeVolumenes();
		app.getInventario().asignarClasesItems();
		app.getInventario().asignarVariablesDeAnalisis();

		ordenarPorCriterio(comboOrdenarItems.getSelectedIndex());

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

					JOptionPane.showMessageDialog(null, "El archivo se ha cargado correctamente!", "Info",
							JOptionPane.INFORMATION_MESSAGE);

				}

			} catch (Exception ex) {

				ex.printStackTrace();
			}

		}

		if (e.getSource().equals(butArchivoPlantilla)) {

			fileSaver = new JFileChooser();
			int op = fileSaver.showSaveDialog(null);
			fileSaver.isVisible();

			if (op == JFileChooser.APPROVE_OPTION) {

				guardarArchivo(fileSaver.getSelectedFile().getPath() + ".xlsx");
				JOptionPane.showMessageDialog(null, "Guardado en " + fileSaver.getSelectedFile().getPath(), "Aviso", 1);

			}

		}

		// Para la seleccion de los items.
		if (e.getSource().equals(items)) {

			if (items.getSelectedIndex() >= 0) {

				Inventario inv = app.getInventario();

				// Formato a dos decimales
				DecimalFormat df = new DecimalFormat("0.00");

				txtCVD.setText(" " + df.format(inv.getItems().get(items.getSelectedIndex()).CVD()));
				txtClase.setText(inv.getItems().get(items.getSelectedIndex()).getClase() + "");
				txtPDemanda.setText(inv.getItems().get(items.getSelectedIndex()).getPatronDemanda());

				if (inv.getItems().get(items.getSelectedIndex()).isStock()) {

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

		if (e.getSource().equals(comboOrdenarItems)) {

			if (app.getInventario() != null) {
				int criterio = comboOrdenarItems.getSelectedIndex();

				ordenarPorCriterio(criterio);
			}

		}

		if (e.getSource().equals(butGrafica)) {

			if (app.getInventario() != null && app.getInventario().getWorkbook() != null) {
				crearGraficaLineal();
			} else {

				JOptionPane.showMessageDialog(null, "Aún no se ha cargado un archivo", "Error",
						JOptionPane.ERROR_MESSAGE);

			}

		}

		if (e.getSource().equals(butVerTabla)) {

			if (app.getInventario() != null) {

				if (comboTablas.getSelectedIndex() == 0)
					crearTablaInventarioItemPorPeriodo();

				if (comboTablas.getSelectedIndex() == 1)
					crearTablaClasificacionABC();

			} else {

				JOptionPane.showMessageDialog(null, "Aún no se ha cargado un archivo", "Error",
						JOptionPane.ERROR_MESSAGE);

			}
		}

		// Boton provisional para realizar pruebas.
		if (e.getSource().equals(butTest)) {

		}

		if (e.getSource().equals(butCreditos)) {

			String s = "Créditos:\n" + "Lider del proyecto: Andrés Felipe Cuastumal\r\n"
					+ "Programador principal: Juan Camilo Vélez Olaya\r\n" + "Diseñadores de interfaz de usuario: \n"
					+ "Andrés Felipe Cuastumal\r\n" + "" + "Juan Camilo Vélez Olaya\r\n"
					+ "Especialistas en gestion de inventarios: \n" + "Camila Varela\r\n" + "Frederick Wutscher" + "";

			JOptionPane.showMessageDialog(null, s, "Créditos", JOptionPane.INFORMATION_MESSAGE);

		}

		if (e.getSource().equals(butHelp)) {

			String ayuda = "El programa lee solo archivos del mismo formato que tiene el Archivo Excel Plantilla, es decir, cualquier archivo distinto al de la plantilla no lo leerá el programa.\n"
					+ "Otra instrucción a tener en cuenta es si el archivo a cargar tiene varias hojas, asegurese de tener en la primera hoja del archivo excel, la que tiene el formato igual al de la plantilla."
					+ " En la hoja con formato de la plantilla se pueden agregar la cantidad de items que se requieran, "
					+ "es decir no hay limite de items. Solo se deben llenar las celdas que corresponden a los datos de un item, cualquier otra celda que no se encuentra en la tabla de los items y tenga algun dato, el programa no leerá correctamente el archivo. "
					+ "Todos los campos que requiere un item deben de estar rellenos segun el formato de la celda. No pueden haber celdas vacias,"
					+ " de lo contrario el programa no leerá correctamente los datos. "
					+ "Si cumple todo lo anterior y no se lee el archivo, delimite la tabla de los items en el archivo excel, borrando el formato de todas las celdas que se encuentran fuera de la tabla de items.";

			JFrame aux = new JFrame();
			aux.setTitle("Help");
			aux.setSize(475, 300);
			aux.setResizable(false);
			JTextArea ta = new JTextArea(ayuda);
			ta.setWrapStyleWord(true);
			ta.setFont(new Font("Garamond", 1, 18));
			ta.setEditable(false);
			ta.setSize(400, 400);
			ta.setLineWrap(true);
			JScrollPane sc = new JScrollPane(ta);
			sc.setPreferredSize(new Dimension(400, 200));
			sc.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			aux.getContentPane().add(sc);
			aux.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			aux.setVisible(true);

		}

	}

	public void guardarArchivo(String ruta) {

		try {

			InputStream file = getClass().getResourceAsStream("/archivos/Plantilla.xlsx");

			XSSFWorkbook wb = new XSSFWorkbook(file);
			FileOutputStream out = new FileOutputStream(ruta);
			wb.write(out);
			out.close();

		} catch (Exception e) {
			e.printStackTrace();
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

	public void ordenarPorCriterio(int criterio) {

		// Por Volumen % -- "Por Clase" Primero los A, luego los B y por ultimo los C
		if (criterio == 0) {

			app.getInventario().ordenarItemsPorClase();
			items.removeAllItems();
			actualizarItems();

		}

		if (criterio == 1) {

			app.getInventario().ordenarItemsPorCodigo();
			items.removeAllItems();
			actualizarItems();

		}

		if (criterio == 2) {

			app.getInventario().ordenarItemsPorCVD();
			items.removeAllItems();
			actualizarItems();

		}

		if (criterio == 3) {

			app.getInventario().ordenarItemsPorDescripcion();
			items.removeAllItems();
			actualizarItems();

		}

	}

	public JLabel getLabIcono() {
		return labIcono;
	}

	public JComboBox<String> getComboOrdenarItems() {
		return comboOrdenarItems;
	}

	public JButton getButArchivoPlantilla() {
		return butArchivoPlantilla;
	}
}