package ks.glowlyapp.address;

import ks.glowlyapp.address.dto.AddressDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/address")
public class AddressController {

    AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }


    @GetMapping("/business/{id}")
    public ResponseEntity<AddressDto> getAddressByBusinessId(@PathVariable long id){
        return addressService.findAddressByBusinessId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/city/{city}")
    public ResponseEntity<AddressDto> getAddressByCity(@PathVariable String city){
        return addressService.findAddressByCity(city)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<String> createNewAddress(@RequestBody AddressDto addressDto){
        addressService.createNewAddress(addressDto);
        return ResponseEntity.ok("Address created successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateAddressDetails(@RequestBody AddressDto addressDto,
                                                       @PathVariable("id") long addressId) {
        addressService.updateAddress(addressDto, addressId);
        return ResponseEntity.ok("Address updated successfully");
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAddress(@PathVariable long id){
        addressService.deleteAddress(id);
        return ResponseEntity.ok("Address deleted successfully");
    }

}
