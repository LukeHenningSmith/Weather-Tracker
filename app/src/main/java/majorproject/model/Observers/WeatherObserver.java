package majorproject.model.observers;

/**
* Observer pattern interface for implementers being notified by changes to weather data state in the model.
*/
public interface WeatherObserver {
  /**
  * Method to indicate that a change in the weather data state has occurred.
  */
    void updateWeatherObserver();
}
