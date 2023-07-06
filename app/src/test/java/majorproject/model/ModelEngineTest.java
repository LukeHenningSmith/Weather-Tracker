package majorproject.test.model;

import majorproject.model.*;
import majorproject.model.inputengine.*;
import majorproject.model.outputengine.*;
import majorproject.model.cacheengine.*;
import majorproject.model.pojo.*;
import majorproject.model.pojowrapper.*;
import majorproject.model.utility.*;
import majorproject.model.observers.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.mockito.InOrder;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;
import static org.mockito.ArgumentMatchers.*;
import java.util.List;
import java.util.ArrayList;
import java.io.File;
import org.mockito.stubbing.*;
import org.mockito.invocation.*;

import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.AdditionalAnswers.returnsSecondArg;

/**
* A class to test the methods and data handling of ModelEngine.
*/
class ModelEngineTest {

  private static InputEngine mockInputEngineOnline;
  private static OutputEngine mockOutputEngineOnline;
  private static CacheEngine mockCacheEngineOnline;
  private static InputEngine mockInputEngineOffline;
  private static OutputEngine mockOutputEngineOffline;
  private static CacheEngine mockCacheEngineOffline;
  private ModelEngine engineOnlineOnline;
  private ModelEngine engineOnlineOffline;
  private ModelEngine engineOfflineOnline;
  private ModelEngine engineOfflineOffline;
  private List<ModelEngine> modelEngines;

  private LocationsFileLoader locationsFileLoader;

  /**
  * A setup method run before each test to construct ModelEngine, make mocks and define default mocked behaviour.
  */
  @BeforeEach void setup(){
    locationsFileLoader = mock(LocationsFileLoader.class);
    List<CityLocation> locations = new ArrayList<CityLocation>();
    locations.add(new CityLocation(2147714,-33.86785,151.20732,"Sydney","New South Wales","Australia"));
    locations.add(new CityLocation(1587923,10.94469,106.82432,"Biên Hòa","Ðồng Nai","Vietnam"));
    locations.add(new CityLocation(373303,4.85165,31.58247,"Juba","",""));
    locations.add(new CityLocation(218253,3.81461,23.68665,"Bondo","","\"Congo, Democratic Republic of The\""));

    when(locationsFileLoader.getAllCityLocations()).thenReturn(locations);

    mockInputEngineOnline = mock(OnlineInputEngine.class);
    mockOutputEngineOnline = mock(OnlineOutputEngine.class);
    mockCacheEngineOnline = mock(OnlineCacheEngine.class);

    mockInputEngineOffline = mock(OfflineInputEngine.class);
    mockOutputEngineOffline = mock(OfflineOutputEngine.class);
    mockCacheEngineOffline = mock(OfflineCacheEngine.class);

    //Setup the mocked returns to give the arguments of lat and lon back as part of the CurrentWeatherData object.
    try{
      when(mockInputEngineOnline.getCurrentWeatherForLocation(anyString(), anyInt(), anyDouble(),anyDouble())).thenAnswer(new Answer<String>(){
        @Override
        public String answer(InvocationOnMock invocation) throws Throwable {
          Object[] args = invocation.getArguments();
          //make string with args put into it.
          return "{\"data\":[{\"rh\":80.6875,\"pod\":\"n\",\"lon\":" + String.valueOf(args[3]) + ",\"pres\":1004,\"timezone\":\"placeholder\",\"ob_time\":\"2022-05-11 13:04\",\"country_code\":\"BD\",\"clouds\":0,\"ts\":1652274295,\"solar_rad\":0,\"state_code\":\"85\",\"city_name\":\"Placeholder\",\"wind_spd\":0.0,\"wind_cdir_full\":\"Placeholder\",\"wind_cdir\":\"S\",\"slp\":1004.5,\"vis\":1.5,\"h_angle\":90,\"sunset\":\"12:31\",\"dni\":0,\"dewpt\":24.1,\"snow\":0,\"uv\":0,\"precip\":0.0,\"wind_dir\":179,\"sunrise\":\"23:17\",\"ghi\":0,\"dhi\":0,\"aqi\":0,\"lat\":" + String.valueOf(args[2]) + ",\"weather\":{\"icon\":\"a01d\",\"code\":800,\"description\":\"Placeholder\"},\"datetime\":\"2022-05-11:12\",\"temp\":0.0,\"station\":\"VGHS\",\"elev_angle\":-7.49,\"app_temp\":31.3}],\"count\":1}";
        }
      });
    } catch (Exception ignored) { }

    try{
      when(mockInputEngineOffline.getCurrentWeatherForLocation(anyString(), anyInt(), anyDouble(),anyDouble())).thenAnswer(new Answer<String>() {
        @Override
        public String answer(InvocationOnMock invocation) throws Throwable {
          Object[] args = invocation.getArguments();
          //make string with args put into it.
          return "{\"data\":[{\"rh\":80.6875,\"pod\":\"n\",\"lon\":" + String.valueOf(args[3]) + ",\"pres\":1004,\"timezone\":\"placeholder\",\"ob_time\":\"2022-05-11 13:04\",\"country_code\":\"BD\",\"clouds\":0,\"ts\":1652274295,\"solar_rad\":0,\"state_code\":\"85\",\"city_name\":\"Placeholder\",\"wind_spd\":0.0,\"wind_cdir_full\":\"Placeholder\",\"wind_cdir\":\"S\",\"slp\":1004.5,\"vis\":1.5,\"h_angle\":90,\"sunset\":\"12:31\",\"dni\":0,\"dewpt\":24.1,\"snow\":0,\"uv\":0,\"precip\":0.0,\"wind_dir\":179,\"sunrise\":\"23:17\",\"ghi\":0,\"dhi\":0,\"aqi\":0,\"lat\":" + String.valueOf(args[2]) + ",\"weather\":{\"icon\":\"a01d\",\"code\":800,\"description\":\"Placeholder\"},\"datetime\":\"2022-05-11:12\",\"temp\":0.0,\"station\":\"VGHS\",\"elev_angle\":-7.49,\"app_temp\":31.3}],\"count\":1}";
        }
      });
    }  catch (Exception ignored) { }

    when(mockCacheEngineOnline.addWeatherDataToCache(anyInt(),anyString())).thenReturn(true);
    when(mockCacheEngineOnline.checkWeatherDataExistsInCache(anyInt())).thenReturn(true);
    when(mockCacheEngineOnline.getWeatherDataForCityFromCache(anyInt())).thenAnswer(new Answer<String>() {
      @Override
      public String answer(InvocationOnMock invocation) throws Throwable {
        return "{\"data\":[{\"rh\":80.6875,\"pod\":\"n\",\"lon\":0.0,\"pres\":1004,\"timezone\":\"placeholder\",\"ob_time\":\"2022-05-11 13:04\",\"country_code\":\"BD\",\"clouds\":0,\"ts\":1652274295,\"solar_rad\":0,\"state_code\":\"85\",\"city_name\":\"Placeholder\",\"wind_spd\":0.0,\"wind_cdir_full\":\"Placeholder\",\"wind_cdir\":\"S\",\"slp\":1004.5,\"vis\":1.5,\"h_angle\":90,\"sunset\":\"12:31\",\"dni\":0,\"dewpt\":24.1,\"snow\":0,\"uv\":0,\"precip\":0.0,\"wind_dir\":179,\"sunrise\":\"23:17\",\"ghi\":0,\"dhi\":0,\"aqi\":0,\"lat\":0.0 ,\"weather\":{\"icon\":\"a01d\",\"code\":800,\"description\":\"Placeholder\"},\"datetime\":\"2022-05-11:12\",\"temp\":0.0,\"station\":\"VGHS\",\"elev_angle\":-7.49,\"app_temp\":31.3}],\"count\":1}";
      }
    });

    when(mockCacheEngineOffline.addWeatherDataToCache(anyInt(),anyString())).thenReturn(true);
    when(mockCacheEngineOffline.checkWeatherDataExistsInCache(anyInt())).thenReturn(true);
    when(mockCacheEngineOffline.getWeatherDataForCityFromCache(anyInt())).thenAnswer(new Answer<String>() {
      @Override
      public String answer(InvocationOnMock invocation) throws Throwable {
        return "{\"data\":[{\"rh\":80.6875,\"pod\":\"n\",\"lon\":0.0,\"pres\":1004,\"timezone\":\"placeholder\",\"ob_time\":\"2022-05-11 13:04\",\"country_code\":\"BD\",\"clouds\":0,\"ts\":1652274295,\"solar_rad\":0,\"state_code\":\"85\",\"city_name\":\"Placeholder\",\"wind_spd\":0.0,\"wind_cdir_full\":\"Placeholder\",\"wind_cdir\":\"S\",\"slp\":1004.5,\"vis\":1.5,\"h_angle\":90,\"sunset\":\"12:31\",\"dni\":0,\"dewpt\":24.1,\"snow\":0,\"uv\":0,\"precip\":0.0,\"wind_dir\":179,\"sunrise\":\"23:17\",\"ghi\":0,\"dhi\":0,\"aqi\":0,\"lat\":0.0 ,\"weather\":{\"icon\":\"a01d\",\"code\":800,\"description\":\"Placeholder\"},\"datetime\":\"2022-05-11:12\",\"temp\":0.0,\"station\":\"VGHS\",\"elev_angle\":-7.49,\"app_temp\":31.3}],\"count\":1}";
      }
    });

    //Since cache is on when input is on, can keep the 4 options.
    engineOnlineOnline = new ModelEngine(mockInputEngineOnline,mockOutputEngineOnline,mockCacheEngineOnline,"dummyKey","dummyKey","dummyEmail",locationsFileLoader);
    engineOnlineOffline = new ModelEngine(mockInputEngineOnline,mockOutputEngineOffline,mockCacheEngineOnline,"dummyKey","dummyKey","dummyEmail",locationsFileLoader);
    engineOfflineOnline = new ModelEngine(mockInputEngineOffline,mockOutputEngineOnline,mockCacheEngineOffline,"dummyKey","dummyKey","dummyEmail",locationsFileLoader);
    engineOfflineOffline = new ModelEngine(mockInputEngineOffline,mockOutputEngineOffline,mockCacheEngineOffline,"dummyKey","dummyKey","dummyEmail",locationsFileLoader);

    modelEngines = new ArrayList<ModelEngine>();
    modelEngines.add(engineOnlineOnline);
    modelEngines.add(engineOnlineOffline);
    modelEngines.add(engineOfflineOnline);
    modelEngines.add(engineOfflineOffline);
  }

  /**
  * Tests that the ModelEngine can be constructed with the 4 variations of online/offline input/output engines.
  */
  @Test void testConstructionOfModelEngineVariants(){
    assertNotNull(engineOnlineOnline);
    assertNotNull(engineOfflineOnline);
    assertNotNull(engineOnlineOffline);
    assertNotNull(engineOfflineOffline);
  }

  /**
  * Tests that the ModelEngine stores all the correct cities.
  */
  @Test void testGetAllCityLocations(){

    for(int j=0; j<modelEngines.size();j++){
      //check the size of the mocked citites.
      assert(modelEngines.get(j).getAllCityLocations().size()==4);
      //check each of them
      boolean containsSydney = false;
      boolean containsVietnameCity = false;
      boolean containsJuba = false;
      boolean containsBondo = false;

      for(int i=0; i<modelEngines.get(j).getAllCityLocations().size();i++){
        //Sydney
        if(modelEngines.get(j).getAllCityLocations().get(i).getCityId()==2147714
        && modelEngines.get(j).getAllCityLocations().get(i).getCityName().equals("Sydney")
        && modelEngines.get(j).getAllCityLocations().get(i).getStateName().equals("New South Wales")
        && modelEngines.get(j).getAllCityLocations().get(i).getCountryName().equals("Australia")){
          assertEquals(modelEngines.get(j).getAllCityLocations().get(i).getLatitude(), -33.86785, 0.00001);
          assertEquals(modelEngines.get(j).getAllCityLocations().get(i).getLongitude() , 151.20732, 0.00001);
          containsSydney=true;
        }

        //Biên Hòa
        if(modelEngines.get(j).getAllCityLocations().get(i).getCityId()==1587923
        && modelEngines.get(j).getAllCityLocations().get(i).getCityName().equals("Biên Hòa")
        && modelEngines.get(j).getAllCityLocations().get(i).getStateName().equals("Ðồng Nai")
        && modelEngines.get(j).getAllCityLocations().get(i).getCountryName().equals("Vietnam")){
          assertEquals(modelEngines.get(j).getAllCityLocations().get(i).getLatitude(), 10.94469, 0.00001);
          assertEquals(modelEngines.get(j).getAllCityLocations().get(i).getLongitude() , 106.82432, 0.00001);
          containsVietnameCity=true;
        }

        //juba
        if(modelEngines.get(j).getAllCityLocations().get(i).getCityId()==373303
        && modelEngines.get(j).getAllCityLocations().get(i).getCityName().equals("Juba")
        && modelEngines.get(j).getAllCityLocations().get(i).getStateName().equals("")
        && modelEngines.get(j).getAllCityLocations().get(i).getCountryName().equals("")){
          assertEquals(modelEngines.get(j).getAllCityLocations().get(i).getLatitude(), 4.85165, 0.00001);
          assertEquals(modelEngines.get(j).getAllCityLocations().get(i).getLongitude() , 31.58247, 0.00001);
          containsJuba=true;
        }

        //bondo
        if(modelEngines.get(j).getAllCityLocations().get(i).getCityId()==218253
        && modelEngines.get(j).getAllCityLocations().get(i).getCityName().equals("Bondo")
        && modelEngines.get(j).getAllCityLocations().get(i).getStateName().equals("")
        && modelEngines.get(j).getAllCityLocations().get(i).getCountryName().equals("\"Congo, Democratic Republic of The\"")){
          assertEquals(modelEngines.get(j).getAllCityLocations().get(i).getLatitude(), 3.81461, 0.00001);
          assertEquals(modelEngines.get(j).getAllCityLocations().get(i).getLongitude() , 23.68665, 0.00001);
          containsBondo=true;
        }
      }
      assert(containsSydney);
      assert(containsVietnameCity);
      assert(containsJuba);
      assert(containsBondo);

    }
  }

  /**
  * Tests that the ModelEngine can get the weather data for a city, adding it to stored data and being able to recall them all back in added order.
  * Ensures it makes a query to InputEngine. Ensures it replaces if already exists.
  * Does not test cache mock. All cache usage set to false.
  */
  @Test void testGetWeatherDataForCity(){
    for(int i=0; i<modelEngines.size();i++){
      LocationWeather returnVal;
      returnVal = modelEngines.get(i).getWeatherDataForCityLocation(new CityLocation(0,70.0,70.0,"placeholderCityName","placeholderStateName","placeholderCountryName"),false);
      assertEquals(returnVal.getLatitude(),70.0,0.00001);
      assertEquals(returnVal.getLongitude(),70.0,0.00001);
      LocationWeather returnValTwo;
      returnValTwo = modelEngines.get(i).getWeatherDataForCityLocation(new CityLocation(1,42.0,42.0,"placeholderCityName","placeholderStateName","placeholderCountryName"),false);
      assertEquals(returnValTwo.getLatitude(),42.0,0.00001);
      assertEquals(returnValTwo.getLongitude(),42.0,0.00001);
      LocationWeather returnValThree;
      returnValThree = modelEngines.get(i).getWeatherDataForCityLocation(new CityLocation(2,2.0,3.0,"placeholderCityName","placeholderStateName","placeholderCountryName"),false);
      assertEquals(returnValThree.getLatitude(),2.0,0.00001);
      assertEquals(returnValThree.getLongitude(),3.0,0.00001);

      //check the order of data
      List<LocationWeather> allData = modelEngines.get(i).getLocationWeather();
      assert(allData.size()==3);
      int size = modelEngines.get(i).sizeOfSavedData();
      assert(size==3);
      assertEquals(allData.get(0).getLongitude(),70.0,0.00001);
      assertEquals(allData.get(1).getLongitude(),42.0,0.00001);
      assertEquals(allData.get(2).getLongitude(),3.0,0.00001);
    }
  }

  /**
  * Tests that a specifc index can be removed from the data.
  */
  @Test void testRemoveSearchedCity(){
    for(int i=0; i<modelEngines.size();i++){
      //first add some data.
      LocationWeather returnVal = modelEngines.get(i).getWeatherDataForCityLocation(new CityLocation(0,70.0,70.0,"placeholderCityName","placeholderStateName","placeholderCountryName"),false);
      LocationWeather returnValTwo = modelEngines.get(i).getWeatherDataForCityLocation(new CityLocation(1,42.0,42.0,"placeholderCityName","placeholderStateName","placeholderCountryName"),false);
      LocationWeather returnValThree = modelEngines.get(i).getWeatherDataForCityLocation(new CityLocation(2,2.0,3.0,"placeholderCityName","placeholderStateName","placeholderCountryName"),false);

      //now remove one of them
      modelEngines.get(i).removeAtIndex(1);
      //check the order of data
      List<LocationWeather> allData = modelEngines.get(i).getLocationWeather();
      assert(allData.size()==2);
      int size = modelEngines.get(i).sizeOfSavedData();
      assert(size==2);
      assertEquals(allData.get(0).getLongitude(),70.0,0.00001);
      assertEquals(allData.get(1).getLongitude(),3.0,0.00001);
    }
  }

  /**
  * Tests that data can be all cleared.
  */
  @Test void testClearAllData(){
    for(int i=0; i<modelEngines.size();i++){
      //first add some data.
      LocationWeather returnVal = modelEngines.get(i).getWeatherDataForCityLocation(new CityLocation(0,70.0,70.0,"placeholderCityName","placeholderStateName","placeholderCountryName"),false);
      LocationWeather returnValTwo = modelEngines.get(i).getWeatherDataForCityLocation(new CityLocation(1,42.0,42.0,"placeholderCityName","placeholderStateName","placeholderCountryName"),false);
      LocationWeather returnValThree = modelEngines.get(i).getWeatherDataForCityLocation(new CityLocation(2,2.0,3.0,"placeholderCityName","placeholderStateName","placeholderCountryName"),false);

      //now remove one of them
      modelEngines.get(i).clearData();
      //check the order of data
      List<LocationWeather> allData = modelEngines.get(i).getLocationWeather();
      assert(allData.size()==0);
      int size = modelEngines.get(i).sizeOfSavedData();
      assert(size==0);
    }
  }

  /**
  * Tests that data can be added again after removing a specific index.
  */
  @Test void testAddDataAfterSpecificRemoval(){
    for(int i=0; i<modelEngines.size();i++){
      //first add some data.
      LocationWeather returnVal = modelEngines.get(i).getWeatherDataForCityLocation(new CityLocation(0,70.0,70.0,"placeholderCityName","placeholderStateName","placeholderCountryName"),false);
      LocationWeather returnValTwo = modelEngines.get(i).getWeatherDataForCityLocation(new CityLocation(1,42.0,42.0,"placeholderCityName","placeholderStateName","placeholderCountryName"),false);
      LocationWeather returnValThree = modelEngines.get(i).getWeatherDataForCityLocation(new CityLocation(2,2.0,3.0,"placeholderCityName","placeholderStateName","placeholderCountryName"),false);

      //now remove one of them
      modelEngines.get(i).removeAtIndex(1);

      //check data can be added again
      LocationWeather returnValFour = modelEngines.get(i).getWeatherDataForCityLocation(new CityLocation(3,14.0,14.0,"placeholderCityName","placeholderStateName","placeholderCountryName"),false);

      //check order
      List<LocationWeather> allData = modelEngines.get(i).getLocationWeather();
      assert(allData.size()==3);
      int size = modelEngines.get(i).sizeOfSavedData();
      assert(size==3);
      assertEquals(allData.get(0).getLongitude(),70.0,0.00001);
      assertEquals(allData.get(1).getLongitude(),3.0,0.00001);
      assertEquals(allData.get(2).getLongitude(),14.0,0.00001);
      assert(allData.get(0).getCityId()==0);
      assert(allData.get(1).getCityId()==2);
      assert(allData.get(2).getCityId()==3);
    }
  }

  /**
  * Tests that data can be added again after clearing.
  */
  @Test void testClearAllDataAddData(){
    for(int i=0; i<modelEngines.size();i++){
      //first add some data.
      LocationWeather returnVal = modelEngines.get(i).getWeatherDataForCityLocation(new CityLocation(0,70.0,70.0,"placeholderCityName","placeholderStateName","placeholderCountryName"),false);
      LocationWeather returnValTwo = modelEngines.get(i).getWeatherDataForCityLocation(new CityLocation(1,42.0,42.0,"placeholderCityName","placeholderStateName","placeholderCountryName"),false);
      LocationWeather returnValThree = modelEngines.get(i).getWeatherDataForCityLocation(new CityLocation(2,2.0,3.0,"placeholderCityName","placeholderStateName","placeholderCountryName"),false);

      modelEngines.get(i).clearData();

      //check data can be added again
      LocationWeather returnValFour = modelEngines.get(i).getWeatherDataForCityLocation(new CityLocation(3,14.0,14.0,"placeholderCityName","placeholderStateName","placeholderCountryName"),false);

      //check order
      List<LocationWeather> allData = modelEngines.get(i).getLocationWeather();
      assert(allData.size()==1);
      int size = modelEngines.get(i).sizeOfSavedData();
      assert(size==1);
      assertEquals(allData.get(0).getLongitude(),14.0,0.00001);
      assert(allData.get(0).getCityId()==3);
    }
  }

  /**
  * Tests that adding the same city again moves it to the back of the list and updates its values.
  */
  @Test void testAddSameCityAgain(){
    for(int i=0; i<modelEngines.size();i++){
      //first add some data.
      LocationWeather returnVal = modelEngines.get(i).getWeatherDataForCityLocation(new CityLocation(0,70.0,70.0,"placeholderCityName","placeholderStateName","placeholderCountryName"),false);
      LocationWeather returnValTwo = modelEngines.get(i).getWeatherDataForCityLocation(new CityLocation(1,42.0,42.0,"placeholderCityName","placeholderStateName","placeholderCountryName"),false);

      //add 1st one again
      LocationWeather returnValThree = modelEngines.get(i).getWeatherDataForCityLocation(new CityLocation(0,2.0,3.0,"placeholderCityName","placeholderStateName","placeholderCountryName"),false);

      //check order & updated values.
      List<LocationWeather> allData = modelEngines.get(i).getLocationWeather();
      assert(allData.size()==2);
      int size = modelEngines.get(i).sizeOfSavedData();
      assert(size==2);
      assertEquals(allData.get(0).getLongitude(),42.0,0.00001);
      assert(allData.get(0).getCityId()==1);
      assertEquals(allData.get(1).getLongitude(),3.0,0.00001);
      assert(allData.get(1).getCityId()==0);
    }
  }


  /**
  * Tests that data state and order correct after multiple clears, removals and additions.
  */
  @Test void testComplexMultipleClearsRemovalsAdditions(){
    for(int i=0; i<modelEngines.size();i++){
      //first add some data.
      LocationWeather returnVal = modelEngines.get(i).getWeatherDataForCityLocation(new CityLocation(0,70.0,70.0,"placeholderCityName","placeholderStateName","placeholderCountryName"),false);
      LocationWeather returnValTwo = modelEngines.get(i).getWeatherDataForCityLocation(new CityLocation(1,42.0,42.0,"placeholderCityName","placeholderStateName","placeholderCountryName"),false);
      LocationWeather returnValThree = modelEngines.get(i).getWeatherDataForCityLocation(new CityLocation(2,2.0,3.0,"placeholderCityName","placeholderStateName","placeholderCountryName"),false);

      modelEngines.get(i).removeAtIndex(0);

      modelEngines.get(i).removeAtIndex(0);

      //check data can be added again
      LocationWeather returnValFour = modelEngines.get(i).getWeatherDataForCityLocation(new CityLocation(3,14.0,14.0,"placeholderCityName","placeholderStateName","placeholderCountryName"),false);

      //check order
      List<LocationWeather> allData = modelEngines.get(i).getLocationWeather();
      assert(allData.size()==2);
      int size = modelEngines.get(i).sizeOfSavedData();
      assert(size==2);
      assertEquals(allData.get(0).getLongitude(),3.0,0.00001);
      assert(allData.get(0).getCityId()==2);
      assertEquals(allData.get(1).getLongitude(),14.0,0.00001);
      assert(allData.get(1).getCityId()==3);

      //clear
      modelEngines.get(i).clearData();

      List<LocationWeather> allDataAgain = modelEngines.get(i).getLocationWeather();
      assert(allDataAgain.size()==0);

      //add new data
      LocationWeather returnValFive = modelEngines.get(i).getWeatherDataForCityLocation(new CityLocation(4,5.0,5.0,"placeholderCityName","placeholderStateName","placeholderCountryName"),false);
      LocationWeather returnValSix = modelEngines.get(i).getWeatherDataForCityLocation(new CityLocation(5,6.0,6.0,"placeholderCityName","placeholderStateName","placeholderCountryName"),false);

      List<LocationWeather> allDataAgainAgain = modelEngines.get(i).getLocationWeather();
      assert(allDataAgainAgain.size()==2);
      int sizeAgain = modelEngines.get(i).sizeOfSavedData();
      assert(sizeAgain==2);
      assertEquals(allDataAgainAgain.get(0).getLongitude(),5.0,0.00001);
      assert(allDataAgainAgain.get(0).getCityId()==4);
      assertEquals(allDataAgainAgain.get(1).getLongitude(),6.0,0.00001);
      assert(allDataAgainAgain.get(1).getCityId()==5);
    }
  }

  /**
  * Tests that the cache interaction is used properly based on the outcome of
  * checkWeatherDataExistsInCache(). The performance of the offline (dummy) cache
  * and online cache is the same in regards to the scenarios where calls are made
  * to cache methods and the arguments that are given. Hence, the mocked online
  * cache is all that is tested.
  */
  @Test void testCacheInteraction(){
    //test the mocked online cache.
    //start by returning true.
    //start with it exsiting in the cache.

    when(mockCacheEngineOnline.checkWeatherDataExistsInCache(anyInt())).thenReturn(true);

    //SCENARIO 1
    //check that when getWeather is called in model and cache used is FALSE
    //and exists is set to true, modify is called on the cache.
    LocationWeather returnValOne = modelEngines.get(0).getWeatherDataForCityLocation(
    new CityLocation(4,5.0,5.0,"placeholderCityName","placeholderStateName","placeholderCountryName"),false);
    //check modify is called.
    ArgumentCaptor<String> stringArgs = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<Integer> intArgs = ArgumentCaptor.forClass(Integer.class);

    verify(mockCacheEngineOnline,times(1)).modifyExistingWeatherDataInCache(intArgs.capture(),stringArgs.capture());

    List<String> capturedStrings = stringArgs.getAllValues();
    List<Integer> capturedInts = intArgs.getAllValues();

    assert(capturedInts.get(0) == 4); //city id is 4
    //convert the string to something easier to check.
    CurrentWeatherData givenWeatherData = JsonWeatherParser.parseJsonStringToCurrentWeatherData(capturedStrings.get(0));
    assert(givenWeatherData.getLatitude()==5.0);
    assert(givenWeatherData.getLongitude()==5.0);


    //SCENARIO 2
    //now check that when getWeather is called in model and cache used is FALSE,
    //if exsits is set to false addWeatherDataToCache() is called instead.
    when(mockCacheEngineOnline.checkWeatherDataExistsInCache(anyInt())).thenReturn(false);
    LocationWeather returnValTwo = modelEngines.get(0).getWeatherDataForCityLocation(
    new CityLocation(4,5.0,5.0,"placeholderCityName","placeholderStateName","placeholderCountryName"),false);

    ArgumentCaptor<String> stringArgsTwo = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<Integer> intArgsTwo = ArgumentCaptor.forClass(Integer.class);

    verify(mockCacheEngineOnline,times(1)).addWeatherDataToCache(intArgsTwo.capture(),stringArgsTwo.capture());

    List<String> capturedStringsTwo = stringArgsTwo.getAllValues();
    List<Integer> capturedIntsTwo = intArgsTwo.getAllValues();

    assert(capturedIntsTwo.get(0) == 4); //city id is 4
    //convert the string to something easier to check.
    CurrentWeatherData givenWeatherDataTwo = JsonWeatherParser.parseJsonStringToCurrentWeatherData(capturedStringsTwo.get(0));
    assert(givenWeatherDataTwo.getLatitude()==5.0);
    assert(givenWeatherDataTwo.getLongitude()==5.0);

    //SCENARIO 3
    //now check that when getWeather is called in model and cache used is TRUE,
    //and it exists is set to true, then get is called on the cache.
    when(mockCacheEngineOnline.checkWeatherDataExistsInCache(anyInt())).thenReturn(true);
    ArgumentCaptor<Integer> intArgsThree = ArgumentCaptor.forClass(Integer.class);

    LocationWeather returnValThree = modelEngines.get(0).getWeatherDataForCityLocation(
    new CityLocation(5,5.0,5.0,"placeholderCityName","placeholderStateName","placeholderCountryName"),true);
    verify(mockCacheEngineOnline,times(1)).getWeatherDataForCityFromCache(intArgsThree.capture());
    List<Integer> capturedIntsThree = intArgsThree.getAllValues();
    assert(capturedIntsThree.get(0)==5);

    //SCENARIO 4
    //now check that when getWeather is called in the model and cache used is TRUE,
    //and it exists is set to FALSE, then addWeatherDataToCache is called.
    when(mockCacheEngineOnline.checkWeatherDataExistsInCache(anyInt())).thenReturn(false);
    LocationWeather returnValFour = modelEngines.get(0).getWeatherDataForCityLocation(
    new CityLocation(2,10.0,10.0,"placeholderCityName","placeholderStateName","placeholderCountryName"),false);

    ArgumentCaptor<String> stringArgsFour = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<Integer> intArgsFour = ArgumentCaptor.forClass(Integer.class);

    verify(mockCacheEngineOnline,times(2)).addWeatherDataToCache(intArgsFour.capture(),stringArgsFour.capture());

    List<String> capturedStringsFour = stringArgsFour.getAllValues();
    List<Integer> capturedIntsFour = intArgsFour.getAllValues();

    assert(capturedIntsFour.get(1) == 2); //city id is 2
    //convert the string to something easier to check.
    CurrentWeatherData givenWeatherDataFour = JsonWeatherParser.parseJsonStringToCurrentWeatherData(capturedStringsFour.get(1));
    assert(givenWeatherDataFour.getLatitude()==10.0);
    assert(givenWeatherDataFour.getLongitude()==10.0);

  }

  /**
  * Tests that the WeatherObserver objects added to the model are notified
  * the correct number of times and in the correct scenarios based on the
  * expected ModelEngine method actions.
  */
  @Test void testWeatherObserverNotifiedByModel(){
    /* updateWeatherObserver occurs in 3 places:
    1. clearData()
    2. removeAtIndex()
    3. getWeatherDataForCityLocation()
    */

    //make observers
    WeatherObserver mockedObserverOne = mock(WeatherObserver.class);
    WeatherObserver mockedObserverTwo = mock(WeatherObserver.class);
    WeatherObserver mockedObserverThree = mock(WeatherObserver.class);

    //testing clearData().
    for(int i=0; i<4; i++){
      ModelEngine engine = modelEngines.get(i);
      //add the observers
      engine.registerWeatherObserver(mockedObserverOne);
      engine.registerWeatherObserver(mockedObserverTwo);
      engine.registerWeatherObserver(mockedObserverThree);

      //call clear data.
      engine.clearData();
      //check notified.
      verify(mockedObserverOne, times(1)).updateWeatherObserver();
      verify(mockedObserverTwo, times(1)).updateWeatherObserver();
      verify(mockedObserverThree, times(1)).updateWeatherObserver();
      reset(mockedObserverOne);
      reset(mockedObserverTwo);
      reset(mockedObserverThree);
    }

    //testing removeAtIndex and getWeatherDataForCityLocation
    //Should be +1 for each getWeatherDataForCityLocation, and +1 for each removeAtIndex
    for(int i=0; i<4; i++){
      ModelEngine engine = modelEngines.get(i);
      //need to add data to work.
      modelEngines.get(i).getWeatherDataForCityLocation(new CityLocation(0,70.0,70.0,"placeholderCityName","placeholderStateName","placeholderCountryName"),false);
      modelEngines.get(i).getWeatherDataForCityLocation(new CityLocation(1,70.0,70.0,"placeholderCityName","placeholderStateName","placeholderCountryName"),false);
      modelEngines.get(i).getWeatherDataForCityLocation(new CityLocation(2,70.0,70.0,"placeholderCityName","placeholderStateName","placeholderCountryName"),false);

      engine.removeAtIndex(1);
      //verify
      verify(mockedObserverOne, times(4)).updateWeatherObserver();
      verify(mockedObserverTwo, times(4)).updateWeatherObserver();
      verify(mockedObserverThree, times(4)).updateWeatherObserver();
      engine.removeAtIndex(1);
      //verify
      verify(mockedObserverOne, times(5)).updateWeatherObserver();
      verify(mockedObserverTwo, times(5)).updateWeatherObserver();
      verify(mockedObserverThree, times(5)).updateWeatherObserver();

      reset(mockedObserverOne);
      reset(mockedObserverTwo);
      reset(mockedObserverThree);
    }
  }

  /**
  * Tests that clearCache() command calls the necessary method in CacheEngine.
  */
  @Test void testClearCache(){
    ModelEngine modelEngineWithOnlineCache = modelEngines.get(0);
    ModelEngine modelEngineWithOfflineCache = modelEngines.get(3);

    //check online cache works
    modelEngineWithOnlineCache.clearCache();
    verify(mockCacheEngineOnline,times(1)).clearCache();

    //check offline cache works
    modelEngineWithOfflineCache.clearCache();
    verify(mockCacheEngineOffline,times(1)).clearCache();
  }


  /**
  * Tests that when getWeatherDataForCityLocation is given null as boolean,
  * the notifyCacheHitObservers() is called when a cache hit occurs, and normal getCurrentWeatherForLocation
  * is called when no cache hit.
  */
  @Test void testGetWeatherForCityLocationNullBooleanCacheHitAndMiss(){
    CacheHitObserver mockedObserverOne = mock(CacheHitObserver.class);
    CacheHitObserver mockedObserverTwo = mock(CacheHitObserver.class);
    WeatherObserver mockedWeatherObserver = mock(WeatherObserver.class);

    for(int i=0; i<4; i++){
      //initially mocked to false.
      when(mockCacheEngineOnline.checkWeatherDataExistsInCache(anyInt())).thenReturn(false);
      when(mockCacheEngineOffline.checkWeatherDataExistsInCache(anyInt())).thenReturn(false);

      ModelEngine engine = modelEngines.get(i);
      //cache by default returns false.

      //add the observers
      engine.registerCacheHitObserver(mockedObserverOne);
      engine.registerCacheHitObserver(mockedObserverTwo);

      engine.registerWeatherObserver(mockedWeatherObserver);

      //add data using null.
      LocationWeather result = engine.getWeatherDataForCityLocation(new CityLocation(0,70.0,70.0,"placeholderCityName","placeholderStateName","placeholderCountryName"),null);
      assertNotNull(result);

      //check that weather observer was called.
      verify(mockedWeatherObserver,times(1)).updateWeatherObserver();
      verify(mockedObserverOne, times(0)).cacheHit();
      verify(mockedObserverTwo, times(0)).cacheHit();

      //reset the mocks;
      reset(mockedObserverOne);
      reset(mockedObserverTwo);
      reset(mockedWeatherObserver);

      //now mock the cache to return true.
      when(mockCacheEngineOnline.checkWeatherDataExistsInCache(anyInt())).thenReturn(true);
      when(mockCacheEngineOffline.checkWeatherDataExistsInCache(anyInt())).thenReturn(true);

      LocationWeather resultTwo = engine.getWeatherDataForCityLocation(new CityLocation(0,70.0,70.0,"placeholderCityName","placeholderStateName","placeholderCountryName"),null);
      //should return null
      assertNull(resultTwo);

      //verify cache hits, but no updated weather:
      verify(mockedWeatherObserver,times(0)).updateWeatherObserver();
      verify(mockedObserverOne, times(1)).cacheHit();
      verify(mockedObserverTwo, times(1)).cacheHit();

      //reset
      reset(mockedObserverOne);
      reset(mockedObserverTwo);
      reset(mockedWeatherObserver);
    }
  }


  /**
  * Tests both the invalid and valid scenarios when emails are sent, ensuring the appropriate
  * calls are made to the mocks. Checks the setting and retreival of getMostRecentError()
  * method for the invalid states.
  * Ensures that an email is not attempted to be sent to OnlineOutputEngine by the ModelEngine
  * if the weather data is empty, the email given is null / empty string, the sendgridAPIKey
  * is null or the sendgridFromEmail is null. Checks no calls to mock are made.
  * Also checks the scenarios of valid email sends, and checks the contents of the
  * arguments sent are correct.
  * Note: This test also contains the equivalent of testGetWeatherDataForCityUpdatesError()
  * for the sendReport method.
  */
  @Test void testSendReportAndGetMostRecentErrorValidAndInvalid(){
    //The sendReport is only valid for online output engine.
    //The sendReport is unaffected by online/offline state of inputEngine or cache, so
    //will not be tested in this method.
    //The offline output engine returns fixed values which are tested in the next method.

    //first check empty data returns null.
    ModelEngine engineOnlineOnline = modelEngines.get(0);

    Exception exceptionOne = assertThrows(IllegalArgumentException.class, () -> engineOnlineOnline.sendReport("example@email.com"));
    //check message.
    assertEquals(exceptionOne.getMessage(),"Weather data is empty.");

    //add a pieces of data, and check that invalid emails also get rejected.
    LocationWeather returnVal = engineOnlineOnline.getWeatherDataForCityLocation(new CityLocation(0,70.0,70.0,"placeholderCityName","placeholderStateName","placeholderCountryName"),false);
    Exception exceptionTwo = assertThrows(IllegalArgumentException.class, () -> engineOnlineOnline.sendReport(null));
    assertEquals(exceptionTwo.getMessage(),"Please enter a valid 'to' email.");
    Exception exceptionThree = assertThrows(IllegalArgumentException.class, () -> engineOnlineOnline.sendReport(""));
    assertEquals(exceptionThree.getMessage(),"Please enter a valid 'to' email.");

    ArgumentCaptor<String> stringArgs = ArgumentCaptor.forClass(String.class);

    //now, with valid email check it does work
    assertDoesNotThrow(() -> engineOnlineOnline.sendReport("example@email.com"));

    //check the mock is only called once.
    try {
      verify(mockOutputEngineOnline, times(1)).sendEmail(stringArgs.capture(),stringArgs.capture());
    } catch (Exception ignored) {assert(1==2);};

    List<String> capturedStrings = stringArgs.getAllValues();

    /* Check the arguments sent to sendEmail in the mock are correct. */
    //arg[0] is the apiKey
    assert(capturedStrings.get(0).equals("dummyKey"));
    //arg[0] is the emailBody.
    //convert the current data to the email string.
    String emailSubject = "Weather Report";
    String htmlEmailContent =  "";
    htmlEmailContent += "<p><strong>"+returnVal.getCityLocation().toString().replace("\"", "\\\"") + "</strong>: "+returnVal.getCurrentWeatherData().toEmailNeatString() +"</p>";
    htmlEmailContent += "<br>";
    String jsonString = "{\"personalizations\":[{\"to\":[{\"email\":\"example@email.com\",\"name\":\"target email\"}]}],\"from\":{\"email\":\"dummyEmail\",\"name\":\"Weather Tracker\"},\"subject\":\""+emailSubject+"\",\"content\":[{\"type\":\"text/html\",\"value\":\""+htmlEmailContent+"\"}]}";
    assert(capturedStrings.get(1).equals(jsonString));

    //now de-set the api key, and ensure it errors.
    //make a new model without the api keys
    ModelEngine noOutputKeyEngineOnlineOnline = new ModelEngine(mockInputEngineOnline,mockOutputEngineOnline,mockCacheEngineOnline,"dummykey",null,"emailKey",locationsFileLoader);
    //add data
    noOutputKeyEngineOnlineOnline.getWeatherDataForCityLocation(new CityLocation(0,70.0,70.0,"placeholderCityName","placeholderStateName","placeholderCountryName"),false);
    //send valid request
    Exception exceptionFour = assertThrows(IllegalArgumentException.class, () -> noOutputKeyEngineOnlineOnline.sendReport("validToEmail@gmail.com"));
    assertEquals(exceptionFour.getMessage(),"The Sendgrid API key is not set in environment variable \"SENDGRID_API_KEY\"");

    //now deset the emailKey and check the getMessage
    ModelEngine noOutputFromEmailEngineOnlineOnline = new ModelEngine(mockInputEngineOnline,mockOutputEngineOnline,mockCacheEngineOnline,"dummykey","dummyKey",null,locationsFileLoader);
    //add data
    noOutputFromEmailEngineOnlineOnline.getWeatherDataForCityLocation(new CityLocation(0,70.0,70.0,"placeholderCityName","placeholderStateName","placeholderCountryName"),false);
    //send valid request
    Exception exceptionFive = assertThrows(IllegalArgumentException.class, () -> noOutputFromEmailEngineOnlineOnline.sendReport("validToEmail@gmail.com"));
    assertEquals(exceptionFive.getMessage(),"The Sendgrid 'from' email is not set in environment variable \"SENDGRID_API_EMAIL\"");
  }

  /**
  * Tests that sendReport() notifies all LoadingObservers only when a valid email, apikey and data
  * input scenario is met, ensuring that they are alerted to both the start of the loading
  * and its conclusion within the same method.
  */
  @Test void testSendReportOnlyValidNotifiesObservers(){
    //need to mock observer class
    LoadingObserver mockedObserverOne = mock(LoadingObserver.class);
    LoadingObserver mockedObserverTwo = mock(LoadingObserver.class);
    //add them to the engine
    ModelEngine engineOnlineOnline = modelEngines.get(0);
    engineOnlineOnline.registerLoadingObserver(mockedObserverOne);
    engineOnlineOnline.registerLoadingObserver(mockedObserverTwo);

    //do a valid email. First add data.
    engineOnlineOnline.getWeatherDataForCityLocation(new CityLocation(0,70.0,70.0,"placeholderCityName","placeholderStateName","placeholderCountryName"),false);

    assertDoesNotThrow(() -> engineOnlineOnline.sendReport("example@email.com"));

    //check both observers are notified. Both notified twice, as told when started and ended.
    //should be 2 for the add weather, and two for the send report.
    verify(mockedObserverOne, times(4)).updateLoadingObserver();
    verify(mockedObserverTwo, times(4)).updateLoadingObserver();


    //now check that invalid doesn't notify observers.
    LoadingObserver mockedObserverThree = mock(LoadingObserver.class);
    LoadingObserver mockedObserverFour = mock(LoadingObserver.class);
    //add them to the engine
    ModelEngine engineOnlineOnlineTwo = new ModelEngine(mockInputEngineOnline,mockOutputEngineOnline,mockCacheEngineOnline,"dummyKey","dummyKey","dummyEmail",locationsFileLoader);
    engineOnlineOnlineTwo.registerLoadingObserver(mockedObserverThree);
    engineOnlineOnlineTwo.registerLoadingObserver(mockedObserverFour);

    //do a valid email. First add data.

    //no data
    Exception exceptionOne = assertThrows(IllegalArgumentException.class, () -> engineOnlineOnlineTwo.sendReport("example@email.com"));
    assertEquals(exceptionOne.getMessage(),"Weather data is empty.");
    //check no observers notified.
    verify(mockedObserverThree, times(0)).updateLoadingObserver();
    verify(mockedObserverFour, times(0)).updateLoadingObserver();

    //with data but invalid email.
    engineOnlineOnlineTwo.getWeatherDataForCityLocation(new CityLocation(0,70.0,70.0,"placeholderCityName","placeholderStateName","placeholderCountryName"),false);
    //will have two from the getWeather, but none from the report.
    Exception exceptionTwo = assertThrows(IllegalArgumentException.class, () -> engineOnlineOnlineTwo.sendReport(null));
    assertEquals(exceptionTwo.getMessage(),"Please enter a valid 'to' email.");
    verify(mockedObserverThree, times(2)).updateLoadingObserver();
    verify(mockedObserverFour, times(2)).updateLoadingObserver();
  }

  /**
  * Tests that getWeatherDataForCityLocation correctly updates mostRecentError
  * when an error occurs within its own method.
  */
  @Test void testGetWeatherDataForCityUpdatesError(){
    /* getWeather only errors on its own when input api key is not set.
    But input API key is only used in 2 scenarios:
    1. cache used is false.
    2. cache used is true, but it doesn't exist.
    */
    //only deals with input api online/offline, so testing only required on 2 models
    ModelEngine customEngineOnlineOnline = new ModelEngine(mockInputEngineOnline,mockOutputEngineOnline,mockCacheEngineOnline,null,null,null,locationsFileLoader);
    ModelEngine customEngineOfflineOffline = new ModelEngine(mockInputEngineOffline,mockOutputEngineOffline,mockCacheEngineOffline,null,null,null,locationsFileLoader);
    when(mockCacheEngineOnline.checkWeatherDataExistsInCache(anyInt())).thenReturn(false);
    when(mockCacheEngineOffline.checkWeatherDataExistsInCache(anyInt())).thenReturn(false);

    Exception exceptionOne = assertThrows(IllegalArgumentException.class, () -> customEngineOnlineOnline.getWeatherDataForCityLocation(
    new CityLocation(0,70.0,70.0,"placeholderCityName","placeholderStateName","placeholderCountryName"),false));
    //check message.
    assertEquals(exceptionOne.getMessage(),"The Weatherbit API key is not set in environment variable 'INPUT_API_KEY'");

    //now try scenario 1 for onlinelonline with use cache as true
    Exception exceptionTwo = assertThrows(IllegalArgumentException.class, () -> customEngineOnlineOnline.getWeatherDataForCityLocation(
    new CityLocation(0,70.0,70.0,"placeholderCityName","placeholderStateName","placeholderCountryName"),true));
    //check message.
    assertEquals(exceptionTwo.getMessage(),"The Weatherbit API key is not set in environment variable 'INPUT_API_KEY'");

    //now try scenario 2
    Exception exceptionThree = assertThrows(IllegalArgumentException.class, () -> customEngineOfflineOffline.getWeatherDataForCityLocation(
    new CityLocation(0,70.0,70.0,"placeholderCityName","placeholderStateName","placeholderCountryName"),false));
    //check message.
    assertEquals(exceptionThree.getMessage(),"The Weatherbit API key is not set in environment variable 'INPUT_API_KEY'");


    //now try scenario 1 for offlineoffline with use cache as true
    Exception exceptionFour = assertThrows(IllegalArgumentException.class, () -> customEngineOfflineOffline.getWeatherDataForCityLocation(
    new CityLocation(0,70.0,70.0,"placeholderCityName","placeholderStateName","placeholderCountryName"),true));
    //check message.
    assertEquals(exceptionFour.getMessage(),"The Weatherbit API key is not set in environment variable 'INPUT_API_KEY'");

  }

  /**
  * Tests that getWeatherDataForCityLocation correctly forwards API errors
  * it receives.
  */
  @Test void testGetWeatherDataForCityForwardsAPIErrors() {
    //if getWEather is perfectly valid in ModelEngine, ensure any error
    //from the mocked API is forwarded.
    for(int i=0; i<4; i++){
      try {
        when(mockInputEngineOnline.getCurrentWeatherForLocation(anyString(), anyInt(), anyDouble(),anyDouble())).thenThrow(new Exception("Customised input API error."));
        when(mockInputEngineOffline.getCurrentWeatherForLocation(anyString(), anyInt(), anyDouble(),anyDouble())).thenThrow(new Exception("Customised input API error."));
      } catch (Exception ignored) {}
      ModelEngine engine = modelEngines.get(i);
      Exception exception = assertThrows(IllegalArgumentException.class, () -> engine.getWeatherDataForCityLocation(
        new CityLocation(0,70.0,70.0,"placeholderCityName","placeholderStateName","placeholderCountryName"),false));
      //check message is forwarded by the model.
      assertEquals(exception.getMessage(),"Customised input API error.");
    }
  }

  /**
  * Tests that sendReport correctly forwards API errors it receives.
  */
  @Test void testSendReportForwardsAPIErrors(){
    //if sendReport is perfectly valid, check that an error in output API is forwarded.
    for(int i=0; i<4; i++){
      try {
        doThrow(new Exception("Another customised output API error.")).when(mockOutputEngineOnline).sendEmail(anyString(), anyString());
        doThrow(new Exception("Another customised output API error.")).when(mockOutputEngineOffline).sendEmail(anyString(), anyString());
      } catch (Exception ignored) {}
        //add some data
        modelEngines.get(i).getWeatherDataForCityLocation(
          new CityLocation(0,70.0,70.0,"placeholderCityName","placeholderStateName","placeholderCountryName"),false);

      ModelEngine engine = modelEngines.get(i);
      Exception exception = assertThrows(IllegalArgumentException.class, () -> engine.sendReport("example@gmail.com"));
      //check message is forwarded by the model.
      assertEquals(exception.getMessage(),"Another customised output API error.");
    }
  }

  /**
  * Tests the About page information is correctly given.
  */
  @Test void testAboutInformation(){
    for(int i=0; i<4; i++){
      assert(modelEngines.get(i).getAboutInformation().getCreatorName().equals("Luke Henning-Smith"));
      assert(modelEngines.get(i).getAboutInformation().getApplicationName().equals("Weather Tracker"));
      assertNotNull(modelEngines.get(i).getAboutInformation().getReferences());
    }
  }


}
