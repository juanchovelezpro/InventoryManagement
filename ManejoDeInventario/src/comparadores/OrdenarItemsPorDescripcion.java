package comparadores;

import java.util.Comparator;

import modelo.Item;

public class OrdenarItemsPorDescripcion implements Comparator<Item> {

	@Override
	public int compare(Item uno, Item dos) {

		return uno.getDescripcion().compareTo(dos.getDescripcion());
	}

}
