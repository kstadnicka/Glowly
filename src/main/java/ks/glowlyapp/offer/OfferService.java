package ks.glowlyapp.offer;

import ks.glowlyapp.business.Business;
import ks.glowlyapp.business.BusinessRepository;
import ks.glowlyapp.offer.dto.OfferDto;
import ks.glowlyapp.offer.dto.OfferShortDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class OfferService {
    private final OfferRepository offerRepository;
    private final BusinessRepository businessRepository;
    private final OfferDtoMapper offerDtoMapper;

    public OfferService(OfferRepository offerRepository, BusinessRepository businessRepository, OfferDtoMapper offerDtoMapper) {
        this.offerRepository = offerRepository;
        this.businessRepository = businessRepository;
        this.offerDtoMapper = offerDtoMapper;
    }

    public List<OfferDto> getAllOffers(){
        return offerRepository.findAll()
                .stream()
                .map(offerDtoMapper::map)
                .toList();
    }

    public Optional<OfferDto> getOfferByName(String name){
        Objects.requireNonNull(name,"Name cannot be null");
        return  offerRepository.findOfferByName(name)
                .map(offerDtoMapper::map);
    }

    public Optional<OfferDto> getOfferByPrice(double price) {
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
        return offerRepository.findOfferByPrice(price)
                .map(offerDtoMapper::map);
    }

    public Optional<OfferDto> getOfferById(long id){
        return offerRepository.findById(id)
                .map(offerDtoMapper::map);
    }

    @Transactional
    public void createOfferForBusiness(OfferShortDto offerShortDto, Long businessId) {
        if (offerShortDto == null) {
            throw new IllegalArgumentException("Offer data cannot be null");
        }
        if (offerShortDto.getName() == null || offerShortDto.getName().isBlank()) {
            throw new IllegalArgumentException("Offer name cannot be empty");
        }
        if (businessId == null) {
            throw new IllegalArgumentException("Business ID cannot be null");
        }
        Business business = businessRepository.findById(businessId)
                .orElseThrow(() -> new RuntimeException("Business not found"));

        Offer offer = new Offer();
        offer.setName(offerShortDto.getName());
        offer.setBusiness(business);
        offerRepository.save(offer);
        business.getOffersList().add(offer);
    }

    @Transactional
    public void updateOfferDetails(OfferDto offerDto, long id){
        Offer offerToUpdate = offerRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Offer not found"));
        if (offerDto == null) {
            throw new IllegalArgumentException("Offer data cannot be null");
        }
        if (offerDto.getName() == null || offerDto.getName().isBlank()) {
            throw new IllegalArgumentException("Offer name cannot be empty");
        }
        if (offerDto.getPrice() < 0) {
            throw new IllegalArgumentException("Offer price cannot be negative");
        }
        offerToUpdate.setName(offerDto.getName());
        offerToUpdate.setDescription(offerDto.getDescription());
        offerToUpdate.setPrice(offerDto.getPrice());
        offerToUpdate.setDuration(offerDto.getDuration());
    }

    @Transactional
    public void deleteOffer(long id) {
        if (!offerRepository.existsById(id)) {
            throw new RuntimeException("Offer not found");
        }
        offerRepository.deleteById(id);
    }
}
