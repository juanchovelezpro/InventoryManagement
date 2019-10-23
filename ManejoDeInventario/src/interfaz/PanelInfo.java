package interfaz;

import java.awt.*;
import java.text.DecimalFormat;

import javax.swing.*;
import modelo.*;

public class PanelInfo extends JPanel {

	/**
	 * Labels para el CVD
	 */
	private JLabel labCVD;
	private JLabel labCVDCalculado;

	private Application app;

	public PanelInfo(Application app) {

		this.app = app;

		setLayout(null);
		settingComponents();

	}

	public void settingComponents() {

		// Label CVD
		labCVD = new JLabel("CVD:");
		labCVD.setFont(new Font("Garamond", 1, 20));
		labCVD.setBounds(0, 50, 100, 20);

		// Label CVD Calculado
		labCVDCalculado = new JLabel();
		labCVDCalculado.setBounds(100, 50, 100, 20);
		labCVDCalculado.setFont(new Font("Garamond", 1, 20));

		add(labCVD);
		add(labCVDCalculado);

	}

	@Override
	public void paintComponent(Graphics g) {

		super.paintComponent(g);

		if (app.getItems().getSelectedIndex() != 0) {

			Inventario inv = app.getInventario();

			//Formato a dos decimales
			DecimalFormat df = new DecimalFormat("#.00");
			
			labCVDCalculado.setText(" " + df.format(inv.getItems().get(app.getItems().getSelectedIndex() - 1).CVD()));

		}else {
			
			labCVDCalculado.setText("");
			
		}

		repaint();

	}

}
