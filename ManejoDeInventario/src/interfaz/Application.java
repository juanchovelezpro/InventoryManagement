package interfaz;

import javax.swing.*;
import javax.swing.event.SwingPropertyChangeSupport;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import org.apache.poi.*;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbookFactory;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import comparadores.OrdenarItemsPorVolumenPorcentaje;
import modelo.Inventario;
import modelo.Item;

public class Application extends JFrame implements ActionListener {

	public static final String CHOOSE = "CHOOSE";
	public static final String TEST = "TEST";
	public static final String GRAPHIC = "GRAPHIC";

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
	 * Boton para hacer tests
	 */
	private JButton butTest;

	/**
	 * Boton para abrir la grafica del item correspondiente.
	 */
	private JButton butGrafica;

	/**
	 * Panel Auxiliar para la info de los items.
	 */
	private PanelInfo panelInfo;

	/**
	 * Objeto inventario de la aplicacion.
	 */
	private Inventario inventario;

	public Application() {

		// Aqui configuraciones basicas de la interfaz.

		setTitle("Manejo de Inventarios");
		setLayout(new BorderLayout());
		setSize(450, 300);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		try {

			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
		}

		settingsComponents();

	}

	/**
	 * Metodo para los componentes de la interfaz.
	 */
	public void settingsComponents() {

		// Donde se colocan los items. (Lista)
		items = new JComboBox<String>();
		items.addItem("Items");
		items.setSelectedIndex(0);

		// Boton para abrir el chooser del excel.
		butChooser = new JButton("Cargar Archivo");

		butChooser.setActionCommand(CHOOSE);
		butChooser.addActionListener(this);

		// Boton tester
		butTest = new JButton("Test");

		butTest.setActionCommand(TEST);
		butTest.addActionListener(this);
		butTest.setBounds(0, 0, 50, 50);

		// Boton grafica
		butGrafica = new JButton("Graficar");
		butGrafica.setActionCommand(GRAPHIC);
		butGrafica.addActionListener(this);
		butGrafica.setBounds(165, 160, 100, 20);

		panelInfo = new PanelInfo(this);
		panelInfo.add(butGrafica);

		// Se agregan los componentes a la interfaz.
		panelInfo.add(butTest);
		add(items, BorderLayout.NORTH);
		add(panelInfo, BorderLayout.CENTER);
		add(butChooser, BorderLayout.SOUTH);

	}

	public Inventario getInventario() {
		return inventario;
	}

	public void setInventario(Inventario inventario) {
		this.inventario = inventario;
	}

	public JComboBox<String> getItems() {
		return items;
	}

	public void setItems(JComboBox<String> items) {
		this.items = items;
	}

	// Actualiza los items en el programa. (La lista que aparece en la ventana)
	public void actualizarItems() {

		for (int i = 0; i < inventario.getItems().size(); i++) {

			items.addItem(
					inventario.getItems().get(i).getCodigo() + " - " + inventario.getItems().get(i).getDescripcion());

		}

	}

	public ArrayList<String> leyendaX() {

		ArrayList<String> leyenda = new ArrayList<>();

		XSSFSheet sheet = inventario.getWorkbook().getSheetAt(0);

		Iterator<Row> rows = sheet.iterator();

		rows.next();
		Row row = rows.next();

		Iterator<Cell> cells = row.cellIterator();

		while (cells.hasNext()) {

			Cell cell = cells.next();

			leyenda.add(cell.toString());

		}

		return leyenda;

	}

	// Para crear la grafica.
	public void crearGraficaLineal() {

		ArrayList<String> leyenda = leyendaX();

		DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		if (items.getSelectedIndex() > 0) {
			int selected = items.getSelectedIndex();
			Item item = inventario.getItems().get(selected - 1);

			for (int i = 0; i < item.getCantidades().size(); i++) {
				dataset.addValue(item.getCantidades().get(i), "Cantidades", leyenda.get(i));
			}

			JFreeChart lineChart = ChartFactory.createLineChart("Grafica Lineal", "Periodo", "Cantidad del Item",
					dataset, PlotOrientation.VERTICAL, true, true, false);

			ChartPanel panel = new ChartPanel(lineChart);

			JFrame grafica = new JFrame("Grafica");
			grafica.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			grafica.setSize(600, 600);
			grafica.add(panel);

			grafica.setVisible(true);
		} else {

			JOptionPane.showMessageDialog(null, "Selecciona un item válido", "Error", JOptionPane.ERROR_MESSAGE);

		}

	}

	@Override
	public void actionPerformed(ActionEvent e) {

		String command = e.getActionCommand();

		// Para cargar el archivo en el programa.
		if (command.equals(CHOOSE)) {

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

					inventario = new Inventario();

					inventario.setWorkbook(new XSSFWorkbook(fs));
					inventario.obtenerItems();
					actualizarItems();
					inventario.itemsParaClasificacion();
					inventario.calcularPorcentajesDeVolumenes();

				}

			} catch (Exception ex) {

				ex.printStackTrace();
			}

		}

		if (command.equals(GRAPHIC)) {

			if (inventario != null && inventario.getWorkbook() != null)
				crearGraficaLineal();

		}

		if (command.equals(TEST)) {

			Collections.sort(inventario.getItems(), new OrdenarItemsPorVolumenPorcentaje());

			DecimalFormat df = new DecimalFormat("0.00");

			for (int i = 0; i < inventario.getItems().size(); i++) {

				System.out.println(df.format(inventario.getItems().get(i).getVolumenPorcentaje()) + "  --> "
						+ df.format(inventario.asignarClasesItems().get(i)));

			}

		}

	}

	public static void main(String[] args) throws IOException, InvalidFormatException {

		Application app = new Application();
		app.setVisible(true);

	}

}
