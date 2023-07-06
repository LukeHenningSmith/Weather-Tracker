package majorproject.view.window;

import majorproject.view.component.*;
import majorproject.view.alert.WeatherAlertWindow;

import majorproject.presenter.WeatherPresenter;
import majorproject.model.pojowrapper.LocationWeather;
import majorproject.model.pojo.CityLocation;

import java.util.Optional;
import java.util.List;
import java.io.File;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.text.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.*;
import javafx.event.*;
import javafx.geometry.*;
import javafx.collections.*;
import org.controlsfx.control.*;
import org.controlsfx.control.textfield.TextFields;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.AutoCompletionBinding.AutoCompletionEvent;

/**
* The main window (or home window) of Weather Tracker application, responsible for creating
* the components contained in MainWindow. This includes the weather map, weather table,
* weather report sender and get weather request.
*/
public class MainWindow {

  private static final int WIDTH = 1280;
  private static final int HEIGHT = 720;

  private WeatherPresenter weatherPresenter;

  private Scene scene;
  private BorderPane borderPane;

  /* Menubar at the top */
  private WeatherMenuBar weatherMenuBar;

  /* The center HBox (contains progress indicator) */
  private HBox centerHBox;
  private TextField inputField;
  private Button getWeatherBtn;
  private WeatherProgressIndicator weatherProgressIndicator;

  /* Center VBox contains centerHBox and weatherWorldMapView */
  private VBox centerVBox;
  private WeatherWorldMapView weatherWorldMapView;

  /* Bottom HBox buttons */
  private HBox bottomHBox;
  private Button clearBtn;
  private Button sendReportBtn;
  private Button removeSelectedBtn;

  /* Bottom VBox contains weatherTableView and bottomHBox */
  private VBox bottomVBox;
  private WeatherTableView weatherTableView;

  /**
  * Creates the main window scene.
  * @param weatherPresenter The presenter this class sends all of its user inputs to.
  * @param locations The list of city locations this class is able to interpret.
  */
  public MainWindow(WeatherPresenter weatherPresenter, List<CityLocation> locations){
    this.weatherPresenter=weatherPresenter;
    constructComponents();
    constructMainWindowScene(locations);
  }

  /**
  * Simple getter method for the scene this window created.
  * @return The Scene this class made.
  */
  public Scene getScene(){
    return this.scene;
  }

  /**
  * Constructs the sub-components of the main window.
  */
  private void constructComponents(){
    this.weatherWorldMapView = new WeatherWorldMapView(this.weatherPresenter);
    this.weatherTableView = new WeatherTableView(this.weatherPresenter);
    this.weatherMenuBar = new WeatherMenuBar(this.weatherPresenter);
    this.weatherProgressIndicator = new WeatherProgressIndicator(this.weatherPresenter);
  }

  /**
  * Coordinates creation of the main window scene.
  */
  private void constructMainWindowScene(List<CityLocation> locations){
    setupCenterHBox(locations);
    setupCenterVBox();
    setupBottomHBox();
    setupBottomVBox();

    BorderPane borderPane = new BorderPane();

    borderPane.setTop(weatherMenuBar.getMenuBar());

    borderPane.setCenter(centerVBox);

    borderPane.setBottom(bottomVBox);

    Scene newScene = new Scene(borderPane, WIDTH, HEIGHT);
    newScene.getStylesheets().add(this.getClass().getResource("/stylesheets/weather-tracker-style.css").toExternalForm());
    this.scene = newScene;
  }

  /**
  * Stores the CityLocation most recently selected in inputField.
  */
  private CityLocation lastAutoCompleteLocation = null;

  /**
  * Method to construct the HBox placed at the top center of the main window's BorderPane.
  * @param locations The city locations able to be interpreted.
  */
  private void setupCenterHBox(List<CityLocation> locations){
    centerHBox = new HBox(30);
    centerHBox.setAlignment(Pos.CENTER);
    centerHBox.setPadding(new Insets(20,20,15,30));

    this.inputField = new TextField("Input a city");
    inputField.setMaxWidth(500);
    inputField.setMinWidth(500);
    inputField.setId("cityInputField");

    AutoCompletionBinding<CityLocation> bind = TextFields.bindAutoCompletion(inputField, locations);
    bind.setPrefWidth(500);
    bind.setOnAutoCompleted(new EventHandler<AutoCompletionEvent<CityLocation>>() {
      @Override
      public void handle(AutoCompletionEvent<CityLocation> event) {
        lastAutoCompleteLocation=event.getCompletion();
      }
    });

    this.getWeatherBtn = new Button();
    getWeatherBtn.setText("Get Weather");
    getWeatherBtn.setId("getWeatherBtn");
    getWeatherBtn.setOnAction( (event) -> {
      weatherPresenter.getWeatherDataForCityLocation(lastAutoCompleteLocation,inputField.getText());
      inputField.setText("Input a city");
    });
    getWeatherBtn.setOnMouseEntered(new EventHandler<MouseEvent>() {
      public void handle(MouseEvent e) {
        getWeatherBtn.setCursor(Cursor.HAND);
      }
    });

    centerHBox.getChildren().addAll(inputField,getWeatherBtn,weatherProgressIndicator.getProgressIndicator());
  }

  /**
  * Method to create WeatherAlertWindow for given weather data.
  * @param weatherData The weather data to display.
  */
  public void displayWeather(LocationWeather weatherData){
      new WeatherAlertWindow(weatherData);
  }

  /**
  * Method to create Alert window for an error that has occurred.
  * @param errorTitle The title of the error window.
  * @param errorMessage The message indicating the details of the error that occurred.
  */
  public void displayErrorAlert(String errorTitle, String errorMessage){
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle("Error");
      alert.setHeaderText(errorTitle);
      alert.setContentText(errorMessage);
      alert.showAndWait();
  }

  /**
  * Method to create Alert window for a non-error notification.
  * @param informationTitle The title of the alert.
  * @param informationMessage The message indicating the details of the information.
  */
  public void displayInformationAlert(String informationTitle, String informationMessage){
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle("Weather Tracker");
    alert.setHeaderText(informationTitle);
    alert.setContentText(informationMessage);
    alert.showAndWait();
  }

  /**
  * Method to construct the VBox placed at the center of the main window's BorderPane.
  * Contains the centerHBox.
  */
  private void setupCenterVBox(){
    centerVBox = new VBox(10);
    centerVBox.setAlignment(Pos.CENTER);
    centerVBox.getChildren().addAll(centerHBox,this.weatherWorldMapView.getWorldMapView());
  }

  /**
  * Method to construct the HBox placed at the bottom center of the main window's BorderPane.
  */
  private void setupBottomHBox(){
    bottomHBox = new HBox(50);
    bottomHBox.setAlignment(Pos.CENTER);

    this.clearBtn = new Button();
    this.clearBtn.setText("Clear Current Cities");
    this.clearBtn.setId("clearBtn");
    this.clearBtn.setOnAction( (event) -> {
      weatherPresenter.clearCurrentWeather();
    });
    this.clearBtn.setOnMouseEntered(new EventHandler<MouseEvent>() {
      public void handle(MouseEvent e) {
        clearBtn.setCursor(Cursor.HAND);
      }
    });

    this.sendReportBtn = new Button();
    this.sendReportBtn.setText("Send Email Report");
    this.sendReportBtn.setId("sendReportBtn");
    this.sendReportBtn.setOnAction( (event) -> {
        weatherPresenter.sendReport(askForEmailToSendTo());
    });
    this.sendReportBtn.setOnMouseEntered(new EventHandler<MouseEvent>() {
      public void handle(MouseEvent e) {
        sendReportBtn.setCursor(Cursor.HAND);
      }
    });

    this.removeSelectedBtn = new Button();
    this.removeSelectedBtn.setText("Remove Selected City");
    this.removeSelectedBtn.setId("removeSelectedBtn");
    this.removeSelectedBtn.setOnAction( (event) -> {
      weatherPresenter.removeCurrentWeather(weatherTableView.getSelectedIndices().toArray());
    });
    this.removeSelectedBtn.setOnMouseEntered(new EventHandler<MouseEvent>() {
      public void handle(MouseEvent e) {
        removeSelectedBtn.setCursor(Cursor.HAND);
      }
    });

    bottomHBox.getChildren().addAll(removeSelectedBtn,clearBtn,sendReportBtn);
  }

  /**
  * Method to construct the VBox placed at the bottom center of the main window's BorderPane.
  * Contains the bottomHBox.
  */
  private void setupBottomVBox(){
    bottomVBox = new VBox(20);
    bottomVBox.setAlignment(Pos.CENTER);
    bottomVBox.setPadding(new Insets(30,30,30,30));
    StackPane stackHolder = new StackPane(this.weatherTableView.getTableView());
    bottomVBox.getChildren().addAll(stackHolder,bottomHBox);
  }

  /**
  * Method to disable the buttons that should not be spammed and could cause race conditions if they
  * were executed simulataneously.
  */
  public void disableButtonsDuringGetWeather(){
    this.clearBtn.setDisable(true);
    this.removeSelectedBtn.setDisable(true);
    this.getWeatherBtn.setDisable(true);
  }

  /**
  * Method to re-enable the disabled buttons during get weather.
  */
  public void enableButtonsAfterGetWeather(){
    this.clearBtn.setDisable(false);
    this.removeSelectedBtn.setDisable(false);
    this.getWeatherBtn.setDisable(false);
  }

  /**
  * Method to disable buttons during send-report to avoid user spam.
  */
  public void disableButtonsDuringReportSend(){
    this.sendReportBtn.setDisable(true);
  }

  /**
  * Method to enable buttons after send-report has completed.
  */
  public void enableButtonsAfterReportSend(){
    this.sendReportBtn.setDisable(false);
  }

  /**
  * Creates dialog window to ask the user for what email to send to.
  * Updates emailGiven field.
  * @return Returns the String the user input. If user clicks "ok", returns zero length string. If user cancels, returns null.
  */
  private String askForEmailToSendTo(){
    TextInputDialog textInput = new TextInputDialog("");
    textInput.setTitle("Send to email");
    textInput.setHeaderText("Enter an email to send to: ");

    Optional<String> input = textInput.showAndWait();
    if (input.isPresent()) {
      String inputString = input.get();
      if(inputString==null){
        //because null is for cancel / exit
        return "";
      }
      return inputString;
    }
    return null;
  }

  /**
  * Creates dialog window to ask if cache is to be used or not.
  * @return Returns boolean result of true or false.
  */
  public boolean askIfCacheUsed(){
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);

    alert.setTitle("Cached data");
    alert.setHeaderText("Cached weather data exists for this city.");
    alert.setContentText("Would you like to use the possibly out-of-date cached data?");

    ButtonType yesButton = new ButtonType("Use Cache", ButtonBar.ButtonData.YES);
    ButtonType noButton = new ButtonType("Request Current Weather", ButtonBar.ButtonData.NO);

    alert.getButtonTypes().setAll(noButton,yesButton);
    Optional<ButtonType> result = alert.showAndWait();
    if(result.isPresent()){
      if(result.get() == yesButton){
        return true;
      }
    }
    return false;
  }


  /**
  * Notifies subcomponents of change to loading state.
  * @param loadingState The current loading state the subcomponents should be displaying.
  */
  public void loadingStateChange(boolean loadingState){
    weatherProgressIndicator.notifyOfLoadingStateChange(loadingState);
  }

  /**
  * Notifies subcomponents of change to weather data.
  * @param weatherData The current weather data the subcomponents should be displaying.
  */
  public void weatherDataChange(List<LocationWeather> weatherData){
    weatherWorldMapView.updateWorldMapViewData(weatherData);
    weatherTableView.updateTableViewData(weatherData);
  }
}
