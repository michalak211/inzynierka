package pl.mrozek.inzynierka.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("koktajl")
public class KoktailController {

    @GetMapping("/add")
    public String addKoktajl(Model model){
        System.out.println("weszlo");
        return "/tytulowa";
    }

    @GetMapping("/edit/{id}")
    public String editKoktajl(@PathVariable Long id, Model model){
        return "test";
    }




}
