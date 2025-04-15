package ks.glowlyapp.business;

import ks.glowlyapp.business.dto.BusinessDto;
import ks.glowlyapp.owner.Owner;
import ks.glowlyapp.owner.OwnerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BusinessService {
    private final BusinessRepository businessRepository;
    private final OwnerRepository ownerRepository;


    public BusinessService(BusinessRepository businessRepository, OwnerRepository ownerRepository) {
        this.businessRepository = businessRepository;
        this.ownerRepository = ownerRepository;
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
}
