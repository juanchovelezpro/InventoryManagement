package modelo;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.stat.StatUtils;

public class Item implements Comparable<Item> {

	private int codigo;
	private String descripcion;
	private ArrayList<Double> cantidades;
	private ArrayList<Double> costosUnitarios;
	private ArrayList<Integer> salidasDeInventario;
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
		cantidades = new ArrayList<>();
		costosUnitarios = new ArrayList<>();
		salidasDeInventario = new ArrayList<>();

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
