package interfaz;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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

import modelo.Inventario;

public class Application extends JFrame implements ActionListener {

	public static final String CHOOSE = "CHOOSE";
	public static final String TEST = "TEST";

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
		
		panelInfo = new PanelInfo(this);

		

		// Se agregan los componentes a la interfaz.
//		add(butTest);
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

					// Cargar definitivamente el excel al programa

					inventario = new Inventario();

					inventario.setWorkbook(new XSSFWorkbook(fs));
					inventario.obtenerItems();
					actualizarItems();

				}

			} catch (Exception ex) {

				ex.printStackTrace();
			}

		}
		
		

		if (command.equals(TEST)) {

			System.out.println(inventario.getItems());

		}
	}

	public static void main(String[] args) throws IOException, InvalidFormatException {

		Application app = new Application();
		app.setVisible(true);

	}

}
