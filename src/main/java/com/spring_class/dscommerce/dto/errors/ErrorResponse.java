package com.spring_class.dscommerce.dto.errors;

import java.time.Instant;

public record ErrorResponse(Instant timeStamp, Integer status, String error, String path) {

}
