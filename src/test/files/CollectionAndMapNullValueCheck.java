import java.util.HashMap;
import java.util.ArrayList;

class CollectionAndMapNullValueCheck {
	  public void test() {
		  HashMap map = new HashMap<String,Object>();
		  ArrayList list = new ArrayList<Object>();
		    map.get("key");// Compliant
			map.get("key").toString(); // Noncompliant {{空指针隐患}}
			list.get(0).toString(); // Noncompliant {{空指针隐患}}
			list.get(0).equals(map); // Noncompliant {{空指针隐患}}
	    }
}
