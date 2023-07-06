package majorproject.view.component;

import majorproject.presenter.*;

import javafx.scene.control.ProgressIndicator;
import javafx.scene.Node;

/**
* A class containing JavaFX ProgressIndicator to encapsulate all its associated methods and construction.
*/
public class WeatherProgressIndicator {

  private WeatherPresenter presenter;
  private ProgressIndicator progressIndicator;
  private Node progressIndicatorNode;

  /**
  * Constructs the ProgressIndicator object.
  * @param presenter The WeatherPresenter this class will use to notify of user input.
  */
  public WeatherProgressIndicator(WeatherPresenter presenter){
    this.presenter=presenter;
    setup();
  }

  /**
  * Getter method for the contained ProgressIndicator
  * @return Returns the ProgressIndicator contained within the class.
  */
  public ProgressIndicator getProgressIndicator(){
    return this.progressIndicator;
  }

  /**
  * Private method to setup the progress indicator and its node.
  */
  private void setup(){
    this.progressIndicator = new ProgressIndicator();
    this.progressIndicator.setId("progressIndicator");
    progressIndicator.setPrefWidth(35);
    progressIndicator.setPrefHeight(35);

    this.progressIndicatorNode = (Node) progressIndicator;
    progressIndicatorNode.setVisible(false);
  }

  /**
  * Method to modify the loading state of the progress indicator contained in this class.
  * @param loadingState The loading state to switch to. True for loading indicator shown, false for not shown.
  */
  public void notifyOfLoadingStateChange(boolean loadingState){
    //get the loading state from engine.
    progressIndicatorNode.setVisible(loadingState);
  }

}
