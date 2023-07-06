package majorproject.model.inputengine;

import majorproject.model.pojo.CurrentWeatherData;

/**
* Interface for interacting with input API that returns json formatted weather data strings for latitude/longitude locations.
*/
public interface InputEngine {

  /**
  * For a given latitude and longitude, gets the json formatted string for that location from the input API.
  * @param apiKey The API key used to query the input API.
  * @param cityId The associated cityId with the given latitude/longitude.
  * @param lat The latitude value of location in real life.
  * @param lon The longitude value of location in real life.
  * @return The json formatted string of location if it exists, otherwise NULL.
  * @throws Exception Throws an exception if an error occurs, with message attached.
  Example format: "{\"data\":[{\"rh\":80.6875,\"pod\":\"n\",\"lon\":0.0,\"pres\":1004,\"timezone\":\"placeholder\",\"ob_time\":\"211\",\"country_code\":\"BD\",\"clouds\":0,\"ts\":1652274295,\"solar_rad\":0,\"state_code\":
  \"85\",\"city_name\":\"Placeholder\",\"wind_spd\":0.0,\"wind_cdir_full\":\"Placeholder\",\"wind_cdir\":\"S\",\"slp\":1004.5,\"vis\":1.5,\"h_angle\":90,\"sunset\":\"12:31\",\"dni\":0,\"dewpt\":24.1,
  \"snow\":0,\"uv\":0,\"precip\":0.0,\"wind_dir\":179,\"sunrise\":\"23:17\",\"ghi\":0,\"dhi\":0,\"aqi\":0,\"lat\":0.0,\"weather\":{\"icon\":\"a01d\",\"code\":800,\"description\":\"Placeholder\"},
  \"datetime\":\"2022-05-11:12\",\"temp\":0.0,\"station\":\"VGHS\",\"elev_angle\":-7.49,\"app_temp\":31.3}],\"count\":1}";
  */
  public String getCurrentWeatherForLocation(String apiKey, int cityId, double lat, double lon) throws Exception;
}
