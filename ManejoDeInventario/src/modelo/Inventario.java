package modelo;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import comparadores.OrdenarItemsPorCVD;
import comparadores.OrdenarItemsPorCodigo;
import comparadores.OrdenarItemsPorDescripcion;
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
	private ArrayList<Integer> years;

	public Inventario() {

		items = new ArrayList<>();
		years = new ArrayList<>();
		workbook = null;

	}

	public void fillYears() {

		Calendar cal = Calendar.getInstance();

		for (int i = 0; i < items.size(); i++) {

			for (int j = 0; j < items.get(i).getFechas().size(); j++) {

				Date date = items.get(i).getFechas().get(j);

				cal.setTime(date);

				if (!years.contains(cal.get(Calendar.YEAR))) {

					years.add(cal.get(Calendar.YEAR));

				}

			}

		}

		Collections.sort(years);

	}

	public int encontrarUltimoPeriodo() {

		fillYears();

		int periodo = 0;
		Calendar cal = Calendar.getInstance();
		int lastYear = years.get(years.size() - 1);

		for (int i = 0; i < items.size(); i++) {

			for (int j = 0; j < items.get(i).getFechas().size(); j++) {

				Date date = items.get(i).getFechas().get(j);
				cal.setTime(date);

				if (cal.get(Calendar.YEAR) == lastYear) {

					if (cal.get(Calendar.MONTH) > periodo) {

						periodo = cal.get(Calendar.MONTH);

					}

				}

			}

		}

		return periodo;

	}

	public ArrayList<Integer> getYears() {
		return years;
	}

	public void setYears(ArrayList<Integer> years) {
		this.years = years;
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

	// Retorna los items organizados por porcentaje de volumen
	public ArrayList<Item> itemsOrganizadosPorVolumenPorcentaje() {

		ArrayList<Item> organizados = items;

		Collections.sort(organizados, new OrdenarItemsPorVolumenPorcentaje());

		return organizados;

	}

	public void ordenarItemsPorCodigo() {

		Collections.sort(items, new OrdenarItemsPorCodigo());

	}

	public void ordenarItemsPorDescripcion() {

		Collections.sort(items, new OrdenarItemsPorDescripcion());

	}

	public void ordenarItemsPorCVD() {

		Collections.sort(items, new OrdenarItemsPorCVD());

	}

	public void ordenarItemsPorClase() {

		Collections.sort(items, new OrdenarItemsPorVolumenPorcentaje());

	}

	// Calcula el porcentaje de volumen de todos los items.
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

	// Calcula los volumenes acumulados de los items.
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

	// Calcula todos los CVD de los items y los asigna al item correspondiente.
	// Además se define el stock y el patron de demanda.
	public void asignarVariablesDeAnalisis() {

		for (int i = 0; i < items.size(); i++) {

			items.get(i).setCvd(items.get(i).CVD());
			items.get(i).definirStock();
			items.get(i).definirPatronDemanda();

		}

	}

	// Metodo para obtener las cantidades de un item en el inventario por periodo
	// (Periodo: meses)
	public void obtenerCantidadesPorPeriodo(int year, int month) {

		for (int i = 0; i < items.size(); i++) {

			int cantidadesMes = 0;

			for (int j = 0; j < items.get(i).getFechas().size(); j++) {

				Calendar cal = Calendar.getInstance();

				Date date = items.get(i).getFechas().get(j);
				cal.setTime(date);

				if (cal.get(Calendar.YEAR) == year) {

					if (cal.get(Calendar.MONTH) == month) {

						cantidadesMes += items.get(i).getSalidasDeInventario().get(j);

					}

				}
			}

			items.get(i).getCantidades().add(cantidadesMes);

		}

	}

	// Método para calcular cantidades de todos los items en el inventario por
	// periodos.
	public void fillCantidadesItemPorPeriodo() {

		int ultimo = encontrarUltimoPeriodo();

		for (int i = 0; i < years.size(); i++) {

			if (years.get(i) != years.get(years.size() - 1)) {
				for (int j = 0; j <= 11; j++) {

					obtenerCantidadesPorPeriodo(years.get(i), j);

				}
			} else {

				for (int k = 0; k <= ultimo; k++) {

					obtenerCantidadesPorPeriodo(years.get(i), k);

				}

			}
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
