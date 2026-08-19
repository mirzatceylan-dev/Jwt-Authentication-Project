package org.example.authentication.Exception;

import jakarta.mail.Message;
import org.example.authentication.dto.response.GenericResponseDto;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Locale;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private MessageSource exceptionMessages;

    public GlobalExceptionHandler(MessageSource exceptionMessages) {

        this.exceptionMessages = exceptionMessages;
    }

    // Authenbtication Exception fırlatıldığında bu metot devreye girecek
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<GenericResponseDto> newAuthenticationException(AuthenticationException authenticationException, Locale locale) {

        GenericResponseDto response = new GenericResponseDto();

        String errorMessages = exceptionMessages.getMessage(
                authenticationException.getErrorCode(),
                null,
                "Bilinmeyen hata",
                locale
        );
        // Generetavie ai bakılıcak
        // message sourse kütüphanesi mantığı anlaşlıcak ve not alınıcak
        response.hatali(authenticationException.getErrorCode(), errorMessages);
        return ResponseEntity.ok().body(response);
    }

}