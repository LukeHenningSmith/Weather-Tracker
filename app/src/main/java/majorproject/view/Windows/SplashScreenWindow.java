package majorproject.view.window;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javafx.scene.control.ProgressBar;
import javafx.util.Duration;
import javafx.stage.*;
import javafx.event.*;
import javafx.scene.*;
import javafx.animation.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.layout.*;
import javafx.scene.control.Button;
import javafx.geometry.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import majorproject.presenter.WeatherPresenter;

/**
* Class containing all the scene information for the Splash screen window.
*/
public class SplashScreenWindow {

  private static final int WIDTH = 825;
  private static final int HEIGHT = 570;

  private Scene scene;

  private WeatherPresenter weatherPresenter;

  /**
  * Constructs and contains the splash screen scene.
  * @param weatherPresenter The WeatherPresenter this class will use to notify of user input.
  */
  public SplashScreenWindow(WeatherPresenter weatherPresenter){
    this.weatherPresenter=weatherPresenter;
    constructSplashScene();
  }

  /**
  * Simple getter method for the scene this window created.
  * @return The Scene this class made.
  */
  public Scene getScene(){
    return this.scene;
  }

  /**
  * Private method to construct the splash screen, add its javafx components and show the stage.
  */
  private void constructSplashScene(){
    BorderPane borderPane = new BorderPane();

    Scene newScene = new Scene(borderPane, WIDTH, HEIGHT);
    newScene.getStylesheets().add(this.getClass().getResource("/stylesheets/weather-tracker-style.css").toExternalForm());
    Image image=null;
    try {
      image = new Image(new FileInputStream("src/main/resources/splash-screen-3.jpg"));
    } catch (FileNotFoundException e){
      System.out.println("File not found exception");
    }

    //Setting the image view
    ImageView imageView = new ImageView(image);

    imageView.setFitHeight(HEIGHT-20);
    imageView.setFitWidth(WIDTH);

    imageView.setPreserveRatio(true);

    ProgressBar progress = new ProgressBar();
    progress.setId("progressBar");
    Timeline timeline = new Timeline(
    new KeyFrame(Duration.ZERO, new KeyValue(progress.progressProperty(), 0)),
    new KeyFrame(Duration.seconds(3), e-> {
      weatherPresenter.displayMainWindow();
    }, new KeyValue(progress.progressProperty(), 1))
    );
    timeline.setCycleCount(1);
    timeline.play();

    progress.setPrefWidth(300);
    // create a tile pane
    VBox vbox = new VBox(0);
    vbox.setAlignment(Pos.CENTER);
    // add button
    vbox.getChildren().addAll(imageView,progress);

    borderPane.setCenter(vbox);
    this.scene=newScene;
  }
}
