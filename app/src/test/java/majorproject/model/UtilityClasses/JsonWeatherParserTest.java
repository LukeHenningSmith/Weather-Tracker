package majorproject.test.model;

import majorproject.model.utility.JsonWeatherParser;
import majorproject.model.pojo.*;
import majorproject.model.pojowrapper.*;

import java.util.List;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import static org.junit.jupiter.api.Assertions.*;

/**
* A class to test that JsonWeatherParser converts from json Strings to CurrentWeatherData objects
* correctly and converts a List of LocationWeather to a specific json formatted email string correctly.
*/
class JsonWeatherParserTest {

    /**
    * Tests that a json formatted string can be converted to CurrentWeatherData object correctly.
    */
    @Test void testParseJsonStringToCurrentWeatherData(){
      String jsonString = "{\"data\":[{\"rh\":80.6875,\"pod\":\"n\",\"lon\":10.0,\"pres\":1004,\"timezone\":\"placeholder\",\"ob_time\":\"2022-05-11 13:04\",\"country_code\":\"BD\",\"clouds\":11,\"ts\":1652274295,\"solar_rad\":0,\"state_code\":\"85\",\"city_name\":\"Placeholder\",\"wind_spd\":10.0,\"wind_cdir_full\":\"West\",\"wind_cdir\":\"S\",\"slp\":1004.5,\"vis\":1.5,\"h_angle\":90,\"sunset\":\"12:31\",\"dni\":0,\"dewpt\":24.1,\"snow\":0,\"uv\":0,\"precip\":0.0,\"wind_dir\":179,\"sunrise\":\"23:17\",\"ghi\":0,\"dhi\":0,\"aqi\":7,\"lat\":20.123 ,\"weather\":{\"icon\":\"a01d\",\"code\":800,\"description\":\"Placeholder\"},\"datetime\":\"2022-05-11:12\",\"temp\":0.0,\"station\":\"VGHS\",\"elev_angle\":-7.49,\"app_temp\":31.3}],\"count\":1}";

      CurrentWeatherData result = JsonWeatherParser.parseJsonStringToCurrentWeatherData(jsonString);

      assertNotNull(result);

      // check its contents is correct
      assert(result.getTemperature() == 0.0);
      assert(result.getWindDirection().equals("West"));
      assert(result.getWindSpeed() == 10.0);
      assert(result.getClouds() == 11);
      assert(result.getPrecipitation() == 0.0);
      assert(result.getAQI() == 7);
      assert(result.getLatitude() == 20.123);
      assert(result.getLongitude() == 10.0);
      assertNotNull(result.getWeatherIconData());
      assert(result.getWeatherIconData().getIcon().equals("a01d"));
      assert(result.getWeatherIconData().getCode()==800 );
      assert(result.getWeatherIconData().getDescription().equals("Placeholder"));

      //check the toString methods format correctly.
      String expectedToEmailNeatString =
        "<p>Lat: 20.123°</p><p>Lon: 10.0°</p><p>Temperature: 0.0°C</p><p>Wind speed: 10.0m/s</p><p>Wind direction: West</p><p>Cloud coverage: 11.0%</p><p>Precipitation: 0.0mm/hr</p><p>Air Quality Index: 7";
      assert(result.toEmailNeatString().equals(expectedToEmailNeatString));

      // check other toString methods
      String expectedToStringVertical =
        "Temperature:   0.0°C\nWind Speed:   10.0 m/s\nWind Direction:   West\nCloud Coverage:   11.0%\nPrecipitation:   0.0 mm/hr\nAir Quality Index:   7";
      assert(result.toStringVertical().equals(expectedToStringVertical));
    }

    /**
    * Tests that varying amounts of current data can be converted to correctly formatted
    * JSONString with nested html email content. Ensures null is returned when data is empty.
    */
    @Test void testParseCurrentWeatherDataToJsonString(){
      //First, check null is returned when data is empty.
      String result = JsonWeatherParser.parseCurrentWeatherDataToJsonString(null,null,null);
      assertNull(result);

      //add single piece of data, and check content
      List<LocationWeather> locationWeather = new ArrayList<LocationWeather>();

      locationWeather.add(new LocationWeather(new CurrentWeatherData(10.0,10.0,"north",0.0,0.0,1,22.2,33.3,new WeatherIconData("code",100,"description")),new CityLocation(0,22.2,33.3,"cityName","stateName","countryName")));
      String resultTwo = JsonWeatherParser.parseCurrentWeatherDataToJsonString(locationWeather,"toEmail@gmail.com","fromEmail@gmail.com");
      assertNotNull(resultTwo);
      String emailSubject = "Weather Report";
      String htmlEmailContent =  "";
      htmlEmailContent += "<p><strong>"+locationWeather.get(0).getCityLocation().toString().replace("\"", "\\\"") + "</strong>: "+locationWeather.get(0).getCurrentWeatherData().toEmailNeatString() +"</p>";
      htmlEmailContent += "<br>";
      String jsonString = "{\"personalizations\":[{\"to\":[{\"email\":\"toEmail@gmail.com\",\"name\":\"target email\"}]}],\"from\":{\"email\":\"fromEmail@gmail.com\",\"name\":\"Weather Tracker\"},\"subject\":\""+emailSubject+"\",\"content\":[{\"type\":\"text/html\",\"value\":\""+htmlEmailContent+"\"}]}";
      assert(resultTwo.equals(jsonString));

      //add another data and check still correct.
      locationWeather.add(new LocationWeather(new CurrentWeatherData(20.0,20.0,"south",1.0,2.0,3,23.2,34.3,new WeatherIconData("code",120,"description")),new CityLocation(1,23.2,34.3,"cityName2","stateName2","countryName2")));
      String resultThree = JsonWeatherParser.parseCurrentWeatherDataToJsonString(locationWeather,"toEmail@gmail.com","fromEmail@gmail.com");
      assertNotNull(resultThree);
      htmlEmailContent =  "";
      htmlEmailContent += "<p><strong>"+locationWeather.get(0).getCityLocation().toString().replace("\"", "\\\"") + "</strong>: "+locationWeather.get(0).getCurrentWeatherData().toEmailNeatString() +"</p>";
      htmlEmailContent += "<br>";
      htmlEmailContent += "<p><strong>"+locationWeather.get(1).getCityLocation().toString().replace("\"", "\\\"") + "</strong>: "+locationWeather.get(1).getCurrentWeatherData().toEmailNeatString() +"</p>";
      htmlEmailContent += "<br>";
      String jsonStringTwo = "{\"personalizations\":[{\"to\":[{\"email\":\"toEmail@gmail.com\",\"name\":\"target email\"}]}],\"from\":{\"email\":\"fromEmail@gmail.com\",\"name\":\"Weather Tracker\"},\"subject\":\""+emailSubject+"\",\"content\":[{\"type\":\"text/html\",\"value\":\""+htmlEmailContent+"\"}]}";
      assert(resultThree.equals(jsonStringTwo));
    }
}
