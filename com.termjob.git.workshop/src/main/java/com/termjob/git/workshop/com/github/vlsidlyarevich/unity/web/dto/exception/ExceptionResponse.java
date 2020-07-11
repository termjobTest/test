package com.github.vlsidlyarevich.unity.web.dto.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public final class ExceptionResponse implements Serializable {
    private static final long serialVersionUID = -7579152361028856318L;

    private String message;
}
