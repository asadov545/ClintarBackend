package org.artisoft.api.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseUtil {

    public static ResponseEntity<Response<Boolean>> getResponseResponseEntity(boolean delete) {
        try {
            Boolean isDeleted = delete;
            Response<Boolean> response = new Response<>();
            HttpStatus httpStatus = !isDeleted ? HttpStatus.NOT_MODIFIED : HttpStatus.OK;

            return new ResponseEntity<>(response.generateResponse(httpStatus, isDeleted), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new Response<Boolean>().generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, false), HttpStatus.OK);
        }
    }
}
