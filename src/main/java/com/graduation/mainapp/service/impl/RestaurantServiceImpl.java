package com.graduation.mainapp.service.impl;

import com.graduation.mainapp.model.Company;
import com.graduation.mainapp.model.Restaurant;
import com.graduation.mainapp.repository.CompanyRepository;
import com.graduation.mainapp.repository.RestaurantRepository;
import com.graduation.mainapp.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.inject.Inject;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class RestaurantServiceImpl implements RestaurantService {
    private final RestaurantRepository restaurantRepository;

    @Override
    public List<Restaurant> findAll() {
        return restaurantRepository.findAll();
    }

    @Override
    public Restaurant save(Restaurant restaurant) {
        return restaurantRepository.save(restaurant);
    }

    @Override
    public Optional<Restaurant> findById(Long companyId) {
        return restaurantRepository.findById(companyId);
    }

    @Override
    public void saveLogo(Restaurant restaurant, MultipartFile logo) throws Exception {
        try {
            restaurant.setLogo(logo.getBytes());
        } catch (IOException e) {
            log.error("IOException caught on saveLogo company:  " + restaurant.getName() + "message" + e.getMessage());
        }
        String fileName = logo.getOriginalFilename();
        int dotIndex = Objects.requireNonNull(fileName).lastIndexOf('.');
        String extension = fileName.substring(dotIndex + 1);
        if (!extension.equals("jpg") && !extension.equals("jpeg") && !extension.equals("png")) {
            throw new Exception("Invalid image format");
        }
        save(restaurant);
    }

    @Override
    public void delete(Restaurant restaurant) {
        // restaurant.getCompanies().forEach(restaurant::deleteCompany);
        restaurantRepository.delete(restaurant);
    }
}
