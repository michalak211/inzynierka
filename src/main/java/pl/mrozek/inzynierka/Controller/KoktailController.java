package pl.mrozek.inzynierka.Controller;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.mrozek.inzynierka.Dto.KoktajlForm;
import pl.mrozek.inzynierka.Entity.przepis.Koktajl;
import pl.mrozek.inzynierka.Entity.user.Authoritiy;
import pl.mrozek.inzynierka.Entity.user.BarUser;
import pl.mrozek.inzynierka.Entity.user.Role;
import pl.mrozek.inzynierka.Repo.*;
import pl.mrozek.inzynierka.Service.KoktailService;
//import pl.mrozek.inzynierka.Service.MailSenderService;
import pl.mrozek.inzynierka.Service.MailSenderService;
import pl.mrozek.inzynierka.Service.UserService;
import pl.mrozek.inzynierka.mapper.Mapper;

import javax.mail.MessagingException;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.transaction.Transactional;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;

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
    private final PasswordEncoder passwordEncoder;
    private final BarUserRepo barUserRepo;
    private final AuthoritiyRepo authoritiyRepo;
    private final UserService userService;
    private final MailSenderService mailSenderService;

    public KoktailController(SkladnikRepo skladnikRepo, AlkoholRepo alkoholRepo, TypRepo typRepo, Mapper mapper, KoktailRepo koktailRepo, SokRepo sokRepo, SyropRepo syropRepo, InnyRepo innyRepo, KoktailService koktailService, PasswordEncoder passwordEncoder, BarUserRepo barUserRepo, AuthoritiyRepo authoritiyRepo, UserService userService, MailSenderService mailSenderService) {
        this.skladnikRepo = skladnikRepo;
        this.alkoholRepo = alkoholRepo;
        this.typRepo = typRepo;
        this.mapper = mapper;
        this.koktailRepo = koktailRepo;
        this.sokRepo = sokRepo;
        this.syropRepo = syropRepo;
        this.innyRepo = innyRepo;
        this.koktailService = koktailService;
        this.passwordEncoder = passwordEncoder;
        this.barUserRepo = barUserRepo;
        this.authoritiyRepo = authoritiyRepo;
        this.userService = userService;
        this.mailSenderService = mailSenderService;
    }

    @Transactional
    @GetMapping("/add")
    public String addKoktajl(Model model) {

        KoktajlForm koktajlForm = new KoktajlForm();
        koktajlForm.setZdobienie("Dowolne");

        model.addAttribute("skladnikList", alkoholRepo.findAll());
        model.addAttribute("koktajlForm", koktajlForm);
        model.addAttribute("sokList", sokRepo.findAll());
        model.addAttribute("syropList", syropRepo.findAll());
        model.addAttribute("innyList", innyRepo.findAll());

        return "edit";
    }

    @Transactional
    @PostMapping("/add")
    public String addKoktajlSubmitt(@ModelAttribute("koktajlForm") KoktajlForm koktajlForm, Model model) {


        Koktajl koktajl = new Koktajl();
        koktajl = mapper.toKoktajl(koktajl, koktajlForm);
        koktailRepo.save(koktajl);

        return "redirect:/przegladaj";

    }

    @GetMapping("/test")
    public String test(Model model) {


//        try {
//            mailSenderService.sendMail("Michalak211@gmail.com", "testTemat","hakunamatta",false);
//        } catch (MessagingException e) {
//            e.printStackTrace();
//        }

//        KoktajlForm koktajlForm = new KoktajlForm();
//        model.addAttribute("skladnikList", alkoholRepo.findAll());
//        model.addAttribute("koktajlForm", koktajlForm);
//        model.addAttribute("sokList", sokRepo.findAll());
//        model.addAttribute("syropList", syropRepo.findAll());
//        model.addAttribute("innyList", innyRepo.findAll());

        return "redirect:/koktajl/add";

    }


    @GetMapping("/edit/{id}")
    public String editKoktajl(@PathVariable Long id, Model model) {

        KoktajlForm koktajlForm = new KoktajlForm();
        if (koktailRepo.findById(id).isPresent()) {
            Koktajl koktajl = koktailRepo.findById(id).get();
            koktajlForm = mapper.toKoktajlForm(koktajl);
        }


        model.addAttribute("skladnikList", alkoholRepo.findAll());
        model.addAttribute("koktajlForm", koktajlForm);
        model.addAttribute("sokList", sokRepo.findAll());
        model.addAttribute("syropList", syropRepo.findAll());
        model.addAttribute("innyList", innyRepo.findAll());


        return "edit";
    }

    @Transactional
    @PostMapping("/edit/{id}")
    public String editKoktajlPost(@PathVariable Long id, @ModelAttribute("koktajlForm") KoktajlForm koktajlForm, Model model) {


        if (koktailRepo.findById(id).isPresent()) {
            Koktajl koktajl = koktailRepo.findById(id).get();
            koktajl = mapper.toKoktajl(koktajl, koktajlForm);
            koktailRepo.save(koktajl);
        }

        model.addAttribute("alkoholList", alkoholRepo.findAll());
        model.addAttribute("typList", typRepo.findAll());
        model.addAttribute("koktajlList", koktailService.getAllKoktajlForms());
        return "redirect:/przegladaj";

    }

    @Transactional
    @GetMapping("/init")
    public String init() {



        if (authoritiyRepo.findAll().size()>0) return "redirect:/przegladaj";

//        Authoritiy authoritiy= new Authoritiy();
//        authoritiy.setAuthority(Role.ROLE_USER.toString());
//        authoritiyRepo.save(authoritiy);
//
//        Authoritiy authoritiy1= new Authoritiy();
//        authoritiy1.setAuthority(Role.ROLE_ADMIN.toString());
//        authoritiyRepo.save(authoritiy1);
//
//        BarUser barUser= new BarUser();
//        barUser.setUsername("admin");
//        barUser.setPassword("admin");
//        barUser.setEnabled(true);
//        userService.addNewUser(barUser, null );


//        Uzytkownik uzytkownik= new Uzytkownik();
//        uzytkownik.setUsername("admin");
//        uzytkownik.setPassword(passwordEncoder.encode("admin"));
//        userRepo.save(uzytkownik);

//        Alkohol alkohol = new Alkohol();
//        alkohol.setNazwa("whyskey");
//
//        alkoholRepo.save(alkohol);
//
//        Alkohol alkohol1 = new Alkohol();
//        alkohol1.setNazwa("rum");
//        alkoholRepo.save(alkohol1);
//
//
//        Typ typ = new Typ();
//        typ.setNazwa("Burbon");
//        typ.setAlkoholID(alkohol.getId());
//        typRepo.save(typ);
//
//
//        Typ typ1 = new Typ();
//        typ1.setNazwa("Szkocka");
//        typ1.setAlkoholID(alkohol.getId());
//        typRepo.save(typ1);
//
//        Typ typ2 = new Typ();
//        typ2.setAlkoholID(alkohol1.getId());
//        typ2.setNazwa("czarny");
//        typRepo.save(typ2);
//
//        Typ typ3 = new Typ();
//        typ3.setAlkoholID(alkohol1.getId());
//        typ3.setNazwa("jasny");
//        typRepo.save(typ3);
//
//        List<Typ> typLista = new ArrayList<>();
//        typLista.add(typ);
//        typLista.add(typ1);
//
//        List<Typ> typLista1 = new ArrayList<>();
//        typLista1.add(typ2);
//        typLista1.add(typ3);
//
//
//        alkohol.setTypList(typLista);
//        alkoholRepo.save(alkohol);
//
//        alkohol1.setTypList(typLista1);
//        alkoholRepo.save(alkohol1);
//
//        Sok sok = new Sok();
//        sok.setNazwa("sok limonkowy");
//        sokRepo.save(sok);
//
//        Syrop syrop = new Syrop();
//        syrop.setNazwa("syrop cukrowy");
//        syropRepo.save(syrop);

//        Barek barek = new Barek();
//        barek.setNazwa("barek mieszkanie");


//        List<Butelka> typList = new ArrayList<>();
//        List<Sok> sokList = new ArrayList<>();
//        List<Syrop> syropList = new ArrayList<>();
//        List<Inny> innyList = new ArrayList<>();
//
//        barek.setButelkaList(typList);
//        barek.setListInny(innyList);
//        barek.setListSyrop(syropList);
//        barek.setListSok(sokList);
//        barek.setNazwa("barek mieszkanie");
//
//        barekRepo.save(barek);

        return "redirect:/koktajl/add";

    }




}
