package ks.glowlyapp.owner;

import ks.glowlyapp.owner.dto.OwnerRegistrationDto;
import ks.glowlyapp.owner.dto.OwnerResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/owners")
public class OwnerController {

    private final OwnerService ownerService;

    public OwnerController(OwnerService ownerService) {
        this.ownerService = ownerService;
    }

    @GetMapping
    public List<OwnerResponseDto> getAllOwners(){
        return ownerService.getAllOwners();
    }

    @GetMapping("/{id}")
    public ResponseEntity<OwnerResponseDto> getOwnerById(@PathVariable long id){
        return ownerService.getOwnerById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<String> newOwner(@RequestBody OwnerRegistrationDto ownerRegistrationDto){
        ownerService.createNewOwner(ownerRegistrationDto);
        return ResponseEntity.ok("Owner created successfully");
    }


    @PutMapping("/{id}")
    public ResponseEntity<String> updateOwner(@PathVariable long id, @RequestBody OwnerResponseDto ownerResponseDto) {
        ownerService.updateOwnerDetails(ownerResponseDto, id);
        return ResponseEntity.ok("Owner updated successfully");
    }


    @DeleteMapping("/{id}")
   public ResponseEntity<String> deleteOwner(@PathVariable long id){
        ownerService.deleteOwner(id);
        return ResponseEntity.ok("Owner deleted successfully");
    }

}
