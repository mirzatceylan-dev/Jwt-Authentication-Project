package org.example.authentication.Exception;

import lombok.Getter;

@Getter
public class AuthenticationException extends RuntimeException{    // Exception sınfından miras almadık çünkü o try-catch e mecbur bırakıyor
                                                                //Biz ise unchecked exception bir yapı kullandık
    private String errorCode;

    public AuthenticationException(String errorCode) {
        this.errorCode = errorCode;
    }
}
