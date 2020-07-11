package com.github.vlsidlyarevich.unity.web.exception.handler;

import com.github.vlsidlyarevich.unity.domain.exception.FileSystemFileNotFoundException;
import com.github.vlsidlyarevich.unity.domain.exception.FileSystemStorageException;
import com.github.vlsidlyarevich.unity.domain.exception.ResourceNotFoundException;
import com.github.vlsidlyarevich.unity.domain.exception.UserNotFoundException;
import com.github.vlsidlyarevich.unity.domain.exception.UsernameExistsException;
import com.github.vlsidlyarevich.unity.i18n.MessageResolver;
import com.github.vlsidlyarevich.unity.web.dto.exception.ExceptionResponse;
import com.mongodb.MongoException;
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
public class PersistenceExceptionHandler {

    private final MessageResolver messageResolver;

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity handleResourceNotFoundException(
            final ResourceNotFoundException exception,
            final HttpServletRequest req) {
        log.warn("Processing resource not found exception: {}", exception.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ExceptionResponse(exception.getLocalizedMessage()));
    }

    @ExceptionHandler(FileSystemStorageException.class)
    public ResponseEntity handleFileSystemStorageException(
            final FileSystemStorageException exception,
            final HttpServletRequest req) {
        log.warn("Processing file system storage exception: {}", exception.getMessage());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ExceptionResponse(messageResolver.getMessage(exception.getKey(), exception.getArgs())));
    }

    @ExceptionHandler(FileSystemFileNotFoundException.class)
    public ResponseEntity handlefileSystemFileNotFoundException(
            final FileSystemFileNotFoundException exception,
            final HttpServletRequest req) {
        log.warn("Processing file system file not found exception: {}", exception.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ExceptionResponse(messageResolver.getMessage(exception.getKey(), exception.getArgs())));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity handleUserNotFoundException(final UserNotFoundException exception,
                                                      final HttpServletRequest req) {
        log.warn("Processing user not found exception: {}", exception.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ExceptionResponse(exception.getLocalizedMessage()));
    }

    @ExceptionHandler(UsernameExistsException.class)
    public ResponseEntity handleUsernameExistsException(final UsernameExistsException exception,
                                                        final HttpServletRequest req) {
        log.warn("Processing user with such username exists exception: {}", exception.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ExceptionResponse(exception.getLocalizedMessage()));
    }

    @ExceptionHandler(MongoException.class)
    public ResponseEntity handleMongoException(final MongoException exception,
                                               final HttpServletRequest req) {
        log.warn("Processing mongo exception:{}", exception.getMessage());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ExceptionResponse(exception.getLocalizedMessage()));
    }
}
