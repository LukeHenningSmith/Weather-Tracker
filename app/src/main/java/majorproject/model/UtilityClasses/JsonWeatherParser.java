package majorproject.model.utility;

import majorproject.model.pojo.CurrentWeatherData;
import majorproject.model.pojowrapper.LocationWeather;

import java.util.List;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.JsonObject;

/**
* Model utility class that converts jsonString to CurrentWeatherData objects and constructs json formatted email string from LocationWeather objects.
*/
public class JsonWeatherParser {

  private static Gson gson = new Gson();

  /**
  * Converts a json formatted string to CurrentWeatherData object.
  * @param jsonString The jsonString to be converted to CurrentWeatherData object. Example format: "{\"data\":[{\"rh\":80.6875,\"pod\":\"n\",\"lon\":0.0,\"pres\":1004,\"timezone\":\"placeholder\",\"ob_time\":\"211\",\"country_code\":\"BD\",\"clouds\":0,\"ts\":1652274295,\"solar_rad\":0,\"state_code\":
  \"85\",\"city_name\":\"Placeholder\",\"wind_spd\":0.0,\"wind_cdir_full\":\"Placeholder\",\"wind_cdir\":\"S\",\"slp\":1004.5,\"vis\":1.5,\"h_angle\":90,\"sunset\":\"12:31\",\"dni\":0,\"dewpt\":24.1,
  \"snow\":0,\"uv\":0,\"precip\":0.0,\"wind_dir\":179,\"sunrise\":\"23:17\",\"ghi\":0,\"dhi\":0,\"aqi\":0,\"lat\":0.0,\"weather\":{\"icon\":\"a01d\",\"code\":800,\"description\":\"Placeholder\"},
  \"datetime\":\"2022-05-11:12\",\"temp\":0.0,\"station\":\"VGHS\",\"elev_angle\":-7.49,\"app_temp\":31.3}],\"count\":1}";
  * @return Returns the created CurrentWeatherData if successful, otherwise null.
  */
  public static CurrentWeatherData parseJsonStringToCurrentWeatherData(String jsonString){
    JsonObject jobj = new Gson().fromJson(jsonString, JsonObject.class);

    int count = jobj.get("count").getAsInt();
    if(count>1){
      //it returned more than 1 current weather... will just take the first one
    } else if(count<1) {
      //didnt return any data for current weather... returning
      return null;
    }
    String result = jobj.get("data").toString();
    CurrentWeatherData[] weatherData = gson.fromJson(result, CurrentWeatherData[].class);
    CurrentWeatherData theCityWeatherData = weatherData[0];
    return theCityWeatherData;
  }

  /**
  * Converts a list of LocationWeather, a sender email and a recipient email into a formatted jsonString.
  * @param data The List of LocationWeather data to convert to formatted json content.
  * @param toEmail The email to send it to.
  * @param fromEmail The email it is sent from.
  * @return The json formatted string. Example format: "{\"personalizations\":[{\"to\":[{\"email\":\"toEmail\",\"name\":\"target email\"}]}],\"from\":{\"email\":\"fromEmail\",\"name\":\"Weather Tracker\"},\"subject\":\"emailSubject\",\"content\":[{\"type\":\"text/html\",\"value\":\"htmlEmailContent\"}]}"
  */
  public static String parseCurrentWeatherDataToJsonString(List<LocationWeather> data, String toEmail, String fromEmail){
    if(data==null || data.size() == 0){
      return null;
    }

    String htmlEmailContent = "";
    //convert data to string formats.
    for(LocationWeather locWeath : data){
      CurrentWeatherData curWeather = locWeath.getCurrentWeatherData();
      String output = locWeath.getCityLocation().toString().replace("\"", "\\\"");
      htmlEmailContent += "<p><strong>"+output + "</strong>: "+curWeather.toEmailNeatString() +"</p>";
      htmlEmailContent += "<br>";
    }

    String emailSubject = "Weather Report";

    String jsonString = "{\"personalizations\":[{\"to\":[{\"email\":\""+toEmail+"\",\"name\":\"target email\"}]}],\"from\":{\"email\":\""+fromEmail+"\",\"name\":\"Weather Tracker\"},\"subject\":\""+emailSubject+"\",\"content\":[{\"type\":\"text/html\",\"value\":\""+htmlEmailContent+"\"}]}";

    return jsonString;
  }

}
