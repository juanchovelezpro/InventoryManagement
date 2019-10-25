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

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import modelo.Inventario;
import modelo.Item;

public class Application extends JFrame implements ActionListener {

	public static final String CHOOSE = "CHOOSE";

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
		setSize(759, 525);
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
		items.setFont(new Font("Garamond", Font.PLAIN, 18));
		items.setBounds(0, 6, 753, 38);
		items.addItem("Items");
		items.setSelectedIndex(0);

		// Boton para abrir el chooser del excel.
		butChooser = new JButton("Cargar Archivo");
		butChooser.setFont(new Font("Garamond", Font.BOLD, 18));
		butChooser.setBounds(0, 440, 753, 50);

		butChooser.setActionCommand(CHOOSE);
		butChooser.addActionListener(this);

		panelInfo = new PanelInfo(this);
		panelInfo.setBounds(0, 45, 753, 396);
		getContentPane().setLayout(null);

		// Se agregan los componentes a la interfaz.
//		panelInfo.add(butTest);
		getContentPane().add(items);
		getContentPane().add(panelInfo);
		getContentPane().add(butChooser);

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

	// Método para realizar tests a la app.
	public void testApp() {

		for (int i = 0; i < inventario.getItems().size(); i++) {

			System.out.println(inventario.getItems().get(i) + " | Acumulado: "
					+ String.format("%.2f", inventario.getVolumenesAcumulados().get(i)) + " | Clase "
					+ inventario.getItems().get(i).getClase());

		}

	}

	// Actualiza los items en el programa. (La lista que aparece en la ventana)
	public void actualizarItems() {

		for (int i = 0; i < inventario.getItems().size(); i++) {

			items.addItem(
					inventario.getItems().get(i).getCodigo() + " - " + inventario.getItems().get(i).getDescripcion());

		}

	}

	// Saca las leyendas X para las graficas lineales.
	public ArrayList<String> leyendaX() {

		ArrayList<String> leyenda = new ArrayList<>();

		int periodos = inventario.getItems().get(0).getCantidades().size();

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
			Item item = inventario.getItems().get(selected - 1);

			for (int i = 0; i < item.getCantidades().size(); i++) {
				dataset.addValue(item.getCantidades().get(i), "Cantidades", leyenda.get(i));
			}

			JFreeChart lineChart = ChartFactory.createLineChart(
					"Gráfica Lineal - Item: " + item.getCodigo() + "| Descripción: " + item.getDescripcion(), "Periodo",
					"Cantidad del Item", dataset, PlotOrientation.VERTICAL, true, true, false);

			ChartPanel panel = new ChartPanel(lineChart);

			JFrame grafica = new JFrame("Gráfica");
			grafica.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			grafica.setSize(600, 600);
			grafica.getContentPane().add(panel);

			grafica.setVisible(true);
		} else {

			JOptionPane.showMessageDialog(null, "Selecciona un item válido", "Error", JOptionPane.ERROR_MESSAGE);

		}

	}

	// Para controlar los eventos de los botones.
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
					inventario.leerArchivo();
					inventario.fillCantidadesItemPorPeriodo();
					inventario.calcularPorcentajesDeVolumenes();
					inventario.asignarClasesItems();
					inventario.asignarVariablesDeAnalisis();

					actualizarItems();

				}

			} catch (Exception ex) {

				ex.printStackTrace();
			}

		}

	}

	public static void main(String[] args) throws IOException, InvalidFormatException {

		Application app = new Application();
		app.setVisible(true);

	}

}
