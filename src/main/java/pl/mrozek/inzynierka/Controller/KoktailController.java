package pl.mrozek.inzynierka.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.mrozek.inzynierka.Dto.KoktajlForm;
import pl.mrozek.inzynierka.Entity.przepis.Alkohol;
import pl.mrozek.inzynierka.Entity.skladniki.Typ;
import pl.mrozek.inzynierka.Repo.*;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("koktajl")

public class KoktailController {


    final
    SkladnikRepo skladnikRepo;
    final
    AlkoholRepo alkoholRepo;
    final
    TypRepo typRepo;

    public KoktailController(SkladnikRepo skladnikRepo, AlkoholRepo alkoholRepo, TypRepo typRepo) {
        this.skladnikRepo = skladnikRepo;
        this.alkoholRepo = alkoholRepo;
        this.typRepo = typRepo;
    }

    @Transactional
    @GetMapping("/add")
    public String addKoktajl(Model model){

//        System.out.println(alkoholRepo.findAll());
//        System.out.println(skladnikRepo.findAll());

        KoktajlForm koktajlForm=new KoktajlForm();
        koktajlForm.setZdobienie("Dowolne");

        model.addAttribute("skladnikList", alkoholRepo.findAll());
        model.addAttribute("koktajlForm",koktajlForm);

        return "/dodaj";
    }

    @Transactional
    @PostMapping("/add")
    public String addKoktajlSubmitt(@ ModelAttribute("koktajlForm") KoktajlForm koktajlForm,Model model){

        System.out.println("odbieram koktajl form");
        System.out.println(koktajlForm);
        model.addAttribute("skladnikList", alkoholRepo.findAll());
        model.addAttribute("koktajlForm",new KoktajlForm());

        return  "/dodaj";
    }







    @GetMapping("/edit/{id}")
    public String editKoktajl(@PathVariable Long id, Model model){
        return "test";
    }

    @Transactional
    @GetMapping("/init")
    public String init(){

        Typ typ= new Typ();
        typ.setNazwa("Burbon");
        typRepo.save(typ);

        Typ typ1= new Typ();
        typ1.setNazwa("Szkocka");
        typRepo.save(typ1);

        Typ typ2= new Typ();
        typ2.setNazwa("czarny");
        typRepo.save(typ2);

        Typ typ3= new Typ();
        typ3.setNazwa("jasny");
        typRepo.save(typ3);

        List<Typ> typLista= new ArrayList<>();
        typLista.add(typ);
        typLista.add(typ1);

        List<Typ> typLista1= new ArrayList<>();
        typLista1.add(typ2);
        typLista1.add(typ3);

        Alkohol alkohol= new Alkohol();
        alkohol.setNazwa("whyskey");

        Alkohol alkohol1= new Alkohol();
        alkohol1.setNazwa("rum");

        alkohol.setTypList(typLista);
        alkoholRepo.save(alkohol);

        alkohol1.setTypList(typLista1);
        alkoholRepo.save(alkohol1);

//        System.out.println("dodano");
//        System.out.println(alkohol);
//        System.out.println(alkohol1);

        return  "redirect:/koktajl/add";

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
