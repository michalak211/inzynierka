package pl.mrozek.inzynierka.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;

@Controller
@RequestMapping("koktajl")
public class KoktailController {

    @GetMapping("/add")
    public String addKoktajl(Model model){
        return "/tytulowa";
    }

    @GetMapping("/edit/{id}")
    public String editKoktajl(@PathVariable Long id, Model model){
        return "test";
    }

//    @Transactional
//    @GetMapping("/add")
//    public String kwn(Model model) {
//        KWNDto formKWN = new KWNDto();
//        List<Niezgodnosc> niezgodnoscList= (List<Niezgodnosc>) niezgodnoscRepo.findAll();
//        List<Employee> employees = employeeService.getAllEmployee();
//        model.addAttribute("kwnForm", formKWN);
//        model.addAttribute("employees", employees);
//        model.addAttribute("niezgodnoscList",niezgodnoscList);
//        model.addAttribute("newKwn", 1);
//        return "/kwn/kwn";
//    }
//
//
//    @Transactional
//    @PostMapping("/add")
//    public String kwnSubmit(@ModelAttribute("kwnForm") KWNDto formKWN) {
//        kwnService.addKWN(formKWN);
//        return  "redirect:/archiwum/kwn";
//    }



}
