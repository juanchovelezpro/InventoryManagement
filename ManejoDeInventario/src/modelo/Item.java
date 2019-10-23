package modelo;

import java.util.ArrayList;

public class Item {

	private int codigo;
	private String descripcion;
	private ArrayList<Integer> cantidades;
	

	public Item(int codigo, String descripcion) {

		this.codigo = codigo;
		this.descripcion = descripcion;
		cantidades = new ArrayList<>();

	}

	public Item() {

		cantidades = new ArrayList<>();

	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public ArrayList<Integer> getCantidades() {
		return cantidades;
	}

	public void setCantidades(ArrayList<Integer> cantidades) {
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
	
	for(int i=0; i<cantidades.size();i++) {
		
	promedio+=cantidades.get(i);	
		
	}
	
	promedio /= cantidades.size();
	
	
	
	cvd = desviacion/promedio;
	
	return cvd;
	
	}
	
	

	@Override
	public String toString() {

		return "Codigo:" + codigo + "\n" + "Descripcion:" + descripcion + "\n" + cantidades;

	}

}
