package ks.glowlyapp.offer;

import ks.glowlyapp.offer.dto.OfferDto;
import ks.glowlyapp.offer.dto.OfferShortDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
public class OfferController {
    public static final String NOTIFICATION_ATTRIBUTE = "notification";

    OfferService offerService;

    public OfferController(OfferService offerService) {
        this.offerService = offerService;
    }

    @GetMapping("/offers")
    public String getAllOffers(){
        offerService.getAllOffers();
        return "all-offers";
    }

    @GetMapping("/offers/{name}")
    public String findOfferByName(@PathVariable String name, Model model){
        Optional<OfferDto> offerByName = offerService.getOfferByName(name);
        offerByName.ifPresent(offer->model.addAttribute("offer",offer));
        return "offer";
    }

    @GetMapping("/offers/sort")
    public String sortOffersByPrice(@RequestParam(defaultValue = "true") boolean asc, Model model) {
        List<OfferDto> offers = offerService.getOffersSortedByPrice(asc);
        model.addAttribute("offers", offers);
        return "offers";
    }

    @PostMapping("/offers/new")
    public String createNewOffer(OfferShortDto offerShortDto, Long businessId, RedirectAttributes redirectAttributes){
        offerService.createOfferForBusiness(offerShortDto,businessId);
        redirectAttributes.addFlashAttribute(NOTIFICATION_ATTRIBUTE,"Dodano nową ofertę");
        return "redirect:/";
    }

    @GetMapping("/offers/new")
    public String newOfferForm(Model model){
        OfferShortDto offer = new OfferShortDto();
        model.addAttribute("offer", offer);
        return "offer-form";
    }

    @PostMapping("offer/update")
    public String updateOffer(@RequestParam Long id, OfferDto offerDto, RedirectAttributes redirectAttributes){
        offerService.updateOfferDetails(offerDto,id);
        redirectAttributes.addFlashAttribute(NOTIFICATION_ATTRIBUTE,"Pomyślnie zaktualizowano ofertę");
        return "offer";
    }

    @PostMapping("offer/delete")
    public String deleteOffer(@RequestParam Long id, OfferDto offerDto, RedirectAttributes redirectAttributes){
        offerService.deleteOffer(id);
        redirectAttributes.addFlashAttribute(NOTIFICATION_ATTRIBUTE,"Oferta usunięta");
        return "redirect:/";
    }

    }
