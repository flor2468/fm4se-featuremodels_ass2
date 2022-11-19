package de.buw.fm4se.featuremodels;

import de.buw.fm4se.featuremodels.fm.CrossTreeConstraint;
import de.buw.fm4se.featuremodels.fm.Feature;
import de.buw.fm4se.featuremodels.fm.FeatureModel;
import de.buw.fm4se.featuremodels.fm.GroupKind;

/**
 * class to create some simple feature models
 *
 */
public class ExampleFmCreator {

  public static FeatureModel getEshopFm() {
    FeatureModel m = new FeatureModel();
    m.setRoot(new Feature("eshop"));
    return m;
  }

  public static FeatureModel getSimpleFm() {
    FeatureModel m = new FeatureModel();

    Feature car = new Feature("car");
    m.setRoot(car);

    Feature motor = car.addChild("motor", true);

    motor.setChildGroupKind(GroupKind.XOR);
    motor.addChild("gasoline", false);
    Feature electric = motor.addChild("electric", false);

    Feature comfort = car.addChild("comfort", false);
    comfort.setChildGroupKind(GroupKind.OR);
    Feature heating = comfort.addChild("heating", false);
    comfort.addChild("entertainment", false);

    m.addConstraint(new CrossTreeConstraint(electric, CrossTreeConstraint.Kind.REQUIRES, heating));

    return m;
  }
  public static FeatureModel getComplexFm() {
    // Feature Model with additional Level

    FeatureModel m = new FeatureModel();

    Feature car = new Feature("car");
    m.setRoot(car);

    Feature motor = car.addChild("motor", true);

    motor.setChildGroupKind(GroupKind.XOR);
    motor.addChild("gasoline", false);
    Feature electric = motor.addChild("electric", false);

    Feature comfort = car.addChild("comfort", false);
    comfort.setChildGroupKind(GroupKind.OR);
    Feature heating = comfort.addChild("heating", false);
    comfort.addChild("entertainment", false);



    Feature comfort_child = comfort.addChild("comfort_child", false);
    comfort_child.setChildGroupKind(GroupKind.OR);
    Feature comfort_child_1 = comfort_child.addChild("comfort_child_1", false);
    comfort_child.addChild("comfort_child_2", false);

    m.addConstraint(new CrossTreeConstraint(electric, CrossTreeConstraint.Kind.REQUIRES, heating));

    return m;
  }
  /**
   * constructs a FM where the mandatory child excludes the root
   * @return
   */
  public static FeatureModel getBadFm() {
    FeatureModel m = new FeatureModel();

    Feature car = new Feature("car");
    m.setRoot(car);

    Feature motor = car.addChild("motor", true);

    m.addConstraint(new CrossTreeConstraint(motor, CrossTreeConstraint.Kind.EXCLUDES, car));

    return m;
  }

}
