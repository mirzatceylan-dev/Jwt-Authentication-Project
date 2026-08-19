package org.example.authentication.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.micrometer.common.util.StringUtils;
import lombok.extern.log4j.Log4j2;
import org.example.authentication.Exception.AuthenticationException;
import org.example.authentication.dto.RequestDto;
import org.example.authentication.dto.response.AuthenticationResponseDto;
import org.example.authentication.dto.response.GenericResponseDto;
import org.example.authentication.model.User;
import org.example.authentication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Optional;

@Log4j2
@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EmailSenderService emailSenderService;

    private long EXPIRATION_TIME = 1000 * 60 * 2;

    @Value("${jwt.secret}")
    private String secretKey;

    private Key getSignKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }

    GenericResponseDto yanit = new GenericResponseDto();

    @Override
    public GenericResponseDto register(RequestDto requestDto) {

        GenericResponseDto response = new GenericResponseDto();

        log.info("Yeni kullanici kaydi baslatildi. Kullanici Adi: {}", requestDto.getUsername());
        log.debug("Veritabanina findByUsername sorgusu atiliyor Gelen isim: {}", requestDto.getUsername());
        Optional<User> mevcutKullanici = userRepository.findByUsername(requestDto.getUsername());

        if (StringUtils.isBlank(requestDto.getUsername())
                || StringUtils.isEmpty(requestDto.getUsername())
                || StringUtils.isEmpty(requestDto.getPassword())  // Burak beyle konuştuğmuz validationlardan ekledim
                || requestDto.getPassword().length() < 6
                || !requestDto.getPassword().matches("\\d+")) {
            throw new AuthenticationException("ERROR-503");
        }
        if (mevcutKullanici.isPresent()) {
            log.error("Kayit reddedildi! Kullanici adi zaten mevcut: {}", requestDto.getUsername());
            throw new AuthenticationException("ERROR-507");
            //response.hatali("ERROR-507", "Bu kullanici adi zaten alinmis!");
            //return response;
        }

        User user = new User();
        user.setUsername(requestDto.getUsername());
        user.setPassword(requestDto.getPassword());
        user.setAktif(false);
        userRepository.save(user);

        // Mail ve link işlemleri
        String girisLink = "http://localhost:9090/v1/request/verify?username=" + user.getUsername();
        String mailKonusu = "Sistemimize Hoş Geldiniz!";
        String mailIcerigi = "Merhaba " + requestDto.getUsername() + "\n Kullanici kaydiniz yapildi\n\n Linke tiklayarak ilerleyiniz : " + girisLink;

        emailSenderService.sendEmail(requestDto.getEmail(), mailKonusu, mailIcerigi);

        log.info("{} kullanicisi basariyla kaydedildi ve dogrulama maili gönderildi.", requestDto.getUsername());
        response.basarili("Yeni kullanici olustu, gelen linke tiklayarak hesabinizi aktiflestirin");
        return response;
    }

    public String generateToken(RequestDto requestDto) {
        String token = Jwts.builder()
                .claim("username", requestDto.getUsername())
                .claim("password", requestDto.getPassword())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();

        return token;
    }

    @Override
    public GenericResponseDto verifyAccount(RequestDto requestDto) {

        // Hem tipi Generic, hem de üretilen gerçek obje Generic olmalı
        GenericResponseDto response = new GenericResponseDto();
        Optional<User> optionalUser = userRepository.findByUsername(requestDto.getUsername());

        if (optionalUser.isEmpty()) {
            log.warn("Hatali link ile verify denendi: {}", requestDto.getUsername());

            throw new AuthenticationException("ERROR-504");
            //response.hatali("ERROR-504", "Geçersiz link!");
            //return response;
        }

        User user = optionalUser.get();

        if (user.getAktif()) {
            response.basarili("Hesabiniz zaten aktif, giris yapabilirsiniz.");
            return response;
        }

        user.setAktif(true);
        userRepository.save(user);
        log.info("Giris yapildi ve hesap aktif edildi: {}", requestDto.getUsername());

        response.basarili("Giris yapildi ve hesap aktif edildi");
        return response;
    }

    @Override
    public AuthenticationResponseDto login(RequestDto requestDto) {
        log.info("Giris denemesi yapiliyor: {}", requestDto.getUsername());
        AuthenticationResponseDto response = new AuthenticationResponseDto();

        Optional<User> optionalUser = userRepository.findByUsername(requestDto.getUsername());

        if (optionalUser.isEmpty()) {
            log.warn("Sistemde olmayan bir kullanici ile giris yapilmaya calisildi: {}", requestDto.getUsername());
            throw new AuthenticationException("ERROR-500");
            //response.hatali("ERROR-500", "Böyle bir kullanıcı bulunamadı.")
            //return response;
            // statik olmadığı mesajı doldurup setleyerek anlamlı hale getirdik
        }


        User user = optionalUser.get();
        if (!user.getPassword().equals(requestDto.getPassword())) {
            log.info("Kullanici sifresini yanlis girdi: {}", requestDto.getUsername());
            throw new AuthenticationException("ERROR-501");
            //response.hatali("ERROR-501", "Sifrenizi yanlis girdiniz.");
            //return response;
        }

        if (!user.getAktif()) {
            log.warn("Kullanici hesabi onaylanmadan giris denedi: {}", requestDto.getUsername());
            throw new AuthenticationException("ERROR-502");
            //response.hatali("ERROR-502", "Hesabiniz onaylanmamis. Lütfen mailinizi kontrol edin.");
            //return response;
        }
        // statik olmayan basarili methodu böyle kullanıllır
        response.basarili("Giris başarılı");
        String token = generateToken(requestDto);
        response.setJwt(token);

        return response;                                    // ctrl + shift + R ile toplu nesne adıdeğişimi ctrl + r sadece bu classı değişir

    }

    public GenericResponseDto validateToken(RequestDto requestDto) {
        GenericResponseDto response = new GenericResponseDto();
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSignKey())
                    .build()
                    .parseClaimsJws(requestDto.getToken());
            response.basarili("İşlem başarılı token hala geçerli");
            return response;
        } catch (Exception e) {

            throw new AuthenticationException("TOKEN-505");
            //response.hatali("TOKEN-505", "Oturumunuzz kapandı lütfen tekrar giriş yapınız");
            //return response;
        }
    }
}