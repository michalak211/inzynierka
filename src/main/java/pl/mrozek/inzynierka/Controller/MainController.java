package pl.mrozek.inzynierka.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.mrozek.inzynierka.Entity.User;
import pl.mrozek.inzynierka.Service.KoktailService;

@Controller
@RequestMapping("")
public class MainController {

    final
    KoktailService koktailService;

    public MainController(KoktailService koktailService) {
        this.koktailService = koktailService;
    }

    @GetMapping("/test")
    public void test(){
        koktailService.addKoktajl();
    }

    @GetMapping ("/przegladaj")
    public String przegladanie(Model model){

        model.addAttribute("koktailList",koktailService.getAllUserKoktajls());
        return "/lista";
    }
}
