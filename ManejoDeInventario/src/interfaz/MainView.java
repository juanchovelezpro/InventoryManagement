package interfaz;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.poi.*;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbookFactory;

public class MainView extends JFrame implements ActionListener {

	public static final String CHOOSE = "CHOOSE";

	private JComboBox<String> items;
	private JFileChooser fileChooser;
	private JButton butChooser;
	private XSSFWorkbook wb;

	public MainView() {

		setTitle("Manejo de Inventarios");
		setSize(800, 800);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(null);

		settingsComponents();

		setVisible(true);

	}

	public void settingsComponents() {

		// Donde se colocan los items
		items = new JComboBox<String>();
		items.setBounds(250, 200, 200, 25);

		butChooser = new JButton("Cargar Archivo");
		butChooser.setBounds(250, 50, 200, 30);
		butChooser.setActionCommand(CHOOSE);
		butChooser.addActionListener(this);

		add(items);
		add(butChooser);

	}

	public XSSFWorkbook getWorkBook() {

		return wb;

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
				fs = new FileInputStream(fileChooser.getSelectedFile());
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			try {
				wb = new XSSFWorkbook(fs);

			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}
	}

	public static void main(String[] args) throws IOException, InvalidFormatException {

		MainView main = new MainView();

	}

}
