package ks.glowlyapp.business;

import ks.glowlyapp.business.dto.BusinessDto;
import ks.glowlyapp.owner.dto.OwnerRegistrationDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
public class BusinessController {
    public static final String NOTIFICATION_ATTRIBUTE = "notification";
    BusinessService businessService;

    @GetMapping("/business/{name}")
    public String findBusinessByName(@PathVariable String name, Model model){
        Optional<BusinessDto> businessByName = businessService.findBusinessByName(name);
        businessByName.ifPresent(business->model.addAttribute("business", business));
        return "business";
    }

    @GetMapping("/businnes/{id}")
    public String findBusinessById(@PathVariable Long id,Model model){
        Optional<BusinessDto> businessById = businessService.findBusinessById(id);
        businessById.ifPresent(business->model.addAttribute("business", business));
        return "business";
    }

    @PostMapping("/business/new")
    public String newBusiness(BusinessDto businessDto,Long ownerId, RedirectAttributes redirectAttributes){
        businessService.addBusinessToOwner(businessDto,ownerId);
        redirectAttributes.addFlashAttribute(NOTIFICATION_ATTRIBUTE,"Dodano nową firmę");
        return "redirect:/";
    }

    @GetMapping("/business/new")
    public String newBusinessForm(Model model){
        BusinessDto business = new BusinessDto();
        model.addAttribute("business", business);
        return "business-form";
    }

    @PostMapping("/business/update")
    public String updateBusiness(@RequestParam Long id, BusinessDto businessDto, RedirectAttributes redirectAttributes){
        businessService.updateBusinessDetails(businessDto,id);
        redirectAttributes.addFlashAttribute(NOTIFICATION_ATTRIBUTE,"Pomyślnie zaktualizowano informacje!");
        return "redirect:/business";
    }

    @PostMapping("/business/delete")
    public String deleteBusiness(@RequestParam Long id, RedirectAttributes redirectAttributes){
        businessService.deleteBusiness(id);
        redirectAttributes.addFlashAttribute(NOTIFICATION_ATTRIBUTE,"Firma usunięta");
        return "redirect:/";
    }
}
