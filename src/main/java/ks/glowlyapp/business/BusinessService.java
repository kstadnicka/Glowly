package ks.glowlyapp.business;

import ks.glowlyapp.business.dto.BusinessDto;
import ks.glowlyapp.offer.Offer;
import ks.glowlyapp.offer.OfferRepository;
import ks.glowlyapp.offer.dto.OfferShortDto;
import ks.glowlyapp.owner.Owner;
import ks.glowlyapp.owner.OwnerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class BusinessService {
    private final BusinessRepository businessRepository;
    private final OwnerRepository ownerRepository;
    private final OfferRepository offerRepository;
    private final BusinessDtoMapper businessDtoMapper;


    public BusinessService(BusinessRepository businessRepository, OwnerRepository ownerRepository, OfferRepository offerRepository, BusinessDtoMapper businessDtoMapper) {
        this.businessRepository = businessRepository;
        this.ownerRepository = ownerRepository;
        this.offerRepository = offerRepository;
        this.businessDtoMapper = businessDtoMapper;
    }

    public Optional<BusinessDto> findBusinessByName(String name){
        Objects.requireNonNull(name,"Name cannot be null");
        return businessRepository.findBusinessesByName(name)
                .map(businessDtoMapper::map);
    }

    public Optional<BusinessDto> findBusinessById(Long id){
        return businessRepository.findById(id)
                .map(businessDtoMapper::map);
    }

    @Transactional
    public void addBusinessToOwner(BusinessDto businessDto, Long ownerId){
        Owner owner = ownerRepository.findById(ownerId)
                .orElseThrow(()->new RuntimeException("Owner not found"));

        Business business = new Business();
        business.setName(businessDto.getName());
        business.setAddress(businessDto.getAddress());
        business.setOwner(owner);
        businessRepository.save(business);
        owner.getBusinessList().add(business);

    }

    @Transactional
    public void deleteBusiness(long id){
        businessRepository.deleteById(id);
    }

    @Transactional
    public void updateBusinessDetails(BusinessDto businessDto, long id){
        Business businessToUpdate = businessRepository.findById(id)
                .orElseThrow(()->new RuntimeException("Business not found."));

        businessToUpdate.setName(businessDto.getName());
        businessToUpdate.setAddress(businessDto.getAddress());

        List<Offer> updateOffersList = Optional.ofNullable(businessDto.getOffersList())
                .orElse(List.of())
                .stream()
                .map(OfferShortDto::getId)
                .map(offerRepository::findById)
                .flatMap(Optional::stream)
                .toList();

        businessToUpdate.setOffersList(updateOffersList);
    }
}
