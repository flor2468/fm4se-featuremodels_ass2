package de.buw.fm4se.featuremodels;

import java.util.ArrayList;
import java.util.List;

import de.buw.fm4se.featuremodels.exec.LimbooleExecutor;
import de.buw.fm4se.featuremodels.fm.Feature;
import de.buw.fm4se.featuremodels.fm.FeatureModel;

/**
 * This code needs to be implemented by translating FMs to input for Limboole
 * and interpreting the output
 */
public class FeatureModelAnalyzer {

    public static boolean checkConsistent(String formula) {

        String result;
        try {
            result = LimbooleExecutor.runLimboole(formula, true);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        if (result.contains("UNSATISFIABLE")) {
            return false;
        }
        return true;
    }

    public static boolean checkConsistent(FeatureModel fm) {
        String formula = FeatureModelTranslator.translateToFormula(fm);

        String result;
        try {
            result = LimbooleExecutor.runLimboole(formula, true);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        if (result.contains("UNSATISFIABLE")) {
            return false;
        }
        return true;
    }

    public static boolean isDead(FeatureModel fm, String feature) {


        boolean consistent = true;

        String formula = FeatureModelTranslator.translateToFormula(fm);

        formula += " & !" + feature;
        System.out.println("formula: " + formula);
        consistent &= checkConsistent(formula);

        System.out.println("Consisten: " + consistent);
        return consistent;
    }

    public static List<String> deadFeatureNames(FeatureModel fm) {
        List<String> deadFeatures = new ArrayList<>();


        List<Feature> allChildren = fm.getRoot().getChildren();

        String formula = FeatureModelTranslator.translateToFormula(fm);
        boolean badFormula = !checkConsistent(formula);

        if (badFormula){
            deadFeatures.add(fm.getRoot().getName());
        }

        for (int i = 0; i < allChildren.size(); i++) {

            if (!deadFeatures.contains(allChildren.get(i).getName())) {

                if (badFormula || isDead(fm, allChildren.get(i).getName())) {
                    deadFeatures.add(allChildren.get(i).getName());
                }
            }
            allChildren.addAll(allChildren.get(i).getChildren());
        }
        System.out.println(deadFeatures.toString());

        return deadFeatures;
    }

    public static List<String> mandatoryFeatureNames(FeatureModel fm) {
        List<String> mandatoryFeatures = new ArrayList<>();

        List<Feature> allChildren = fm.getRoot().getChildren();

        for (int i = 0; i < allChildren.size(); i++) {

            if (!mandatoryFeatures.contains(allChildren.get(i).getName()) && allChildren.get(i).isMandatory()) {
                mandatoryFeatures.add(allChildren.get(i).getName());
            }

            allChildren.addAll(allChildren.get(i).getChildren());
        }

        return mandatoryFeatures;
    }

}
