package pl.mrozek.inzynierka.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.mrozek.inzynierka.Dto.SkladnikP;
import pl.mrozek.inzynierka.Entity.bar.Barek;
import pl.mrozek.inzynierka.Entity.bar.Butelka;
import pl.mrozek.inzynierka.Repo.*;
import pl.mrozek.inzynierka.Service.KoktailService;
import pl.mrozek.inzynierka.Service.SkladnikService;
import pl.mrozek.inzynierka.mapper.Mapper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

@Controller
@RequestMapping("")
public class MainController {

    final
    KoktailService koktailService;
    private final AlkoholRepo alkoholRepo;
    private final TypRepo typRepo;
    private final KoktailRepo koktailRepo;
    private final SokRepo sokRepo;
    private final SyropRepo syropRepo;
    private final InnyRepo innyRepo;
    private final BarekRepo barekRepo;
    private final SkladnikService skladnikService;
    private final Mapper mapper;
    private final ButelkaRepo butelkaRepo;


    public MainController(KoktailService koktailService, AlkoholRepo alkoholRepo, TypRepo typRepo, KoktailRepo koktailRepo, SokRepo sokRepo, SyropRepo syropRepo, InnyRepo innyRepo, BarekRepo barekRepo, SkladnikService skladnikService, Mapper mapper, ButelkaRepo butelkaRepo) {
        this.koktailService = koktailService;
        this.alkoholRepo = alkoholRepo;
        this.typRepo = typRepo;
        this.koktailRepo = koktailRepo;
        this.sokRepo = sokRepo;
        this.syropRepo = syropRepo;
        this.innyRepo = innyRepo;
        this.barekRepo = barekRepo;
        this.skladnikService = skladnikService;
        this.mapper = mapper;
        this.butelkaRepo = butelkaRepo;
    }

    @GetMapping("/test")
    public String test() {

//        if (butelkaRepo.findById((long)16).isPresent()) {
//            Butelka butelka= butelkaRepo.findById((long)16).get();
//            butelkaRepo.delete(butelka);
//        }
        return "redirect:/przegladaj";

    }

    @GetMapping("/przegladaj")
    public String przegladanie(Model model) {


        model.addAttribute("alkoholList", alkoholRepo.findAll());
        model.addAttribute("typList", typRepo.findAll());
        model.addAttribute("koktajlList", koktailService.getAllUserForms());
        return "/wyswietl";
    }


    @Transactional
    @PostMapping(value = "/dodZdj", consumes = {"multipart/form-data"})
    public String addPicture(@RequestParam String zdjecie,
                             HttpServletRequest request
    ) {
        koktailService.addPhoto(zdjecie, request);
        return "redirect:/przegladaj";
    }

    @GetMapping("/image/{id}")
    @ResponseBody
    void showImage(@PathVariable("id") Long id, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("image/jpeg, image/jpg, image/png, image/gif");

        if (koktailService.getPhoto(id) != null) {
            response.getOutputStream().write(koktailService.getPhoto(id));
        } else {
            try {
                byte[] bytes = Files.readAllBytes(Paths.get(Objects.requireNonNull(this.getClass().getClassLoader().getResource("static/img/fotka.jpg")).toURI()));
                response.getOutputStream().write(bytes);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
        response.getOutputStream().close();

    }

    @GetMapping("/bar")
    public String chooseBar(Model model) {

        model.addAttribute("bars", barekRepo.findAll());
        model.addAttribute("bar", new Barek());

        return "/barowe/barChoice";
    }

    @PostMapping("/bar")
    public String newBar( @ModelAttribute("bar") Barek barek) {

        barek = skladnikService.saveBarek(barek);
        return "redirect:/bar/" + barek.getId();
    }


    @GetMapping("/bar/{id}")
    public String barManager(Model model, @PathVariable("id") long id) {


        if (barekRepo.findById(id).isPresent()) {
            Barek barek = barekRepo.findById(id).get();

            model.addAttribute("skladnikP", new SkladnikP());
            model.addAttribute("barek", barek);
            model.addAttribute("sokList", skladnikService.getSoksToAdd(barek));
            model.addAttribute("syropList", skladnikService.getSyropsToAdd(barek));
            model.addAttribute("innyList", skladnikService.getInnyToAdd(barek));
            model.addAttribute("barBottles", skladnikService.getBarButlaForms(id));

            //do dodawania
            model.addAttribute("alkoList", alkoholRepo.findAll());
            model.addAttribute("typList", typRepo.findAll());
            model.addAttribute("butlaList", skladnikService.getAllbutlaFormsNotBarek(id));

            return "/barowe/barManager";
        }

        return "redirect:/przegladaj";
    }

    @PostMapping(value = "/bar/{id}", params = "dodaj")
    public String barDodaj(@ModelAttribute("skladnikP") SkladnikP skladnikP, @PathVariable("id") long id) {

        skladnikService.addToBar(skladnikP, id);
        return "redirect:/bar/" + id;
    }

    @PostMapping(value = "/bar/{id}", params = "dodajBottle")
    public String barDodajBottle(@ModelAttribute("skladnikP") SkladnikP skladnikP, @PathVariable("id") long id) {

//        System.out.println(skladnikP);
        skladnikService.addBottleToBar(skladnikP.getId(), id);
        return "redirect:/bar/" + id;
    }


    @GetMapping(value = "/{barID}/sok/delete/{id}")
    public String sokDelete(@PathVariable("id") Long id, @PathVariable("barID") Long barid) {
        skladnikService.deleteSokFromBar(id, barid);
        return "redirect:/bar/" + barid;
    }

    @GetMapping(value = "/{barID}/syrop/delete/{id}")
    public String syropDelete(@PathVariable("id") Long id, @PathVariable("barID") Long barid) {
        skladnikService.deleteSyropFromBar(id, barid);
        return "redirect:/bar/" + barid;
    }

    @GetMapping(value = "/{barID}/inny/delete/{id}")
    public String innyDelete(@PathVariable("id") Long id, @PathVariable("barID") Long barid) {
        skladnikService.deleteInnyFromBar(id, barid);
        return "redirect:/bar/" + barid;
    }


    @GetMapping(value = "/skladniki")
    public String skladnikManager(Model model) {

        model.addAttribute("skladnikP", new SkladnikP());
        model.addAttribute("sokList", sokRepo.findAll());
        model.addAttribute("syropList", syropRepo.findAll());
        model.addAttribute("innyList", innyRepo.findAll());

        model.addAttribute("alkoList", alkoholRepo.findAll());
        model.addAttribute("typList", typRepo.findAll());
        model.addAttribute("butlaList", skladnikService.getAllbutlaForms());
        return "/barowe/skladnikManager";
    }

    @PostMapping(value = "/skladniki", params = "dodaj")
    public String skladnikDodaj(@ModelAttribute("skladnikP") SkladnikP skladnikP) {

        skladnikService.saveSkladnik(skladnikP);


        return "redirect:/skladniki";
    }

    @GetMapping(value = "/skladniki/dodajbutle")
    public String createBottle(Model model) {

        model.addAttribute("skladnikList", alkoholRepo.findAll());
        model.addAttribute("alkoList", alkoholRepo.findAll());
        model.addAttribute("butelka", new Butelka());
        return "/butelkaAdd";
    }

    @PostMapping(value = "/skladniki/dodajbutle", params = "dodaj")
    public String addBottle(@ModelAttribute("butelka") Butelka butelka) {

        butelkaRepo.save(mapper.butlaToBase(butelka));

        return "redirect:/skladniki/dodajbutle";
    }


}
