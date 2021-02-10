package pl.mrozek.inzynierka.Controller;

import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import pl.mrozek.inzynierka.Entity.przepis.Koktajl;
import pl.mrozek.inzynierka.Repo.AlkoholRepo;
import pl.mrozek.inzynierka.Repo.TypRepo;
import pl.mrozek.inzynierka.Service.KoktailService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ClassPathResource;


import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@RequestMapping("")
public class MainController {

    final
    KoktailService koktailService;
    private final AlkoholRepo alkoholRepo;
    private final TypRepo typRepo;

    public MainController(KoktailService koktailService, AlkoholRepo alkoholRepo, TypRepo typRepo) {
        this.koktailService = koktailService;
        this.alkoholRepo = alkoholRepo;
        this.typRepo = typRepo;
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
//        System.out.println("controller");
//        System.out.println(koktailService.getAllUserForms());
        return "/wyswietl";
    }


    @Transactional
    @PostMapping(value = "/dodZdj" ,consumes = {"multipart/form-data"})
    public String addPicture(Model model, @RequestParam String zdjecie,
//                             @RequestParam(value = "uploadFile",required=false) MultipartFile file,
                             @RequestParam(required=false) Map<String, String> allParams,
                             HttpServletRequest request
//                             @PathVariable Long id
    ) {

        System.out.println(zdjecie);
        long id=Integer.parseInt(zdjecie);

        Map<String, MultipartFile> fileMap = new HashMap<String, MultipartFile>();
        if (request instanceof MultipartHttpServletRequest) {
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
            fileMap = multiRequest.getFileMap();
        }

        for (Map.Entry<String, MultipartFile> entry: fileMap.entrySet()){
            System.out.println(entry.getKey());
            System.out.println(entry.getValue().getOriginalFilename());

            if (id==Integer.parseInt(entry.getKey())){
                System.out.println("dziala kuffa!");
//                koktailService.addPictureToKoktajl(entry.getValue(),id);
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
        System.out.println("test");
        System.out.println(id);
        response.setContentType("image/jpeg, image/jpg, image/png, image/gif");


        if (koktailService.getPhoto(id)!=null) {
            response.getOutputStream().write(koktailService.getPhoto(id));
        } else {

//            Resource resource= new ClassPathResource("res/fotka.jpg");
//            InputStream input = resource.getInputStream();
//            File file = resource.getFile();
//            response.getOutputStream().write();

            try {
                byte[] bytes = Files.readAllBytes(Paths.get(this.getClass().getClassLoader().getResource("res/fotka.jpg").toURI()));
                response.getOutputStream().write(bytes);

            } catch (URISyntaxException e) {
                e.printStackTrace();
            }

        }
        response.getOutputStream().close();

    }

}
