package com.spring_class.dscommerce.dto.errors;

import java.time.Instant;
import java.util.List;

public record ValidationErrorResponse(Instant timestamp, Integer status, List<FieldError> errors, String path) {

}
