package majorproject.model.observers;

/**
* Observer pattern interface for implementers being notified by cache hits
*/
public interface CacheHitObserver {
    /**
    * Method to indicate that a cache hit has occurred.
    */
    void cacheHit();
}
