package io.github.lancelothuxi.mock.agent.polling;

/**
 * @author Jason Song(song_s@ctrip.com)
 */
public class LongPollingStatusCodeException extends RuntimeException {
    private final int m_statusCode;

    public LongPollingStatusCodeException(int statusCode, String message) {
        super(String.format("[status code: %d] %s", statusCode, message));
        this.m_statusCode = statusCode;
    }

    public LongPollingStatusCodeException(int statusCode, Throwable cause) {
        super(cause);
        this.m_statusCode = statusCode;
    }

    public int getStatusCode() {
        return m_statusCode;
    }
}
