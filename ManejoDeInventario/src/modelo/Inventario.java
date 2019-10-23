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

	/**
	 * Archivo de excel.
	 */
	private XSSFWorkbook workbook;

	public Inventario() {

		items = new ArrayList<>();
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
					item.setCodigo((int)Double.parseDouble(cell.toString()));
					break;

				case 1:
					item.setDescripcion(cell.toString());
					break;

				default:
					item.getCantidades().add((int)Double.parseDouble(cell.toString()));
					break;

				}

				contadorCeldas++;

			}
			
			items.add(item);

		}

	}

}
