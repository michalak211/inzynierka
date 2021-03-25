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

    private final KoktailService koktailService;
    private final AlkoholRepo alkoholRepo;
    private final TypRepo typRepo;
    private final SkladnikService skladnikService;


    public MainController(KoktailService koktailService, AlkoholRepo alkoholRepo, TypRepo typRepo, SkladnikService skladnikService) {
        this.koktailService = koktailService;
        this.alkoholRepo = alkoholRepo;
        this.typRepo = typRepo;
        this.skladnikService = skladnikService;
    }

    @GetMapping("/")
    public String tytulowa() {
        System.out.println("testy6 heroku main");
        return "tytulowa";
    }

    @GetMapping("/test")
    public String test() {
        return "redirect:/przegladaj";
    }

    @GetMapping("/przegladaj")
    public String przegladanie(Model model) {
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


    @GetMapping(value = "/struktura")
    public String struktura(Model model) {
        model.addAttribute("alkoList", alkoholRepo.findAll());
        return "struktura";
    }

    @PostMapping(value = "/struktura", params = "dodaj")
    public String strukturaAdd(Model model,@RequestParam String nowyAlko) {

        skladnikService.addNewAlko(nowyAlko);
        model.addAttribute("alkoList", alkoholRepo.findAll());
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
