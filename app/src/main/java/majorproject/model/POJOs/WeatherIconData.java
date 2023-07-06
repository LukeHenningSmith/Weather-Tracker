package majorproject.model.pojo;

/**
* Simple POJO class to store weather image data.
*/
public class WeatherIconData {

  private final String icon;
  private final int code;
  private final String description;

  /**
  * Simple constructor to set WeatherIconData variables.
  * @param icon The String icon.
  * @param code The int code description associated with this icon.
  * @param description The description of what this icon depicts.
  */
  public WeatherIconData(String icon, int code, String description){
    this.icon=icon;
    this.code=code;
    this.description=description;
  }

  /**
  * Simple getter method for icon variable.
  * @return The String value for icon.
  */
  public String getIcon(){
    return this.icon;
  }

  /**
  * Simple getter method for code variable.
  * @return The int value for code.
  */
  public int getCode(){
    return this.code;
  }

  /**
  * Simple getter method for description variable.
  * @return The String value for description.
  */
  public String getDescription(){
    return this.description;
  }

}
