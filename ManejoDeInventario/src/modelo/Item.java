package modelo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.math3.stat.StatUtils;

public class Item implements Comparable<Item> {

	private ArrayList<String> ordenesInterno;
	private int codigo;
	private String referencia;
	private String descripcion;
	private ArrayList<String> bodegas;
	private ArrayList<String> ubicaciones;
	private ArrayList<Double> entradas;
	private ArrayList<Date> fechas;
	private ArrayList<Integer> salidasDeInventario;
	private ArrayList<Integer> netosInventario;
	private ArrayList<Double> costosEntradas;
	private ArrayList<Double> costosSalidas;
	private ArrayList<Double> costosNetos;
	private ArrayList<Double> costosUnitarios;
	private ArrayList<String> cCostos;
	private ArrayList<String> descCCostos;
	private ArrayList<Double> costosUnitariosReExpresion;

	private ArrayList<Double> cantidades;

	private double volumenPorcentaje;

	private char clase;

	public Item(int codigo, String descripcion) {

		this.codigo = codigo;
		this.descripcion = descripcion;
		cantidades = new ArrayList<>();

	}

	public Item() {

		codigo = 0;
		descripcion = "";
		clase = ' ';
		volumenPorcentaje = 0.0;
		referencia = "";

		ordenesInterno = new ArrayList<>();
		bodegas = new ArrayList<>();
		ubicaciones = new ArrayList<>();
		entradas = new ArrayList<>();
		fechas = new ArrayList<>();
		salidasDeInventario = new ArrayList<>();
		netosInventario = new ArrayList<>();
		costosEntradas = new ArrayList<>();
		costosSalidas = new ArrayList<>();
		costosNetos = new ArrayList<>();
		costosUnitarios = new ArrayList<>();
		cCostos = new ArrayList<>();
		descCCostos = new ArrayList<>();
		costosUnitariosReExpresion = new ArrayList<>();

		cantidades = new ArrayList<>();

	}

	public ArrayList<String> getOrdenInterno() {
		return ordenesInterno;
	}

	public void setOrdenInterno(ArrayList<String> ordenesInterno) {
		this.ordenesInterno = ordenesInterno;
	}

	public String getReferencia() {
		return referencia;
	}

	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}

	public ArrayList<String> getBodegas() {
		return bodegas;
	}

	public void setBodegas(ArrayList<String> bodegas) {
		this.bodegas = bodegas;
	}

	public ArrayList<String> getUbicaciones() {
		return ubicaciones;
	}

	public void setUbicaciones(ArrayList<String> ubicaciones) {
		this.ubicaciones = ubicaciones;
	}

	public ArrayList<Double> getEntradas() {
		return entradas;
	}

	public void setEntradas(ArrayList<Double> entradas) {
		this.entradas = entradas;
	}

	public ArrayList<Date> getFechas() {
		return fechas;
	}

	public void setFechas(ArrayList<Date> fechas) {
		this.fechas = fechas;
	}

	public ArrayList<Integer> getNetosInventario() {
		return netosInventario;
	}

	public void setNetosInventario(ArrayList<Integer> netosInventario) {
		this.netosInventario = netosInventario;
	}

	public ArrayList<Double> getCostosEntradas() {
		return costosEntradas;
	}

	public void setCostosEntradas(ArrayList<Double> costosEntradas) {
		this.costosEntradas = costosEntradas;
	}

	public ArrayList<Double> getCostosSalidas() {
		return costosSalidas;
	}

	public void setCostosSalidas(ArrayList<Double> costosSalidas) {
		this.costosSalidas = costosSalidas;
	}

	public ArrayList<Double> getCostosNetos() {
		return costosNetos;
	}

	public void setCostosNetos(ArrayList<Double> costosNetos) {
		this.costosNetos = costosNetos;
	}

	public ArrayList<String> getcCostos() {
		return cCostos;
	}

	public void setcCostos(ArrayList<String> cCostos) {
		this.cCostos = cCostos;
	}

	public ArrayList<String> getDescCCostos() {
		return descCCostos;
	}

	public void setDescCCostos(ArrayList<String> descCCostos) {
		this.descCCostos = descCCostos;
	}

	public ArrayList<Double> getCostosUnitariosReExpresion() {
		return costosUnitariosReExpresion;
	}

	public void setCostosUnitariosReExpresion(ArrayList<Double> costosUnitariosReExpresion) {
		this.costosUnitariosReExpresion = costosUnitariosReExpresion;
	}

	public ArrayList<Double> getCostosUnitarios() {
		return costosUnitarios;
	}

	public void setCostosUnitarios(ArrayList<Double> costosUnitarios) {
		this.costosUnitarios = costosUnitarios;
	}

	public ArrayList<Integer> getSalidasDeInventario() {
		return salidasDeInventario;
	}

	public void setSalidasDeInventario(ArrayList<Integer> salidasDeInventario) {
		this.salidasDeInventario = salidasDeInventario;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public ArrayList<Double> getCantidades() {
		return cantidades;
	}

	public void setCantidades(ArrayList<Double> cantidades) {
		this.cantidades = cantidades;
	}

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public double CVD() {

		double cvd = 0.0;
		double promedio = 0.0;
		double desviacion = 0.0;

		for (int i = 0; i < cantidades.size(); i++) {

			promedio += cantidades.get(i);

		}

		promedio /= cantidades.size();

		desviacion = Math.sqrt(StatUtils.variance(getCantidadesDouble()));

		cvd = desviacion / promedio;

		return cvd;

	}

	public double volumen() {

		double volumen = 0.0;

		for (int i = 0; i < salidasDeInventario.size(); i++) {

			volumen += salidasDeInventario.get(i) * costosUnitarios.get(i);

		}

		return volumen;

	}

	public char getClase() {
		return clase;
	}

	public void setClase(char clase) {
		this.clase = clase;
	}

	public double getVolumenPorcentaje() {
		return volumenPorcentaje;
	}

	public void setVolumenPorcentaje(double volumenPorcentaje) {
		this.volumenPorcentaje = volumenPorcentaje;
	}

	public double[] getCantidadesDouble() {

		double[] c = new double[cantidades.size()];

		for (int i = 0; i < cantidades.size(); i++) {

			c[i] = cantidades.get(i);

		}

		return c;

	}

	@Override
	public String toString() {

		return "Codigo: " + codigo + "| V:  " + String.format("%.2f", volumen()) + "| V%: "
				+ String.format("%.2f", getVolumenPorcentaje());

	}

	@Override
	public int compareTo(Item item) {

		int value = 0;

		if (this.codigo >= item.getCodigo()) {

			value = 1;

		} else {

			value = -1;

		}

		return value;
	}

}
