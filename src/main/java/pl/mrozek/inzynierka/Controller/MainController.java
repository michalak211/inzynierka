package pl.mrozek.inzynierka.Controller;


import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import pl.mrozek.inzynierka.Config.UserDetailServiceB;
import pl.mrozek.inzynierka.Dto.KoktajlForm;
import pl.mrozek.inzynierka.Dto.SkladnikP;
import pl.mrozek.inzynierka.Entity.bar.Barek;
import pl.mrozek.inzynierka.Entity.przepis.Alkohol;
import pl.mrozek.inzynierka.Entity.przepis.Koktajl;
import pl.mrozek.inzynierka.Entity.skladniki.Skladnik;
import pl.mrozek.inzynierka.Entity.user.BarUser;
import pl.mrozek.inzynierka.Repo.*;
import pl.mrozek.inzynierka.Services.*;
import pl.mrozek.inzynierka.mapper.Mapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;


@Controller
@RequestMapping("")
public class MainController {

    private final KoktailService koktailService;
    private final AlkoholRepo alkoholRepo;
    private final TypRepo typRepo;
    private final SkladnikService skladnikService;
    private final BarekRepo barekRepo;
    private final ButelkaRepo butelkaRepo;
    private final UserService userService;
    private final BackupService backupService;
    private final SkladnikRepo skladnikRepo;
    private final SokRepo sokRepo;
    private final SyropRepo syropRepo;
    private final InnyRepo innyRepo;
    private final FilterService filterService;
    private final Mapper mapper;
    private final UserDetailServiceB userDetailServiceB;


    public MainController(KoktailService koktailService, AlkoholRepo alkoholRepo, TypRepo typRepo, SkladnikService skladnikService, BarekRepo barekRepo, ButelkaRepo butelkaRepo, UserService userService, BackupService backupService, SkladnikRepo skladnikRepo, SokRepo sokRepo, SyropRepo syropRepo, InnyRepo innyRepo, FilterService filterService, Mapper mapper, UserDetailServiceB userDetailServiceB) {
        this.koktailService = koktailService;
        this.alkoholRepo = alkoholRepo;
        this.typRepo = typRepo;
        this.skladnikService = skladnikService;
        this.barekRepo = barekRepo;
        this.butelkaRepo = butelkaRepo;
        this.userService = userService;
        this.backupService = backupService;
        this.skladnikRepo = skladnikRepo;
        this.sokRepo = sokRepo;
        this.syropRepo = syropRepo;
        this.innyRepo = innyRepo;
        this.filterService = filterService;
        this.mapper = mapper;
        this.userDetailServiceB = userDetailServiceB;
    }

    @GetMapping("/")
    public String tytulowa() {
        return "tytulowa";
    }

    @GetMapping("/test")
    public String test() {
        return "redirect:/przegladaj";
    }

    @GetMapping("/przegladaj")
    public String przegladanie(Model model) {
        List<KoktajlForm> koktajlFormList = koktailService.getAllKoktajlForms();
        Barek barek = barekRepo.findByNazwaEquals("barek mieszkanie");
        if (barek != null) {
            filterService.checkSkladnikAccesability(barek.getId(), koktajlFormList);
        }

        model.addAttribute("bars", barekRepo.findAll());
        model.addAttribute("filterSet", new KoktajlForm());
        model.addAttribute("typList", typRepo.findAll());
        model.addAttribute("koktajlList", koktajlFormList);
        model.addAttribute("isAdmin",userService.isAdmin());

        model.addAttribute("alkoholList", alkoholRepo.findAll());
        model.addAttribute("sokList", sokRepo.findAll());
        model.addAttribute("syropList", syropRepo.findAll());
        model.addAttribute("innyList", innyRepo.findAll());

        return "wyswietl";
    }

    @PostMapping(value = "/przegladaj", params = "filtruj")
    public String filtrowanie(Model model, @ModelAttribute("filterSet") KoktajlForm filterSet) {


        System.out.println(filterSet);

        model.addAttribute("isAdmin",userService.isAdmin());
        model.addAttribute("bars", barekRepo.findAll());
        model.addAttribute("filterSet", new KoktajlForm());
        model.addAttribute("typList", typRepo.findAll());
        model.addAttribute("koktajlList", filterService.completeFilters(filterSet));

        model.addAttribute("alkoholList", alkoholRepo.findAll());
        model.addAttribute("sokList", sokRepo.findAll());
        model.addAttribute("syropList", syropRepo.findAll());
        model.addAttribute("innyList", innyRepo.findAll());

        return "wyswietl";

    }


    @Transactional
    @PostMapping(value = "/dodZdj", consumes = {"multipart/form-data"})
    public String addPicture(@RequestParam String zdjecie, HttpServletRequest request) {
        koktailService.addPhoto(zdjecie, request);
        return "redirect:/przegladaj";
    }

    @GetMapping("/image/{id}")
    @ResponseBody
    void showImage(@PathVariable("id") Long id, HttpServletResponse response) throws IOException {
        response.setContentType("image/*");


        if (koktailService.getPhoto(id) != null) {
            response.getOutputStream().write(koktailService.getPhoto(id));
            response.getOutputStream().close();
            return;
        }
        InputStream inputStream = getClass().getResourceAsStream("/static/img/fotka.jpg");
        assert inputStream != null;
        byte[] bytes = IOUtils.toByteArray(inputStream);
        response.getOutputStream().write(bytes);
        response.getOutputStream().close();


    }


    @GetMapping(value = "/struktura")
    public String struktura(Model model, Principal principal) {

//        System.out.println(principal);
        model.addAttribute("alkoList", alkoholRepo.findAll());
        return "struktura";
    }

    @PostMapping(value = "/struktura", params = "dodaj")
    public String strukturaAdd(Model model, @RequestParam String nowyAlko) {

        skladnikService.addNewAlko(nowyAlko);
        model.addAttribute("alkoList", alkoholRepo.findAll());
        return "struktura";
    }

    @PostMapping(value = "/struktura", params = "download")
    public void dowloadJson(HttpServletResponse response) {
        backupService.generateBackup(response);
    }


    @PostMapping(value = "/struktura", params = "update")
    public String updateDatabase(@RequestParam("uploadFile") MultipartFile multipartFile) {
        if (!multipartFile.isEmpty()) {
            backupService.getFiletoUpdate(multipartFile);
        }
        return "redirect:/struktura";
    }


    @GetMapping(value = "/struktura/edit/{id}")
    public String strukturaEdit(Model model, @PathVariable("id") Long id) {

        if (alkoholRepo.findById(id).isPresent()) {
            Alkohol alkohol = alkoholRepo.findById(id).get();
            model.addAttribute("alkohol", alkohol);
            model.addAttribute("bottleList", butelkaRepo.findAllByAlkoholIdEquals(id));
            return "strukturaEdit";
        }
        return "redirect:/struktura";
    }

    @PostMapping(value = "/struktura/edit/{id}", params = "zapisz")
    public String strukturaEditPost(Model model, @ModelAttribute("alkohol") Alkohol alkohol, @PathVariable("id") Long id) {
        return skladnikService.editAlko(id, alkohol, model);
    }

    @Transactional
    @PostMapping(value = "/struktura/edit/{id}", params = "usunTyp")
    public String typDelete(Model model, @RequestParam long usunTyp, @PathVariable("id") Long id) {
        if (skladnikService.deleteTyp(usunTyp, id)) {
            if (alkoholRepo.findById(id).isPresent()) {
                Alkohol alkoholBaza = alkoholRepo.findById(id).get();
                model.addAttribute("alkohol", alkoholBaza);
                model.addAttribute("bottleList", butelkaRepo.findAllByAlkoholIdEquals(id));
                return "strukturaEdit";
            }
        }
        return "redirect:/struktura/edit/" + id;
    }

    @PostMapping(value = "/struktura/edit/{id}", params = "dodaj")
    public String addTyp(@RequestParam String nowyTyp, @PathVariable("id") Long id) {

        skladnikService.addTyp(id, nowyTyp);
        return "redirect:/struktura/edit/" + id;
    }

    @RequestMapping("/login")
    public String login() {
        return "userManager/login";
    }

    @RequestMapping("/singup")
    public ModelAndView singup() {
        return new ModelAndView("userManager/registration", "user", new BarUser());
    }

    @RequestMapping("/register")
    public ModelAndView register(BarUser user, HttpServletRequest request) {
        System.out.println(user.getMail());
        userService.addNewUser(user, request);
        return new ModelAndView("redirect:/login");
    }

    @RequestMapping("/verify-token")
    public ModelAndView register(@RequestParam String token) {
        userService.verifyToken(token);
        return new ModelAndView("redirect:/login");
    }

}
