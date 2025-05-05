package ks.glowlyapp.offer;

import ks.glowlyapp.offer.dto.OfferDto;
import ks.glowlyapp.offer.dto.OfferShortDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/offers")
public class OfferController {
    OfferService offerService;

    public OfferController(OfferService offerService) {
        this.offerService = offerService;
    }

    @GetMapping
    public List<OfferDto> getAllOffers(){
        return offerService.getAllOffers();
    }

    @GetMapping("/{name}")
    public ResponseEntity<OfferDto> getOfferByName(@PathVariable String name){
       return offerService.getOfferByName(name)
               .map(ResponseEntity::ok)
               .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/sort")
    public ResponseEntity<List<OfferDto>> sortOffersByPrice(@RequestParam(defaultValue = "true") boolean asc) {
        return ResponseEntity.ok(offerService.getOffersSortedByPrice(asc));
    }

    @PostMapping
    public ResponseEntity<String> createNewOffer(@RequestBody OfferShortDto offerShortDto, Long businessId){
        offerService.createOfferForBusiness(offerShortDto,businessId);
        return ResponseEntity.ok("Offer created successfully");
    }


    @PutMapping("/{id}")
    public ResponseEntity<String> updateOffer(@RequestParam Long id, @RequestBody OfferDto offerDto){
        offerService.updateOfferDetails(offerDto,id);
        return ResponseEntity.ok("Offer updated successfully");
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteOffer(@RequestParam Long id, @RequestBody OfferDto offerDto){
        offerService.deleteOffer(id);
        return ResponseEntity.ok("Offer deleted successfully");
    }

    }
