package majorproject.model.pojowrapper;

import majorproject.model.pojo.*;

/**
* A simple wrapper class for CurrentWeatherData and CityLocation object.
*/
public class LocationWeather {

  private final CurrentWeatherData weatherData;
  private final CityLocation location;

  /**
  * Constructor to set the two varibales of LocationWeather
  * @param weatherData The CurrentWeatherData object that contains the weather information for this location.
  * @param location The CityLocation the weather data is for.
  */
  public LocationWeather(CurrentWeatherData weatherData, CityLocation location){
    this.weatherData=weatherData;
    this.location=location;
  }

  /**
  * Simple getter method for cityId variable in CityLocation object.
  * @return The int value for cityId.
  */
  public int getCityId(){
    return this.location.getCityId();
  }

  /**
  * Simple getter method for the CurrentWeatherData object contained in this wrapper.
  * @return The CurrentWeatherData contained in this wrapper.
  */
  public CurrentWeatherData getCurrentWeatherData(){
    return this.weatherData;
  }

  /**
  * Simple getter method for the CityLocation object contained in this wrapper.
  * @return The CityLocation contained in this wrapper.
  */
  public CityLocation getCityLocation(){
    return this.location;
  }

  /**
  * Simple getter method for cityName variable in CityLocation object.
  * @return The String value for cityName.
  */
  public String getCityName(){
    return this.location.getCityName();
  }

  /**
  * Simple getter method for stateName variable in CityLocation object.
  * @return The String value for stateName.
  */
  public String getStateName(){
    return this.location.getStateName();
  }

  /**
  * Simple getter method for countryName variable in CityLocation object.
  * @return The String value for countryName.
  */
  public String getCountryName(){
    return this.location.getCountryName();
  }

  /**
  * Simple getter method for temperature variable in CurrentWeatherData object.
  * @return The Double value for temperature.
  */
  public Double getTemperature(){
    return this.weatherData.getTemperature();
  }

  /**
  * Simple getter method for clouds variable in CurrentWeatherData object.
  * @return The Double value for clouds.
  */
  public Double getClouds(){
    return this.weatherData.getClouds();
  }

  /**
  * Simple getter method for windSpeed variable in CurrentWeatherData object.
  * @return The Double value for windSpeed.
  */
  public Double getWindSpeed(){
    return this.weatherData.getWindSpeed();
  }

  /**
  * Simple getter method for windDirection variable in CurrentWeatherData object.
  * @return The String value for windDirection.
  */
  public String getWindDirection(){
    return this.weatherData.getWindDirection();
  }

  /**
  * Simple getter method for precip variable in CurrentWeatherData object.
  * @return The Double value for precip.
  */
  public Double getPrecipitation(){
    return this.weatherData.getPrecipitation();
  }

  /**
  * Simple getter method for aqi variable in CurrentWeatherData object.
  * @return The Integer value for aqi.
  */
  public Integer getAqi(){
    return this.weatherData.getAQI();
  }

  /**
  * Simple getter method for latitude variable in CurrentWeatherData object.
  * @return The Double value for latitude.
  */
  public Double getLatitude(){
    return this.weatherData.getLatitude();
  }

  /**
  * Simple getter method for longitude variable in CurrentWeatherData object.
  * @return The Double value for longitude.
  */
  public Double getLongitude(){
    return this.weatherData.getLongitude();
  }

  /**
  * Method to format the nested CurrentWeatherData to vertical arrangement.
  * @return The vertically formatted CurrentWeatherData String.
  */
  public String getVerticalWeatherData(){
    return weatherData.toStringVertical();
  }

  /**
  * A simple formatted toString method concatenating the CityLocation with CurrentWeatherData
  * @return The formatted String.
  */
  public String toString(){
    return location.toString() + weatherData.toEmailNeatString();
  }

}
