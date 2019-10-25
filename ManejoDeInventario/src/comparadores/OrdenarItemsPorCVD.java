package comparadores;

import java.util.Comparator;
import modelo.Item;

public class OrdenarItemsPorCVD implements Comparator<Item>{

	@Override
	public int compare(Item uno, Item dos) {
		
		if (uno.getCvd() > dos.getCvd()) {

			return 1;

		} else if (uno.getCvd() < dos.getCvd()) {

			return -1;

		} else {

			return 0;

		}
	}

	
	
}
