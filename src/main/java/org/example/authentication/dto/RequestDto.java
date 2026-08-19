package org.example.authentication.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

// requestdto olucak bu clas
                                        // gidenler için ayrı bir dto açılıcak  bu da response dto olucak
                                        // her dtoclassında error kdolarım olmalı
@Data
@Getter
@Setter                             // @Data geniş kapsamlı bir anatasyon bu unutma birçok anatsayonuda içerir
public class RequestDto {


    private String username;
    private String password;
    private String email;
    private String token;

}
