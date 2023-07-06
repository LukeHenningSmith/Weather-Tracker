package majorproject.model.utility;

import majorproject.model.pojo.*;

import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;

/**
* Class to load the CSV files of cities with 15,000+ population.
*/
public class LocationsFileLoader {

  private List<CityLocation> allCityLocations = null;

  //map of city id --> city name
  private Map<Integer,String> mapCityIDCityName = new HashMap<Integer,String>();

  //map of city id --> state code
  private Map<Integer,String> mapCityIdStateCode = new HashMap<Integer,String>();

  //map of city id --> country full
  private Map<Integer,String> mapCityIdCountryFull = new HashMap<Integer,String>();

  //map of cityId --> country code
  private Map<Integer,String> mapCityIdCountryCode = new HashMap<Integer,String>();

  //map of cityid --> latitude
  private Map<Integer,Double> mapCityIdLatitude = new HashMap<Integer,Double>();

  //map of cityid --> longitude
  private Map<Integer,Double> mapCityIdLongitude = new HashMap<Integer,Double>();

  //index 0 = state_code, index 1 = state_name, index 2 = country_code
  //the state_code and country_code collectively uniquely define state_name
  private List<List<String>> arrayStateCodeStateName = new ArrayList<>();

  /**
  * Method to return the constructed CityLocation objects.
  * @return Returns a list of CityLocation objects for citites with >15000 population.
  */
  public List<CityLocation> getAllCityLocations(){
    if(this.allCityLocations==null){
      this.loadCSVFiles();
      this.loadAllCityLocations();
    }
    return this.allCityLocations;
  }

  private void loadCSVFiles(){
    //loads in the CSV files into maps of values
    List<List<String>> records = new ArrayList<>();
    int count=0;
    //"/Users/lhs/Desktop/SOFT GITHUB REPO/major_project/app/resources/cities_20000.csv"
    try (Scanner scanner = new Scanner(new File("src/main/resources/cities_20000.csv"));) {
        while (scanner.hasNextLine()) {
          if(count!=0){
            List<String> row = getRecordFromLine(scanner.nextLine());
            //this line is the row line, need to make it into the maps
            //city_id	city_name	state_code	country_code	country_full	lat	lon
            Integer cityId = Integer.valueOf(row.get(0));
            String cityName = row.get(1);
            if(cityName.length()<1){
              System.out.println("No city name");
            }

            String stateCode = row.get(2);
            String countryCode = row.get(3);
            String countryFull = row.get(4);
            // if(countryFull.length()<1){
            //   System.out.println("No country full name. city is: "+cityName);
            // }
            Double lat = Double.valueOf(row.get(5));
            Double lon = Double.valueOf(row.get(6));

            mapCityIDCityName.put(cityId,cityName);
            mapCityIdLatitude.put(cityId,lat);
            mapCityIdLongitude.put(cityId,lon);
            mapCityIdStateCode.put(cityId,stateCode);
            mapCityIdCountryCode.put(cityId,countryCode);
            mapCityIdCountryFull.put(cityId,countryFull);
          } else {
            scanner.nextLine();
          }
          count++;
        }
    } catch (FileNotFoundException e){
      System.out.println("not found");
    }

    count = 0;
    //now load in the other file
    try (Scanner scanner = new Scanner(new File("src/main/resources/states.csv"));) {
        while (scanner.hasNextLine()) {
          if(count!=0){
            List<String> row = getRecordFromLine(scanner.nextLine());
            if(row.size()!=3){
              System.out.println("+++++++++++++++++++++++ ERROR");
            }
            arrayStateCodeStateName.add(row);
          } else {
            scanner.nextLine();
          }
          count++;
        }
    } catch (FileNotFoundException e){
      System.out.println("not found");
    }
  }

  private List<String> getRecordFromLine(String line) {
    String COMMA_DELIMITER = ",";
      List<String> values = new ArrayList<String>();
      try (Scanner rowScanner = new Scanner(line)) {
          rowScanner.useDelimiter(COMMA_DELIMITER);
          while (rowScanner.hasNext()) {
            String tmpString = rowScanner.next();
            if(tmpString.length()>0){
              if(tmpString.charAt(0)=='"'){
                //get rid of it
                tmpString = tmpString.substring(1); //removes first character.
                //then it is a string containing commas, so read until string contains " again
                String anotherTmpString = tmpString;

                while(!anotherTmpString.contains("\"")){
                  anotherTmpString+=","+rowScanner.next();
                }
                //add back the first quotation
                anotherTmpString = "\""+anotherTmpString;

                values.add(anotherTmpString);
              } else {
                values.add(tmpString);
              }
            }
            else {
              values.add(tmpString);
            }
              //values.add(rowScanner.next());
          }
      }
      return values;
  }

  private void loadAllCityLocations(){
    this.allCityLocations = new ArrayList<CityLocation>();
    //for each cityid
    for(Integer i : mapCityIDCityName.keySet()){
      //i = cityId
      String cityName = mapCityIDCityName.get(i);
      String countryName = mapCityIdCountryFull.get(i);
      String stateCode = mapCityIdStateCode.get(i);
      String countryCode = mapCityIdCountryCode.get(i);
      String stateName = null;
      //now find where stateCode and countryCode equal in the array
      for(List<String> ls : arrayStateCodeStateName){
        if(ls.get(0).equals(stateCode) && ls.get(2).equals(countryCode)){
          //found it
          stateName=ls.get(1);
        }
      }
      double lat = mapCityIdLatitude.get(i);
      double lon = mapCityIdLongitude.get(i);
      this.allCityLocations.add(new CityLocation(i,lat,lon,cityName,stateName,countryName));
    }
  }


}
