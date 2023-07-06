package majorproject.model.pojo;

import com.google.gson.annotations.SerializedName;
/**
* Simple POJO class to store current weather data for lattitude / longitude location.
*/
public class CurrentWeatherData {

  private double temp;

  @SerializedName("wind_spd")
  private final double windSpeed;

  @SerializedName("wind_cdir_full")
  private final String windDirection;
  private final double clouds;
  private final double precip;
  private final int aqi;
  private final double lat;
  private final double lon;

  @SerializedName("weather")
  private final WeatherIconData weatherIconData;

  /**
  * Simple constructor to set all variables in CurrentWeatherData class.
  * @param temp The temperture in degrees celsius.
  * @param windSpeed The speed of the wind in metres per second.
  * @param windDirection The direction of the wind. (Eg. North-east).
  * @param clouds The percentage of cloud coverage.
  * @param precip The amount of precipitation in mm/hr.
  * @param aqi The Air Quality Index.
  * @param lat The latitude of the city the weather is for.
  * @param lon The longitude of the city the weather is for.
  * @param weatherIconData The image data POJO for the weather.
  */
  public CurrentWeatherData(double temp,double windSpeed,String windDirection,double clouds,double precip,int aqi,double lat,double lon,WeatherIconData weatherIconData){
    this.temp=temp;
    this.windSpeed=windSpeed;
    this.windDirection=windDirection;
    this.clouds=clouds;
    this.precip=precip;
    this.aqi=aqi;
    this.lat=lat;
    this.lon=lon;
    this.weatherIconData=weatherIconData;
  }

  /**
  * Getter method for temperature variable.
  * @return The double value for temperature.
  */
  public double getTemperature(){
    return this.temp;
  }

  /**
  * Getter method for windSpeed variable.
  * @return The double value for windSpeed.
  */
  public double getWindSpeed(){
    return this.windSpeed;
  }

  /**
  * Getter method for windDirection variable.
  * @return The String value for windDirection.
  */
  public String getWindDirection(){
    return this.windDirection;
  }

  /**
  * Getter method for clouds variable.
  * @return The double value for clouds.
  */
  public double getClouds(){
    return this.clouds;
  }

  /**
  * Getter method for precip variable.
  * @return The double value for precip.
  */
  public double getPrecipitation(){
    return this.precip;
  }

  /**
  * Getter method for aqi variable.
  * @return The int value for aqi.
  */
  public int getAQI(){
    return this.aqi;
  }

  /**
  * Getter method for latitude variable.
  * @return The double value for latitude.
  */
  public double getLatitude(){
    return this.lat;
  }

  /**
  * Getter method for longitude variable.
  * @return The double value for longitude.
  */
  public double getLongitude(){
    return this.lon;
  }

  /**
  * Formats string for neat email using HTML formatting.
  * @return The formatted String.
  */
  public String toEmailNeatString(){
    return "<p>Lat: " + Double.toString(lat) + "°"
    +"</p>"+
    "<p>Lon: " + Double.toString(lon) + "°"
    +"</p>"+
    "<p>Temperature: " + Double.toString(temp) + "°C"
    +"</p>"+
    "<p>Wind speed: " + Double.toString(windSpeed) + "m/s"
    +"</p>"+
    "<p>Wind direction: " + windDirection
    +"</p>"+
    "<p>Cloud coverage: " + Double.toString(clouds) + "%"
    +"</p>"+
    "<p>Precipitation: " + Double.toString(precip) +"mm/hr"
    +"</p>"+
    "<p>Air Quality Index: " + Integer.toString(aqi);
  }

  /**
  * Formats string for neat vertical arrangement using new line character.
  * @return The formatted String.
  */
  public String toStringVertical(){
    return "Temperature:   " + Double.toString(temp) + "°C"
    +"\n"+
    "Wind Speed:   " + Double.toString(windSpeed) + " m/s"
    +"\n"+
    "Wind Direction:   " + windDirection
    +"\n"+
    "Cloud Coverage:   " + Double.toString(clouds) + "%"
    +"\n"+
    "Precipitation:   " + Double.toString(precip) +" mm/hr"
    +"\n"+
    "Air Quality Index:   " + Integer.toString(aqi);
  }

  /**
  * Simple getter method for subclass of WeatherIconData.
  * @return Returns WeatherIconData contained within it.
  */
  public WeatherIconData getWeatherIconData(){
    return this.weatherIconData;
  }

}
