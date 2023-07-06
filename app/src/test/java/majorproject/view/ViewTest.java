package majorproject.test.view;

import majorproject.App;
import majorproject.model.ModelEngine;
import majorproject.model.inputengine.*;
import majorproject.model.outputengine.*;
import majorproject.model.cacheengine.*;
import majorproject.model.utility.*;
import majorproject.model.pojo.*;
import majorproject.model.pojowrapper.*;
import majorproject.view.window.*;
import majorproject.view.ViewCoordinator;
import majorproject.presenter.WeatherPresenter;

import java.util.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

import org.testfx.api.FxAssert;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.framework.junit5.Start;
import org.testfx.matcher.control.LabeledMatchers;
import org.testfx.util.WaitForAsyncUtils;
import org.testfx.matcher.base.NodeMatchers;
import static org.testfx.api.FxAssert.verifyThat;

import javafx.scene.*;
import javafx.scene.input.*;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.application.Platform;
import javafx.geometry.VerticalDirection;
import javafx.scene.robot.Robot;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.controlsfx.control.WorldMapView;

import org.mockito.*;
import org.mockito.stubbing.*;
import org.mockito.invocation.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.AdditionalAnswers.returnsSecondArg;

/**
* Class to test the view package (but really the entire application execution).
* Only tests with all APIs set to offline mode.
*/
@TestMethodOrder(OrderAnnotation.class)
class ViewTest extends ApplicationTest{

  private Stage primaryStage;

  private MainWindow mainWindow;
  private SplashScreenWindow splashScreenWindow;
  private AboutPageWindow aboutPageWindow;

  private Robot robot;

  /**
  * Set the robot used to 'mock' user input to the test GUI.
  * @param robot The Robot mocking user input.
  */
  private void setRobot(Robot robot){
    this.robot=robot;
  }

  private double mouseXPosition;
  private double mouseYPosition;

  /**
  * Simple setter for mouse positions variables.
  * @param x The x value of mouse location.
  * @param y The y value of mouse location.
  */
  private void setMousePosition(double x, double y){
    this.mouseXPosition=x;
    this.mouseYPosition=y;
  }

  /**
  * Used to find nodes on the current window based on their id.
  * @param query The name of the node to look for.
  * @return Returns the node if it is found.
  */
  private <T extends Node> T find(final String query){
    return (T) lookup(query).queryAll().iterator().next();
  }

  /**
  * Gets the stage, and GUI launches in offline mode.
  */
  @BeforeAll
  public static void setupClass() throws Exception {
    ApplicationTest.launch(App.class,"offline","offline");
  }

  /**
  * The javaFX start method to launch GUI given the stage.
  * @param stage The primary stage to launch.
  */
  @Override
  public void start(Stage stage) throws Exception {
    this.primaryStage=stage;
  }

  /**
  * Tests that splash screen can be launched, has right components and lasts the correct duration.
  */
  @Test
  @Order(1)
  public void testSplashScreenWindow(){
    WaitForAsyncUtils.waitForFxEvents();
    //check can find the right components.
    //waits 15 seconds. At each second, checks is still on the loading screen.
    for(int i=0; i<15; i++){
      try {
        find("#progressBar");
      } catch (Exception e){ //failed.
        assert(1==2);
      }
      try {
        Thread.sleep(1000);
      } catch (Exception ignored){}
        //check still on loading window. progressBar only on loading window.
      }
      WaitForAsyncUtils.waitForFxEvents();
      //to check on main window, move to an element on it.
      moveTo("#getWeatherBtn");
    }

    /**
    * Method to skip splash screen by mocking the ViewCoordinator behaviour.
    */
    private void setupApplicationSkipSplashScreen(){
      //mock the locations
      LocationsFileLoader locationsFileLoader = mock(LocationsFileLoader.class);
      List<CityLocation> locations = new ArrayList<CityLocation>();
      locations.add(new CityLocation(2147714,-33.86785,151.20732,"Sydney","New South Wales","Australia"));
      locations.add(new CityLocation(1587923,10.94469,106.82432,"Biên Hòa","Ðồng Nai","Vietnam"));
      locations.add(new CityLocation(373303,4.85165,31.58247,"Juba","",""));
      locations.add(new CityLocation(218253,3.81461,23.68665,"Bondo","","Democratic Republic of The Congo"));
      when(locationsFileLoader.getAllCityLocations()).thenReturn(locations);

      release(new KeyCode[]{});
      release(new MouseButton[]{});

      WaitForAsyncUtils.waitForFxEvents();

      InputEngine inputEngine = new OfflineInputEngine();
      OutputEngine outputEngine = new OfflineOutputEngine();
      CacheEngine cacheEngine = new OfflineCacheEngine();

      String sendgridFromEmail = "dummyEmail";
      String sendgridAPIKey = "dummyKey";
      String weatherbitAPIKey = "dummyKey";

      ModelEngine engine = new ModelEngine(inputEngine, outputEngine, cacheEngine, weatherbitAPIKey,
      sendgridAPIKey, sendgridFromEmail, locationsFileLoader);

      //going to need to mock the viewCoordinator to skip the splash screen.
      ViewCoordinator mockedViewCoordinator = mock(ViewCoordinator.class);
      when(mockedViewCoordinator.displayMainWindow(any(), any())).thenAnswer(new Answer<MainWindow>() {
        @Override
        public MainWindow answer(InvocationOnMock invocation) throws Throwable {
          Object[] args = invocation.getArguments();
          if(mainWindow==null){
            //make it
            mainWindow = new MainWindow((WeatherPresenter)args[0],(List<CityLocation>)args[1]);
          }
          //set the scene
          primaryStage.hide();
          primaryStage.setScene(mainWindow.getScene());
          primaryStage.show();
          return mainWindow;
        }
      });

      when(mockedViewCoordinator.displayAboutWindow(any(), any())).thenAnswer(new Answer<AboutPageWindow>() {
        @Override
        public AboutPageWindow answer(InvocationOnMock invocation) throws Throwable {
          Object[] args = invocation.getArguments();
          if(aboutPageWindow==null){
            //make it
            aboutPageWindow = new AboutPageWindow((WeatherPresenter)args[0],(About)args[1]);
          }
          //set the scene
          primaryStage.hide();
          primaryStage.setScene(aboutPageWindow.getScene());
          primaryStage.show();
          return aboutPageWindow;
        }
      });

      when(mockedViewCoordinator.displaySplashScreenWindow(any())).thenAnswer(new Answer<AboutPageWindow>() {
        @Override
        public AboutPageWindow answer(InvocationOnMock invocation) throws Throwable {
          Object[] args = invocation.getArguments();
          WeatherPresenter presenter = (WeatherPresenter)args[0];
          presenter.displayMainWindow();
          return null;
        }
      });


      Platform.runLater(() ->{
        WeatherPresenter presenter = new WeatherPresenter(engine,mockedViewCoordinator);
      });

      WaitForAsyncUtils.waitForFxEvents();
    }

    /**
    * Tests that the main window is with all the correct buttons and text when initialised.
    */
    @Test
    public void testMainWindowInit(){
      setupApplicationSkipSplashScreen();

      //just moveTo() each button to check it is there.
      moveTo("#cityInputField");
      moveTo("#getWeatherBtn");
      moveTo("#removeSelectedBtn");
      moveTo("#clearBtn");
      moveTo("#sendReportBtn");

      clickOn("Menu");
      moveTo("#about");
      moveTo("#clearCache");

      //check the content of the tableview and inputtextfield.
      TextField inputTextField = find("#cityInputField");
      assert(inputTextField.getText().equals("Input a city"));

      //check tableview has correct text when empty.
      TableView tableView = find("#weatherTable");
      Label emptyLabel = (Label) tableView.getPlaceholder();
      assert(emptyLabel.getText().equals("No city weather data to display"));

      //test that worldMapView and tableView are empty on startup.
      assert(tableView.getItems().size()==0);

      WorldMapView worldMapView = find("#weatherMap");

      assert(worldMapView.locationsProperty().size()==0);
    }

    /**
    * Tests that about page can be navigated to, contains correct content, and can be closed to return to main menu.
    */
    @Test
    public void testAboutPage(){
      setupApplicationSkipSplashScreen();
      //open the about page (navigated to from the main menu)
      clickOn("Menu");
      clickOn("#about");

      WaitForAsyncUtils.waitForFxEvents();

      //check the about image is present.
      moveTo("#aboutImage");

      //check creator name is present.
      verifyThat("#creatorNameText", (Text text) -> {
        String theText = text.getText();
        return theText.equals("Created by Luke Henning-Smith");
      });

      //check references title:
      verifyThat("#referencesText", (Text text) -> {
        String theText = text.getText();
        return theText.equals("References:");
      });

      //check the references
      verifyThat("#referenceText0", (Text text) -> {
        String theText = text.getText();
        return theText.equals("Splash image:  https://www.efio.dk/");
      });
      verifyThat("#referenceText1", (Text text) -> {
        String theText = text.getText();
        return theText.equals("Input API:  https://www.weatherbit.io/api");
      });
      verifyThat("#referenceText2", (Text text) -> {
        String theText = text.getText();
        return theText.equals("Weather icons:  https://www.weatherbit.io/api/meta");
      });
      verifyThat("#referenceText3", (Text text) -> {
        String theText = text.getText();
        return theText.equals("15,000+ Pop. Cities:  https://www.weatherbit.io/api/meta");
      });
      verifyThat("#referenceText4", (Text text) -> {
        String theText = text.getText();
        return theText.equals("Output API:  https://sendgrid.com/solutions/email-api/");
      });
      verifyThat("#referenceText5", (Text text) -> {
        String theText = text.getText();
        return theText.equals("JavaFX 17 UI:  https://openjfx.io/javadoc/17/");
      });
      verifyThat("#referenceText6", (Text text) -> {
        String theText = text.getText();
        return theText.equals("JUnit Jupiter 5.8.2:  https://junit.org/junit5/docs/current/api/");
      });
      verifyThat("#referenceText7", (Text text) -> {
        String theText = text.getText();
        return theText.equals("Mockito-Junit-Jupiter 4.4.0:  https://javadoc.io/doc/org.mockito/mockito-junit-jupiter/4.4.0/index.html");
      });
      verifyThat("#referenceText8", (Text text) -> {
        String theText = text.getText();
        return theText.equals("Gson library:  https://javadoc.io/doc/com.google.code.gson/gson/latest/com.google.gson/com/google/gson/Gson.html");
      });

      //go back to main page
      clickOn("#aboutPageBackBtn");
      WaitForAsyncUtils.waitForFxEvents();

      //check on main page by moving to a main-page only node.
      moveTo("#cityInputField");

    }

    /**
    * Test that the clearCache pops up the appropriate alert.
    * The actual functionality of cache cannot be tested as GUI testing is in offline mode only.
    */
    @Test
    public void testClearCache(){
      setupApplicationSkipSplashScreen();

      clickOn("Menu");
      clickOn("#clearCache");
      WaitForAsyncUtils.waitForFxEvents();

      //check alert popped up.
      verifyThat("OK", NodeMatchers.isVisible());

      //click it
      clickOn("OK");

      WaitForAsyncUtils.waitForFxEvents();
      //verify back on main menu.
      moveTo("#getWeatherBtn");
    }

    /**
    * Tests that 4 city locations can be searched and added to the table and map in correct order.
    * Ensures weather alerts pop up, the table is scrollable, and double clicking a city in the
    * table brings up their weather alert.
    * Ensures that each location is added to the map with the correct image and latitude/longitude.
    */
    @Test
    public void testGetWeatherMultiple(){
      setupApplicationSkipSplashScreen();

      //search sydney
      doubleClickOn("#cityInputField");
      clickOn("#cityInputField");
      write("sydney");
      moveTo("#cityInputField");
      WaitForAsyncUtils.waitForFxEvents();
      moveBy(0,20);
      WaitForAsyncUtils.waitForFxEvents();
      Platform.runLater(() ->{
        setRobot(new Robot());
        setMousePosition(robot.getMouseX(),robot.getMouseY());
      });
      WaitForAsyncUtils.waitForFxEvents();
      clickOn(mouseXPosition,mouseYPosition);
      WaitForAsyncUtils.waitForFxEvents();
      clickOn("#getWeatherBtn");
      WaitForAsyncUtils.waitForFxEvents();
      //check the alert window is for the correct location
      Text textForLocation = find("#textForLocation");
      assert(textForLocation.getText().equals("City: Sydney\nState: New South Wales\nCountry: Australia"));
      Text textForLatLon = find("#textForLatLon");
      assert(textForLatLon.getText().equals("\n(-33.86785°, 151.20732°)"));
      Label textForWeather = find("#weatherDataLabel");
      assert(textForWeather.getText().equals("Temperature:   0.0°C\nWind Speed:   0.0 m/s\nWind Direction:   Placeholder\nCloud Coverage:   0.0%\nPrecipitation:   0.0 mm/hr\nAir Quality Index:   0"));
      clickOn("#closeWeatherAlertBtn");

      //search Bondo
      doubleClickOn("#cityInputField");
      clickOn("#cityInputField");
      write("Bondo");
      moveTo("#cityInputField");
      WaitForAsyncUtils.waitForFxEvents();
      moveBy(0,20);
      WaitForAsyncUtils.waitForFxEvents();
      Platform.runLater(() ->{
        setMousePosition(robot.getMouseX(),robot.getMouseY());
      });
      WaitForAsyncUtils.waitForFxEvents();
      clickOn(mouseXPosition,mouseYPosition);
      WaitForAsyncUtils.waitForFxEvents();
      clickOn("#getWeatherBtn");
      WaitForAsyncUtils.waitForFxEvents();
      //check the alert window is for the correct location
      Text textForLocationBondo = find("#textForLocation");
      assert(textForLocationBondo.getText().equals("City: Bondo\nCountry: Democratic Republic of The Congo"));
      Text textForLatLonBondo = find("#textForLatLon");
      assert(textForLatLonBondo.getText().equals("\n(3.81461°, 23.68665°)"));
      Label textForWeatherBondo = find("#weatherDataLabel");
      assert(textForWeatherBondo.getText().equals("Temperature:   0.0°C\nWind Speed:   0.0 m/s\nWind Direction:   Placeholder\nCloud Coverage:   0.0%\nPrecipitation:   0.0 mm/hr\nAir Quality Index:   0"));
      clickOn("#closeWeatherAlertBtn");

      //search Biên Hòa
      doubleClickOn("#cityInputField");
      clickOn("#cityInputField");
      write("Biên Hòa");
      moveTo("#cityInputField");
      WaitForAsyncUtils.waitForFxEvents();
      moveBy(0,20);
      WaitForAsyncUtils.waitForFxEvents();
      Platform.runLater(() ->{
        setMousePosition(robot.getMouseX(),robot.getMouseY());
      });
      WaitForAsyncUtils.waitForFxEvents();
      clickOn(mouseXPosition,mouseYPosition);
      WaitForAsyncUtils.waitForFxEvents();
      clickOn("#getWeatherBtn");
      WaitForAsyncUtils.waitForFxEvents();
      //check the alert window is for the correct location
      Text textForLocationBienHoa = find("#textForLocation");
      assert(textForLocationBienHoa.getText().contains("Biên Hòa"));
      clickOn("#closeWeatherAlertBtn");

      //search Juba
      doubleClickOn("#cityInputField");
      clickOn("#cityInputField");
      write("Juba");
      moveTo("#cityInputField");
      WaitForAsyncUtils.waitForFxEvents();
      moveBy(0,20);
      WaitForAsyncUtils.waitForFxEvents();
      Platform.runLater(() ->{
        setMousePosition(robot.getMouseX(),robot.getMouseY());
      });
      WaitForAsyncUtils.waitForFxEvents();
      clickOn(mouseXPosition,mouseYPosition);
      WaitForAsyncUtils.waitForFxEvents();
      clickOn("#getWeatherBtn");
      WaitForAsyncUtils.waitForFxEvents();
      //check the alert window is for the correct location
      Text textForLocationJuba = find("#textForLocation");
      assert(textForLocationJuba.getText().contains("Juba"));
      clickOn("#closeWeatherAlertBtn");

      //check TableView and WorldMapView both updated.
      TableView tableView = find("#weatherTable");
      WorldMapView worldMapView = find("#weatherMap");
      assert(tableView.getItems().size()==4);
      assert(worldMapView.locationsProperty().size()==4);

      //check they were added to the map in the correct locations with the correct image name.
      //sydney
      assert(worldMapView.locationsProperty().get(0).getName().equals("a01d"));
      assert(worldMapView.locationsProperty().get(0).getLatitude() == -33.86785);
      assert(worldMapView.locationsProperty().get(0).getLongitude() == 151.20732);
      //bondo
      assert(worldMapView.locationsProperty().get(1).getName().equals("a01d"));
      assert(worldMapView.locationsProperty().get(1).getLatitude() == 3.81461);
      assert(worldMapView.locationsProperty().get(1).getLongitude() == 23.68665);
      //bien hoa
      assert(worldMapView.locationsProperty().get(2).getName().equals("a01d"));
      assert(worldMapView.locationsProperty().get(2).getLatitude() == 10.94469);
      assert(worldMapView.locationsProperty().get(2).getLongitude() == 106.82432);
      //juba
      assert(worldMapView.locationsProperty().get(3).getName().equals("a01d"));
      assert(worldMapView.locationsProperty().get(3).getLatitude() == 4.85165);
      assert(worldMapView.locationsProperty().get(3).getLongitude() == 31.58247);

      //check added to table in right order. Can be done by their unique city ids.
      //The contents of the weather data already checked to be correct from the get weather
      //message check.
      assert(tableView.getItems().get(0) instanceof LocationWeather);
      assert(((LocationWeather) tableView.getItems().get(0)).getCityId() == 2147714);
      assert(((LocationWeather) tableView.getItems().get(1)).getCityId() == 218253);
      assert(((LocationWeather) tableView.getItems().get(2)).getCityId() == 1587923);
      assert(((LocationWeather) tableView.getItems().get(3)).getCityId() == 373303);

      //check the table is scrollable, and double clicking the entries gives you the weather alert for each.
      moveTo("#weatherTable");
      //scroll down.
      scroll(5,VerticalDirection.UP);
      moveBy(0,30);
      WaitForAsyncUtils.waitForFxEvents();

      //double click.
      Platform.runLater(() ->{
        setMousePosition(robot.getMouseX(),robot.getMouseY());
      });
      WaitForAsyncUtils.waitForFxEvents();
      doubleClickOn(mouseXPosition,mouseYPosition);
      WaitForAsyncUtils.waitForFxEvents();

      //check correct alert popped up.
      Text textForLocationJubaAgain = find("#textForLocation");
      assert(textForLocationJubaAgain.getText().contains("Juba"));
      clickOn("#closeWeatherAlertBtn");
      WaitForAsyncUtils.waitForFxEvents();

      //check back on main window.
      moveTo("#getWeatherBtn");
    }

    /**
    * Tests that if you search a city that has already been searched, it moves to the bottom of the table
    * (and is still on the map).
    */
    @Test
    public void testGetWeatherRepeatLocation(){
      setupApplicationSkipSplashScreen();
      //add sydney.
      doubleClickOn("#cityInputField");
      clickOn("#cityInputField");
      write("sydney");
      moveTo("#cityInputField");
      WaitForAsyncUtils.waitForFxEvents();
      moveBy(0,20);
      WaitForAsyncUtils.waitForFxEvents();
      Platform.runLater(() ->{
        setRobot(new Robot());
        setMousePosition(robot.getMouseX(),robot.getMouseY());
      });
      WaitForAsyncUtils.waitForFxEvents();
      clickOn(mouseXPosition,mouseYPosition);
      WaitForAsyncUtils.waitForFxEvents();
      clickOn("#getWeatherBtn");
      WaitForAsyncUtils.waitForFxEvents();
      //check the alert window is for the correct location
      Text textForLocation = find("#textForLocation");
      assert(textForLocation.getText().contains("Sydney"));
      clickOn("#closeWeatherAlertBtn");

      //add Bondo
      doubleClickOn("#cityInputField");
      clickOn("#cityInputField");
      write("Bondo");
      moveTo("#cityInputField");
      WaitForAsyncUtils.waitForFxEvents();
      moveBy(0,20);
      WaitForAsyncUtils.waitForFxEvents();
      Platform.runLater(() ->{
        setMousePosition(robot.getMouseX(),robot.getMouseY());
      });
      WaitForAsyncUtils.waitForFxEvents();
      clickOn(mouseXPosition,mouseYPosition);
      WaitForAsyncUtils.waitForFxEvents();
      clickOn("#getWeatherBtn");
      WaitForAsyncUtils.waitForFxEvents();
      //check the alert window is for the correct location
      Text textForLocationBondo = find("#textForLocation");
      assert(textForLocationBondo.getText().contains("Bondo"));
      clickOn("#closeWeatherAlertBtn");

      //add sydney again.
      doubleClickOn("#cityInputField");
      clickOn("#cityInputField");
      write("sydney");
      moveTo("#cityInputField");
      WaitForAsyncUtils.waitForFxEvents();
      moveBy(0,20);
      WaitForAsyncUtils.waitForFxEvents();
      Platform.runLater(() ->{
        setRobot(new Robot());
        setMousePosition(robot.getMouseX(),robot.getMouseY());
      });
      WaitForAsyncUtils.waitForFxEvents();
      clickOn(mouseXPosition,mouseYPosition);
      WaitForAsyncUtils.waitForFxEvents();
      clickOn("#getWeatherBtn");
      WaitForAsyncUtils.waitForFxEvents();
      //check the alert window is for the correct location
      Text textForLocationSydneyAgain = find("#textForLocation");
      assert(textForLocationSydneyAgain.getText().contains("Sydney"));
      clickOn("#closeWeatherAlertBtn");

      //check still size 2.
      TableView tableView = find("#weatherTable");
      WorldMapView worldMapView = find("#weatherMap");
      assert(tableView.getItems().size()==2);
      assert(worldMapView.locationsProperty().size()==2);

      //check order (based on city ids).
      assert(((LocationWeather) tableView.getItems().get(0)).getCityId() == 218253); //Bondo's city id.
      assert(((LocationWeather) tableView.getItems().get(1)).getCityId() == 2147714); //sydney's city id.

      //check still on the map.
      //bondo
      assert(worldMapView.locationsProperty().get(0).getName().equals("a01d"));
      assert(worldMapView.locationsProperty().get(0).getLatitude() == 3.81461);
      assert(worldMapView.locationsProperty().get(0).getLongitude() == 23.68665);
      //sydney
      assert(worldMapView.locationsProperty().get(1).getName().equals("a01d"));
      assert(worldMapView.locationsProperty().get(1).getLatitude() == -33.86785);
      assert(worldMapView.locationsProperty().get(1).getLongitude() == 151.20732);
    }


    /**
    * Tests that if you search a city but do not select an option, selection error occurs.
    * Also checks that if the search is empty, selection error.
    */
    @Test
    public void testGetWeatherSelectionError(){
      setupApplicationSkipSplashScreen();

      //test get weather with nothing typed or selected.
      clickOn("#getWeatherBtn");
      WaitForAsyncUtils.waitForFxEvents();
      //check alert popped up.
      verifyThat("OK", NodeMatchers.isVisible());
      verifyThat("Location Selection Error", NodeMatchers.isVisible());
      verifyThat("Please select a city from the drop down list.", NodeMatchers.isVisible());
      //close the alert
      clickOn("OK");

      WaitForAsyncUtils.waitForFxEvents();

      //get weather without selecting option.
      doubleClickOn("#cityInputField");
      clickOn("#cityInputField");
      write("sydney");
      WaitForAsyncUtils.waitForFxEvents();
      //click get weather without selecting option.
      doubleClickOn("#getWeatherBtn");
      WaitForAsyncUtils.waitForFxEvents();
      //check alert popped up.
      verifyThat("OK", NodeMatchers.isVisible());
      verifyThat("Location Selection Error", NodeMatchers.isVisible());
      verifyThat("Please select a city from the drop down list.", NodeMatchers.isVisible());
      //close the alert
      clickOn("OK");

      WaitForAsyncUtils.waitForFxEvents();

      //now do it again but the searched city is a perfect match (but isn't selected).
      doubleClickOn("#cityInputField");
      clickOn("#cityInputField");
      write("City: Sydney,  State: New South Wales,  Country: Australia");
      WaitForAsyncUtils.waitForFxEvents();
      //click get weather without selecting option.
      doubleClickOn("#getWeatherBtn");
      WaitForAsyncUtils.waitForFxEvents();
      //check alert popped up.
      verifyThat("OK", NodeMatchers.isVisible());
      verifyThat("Location Selection Error", NodeMatchers.isVisible());
      verifyThat("Please select a city from the drop down list.", NodeMatchers.isVisible());
      //close the alert
      clickOn("OK");
    }

    /**
    * Tests that if you search a city, select an option, then modify the text, gives selection error.
    */
    @Test
    public void testGetWeatherSelectedButModified(){
      setupApplicationSkipSplashScreen();
      doubleClickOn("#cityInputField");
      clickOn("#cityInputField");
      write("sydney");
      moveTo("#cityInputField");
      WaitForAsyncUtils.waitForFxEvents();
      moveBy(0,20);
      WaitForAsyncUtils.waitForFxEvents();
      Platform.runLater(() ->{
        setRobot(new Robot());
        setMousePosition(robot.getMouseX(),robot.getMouseY());
      });
      WaitForAsyncUtils.waitForFxEvents();
      clickOn(mouseXPosition,mouseYPosition);
      WaitForAsyncUtils.waitForFxEvents();
      //now modify it.
      doubleClickOn("#cityInputField");
      clickOn("#cityInputField");
      write("sydney");
      WaitForAsyncUtils.waitForFxEvents();
      //now try to get the weather.
      doubleClickOn("#getWeatherBtn");
      WaitForAsyncUtils.waitForFxEvents();

      //ensure selection error occurred.
      verifyThat("OK", NodeMatchers.isVisible());
      verifyThat("Location Selection Error", NodeMatchers.isVisible());
      verifyThat("Please select a city from the drop down list.", NodeMatchers.isVisible());
      //close the alert
      clickOn("OK");
    }

    /**
    * Test that pressing the clear city button when searched cities is empty produces an error alert.
    */
    @Test
    public void testClearCitiesWhenEmpty(){
      setupApplicationSkipSplashScreen();
      clickOn("#clearBtn");
      //check error
      verifyThat("OK", NodeMatchers.isVisible());
      verifyThat("Clear Failed", NodeMatchers.isVisible());
      verifyThat("There is no data to clear.", NodeMatchers.isVisible());
      clickOn("OK");
      //check back on main menu.
      moveTo("#getWeatherBtn");
    }

    /**
    * Tests that when clear entries button is pressed and searched cities are not empty, removes all
    * the cities from the table and map. Table goes back to displaying empty message.
    * Navigating to different page and returning means it is still empty.
    */
    @Test
    public void testClearCitiesValid(){
      setupApplicationSkipSplashScreen();
      //add two citites
      //add sydney.
      doubleClickOn("#cityInputField");
      clickOn("#cityInputField");
      write("sydney");
      moveTo("#cityInputField");
      WaitForAsyncUtils.waitForFxEvents();
      moveBy(0,20);
      WaitForAsyncUtils.waitForFxEvents();
      Platform.runLater(() ->{
        setRobot(new Robot());
        setMousePosition(robot.getMouseX(),robot.getMouseY());
      });
      WaitForAsyncUtils.waitForFxEvents();
      clickOn(mouseXPosition,mouseYPosition);
      WaitForAsyncUtils.waitForFxEvents();
      clickOn("#getWeatherBtn");
      WaitForAsyncUtils.waitForFxEvents();
      //check the alert window is for the correct location
      Text textForLocation = find("#textForLocation");
      assert(textForLocation.getText().contains("Sydney"));
      clickOn("#closeWeatherAlertBtn");

      //add Bondo
      doubleClickOn("#cityInputField");
      clickOn("#cityInputField");
      write("Bondo");
      moveTo("#cityInputField");
      WaitForAsyncUtils.waitForFxEvents();
      moveBy(0,20);
      WaitForAsyncUtils.waitForFxEvents();
      Platform.runLater(() ->{
        setMousePosition(robot.getMouseX(),robot.getMouseY());
      });
      WaitForAsyncUtils.waitForFxEvents();
      clickOn(mouseXPosition,mouseYPosition);
      WaitForAsyncUtils.waitForFxEvents();
      clickOn("#getWeatherBtn");
      WaitForAsyncUtils.waitForFxEvents();
      //check the alert window is for the correct location
      Text textForLocationBondo = find("#textForLocation");
      assert(textForLocationBondo.getText().contains("Bondo"));
      clickOn("#closeWeatherAlertBtn");

      //do clear cities.
      clickOn("#clearBtn");
      WaitForAsyncUtils.waitForFxEvents();

      //make sure still on main menu, and no alert.
      moveTo("#getWeatherBtn");
      WaitForAsyncUtils.waitForFxEvents();

      //check it is cleared.
      //check still size 2.
      TableView tableView = find("#weatherTable");
      WorldMapView worldMapView = find("#weatherMap");
      assert(tableView.getItems().size()==0);
      assert(worldMapView.locationsProperty().size()==0);

      //navigate to about page and go back to main menu.
      clickOn("Menu");
      clickOn("#about");
      WaitForAsyncUtils.waitForFxEvents();
      clickOn("#aboutPageBackBtn");
      WaitForAsyncUtils.waitForFxEvents();

      //check still empty.
      tableView = find("#weatherTable");
      worldMapView = find("#weatherMap");
      assert(tableView.getItems().size()==0);
      assert(worldMapView.locationsProperty().size()==0);

      //check empty message in table.
      Label emptyLabel = (Label) tableView.getPlaceholder();
      assert(emptyLabel.getText().equals("No city weather data to display"));

      //doing clear again produces error.
      clickOn("#clearBtn");
      //check error
      verifyThat("OK", NodeMatchers.isVisible());
      verifyThat("Clear Failed", NodeMatchers.isVisible());
      verifyThat("There is no data to clear.", NodeMatchers.isVisible());
      clickOn("OK");
      //check back on main menu.
      moveTo("#getWeatherBtn");
    }

    /**
    * Tests that send email button opens up email to send to box regardless of data empty or not.
    * If closed button is pressed, no error are produced and nothing occurs.
    * If data is empty, produces error.
    */
    @Test
    public void testSendEmailAsksForEmailInvalid(){
      setupApplicationSkipSplashScreen();

      //check cancelling the send produces no error message and alerts no data.
      clickOn("#sendReportBtn");
      clickOn("Cancel");
      moveTo("#getWeatherBtn");
      WaitForAsyncUtils.waitForFxEvents();

      //check clicking "OK" with empty email produces error.
      clickOn("#sendReportBtn");
      clickOn("OK");
      WaitForAsyncUtils.waitForFxEvents();
      //check error;
      verifyThat("OK", NodeMatchers.isVisible());
      verifyThat("Invalid Email", NodeMatchers.isVisible());
      verifyThat("The email given was invalid.", NodeMatchers.isVisible());
      clickOn("OK");
      WaitForAsyncUtils.waitForFxEvents();

      //putting in 2 characters as the email produces same error.
      clickOn("#sendReportBtn");
      write("12");
      clickOn("OK");
      WaitForAsyncUtils.waitForFxEvents();
      //check error;
      verifyThat("OK", NodeMatchers.isVisible());
      verifyThat("Invalid Email", NodeMatchers.isVisible());
      verifyThat("The email given was invalid.", NodeMatchers.isVisible());
      clickOn("OK");
      WaitForAsyncUtils.waitForFxEvents();

      //check that writing a "valid" (>3 character) email procuces error when is data is empty.
      clickOn("#sendReportBtn");
      write("valid@mail.com");
      clickOn("OK");
      WaitForAsyncUtils.waitForFxEvents();
      //check error;
      verifyThat("OK", NodeMatchers.isVisible());
      verifyThat("Email Failed", NodeMatchers.isVisible());
      verifyThat("Weather data is empty.", NodeMatchers.isVisible());
      clickOn("OK");
      WaitForAsyncUtils.waitForFxEvents();
    }

    /**
    * Tests that with a "valid" (>3 characters) email and not empty weather data, an email sent message
    * is displayed (not actually sent, as this is offline testing).
    */
    @Test
    public void testSendEmailValid(){
      setupApplicationSkipSplashScreen();
      //add data.
      doubleClickOn("#cityInputField");
      clickOn("#cityInputField");
      write("sydney");
      moveTo("#cityInputField");
      WaitForAsyncUtils.waitForFxEvents();
      moveBy(0,20);
      WaitForAsyncUtils.waitForFxEvents();
      Platform.runLater(() ->{
        setRobot(new Robot());
        setMousePosition(robot.getMouseX(),robot.getMouseY());
      });
      WaitForAsyncUtils.waitForFxEvents();
      clickOn(mouseXPosition,mouseYPosition);
      WaitForAsyncUtils.waitForFxEvents();
      clickOn("#getWeatherBtn");
      WaitForAsyncUtils.waitForFxEvents();
      //check the alert window is for the correct location
      Text textForLocation = find("#textForLocation");
      assert(textForLocation.getText().contains("Sydney"));
      clickOn("#closeWeatherAlertBtn");
      WaitForAsyncUtils.waitForFxEvents();

      //send email.
      clickOn("#sendReportBtn");
      write("valid@mail.com");
      clickOn("OK");
      WaitForAsyncUtils.waitForFxEvents();
      //verify success message.
      verifyThat("OK", NodeMatchers.isVisible());
      verifyThat("Email Sent", NodeMatchers.isVisible());
      verifyThat("The email weather report has been sent.", NodeMatchers.isVisible());
      clickOn("OK");
      WaitForAsyncUtils.waitForFxEvents();

      //try to send again
      clickOn("#sendReportBtn");
      write("valid@mail.com");
      clickOn("OK");
      WaitForAsyncUtils.waitForFxEvents();
      //verify success message.
      verifyThat("OK", NodeMatchers.isVisible());
      verifyThat("Email Sent", NodeMatchers.isVisible());
      verifyThat("The email weather report has been sent.", NodeMatchers.isVisible());
      clickOn("OK");
      WaitForAsyncUtils.waitForFxEvents();

      //check back on main window
      moveTo("#getWeatherBtn");
    }

    /**
    * Tests that attempts to remove entry when empty produces selection error.
    */
    @Test
    public void testRemoveWhenEmpty(){
      setupApplicationSkipSplashScreen();

      //when empty, remove produces error.
      clickOn("#removeSelectedBtn");
      WaitForAsyncUtils.waitForFxEvents();
      verifyThat("OK", NodeMatchers.isVisible());
      verifyThat("Location Selection Error", NodeMatchers.isVisible());
      verifyThat("Please select a city in the table to remove.", NodeMatchers.isVisible());
      clickOn("OK");
      WaitForAsyncUtils.waitForFxEvents();

      //when not empty, not selecting a location and clicking remove produces same error.
      //add data.
      doubleClickOn("#cityInputField");
      clickOn("#cityInputField");
      write("sydney");
      moveTo("#cityInputField");
      WaitForAsyncUtils.waitForFxEvents();
      moveBy(0,20);
      WaitForAsyncUtils.waitForFxEvents();
      Platform.runLater(() ->{
        setRobot(new Robot());
        setMousePosition(robot.getMouseX(),robot.getMouseY());
      });
      WaitForAsyncUtils.waitForFxEvents();
      clickOn(mouseXPosition,mouseYPosition);
      WaitForAsyncUtils.waitForFxEvents();
      clickOn("#getWeatherBtn");
      WaitForAsyncUtils.waitForFxEvents();
      //check the alert window is for the correct location
      Text textForLocation = find("#textForLocation");
      assert(textForLocation.getText().contains("Sydney"));
      clickOn("#closeWeatherAlertBtn");
      WaitForAsyncUtils.waitForFxEvents();

      //click remove.
      clickOn("#removeSelectedBtn");
      WaitForAsyncUtils.waitForFxEvents();
      verifyThat("OK", NodeMatchers.isVisible());
      verifyThat("Location Selection Error", NodeMatchers.isVisible());
      verifyThat("Please select a city in the table to remove.", NodeMatchers.isVisible());
      clickOn("OK");
      WaitForAsyncUtils.waitForFxEvents();


      //if u modify the table after selecting, the removal doesn't work.
      //select entry.
      moveTo("#weatherTable");
      moveBy(0,10);
      Platform.runLater(() ->{
        setRobot(new Robot());
        setMousePosition(robot.getMouseX(),robot.getMouseY());
      });
      WaitForAsyncUtils.waitForFxEvents();
      clickOn(mouseXPosition,mouseYPosition);
      WaitForAsyncUtils.waitForFxEvents();

      //add new data
      doubleClickOn("#cityInputField");
      clickOn("#cityInputField");
      write("juba");
      moveTo("#cityInputField");
      WaitForAsyncUtils.waitForFxEvents();
      moveBy(0,20);
      WaitForAsyncUtils.waitForFxEvents();
      Platform.runLater(() ->{
        setRobot(new Robot());
        setMousePosition(robot.getMouseX(),robot.getMouseY());
      });
      WaitForAsyncUtils.waitForFxEvents();
      clickOn(mouseXPosition,mouseYPosition);
      WaitForAsyncUtils.waitForFxEvents();
      clickOn("#getWeatherBtn");
      WaitForAsyncUtils.waitForFxEvents();
      //check the alert window is for the correct location
      Text textForLocationJuba = find("#textForLocation");
      assert(textForLocationJuba.getText().contains("Juba"));
      clickOn("#closeWeatherAlertBtn");
      WaitForAsyncUtils.waitForFxEvents();

      //click remove.
      //now click remove.
      clickOn("#removeSelectedBtn");
      WaitForAsyncUtils.waitForFxEvents();
      //check error
      WaitForAsyncUtils.waitForFxEvents();
      verifyThat("OK", NodeMatchers.isVisible());
      verifyThat("Location Selection Error", NodeMatchers.isVisible());
      verifyThat("Please select a city in the table to remove.", NodeMatchers.isVisible());
      clickOn("OK");
    }

    /**
    * Tests valid remove city scenarios. Includes basic removal, as well as selecting a city, going to other page,
    * returning to main menu and clicking remove.
    * Ensures multiple removals can occur, and that the table and map updates correctly.
    * Also ensures that once removed, the same city can be re-added, and also removed again.
    */
    @Test
    public void testRemoveCityValidScenarios(){
      setupApplicationSkipSplashScreen();

      //basic removal.
      //add data first.
      doubleClickOn("#cityInputField");
      clickOn("#cityInputField");
      write("juba");
      moveTo("#cityInputField");
      WaitForAsyncUtils.waitForFxEvents();
      moveBy(0,20);
      WaitForAsyncUtils.waitForFxEvents();
      Platform.runLater(() ->{
        setRobot(new Robot());
        setMousePosition(robot.getMouseX(),robot.getMouseY());
      });
      WaitForAsyncUtils.waitForFxEvents();
      clickOn(mouseXPosition,mouseYPosition);
      WaitForAsyncUtils.waitForFxEvents();
      clickOn("#getWeatherBtn");
      WaitForAsyncUtils.waitForFxEvents();
      //check the alert window is for the correct location
      Text textForLocationJuba = find("#textForLocation");
      assert(textForLocationJuba.getText().contains("Juba"));
      clickOn("#closeWeatherAlertBtn");
      WaitForAsyncUtils.waitForFxEvents();

      //remove the data.
      moveTo("#weatherTable");
      moveBy(0,10);
      Platform.runLater(() ->{
        setRobot(new Robot());
        setMousePosition(robot.getMouseX(),robot.getMouseY());
      });
      WaitForAsyncUtils.waitForFxEvents();
      clickOn(mouseXPosition,mouseYPosition);
      WaitForAsyncUtils.waitForFxEvents();

      //remove it.
      clickOn("#removeSelectedBtn");
      WaitForAsyncUtils.waitForFxEvents();

      //check it was removed from tableView and weather map.
      TableView tableView = find("#weatherTable");
      WorldMapView worldMapView = find("#weatherMap");
      assert(tableView.getItems().size()==0);
      assert(worldMapView.locationsProperty().size()==0);

      //if you select an entry, go to a different page and return, clicking remove still removes it.
      //add new data.
      doubleClickOn("#cityInputField");
      clickOn("#cityInputField");
      write("juba");
      moveTo("#cityInputField");
      WaitForAsyncUtils.waitForFxEvents();
      moveBy(0,20);
      WaitForAsyncUtils.waitForFxEvents();
      Platform.runLater(() ->{
        setRobot(new Robot());
        setMousePosition(robot.getMouseX(),robot.getMouseY());
      });
      WaitForAsyncUtils.waitForFxEvents();
      clickOn(mouseXPosition,mouseYPosition);
      WaitForAsyncUtils.waitForFxEvents();
      clickOn("#getWeatherBtn");
      WaitForAsyncUtils.waitForFxEvents();
      //check the alert window is for the correct location
      Text textForLocationJubaAgain = find("#textForLocation");
      assert(textForLocationJubaAgain.getText().contains("Juba"));
      clickOn("#closeWeatherAlertBtn");
      WaitForAsyncUtils.waitForFxEvents();
      //select entry.

      moveTo("#weatherTable");
      moveBy(0,10);
      Platform.runLater(() ->{
        setRobot(new Robot());
        setMousePosition(robot.getMouseX(),robot.getMouseY());
      });
      WaitForAsyncUtils.waitForFxEvents();
      clickOn(mouseXPosition,mouseYPosition);
      WaitForAsyncUtils.waitForFxEvents();

      //now go to about page and back.
      clickOn("Menu");
      clickOn("#about");
      WaitForAsyncUtils.waitForFxEvents();
      clickOn("#aboutPageBackBtn");
      WaitForAsyncUtils.waitForFxEvents();

      //now click remove.
      clickOn("#removeSelectedBtn");
      WaitForAsyncUtils.waitForFxEvents();

      //check it removed.
      assert(tableView.getItems().size()==0);
      assert(worldMapView.locationsProperty().size()==0);

      //test that if multiple are selected, the last selected is what is removed.
      //add new data.
      doubleClickOn("#cityInputField");
      clickOn("#cityInputField");
      write("juba");
      moveTo("#cityInputField");
      WaitForAsyncUtils.waitForFxEvents();
      moveBy(0,20);
      WaitForAsyncUtils.waitForFxEvents();
      Platform.runLater(() ->{
        setRobot(new Robot());
        setMousePosition(robot.getMouseX(),robot.getMouseY());
      });
      WaitForAsyncUtils.waitForFxEvents();
      clickOn(mouseXPosition,mouseYPosition);
      WaitForAsyncUtils.waitForFxEvents();
      clickOn("#getWeatherBtn");
      WaitForAsyncUtils.waitForFxEvents();
      clickOn("#closeWeatherAlertBtn");
      WaitForAsyncUtils.waitForFxEvents();
      //sydney
      doubleClickOn("#cityInputField");
      clickOn("#cityInputField");
      write("sydney");
      moveTo("#cityInputField");
      WaitForAsyncUtils.waitForFxEvents();
      moveBy(0,20);
      WaitForAsyncUtils.waitForFxEvents();
      Platform.runLater(() ->{
        setRobot(new Robot());
        setMousePosition(robot.getMouseX(),robot.getMouseY());
      });
      WaitForAsyncUtils.waitForFxEvents();
      clickOn(mouseXPosition,mouseYPosition);
      WaitForAsyncUtils.waitForFxEvents();
      clickOn("#getWeatherBtn");
      WaitForAsyncUtils.waitForFxEvents();
      clickOn("#closeWeatherAlertBtn");
      WaitForAsyncUtils.waitForFxEvents();

      //select juba.
      moveTo("#weatherTable");
      moveBy(0,10);
      Platform.runLater(() ->{
        setRobot(new Robot());
        setMousePosition(robot.getMouseX(),robot.getMouseY());
      });
      WaitForAsyncUtils.waitForFxEvents();
      clickOn(mouseXPosition,mouseYPosition);

      //select sydney
      moveTo("#weatherTable");
      moveBy(0,30);
      Platform.runLater(() ->{
        setRobot(new Robot());
        setMousePosition(robot.getMouseX(),robot.getMouseY());
      });
      WaitForAsyncUtils.waitForFxEvents();
      clickOn(mouseXPosition,mouseYPosition);

      //remove.
      clickOn("#removeSelectedBtn");
      WaitForAsyncUtils.waitForFxEvents();
      //check sydney was removed.
      assert(tableView.getItems().size()==1);
      assert(worldMapView.locationsProperty().size()==1);
      //check juba's city id and location is all that is left in table and map.
      assert(((LocationWeather) tableView.getItems().get(0)).getCityId() == 373303);
      assert(worldMapView.locationsProperty().get(0).getName().equals("a01d"));
      assert(worldMapView.locationsProperty().get(0).getLatitude() == 4.85165);
      assert(worldMapView.locationsProperty().get(0).getLongitude() == 31.58247);
    }

    /**
    * Tests that alerts cannot be avoided by ensuring nothing works on the main menu until the alerts are
    * responded to.
    */
    @Test
    public void testAlertsApplicationModal(){
      //click remove selected to get an alert.
      setupApplicationSkipSplashScreen();

      clickOn("#removeSelectedBtn");
      WaitForAsyncUtils.waitForFxEvents();

      //try to click send report.
      clickOn("#sendReportBtn");
      WaitForAsyncUtils.waitForFxEvents();

      //if it wasn't application modal, the send report would be on top.
      //so check the remove selected error is still showing.
      verifyThat("OK", NodeMatchers.isVisible());
      verifyThat("Location Selection Error", NodeMatchers.isVisible());
      verifyThat("Please select a city in the table to remove.", NodeMatchers.isVisible());
      clickOn("OK");
      WaitForAsyncUtils.waitForFxEvents();

      //now check on main menu.
      moveTo("#getWeatherBtn");
    }

  }
