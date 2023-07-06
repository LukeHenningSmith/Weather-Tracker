package majorproject.model.cacheengine;

import java.io.File;
import java.sql.*;

import java.util.List;
import java.util.ArrayList;

/**
* An "offline" or "dummy" implementation of CacheEngine that interacts with no database and always returns static information.
*/
public class OfflineCacheEngine implements CacheEngine {

  public boolean addWeatherDataToCache(int cityId, String jsonString){
    return true;
  }


  public boolean checkWeatherDataExistsInCache(int cityId){
    return false;
  }


  public void modifyExistingWeatherDataInCache(int cityId, String jsonString){
  }


  public String getWeatherDataForCityFromCache(int cityId){
    return "{\"data\":[{\"rh\":80.6875,\"pod\":\"n\",\"lon\":0.0,\"pres\":1004,\"timezone\":\"placeholder\",\"ob_time\":\"2022-05-11 13:04\",\"country_code\":\"BD\",\"clouds\":0,\"ts\":1652274295,\"solar_rad\":0,\"state_code\":\"85\",\"city_name\":\"Placeholder\",\"wind_spd\":0.0,\"wind_cdir_full\":\"Placeholder\",\"wind_cdir\":\"S\",\"slp\":1004.5,\"vis\":1.5,\"h_angle\":90,\"sunset\":\"12:31\",\"dni\":0,\"dewpt\":24.1,\"snow\":0,\"uv\":0,\"precip\":0.0,\"wind_dir\":179,\"sunrise\":\"23:17\",\"ghi\":0,\"dhi\":0,\"aqi\":0,\"lat\":0.0 ,\"weather\":{\"icon\":\"a01d\",\"code\":800,\"description\":\"Placeholder\"},\"datetime\":\"2022-05-11:12\",\"temp\":0.0,\"station\":\"VGHS\",\"elev_angle\":-7.49,\"app_temp\":31.3}],\"count\":1}";
  }


  public void clearCache(){
  }

}
