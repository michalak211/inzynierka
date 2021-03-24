package pl.mrozek.inzynierka.Controller;


import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.mrozek.inzynierka.Dto.SkladnikP;
import pl.mrozek.inzynierka.Entity.bar.Barek;
import pl.mrozek.inzynierka.Entity.bar.Butelka;
import pl.mrozek.inzynierka.Entity.przepis.Alkohol;
import pl.mrozek.inzynierka.Entity.skladniki.Typ;
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
import java.util.List;


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
    public String tytulowa() {
        System.out.println("testy6 heroku main");
        return "tytulowa";

    }

    @GetMapping("/test")
    public String test() {

//        System.out.println("testy heroku test");
//        if (butelkaRepo.findById((long)16).isPresent()) {
//            Butelka butelka= butelkaRepo.findById((long)16).get();
//            butelkaRepo.delete(butelka);
//        }

//        butelkaRepo.deleteById((long)31);

//        List<Typ> typList= (List<Typ>) typRepo.findAll();
//        for (Typ typ:typList){
//            if (typ.getAlkoholID()==2){
//                long tempid=typ.getId();
//                Typ typ1=new Typ();
//                typ1.setId(tempid);
//                typRepo.save(typ1);
//
//                typRepo.delete(typ1);
//            }
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

        if (koktailService.getPhoto(id) != null) {
            response.getOutputStream().write(koktailService.getPhoto(id));
        } else {
            InputStream inputStream = getClass().getResourceAsStream("/static/img/fotka.jpg");
            byte[] bytes = IOUtils.toByteArray(inputStream);
            response.getOutputStream().write(bytes);
        }
        response.getOutputStream().close();

    }


    @GetMapping(value = "/skladniki")
    public String skladnikManager(Model model) {

        return skladnikService.completeSkladnikiModel(model, 0);
    }

    @PostMapping(value = "/skladniki", params = "dodaj")
    public String skladnikDodaj(Model model, @ModelAttribute("skladnikP") SkladnikP skladnikP) {

        if (skladnikP.getNazwa() != null && !skladnikP.getNazwa().equals("")) {
            skladnikService.saveSkladnik(skladnikP);
        }
        return skladnikService.completeSkladnikiModel(model, skladnikP.getRodzaj());
    }

    @PostMapping(value = "/skladniki", params = "usunButla")
    public String bottleDelete(Model model, @RequestParam long usunButla, @ModelAttribute("skladnikP") SkladnikP skladnikP) {

        if (skladnikService.deleteBottle(usunButla)) {
            return skladnikService.completeSkladnikiModel(model, skladnikP.getRodzaj());
        }
        return "redirect:/skladniki";
    }

    @PostMapping(value = "/skladniki", params = "usunSok")
    public String sokDelete(Model model, @RequestParam long usunSok, @ModelAttribute("skladnikP") SkladnikP skladnikP) {

        if (skladnikService.deleteSok(usunSok)) {
            return skladnikService.completeSkladnikiModel(model, skladnikP.getRodzaj());
        }
        return "redirect:/skladniki";
    }

    @PostMapping(value = "/skladniki", params = "usunSyrop")
    public String syropDelete(Model model, @RequestParam long usunSyrop, @ModelAttribute("skladnikP") SkladnikP skladnikP) {

        if (skladnikService.deleteSyrop(usunSyrop)) {
            return skladnikService.completeSkladnikiModel(model, skladnikP.getRodzaj());
        }
        return "redirect:/skladniki";
    }

    @PostMapping(value = "/skladniki", params = "usunInny")
    public String innyDelete(Model model, @RequestParam long usunInny, @ModelAttribute("skladnikP") SkladnikP skladnikP) {

        if (skladnikService.deleteInny(usunInny)) {
            return skladnikService.completeSkladnikiModel(model, skladnikP.getRodzaj());
        }
        return "redirect:/skladniki";
    }

    @GetMapping(value = "/skladniki/dodajbutle")
    public String createBottle(Model model) {

        model.addAttribute("skladnikList", alkoholRepo.findAll());
        model.addAttribute("alkoList", alkoholRepo.findAll());
        model.addAttribute("butelka", new Butelka());
        return "butelkaAdd";
    }

    @PostMapping(value = "/skladniki/dodajbutle", params = "dodaj")
    public String addBottle(Model model, @ModelAttribute("butelka") Butelka butelka) {

        if (butelkaRepo.findByNazwaEquals(butelka.getNazwa()) == null) {
            butelkaRepo.save(mapper.butlaToBase(butelka));
            return skladnikService.completeSkladnikiModel(model, 1);
        } else {
            model.addAttribute("skladnikList", alkoholRepo.findAll());
            model.addAttribute("alkoList", alkoholRepo.findAll());
            model.addAttribute("butelka", butelka);
            return "butelkaAdd";
        }
    }

    @GetMapping(value = "/skladniki/butelka/edit/{id}")
    public String editBottle(Model model, @PathVariable("id") long id) {

        if (butelkaRepo.findById(id).isPresent()) {
            Butelka butelka = butelkaRepo.findById(id).get();
            model.addAttribute("skladnikList", alkoholRepo.findAll());
            model.addAttribute("alkoList", alkoholRepo.findAll());
            model.addAttribute("butelka", butelka);
            return "butelkaAdd";
        }
        return "redirect:/skladniki";
    }

    @PostMapping(value = "/skladniki/butelka/edit/{id}", params = "dodaj")
    public String editBottlePost(Model model, @PathVariable("id") long id, @ModelAttribute("butelka") Butelka butelka) {

        butelka.setId(id);
        butelkaRepo.save(mapper.butlaToBase(butelka));
//        return skladnikService.completeSkladnikiModel(model, 1);


        return "redirect:/skladniki";
    }


    @GetMapping(value = "/skladniki/dodajbutle/{id}")
    public String createBottletoBar(Model model, @PathVariable("id") Long id) {

        model.addAttribute("skladnikList", alkoholRepo.findAll());
        model.addAttribute("alkoList", alkoholRepo.findAll());
        model.addAttribute("butelka", new Butelka());
        return "butelkaAdd";
    }

    @PostMapping(value = "/skladniki/dodajbutle/{id}", params = "dodaj")
    public String addBottletoBar(Model model, @ModelAttribute("butelka") Butelka butelka, @PathVariable("id") Long id) {

        if (butelkaRepo.findByNazwaEquals(butelka.getNazwa()) == null) {
            butelkaRepo.save(mapper.butlaToBase(butelka));
            Butelka butelka1 = butelkaRepo.findByNazwaEquals(butelka.getNazwa());
            skladnikService.addBottleToBar(butelka1.getId(), id);
        } else {
            model.addAttribute("skladnikList", alkoholRepo.findAll());
            model.addAttribute("alkoList", alkoholRepo.findAll());
            model.addAttribute("butelka", mapper.butlaToForm(butelka));
            return "butelkaAdd";
        }

        return "redirect:/skladniki/dodajbutle/" + id;
    }

    @GetMapping(value = "/skladniki/edit/{id}")
    public String editSkladnik(Model model, @PathVariable("id") Long id) {

        SkladnikP skladnikP = mapper.toSkladnikP(id);
        model.addAttribute("skladnikP", skladnikP);
        return "skladniki/skladnikEditor";
    }

    @PostMapping(value = "/skladniki/edit/{id}", params = "zapisz")
    public String saveEdit(Model model, @ModelAttribute("skladnikP") SkladnikP skladnikP) {
        skladnikService.editSkladnik(skladnikP);
        return skladnikService.completeSkladnikiModel(model, skladnikP.getRodzaj());
    }


    @GetMapping(value = "/struktura")
    public String struktura(Model model) {

        model.addAttribute("alkoList", alkoholRepo.findAll());
//        for (Alkohol alkohol:alkoholRepo.findAll()){
//            System.out.println(alkohol.getNazwa());
//        }

        return "struktura";
    }

    @GetMapping(value = "/struktura/edit/{id}")
    public String strukturaEdit(Model model, @PathVariable("id") Long id) {

        if (alkoholRepo.findById(id).isPresent()) {
            Alkohol alkohol = alkoholRepo.findById(id).get();
            model.addAttribute("alkohol", alkohol);
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
                return "strukturaEdit";
            }
        }
        return "redirect:/struktura/edit/" + id;
    }

    @PostMapping(value = "/struktura/edit/{id}", params = "dodaj")
    public String addTyp(Model model, @RequestParam String nowyTyp, @PathVariable("id") Long id) {

        skladnikService.addTyp(id,nowyTyp);
        return "redirect:/struktura/edit/" + id;
    }

}
