package dph.com.filmplus.PlayerMessages;

/**
 * This generic interface for messages
 */
public interface Message {
    void runMessage();
    void polledFromQueue();
    void messageFinished();
}
