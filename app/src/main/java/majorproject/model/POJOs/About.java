package majorproject.model.pojo;

import java.util.List;

/**
* Simple POJO class containing the 'About' information of the application.
*/
public class About {

  private final String applicationName;
  private final String creatorName;
  private final List<String> references;

  /**
  * Constructs About object by setting the parameters to same named variables.
  * @param applicationName The name of the application.
  * @param creatorName The name of the application creator.
  * @param references A list of String objects indicating the references used.
  */
  public About(String applicationName, String creatorName, List<String> references){
    this.applicationName=applicationName;
    this.creatorName=creatorName;
    this.references=references;
  }

  /**
  * Getter method for applicationName variable.
  * @return The String value for applicationName.
  */
  public String getApplicationName(){
    return this.applicationName;
  }

  /**
  * Getter method for creatorName variable.
  * @return The String value for creatorName.
  */
  public String getCreatorName(){
    return this.creatorName;
  }

  /**
  * Getter method for references variable.
  * @return The List of Strings value for references.
  */
  public List<String> getReferences(){
    return this.references;
  }

}
