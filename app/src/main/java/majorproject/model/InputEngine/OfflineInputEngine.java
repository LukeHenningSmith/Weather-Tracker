package majorproject.model.inputengine;

import majorproject.model.pojo.*;

/**
* An 'offline' or 'dummy' implementation of InputEngine that doesn't interact with any web APIs. Simply returns static correctly formatted data.
*/
public class OfflineInputEngine implements InputEngine {

  public String getCurrentWeatherForLocation(String apiKey, int cityId, double lat, double lon){
    return "{\"data\":[{\"rh\":80.6875,\"pod\":\"n\",\"lon\":" + String.valueOf(lon) + ",\"pres\":1004,\"timezone\":\"placeholder\",\"ob_time\":\"2022-05-11 13:04\",\"country_code\":\"BD\",\"clouds\":0,\"ts\":1652274295,\"solar_rad\":0,\"state_code\":\"85\",\"city_name\":\"Placeholder\",\"wind_spd\":0.0,\"wind_cdir_full\":\"Placeholder\",\"wind_cdir\":\"S\",\"slp\":1004.5,\"vis\":1.5,\"h_angle\":90,\"sunset\":\"12:31\",\"dni\":0,\"dewpt\":24.1,\"snow\":0,\"uv\":0,\"precip\":0.0,\"wind_dir\":179,\"sunrise\":\"23:17\",\"ghi\":0,\"dhi\":0,\"aqi\":0,\"lat\":" + String.valueOf(lat) + ",\"weather\":{\"icon\":\"a01d\",\"code\":800,\"description\":\"Placeholder\"},\"datetime\":\"2022-05-11:12\",\"temp\":0.0,\"station\":\"VGHS\",\"elev_angle\":-7.49,\"app_temp\":31.3}],\"count\":1}";
  }
}
