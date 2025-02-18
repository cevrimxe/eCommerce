package com.example.eCommerce.service;

import com.example.eCommerce.exception.UserAlreadyExistsException;
import com.example.eCommerce.exception.UserNotFoundException;
import com.example.eCommerce.exception.InvalidCredentialsException;
import com.example.eCommerce.model.User;
import com.example.eCommerce.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Yeni bir kullanıcı kaydeder.
     *
     * @param user Kaydedilecek kullanıcı bilgileri.
     * @return Kaydedilen kullanıcı.
     * @throws UserAlreadyExistsException E-posta adresi zaten kayıtlıysa fırlatılır.
     */
    public User registerUser(User user) {
        logger.info("Yeni kullanıcı kaydı denemesi: {}", user.getEmail());

        // E-posta adresi zaten kayıtlı mı kontrol et
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            logger.warn("E-posta adresi zaten kayıtlı: {}", user.getEmail());
            throw new UserAlreadyExistsException("Bu e-posta adresi zaten kayıtlı!");
        }

        // Şifreyi hash'le
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Kullanıcıyı veritabanına kaydet
        User savedUser = userRepository.save(user);
        logger.info("Kullanıcı başarıyla kaydedildi: {}", savedUser.getEmail());

        return savedUser;
    }

    /**
     * Kullanıcı girişi yapar.
     *
     * @param email    Kullanıcının e-posta adresi.
     * @param password Kullanıcının şifresi.
     * @return Giriş yapan kullanıcı.
     * @throws UserNotFoundException     Kullanıcı bulunamazsa fırlatılır.
     * @throws InvalidCredentialsException Şifre yanlışsa fırlatılır.
     */
    public User loginUser(String email, String password) {
        logger.info("Kullanıcı girişi denemesi: {}", email);

        // Kullanıcıyı e-posta adresine göre bul
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    logger.warn("Kullanıcı bulunamadı: {}", email);
                    return new UserNotFoundException("Kullanıcı bulunamadı!");
                });

        // Şifreleri karşılaştır
        if (!passwordEncoder.matches(password, user.getPassword())) {
            logger.warn("Geçersiz şifre denemesi: {}", email);
            throw new InvalidCredentialsException("Geçersiz şifre!");
        }

        logger.info("Kullanıcı başarıyla giriş yaptı: {}", email);
        return user;
    }
}