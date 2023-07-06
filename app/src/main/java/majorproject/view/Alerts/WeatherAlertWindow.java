package majorproject.view.alert;

import majorproject.model.pojowrapper.LocationWeather;
import majorproject.model.pojo.CityLocation;

import java.io.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.stage.*;
import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.*;

/**
* Class containing all the stage information for the Weather Alert window.
*/
public class WeatherAlertWindow {

  private static final int WIDTH = 420;
  private static final int HEIGHT = 320;

  /**
  * Constructs the weather alert window and shows it.
  * @param locationWeather The LocationWeather object to display in the Weather Alert window.
  */
  public WeatherAlertWindow(LocationWeather locationWeather){
    constructWeatherAlertWindow(locationWeather);
  }

  /**
  * Private method that creates a new window, adds its components, and displays it to the screen.
  * @param locationWeather The LocationWeather object to display in the Weather Alert window.
  */
  private void constructWeatherAlertWindow(LocationWeather locationWeather){
    Stage newWindow = new Stage();
    newWindow.setResizable(false);
    BorderPane borderPane = new BorderPane();

    Scene scene = new Scene(borderPane,WIDTH,HEIGHT);
    scene.getStylesheets().add(this.getClass().getResource("/stylesheets/weather-tracker-style.css").toExternalForm());
    newWindow.setTitle("Weather Report");

    HBox topBox = new HBox(40);
    topBox.setPadding(new Insets(30,10,10,10));
    //add weather image top right.
    // File imageFile = engine.getImageFile(locationWeather.getCurrentWeatherData().getWeatherIconData().getIcon());
    File imageFile = new File("src/main/resources/icons/"+
      locationWeather.getCurrentWeatherData().getWeatherIconData().getIcon()+".png");
    Image image = new Image(imageFile.toURI().toString());
    ImageView imageView = new ImageView();
    imageView.setImage(image);
    imageView.setFitWidth(50.0);
    imageView.setPreserveRatio(true);

    TextFlow locationText = new TextFlow();

    Text textForLocation=new Text(locationWeather.getCityLocation().toStringVertical());
    textForLocation.setId("textForLocation");
    textForLocation.setStyle("-fx-font-weight: bold");
    textForLocation.setStyle("-fx-font-size: 16px");

    Text textForLatLon=new Text(("\n(" + String.valueOf(locationWeather.getLatitude()) + "°, " +
      String.valueOf(locationWeather.getLongitude()) + "°)"));
    textForLatLon.setId("textForLatLon");
    textForLatLon.setStyle("-fx-font-weight: bold");
    textForLatLon.setStyle("-fx-font-size: 16px");

    locationText.getChildren().addAll(textForLocation,textForLatLon);

    topBox.getChildren().addAll(imageView,locationText);
    topBox.setAlignment(Pos.CENTER);
    borderPane.setTop(topBox);

    HBox centerBox = new HBox(10);
    Label weatherDataLabel = new Label(locationWeather.getVerticalWeatherData());
    weatherDataLabel.setId("weatherDataLabel");
    weatherDataLabel.setFont(Font.font("Arial", 14));

    centerBox.setAlignment(Pos.CENTER);
    centerBox.getChildren().addAll(weatherDataLabel);
    borderPane.setCenter(centerBox);

    VBox bottomBox = new VBox(10);
    Button closeBtn = new Button();
    closeBtn.setText("Close");
    closeBtn.setId("closeWeatherAlertBtn");
    closeBtn.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        //clear the weather saved
        Stage stage = (Stage) closeBtn.getScene().getWindow();
        stage.close();
      }
    });
    closeBtn.setOnMouseEntered(new EventHandler<MouseEvent>() {
      public void handle(MouseEvent e) {
        closeBtn.setCursor(Cursor.HAND);
      }
    });

    bottomBox.getChildren().addAll(closeBtn);
    bottomBox.setAlignment(Pos.CENTER);
    bottomBox.setPadding(new Insets(20,20,20,20));

    borderPane.setBottom(bottomBox);

    newWindow.setScene(scene);
    newWindow.initModality(Modality.APPLICATION_MODAL);
    newWindow.show();
  }
}
