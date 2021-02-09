package pl.mrozek.inzynierka.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import pl.mrozek.inzynierka.Repo.AlkoholRepo;
import pl.mrozek.inzynierka.Repo.TypRepo;
import pl.mrozek.inzynierka.Service.KoktailService;

import javax.transaction.Transactional;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

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


    @Transactional
@PostMapping(value = "/przegladaj")
    public String addPicture(Model model, @RequestParam String zdjecie,@RequestParam("image") MultipartFile file){

        System.out.println(zdjecie);



        model.addAttribute("alkoholList", alkoholRepo.findAll());
        model.addAttribute("typList", typRepo.findAll());
        model.addAttribute("koktajlList",koktailService.getAllUserForms());
        return  "redirect:/przegladaj";

    }

}
