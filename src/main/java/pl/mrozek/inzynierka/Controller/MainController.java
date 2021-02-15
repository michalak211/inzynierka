package pl.mrozek.inzynierka.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import pl.mrozek.inzynierka.Dto.SkladnikP;
import pl.mrozek.inzynierka.Entity.bar.Barek;
import pl.mrozek.inzynierka.Entity.skladniki.Sok;
import pl.mrozek.inzynierka.Entity.skladniki.Syrop;
import pl.mrozek.inzynierka.Repo.*;
import pl.mrozek.inzynierka.Service.KoktailService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

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


    public MainController(KoktailService koktailService, AlkoholRepo alkoholRepo, TypRepo typRepo, KoktailRepo koktailRepo, SokRepo sokRepo, SyropRepo syropRepo, InnyRepo innyRepo, BarekRepo barekRepo) {
        this.koktailService = koktailService;
        this.alkoholRepo = alkoholRepo;
        this.typRepo = typRepo;
        this.koktailRepo = koktailRepo;
        this.sokRepo = sokRepo;
        this.syropRepo = syropRepo;
        this.innyRepo = innyRepo;
        this.barekRepo = barekRepo;
    }

    @GetMapping("/test")
    public void test() {
        koktailService.addKoktajl();
    }

    @GetMapping("/przegladaj")
    public String przegladanie(Model model) {


        model.addAttribute("alkoholList", alkoholRepo.findAll());
        model.addAttribute("typList", typRepo.findAll());
        model.addAttribute("koktajlList", koktailService.getAllUserForms());
        return "/wyswietl";
    }


    @Transactional
    @PostMapping(value = "/dodZdj" ,consumes = {"multipart/form-data"})
    public String addPicture(Model model, @RequestParam String zdjecie,
                             @RequestParam(required=false) Map<String, String> allParams,
                             HttpServletRequest request
    ) {

        long id=Integer.parseInt(zdjecie);
        Map<String, MultipartFile> fileMap = new HashMap<String, MultipartFile>();
        if (request instanceof MultipartHttpServletRequest) {
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
            fileMap = multiRequest.getFileMap();
        }

        for (Map.Entry<String, MultipartFile> entry: fileMap.entrySet()){
            if (id==Integer.parseInt(entry.getKey())){
                koktailService.addPictureToKoktajl(entry.getValue(),id);
                break;
            }
        }



        model.addAttribute("alkoholList", alkoholRepo.findAll());
        model.addAttribute("typList", typRepo.findAll());
        model.addAttribute("koktajlList", koktailService.getAllUserForms());
        return "redirect:/przegladaj";

    }

    @GetMapping("/image/{id}")
    @ResponseBody
    void showImage(@PathVariable("id") Long id, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("image/jpeg, image/jpg, image/png, image/gif");

        if (koktailService.getPhoto(id)!=null) { response.getOutputStream().write(koktailService.getPhoto(id)); }
        else {try {
                byte[] bytes = Files.readAllBytes(Paths.get(this.getClass().getClassLoader().getResource("static/img/fotka.jpg").toURI()));
                response.getOutputStream().write(bytes);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }

        }
        response.getOutputStream().close();

    }

    @GetMapping("/bar")
    public String barManager(Model model){


        if (barekRepo.findById((long)54).isPresent()){
            Barek barek=barekRepo.findById((long)54).get();
            model.addAttribute("barek",barek);
        }

        // zmiana z find all na serwice nid all nie w barklu
        model.addAttribute("skladnikP",new SkladnikP());
        model.addAttribute("sokList",sokRepo.findAll());
        model.addAttribute("syropList",syropRepo.findAll());
        model.addAttribute("innyList",innyRepo.findAll());
        model.addAttribute("skladnikList", alkoholRepo.findAll());
        model.addAttribute("typList", typRepo.findAll());

        return "/barowe/barManager";
    }

    @PostMapping(value ="/bar", params = "dodaj")
    public String barDodaj(@ModelAttribute ("skladnikP")SkladnikP skladnikP){

        System.out.println(skladnikP);
        if (barekRepo.findById((long)54).isPresent()){
            Barek barek=barekRepo.findById((long)54).get();

            switch (skladnikP.getRodzaj()){
                case 1:
                    break;
                case 2:
                    if (sokRepo.findById(skladnikP.getId()).isPresent()) {
                        Sok sok=sokRepo.findById(skladnikP.getId()).get();

                        if (!barek.getListSok().contains(sok)) {
                            barek.getListSok().add(sok);
                            barekRepo.save(barek);
                        }
                    }
                    break;
                case 3:

                    if (syropRepo.findById(skladnikP.getId()).isPresent()) {
                        Syrop syrop =syropRepo.findById(skladnikP.getId()).get();
                        if (!barek.getListSyrop().contains(syrop)) {
                            barek.getListSyrop().add(syrop);
                            barekRepo.save(barek);
                        }
                    }


                    break;
                case 4:
                    break;
            }
        }
        return "redirect:/bar";
    }


    @GetMapping(value = "/sok/delete/{id}")
    public String sokDelete(@PathVariable("id") Long id){

        if (barekRepo.findById((long)54).isPresent()){
            Barek barek=barekRepo.findById((long)54).get();
            int inte= (int) (id-1);
            barek.getListSok().remove(inte);
            barekRepo.save(barek);
            }
        return "redirect:/bar";
    }

    @GetMapping(value = "/syrop/delete/{id}")
    public String syropDelete(@PathVariable("id") Long id){

        if (barekRepo.findById((long)54).isPresent()){
            Barek barek=barekRepo.findById((long)54).get();
            int inte= (int) (id-1);
            barek.getListSyrop().remove(inte);
            barekRepo.save(barek);
        }
        return "redirect:/bar";
    }


    @GetMapping(value ="/skladniki")
    public String skladnikManager(Model model){




        model.addAttribute("skladnikP",new SkladnikP());
        model.addAttribute("sokList",sokRepo.findAll());
        model.addAttribute("syropList",syropRepo.findAll());
        model.addAttribute("innyList",innyRepo.findAll());
        model.addAttribute("skladnikList", alkoholRepo.findAll());
        model.addAttribute("typList", typRepo.findAll());
        return "/barowe/skladnikManager";
    }

    @PostMapping(value ="/skladniki", params = "dodaj")
    public String skladnikDodaj(@ModelAttribute ("skladnikP")SkladnikP skladnikP){

        System.out.println(skladnikP);
            switch (skladnikP.getRodzaj()){
                case 1:
                    break;
                case 2:
                    if (sokRepo.findByNazwaEquals(skladnikP.getNazwa())==null) {
                        //else strona bledu?
                        Sok sok= new Sok();
                        sok.setNazwa(skladnikP.getNazwa());
                        sok.setCenaZaLitr(skladnikP.getIloscML());
                        sokRepo.save(sok);

                    }
                    break;
                case 3:

                    if (syropRepo.findByNazwaEquals(skladnikP.getNazwa())==null) {

                        Syrop syrop= new Syrop();
                        syrop.setNazwa(skladnikP.getNazwa());
                        syrop.setCenaZaLitr(skladnikP.getIloscML());
                        if (skladnikP.getOpisDodatkowy()!=null) {syrop.setPrzepis(skladnikP.getOpisDodatkowy());}
                        syropRepo.save(syrop);


                    }


                    break;
                case 4:
                    break;
            }

        return "redirect:/bar";
    }

}
