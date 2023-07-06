package majorproject.test.model;

import majorproject.model.pojo.About;

import java.util.List;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
* A class to test the About objects can be constructed correctly, with their
* data retrievable.
*/
class AboutTest {

    /**
    * Tests that About can be constructed and retreived correctly.
    */
    @Test void testConstructionAbout(){
      List<String> references = new ArrayList<String>();
      references.add("Example 1");
      references.add("exaple 2 www.website.com.au");
      About aboutOne = new About("appName","creatorName",references);
      assertNotNull(aboutOne);
      assert(aboutOne.getApplicationName().equals("appName"));
      assert(aboutOne.getCreatorName().equals("creatorName"));
      assert(aboutOne.getReferences().size()==2);
      assert(aboutOne.getReferences().get(0).equals("Example 1"));
      assert(aboutOne.getReferences().get(1).equals("exaple 2 www.website.com.au"));
    }

}
