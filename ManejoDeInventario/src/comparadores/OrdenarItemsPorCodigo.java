package comparadores;

import java.util.Comparator;

import modelo.Item;

public class OrdenarItemsPorCodigo implements Comparator<Item> {

	@Override
	public int compare(Item uno, Item dos) {

		if (uno.getCodigo() > dos.getCodigo()) {

			return 1;

		} else if (uno.getCodigo() < dos.getCodigo()) {

			return -1;

		} else {

			return 0;

		}
	}

}
