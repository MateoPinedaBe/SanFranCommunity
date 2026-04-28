package com.sanfran.community.applications.appservice;

import com.sanfran.community.domain.usecase.FacilityUseCase;
import com.sanfran.community.domain.usecase.ReservationUseCase;
import com.sanfran.community.domain.usecase.UserUseCase;
import com.sanfran.community.domain.usecase.port.FacilityRepository;
import com.sanfran.community.domain.usecase.port.ReservationRepository;
import com.sanfran.community.domain.usecase.port.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class UseCaseConfig {

    @Bean
    public UserUseCase userUseCase(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return new UserUseCase(userRepository, passwordEncoder);
    }

    @Bean
    public FacilityUseCase facilityUseCase(FacilityRepository facilityRepository) {
        return new FacilityUseCase(facilityRepository);
    }

    @Bean
    public ReservationUseCase reservationUseCase(
            ReservationRepository reservationRepository,
            UserRepository userRepository,
            FacilityRepository facilityRepository
    ) {
        return new ReservationUseCase(reservationRepository, userRepository, facilityRepository);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
