package pl.mrozek.inzynierka.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.mrozek.inzynierka.Dto.KoktajlForm;
import pl.mrozek.inzynierka.Dto.SkladnikP;
import pl.mrozek.inzynierka.Entity.przepis.Alkohol;
import pl.mrozek.inzynierka.Entity.przepis.Koktajl;
import pl.mrozek.inzynierka.Entity.skladniki.Sok;
import pl.mrozek.inzynierka.Entity.skladniki.Syrop;
import pl.mrozek.inzynierka.Entity.skladniki.Typ;
import pl.mrozek.inzynierka.Repo.*;
import pl.mrozek.inzynierka.Service.KoktailService;
import pl.mrozek.inzynierka.mapper.Mapper;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("koktajl")

public class KoktailController {


    private final
    SkladnikRepo skladnikRepo;
    private final
    AlkoholRepo alkoholRepo;
    private final
    TypRepo typRepo;
    private final Mapper mapper;
    private final KoktailRepo koktailRepo;
    private final SokRepo sokRepo;
    private final SyropRepo syropRepo;
    private final InnyRepo innyRepo;
    private final KoktailService koktailService;

    public KoktailController(SkladnikRepo skladnikRepo, AlkoholRepo alkoholRepo, TypRepo typRepo, Mapper mapper, KoktailRepo koktailRepo, SokRepo sokRepo, SyropRepo syropRepo, InnyRepo innyRepo, KoktailService koktailService) {
        this.skladnikRepo = skladnikRepo;
        this.alkoholRepo = alkoholRepo;
        this.typRepo = typRepo;
        this.mapper = mapper;
        this.koktailRepo = koktailRepo;
        this.sokRepo = sokRepo;
        this.syropRepo = syropRepo;
        this.innyRepo = innyRepo;
        this.koktailService = koktailService;
    }

    @Transactional
    @GetMapping("/add")
    public String addKoktajl(Model model){

        KoktajlForm koktajlForm=new KoktajlForm();
        koktajlForm.setZdobienie("Dowolne");

        model.addAttribute("skladnikList", alkoholRepo.findAll());
        model.addAttribute("koktajlForm",koktajlForm);
        model.addAttribute("sokList",sokRepo.findAll());
        model.addAttribute("syropList",syropRepo.findAll());
        model.addAttribute("innyList",innyRepo.findAll());

        return "/dodaj";
    }

    @Transactional
    @PostMapping("/add")
    public String addKoktajlSubmitt(@ ModelAttribute("koktajlForm") KoktajlForm koktajlForm,Model model){


        System.out.println("koktail form");
        for (SkladnikP skladnikP: koktajlForm.getListaSkladnikow()){
            System.out.println(skladnikP);


        }
        System.out.println("post mapping");

        Koktajl koktajl = new Koktajl();
        koktajl =mapper.toKoktajl(koktajl,koktajlForm);
        koktailRepo.save(koktajl);
        System.out.println("zapisano");
        System.out.println(koktajl);

        model.addAttribute("alkoholList", alkoholRepo.findAll());
        model.addAttribute("typList", typRepo.findAll());
        model.addAttribute("koktajlList",koktailService.getAllUserForms());


//        model.addAttribute("skladnikList", alkoholRepo.findAll());
//        model.addAttribute("koktajlForm",new KoktajlForm());


        //planowane przeniesienie do edycji,a w edycjki dodwanie zdjecia
        return  "redirect:/przegladaj";

    }

    @GetMapping("/test")
    public String test(Model model){


        for (Alkohol alkohol:alkoholRepo.findAll()){
            System.out.println(alkohol);
        }

        KoktajlForm koktajlForm=new KoktajlForm();
        model.addAttribute("skladnikList", alkoholRepo.findAll());
        model.addAttribute("koktajlForm",koktajlForm);
        model.addAttribute("sokList",sokRepo.findAll());
        model.addAttribute("syropList",syropRepo.findAll());
        model.addAttribute("innyList",innyRepo.findAll());

        return  "redirect:/koktajl/add";

    }








    @GetMapping("/edit/{id}")
    public String editKoktajl(@PathVariable Long id, Model model){

        KoktajlForm koktajlForm= new KoktajlForm();
        if (koktailRepo.findById(id).isPresent()){
            Koktajl koktajl= koktailRepo.findById(id).get();
            koktajlForm= mapper.toKoktajlForm(koktajl);
        }



        model.addAttribute("skladnikList", alkoholRepo.findAll());
        model.addAttribute("koktajlForm",koktajlForm);
        model.addAttribute("sokList",sokRepo.findAll());
        model.addAttribute("syropList",syropRepo.findAll());
        model.addAttribute("innyList",innyRepo.findAll());


        return "/edit";
    }

    @Transactional
    @GetMapping("/init")
    public String init(){

        Alkohol alkohol= new Alkohol();
        alkohol.setNazwa("whyskey");

        alkoholRepo.save(alkohol);

        Alkohol alkohol1= new Alkohol();
        alkohol1.setNazwa("rum");
        alkoholRepo.save(alkohol1);



        Typ typ= new Typ();
        typ.setNazwa("Burbon");
        typ.setAlkoholID(alkohol.getId());
        typRepo.save(typ);


        Typ typ1= new Typ();
        typ1.setNazwa("Szkocka");
        typ1.setAlkoholID(alkohol.getId());
        typRepo.save(typ1);

        Typ typ2= new Typ();
        typ2.setAlkoholID(alkohol1.getId());
        typ2.setNazwa("czarny");
        typRepo.save(typ2);

        Typ typ3= new Typ();
        typ3.setAlkoholID(alkohol1.getId());
        typ3.setNazwa("jasny");
        typRepo.save(typ3);

        List<Typ> typLista= new ArrayList<>();
        typLista.add(typ);
        typLista.add(typ1);

        List<Typ> typLista1= new ArrayList<>();
        typLista1.add(typ2);
        typLista1.add(typ3);


        alkohol.setTypList(typLista);
        alkoholRepo.save(alkohol);

        alkohol1.setTypList(typLista1);
        alkoholRepo.save(alkohol1);

        Sok sok= new Sok();
        sok.setNazwa("soczek");
        sokRepo.save(sok);

        Syrop syrop= new Syrop();
        syrop.setNazwa("syropek");
        syropRepo.save(syrop);

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
