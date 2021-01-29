package pl.mrozek.inzynierka.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.mrozek.inzynierka.Repo.AlkoholRepo;
import pl.mrozek.inzynierka.Repo.TypRepo;
import pl.mrozek.inzynierka.Service.KoktailService;

@Controller
@RequestMapping("")
public class MainController {

    final
    KoktailService koktailService;
    private final AlkoholRepo alkoholRepo;
    private final TypRepo typRepo;

    public MainController(KoktailService koktailService, AlkoholRepo alkoholRepo, TypRepo typRepo) {
        this.koktailService = koktailService;
        this.alkoholRepo = alkoholRepo;
        this.typRepo = typRepo;
    }

    @GetMapping("/test")
    public void test(){
        koktailService.addKoktajl();
    }

    @GetMapping ("/przegladaj")
    public String przegladanie(Model model){


        model.addAttribute("alkoholList", alkoholRepo.findAll());
        model.addAttribute("typList", typRepo.findAll());
        model.addAttribute("koktajlList",koktailService.getAllUserForms());
//        System.out.println("controller");
//        System.out.println(koktailService.getAllUserForms());
        return "/wyswietl";
    }
}
