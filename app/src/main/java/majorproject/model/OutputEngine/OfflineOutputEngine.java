package majorproject.model.outputengine;

import java.util.List;

import majorproject.model.pojowrapper.LocationWeather;
import majorproject.model.pojo.CurrentWeatherData;

/**
* An 'offline' or 'dummy' implementation of OutputEngine that doesn't interact with any web APIs. Simply returns static correctly formatted data.
*/
public class OfflineOutputEngine implements OutputEngine {

  public void sendEmail(String apiKey, String emailBody){
    System.out.println("The email that would have been sent would have the content of:");
    System.out.println(emailBody);
  }
}
