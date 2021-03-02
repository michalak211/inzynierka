package pl.mrozek.inzynierka.Controller;

import com.sun.org.apache.xpath.internal.operations.Mod;
import org.apache.commons.io.IOUtils;
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
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
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

    @GetMapping("/")
    public String tytulowa(){
        System.out.println("testy6 heroku main");
        return "tytulowa";

    }

    @GetMapping("/test")
    public String test() {

        System.out.println("testy heroku test");
//        if (butelkaRepo.findById((long)16).isPresent()) {
//            Butelka butelka= butelkaRepo.findById((long)16).get();
//            butelkaRepo.delete(butelka);
//        }
        return "redirect:/przegladaj";

    }

    @GetMapping("/przegladaj")
    public String przegladanie(Model model) {

        System.out.println("testy heroku przegladaj");

        model.addAttribute("alkoholList", alkoholRepo.findAll());
        model.addAttribute("typList", typRepo.findAll());
        model.addAttribute("koktajlList", koktailService.getAllUserForms());

        return "wyswietl";
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
//
//        System.out.println(Arrays.toString(koktailService.getPhoto(id)));
        if (koktailService.getPhoto(id) != null) {
            System.out.println("nie null");
            response.getOutputStream().write(koktailService.getPhoto(id));
        } else {
            InputStream inputStream= getClass().getResourceAsStream("static/img/fotka.jpg");
            System.out.println("try empty photo");
            byte[] bytes = IOUtils.toByteArray(inputStream);

//                byte[] bytes = Files.readAllBytes(Paths.get(Objects.requireNonNull(this.getClass().getClassLoader().getResource("static/img/fotka.jpg")).toURI()));
            response.getOutputStream().write(bytes);
        }
        response.getOutputStream().close();

    }

    @GetMapping("/bar")
    public String chooseBar(Model model) {

        System.out.println("jestem w bar");
        model.addAttribute("bars", barekRepo.findAll());
        model.addAttribute("bar", new Barek());

        return "barowe/barChoice";
    }

    @PostMapping("/bar")
    public String newBar(@ModelAttribute("bar") Barek barek) {

        barek = skladnikService.saveBarek(barek);
        return "redirect:/bar/" + barek.getId();
    }


    @GetMapping("/bar/{id}")
    public String barManager(Model model, @PathVariable("id") long id) {


        if (barekRepo.findById(id).isPresent()) {
            Barek barek = barekRepo.findById(id).get();
            return skladnikService.completeBarModel(model, barek, 0);
        }

        return "redirect:/bar";
    }

    @PostMapping(value = "/bar/{id}", params = "dodaj")
    public String barDodajSkladnik(Model model, @ModelAttribute("skladnikP") SkladnikP skladnikP, @PathVariable("id") long id) {

        skladnikService.addToBar(skladnikP, id);

        if (barekRepo.findById(id).isPresent()) {
            Barek barek = barekRepo.findById(id).get();
            return skladnikService.completeBarModel(model, barek, skladnikP.getRodzaj());
        }

        return "redirect:/bar";
    }

    @PostMapping(value = "/bar/{id}", params = "dodajBottle")
    public String barDodajBottle(Model model, @ModelAttribute("skladnikP") SkladnikP skladnikP, @PathVariable("id") long id) {

        skladnikService.addBottleToBar(skladnikP.getId(), id);
        if (barekRepo.findById(id).isPresent()) {
            Barek barek = barekRepo.findById(id).get();
            return skladnikService.completeBarModel(model, barek, 1);
        }

        return "redirect:/bar";
    }


    @GetMapping(value = "/{barID}/sok/delete/{id}")
    public String sokDelete(Model model, @PathVariable("id") Long id, @PathVariable("barID") Long barid) {
        skladnikService.deleteSokFromBar(id, barid);
        if (barekRepo.findById(id).isPresent()) {
            Barek barek = barekRepo.findById(id).get();
            return skladnikService.completeBarModel(model, barek, 2);
        }


        return "redirect:/bar/" + barid;
    }

    @GetMapping(value = "/{barID}/syrop/delete/{id}")
    public String syropDelete(Model model, @PathVariable("id") Long id, @PathVariable("barID") Long barid) {
        skladnikService.deleteSyropFromBar(id, barid);
        if (barekRepo.findById(id).isPresent()) {
            Barek barek = barekRepo.findById(id).get();
            return skladnikService.completeBarModel(model, barek, 2);
        }

        return "redirect:/bar/" + barid;
    }

    @GetMapping(value = "/{barID}/inny/delete/{id}")
    public String innyDelete(Model model, @PathVariable("id") Long id, @PathVariable("barID") Long barid) {
        skladnikService.deleteInnyFromBar(id, barid);
        if (barekRepo.findById(id).isPresent()) {
            Barek barek = barekRepo.findById(id).get();
            return skladnikService.completeBarModel(model, barek, 2);
        }

        return "redirect:/bar/" + barid;
    }


    @GetMapping(value = "/skladniki")
    public String skladnikManager(Model model) {

        return skladnikService.completeSkladnikiModel(model, 0);
    }

    @PostMapping(value = "/skladniki", params = "dodaj")
    public String skladnikDodaj(Model model, @ModelAttribute("skladnikP") SkladnikP skladnikP) {

        skladnikService.saveSkladnik(skladnikP);
        return skladnikService.completeSkladnikiModel(model, skladnikP.getRodzaj());
    }

    @GetMapping(value = "/skladniki/dodajbutle")
    public String createBottle(Model model) {

        model.addAttribute("skladnikList", alkoholRepo.findAll());
        model.addAttribute("alkoList", alkoholRepo.findAll());
        model.addAttribute("butelka", new Butelka());
        return "butelkaAdd";
    }

    @PostMapping(value = "/skladniki/dodajbutle", params = "dodaj")
    public String addBottle(Model model,@ModelAttribute("butelka") Butelka butelka) {


        if (butelkaRepo.findByNazwaEquals(butelka.getNazwa())==null) {
            butelkaRepo.save(mapper.butlaToBase(butelka));
        }else {
            model.addAttribute("skladnikList", alkoholRepo.findAll());
            model.addAttribute("alkoList", alkoholRepo.findAll());
            model.addAttribute("butelka", butelka);
            return "butelkaAdd";
        }


        return "redirect:/skladniki/dodajbutle";
    }

    @GetMapping(value = "/skladniki/dodajbutle/{id}")
    public String createBottletoBar(Model model, @PathVariable("id") Long id) {

        model.addAttribute("skladnikList", alkoholRepo.findAll());
        model.addAttribute("alkoList", alkoholRepo.findAll());
        model.addAttribute("butelka", new Butelka());
        return "butelkaAdd";
    }

    @PostMapping(value = "/skladniki/dodajbutle/{id}", params = "dodaj")
    public String addBottletoBar(Model model,@ModelAttribute("butelka") Butelka butelka, @PathVariable("id") Long id) {

        if (butelkaRepo.findByNazwaEquals(butelka.getNazwa())==null) {
            butelkaRepo.save(mapper.butlaToBase(butelka));
            Butelka butelka1 = butelkaRepo.findByNazwaEquals(butelka.getNazwa());
            skladnikService.addBottleToBar(butelka1.getId(), id);
        }else {
            model.addAttribute("skladnikList", alkoholRepo.findAll());
            model.addAttribute("alkoList", alkoholRepo.findAll());
            model.addAttribute("butelka", butelka);
            return "butelkaAdd";
        }

        //no i zobaczymy
        return "redirect:/skladniki/dodajbutle/" + id;
    }


}
