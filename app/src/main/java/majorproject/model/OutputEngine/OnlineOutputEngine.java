package majorproject.model.outputengine;

import majorproject.model.pojowrapper.LocationWeather;
import majorproject.model.pojo.CurrentWeatherData;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
* An implementation of OutputEngine that uses sendgrid API as the method of sending emails.
*/
public class OnlineOutputEngine implements OutputEngine {

  public void sendEmail(String apiKey, String emailBody) throws Exception {
    try {
      String uri = "https://api.sendgrid.com/v3/mail/send";
      HttpRequest request = HttpRequest.newBuilder(new URI(uri))
      .headers("Authorization","Bearer "+apiKey)
      .header("Content-Type", "application/json")
      .POST(HttpRequest.BodyPublishers.ofString(emailBody))
      .build();
      HttpClient client = HttpClient.newBuilder().build();
      HttpResponse<String> response = client.send(request,HttpResponse.BodyHandlers.ofString());

      if(Integer.valueOf(response.statusCode())!=202){
        throw new Exception("Sendgrid API error: Response code "+response.statusCode());
      }
    } catch (IOException | InterruptedException e) {
      throw new Exception("Network error when trying to send email.");
    } catch (URISyntaxException ignored) {
      throw new Exception("Something went wrong with environment variables.");
    }
  }
}
