package my.spring.siw.tud.myUtils;

import java.util.Iterator;
import java.util.List;

public class MyUtil {
	
	public static <T> void deleteFromList(List<T> list, T item) {
		Iterator<T> it = list.iterator();
		while(it.hasNext()) {
			T current = it.next();
			if(current.equals(item))
				it.remove();
		}
	}

}
