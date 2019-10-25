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

public class Application extends JFrame {

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

		getContentPane().setLayout(new BorderLayout());
		setTitle("Manejo de Inventarios");
		setSize(660, 522);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		inventario = new Inventario();

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

		panelInfo = new PanelInfo(this);

		getContentPane().add(panelInfo, BorderLayout.CENTER);

	}

	public Inventario getInventario() {
		return inventario;
	}

	public void setInventario(Inventario inventario) {
		this.inventario = inventario;
	}

	public void testApp() {

		for (int i = 0; i < inventario.getItems().size(); i++) {

			System.out.println(inventario.getItems().get(i) + " | Acumulado: "
					+ String.format("%.2f", inventario.getVolumenesAcumulados().get(i)) + " | Clase "
					+ inventario.getItems().get(i).getClase());

		}

	}

}
