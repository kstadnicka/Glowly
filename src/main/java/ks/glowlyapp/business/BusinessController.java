package ks.glowlyapp.business;

import ks.glowlyapp.business.dto.BusinessDto;
import ks.glowlyapp.owner.dto.OwnerRegistrationDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@RestController
@RequestMapping("/api/business")
public class BusinessController {
    BusinessService businessService;

    @GetMapping("/{name}")
    public ResponseEntity<BusinessDto> getBusinessByName(@PathVariable String name){
       return businessService.findBusinessByName(name)
               .map(ResponseEntity::ok)
               .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BusinessDto> findBusinessById(@PathVariable Long id){
        return businessService.findBusinessById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<String> newBusiness(@RequestBody BusinessDto businessDto,Long ownerId){
        businessService.addBusinessToOwner(businessDto,ownerId);
        return ResponseEntity.ok("Business created successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateBusiness(@RequestParam Long id,@RequestBody BusinessDto businessDto){
        businessService.updateBusinessDetails(businessDto,id);
        return ResponseEntity.ok("Business updated successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBusiness(@RequestParam Long id){
        businessService.deleteBusiness(id);
        return ResponseEntity.ok("Business deleted successfully");
    }
}
