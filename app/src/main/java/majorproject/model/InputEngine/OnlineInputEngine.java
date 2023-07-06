package majorproject.model.inputengine;

import majorproject.model.pojo.CurrentWeatherData;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
* An implementation of InputEngine that uses weatherbit.io API as the source of weather data for city locations.
*/
public class OnlineInputEngine implements InputEngine {

  public String getCurrentWeatherForLocation(String apiKey, int cityId, double lat, double lon) throws Exception {
    try {
      HttpRequest request = HttpRequest.newBuilder(new
        URI("https://api.weatherbit.io/v2.0/current?lat="+Double.toString(lat)+"&lon="+Double.toString(lon)+"&key="+apiKey))
      .GET()
      .build();
      HttpClient client = HttpClient.newBuilder().build();
      HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

      if(Integer.valueOf(response.statusCode())!=200){
        throw new Exception("Weatherbit API Error: Response code " + response.statusCode());
      }

      if(response.body() != null){
        return response.body();
      }

    } catch (IOException | InterruptedException e) {
      throw new Exception("Weatherbit API Error: Response code " + e.getMessage());
    } catch (URISyntaxException ignored) {
      throw new Exception("Error with environment variable inputs.");
    }
    return null;
  }
}
