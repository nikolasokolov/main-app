package com.graduation.mainapp.service.impl;

import com.graduation.mainapp.model.Company;
import com.graduation.mainapp.model.Restaurant;
import com.graduation.mainapp.repository.CompanyRepository;
import com.graduation.mainapp.service.CompanyService;
import com.graduation.mainapp.service.RestaurantService;
import com.graduation.mainapp.web.dto.CompanyDTO;
import com.graduation.mainapp.web.dto.RestaurantDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.inject.Inject;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.graduation.mainapp.util.LogoValidationUtil.validateLogoFormat;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class CompanyServiceImpl implements CompanyService {
    private final CompanyRepository companyRepository;
    private final RestaurantService restaurantService;

    @Override
    public List<Company> findAll() {
        return companyRepository.findAll();
    }

    @Override
    public Company save(Company company) {
        return companyRepository.save(company);
    }

    @Override
    public Optional<Company> findById(Long companyId) {
        return companyRepository.findById(companyId);
    }

    @Override
    public Company saveLogo(Long companyId, MultipartFile logo) throws Exception {
        if (!logo.isEmpty()) {
            Optional<Company> companyOptional = this.findById(companyId);
            if (companyOptional.isPresent()) {
                Company company = companyOptional.get();
                try {
                    company.setLogo(logo.getBytes());
                } catch (IOException e) {
                    log.error("IOException caught on saveLogo company:  " + company.getName() + "message" + e.getMessage());
                } catch (Exception exception) {
                    log.error("Error while trying to save logo for company with ID [{}]", companyId);
                }
                validateLogoFormat(logo);
                return this.save(company);
            } else {
                log.warn("Company with ID [{}] is not found", companyId);
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found");
            }
        } else {
            log.error("Logo not present");
            throw new Exception("Logo not present");
        }
    }

    @Override
    public boolean delete(Long companyId) {
        Optional<Company> optionalCompany = this.findById(companyId);
        if (optionalCompany.isPresent()) {
            Company company = optionalCompany.get();
            companyRepository.delete(company);
            return true;
        } else {
            log.error("Company with ID [{}] is not found", companyId);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found");
        }
    }

    @Override
    public List<CompanyDTO> createCompanyDTOs(Collection<Company> companies) {
        return companies.stream().map(company -> {
            byte[] companyLogo = company.getLogo();
            return new CompanyDTO(
                    company.getId(),
                    company.getName(),
                    company.getAddress(),
                    company.getAddress(),
                    companyLogo);
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public boolean addRestaurantForCompany(CompanyDTO companyDTO, Long restaurantId) {
        Optional<Company> optionalCompany = this.findById(companyDTO.getId());
        if (optionalCompany.isPresent()) {
            Company company = optionalCompany.get();
            Optional<Restaurant> optionalRestaurant = restaurantService.findById(restaurantId);
            if (optionalRestaurant.isPresent()) {
                Restaurant restaurant = optionalRestaurant.get();
                company.getRestaurants().add(restaurant);
                restaurant.getCompanies().add(company);
                companyRepository.save(company);
                restaurantService.save(restaurant);
                return true;
            } else {
                log.warn("Restaurant with ID [{}] is not found", restaurantId);
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Restaurant not found");
            }
        } else {
            log.warn("Company with ID [{}] is not found", companyDTO.getId());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found");
        }
    }

    @Override
    public Company createCompanyObjectFromCompanyDTO(CompanyDTO companyDTO) {
        return Company.builder()
                .name(companyDTO.getName())
                .address(companyDTO.getAddress())
                .phoneNumber(companyDTO.getPhoneNumber())
                .build();
    }

    @Override
    public CompanyDTO createCompanyDTOFromCompanyObject(Company company) {
        return CompanyDTO.builder()
                .id(company.getId())
                .name(company.getName())
                .address(company.getAddress())
                .phoneNumber(company.getPhoneNumber())
                .logo(company.getLogo())
                .build();
    }

    @Override
    public Company createCompanyObjectForUpdate(Company company, CompanyDTO companyDTO) {
        return Company.builder()
                .id(companyDTO.getId())
                .name(companyDTO.getName())
                .address(companyDTO.getAddress())
                .phoneNumber(companyDTO.getPhoneNumber())
                .logo(company.getLogo())
                .restaurants(company.getRestaurants())
                .build();
    }

    @Override
    @Transactional
    public boolean deleteRestaurantForCompany(Long companyId, Long restaurantId) {
        Optional<Company> optionalCompany = this.findById(companyId);
        if (optionalCompany.isPresent()) {
            Company company = optionalCompany.get();
            Optional<Restaurant> optionalRestaurant = restaurantService.findById(restaurantId);
            if (optionalRestaurant.isPresent()) {
                Restaurant restaurant = optionalRestaurant.get();
                restaurant.removeCompany(company);
                company.removeRestaurant(restaurant);
                this.save(company);
                restaurantService.save(restaurant);
                return true;
            } else {
                log.error("Restaurant with ID [{}] is not found", restaurantId);
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Restaurant not found");
            }
        } else {
            log.error("Company with ID [{}] is not found", companyId);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found");
        }
    }

    @Override
    @Transactional
    public List<RestaurantDTO> getRestaurantsForCompany(Long companyId) {
        Optional<Company> optionalCompany = this.findById(companyId);
        if (optionalCompany.isPresent()) {
            Company company = optionalCompany.get();
            Set<Restaurant> restaurantsForCompany = company.getRestaurants();
            return restaurantService.createRestaurantDTOs(restaurantsForCompany);
        } else {
            log.info("Company with ID [{}] is not found", companyId);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found");
        }
    }

    @Override
    public Company updateCompany(CompanyDTO companyDTO) {
        Optional<Company> companyFromDatabase = this.findById(companyDTO.getId());
        if (companyFromDatabase.isPresent()) {
            Company company = companyFromDatabase.get();
            Company companyToBeUpdated = this.createCompanyObjectForUpdate(company, companyDTO);
            return this.save(companyToBeUpdated);
        } else {
            log.error("Company with ID [{}] is not found", companyDTO.getId());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found");
        }
    }
}
