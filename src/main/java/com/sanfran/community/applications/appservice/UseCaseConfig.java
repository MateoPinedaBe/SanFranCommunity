package com.sanfran.community.applications.appservice;

import com.sanfran.community.domain.usecase.FacilityUseCase;
import com.sanfran.community.domain.usecase.ReservationUseCase;
import com.sanfran.community.domain.usecase.UserUseCase;
import com.sanfran.community.domain.usecase.port.FacilityRepository;
import com.sanfran.community.domain.usecase.port.ReservationRepository;
import com.sanfran.community.domain.usecase.port.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    public UserUseCase userUseCase(UserRepository userRepository) {
        return new UserUseCase(userRepository);
    }

    @Bean
    public FacilityUseCase facilityUseCase(FacilityRepository facilityRepository) {
        return new FacilityUseCase(facilityRepository);
    }

    @Bean
    public ReservationUseCase reservationUseCase(ReservationRepository reservationRepository) {
        return new ReservationUseCase(reservationRepository);
    }
}
