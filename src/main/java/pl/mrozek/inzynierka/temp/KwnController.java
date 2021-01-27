//package pl.mrozek.inzynierka.temp;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//import pl.pwpw.demo.KWN.Dto.KWNDto;
//import pl.pwpw.demo.KWN.Entity.KWN;
//import pl.pwpw.demo.KWN.Mapper.Mapper;
//import pl.pwpw.demo.KWN.Repo.KWNRepo;
//import pl.pwpw.demo.KWN.Service.KWNService;
//import pl.pwpw.demo.ogolne.Entity.Employee;
//import pl.pwpw.demo.ogolne.Entity.Select.Niezgodnosc;
//import pl.pwpw.demo.ogolne.ExcelGenerators.ExcelgeneralGenerator;
//import pl.pwpw.demo.ogolne.Repo.select.NiezgodnoscRepo;
//import pl.pwpw.demo.ogolne.Service.EmployeeService;
//import pl.pwpw.demo.ogolne.Service.LDAPService;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.transaction.Transactional;
//import java.io.IOException;
//import java.util.List;
//
//@Controller
//@RequestMapping("kwn")
//public class KwnController {
//
//    @Autowired
//    KWNService kwnService;
//    @Autowired
//    ExcelgeneralGenerator excelgeneralGenerator;
//    @Autowired
//    KWNRepo kwnRepo;
//    @Autowired
//    Mapper mapper;
//    @Autowired
//    EmployeeService employeeService;
//    @Autowired
//    LDAPService ldapService;
//    @Autowired
//    NiezgodnoscRepo niezgodnoscRepo;
//
//
//
//
//
//    @Transactional
//    @GetMapping("/add")
//    public String kwn(Model model) {
//        KWNDto formKWN = new KWNDto();
//        List<Niezgodnosc>niezgodnoscList= (List<Niezgodnosc>) niezgodnoscRepo.findAll();
//        List<Employee> employees = employeeService.getAllEmployee();
//        model.addAttribute("kwnForm", formKWN);
//        model.addAttribute("employees", employees);
//        model.addAttribute("niezgodnoscList",niezgodnoscList);
//        model.addAttribute("newKwn", 1);
//        return "/kwn/kwn";
//    }
//
//
//    @Transactional
//    @PostMapping("/add")
//    public String kwnSubmit(@ModelAttribute("kwnForm") KWNDto formKWN) {
//        kwnService.addKWN(formKWN);
//        return  "redirect:/archiwum/kwn";
//    }
//
//    @GetMapping(path = "/edit/{id}")
//    @Transactional
//    public String editKWN(@PathVariable Long id, Model model) {
//
//        List<Niezgodnosc>niezgodnoscList= (List<Niezgodnosc>) niezgodnoscRepo.findAll();
//        List<Employee> employees = employeeService.getAllEmployee();
//
//        model.addAttribute("employees", employees);
//        model.addAttribute("niezgodnoscList",niezgodnoscList);
//        model.addAttribute("kwnForm", kwnService.getKwnForm(id));
//        model.addAttribute("kwnid", id);
//        model.addAttribute("newKwn", 0);
//
//        return "/kwn/kwn";
//    }
//
//    @RequestMapping(value = "/edit/{id}", params = "zadania")
//    @Transactional
//    public String editTasks(Model model, @PathVariable Long id, @ModelAttribute("formKWN") KWNDto formKWN){
//        kwnService.editKWN(id, formKWN);
//
//        return "redirect:/task/kwn/editkwntasks/" + id;
//    }
//
//
//
//    @RequestMapping(path = "/edit/{id}",params = "zapisz")
//    @Transactional
//    public String editKWNPost(@PathVariable Long id, @ModelAttribute("formKWN") KWNDto formKWN, Model model) {
//        kwnService.editKWN(id, formKWN);
//        model.addAttribute("KWNList", kwnService.getAllKWN());
//        return  "redirect:/archiwum/kwn";
//    }
//
////    @PostMapping(path = "/edit/{id}")
////    public String editKWN(@ModelAttribute("formKWN") FormKWN formKWN, Model model) {
////        kwnService.addKWN(formKWN);
////        System.out.println(formKWN);
////        model.addAttribute("KWNList", kwnService.getAllKWN());
////        return "/archiwumkwn";
////    }
//
//
//    //
//    @GetMapping(path = "/{id}/meeting")
//    @Transactional
//    public String newMeetingKWN(@PathVariable Long id, Model model){
//        KWN newMeeting = kwnService.newMeeting(id);
//        newMeeting.setTaskList(null);
//        List<Niezgodnosc>niezgodnoscList= (List<Niezgodnosc>) niezgodnoscRepo.findAll();
//        List<Employee> employees = employeeService.getAllEmployee();
//        model.addAttribute("niezgodnoscList",niezgodnoscList);
//        model.addAttribute("kwnForm", mapper.toFormKWN(newMeeting));
//        model.addAttribute("employees", employees);
//        model.addAttribute("newKwn", 1);
//
//        return "/kwn/kwn";
//    }
//    //
//    @PostMapping(path = "/{id}/meeting")
//    @Transactional
//    public String newMeetingKWNPost(@ModelAttribute("formKWN") KWNDto formKWN, @PathVariable Long id, Model model){
////        KWN newMeeting = kwnService.newMeeting(id);
////        System.out.println("zapisuje");
////        kwnRepo.save(newMeeting);
//        kwnService.addKWN(formKWN);
////        model.addAttribute("kwnForm", Mapper.toFormKWN(newMeeting));
//        return "redirect:/archiwum/kwn";
//    }
//
//    @GetMapping("/archiwum")
//    public String archiwumkwn(Model model) {
//        model.addAttribute("KWNList", kwnService.getAllKWN());
//
//        return "/kwn/archiwumkwn";
//    }
//
//    @GetMapping("/{id}/download")
//    @Transactional
//    public String downloadKWNForm(@PathVariable Long id, HttpServletRequest request, HttpServletResponse response) throws IOException { try {
//            excelgeneralGenerator.fillKWNForm(id,request,response);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return "redirect:/archiwum/kwn";
//    }
//
//
//    @GetMapping(path = "/upload/{id}")
//    public String UploadKWNFormPDF(@PathVariable Long id, Model model) {
//        model.addAttribute("KWNToUpload", id);
//        return "/kwn/uploadKWN";
//    }
//
//
//    @PostMapping("/upload/{id}")
//    public String handleFileUpload(@RequestParam("uploadFile") MultipartFile file,
//                                   @PathVariable Long id, Model model) {
//
//        kwnService.saveFileKwnRap(file,id);
//        model.addAttribute("KWNList", kwnService.getAllKWN());
//        return "redirect:/archiwum/kwn";
//    }
//
//    @GetMapping("/files/{id1}/{id2}/delete")
//    public String fileKWNdelete(@PathVariable Long id1, @PathVariable Long id2, Model model, HttpServletRequest request, HttpServletResponse response) {
//        KWN kwn = kwnRepo.findOne(id1);
//
//        kwnService.deleteFileKWN(id1, id2);
//        model.addAttribute("accKwn", id1);
//        model.addAttribute("filesList", kwn.getFileList());
//
//
//        return "redirect:/archiwum/kwn/files/"+id1;
//    }
//
//
//
//
//
//
//}

//TODO
//tu jest kontroller do pobierania plkikow

//@GetMapping(path = "/upload/{id}")
//public String UploadRRormPDF(@PathVariable Long id, Model model) {
//        model.addAttribute("RRToUpload", id);
//        model.addAttribute("rr", rrRepo.getOne(id));
//
//        return "/rr/uploadRR";
//        }
//
//@PostMapping("/upload/{id}")
//public String handleFileUpload(@RequestParam("uploadFile") MultipartFile file,
//@PathVariable Long id, Model model) {
//        rrService.saveFileRRRap(file, id);
//        return "redirect:/archiwum/rr";
//        }


//TODO
//html do pobierania plikow

//<div>
//<br> <br>
//<form class="form-horizontal row-border" action="#" th:action="@{/rr/upload/} + ${RRToUpload}"
//        method="post"
//        enctype="multipart/form-data">
//<!--        <form method="POST" enctype="multipart/form-data" action="/">-->
//
//<h3>Dodaj nowy plik</h3>
//<br>
//<input type="file" name="uploadFile"/>
//<br>
//<input type="submit" value="Upload" class="btn btn-primary"/>
//
//</form>
//<br>
//<br>
//</div>