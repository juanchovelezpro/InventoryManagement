package modelo;

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Inventario {

	/**
	 * Lista de items.
	 */
	private ArrayList<Item> items;
	private ArrayList<Double> volumenesPorcentajes;

	/**
	 * Archivo de excel.
	 */
	private XSSFWorkbook workbook;

	public Inventario() {

		items = new ArrayList<>();
		volumenesPorcentajes = new ArrayList<>();
		workbook = null;

	}

	public ArrayList<Item> getItems() {
		return items;
	}

	public void setItems(ArrayList<Item> items) {
		this.items = items;
	}

	public XSSFWorkbook getWorkbook() {
		return workbook;
	}

	public void setWorkbook(XSSFWorkbook workbook) {
		this.workbook = workbook;
	}

	public void calcularPorcentajesDeVolumenes() {

		double volumenTotal = 0.0;

		// Sumatoria de los volumenes de todos los items.
		for (int i = 0; i < items.size(); i++) {

			volumenTotal += items.get(i).volumen();

		}

		// Porcentajes de los volumenes de cada item. (Volumen de cada item dividido por
		// la sumatoria de los volumenes de todos los items).
		for (int i = 0; i < items.size(); i++) {

			double porcentaje = (items.get(i).volumen() / volumenTotal) * 100;

			volumenesPorcentajes.add(porcentaje);

		}

	}

	// Para calcular CVD
	public void obtenerItems() {

		XSSFSheet sheet = workbook.getSheetAt(0);

		Iterator<Row> rows = sheet.iterator();

		int x = 0;

		// Para empezar a obtener los datos desde la fila 3.
		while (x < 3) {

			rows.next();
			x++;

		}

		while (rows.hasNext()) {

			Row row = rows.next();

			Iterator<Cell> cells = row.cellIterator();

			int contadorCeldas = 0;
			Item item = new Item();

			while (cells.hasNext()) {

				Cell cell = cells.next();

				switch (contadorCeldas) {

				case 0:
					item.setCodigo((int) Double.parseDouble(cell.toString()));
					break;

				case 1:
					item.setDescripcion(cell.toString());
					break;

				default:
					item.getCantidades().add(Double.parseDouble(cell.toString()));
					break;

				}

				contadorCeldas++;

			}

			items.add(item);

		}

	}

	// Busca el item por el codigo.
	public Item buscarItem(int codigo) {

		Item item = null;

		for (int i = 0; i < items.size(); i++) {

			if (items.get(i).getCodigo() == codigo) {

				item = items.get(i);

			}

		}

		return item;

	}

	// Lee la segunda hoja del archivo excel para clasificar los items.
	public void itemsParaClasificacion() {

		XSSFSheet sheet = workbook.getSheetAt(1);

		Iterator<Row> rows = sheet.iterator();

		rows.next();

		while (rows.hasNext()) {

			Row row = rows.next();

			Iterator<Cell> cells = row.cellIterator();
			Item item = null;
			int contadorCeldas = 0;

			while (cells.hasNext()) {

				Cell cell = cells.next();

				switch (contadorCeldas) {

				case 0:

					item = buscarItem((int) Double.parseDouble(cell.toString()));

					break;

				case 1:

					item.getSalidasDeInventario().add((int) Double.parseDouble(cell.toString()));

					break;

				case 2:

					item.getCostosUnitarios().add(Double.parseDouble(cell.toString()));

					break;

				}

				contadorCeldas++;

			}

		}

	}

}
