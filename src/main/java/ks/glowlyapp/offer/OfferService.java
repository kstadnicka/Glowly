package ks.glowlyapp.offer;

import ks.glowlyapp.business.Business;
import ks.glowlyapp.business.BusinessRepository;
import ks.glowlyapp.offer.dto.OfferShortDto;
import ks.glowlyapp.owner.OwnerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OfferService {
    private final OfferRepository offerRepository;
    private final BusinessRepository businessRepository;
    private final OwnerRepository ownerRepository;

    public OfferService(OfferRepository offerRepository, BusinessRepository businessRepository, OwnerRepository ownerRepository) {
        this.offerRepository = offerRepository;
        this.businessRepository = businessRepository;
        this.ownerRepository = ownerRepository;
    }

    @Transactional
    public void addOfferToBusiness(OfferShortDto offerShortDto, Long businessId){
        Business business = businessRepository.findById(businessId)
                .orElseThrow(()->new RuntimeException("Business not found"));

        Offer offer = new Offer();
        offer.setName(offerShortDto.getName());
        offer.setBusiness(business);
        offerRepository.save(offer);
        business.getOffersList().add(offer);
    }
}
