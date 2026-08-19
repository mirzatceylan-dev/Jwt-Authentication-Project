package org.example.authentication.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;


@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @Column(name = "id_")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_name", nullable = false)
    private String username;

    @Column(name = "password_", nullable = false)
    private String password;

    @Column(name = "aktifmi", nullable = false)
    private Boolean aktif;
}


//if (!StringUtils.hasText(requestDto.getUsername()) || StringUtils.isEmpty(requestDto.getPassword())  // Burak beyle konuştuğmuz validationlardan ekledim
//                || requestDto.getPassword().length() < 6 || !requestDto.getPassword().matches("\\d+")) {
//            return ResponseEntity.badRequest().body("Geçersiz kullanıcı adı veya şifre");
//
//