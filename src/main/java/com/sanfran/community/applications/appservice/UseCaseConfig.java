package com.sanfran.community.applications.appservice;

import com.sanfran.community.domain.usecase.FacilityUseCase;
import com.sanfran.community.domain.usecase.ReservationUseCase;
import com.sanfran.community.domain.usecase.UserUseCase;
import com.sanfran.community.domain.usecase.IUserUseCase;
import com.sanfran.community.domain.usecase.IFacilityUseCase;
import com.sanfran.community.domain.usecase.IReservationUseCase;
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
    public IUserUseCase userUseCase(UserRepository userRepository, PasswordEncoder passwordEncoder, ReservationRepository reservationRepository) {
        return new UserUseCase(userRepository, passwordEncoder, reservationRepository);
    }

    @Bean
    public IFacilityUseCase facilityUseCase(FacilityRepository facilityRepository, ReservationRepository reservationRepository) {
        return new FacilityUseCase(facilityRepository, reservationRepository);
    }

    @Bean
    public IReservationUseCase reservationUseCase(
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
