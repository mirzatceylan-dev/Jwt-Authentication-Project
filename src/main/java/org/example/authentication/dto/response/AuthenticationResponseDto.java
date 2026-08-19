package org.example.authentication.dto.response;

import lombok.Data;
                                                // Tight coupling!!!
@Data
public class AuthenticationResponseDto extends GenericResponseDto {
    private String jwt;
}
