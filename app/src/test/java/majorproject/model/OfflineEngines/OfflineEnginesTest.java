package majorproject.test.model;

import majorproject.model.outputengine.*;
import majorproject.model.inputengine.*;

import java.util.List;
import java.util.ArrayList;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;

/**
* Tests the OfflineInputEngine and OfflineOutputEngine classes single methods perform correctly.
*/
class OfflineEnginesTest {
  /*
    Since the Offline engines are not tested in ModelEngineTest, they are tested
    in this class simply for completeness of model testing.
    Obviously the Online engines are not tested.
  */

  private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
  private final PrintStream originalOut = System.out;

  /**
  * Setups up the standard out print stream to a new print stream for the purpose of testing.
  */
  @BeforeEach
  void setUpStreams() {
      System.setOut(new PrintStream(outContent));
  }

  /**
  * Reverts the modified standard out print stream back to normal standard out stream.
  */
  @AfterEach
  void restoreStreams() {
      System.setOut(originalOut);
  }

  /**
  * Tests that OfflineOutputEngine can be constructed.
  */
  @Test
  void testOfflineOutputEngineConstruction(){
    OfflineOutputEngine engine = new OfflineOutputEngine();
    assertNotNull(engine);
  }

  /**
  * Tests that the single method in OfflineOutputEngine does the correct print statements to standard out.
  */
  @Test
  void testOfflineOutputEngineSendEmail(){
    OfflineOutputEngine engine = new OfflineOutputEngine();
    engine.sendEmail("key","Test body.");
    assertEquals("The email that would have been sent would have the content of:\nTest body.\n",outContent.toString());
  }

  /**
  * Tests that OfflineInputEngine can be constructed.
  */
  @Test
  void testOfflineInputEngineConstruction(){
    OfflineInputEngine engine = new OfflineInputEngine();
    assertNotNull(engine);
  }

  /**
  * Tests that the single method in OfflineInputEngine returns the correct value.
  */
  @Test
  void testOfflineInputEngineGetWeather(){
    OfflineInputEngine engine = new OfflineInputEngine();
    String returnedString = engine.getCurrentWeatherForLocation("Key",101,1001.9,-1001.1);
    assertEquals("{\"data\":[{\"rh\":80.6875,\"pod\":\"n\",\"lon\":-1001.1,\"pres\":1004,\"timezone\":\"placeholder\",\"ob_time\":\"2022-05-11 13:04\",\"country_code\":\"BD\",\"clouds\":0,\"ts\":1652274295,\"solar_rad\":0,\"state_code\":\"85\",\"city_name\":\"Placeholder\",\"wind_spd\":0.0,\"wind_cdir_full\":\"Placeholder\",\"wind_cdir\":\"S\",\"slp\":1004.5,\"vis\":1.5,\"h_angle\":90,\"sunset\":\"12:31\",\"dni\":0,\"dewpt\":24.1,\"snow\":0,\"uv\":0,\"precip\":0.0,\"wind_dir\":179,\"sunrise\":\"23:17\",\"ghi\":0,\"dhi\":0,\"aqi\":0,\"lat\":1001.9,\"weather\":{\"icon\":\"a01d\",\"code\":800,\"description\":\"Placeholder\"},\"datetime\":\"2022-05-11:12\",\"temp\":0.0,\"station\":\"VGHS\",\"elev_angle\":-7.49,\"app_temp\":31.3}],\"count\":1}",returnedString);
  }
}
