package com.github.vlsidlyarevich.unity.web.exception.handler;

import com.github.vlsidlyarevich.unity.twitter.exception.TwitterServiceException;
import com.github.vlsidlyarevich.unity.web.dto.exception.ExceptionResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class TwitterExceptionHandler {

    @ExceptionHandler(TwitterServiceException.class)
    public ResponseEntity handleTwitterServiceException(
            final TwitterServiceException exception,
            final HttpServletRequest req) {
        log.warn("Processing twitter service exception: {}", exception.getException().getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ExceptionResponse(exception.getLocalizedMessage()));
    }
}
