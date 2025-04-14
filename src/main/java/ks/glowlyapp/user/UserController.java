package ks.glowlyapp.user;

import ks.glowlyapp.user.dto.UserRegistrationDto;
import ks.glowlyapp.user.dto.UserResponseDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
public class UserController {
    public static final String NOTIFICATION_ATTRIBUTE = "notification";

    UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public String getAllUsers(){
        userService.getAllUsers();
        return "all-users";
    }

    @GetMapping("users/{id}")
    public String getUserById(@PathVariable long id, Model model){
        Optional<UserResponseDto> userById = userService.getUserById(id);
        userById.ifPresent(user->model.addAttribute("user", user));
        return "user";
    }

    @PostMapping("/users/new")
    public String newUser(UserRegistrationDto userRegistrationDto, RedirectAttributes redirectAttributes){
        userService.createNewUser(userRegistrationDto);
        redirectAttributes.addFlashAttribute(NOTIFICATION_ATTRIBUTE,"Rejestracja udana!");
        return "redirect:/";
    }

    @GetMapping("/users/new")
    public String newUserForm(Model model){
        UserRegistrationDto user = new UserRegistrationDto();
        model.addAttribute("user", user);
        return "user-form";
    }

    @PostMapping("/users/update")
    public String updateUser(@RequestParam long id, UserResponseDto userResponseDto, RedirectAttributes redirectAttributes){
        userService.updateUserDetails(userResponseDto,id);
        redirectAttributes.addFlashAttribute(NOTIFICATION_ATTRIBUTE,"Dane zostały zaktualizowane!");
        return "redirect:/user/{id}";
    }

    @PostMapping("users/delete")
    String deleteUser(@RequestParam long id, RedirectAttributes redirectAttributes){
        userService.deleteUser(id);
        redirectAttributes.addFlashAttribute(NOTIFICATION_ATTRIBUTE,"Konto usunięte");
        return "redirect:/";
    }




}
