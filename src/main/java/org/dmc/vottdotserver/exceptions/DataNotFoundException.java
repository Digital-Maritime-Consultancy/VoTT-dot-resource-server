package org.dmc.vottdotserver.exceptions;

import org.dmc.vottdotserver.models.JsonSerializable;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

/**
 * The Data Not Found Exception.
 *
 * A generic runtime exception to be thrown whenever some necessary data is not
 * detected.
 *
 * @author Nikolaos Vastardis (email: Nikolaos.Vastardis@gla-rad.org)
 */
public class DataNotFoundException  extends RuntimeException implements Serializable, JsonSerializable {

    private static final long serialVersionUID = -7568214025054434706L;

    public static HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public static void setHttpStatus(HttpStatus httpStatus) {
        DataNotFoundException.httpStatus = httpStatus;
    }

    /**
     * The Exception HTTP Status Code.
     */
    private static HttpStatus httpStatus = HttpStatus.NOT_FOUND;

    /**
     * Instantiates an empty Data Not Found exception.
     */
    public DataNotFoundException() {
        super("The requested data was not found", null);
    }

    /**
     * Instantiates a new Data Not Found exception.
     *
     * @param message the message
     * @param t       the throwable
     */
    public DataNotFoundException(String message, Throwable t) {
        super(message, t);
    }

}
