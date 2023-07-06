package majorproject.model;

import majorproject.model.pojowrapper.LocationWeather;
import majorproject.model.pojo.*;
import majorproject.model.*;
import majorproject.model.inputengine.*;
import majorproject.model.outputengine.*;
import majorproject.model.cacheengine.*;
import majorproject.model.utility.*;
import majorproject.model.observers.*;

import java.util.List;
import java.util.*;

/**
* The main access point to the model package. Contains the state of the model (weather data in LocationWeather objects),
* controlling all attempts to modify the state of the model.
*/
public class ModelEngine {

  private final List<CityLocation> allCityLocations;

  private final InputEngine inputEngine;
  private final OutputEngine outputEngine;
  private final CacheEngine cacheEngine;

  private List<LocationWeather> allCollectedWeatherData = new ArrayList<LocationWeather>();

  private final About aboutInformation;

  private final Set<WeatherObserver> weatherObservers;
  private final Set<LoadingObserver> loadingObservers;
  private final Set<CacheHitObserver> cacheHitObservers;

  private final String sendgridFromEmail;
  private final String sendgridAPIKey;
  private final String weatherbitAPIKey;

  //flag to indicate loading state of the model.
  private boolean isLoading;

  /**
  * Constructs ModelEngine, dictating what InputEngine, OutputEngine and CacheEngine it will communicate with,
  * as well as the locations it will be able to display. The necessary API keys / email are given as arguments.
  * Constructs all available CityLocation objects and application About object.
  * @param inputEngine The InputEngine to send 'get' weather requests for location to.
  * @param outputEngine The OutputEngine to send reports of stored weather to.
  * @param cacheEngine The CacheEngine to be used to cache the weather data for searched citites.
  * @param weatherbitAPIKey The String API key for weatherbit.io API.
  * @param sendgridAPIKey The String API key for sendgrid API.
  * @param sendgridFromEmail The String email to send from when using the sendgrid API.
  * @param locationsFileLoader Injected utility class that loads city location data.
  */
  public ModelEngine(InputEngine inputEngine, OutputEngine outputEngine, CacheEngine cacheEngine,
        String weatherbitAPIKey, String sendgridAPIKey, String sendgridFromEmail,
        LocationsFileLoader locationsFileLoader){
    this.inputEngine=inputEngine;
    this.outputEngine=outputEngine;
    this.cacheEngine=cacheEngine;

    this.weatherbitAPIKey=weatherbitAPIKey;
    this.sendgridFromEmail=sendgridFromEmail;
    this.sendgridAPIKey=sendgridAPIKey;
    this.allCityLocations = locationsFileLoader.getAllCityLocations();

    this.aboutInformation = constructAboutInformation();

    weatherObservers = new HashSet<>();
    loadingObservers = new HashSet<>();
    cacheHitObservers = new HashSet<>();
  }

  /**
  * Adds a WeatherObserver to list of those currently observing weather data changes.
  * @param weatherObserver The WeatherObserver wanting to be notified by weather data changes.
  */
  public void registerWeatherObserver(WeatherObserver weatherObserver) {
    this.weatherObservers.add(weatherObserver);
  }

  /**
  * Private method to notify each WeatherObserver of a change in weather data in the ModelEngine.
  */
  private void updateWeatherObservers() {
    for (WeatherObserver observer: weatherObservers) {
      observer.updateWeatherObserver();
    }
  }

  /**
  * Adds a LoadingObserver to list of those currently observing changes to loading state of the model.
  * @param loadingObserver The LoadingObserver wanting to be notified by loading state changes.
  */
  public void registerLoadingObserver(LoadingObserver loadingObserver) {
    this.loadingObservers.add(loadingObserver);
  }

  /**
  * Private method to notify each LoadingObserver of a change in the loading state in the ModelEngine.
  */
  private void updateLoadingObservers() {
    for (LoadingObserver observer: loadingObservers) {
      observer.updateLoadingObserver();
    }
  }

  /**
  * Adds a CacheHitObserver to list of those currently observing if a cache hit has occurred.
  * @param cacheHitObserver The CacheHitObserver wanting to be notified when cache hits occur.
  */
  public void registerCacheHitObserver(CacheHitObserver cacheHitObserver) {
    this.cacheHitObservers.add(cacheHitObserver);
  }

  /**
  * Private method to notify each CacheHitObserver of a cache hit.
  */
  private void notifyCacheHitObservers() {
    for (CacheHitObserver observer: cacheHitObservers) {
      observer.cacheHit();
    }
  }

  /**
  * Getter method for the loading state of the ModelEngine.
  * @return Returns boolean of true if loading, false if not currently loading.
  */
  public boolean getIsLoadingState(){
    return this.isLoading;
  }

  /**
  * Private method to construct the About information for the application.
  * Includes information about the resources found in resources folder.
  */
  private About constructAboutInformation(){
    String creatorName = "Luke Henning-Smith";
    String applicationName = "Weather Tracker";
    List<String> references = new ArrayList<String>();
    references.add("Splash image:  https://www.efio.dk/");
    references.add("Input API:  https://www.weatherbit.io/api");
    references.add("Weather icons:  https://www.weatherbit.io/api/meta");
    references.add("15,000+ Pop. Cities:  https://www.weatherbit.io/api/meta");
    references.add("Output API:  https://sendgrid.com/solutions/email-api/");
    references.add("JavaFX 17 UI:  https://openjfx.io/javadoc/17/");
    references.add("JUnit Jupiter 5.8.2:  https://junit.org/junit5/docs/current/api/");
    references.add("Mockito-Junit-Jupiter 4.4.0:  https://javadoc.io/doc/org.mockito/mockito-junit-jupiter/4.4.0/index.html");
    references.add("Gson library:  https://javadoc.io/doc/com.google.code.gson/gson/latest/com.google.gson/com/google/gson/Gson.html");
    return new About(applicationName,creatorName,references);
  }

  /**
  * Getter method for the About information of the application.
  * @return An About object containing the about information for the application.
  */
  public About getAboutInformation(){
    return this.aboutInformation;
  }

  /**
  * Simple get method for all the city locations the ModelEngine can interpret and store weather for.
  * @return The List of CityLocation objects.
  */
  public List<CityLocation> getAllCityLocations(){
    return Collections.unmodifiableList(this.allCityLocations);
  }

  /**
  * Removes all stored city weather data.
  */
  public void clearData(){
    //clears weather data
    this.allCollectedWeatherData = new ArrayList<LocationWeather>();
    updateWeatherObservers();
  }

  /**
  * Removes a stored weather data at a specific index.
  * @param index The index to be removed.
  * @return Returns true if removed, returns false otherwise.
  */
  public boolean removeAtIndex(int index){
    //given in index, remove that in data.
    if(index<0 || index>sizeOfSavedData()){
      return false;
    }
    //else, remove that index.
    this.allCollectedWeatherData.remove(index);
    updateWeatherObservers();
    return true;
  }

  /**
  * Returns the number of cities stored in the current weather data.
  * @return An int of the number of cities stored in the current weather data.
  */
  public int sizeOfSavedData(){
    return this.allCollectedWeatherData.size();
  }

  /**
  * Sends a report to OutputEngine for a given toEmail. Uses JsonWeatherParser to convert current data to
  * json formatted email String which contains the necessary API output key, to email and from email.
  * @param toEmail The email string to send it to.
  * @throws IllegalArgumentException Throws an exception if error occurred due to the inputs provided in ModelEngine or in its dependencies (OutputEngine).
  */
  public void sendReport(String toEmail) throws IllegalArgumentException {
    //email is not null or empty, but no guarantee it is valid.
    if(this.allCollectedWeatherData.size()==0){
      throw new IllegalArgumentException("Weather data is empty.");
    }
    if(toEmail==null || toEmail.length()<1){
      throw new IllegalArgumentException("Please enter a valid 'to' email.");
    }
    if(sendgridFromEmail==null){
      throw new IllegalArgumentException("The Sendgrid 'from' email is not set in environment variable \"SENDGRID_API_EMAIL\"");
    }
    if(sendgridAPIKey==null){
      throw new IllegalArgumentException("The Sendgrid API key is not set in environment variable \"SENDGRID_API_KEY\"");
    }
    this.isLoading=true;
    updateLoadingObservers();

    //first, format current weather data to email.
    String jsonFormattedEmail = JsonWeatherParser.parseCurrentWeatherDataToJsonString(this.allCollectedWeatherData,toEmail,sendgridFromEmail);
    try {
      outputEngine.sendEmail(sendgridAPIKey,jsonFormattedEmail);
    } catch (Exception e){
      this.isLoading=false;
      updateLoadingObservers();
      throw new IllegalArgumentException(e.getMessage());
    }

    this.isLoading=false;
    updateLoadingObservers();
  }

  /**
  * A simple get method for all currently stored weather data for cities.
  * @return List of LocationWeather objects of all the currently stored weather data.
  */
  public List<LocationWeather> getLocationWeather(){
    return Collections.unmodifiableList(this.allCollectedWeatherData);
  }

  /**
  * Method to get weather data for a given CityLocation.
  * Is responsible for coordinating cache usage, as well as notifying LoadingObserver, WeatherObserver
  * and CacheHitObserver of all appropriate events.
  * @param cityLocation the CityLocation to get the weather data of.
  * @param useCache A Boolean flag that is null to indicate cache usage unknown, true to indicate wants to use cache and false to indicate no cache usage wanted.
  * @return The LocationWeather object containing the CurrentWeatherData and CityLocation. Returns null if cache hit occurred, and unsure how to proceed.
  * @throws IllegalArgumentException Throws exception if any error occurs in the ModelEngine or its dependencies (InputEngine).
  */
  public LocationWeather getWeatherDataForCityLocation(CityLocation cityLocation, Boolean useCache) throws IllegalArgumentException {
    //if given true to useCache, should attempt to use cache if it exists
    String jsonString = null;
    if(cityLocation==null){
      throw new IllegalArgumentException("Invalid city location.");
    }
    if(useCache==null){
      //doesn't know if should use cache
      if(this.cacheEngine.checkWeatherDataExistsInCache(cityLocation.getCityId())){
        //cache exists
        notifyCacheHitObservers();
        return null;
      } else {
        //cache doesn't exist.
        //check api key
        if(weatherbitAPIKey==null){
          throw new IllegalArgumentException("The Weatherbit API key is not set in environment variable 'INPUT_API_KEY'");
        }
        this.isLoading=true;
        updateLoadingObservers();
        try {
          jsonString = this.inputEngine.getCurrentWeatherForLocation(weatherbitAPIKey,cityLocation.getCityId(),cityLocation.getLatitude(),cityLocation.getLongitude());
        } catch (Exception e){
          //forward the error on
          throw new IllegalArgumentException(e.getMessage());
        }
        this.isLoading=false;
        updateLoadingObservers();
      }
    } else {
      //useCache is not null
      if(useCache){
        //wants to use the cache, still must check cache exists.
        if(this.cacheEngine.checkWeatherDataExistsInCache(cityLocation.getCityId())){
          //cache exists, use it
          jsonString = this.cacheEngine.getWeatherDataForCityFromCache(cityLocation.getCityId());
        } else {
          //cache doesn't exist. Get fresh data. Check API key first.
          if(weatherbitAPIKey==null){
            throw new IllegalArgumentException("The Weatherbit API key is not set in environment variable 'INPUT_API_KEY'");
          }
          this.isLoading=true;
          updateLoadingObservers();
          try {
            jsonString = this.inputEngine.getCurrentWeatherForLocation(weatherbitAPIKey,cityLocation.getCityId(),cityLocation.getLatitude(),cityLocation.getLongitude());
          } catch (Exception e){ //forward error
            throw new IllegalArgumentException(e.getMessage());
          }
          this.isLoading=false;
          updateLoadingObservers();
        }
      } else {
        //doesn't want to use the cache. Check api key
        if(weatherbitAPIKey==null){
          throw new IllegalArgumentException("The Weatherbit API key is not set in environment variable 'INPUT_API_KEY'");
        }
        this.isLoading=true;
        updateLoadingObservers();
        try {
          jsonString = this.inputEngine.getCurrentWeatherForLocation(weatherbitAPIKey,cityLocation.getCityId(),cityLocation.getLatitude(),cityLocation.getLongitude());
        } catch (Exception e){ //forward the error
          throw new IllegalArgumentException(e.getMessage());
        }
        this.isLoading=false;
        updateLoadingObservers();
      }
    }
    //once here, some data must have been received, so check if need to add or modify to the cache.
    if(jsonString!=null){
      if(this.cacheEngine.checkWeatherDataExistsInCache(cityLocation.getCityId())){
        //modify
          this.cacheEngine.modifyExistingWeatherDataInCache(cityLocation.getCityId(),jsonString);
      }  else { //add
          this.cacheEngine.addWeatherDataToCache(cityLocation.getCityId(),jsonString);
      }
    } else {
      throw new IllegalArgumentException("Invalid city location.");
    }

    //convert jsonString
    CurrentWeatherData data = JsonWeatherParser.parseJsonStringToCurrentWeatherData(jsonString);

    //if the weather for that location has all been retreived, remove it from the list, and add it to the end as usual.
    int cityId = cityLocation.getCityId();
    for(int i=0; i<this.allCollectedWeatherData.size();i++){
      if(this.allCollectedWeatherData.get(i).getCityId()==cityId){
        removeAtIndex(i);
      }
    }

    //add the weather data.
    LocationWeather locWeath = new LocationWeather(data,cityLocation);
    this.allCollectedWeatherData.add(locWeath);

    updateWeatherObservers();
    return locWeath;
  }

  /**
  * Method to clear all cached data in CacheEngine.
  */
  public void clearCache(){
    this.cacheEngine.clearCache();
  }

}
