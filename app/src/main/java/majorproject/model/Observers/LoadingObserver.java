package majorproject.model.observers;

/**
* Observer pattern interface for implementers being notified by changes to 'loading' state in model.
*/
public interface LoadingObserver {
  /**
  * Method to indicate that a change in the loading state has occurred.
  */
    void updateLoadingObserver();
}
