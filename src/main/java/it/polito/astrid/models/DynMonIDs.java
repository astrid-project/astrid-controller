package it.polito.astrid.models;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DynMonIDs {
	
	public Map <String, String> setDID;
	 
		
		public void removeSetDID(String id,String name) {
					 setDID.remove(id, name);
		}


	public void addToSetDID(String id,String name) {
		setDID.put(id, name);
	}


		public DynMonIDs() {
			setDID = new HashMap();
		}
		
		
		
}
