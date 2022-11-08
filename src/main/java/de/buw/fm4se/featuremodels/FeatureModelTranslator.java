package de.buw.fm4se.featuremodels;

import java.util.List;

//import de.buw.fm4se.featuremodels.fm.FeatureModel;
import de.buw.fm4se.featuremodels.fm.*;

import static de.buw.fm4se.featuremodels.fm.GroupKind.*;

public class FeatureModelTranslator {

    private static String parent(Feature parent, Feature child) {
        //System.out.println("parent: (" + child.getName() + " -> " + parent.getName() + ")");

        return "(" + child.getName() + " -> " + parent.getName() + ")";
    }

    private static String mandatorySubfeature(Feature parent, Feature mandatory) {
        //System.out.println("mandatory: (" + parent.getName() + " -> " + mandatory.getName() + ") & (" + mandatory.getName() + " -> " + parent.getName() + ")");
        return "(" + parent.getName() + " -> " + mandatory.getName() + ") & (" + mandatory.getName() + " -> " + parent.getName() + ")";
    }
    private static String Xor(String featureName, List<Feature> children) {

        String result = featureName + " -> (";

        for (int i = 0; i < children.size(); ++i) {

            for (int j = 0; j < children.size(); ++j) {
                if (i != j) {
                    result += "!";
                }
                result += children.get(j).getName();

                if (j < children.size() - 1) {
                    result += " & ";

                }
            }
            if (i < children.size() - 1) {
                result += "| ";
            }
        }

        result += ") ";
        //System.out.println("Xor: " + result);

        return result;
    }


    private static String Or(String featureName, List<Feature> children) {
        String result = "(" + featureName + " -> (";

        for (int i = 0; i < children.size(); ++i) {


            result += children.get(i).getName();

            if (i < children.size() - 1) {
                result += " | ";
            }
        }
        result += "))";

        //System.out.println("Or: " + result);

        return result;
    }

    private static String CrossTreeConstraints(FeatureModel fm) {

        List<CrossTreeConstraint> ctc = fm.getConstraints();

        String result = "";

        for (int i = 0; i < ctc.size(); ++i){
            String evaluatedCtc = "(";

            if (ctc.get(i).getKind() == CrossTreeConstraint.Kind.EXCLUDES){
                evaluatedCtc += ctc.get(i).getLeft().getName() + " -> !" + ctc.get(i).getRight().getName();
            }else {
                // Kind == REQUIRES
                evaluatedCtc += ctc.get(i).getLeft().getName() + " -> " + ctc.get(i).getRight().getName();
            }


            evaluatedCtc += ")";

            result += evaluatedCtc;
            if (i < ctc.size()-1){
                result += " & ";
            }
        }

        //System.out.println("CrossTreeConstraints: " + result);

        return result;
    }

    public static String translateToFormula(FeatureModel fm) {

        // TODO implement a real translation

        List<Feature> allChildren = fm.getRoot().getChildren();
        Feature root = fm.getRoot();

        String result = "";

        //Or(root.getName(), allChildren);



        for (int i = 0; i < allChildren.size(); i++) {

            List<Feature> newChildren = allChildren.get(i).getChildren();

            GroupKind kind = allChildren.get(i).getChildGroupKind();;


            for (int j = 0; j < newChildren.size(); ++j) {
                //kind = newChildren.get(j).getChildGroupKind(); // assumed to be the same for all children

                if (newChildren.get(i).getChildGroupKind() == NONE) {
                    if (newChildren.get(i).isMandatory()) {
                        result += mandatorySubfeature(allChildren.get(i), newChildren.get(j));

                    } else {
                        result += parent(allChildren.get(i), newChildren.get(j));

                    }

                    if (j < newChildren.size() - 1) {
                        System.out.println("j: " + j);
                        result += " & ";
                    }
                }
            }

            if (kind == OR){
                result += " & " + Or(allChildren.get(i).getName(),newChildren);
            }else if(kind == XOR){
                result += " & " + Xor(allChildren.get(i).getName(),newChildren);
            }


            allChildren.addAll(newChildren);

            if (newChildren.size() > 0){
                result += " & ";
            }
        }
        result += CrossTreeConstraints(fm);

        System.out.println("result: " + result);
        return result;
    }
}
