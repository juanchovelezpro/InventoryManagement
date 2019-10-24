package comparadores;

import java.util.Comparator;

import modelo.Item;

public class OrdenarItemsPorVolumenPorcentaje implements Comparator<Item> {

	//Organiza de mayor a menor los items por volumen %
	@Override
	public int compare(Item itemUno, Item itemDos) {

		if (itemUno.getVolumenPorcentaje() >= itemDos.getVolumenPorcentaje()) {

			return -1;

		} else {

			return 1;

		}
	}

}
