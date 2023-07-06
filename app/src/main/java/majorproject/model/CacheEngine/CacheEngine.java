package majorproject.model.cacheengine;

/**
* Interface for caching jsonString formatted city weather data into a database.
*/
public interface CacheEngine {

  /**
  * Adds json string formatted data into database with primary key of cityId.
  * @param cityId The id of the city, to be used as the primary key.
  * @param jsonString The json formatted string containing weather data for a city. Example format: "{\"data\":[{\"rh\":80.6875,\"pod\":\"n\",\"lon\":0.0,\"pres\":1004,\"timezone\":\"placeholder\",\"ob_time\":\"2022-05-11\",\"country_code\":\"BD\",\"clouds\":0,\"ts\":1652274295,\"solar_rad\":0,\"state_code\":\"85\",\"city_name\":\"Placeholder\",\"wind_spd\":0.0,\"wind_cdir_full\":\"Placeholder\",\"wind_cdir\":\"S\",\"slp
  \":1004.5,\"vis\":1.5,\"h_angle\":90,\"sunset\":\"12:31\",\"dni\":0,\"dewpt\":24.1,\"snow\":0,\"uv\":0,\"precip\":0.0,\"wind_dir\":179,\"sunrise\":\"23:17\",\"ghi\":0,\"dhi\":0,\"aqi\":0,\"lat\":0.0,\"weather\":{\"icon\":\"a01d\",\"code\":800,\"description\":\"Placeholder\"},\"datetime\":\"2022-05-11:12\",\"temp\":0.0,\"station\":\"VGHS\",\"elev_angle\":-7.49,\"app_temp\":31.3}],\"count\":1}";
  * @return Returns true if params added to the database, and false otherwise.
  */
  public boolean addWeatherDataToCache(int cityId, String jsonString);

  /**
  * Checks if a primary key cityId exists in the current state of the database.
  * @param cityId The primary key int city id to check if exists.
  * @return Returns true if cityId is in the database, otherwise false.
  */
  public boolean checkWeatherDataExistsInCache(int cityId);

  /**
  * Modifies the current database at primary key cityId, replacing existing string data with new jsongString parameter.
  * @param cityId The city id primary key to replace string data.
  * @param jsonString The string to be added as the data for cityId in the database.
  */
  public void modifyExistingWeatherDataInCache(int cityId, String jsonString);

  /**
  * Gets a json formatted string of the weather data for a given city based on its primary key cityId.
  * @param cityId The primary key queried in the database to get the json string associated.
  * @return Returns not null json string if found, otherwise returns null.
  */
  public String getWeatherDataForCityFromCache(int cityId);

  /**
  * Clears all city weather data entries in the current database.
  */
  public void clearCache();

}
