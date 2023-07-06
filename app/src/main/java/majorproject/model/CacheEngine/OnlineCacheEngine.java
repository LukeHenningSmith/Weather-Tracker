package majorproject.model.cacheengine;

import java.io.File;
import java.sql.*;

import java.util.List;
import java.util.ArrayList;

/**
* An implementation of CacheEngine that uses a local sqlite database to cache weather data jsonStrings to cityId primary keys.
*/
public class OnlineCacheEngine implements CacheEngine {
  /*
  Private variables storing the location of the database from root of gradle project,
  as well as the URL for the database.
  */
  private static final String dbName = "src/main/resources/database/weatherdata.db";
  private static final String dbURL = "jdbc:sqlite:" + dbName;

  /**
  * Constructs the city weather data database if it doesn't exist yet.
  */
  public OnlineCacheEngine(){
    createDB();
    setupDB();
  }

  /**
  * Creates the city weather database.
  */
  private void createDB() {
    File dbFile = new File(dbName);
    if (dbFile.exists()) {
      return;
    }
    try (Connection ignored = DriverManager.getConnection(dbURL)) {
      // If we get here that means no exception raised from getConnection - meaning it worked
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  /**
  * Sets up the single table of cityId primary key and jsonString weather data for the database (if it doesn't already exist).
  */
  private void setupDB() {
    String createWeatherTableSQL =
    """
    CREATE TABLE IF NOT EXISTS weather (
    cityId integer PRIMARY KEY,
    jsonString text NOT NULL
    );
    """;

    try (Connection conn = DriverManager.getConnection(dbURL);
    Statement statement = conn.createStatement()) {
      statement.execute(createWeatherTableSQL);
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  public boolean addWeatherDataToCache(int cityId, String jsonString){
    String addNewWeatherSQL =
    """
    INSERT INTO weather(cityId, jsonString) VALUES
    (?,?)
    """;

    try (Connection conn = DriverManager.getConnection(dbURL);
    PreparedStatement preparedStatement = conn.prepareStatement(addNewWeatherSQL)) {
      preparedStatement.setInt(1, cityId);
      preparedStatement.setString(2,jsonString);
      preparedStatement.executeUpdate();

    } catch (SQLException e) {
      System.out.println(e.getMessage());
      return false;
    }
    return true;
  }

  public boolean checkWeatherDataExistsInCache(int cityId) {
    String weatherExistsSQL =
    """
    SELECT cityId FROM 'weather' WHERE cityId=?
    """;
    try (Connection conn = DriverManager.getConnection(dbURL);
    PreparedStatement preparedStatement = conn.prepareStatement(weatherExistsSQL)) {
      preparedStatement.setInt(1,cityId);
      ResultSet results = preparedStatement.executeQuery();
      if(results == null){
        preparedStatement.close();
        results.close();
        return false;
      }
      int loopCount = 0;
      while(results.next()){
        loopCount++;
        if(loopCount>1){
          return false;
        }
        if(results.getInt("cityId") != cityId){
          preparedStatement.close();
          results.close();
          return false;
        } else {
          preparedStatement.close();
          results.close();
          return true;
        }
      }
      return false;
    } catch (SQLException e) {
      System.out.println(e.getMessage());
      return false;
    }
  }

  public void modifyExistingWeatherDataInCache(int cityId, String jsonString) {
    //lat, lon, temp, wind_spd, wind_cdir_full, clouds, precip, aqi, icon, code, description
    String modifyExistingWeatherDataSQL =
    """
    UPDATE weather SET jsonString=? WHERE cityId=?
    """;

    try (Connection conn = DriverManager.getConnection(dbURL);
    PreparedStatement preparedStatement = conn.prepareStatement(modifyExistingWeatherDataSQL)) {
      preparedStatement.setString(1, jsonString);
      preparedStatement.setInt(2, cityId);
      preparedStatement.executeUpdate();
    } catch (SQLException e) {
      System.out.println(e.getMessage());
      return;
    }
  }

  public String getWeatherDataForCityFromCache(int cityId) {
    //returns null if doesn't exist
    //else, returns the id
    String getWeatherForCitySQL =
    """
    SELECT * FROM weather WHERE cityId=?
    """;
    try (Connection conn = DriverManager.getConnection(dbURL);
    PreparedStatement preparedStatement = conn.prepareStatement(getWeatherForCitySQL)) {
      preparedStatement.setInt(1,cityId);
      ResultSet results = preparedStatement.executeQuery();
      //should be a single row
      if(results == null){
        return null;
      }
      //initially positioned before the first row
      if(results.next() == false){
        //no rows
        return null;
      }
      return results.getString("jsonString");

    } catch (SQLException e) {
      System.out.println(e.getMessage());
      return null;
    }
  }


  public void clearCache(){
    //drop table, then remake it
    String dropTableSQL =
    """
    DROP TABLE weather
    """;
    try (Connection conn = DriverManager.getConnection(dbURL);
    Statement statement = conn.createStatement()) {
      statement.execute(dropTableSQL);
    } catch (SQLException e) {
      System.out.println(e.getMessage());
      return;
    }

    //then add table back
    setupDB();
  }

}
