package majorproject.test.model;

import majorproject.model.pojo.CityLocation;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
* A class to test the CityLocation pojo can be constructed correctly, and produces expected output
* when using its various toString() methods.
*/
class CityLocationTest {

    /**
    * Tests that CityLocation is initialised correctly with and without city/state/country names.
    */
    @Test void testConstructCityLocationObjects(){
      //First city: Normal
      CityLocation cityOne = new CityLocation(0,10.0,20.0,"city","state","country");
      assertNotNull(cityOne);
      assert(cityOne.getCityId()==0);
      assert(cityOne.getLatitude() == 10.0);
      assert(cityOne.getLongitude() == 20.0);
      assert(cityOne.getCityName().equals("city"));
      assert(cityOne.getStateName().equals("state"));
      assert(cityOne.getCountryName().equals("country"));

      //no state name
      CityLocation cityTwo = new CityLocation(1,10.0,20.0,"city",null,"country");
      assertNotNull(cityTwo);
      assert(cityTwo.getCityId()==1);
      assert(cityTwo.getLatitude() == 10.0);
      assert(cityTwo.getLongitude() == 20.0);
      assert(cityTwo.getCityName().equals("city"));
      assert(cityTwo.getStateName().equals(""));
      assert(cityTwo.getCountryName().equals("country"));

      //no state or country name
      CityLocation cityThree = new CityLocation(2,10.0,20.0,"city",null,null);
      assertNotNull(cityThree);
      assert(cityThree.getCityId()==2);
      assert(cityThree.getLatitude() == 10.0);
      assert(cityThree.getLongitude() == 20.0);
      assert(cityThree.getCityName().equals("city"));
      assert(cityThree.getStateName().equals(""));
      assert(cityThree.getCountryName().equals(""));

      //no city state or country name
      CityLocation cityFour = new CityLocation(3,10.0,20.0,null,null,null);
      assertNotNull(cityTwo);
      assert(cityFour.getCityId()==3);
      assert(cityFour.getLatitude() == 10.0);
      assert(cityFour.getLongitude() == 20.0);
      assert(cityFour.getCityName().equals(""));
      assert(cityFour.getStateName().equals(""));
      assert(cityFour.getCountryName().equals(""));
    }

    /**
    * Tests that CityLocation is converted to a string based only on the city/state/country it
    contains, as well as removing any quotation marks present in the json formatting.
    */
    @Test void testCityLocationObjectsToString(){
      //First city: Normal
      CityLocation cityOne = new CityLocation(0,10.0,20.0,"city","state","country");
      String expectedToStringOne = "City: city,  State: state,  Country: country";
      assert(expectedToStringOne.equals(cityOne.toString()));

      //no state name
      CityLocation cityTwo = new CityLocation(1,10.0,20.0,"city",null,"country");
      String expectedToStringTwo = "City: city,  Country: country";
      assert(expectedToStringTwo.equals(cityTwo.toString()));

      //no state or country name
      CityLocation cityThree = new CityLocation(2,10.0,20.0,"city",null,null);
      String expectedToStringThree = "City: city";
      assert(expectedToStringThree.equals(cityThree.toString()));

      //no city state or country name
      CityLocation cityFour = new CityLocation(3,10.0,20.0,null,null,null);
      assert(cityFour.toString().equals(""));


      //now check it replaces quotation marks
      CityLocation cityFive = new CityLocation(0,10.0,20.0,"city\"\"","\"\"sta\"te","c\"\"\"ountry\"");
      String expectedToStringFive = "City: city,  State: state,  Country: country";
      assert(expectedToStringFive.equals(cityFive.toString()));
    }

    /**
    * Tests that CityLocation is converted to a vertical string based only on the city/state/country it
    contains, as well as removing any quotation marks present in the json formatting.
    */
    @Test void testCityLocationObjectsToStringVertical(){
      //First city: Normal
      CityLocation cityOne = new CityLocation(0,10.0,20.0,"city","state","country");
      String expectedToStringOne = "City: city\nState: state\nCountry: country";
      assert(expectedToStringOne.equals(cityOne.toStringVertical()));

      //no state name
      CityLocation cityTwo = new CityLocation(1,10.0,20.0,"city",null,"country");
      String expectedToStringTwo = "City: city\nCountry: country";
      assert(expectedToStringTwo.equals(cityTwo.toStringVertical()));

      //no state or country name
      CityLocation cityThree = new CityLocation(2,10.0,20.0,"city",null,null);
      String expectedToStringThree = "City: city";
      assert(expectedToStringThree.equals(cityThree.toStringVertical()));

      //no city state or country name
      CityLocation cityFour = new CityLocation(3,10.0,20.0,null,null,null);
      assert(cityFour.toStringVertical().equals(""));


      //now check it replaces quotation marks
      CityLocation cityFive = new CityLocation(0,10.0,20.0,"city\"\"","\"\"sta\"te","c\"\"\"ountry\"");
      String expectedToStringFive = "City: city\nState: state\nCountry: country";
      assert(expectedToStringFive.equals(cityFive.toStringVertical()));
    }

}
