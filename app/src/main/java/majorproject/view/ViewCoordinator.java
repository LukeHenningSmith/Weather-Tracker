package majorproject.view;

import majorproject.view.window.*;

import majorproject.model.pojo.*;
import majorproject.presenter.WeatherPresenter;

import javafx.stage.Stage;

import java.util.List;

/**
* Holds the primary scene. Creates and stores the view windows.
* Is responsible for coordinating what scene is being displayed on the primary stage.
*/
public class ViewCoordinator {

  private final Stage primaryStage;

  private MainWindow mainWindow;
  private SplashScreenWindow splashScreenWindow;
  private AboutPageWindow aboutPageWindow;

  /**
  * Simple constructor to set the primary stage of the view package.
  * @param primaryStage The primary stage of this javaFX view package.
  */
  public ViewCoordinator(Stage primaryStage){
    this.primaryStage=primaryStage;
  }

  /**
  * Displays the main window. Responsible for creating it if it doesn't already exist.
  * @param weatherPresenter The presenter the MainWindow needs to relay user input to.
  * @param locations The locations MainWindow is able to interpret for weather requests.
  * @return Returns the MainWindow instance the application is using.
  */
  public MainWindow displayMainWindow(WeatherPresenter weatherPresenter, List<CityLocation> locations){
    //makes the mainwindow if it is not initialised.
    if(this.mainWindow==null){
      this.mainWindow = new MainWindow(weatherPresenter,locations);
    }
    //set the scene
      primaryStage.hide();
      primaryStage.setScene(this.mainWindow.getScene());
      primaryStage.show();
      return this.mainWindow;
  }

  /**
  * Displays the about window. Responsible for creating it if it doesn't already exist.
  * @param weatherPresenter The presenter the AboutPageWindow needs to relay user input to.
  * @param aboutInformation The about information the AboutPageWindow will display.
  * @return Returns the AboutPageWindow instance the application is using.
  */
  public AboutPageWindow displayAboutWindow(WeatherPresenter weatherPresenter, About aboutInformation){
    //makes the aboutwindow if it is not initialised.
    if(this.aboutPageWindow==null){
      this.aboutPageWindow = new AboutPageWindow(weatherPresenter,aboutInformation);
    }
    //set the scene
      primaryStage.hide();
      primaryStage.setScene(this.aboutPageWindow.getScene());
      primaryStage.show();
      return this.aboutPageWindow;
  }

  /**
  * Displays the splash screen window. Responsible for creating it if it doesn't already exist.
  * @param weatherPresenter The presenter the SplashScreenWindow needs to communicate need for view change.
  * @return Returns the SplashScreenWindow instance the application is using.
  */
  public SplashScreenWindow displaySplashScreenWindow(WeatherPresenter weatherPresenter){
    //makes the splashscreen if it is not initialised.
    if(this.splashScreenWindow==null){
      this.splashScreenWindow = new SplashScreenWindow(weatherPresenter);
    }
    primaryStage.hide();
    primaryStage.setScene(this.splashScreenWindow.getScene());
    primaryStage.show();
    return this.splashScreenWindow;
  }

}
