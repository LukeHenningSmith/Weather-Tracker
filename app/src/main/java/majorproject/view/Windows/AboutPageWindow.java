package majorproject.view.window;

import majorproject.model.pojo.About;
import majorproject.presenter.WeatherPresenter;

import javafx.scene.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.*;
import javafx.event.*;
import javafx.scene.layout.*;
import javafx.scene.control.Button;
import javafx.geometry.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javafx.scene.input.MouseEvent;

/**
* Class containing all the scene information for the About page.
*/
public class AboutPageWindow {

  private static final int WIDTH = 1280;
  private static final int HEIGHT = 720;

  private WeatherPresenter weatherPresenter;

  private Scene scene;

  /**
  * Constructs the about page given an About page object of information.
  * @param weatherPresenter The WeatherPresenter this class will use to notify of user input.
  * @param aboutInformation The About information to be shown in this class.
  */
  public AboutPageWindow(WeatherPresenter weatherPresenter,About aboutInformation){
    this.weatherPresenter=weatherPresenter;
    constructAboutPage(aboutInformation);
  }

  /**
  * Simple getter method for the scene this window created.
  * @return The Scene this class made.
  */
  public Scene getScene(){
    return this.scene;
  }

  /**
  * Private method to create About page Stage, add its JavaFX content, and display it.
  * @param aboutInformation The information to be contained in this window.
  */
  private void constructAboutPage(About aboutInformation){
    BorderPane borderPane = new BorderPane();
    borderPane.setId("about");
    Scene newScene = new Scene(borderPane, WIDTH, HEIGHT);

    newScene.getStylesheets().add(this.getClass().getResource("/stylesheets/weather-tracker-style.css").toExternalForm());

    Image image=null;
    try {
      image = new Image(new FileInputStream("src/main/resources/logo.png"));

    } catch (FileNotFoundException e){
      //ignored
    }
    ImageView imageView = new ImageView(image);
    imageView.setFitHeight(350);
    imageView.setFitWidth(500);
    imageView.setPreserveRatio(true);
    imageView.setId("aboutImage");

    VBox topVBox = new VBox(40);

    // About aboutInformation = this.weatherPresenter.getAboutInformation();

    Text creatorNameText = new Text("Created by " + aboutInformation.getCreatorName());
    creatorNameText.setId("creatorNameText");
    creatorNameText.setFill(Color.BLACK);
    creatorNameText.setStyle("-fx-font: 18 arial;-fx-font-weight: bold;");

    VBox referencesVBox = new VBox(10);
    referencesVBox.setAlignment(Pos.CENTER);

    Text referencesText = new Text("References:");
    referencesText.setId("referencesText");
    referencesText.setFill(Color.BLACK);
    referencesText.setStyle("-fx-font: 18 arial;-fx-font-weight: bold;");
    referencesVBox.getChildren().add(referencesText);

    for(int i=0; i<aboutInformation.getReferences().size(); i++){
      Text referenceText = new Text(aboutInformation.getReferences().get(i));
      referenceText.setFill(Color.BLACK);
      referenceText.setId("referenceText" + String.valueOf(i));
      referenceText.setStyle("-fx-font: 16 arial;-fx-font-style: italic;");
      referencesVBox.getChildren().add(referenceText);
    }

    Button backButton = new Button();
    backButton.setText("Back");
    backButton.setId("aboutPageBackBtn");
    backButton.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        weatherPresenter.displayMainWindow();
      }
    });
    backButton.setOnMouseEntered(new EventHandler<MouseEvent>() {
      public void handle(MouseEvent e) {
        backButton.setCursor(Cursor.HAND);
      }
    });

    // topVBox.getChildren().addAll(imageView,backButton);
    topVBox.getChildren().addAll(imageView,creatorNameText,referencesVBox,backButton);
    topVBox.setAlignment(Pos.CENTER);

    borderPane.setCenter(topVBox);

    this.scene=newScene;
  }
}
