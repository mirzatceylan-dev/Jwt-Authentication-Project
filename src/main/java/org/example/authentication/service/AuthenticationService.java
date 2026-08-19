package org.example.authentication.service;

import org.example.authentication.dto.RequestDto;
import org.example.authentication.dto.response.AuthenticationResponseDto;
import org.example.authentication.dto.response.GenericResponseDto;

public interface AuthenticationService {

    GenericResponseDto register(RequestDto requestDto);

    GenericResponseDto verifyAccount(RequestDto requestDto);

    GenericResponseDto validateToken(RequestDto requestDto);

    AuthenticationResponseDto login(RequestDto requestDto);
}