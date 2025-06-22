package com.niranzan.photoapp.user.ws.exceptions.decoder;

import com.niranzan.photoapp.user.ws.exceptions.GenericException;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
@RequiredArgsConstructor
public class FeignErrorDecoder implements ErrorDecoder {
    private final Environment environment;

    @Override
    public Exception decode(String methodKey, Response response) {
        switch (response.status()) {
            case 404:
                if (methodKey.contains("getAlbums")) {
                    return new ResponseStatusException(HttpStatus.valueOf(response.status()), environment.getProperty("albums.exceptions.not-found"));
                }
                break;
            case 401:
                return new AccessDeniedException("AccessDeniedException: " + response.body().toString());
            default:
                return new GenericException("GenericException : " + response.body().toString());
        }
        return new GenericException("GenericException : " + response.body().toString());
    }
}
