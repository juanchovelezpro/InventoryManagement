package modelo;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import comparadores.OrdenarItemsPorVolumenPorcentaje;

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

	public ArrayList<Item> itemsOrganizadosPorVolumenPorcentaje() {

		ArrayList<Item> organizados = items;

		Collections.sort(organizados, new OrdenarItemsPorVolumenPorcentaje());

		return organizados;

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

			items.get(i).setVolumenPorcentaje(porcentaje);

		}

	}

	public ArrayList<Double> getVolumenesAcumulados() {

		ArrayList<Item> itemsOrganizados = itemsOrganizadosPorVolumenPorcentaje();

		ArrayList<Double> volumenesAcumulados = new ArrayList<>();

		volumenesAcumulados.add(itemsOrganizados.get(0).getVolumenPorcentaje());

		for (int i = 1; i < itemsOrganizados.size(); i++) {

			volumenesAcumulados.add(volumenesAcumulados.get(i - 1) + itemsOrganizados.get(i).getVolumenPorcentaje());

		}

		return volumenesAcumulados;

	}

	// Asigna la clase a los items. (A, B o C)
	public void asignarClasesItems() {

		ArrayList<Item> itemsOrganizados = itemsOrganizadosPorVolumenPorcentaje();

		ArrayList<Double> volumenesAcumulados = getVolumenesAcumulados();

		// Sacar rangos para asignar clases a los items.

		int indexAFinal = 0;
		int indexB = 0;
		int indexBFinal = 0;
		int indexC = 0;

		boolean aDefinido = false;
		boolean cDefinido = false;

		for (int i = 0; i < volumenesAcumulados.size(); i++) {

			if (volumenesAcumulados.get(i) > 80.0 && !aDefinido) {

				indexAFinal = i - 1;
				indexB = i;

				aDefinido = true;

			}

			if (volumenesAcumulados.get(i) >= 95.0 && !cDefinido) {

				indexC = i;
				indexBFinal = i - 1;

				cDefinido = true;

			}

		}

		// Asignar clases A
		for (int i = 0; i <= indexAFinal; i++) {

			itemsOrganizados.get(i).setClase('A');

		}

		// Asignar clases B
		for (int i = indexB; i <= indexBFinal; i++) {

			itemsOrganizados.get(i).setClase('B');

		}

		// Asignar clases C
		for (int i = indexC; i < itemsOrganizados.size(); i++) {

			itemsOrganizados.get(i).setClase('C');

		}

		items = itemsOrganizados;

	}

	public void obtenerCantidadesPorPeriodo() {

//		for (int i = 0; i < items.size(); i++) {
//
//			for (int j = 0; j < items.get(i).getFechas().size(); j++) {
//
				Calendar cal = Calendar.getInstance();
				Date date = items.get(0).getFechas().get(0);
//
//				cal.setTime(date);
				
				System.out.println(cal.get(Calendar.MONTH));

//			}
//
//		}

	}

	// Lee la primera hoja del excel para calcular CVD y sacar las graficas de
	// cantidad de item por periodo.

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
		boolean encontrado = false;

		for (int i = 0; i < items.size() && !encontrado; i++) {

			if (items.get(i).getCodigo() == codigo) {

				item = items.get(i);
				encontrado = true;

			}

		}

		return item;

	}

	public boolean existe(int codigo) {

		boolean si = false;
		boolean stop = false;

		for (int i = 0; i < items.size() && !stop; i++) {

			if (items.get(i).getCodigo() == codigo) {

				si = true;
				stop = true;

			}

		}

		return si;

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

	// Lectura de todos los datos (Base de datos completa sin modificacion)
	public void leerArchivo() {

		XSSFSheet sheet = workbook.getSheetAt(0);

		Iterator<Row> rows = sheet.iterator();
		rows.next();

		while (rows.hasNext()) {

			Row row = rows.next();
			boolean agregar = false;

			Iterator<Cell> cells = row.cellIterator();

			int contadorCeldas = 0;
			Item item = new Item();

			while (cells.hasNext()) {

				Cell cell = cells.next();

				switch (contadorCeldas) {

				case 0:
//					item.getOrdenInterno().add(cell.toString());
					break;

				case 1:

					int codigo = (int) Double.parseDouble(cell.toString());

					if (existe(codigo)) {

						item = buscarItem(codigo);

					} else {

						agregar = true;
						item.setCodigo(codigo);

					}

					break;

				case 2:
					item.setReferencia(cell.toString());
					break;

				case 3:
					item.setDescripcion(cell.toString());
					break;

				case 4:
					item.getBodegas().add(cell.toString());
					break;

				case 5:
					item.getUbicaciones().add(cell.toString());
					break;

				case 6:
					item.getEntradas().add(Double.parseDouble(cell.toString()));
					break;

				case 7:

					Date date = cell.getDateCellValue();

					item.getFechas().add(date);

					break;

				case 8:
					item.getSalidasDeInventario().add((int) Double.parseDouble((cell.toString())));
					break;

				case 9:
					item.getNetosInventario().add((int) Double.parseDouble(cell.toString()));
					break;

				case 10:
					item.getCostosEntradas().add(Double.parseDouble(cell.toString()));
					break;

				case 11:
					item.getCostosSalidas().add(Double.parseDouble(cell.toString()));
					break;

				case 12:
					item.getCostosNetos().add(Double.parseDouble(cell.toString()));
					break;

				case 13:
					item.getCostosUnitarios().add(Double.parseDouble(cell.toString()));
					break;

				case 14:
					item.getcCostos().add(cell.toString());
					break;

				case 15:
					item.getDescCCostos().add(cell.toString());
					break;

				case 16:
					item.getCostosUnitariosReExpresion().add(Double.parseDouble(cell.toString()));
					break;
				}

				contadorCeldas++;

			}

			if (agregar) {
				items.add(item);
			}
		}

	}

}
