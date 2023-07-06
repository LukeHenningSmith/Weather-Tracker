package majorproject.presenter;

import majorproject.model.ModelEngine;
import majorproject.model.observers.*;
import majorproject.model.pojowrapper.LocationWeather;
import majorproject.model.pojo.*;

import majorproject.view.ViewCoordinator;
import majorproject.view.window.*;

import java.util.*;
import java.util.concurrent.*;

import javafx.concurrent.Task;
import javafx.application.Platform;

/**
* The Presenter class for the application (according to the MVP multi-tier architecture).
* Responsible for 'presenting' the model data to the view based on the inputs provided by the user to the view that are given to the presenter.
*/
public class WeatherPresenter implements WeatherObserver, LoadingObserver, CacheHitObserver {

  private final ModelEngine model;

  private final ViewCoordinator viewCoordinator;

  //filled by the returns from viewcoordinator methods
  private MainWindow mainWindow;
  private SplashScreenWindow splashScreenWindow;
  private AboutPageWindow aboutPageWindow;

  private Task<Integer> task;

  //Thread pool manager
  private final ExecutorService pool = Executors.newFixedThreadPool(2, runnable -> {
    Thread thread = new Thread(runnable);
    thread.setDaemon(true);
    return thread;
  });

  /**
  * Constructs the Presenter. Registers presenter as the observer for WeatherObserver, LoadingObserver and
  * CacheHitObserver in the model. Finishes by requesting the view to display the splash screen.
  * @param model The ModelEngine instance for the application.
  * @param viewCoordinator The ViewCoordinator object that the presenter communicates with to switch the view windows.
  */
  public WeatherPresenter(ModelEngine model, ViewCoordinator viewCoordinator){
    this.model=model;
    this.viewCoordinator=viewCoordinator;

    //need to subscribe this to WeatherObserver
    this.model.registerWeatherObserver(this);
    this.model.registerLoadingObserver(this);
    this.model.registerCacheHitObserver(this);

    //presenter makes splash screen go first.
    displaySplashScreenWindow();
  }

  /**
  * Implementation of LoadingObserver interface method. Is called when a loading state changes in the model.
  */
  public void updateLoadingObserver(){
    Platform.runLater(() ->{
      mainWindow.loadingStateChange(this.model.getIsLoadingState());
    });
  }

  /**
  * Implementation of WeatherObserver interface method. Is called when a weather data changes in the model.
  */
  public void updateWeatherObserver(){
    Platform.runLater(() ->{
      mainWindow.weatherDataChange(this.model.getLocationWeather());
    });
  }

  /**
  * Switches the current displayed window to the Splash Screen by making request to the viewCoordinator.
  */
  public void displaySplashScreenWindow(){
    this.splashScreenWindow = viewCoordinator.displaySplashScreenWindow(this);
  }

  /**
  * Switches the current displayed window to the Main Window by making request to the viewCoordinator.
  */
  public void displayMainWindow(){
    this.mainWindow = viewCoordinator.displayMainWindow(this,this.model.getAllCityLocations());
  }

  /**
  * Switches the current displayed window to the About page window by making request to the viewCoordinator.
  */
  public void displayAboutPageWindow(){
    this.aboutPageWindow = viewCoordinator.displayAboutWindow(this,this.model.getAboutInformation());
  }

  /**
  * Attempts to remove a current weather data entry. Only the first index is removed.
  * @param selectedIndices A list of Object[] indices of the weather attempting to be removed. Only the first index is removed.
  */
  public void removeCurrentWeather(Object[] selectedIndices){
    if(selectedIndices==null || selectedIndices.length<1){
      mainWindow.displayErrorAlert("Location Selection Error","Please select a city in the table to remove.");
    } else {
      int index = (Integer) selectedIndices[0];
      boolean result = this.model.removeAtIndex(index);
      if(!result){
        mainWindow.displayErrorAlert("Error Removing Location","An error occurred. Please try again later.");
      }
    }
  }

  /**
  * Deletes all current weather data by requesting ModelEngine to clear.
  */
  public void clearCurrentWeather(){
    if(this.model.sizeOfSavedData() < 1){
      mainWindow.displayErrorAlert("Clear Failed","There is no data to clear.");
    } else {
      this.model.clearData();
    }
  }

  /**
  * Deletes all current weather data in the cache by requesting ModelEngine to clear the cache.
  */
  public void clearCache(){
    this.model.clearCache();
  }

  /**
  * Method to run the sendReport task on separate thread. Uses Platform.runLater() to make changes
  * to the JavaFX thread.
  * @param emailGiven The email to send the weather report to.
  */
  public void sendReport(String emailGiven) {
    final String emailToSendTo = emailGiven;
    task = new Task<>() {
      @Override
      protected Integer call() {
        if(emailToSendTo==null){
          //must have clicked exit or cancel.
          return 0;
        }
        if(emailToSendTo.length()<3){
          Platform.runLater(() ->{
            mainWindow.displayErrorAlert("Invalid Email","The email given was invalid.");
          });
          return 1;
        }
        Platform.runLater(() ->{
          mainWindow.disableButtonsDuringReportSend();
        });
        try {
          model.sendReport(emailToSendTo);
        } catch (IllegalArgumentException e){
          Platform.runLater(() ->{
            mainWindow.enableButtonsAfterReportSend();
            mainWindow.displayErrorAlert("Email Failed",e.getMessage());
          });
          return 1;
        }
        Platform.runLater(() ->{
          mainWindow.enableButtonsAfterReportSend();
          mainWindow.displayInformationAlert("Email Sent","The email weather report has been sent.");
        });
        return 0;
      }

    };
    pool.execute(task);
  }

  //Flag to store the pending weather data request (if it exists, otherwise null).
  private CityLocation pendingWeatherForLocation;

  //flag to store responses from askIfUseCacheAndWait().
  private boolean cacheUsed = false;

  /**
  * Method to get weather data for a given location, achieved by request to model engine.
  * @param location The location the weather is being requested for.
  * @param inputField The string typed in by the user to request the location.
  */
  public void getWeatherDataForCityLocation(CityLocation location,String inputField) {
    task = new Task<>() {
      @Override
      protected Integer call() {
        LocationWeather data;
        //check input field is the same as the location.
        if(location==null || !location.toString().equals(inputField)){
          Platform.runLater(() ->{
            mainWindow.displayErrorAlert("Location Selection Error","Please select a city from the drop down list.");
          });
          return 1;
        }

        //pending.
        Platform.runLater(() ->{
          mainWindow.disableButtonsDuringGetWeather();
        });
        pendingWeatherForLocation=location;
        //is null as don't know if us or not use cache.
        try {
          data = model.getWeatherDataForCityLocation(location,null);
        } catch (IllegalArgumentException e){
          //means it errored.
          // String mostRecentError = model.getMostRecentError();
          Platform.runLater(() ->{
            mainWindow.enableButtonsAfterGetWeather();
            mainWindow.displayErrorAlert("Get Weather Error",e.getMessage());
          });
          return 1;
        }
        //if it is null here, no error, but means the cache hit. will be handled elsewhere.
        //if didn't cache hit:
        if(data!=null){
          //not pending
          pendingWeatherForLocation=null;
          //handle it
          Platform.runLater(() ->{
            mainWindow.enableButtonsAfterGetWeather();
            mainWindow.displayWeather(data);
          });
        }
        return 0;
      }
    };
    pool.execute(task);
  }

  /**
  * Implementation of CacheHitObserver interface method. Is called when a cache hit occurs.
  */
  public void cacheHit(){
    //for this to be called, buttons are already disabled, and pendingWeatherForLocation is not null.
    //this is executing on the newly made thread.
    //need to ask the user to use cache.
    LocationWeather data;
    try{
      askIfUseCacheAndWait();
    } catch (InterruptedException ignored){}
      if(cacheUsed){
        //use cache
        try {
          data=model.getWeatherDataForCityLocation(pendingWeatherForLocation, true);
        } catch (IllegalArgumentException e){
          //means it errored.
          Platform.runLater(() ->{
            mainWindow.displayErrorAlert("Get Weather Error",e.getMessage());
            mainWindow.enableButtonsAfterGetWeather();
          });
          return;
        }
      } else {
        try {
          data=model.getWeatherDataForCityLocation(pendingWeatherForLocation, false);
        } catch (IllegalArgumentException e){
          //means it errored.
          Platform.runLater(() ->{
            mainWindow.displayErrorAlert("Get Weather Error",e.getMessage());
            mainWindow.enableButtonsAfterGetWeather();
          });
          return;
        }
      }
      //if made it here, didn't error.
      Platform.runLater(() ->{
        mainWindow.displayWeather(data);
        mainWindow.enableButtonsAfterGetWeather();
      });
    }

    /**
    * Supporting method to ensure thread halts whilst waiting for cache response.
    */
    private void askIfUseCacheAndWait() throws InterruptedException {
      Semaphore semaphore = new Semaphore(0);
      Platform.runLater(() -> {
        cacheUsed = mainWindow.askIfCacheUsed();
        semaphore.release();
      });
      semaphore.acquire();
    }

  }
