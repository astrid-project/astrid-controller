package it.polito.astrid.models;

import java.util.HashSet;
import java.util.Set;

public class DynMonIDs {
	
	public Set<String> setDID;
	 
		
		public void removeSetDID(String idNo) {
					 setDID.remove(idNo);
		}


	public void addToSetDID(String idNo) {
		setDID.add(idNo);
	}


		public DynMonIDs() {
			 setDID = new HashSet();
		}
		
		
		
}
