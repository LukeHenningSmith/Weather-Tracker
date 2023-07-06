package majorproject.model.outputengine;

import majorproject.model.pojowrapper.LocationWeather;

import java.util.List;

/**
* Interface for interacting with output API that sends emails based on json formatted email information.
*/
public interface OutputEngine {

  /**
  * Method to send email based on json formatted string of email body.
  * @param apiKey The API key used to access the output web API.
  * @param emailBody The content, recipient and subject formatted as a json string. Example format is: "{\"personalizations\":[{\"to\":[{\"email\":\"example@email.com\",\"name\":\"target email\"}]}],\"from\":{\"email\":\"dummyEmail\",\"name\":\"Weather Tracker\"},\"subject\":\"Weather report\",\"content\":[{\"type\":\"text/html\",\"value\":\"htmlFormattedEmailContent\"}]}";
  * @throws Exception Throws an exception if an error occurs, with message attached.
  */
  public void sendEmail(String apiKey, String emailBody) throws Exception;
}
