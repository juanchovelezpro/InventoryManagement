package interfaz;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

import javax.swing.*;
import modelo.*;

public class PanelInfo extends JPanel implements ActionListener {

	public static final String GRAPHIC = "GRAPHIC";

	/**
	 * Labels para el CVD
	 */
	private JLabel labCVD;
	private JLabel labCVDCalculado;
	private JLabel labClase;
	private JLabel labClaseRes;


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

		// Label CVD Calculado
		labCVDCalculado = new JLabel();
		labCVDCalculado.setBounds(344, 165, 100, 20);
		labCVDCalculado.setFont(new Font("Garamond", 1, 20));

		// Boton grafica
		butGrafica = new JButton("Graficar");
		butGrafica.setActionCommand(GRAPHIC);
		butGrafica.addActionListener(this);
		butGrafica.setBounds(304, 291, 140, 46);

		add(butGrafica);
		add(labCVD);
		add(labCVDCalculado);
		
		labClase = new JLabel("Clase:");
		labClase.setFont(new Font("Garamond", Font.BOLD, 20));
		labClase.setBounds(257, 226, 56, 16);
		add(labClase);
		
		labClaseRes = new JLabel("");
		labClaseRes.setFont(new Font("Garamond", Font.BOLD, 20));
		labClaseRes.setBounds(325, 215, 66, 27);
		add(labClaseRes);

	}

	@Override
	public void paintComponent(Graphics g) {

		super.paintComponent(g);

		if (app.getItems().getSelectedIndex() != 0) {

			Inventario inv = app.getInventario();

			// Formato a dos decimales
			DecimalFormat df = new DecimalFormat("0.00");

			labCVDCalculado.setText(" " + df.format(inv.getItems().get(app.getItems().getSelectedIndex() - 1).CVD()));
			labClaseRes.setText(inv.getItems().get(app.getItems().getSelectedIndex()-1).getClase()+"");

		} else {

			labCVDCalculado.setText("");

		}

		repaint();

	}

	@Override
	public void actionPerformed(ActionEvent e) {

		String command = e.getActionCommand();

		if (command.equals(GRAPHIC)) {

			if (app.getInventario() != null && app.getInventario().getWorkbook() != null)
				app.crearGraficaLineal();

		}

	}
}
