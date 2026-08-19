package org.example.authentication.controller;

import org.example.authentication.dto.RequestDto;
import org.example.authentication.dto.response.AuthenticationResponseDto;
import org.example.authentication.dto.response.GenericResponseDto;
import org.example.authentication.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.base-path}")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<GenericResponseDto> register(@RequestBody RequestDto requestDto) {
        GenericResponseDto response = authenticationService.register(requestDto);
        if (!response.isSuccess()) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/verify")
    public ResponseEntity<GenericResponseDto> verifyAccount(@ModelAttribute RequestDto requestDto) {
        // GET isteklerinde obje doldurmak için @ModelAttribute kullanılır !! burda @RequestParam olmaz
        GenericResponseDto response = authenticationService.verifyAccount(requestDto);
        if (!response.isSuccess()) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponseDto> login(@RequestBody RequestDto requestDto) {
        AuthenticationResponseDto response = authenticationService.login(requestDto);

        if (!response.isSuccess()) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/test")
    public ResponseEntity<GenericResponseDto> testEt(@RequestHeader("Authorization") String islem) {

        RequestDto requestDto = new RequestDto();
        requestDto.setToken(islem);
        GenericResponseDto response = authenticationService.validateToken(requestDto);

        if (!response.isSuccess()) {
            return ResponseEntity.status(401).body(response);
        }
        return ResponseEntity.ok(response);
    }
}