//package pl.mrozek.inzynierka.temp;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//import org.springframework.web.servlet.mvc.support.RedirectAttributes;
//import pl.pwpw.demo.KWN.Dto.KWNDto;
//import pl.pwpw.demo.KWN.Entity.KWN;
//import pl.pwpw.demo.KWN.Entity.Task;
//import pl.pwpw.demo.KWN.Mapper.Mapper;
//import pl.pwpw.demo.KWN.Repo.KWNRepo;
//import pl.pwpw.demo.KWN.Service.KWNService;
//import pl.pwpw.demo.KWN.filters.filterSetKWN;
//import pl.pwpw.demo.ogolne.Entity.Employee;
//import pl.pwpw.demo.ogolne.Entity.Status;
//import pl.pwpw.demo.ogolne.ExcelGenerators.ExcelgeneralGenerator;
//import pl.pwpw.demo.ogolne.Repo.select.OpisNiezgodnosciRepo;
//import pl.pwpw.demo.ogolne.Service.EmployeeService;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.transaction.Transactional;
//import java.util.List;
//
//@Controller
//@RequestMapping("archiwum")
//public class ArchivesController {
//
//    @Autowired
//    KWNService kwnService;
//    @Autowired
//    EmployeeService employeeService;
//    @Autowired
//    KWNRepo kwnRepo;
//    @Autowired
//    Mapper mapper;
//    @Autowired
//    ExcelgeneralGenerator excelgeneralGenerator;
//
//    @Autowired
//    OpisNiezgodnosciRepo opisNiezgodnosciRepo;
//
//
//    @GetMapping("/kwn")
//    public String archiwumkwn(Model model) {
//        filterSetKWN formKWN = new filterSetKWN();
//        kwnService.checkKwnStatus();
//
//        Employee employee = employeeService.getCurrentEmployee();
//        model.addAttribute("accEmployee", employee.getUserName());
//        model.addAttribute("opisNiezgodnoscis", opisNiezgodnosciRepo.findAll());
//
//
//        model.addAttribute("filterSetKWN", formKWN);
//        model.addAttribute("KWNList", kwnService.getAllEmployeeKWN());
//
//        return "/kwn/archiwumkwn";
//    }
//
//
//    @RequestMapping(value = "/kwn", params = "filtr")
//    @Transactional
//    public String filter(@ModelAttribute("filterSetKWN") filterSetKWN formKWN, Model model) {
//        if (formKWN == null) {
//            formKWN = new filterSetKWN();
//        }
//
//        List<KWN> KwnList = kwnService.firstfilter(formKWN);
//
//
//        model.addAttribute("filterSetKWN", formKWN);
//        model.addAttribute("KWNList", KwnList);
//        return "/kwn/archiwumkwn";
//    }
//
//
//
//    @RequestMapping(value = "/kwn", params = "down")
//    public void filter(@ModelAttribute("filterSetKWN") filterSetKWN formKWN, Model model, HttpServletRequest request, HttpServletResponse response) {
//
//        List<KWN> KwnList = kwnService.firstfilter(formKWN);
//
//        try {
//            excelgeneralGenerator.createExcelKWN(KwnList, request, response);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    @GetMapping(path = "/kwn/accept/{id}")
//    @Transactional
//    public String kwnAccept(@PathVariable Long id, Model model) {
//
//        KWN kwn =kwnService.getKwn(id);
//
//        if (kwn.getModerator().getEmployee().getUserName().equals(
//                employeeService.getCurrentEmployee().getUserName())) {
//
//            kwn.setStatus(Status.ZAKONCZONY);
//        }
//
//        filterSetKWN formKWN= new filterSetKWN();
//        List<KWN> KwnList = kwnService.firstfilter(formKWN);
//
//
//
//        model.addAttribute("filterSetKWN", formKWN);
//        model.addAttribute("KWNList", KwnList);
//
//
//        return "redirect:/archiwum/kwn";
//    }
//
//    @GetMapping(path = "/kwn/{id}")
//    @Transactional
//    public String kwnPreview(@PathVariable Long id, Model model) {
//        boolean acces = false;
//        for (KWN kwn : kwnService.getAllEmployeeKWN()) {
//            if(kwn != null && kwn.getId() == id){
//                acces = true;
//                break;
//            }
//        }
//        model.addAttribute("acces", acces);
//        if (!acces) {
//            return "redirect:/archiwum/kwn";
//        }
//        List<Employee> employees = employeeService.getAllEmployee();
//        model.addAttribute("employees", employees);
//
//        KWNDto kwnDto = kwnService.getKwnForm(id);
//        List<Task> tasks = kwnService.getKwn(id).getTaskList();
//
//        kwnDto.setTasks(tasks);
//        model.addAttribute("kwnForm", kwnDto);
//
//        return "/kwn/kwnpodglad";
//    }
//
//    @GetMapping("/kwn/download/{id}")
//    public void downloadPdf(@PathVariable Long id, HttpServletResponse response) {
//        kwnService.downPdf(id, response);
//    }
//
//    @GetMapping("/kwn/files/{id}")
//    @Transactional
//    public String fileList(@PathVariable Long id, Model model) {
////        model.addAttribute("KWNList", kwnService.getAllKWN());
//        KWN kwn = kwnRepo.findOne(id);
//        model.addAttribute("accKwn", id);
//        model.addAttribute("filesList", kwn.getFileList());
//
//        return "/kwn/fileList";
//    }
//
//
//    @PostMapping("/kwn/files/{id}")
//    public String handleFileListUpload(@RequestParam("uploadFile") MultipartFile file,
//                                       RedirectAttributes redirectAttributes, @PathVariable Long id, Model model) {
//
//        kwnService.saveFileKwnProof(file, id);
//        KWN kwn = kwnRepo.findOne(id);
//
//
//        model.addAttribute("accKwn", id);
//        model.addAttribute("filesList", kwn.getFileList());
//        return "/kwn/fileList";
//
//    }
//
//
//    @GetMapping("/kwn/files/{id1}/{id2}")
//    public void fileListDownload(@PathVariable Long id1, @PathVariable Long id2, Model model, HttpServletRequest request, HttpServletResponse response) {
//        KWN kwn = kwnRepo.findOne(id1);
//
//        kwnService.downloadFileKwnProof(id1, id2, request, response);
//
//
//        model.addAttribute("accKwn", id1);
//        model.addAttribute("filesList", kwn.getFileList());
//
//
//        //return "/fileList";
//    }
//
//
////    @GetMapping("/kwn/filter")
////    public String filterKWN(@ModelAttribute("formKWN") KWNDto formKWN,Model model,HttpServletRequest request, HttpServletResponse response) {
////
////        if (formKWN==null){
////            formKWN= new KWNDto();
////        }
////
////            String pattern = "MM-dd-yyyy";
////        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
////
////        System.out.println("KWN form======================================");
////        System.out.println(formKWN);
////
////        List<KWN> KwnList=kwnService.firstfilter();
////
////
////        model.addAttribute("kwnForm", formKWN);
////        model.addAttribute("KWNList", KwnList);
////
////
////        return "/archiwumkwn";
////    }
//
//}
