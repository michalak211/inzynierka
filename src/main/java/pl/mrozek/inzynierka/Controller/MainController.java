package pl.mrozek.inzynierka.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.mrozek.inzynierka.Service.KoktailService;

@Controller
@RequestMapping("baza")
public class MainController {

    final
    KoktailService koktailService;

    public MainController(KoktailService koktailService) {
        this.koktailService = koktailService;
    }

    @GetMapping("/test")
    public void test(){
        koktailService.addKoktai();
    }

}
