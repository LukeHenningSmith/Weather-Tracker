package majorproject.test.model;

import majorproject.model.utility.LocationsFileLoader;
import majorproject.model.pojo.CityLocation;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import static org.junit.jupiter.api.Assertions.*;

/**
* A class to test that LocationsFileLoader creates CityLocation from CSV files correctly.
*/
class LocationsFileLoaderTest {

  //takes time to load, so just do it once.
  private static LocationsFileLoader fileLoader;
  private static List<CityLocation> locations;

    /**
    * Tests that LocationsFileLoader can be constructed and can return a not null list of CityLocation.
    */
    @BeforeAll static void testConstructionAndGetAllCityLocations() {
      fileLoader = new LocationsFileLoader();
      assertNotNull(fileLoader);
      locations = fileLoader.getAllCityLocations();
      assertNotNull(locations);
    }

    /**
    * Tests that the number of CityLocation loaded meets number of cities.
    */
    @Test void testCreatedEnoughCityLocations(){
      //23524 rows [excluding first row, which is skipped]
      assert(locations.size()==23524);
    }

    /**
    * Tests that the cities with city name, state name and country name are loaded in correctly. Includes non-english characters.
    */
    @Test void testLoadedSimpleLocations(){
      //examples:
      boolean containsSydney = false;
      boolean containsVietnameCity = false;
      for(int i=0; i<locations.size();i++){
        //Sydney
        if(locations.get(i).getCityId()==2147714
        && locations.get(i).getCityName().equals("Sydney")
        && locations.get(i).getStateName().equals("New South Wales")
        && locations.get(i).getCountryName().equals("Australia")){
          assertEquals(locations.get(i).getLatitude(), -33.86785, 0.00001);
          assertEquals(locations.get(i).getLongitude() , 151.20732, 0.00001);
          containsSydney=true;
        }

        //Biên Hòa
        if(locations.get(i).getCityId()==1587923
        && locations.get(i).getCityName().equals("Biên Hòa")
        && locations.get(i).getStateName().equals("Ðồng Nai")
        && locations.get(i).getCountryName().equals("Vietnam")){
          assertEquals(locations.get(i).getLatitude(), 10.94469, 0.00001);
          assertEquals(locations.get(i).getLongitude() , 106.82432, 0.00001);
          containsVietnameCity=true;
        }
      }
      assert(containsSydney);
      assert(containsVietnameCity);
    }

    /**
    * Tests that the cities with missing state name and/or country name are still loaded in correctly.
    * Ensures quotation marks are retained in entries.
    */
    @Test void testLoadingCitiesWithMissingFields(){
      //examples:
      boolean containsJuba = false;
      boolean containsBondo = false;
      for(int i=0; i<locations.size();i++){
        //Juba
        if(locations.get(i).getCityId()==373303
        && locations.get(i).getCityName().equals("Juba")
        && locations.get(i).getStateName().equals("")
        && locations.get(i).getCountryName().equals("")){
          assertEquals(locations.get(i).getLatitude(), 4.85165, 0.00001);
          assertEquals(locations.get(i).getLongitude() , 31.58247, 0.00001);
          containsJuba=true;
        }

        //Bondo
        if(locations.get(i).getCityId()==218253
        && locations.get(i).getCityName().equals("Bondo")
        && locations.get(i).getStateName().equals("")
        && locations.get(i).getCountryName().equals("\"Congo, Democratic Republic of The\"")){
          assertEquals(locations.get(i).getLatitude(), 3.81461, 0.00001);
          assertEquals(locations.get(i).getLongitude() , 23.68665, 0.00001);
          containsBondo=true;
        }
      }
      assert(containsJuba);
      assert(containsBondo);
    }


}
