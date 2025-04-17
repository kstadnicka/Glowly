package ks.glowlyapp.owner;

import ks.glowlyapp.owner.dto.OwnerRegistrationDto;
import ks.glowlyapp.owner.dto.OwnerResponseDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
public class OwnerController {
    public static final String NOTIFICATION_ATTRIBUTE = "notification";

    OwnerService ownerService;

    public OwnerController(OwnerService ownerService) {
        this.ownerService = ownerService;
    }

    @GetMapping("/owners")
    public String getAllOwners(){
        ownerService.getAllOwners();
        return "all-owners";
    }

    @GetMapping("owners/{id}")
    public String getOwnerById(@PathVariable long id, Model model){
        Optional<OwnerResponseDto> ownerById = ownerService.getOwnerById(id);
        ownerById.ifPresent(owner->model.addAttribute("owner", owner));
        return "owner";
    }

    @PostMapping("/owners/new")
    public String newOwner(OwnerRegistrationDto ownerRegistrationDto, RedirectAttributes redirectAttributes){
        ownerService.createNewOwner(ownerRegistrationDto);
        redirectAttributes.addFlashAttribute(NOTIFICATION_ATTRIBUTE,"Rejestracja udana!");
        return "redirect:/";
    }

    @GetMapping("/owners/new")
    public String newOwnerForm(Model model){
        OwnerRegistrationDto owner = new OwnerRegistrationDto();
        model.addAttribute("owner", owner);
        return "owner-form";
    }

    @PostMapping("/owners/update")
    public String updateOwner(@RequestParam long id, OwnerRegistrationDto ownerResponseDto, RedirectAttributes redirectAttributes){
        ownerService.updateOwnerDetails(ownerResponseDto,id);
        redirectAttributes.addFlashAttribute(NOTIFICATION_ATTRIBUTE,"Dane zostały zaktualizowane!");
        return "redirect:/owner/{id}";
    }

    @PostMapping("owners/delete")
    String deleteOwner(@RequestParam long id, RedirectAttributes redirectAttributes){
        ownerService.deleteOwner(id);
        redirectAttributes.addFlashAttribute(NOTIFICATION_ATTRIBUTE,"Konto usunięte");
        return "redirect:/";
    }

}
