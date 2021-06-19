package pl.mrozek.inzynierka.Controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.mrozek.inzynierka.Dto.SkladnikP;
import pl.mrozek.inzynierka.Entity.bar.Butelka;
import pl.mrozek.inzynierka.Repo.AlkoholRepo;
import pl.mrozek.inzynierka.Repo.ButelkaRepo;
import pl.mrozek.inzynierka.Services.SkladnikService;
import pl.mrozek.inzynierka.mapper.Mapper;

@Controller
@RequestMapping("/skladniki")
public class SkladnikController {
    private final SkladnikService skladnikService;
    private final AlkoholRepo alkoholRepo;
    private final ButelkaRepo butelkaRepo;
    private final Mapper mapper;

    public SkladnikController(SkladnikService skladnikService, AlkoholRepo alkoholRepo, ButelkaRepo butelkaRepo, Mapper mapper) {
        this.skladnikService = skladnikService;
        this.alkoholRepo = alkoholRepo;
        this.butelkaRepo = butelkaRepo;
        this.mapper = mapper;
    }


    @GetMapping(value = "")
    public String skladnikManager(Model model) {
        return skladnikService.completeSkladnikiModel(model, 0);
    }

    @PostMapping(value = "", params = "dodaj")
    public String skladnikDodaj(Model model, @ModelAttribute("skladnikP") SkladnikP skladnikP) {

        if (skladnikP.getNazwa() != null && !skladnikP.getNazwa().equals("")) {
            skladnikService.saveSkladnik(skladnikP);
        }
        return skladnikService.completeSkladnikiModel(model, skladnikP.getRodzaj());
    }

    @PostMapping(value = "", params = "usunButla")
    public String bottleDelete(Model model, @RequestParam long usunButla, @ModelAttribute("skladnikP") SkladnikP skladnikP) {

        if (skladnikService.deleteBottle(usunButla)) {
            return skladnikService.completeSkladnikiModel(model, skladnikP.getRodzaj());
        }
        return "redirect:/skladniki";
    }

    @PostMapping(value = "", params = "usunSok")
    public String sokDelete(Model model, @RequestParam long usunSok, @ModelAttribute("skladnikP") SkladnikP skladnikP) {

        if (skladnikService.deleteSok(usunSok)) {
            return skladnikService.completeSkladnikiModel(model, skladnikP.getRodzaj());
        }
        return "redirect:/skladniki";
    }

    @PostMapping(value = "", params = "usunSyrop")
    public String syropDelete(Model model, @RequestParam long usunSyrop, @ModelAttribute("skladnikP") SkladnikP skladnikP) {

        if (skladnikService.deleteSyrop(usunSyrop)) {
            return skladnikService.completeSkladnikiModel(model, skladnikP.getRodzaj());
        }
        return "redirect:/skladniki";
    }

    @PostMapping(value = "", params = "usunInny")
    public String innyDelete(Model model, @RequestParam long usunInny, @ModelAttribute("skladnikP") SkladnikP skladnikP) {

        if (skladnikService.deleteInny(usunInny)) {
            return skladnikService.completeSkladnikiModel(model, skladnikP.getRodzaj());
        }
        return "redirect:/skladniki";
    }


    @GetMapping(value = "/dodajbutle")
    public String createBottle(Model model) {

        model.addAttribute("skladnikList", alkoholRepo.findAll());
        model.addAttribute("alkoList", alkoholRepo.findAll());
        model.addAttribute("butelka", new Butelka());
        return "butelkaAdd";
    }

    @PostMapping(value = "/dodajbutle", params = "dodaj")
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

    @GetMapping(value = "/butelka/edit/{id}")
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

    @PostMapping(value = "/butelka/edit/{id}", params = "dodaj")
    public String editBottlePost(@PathVariable("id") long id, @ModelAttribute("butelka") Butelka butelka) {

        butelka.setId(id);
        butelkaRepo.save(mapper.butlaToBase(butelka));
        return "redirect:/skladniki";
    }


    @GetMapping(value = "/dodajbutle/{id}")
    public String createBottletoBar(Model model) {

        model.addAttribute("skladnikList", alkoholRepo.findAll());
        model.addAttribute("alkoList", alkoholRepo.findAll());
        model.addAttribute("butelka", new Butelka());
        return "butelkaAdd";
    }

    @PostMapping(value = "/dodajbutle/{id}", params = "dodaj")
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

    @GetMapping(value = "/edit/{id}")
    public String editSkladnik(Model model, @PathVariable("id") Long id) {

        SkladnikP skladnikP = mapper.toSkladnikP(id);
        model.addAttribute("skladnikP", skladnikP);
        return "skladniki/skladnikEditor";
    }

    @PostMapping(value = "/edit/{id}", params = "zapisz")
    public String saveEdit(Model model, @ModelAttribute("skladnikP") SkladnikP skladnikP) {
        skladnikService.editSkladnik(skladnikP);
        return skladnikService.completeSkladnikiModel(model, skladnikP.getRodzaj());
    }






}
