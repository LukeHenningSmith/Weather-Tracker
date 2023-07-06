package majorproject.model.pojo;

/**
* Simple POJO class to store city location parameters.
*/
public class CityLocation {

  private final String stateName;
  private final String cityName;
  private final String countryName;
  private final int cityId;
  private final double latitude;
  private final double longitude;

  /**
  * Constructor that sets the parameters to same named variables.
  * @param cityId The unique int identifier of the city.
  * @param latitude Latitude value of the city in real life.
  * @param longitude Longitude value of the city in real life.
  * @param cityName The name of the city (in real life).
  * @param stateName The name of the state the city is in (in real life).
  * @param countryName The name of the country the city is in (in real life).
  * Any null Strings are converted to empty strings for neater formatting.
  */
  public CityLocation(int cityId, double latitude, double longitude, String cityName, String stateName, String countryName){
    if(stateName==null){
      this.stateName="";
    } else {
      this.stateName=stateName;
    }
    if(cityName==null){
      this.cityName="";
    } else {
      this.cityName=cityName;
    }
    if(countryName==null){
      this.countryName="";
    } else {
      this.countryName=countryName;
    }
    this.cityId=cityId;
    this.latitude=latitude;
    this.longitude=longitude;
  }

  /**
  * Getter method for cityId variable.
  * @return The int value for cityId.
  */
  public int getCityId(){
    return this.cityId;
  }

  /**
  * Getter method for cityName variable.
  * @return The String value for cityName. Never null, but can be empty string.
  */
  public String getCityName(){
    return this.cityName;
  }

  /**
  * Getter method for stateName variable.
  * @return The String value for stateName. Never null, but can be empty string.
  */
  public String getStateName(){
    return this.stateName;
  }

  /**
  * Getter method for countryName variable.
  * @return The String value for countryName. Never null, but can be empty string.
  */
  public String getCountryName(){
    return this.countryName;
  }

  /**
  * Getter method for latitude variable.
  * @return The double value for latitude.
  */
  public double getLatitude(){
    return this.latitude;
  }

  /**
  * Getter method for longitude variable.
  * @return The double value for longitude.
  */
  public double getLongitude(){
    return this.longitude;
  }

  /**
  * Formats CityLocation for "City: State: Country:" display with JSON formatting removed.
  * @return The formatted string.
  */
  public String toString(){
    String str = "";
    if(cityName!=null){
      if(cityName.length()>0){
        str+= "City: ";
        str+=cityName.replaceAll("\"","");
      }
    }
    if(stateName!=null){
      if(stateName.length()>0){
        if(str.length()>0){
          str += ",  State: " + stateName.replaceAll("\"","");
        } else {
          str+= "State: "+stateName.replaceAll("\"","");
        }
      }
    }
    if(countryName!=null){
      if(countryName.length()>0){
        if(str.length()>0){
          str += ",  Country: " + countryName.replaceAll("\"","");
        } else {
          str+= "Country: "+countryName.replaceAll("\"","");
        }
      }
    }
    return str;
  }

  /**
  * Formats CityLocation for vertical arrangement with JSON formatting removed.
  * @return The formatted string.
  */
  public String toStringVertical(){
    String str = "";
    if(cityName!=null){
      if(cityName.length()>0){
        str+= "City: ";
        str+=cityName.replaceAll("\"","");
      }
    }
    if(stateName!=null){
      if(stateName.length()>0){
        if(str.length()>0){
          str += "\nState: " + stateName.replaceAll("\"","");
        } else {
          str+= "State: "+stateName.replaceAll("\"","");
        }
      }
    }
    if(countryName!=null){
      if(countryName.length()>0){
        if(str.length()>0){
          str += "\nCountry: " + countryName.replaceAll("\"","");
        } else {
          str+= "Country: "+countryName.replaceAll("\"","");
        }
      }
    }
    return str;
  }

}
