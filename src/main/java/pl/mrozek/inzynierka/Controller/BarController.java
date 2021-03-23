package pl.mrozek.inzynierka.Controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.mrozek.inzynierka.Dto.SkladnikP;
import pl.mrozek.inzynierka.Entity.bar.Barek;
import pl.mrozek.inzynierka.Repo.BarekRepo;
import pl.mrozek.inzynierka.Service.SkladnikService;

@Controller
@RequestMapping("/bar")
public class BarController {
    private final BarekRepo barekRepo;
    private final SkladnikService skladnikService;

    public BarController(BarekRepo barekRepo, SkladnikService skladnikService) {
        this.barekRepo = barekRepo;
        this.skladnikService = skladnikService;
    }


    @GetMapping("")
    public String chooseBar(Model model) {

        model.addAttribute("bars", barekRepo.findAll());
        model.addAttribute("bar", new Barek());

        return "barowe/barChoice";
    }

    @PostMapping("")
    public String newBar(@ModelAttribute("bar") Barek barek) {

        barek = skladnikService.saveBarek(barek);
        return "redirect:/bar/" + barek.getId();
    }

    @GetMapping("/{id}")
    public String barManager(Model model, @PathVariable("id") long id) {

        if (barekRepo.findById(id).isPresent()) {
            Barek barek = barekRepo.findById(id).get();
            return skladnikService.completeBarModel(model, barek, 0);
        }

        return "redirect:/bar";
    }

    @PostMapping(value = "/{id}", params = "dodajBottle")
    public String barDodajBottle(Model model, @ModelAttribute("skladnikP") SkladnikP skladnikP, @PathVariable("id") long id) {

        skladnikService.addBottleToBar(skladnikP.getId(), id);
        if (barekRepo.findById(id).isPresent()) {
            Barek barek = barekRepo.findById(id).get();
            return skladnikService.completeBarModel(model, barek, 1);
        }
        return "redirect:/bar";
    }

    @PostMapping(value = "/{id}", params = "dodaj")
    public String barDodajSkladnik(Model model, @ModelAttribute("skladnikP") SkladnikP skladnikP, @PathVariable("id") long id) {

        skladnikService.addToBar(skladnikP, id);
        if (barekRepo.findById(id).isPresent()) {
            Barek barek = barekRepo.findById(id).get();
            return skladnikService.completeBarModel(model, barek, skladnikP.getRodzaj());
        }
        return "redirect:/bar";
    }


    @PostMapping(value = "/{id}", params = "usunButla")
    public String bottleDelete(Model model,@RequestParam long usunButla, @ModelAttribute("skladnikP") SkladnikP skladnikP, @PathVariable("id") long id) {

        skladnikService.deleteBottleFromBar(usunButla, id);
        if (barekRepo.findById(id).isPresent()) {
            Barek barek = barekRepo.findById(id).get();
            return skladnikService.completeBarModel(model, barek, skladnikP.getRodzaj());
        }
        return "redirect:/bar";
    }


    @PostMapping(value = "/{id}", params = "usunSok")
    public String sokDelete(Model model,@RequestParam long usunSok, @ModelAttribute("skladnikP") SkladnikP skladnikP, @PathVariable("id") long id) {

        skladnikService.deleteSokFromBar(usunSok, id);
        if (barekRepo.findById(id).isPresent()) {
            Barek barek = barekRepo.findById(id).get();
            return skladnikService.completeBarModel(model, barek, skladnikP.getRodzaj());
        }
        return "redirect:/bar";
    }

    @PostMapping(value = "/{id}", params = "usunSyrop")
    public String syropDelete(Model model,@RequestParam long usunSyrop, @ModelAttribute("skladnikP") SkladnikP skladnikP, @PathVariable("id") long id) {

        System.out.println("usun syrop");
        skladnikService.deleteSyropFromBar(usunSyrop, id);
        if (barekRepo.findById(id).isPresent()) {
            Barek barek = barekRepo.findById(id).get();
            return skladnikService.completeBarModel(model, barek, skladnikP.getRodzaj());
        }
        return "redirect:/bar";
    }


    @PostMapping(value = "/{id}", params = "usunInny")
    public String innyDelete(Model model,@RequestParam long usunInny, @ModelAttribute("skladnikP") SkladnikP skladnikP, @PathVariable("id") long id) {

        skladnikService.deleteInnyFromBar(usunInny, id);
        if (barekRepo.findById(id).isPresent()) {
            Barek barek = barekRepo.findById(id).get();
            return skladnikService.completeBarModel(model, barek, skladnikP.getRodzaj());
        }
        return "redirect:/bar";
    }




}
