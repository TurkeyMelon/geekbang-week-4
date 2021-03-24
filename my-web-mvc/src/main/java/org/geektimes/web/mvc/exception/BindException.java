package org.geektimes.web.mvc.exception;

import java.util.Map;

/**
 * @author Geoffrey
 */
public class BindException extends RuntimeException {

    private final Map<String, String> errorMap;

    public BindException(Map<String, String> errorMap, String message) {
        super(message);
        this.errorMap = errorMap;
    }

    public Map<String, String> getErrorMap() {
        return errorMap;
    }
}
