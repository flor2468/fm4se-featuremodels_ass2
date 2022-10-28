package de.buw.fm4se.featuremodels;

import java.util.List;

//import de.buw.fm4se.featuremodels.fm.FeatureModel;
import de.buw.fm4se.featuremodels.fm.*;

public class FeatureModelTranslator {
  public static String translateToFormula(FeatureModel fm) {
    
    // TODO implement a real translation
	  
	List<Feature> allChildren = fm.getRoot().getChildren();
	String result = fm.getRoot().getName();
	
	for (int i = 0; i < allChildren.size(); i++) {
		result += " & (" + allChildren.get(i).getName() + " -> " + fm.getRoot().getName() + ")";
		
		if (allChildren.get(i).isMandatory()) {
			result += " & (" + fm.getRoot().getName() + " -> " + allChildren.get(i).getName() + ")";
		}
	}
	
	System.out.println("TEST: " + result);
	    
	return result;
  }
}
