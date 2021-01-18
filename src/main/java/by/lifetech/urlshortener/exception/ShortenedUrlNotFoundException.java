package by.lifetech.urlshortener.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ShortenedUrlNotFoundException extends RuntimeException {

    public ShortenedUrlNotFoundException() {
    }

    public ShortenedUrlNotFoundException(String message) {
        super(message);
    }

    public ShortenedUrlNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ShortenedUrlNotFoundException(Throwable cause) {
        super(cause);
    }

    public ShortenedUrlNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
