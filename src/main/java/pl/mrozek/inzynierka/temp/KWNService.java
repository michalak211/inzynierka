//package pl.mrozek.inzynierka.temp;
//
//import lombok.SneakyThrows;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpHeaders;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.ldap.userdetails.LdapUserDetails;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//import pl.pwpw.demo.KWN.Entity.KWN;
//import pl.pwpw.demo.KWN.Entity.KwnMeetingFiles;
//import pl.pwpw.demo.KWN.Entity.Task;
//import pl.pwpw.demo.KWN.Mapper.Mapper;
//import pl.pwpw.demo.KWN.Repo.KWNRepo;
//import pl.pwpw.demo.KWN.Repo.TaskRepo;
//import pl.pwpw.demo.KWN.filters.Criterias.SearchOperation;
//import pl.pwpw.demo.KWN.filters.Criterias.SpecSearchCriteria;
//import pl.pwpw.demo.KWN.filters.KWNSpecification;
//import pl.pwpw.demo.KWN.filters.filterSetKWN;
//import pl.pwpw.demo.ogolne.Entity.*;
//import pl.pwpw.demo.ogolne.Entity.Select.Cecha;
//import pl.pwpw.demo.ogolne.Entity.Select.Niezgodnosc;
//import pl.pwpw.demo.ogolne.Entity.Select.OpisNiezgodnosci;
//import pl.pwpw.demo.ogolne.Repo.select.CechaRepo;
//import pl.pwpw.demo.ogolne.Repo.select.NiezgodnoscRepo;
//import pl.pwpw.demo.ogolne.Repo.select.OpisNiezgodnosciRepo;
//import pl.pwpw.demo.ogolne.Service.EmployeeService;
//import pl.pwpw.demo.ogolne.Service.FileService;
//
//import javax.servlet.ServletContext;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.BufferedOutputStream;
//import java.io.IOException;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.*;
//import java.util.stream.Collectors;
//
//@Service
//public class KWNService {
//
//
//    @Autowired
//    KWNRepo kwnRepo;
//    @Autowired
//    private ServletContext servletContext;
//    @Autowired
//    TaskRepo taskRepo;
//    @Autowired
//    Mapper mapper;
//    @Autowired
//    EmployeeService employeeService;
//    @Autowired
//    NiezgodnoscRepo niezgodnoscRepo;
//    @Autowired
//    CechaRepo cechaRepo;
//    @Autowired
//    OpisNiezgodnosciRepo opisNiezgodnosciRepo;
//
//
//
//    List<TP> tps = new ArrayList<>();
//    List<PC> pcs = new ArrayList<>();
//
//    public List<KWN> getKwns() {
//        return (List<KWN>) kwnRepo.findAll();
//    }
//
////    public void setKwns(List<KWN> kwns) {
////        this.kwns = kwns;
////
////    }
//
//
//    public void addKWN(KWN kwn) {
//        kwnRepo.save(kwn);
//        KWN kwn2 = kwnRepo.findByNrKwn(kwn.getNrKwn());
//        mapper.addKWNtoTask(kwn2);
//    }
//
//
//    public List<KWN> getAllKWN() {
//        return (List<KWN>) kwnRepo.findAll();
//    }
//
//
//    public String getNewNumber(String obszar) {
//        int year = Calendar.getInstance().get(Calendar.YEAR);
//        Date date = new GregorianCalendar(year, Calendar.JANUARY, 1).getTime();
//        List<KWN> kwns = kwnRepo.findAllByObszar(obszar);
//        kwns = kwns.stream().filter(x -> x.getDataSpotkaniaZ().after(date)).collect(Collectors.toList());
//        int max = 1;
//        if (kwns.size() > 0) {
//            for (KWN kwn : kwns) {
//                int number = Integer.parseInt(kwn.getNrKwn().split("/")[0]);
//                if (max < number) {
//                    max = number;
//                }
//            }
//            max++;
//        }
//
//        return "" + max + "/" + year + "/" + obszar + "/" + 1;
//    }
//
//    public void deleteKwn(Long id) {
//        try {
//            KWN kwn=kwnRepo.getOne(id);
//            kwn.getTaskList().clear();
//            kwnRepo.save(kwn);
//            kwnRepo.delete(id);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
////        taskRepo.delete(taskRepo.findAllByKwnIdEquals(id));
//    }
//
//    public KWNDto getKwnForm(Long id) {
//        return mapper.toFormKWN(kwnRepo.findOne(id));
//    }
//
//    public KWN getKwn(Long id) {
//        return kwnRepo.findOne(id);
//    }
//
//    public void addKWN(KWNDto KWNDto) {
//        if (KWNDto.getNrKwn().equals("")) {
//            KWNDto.setNrKwn(getNewNumber(KWNDto.getObszar()));
//        }
//        KWN kwn= new KWN();
//        kwn=mapper.toKWN(KWNDto,kwn);
//        kwn.setStatus(Status.AKTYWNY);
//        nowaCehca(KWNDto,kwn);
//        nowyOpis(KWNDto,kwn);
//
//        addKWN(kwn);
//        LdapUserDetails ldapUserDetails = employeeService.getLdapUserDetailsPrincipal();
//        Employee employee = employeeService.findByUserName(ldapUserDetails.getUsername());
//        System.out.println("KWN dodany nr "+kwn.getNrKwn()+" przez "+employee.getFirstAndLastName());
//        System.out.println(new Date());
//
//
//    }
//    private void nowaCehca(KWNDto KWNDto, KWN kwn){
//
//        Niezgodnosc niez=niezgodnoscRepo.findByNiezgodnoscNazEquals(KWNDto.getNiezgodnoscDotyczy());
//        Niezgodnosc niezNiokreslona=niezgodnoscRepo.findByNiezgodnoscNazEquals("nieokreślone");
//
//        if (KWNDto.getNowaCecha()!=null&&!KWNDto.getNowaCecha().equals("")){
//            kwn.setCecha(kwn.getCecha()+","+KWNDto.getNowaCecha());
//
//            Cecha cecha= new Cecha();
//            cecha.setCechaNaz(KWNDto.getNowaCecha());
//            niez.getCechaList().add(cecha);
//            niezgodnoscRepo.save(niez);
//
//            boolean is=false;
//            for (Cecha cecha1:niezNiokreslona.getCechaList()){
//                if (cecha1.getCechaNaz()!=null) {
//                    if (cecha1.getCechaNaz().equals(KWNDto.getNowaCecha())){
//                        is=true;
//                        break;
//                    }
//                }
//            }
//            if (!is) {
//                Cecha cecha1=new Cecha();
//                cecha1.setCechaNaz(KWNDto.getNowaCecha());
//                niezNiokreslona.getCechaList().add(cecha1);
//                niezgodnoscRepo.save(niezNiokreslona);
//
//                LdapUserDetails ldapUserDetails = employeeService.getLdapUserDetailsPrincipal();
//                Employee employee = employeeService.findByUserName(ldapUserDetails.getUsername());
//                System.out.println("Dodano nowa cecha do niezgodnosc "+niez.getNiezgodnoscNaz()+" ceche "+cecha1.getCechaNaz());
//                System.out.println(employee.getFirstAndLastName());
//                System.out.println(new Date());
//
//            }
//        }
//    }
//
//    private void nowyOpis(KWNDto KWNDto, KWN kwn){
//
//        Niezgodnosc niez=niezgodnoscRepo.findByNiezgodnoscNazEquals(KWNDto.getNiezgodnoscDotyczy());
//        Niezgodnosc niezNiokreslona=niezgodnoscRepo.findByNiezgodnoscNazEquals("nieokreślone");
//
//
//        if (KWNDto.getNowyOpisNiezgodnosci()!=null&&!KWNDto.getNowyOpisNiezgodnosci().equals("")){
//            kwn.setOpisNiezgodnosci(kwn.getOpisNiezgodnosci()+","+KWNDto.getNowyOpisNiezgodnosci());
//
//            OpisNiezgodnosci opisNiezgodnosci= new OpisNiezgodnosci();
//            opisNiezgodnosci.setOpis(KWNDto.getNowyOpisNiezgodnosci());
//            niez.getOpisList().add(opisNiezgodnosci);
//            niezgodnoscRepo.save(niez);
//
//            boolean is=false;
//            for (OpisNiezgodnosci opisNiezgodnosci1:niezNiokreslona.getOpisList()){
//                if (opisNiezgodnosci1.getOpis().equals(KWNDto.getNowyOpisNiezgodnosci())){
//                    is=true;
//                    break;
//                }
//            }
//            if (!is) {
//                OpisNiezgodnosci opisNiezgodnosci1= new OpisNiezgodnosci();
//                opisNiezgodnosci1.setOpis(KWNDto.getNowyOpisNiezgodnosci());
//                niezNiokreslona.getOpisList().add(opisNiezgodnosci1);
//                niezgodnoscRepo.save(niezNiokreslona);
//
//                LdapUserDetails ldapUserDetails = employeeService.getLdapUserDetailsPrincipal();
//                Employee employee = employeeService.findByUserName(ldapUserDetails.getUsername());
//                System.out.println("Dodano nowy opis niezgodnosci do niezgodnosc "+niez.getNiezgodnoscNaz()+" ceche "+niez.getNiezgodnoscNaz());
//                System.out.println(employee.getFirstAndLastName());
//                System.out.println(new Date());
//
//            }
//
//        }
//
//    }
//
//
//
//
//        public void editKWN(Long id, KWNDto KWNDto) {
//        KWN kwn = kwnRepo.findOne(id);
//        if (kwn != null) {
//            kwn = mapper.toKWN(KWNDto,kwn);
//            nowyOpis(KWNDto,kwn);
//            nowaCehca(KWNDto,kwn);
//
//
//            kwnRepo.save(kwn);
//            mapper.addKWNtoTask(kwn);
//            LdapUserDetails ldapUserDetails = employeeService.getLdapUserDetailsPrincipal();
//            Employee employee = employeeService.findByUserName(ldapUserDetails.getUsername());
//            System.out.println("edycja KWN nr "+kwn.getNrKwn());
//            System.out.println(employee.getFirstAndLastName());
//            System.out.println(new Date());
//
//        }
//    }
//
//    public KWN newMeeting(Long id) {
//        KWN newMeetingKwn = new KWN();
//        KWN kwn = kwnRepo.findOne(id);
//        try {
//            newMeetingKwn = (KWN) kwn.clone();
//        } catch (CloneNotSupportedException e) {
//            e.printStackTrace();
//        }
//        String oldKWN = newMeetingKwn.getNrKwn();
//        String[] kwnArray = oldKWN.split("/");
//        int lastNumber = Integer.parseInt(kwnArray[kwnArray.length - 1]);
//        lastNumber++;
//        kwnArray[kwnArray.length - 1] = Integer.toString(lastNumber);
//        String newNumber = String.join("/", kwnArray);
//        newMeetingKwn.setNrKwn(newNumber);
//
//        List<Task> copyTasks = new ArrayList<>(newMeetingKwn.getTaskList());
//        newMeetingKwn.setTaskList(copyTasks);
//
//        List<PC> copyPC = new ArrayList<>(newMeetingKwn.getPcList());
//        newMeetingKwn.setPcList(copyPC);
//
//        LdapUserDetails ldapUserDetails = employeeService.getLdapUserDetailsPrincipal();
//        Employee employee = employeeService.findByUserName(ldapUserDetails.getUsername());
//        System.out.println("nowe spotrkanie nr "+kwn.getNrKwn());
//        System.out.println(employee.getFirstAndLastName());
//        System.out.println(new Date());
//
//
//        return newMeetingKwn;
//
//
//
//    }
//
//
//    public void saveFileKwnRap(MultipartFile file, Long id) {
//
//        try {
//            byte[] bytes = file.getBytes();
//            KWN kwn = kwnRepo.findOne(id);
//            kwn.setPlik(bytes);
//            kwnRepo.save(kwn);
//
//            LdapUserDetails ldapUserDetails = employeeService.getLdapUserDetailsPrincipal();
//            Employee employee = employeeService.findByUserName(ldapUserDetails.getUsername());
//            System.out.println("dodano plik ze spotkania "+kwn.getNrKwn());
//            System.out.println(employee.getFirstAndLastName());
//            System.out.println(new Date());
//
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    @SneakyThrows
//    public void downPdf(Long id, HttpServletResponse response) {
//
//        KWN kwn = kwnRepo.findOne(id);
//        String CONTENT_TYPE = "MediaType.APPLICATION_PDF";
//
////        Path path= Paths.get(kwn.getPlik());
////        File file = new File(path.toString());
//
//        response.setContentType(CONTENT_TYPE);
//        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + generateName(kwn.getNrKwn()) + ".pdf");
//        byte[] bytes = kwn.getPlik();
//        BufferedOutputStream outStream = new BufferedOutputStream(response.getOutputStream());
//        outStream.write(bytes);
//        outStream.flush();
//    }
//
//
//    public void saveFileKwnProof(MultipartFile file, Long id) {
//
//        try {
//            if (file.getSize() >= 100) {
//                byte[] bytes = file.getBytes();
//                KWN kwn = kwnRepo.findOne(id);
//                KwnMeetingFiles kwnFile = new KwnMeetingFiles();
//                kwnFile.setExtention(FileService.getExtention(file.getOriginalFilename()));
//
//                String temp= FileService.getName(file.getOriginalFilename());
//                kwnFile.setNazwaPliku(temp);
//
//                kwnFile.setPlik(bytes);
//                kwn.getFileList().add(kwnFile);
//                kwnRepo.save(kwn);
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    public void downloadFileKwnProof(Long KWNIDid, Long fileId, HttpServletRequest request, HttpServletResponse response) {
//
//        KWN kwn = kwnRepo.findOne(KWNIDid);
//
//        int longFileId = Math.toIntExact(fileId);
//        KwnMeetingFiles plikOb = kwn.getFileList().get(longFileId);
//        String nazwa = plikOb.getNazwaPliku();
//        String extention = plikOb.getExtention();
//        byte[] bytes = plikOb.getPlik();
//
//        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + nazwa + "." + extention);
//
//        FileService.setExtentionRsponse(response, extention);
//
//
//        try {
//            BufferedOutputStream outStream = new BufferedOutputStream(response.getOutputStream());
//            outStream.write(bytes);
//            outStream.flush();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }
//
//
//    public void deleteFileKWN(long KWNId, long fileId) {
//
//        KWN kwn = kwnRepo.findOne(KWNId);
//        int longFileId = Math.toIntExact(fileId);
//        kwn.getFileList().remove(longFileId);
//        kwnRepo.save(kwn);
//
//        LdapUserDetails ldapUserDetails = employeeService.getLdapUserDetailsPrincipal();
//        Employee employee = employeeService.findByUserName(ldapUserDetails.getUsername());
//        System.out.println("usunieto plik z "+kwn.getNrKwn());
//        System.out.println(employee.getFirstAndLastName());
//        System.out.println(new Date());
//
//    }
//
//
//    private String generateName(String nrKwn) {
//
//        String[] parts = nrKwn.split("/");
//        String kwnName = "";
//        for (int j = 0; j <= parts.length - 1; j++) {
//            kwnName = kwnName + parts[j];
//            if (j <= parts.length - 2) {
//                kwnName = kwnName + "-";
//            }
//        }
//        return kwnName;
//    }
//
//
//    public List<KWN> firstfilter(filterSetKWN kwnForm) {
//
//
//
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
//
//        LdapUserDetails ldapUserDetails = employeeService.getLdapUserDetailsPrincipal();
//        boolean adm = false;
//        for (GrantedAuthority autority: ldapUserDetails.getAuthorities()){
//            if (autority.getAuthority().equals("SG-RTAdmin")){
//                adm=true;
//            }
//        }
//        ArrayList<KWN> kwnList;
//        if (adm){
//           kwnList=(ArrayList<KWN>) kwnRepo.findAll();
//        }else {
//            kwnList=(ArrayList<KWN>) getAllEmployeeKWN();
//        }
//
//
////        if (kwnForm.isAktywne()) {
////            kwnList.addAll(kwnRepo.findAllByStatusEquals(Status.AKTYWNY));
////        }
////        if (kwnForm.isZakonczone()) {
////            kwnList.addAll(kwnRepo.findAllByStatusEquals(Status.AKTYWNY));
////        }
//
//        SpecSearchCriteria crit = new SpecSearchCriteria("nazwaWyrobu", SearchOperation.CONTAINS, kwnForm.getNazwaWyrobu());
//
//
//        if (kwnForm.getNazwaWyrobu() != null && !kwnForm.getNazwaWyrobu().equals("")) {
//            inFiltr(crit, kwnList);
//        }
//
//        if (kwnForm.getObszar() != null && !kwnForm.getObszar().equals("")) {
//            crit.setKey("obszar");
//            crit.setValue(kwnForm.getObszar());
//            inFiltr(crit, kwnList);
//        }
//
//        if (kwnForm.getZglaszajacyNiezgodnosc() != null && !kwnForm.getZglaszajacyNiezgodnosc().equals("")) {
//            crit.setKey("zglaszajacyNiezgodnosc");
//            crit.setValue(kwnForm.getZglaszajacyNiezgodnosc());
//            inFiltr(crit, kwnList);
//        }
//
//
//        if (kwnForm.getWykryteNaEtapie() != null && !kwnForm.getWykryteNaEtapie().equals("")) {
//            crit.setKey("wykryteNaEtapie");
//            crit.setValue(kwnForm.getWykryteNaEtapie());
//            inFiltr(crit, kwnList);
//        }
//        if (kwnForm.getDecyzjaZKWN() != null && !kwnForm.getDecyzjaZKWN().equals("")) {
//            crit.setKey("decyzjaZKWN");
//            crit.setValue(kwnForm.getDecyzjaZKWN());
//            inFiltr(crit, kwnList);
//        }
//
//        if (kwnForm.getOpisNiezgodnosci() != null && !kwnForm.getOpisNiezgodnosci().equals("")) {
//            crit.setKey("opisNiezgodnosci");
//            crit.setValue(kwnForm.getOpisNiezgodnosci());
//            inFiltr(crit, kwnList);
//        }
//
//        if (kwnForm.getDataZgloszeniaPrzed() != null) {
//            if (!kwnForm.getDataZgloszeniaPrzed().equals("")) {
//                Date dateprzed = new Date();
//                try {
//                    dateprzed = sdf.parse(kwnForm.getDataZgloszeniaPrzed());
//                    //                dateprzed = sdf.parse(kwnForm.getDataZgloszeniaPrzed());
//
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//
//                inFiltr(kwnList, (ArrayList<KWN>) kwnRepo.findByDataZgloszeniaBefore(dateprzed));
//            }
//        }
//
//
//        if (kwnForm.getDataZgloszeniaPo() != null) {
//            if (!kwnForm.getDataZgloszeniaPo().equals("")) {
//                Date datepo = new Date();
//                try {
//                    datepo = sdf.parse(kwnForm.getDataZgloszeniaPo());
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//
//                inFiltr(kwnList, (ArrayList<KWN>) kwnRepo.findByDataZgloszeniaAfter(datepo));
//            }
//        }
//
//        if (kwnForm.getDataSpotkaniaPrzed() != null) {
//            if (!kwnForm.getDataSpotkaniaPrzed().equals("")) {
//                Date dateprzed = new Date();
//                try {
//                    dateprzed = sdf.parse(kwnForm.getDataSpotkaniaPrzed());
//                    //                dateprzed = sdf.parse(kwnForm.getDataZgloszeniaPrzed());
//
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//
//                inFiltr(kwnList, (ArrayList<KWN>) kwnRepo.findByDataSpotkaniaZBefore(dateprzed));
//            }
//        }
//
//
//        if (kwnForm.getDataSpotkaniaPo() != null) {
//            if (!kwnForm.getDataSpotkaniaPo().equals("")) {
//                Date dateprzed = new Date();
//                try {
//                    dateprzed = sdf.parse(kwnForm.getDataSpotkaniaPo());
//                    //                dateprzed = sdf.parse(kwnForm.getDataZgloszeniaPrzed());
//
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//
//                inFiltr(kwnList, (ArrayList<KWN>) kwnRepo.findByDataSpotkaniaZAfter(dateprzed));
//            }
//        }
//
//
//
//
//        if (kwnForm.getNrZlecenia() != null && !kwnForm.getNrZlecenia().equals("")) {
//
//            ArrayList<KWN> kwnList2 = new ArrayList<>();
//
//            for (KWN kwn : kwnList) {
//                boolean tt = false;
//                for (Zlecenia zlec : kwn.getNrZlecenia()) {
//                    if (zlec.getNrZlecenia().contains(kwnForm.getNrZlecenia())) {
//                        tt = true;
//                        break;
//                    }
//                }
//                if (tt) {
//                    kwnList2.add(kwn);
//                }
//            }
//            inFiltr(kwnList, kwnList2);
//        }
//
//        return kwnList;
//    }
//
//
//    private void inFiltr(SpecSearchCriteria crit, ArrayList<KWN> kwnList) {
//
//        KWNSpecification spec = new KWNSpecification(crit);
//        ArrayList<KWN> tempList = (ArrayList<KWN>) kwnList.clone();
//
//
//        ArrayList<KWN> kwnList2 = (ArrayList<KWN>) kwnRepo.findAll(spec);
//        kwnList.clear();
//        for (KWN kwn : kwnList2) {
//            if (tempList.contains(kwn)) {
//                kwnList.add(kwn);
//
//            }
//        }
//    }
//
////    public ArrayList<KWN> usersKWN(){
////        LdapUserDetails ldapUserDetails = (LdapUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
////        Employee employee = employeeService.findByUserName(ldapUserDetails.getUsername());
////
////        ArrayList<KWN> kwnList= new ArrayList<>();
////        for (Task task:taskRepo.findAllByEmployee_Id(employee.getId())){
////            KWN kwn=task.getKwn();
////            if (!kwnList.contains(kwn)){
////                kwnList.add(kwn);
////            }
////        }
////        return kwnList;
////    }
//
//    private void inFiltr(ArrayList<KWN> kwnList, ArrayList<KWN> kwnList2) {
//        ArrayList<KWN> tempList = (ArrayList<KWN>) kwnList.clone();
//        kwnList.clear();
//        for (KWN kwn : kwnList2) {
//            if (tempList.contains(kwn)) {
//                kwnList.add(kwn);
//            }
//        }
//    }
//
//
//    public void checkKwnStatus() {
//
//        List<KWN> kwnList = kwnRepo.findAll();
//        for (KWN kwn: kwnList){
//            if (kwn.getStatus()==null){
//                kwn.setStatus(Status.AKTYWNY);
//                kwnRepo.save(kwn);
//            }
//        }
//
//    }
//
//    static Comparator<KWN> StuNameComparator = new Comparator<KWN>() {
//
//        public int compare(KWN s1, KWN s2) {
//
//            //ascending order
//            return s1.getId().compareTo(s2.getId());
//
//
//        }};
//
//    public List<KWN> getAllEmployeeKWN() {
//
//        LdapUserDetails ldapUserDetails = employeeService.getLdapUserDetailsPrincipal();
//
//        for (GrantedAuthority autority: ldapUserDetails.getAuthorities()){
//
//            if (autority.getAuthority().equals("SG-RTAdmin") || autority.getAuthority().equals("SG-RTViewer")) {
//                return kwnRepo.findAll();
//            }
//
//        }
//
//        Employee employee = employeeService.findByUserName(ldapUserDetails.getUsername());
//        List<Task> taskList = taskRepo.findAllByEmployee_Id(employee.getId());
//        List<KWN> kwnList2 = kwnRepo.findAll();
//
//        Set<KWN> pomKwn = new HashSet<>();
//        for (KWN kwn : kwnList2) {
//            for (PC pc : kwn.getPcList()) {
//                if (pc.getEmployee() != null && pc.getEmployee().getId() == employee.getId()) {
//                    pomKwn.add(kwn);
//                    break;
//                }
//            }
//        }
//
//        Set<KWN> kwnSet = new HashSet<>();
//        for (Task t : taskList) {
//            kwnSet.add(kwnRepo.findOne(t.getKwnId()));
//        }
//
//        kwnSet.addAll(pomKwn);
//
//        List<KWN> listofkwns = new ArrayList<>(kwnSet);
//
//        Collections.sort(listofkwns);
//        return listofkwns;
//
//    }
//
//
//    public void niezgodnoscCreator() {
//        //init
//        ArrayList<Niezgodnosc> niezList=new ArrayList<>();
//        List<Niezgodnosc> temp= (List<Niezgodnosc>) niezgodnoscRepo.findAll();
//        niezgodnoscRepo.delete(temp);
//
//        List<Cecha> cechaList=new ArrayList<>();
//        Cecha cecha= new Cecha();
//        cecha.setCechaNaz("cechy wizualne");
//        cechaList.add(cecha);
//        cecha= new Cecha();
//        cecha.setCechaNaz("cechy mierzalne");
//        cechaList.add(cecha);
//
//        List<OpisNiezgodnosci> opisNiezgodnosciList= new ArrayList<>();
//        OpisNiezgodnosci opisNiezgodnosci= new OpisNiezgodnosci();
//        opisNiezgodnosci.setOpis("niezgodność z wzorcem");
//        opisNiezgodnosciList.add(opisNiezgodnosci);
//
//        opisNiezgodnosci= new OpisNiezgodnosci();
//        opisNiezgodnosci.setOpis("niezgodność z ST");
//        opisNiezgodnosciList.add(opisNiezgodnosci);
//
//
//        Niezgodnosc niezgodnosc= new Niezgodnosc("dostawa",cechaList,opisNiezgodnosciList);
//        niezList.add(niezgodnosc);
//        niezgodnoscRepo.save(niezgodnosc);
//
//
//        cechaList=new ArrayList<>();
//
//
//        cecha= new Cecha();
//        cecha.setCechaNaz("kolorystyka VIS");
//        cechaList.add(cecha);
//
//        cecha= new Cecha();
//        cecha.setCechaNaz("widok w  UV");
//        cechaList.add(cecha);
//
//
//        cecha= new Cecha();
//        cecha.setCechaNaz("znak wodny");
//        cechaList.add(cecha);
//
//        cecha= new Cecha();
//        cecha.setCechaNaz("nitka zabezpieczająca");
//        cechaList.add(cecha);
//
//        cecha= new Cecha();
//        cecha.setCechaNaz("włókna zabezpieczające");
//        cechaList.add(cecha);
//
//        cecha= new Cecha();
//        cecha.setCechaNaz("przezrocze");
//        cechaList.add(cecha);
//
//        cecha= new Cecha();
//        cecha.setCechaNaz("wygląd/odwzorowanie");
//        cechaList.add(cecha);
//
//        cecha= new Cecha();
//        cecha.setCechaNaz("parametry");
//        cechaList.add(cecha);
//
//        cecha= new Cecha();
//        cecha.setCechaNaz("format");
//        cechaList.add(cecha);
//
//        cecha= new Cecha();
//        cecha.setCechaNaz("pozycjonowanie");
//        cechaList.add(cecha);
//
//        cecha= new Cecha();
//        cecha.setCechaNaz("aplikacja (hologram/kinegram/druk irydescentny");
//        cechaList.add(cecha);
//
//        cecha= new Cecha();
//        cecha.setCechaNaz("połączenie warstw klejem");
//        cechaList.add(cecha);
//
//        cecha= new Cecha();
//        cecha.setCechaNaz("zabezpieczenia III stopnia");
//        cechaList.add(cecha);
//
//        cecha= new Cecha();
//        cecha.setCechaNaz("drukowność/zadrukowywalność");
//        cechaList.add(cecha);
//
//
//        opisNiezgodnosciList= new ArrayList<>();
//
//
//        opisNiezgodnosci= new OpisNiezgodnosci();
//        opisNiezgodnosci.setOpis("brak elementu/zabezpieczenia");
//        opisNiezgodnosciList.add(opisNiezgodnosci);
//
//        opisNiezgodnosci= new OpisNiezgodnosci();
//        opisNiezgodnosci.setOpis("niezgodność z wzorcem");
//        opisNiezgodnosciList.add(opisNiezgodnosci);
//
//        opisNiezgodnosci= new OpisNiezgodnosci();
//        opisNiezgodnosci.setOpis("niezgodność z ST");
//        opisNiezgodnosciList.add(opisNiezgodnosci);
//
//        opisNiezgodnosci= new OpisNiezgodnosci();
//        opisNiezgodnosci.setOpis("dziury");
//        opisNiezgodnosciList.add(opisNiezgodnosci);
//
//        opisNiezgodnosci= new OpisNiezgodnosci();
//        opisNiezgodnosci.setOpis("zgęstki");
//        opisNiezgodnosciList.add(opisNiezgodnosci);
//
//        opisNiezgodnosci= new OpisNiezgodnosci();
//        opisNiezgodnosci.setOpis("cętki");
//        opisNiezgodnosciList.add(opisNiezgodnosci);
//
//
//        opisNiezgodnosci= new OpisNiezgodnosci();
//        opisNiezgodnosci.setOpis("nierównomierne przezrocze");
//        opisNiezgodnosciList.add(opisNiezgodnosci);
//
//        opisNiezgodnosci= new OpisNiezgodnosci();
//        opisNiezgodnosci.setOpis("plamy");
//        opisNiezgodnosciList.add(opisNiezgodnosci);
//
//
//        opisNiezgodnosci= new OpisNiezgodnosci();
//        opisNiezgodnosci.setOpis("pozycjonowanie poza tolerancją");
//        opisNiezgodnosciList.add(opisNiezgodnosci);
//
//        opisNiezgodnosci= new OpisNiezgodnosci();
//        opisNiezgodnosci.setOpis("format poza tolerancją");
//        opisNiezgodnosciList.add(opisNiezgodnosci);
//
//
//        niezgodnosc= new Niezgodnosc("papier",cechaList,opisNiezgodnosciList);
//        niezList.add(niezgodnosc);
//        niezgodnoscRepo.save(niezgodnosc);
//
//        cechaList=new ArrayList<>();
//        opisNiezgodnosciList= new ArrayList<>();
//
//        cecha= new Cecha();
//        cecha.setCechaNaz("kolorystyka VIS");
//        cechaList.add(cecha);
//
//        cecha= new Cecha();
//        cecha.setCechaNaz("kolorystyka UV");
//        cechaList.add(cecha);
//
//        cecha= new Cecha();
//        cecha.setCechaNaz("widok w IR");
//        cechaList.add(cecha);
//
//        cecha= new Cecha();
//        cecha.setCechaNaz("irysy");
//        cechaList.add(cecha);
//
//        cecha= new Cecha();
//        cecha.setCechaNaz("grafika");
//        cechaList.add(cecha);
//
//        cecha= new Cecha();
//        cecha.setCechaNaz("teksty");
//        cechaList.add(cecha);
//
//        cecha= new Cecha();
//        cecha.setCechaNaz("mikroteksty");
//        cechaList.add(cecha);
//
//        cecha= new Cecha();
//        cecha.setCechaNaz("wygląd/odwzorowanie");
//        cechaList.add(cecha);
//
//        cecha= new Cecha();
//        cecha.setCechaNaz("pozycjonowanie");
//        cechaList.add(cecha);
//
//        cecha= new Cecha();
//        cecha.setCechaNaz("efekt termochromowy");
//        cechaList.add(cecha);
//
//        cecha= new Cecha();
//        cecha.setCechaNaz("zabezpieczenia III stopnia");
//        cechaList.add(cecha);
//
//
//
//        opisNiezgodnosci= new OpisNiezgodnosci();
//        opisNiezgodnosci.setOpis("brak elementu/zabezpieczenia");
//        opisNiezgodnosciList.add(opisNiezgodnosci);
//
//        opisNiezgodnosci= new OpisNiezgodnosci();
//        opisNiezgodnosci.setOpis("niezgodność z wzorcem");
//        opisNiezgodnosciList.add(opisNiezgodnosci);
//
//        opisNiezgodnosci= new OpisNiezgodnosci();
//        opisNiezgodnosci.setOpis("niezgodność z ST");
//        opisNiezgodnosciList.add(opisNiezgodnosci);
//
//        opisNiezgodnosci= new OpisNiezgodnosci();
//        opisNiezgodnosci.setOpis("nieprawidłowe rozstawienie irysów");
//        opisNiezgodnosciList.add(opisNiezgodnosci);
//
//        opisNiezgodnosci= new OpisNiezgodnosci();
//        opisNiezgodnosci.setOpis("ubytki");
//        opisNiezgodnosciList.add(opisNiezgodnosci);
//
//        opisNiezgodnosci= new OpisNiezgodnosci();
//        opisNiezgodnosci.setOpis("zabrudzenia z farby");
//        opisNiezgodnosciList.add(opisNiezgodnosci);
//
//
//        opisNiezgodnosci= new OpisNiezgodnosci();
//        opisNiezgodnosci.setOpis("zaburzona czyteność mikrotektów");
//        opisNiezgodnosciList.add(opisNiezgodnosci);
//
//        opisNiezgodnosci= new OpisNiezgodnosci();
//        opisNiezgodnosci.setOpis("pozycjonowanie poza tolerancją");
//        opisNiezgodnosciList.add(opisNiezgodnosci);
//
//        opisNiezgodnosci= new OpisNiezgodnosci();
//        opisNiezgodnosci.setOpis("odciągnięcia");
//        opisNiezgodnosciList.add(opisNiezgodnosci);
//
//        niezgodnosc= new Niezgodnosc("offset",cechaList,opisNiezgodnosciList);
//        niezList.add(niezgodnosc);
//        niezgodnoscRepo.save(niezgodnosc);
//
//        cechaList=new ArrayList<>();
//        opisNiezgodnosciList= new ArrayList<>();
//
//        cechaAdd(cechaList,"kolorystyka VIS");
//        cechaAdd(cechaList,"zmienność optyczna");
//        cechaAdd(cechaList,"Spark");
//        cechaAdd(cechaList,"Irydescencja");
//        cechaAdd(cechaList,"grafika");
//        cechaAdd(cechaList,"wygląd/odwzorowanie");
//        cechaAdd(cechaList,"pozycjonowanie");
//        cechaAdd(cechaList,"zabezpieczenia III stopnia");
//
//        opisAdd(opisNiezgodnosciList,"brak elementu/zabezpieczenia");
//        opisAdd(opisNiezgodnosciList,"niezgodność z wzorcem");
//        opisAdd(opisNiezgodnosciList,"niezgodność z ST");
//        opisAdd(opisNiezgodnosciList,"ubytki/zabezpieczenia");
//        opisAdd(opisNiezgodnosciList,"zabrudzenia z farby");
//        opisAdd(opisNiezgodnosciList,"pozycjonowanie poza tolerancją");
//        opisAdd(opisNiezgodnosciList,"odciągnięcia");
//
//
//        niezgodnosc= new Niezgodnosc("sitodruk",cechaList,opisNiezgodnosciList);
//        niezList.add(niezgodnosc);
//        niezgodnoscRepo.save(niezgodnosc);
//
//
//        cechaList=new ArrayList<>();
//        opisNiezgodnosciList= new ArrayList<>();
//
//        cechaAdd(cechaList,"kolorystyka VIS");
//        cechaAdd(cechaList,"kolorystyka UV");
//        cechaAdd(cechaList,"widok w IR");
//        cechaAdd(cechaList,"metameria IR");
//        cechaAdd(cechaList,"OVI");
//        cechaAdd(cechaList,"grafika");
//        cechaAdd(cechaList,"teksty");
//        cechaAdd(cechaList,"mikroteksty");
//        cechaAdd(cechaList,"wygląd/odwzorowanie");
//        cechaAdd(cechaList,"kontrast (lab)");
//        cechaAdd(cechaList,"magnetyzm");
//        cechaAdd(cechaList,"efekt kątowy");
//        cechaAdd(cechaList,"DR mark");
//        cechaAdd(cechaList,"wysokość druku");
//
//        opisAdd(opisNiezgodnosciList,"brak elementu");
//        opisAdd(opisNiezgodnosciList,"niezgodność z wzorcem");
//        opisAdd(opisNiezgodnosciList,"niezgodność z ST");
//        opisAdd(opisNiezgodnosciList,"ubytki");
//        opisAdd(opisNiezgodnosciList,"zabrudzenia z farby");
//        opisAdd(opisNiezgodnosciList,"pozycjonowanie poza tolerancją");
//        opisAdd(opisNiezgodnosciList,"odciągnięcia");
//
//
//
//        niezgodnosc= new Niezgodnosc("staloryt",cechaList,opisNiezgodnosciList);
//        niezList.add(niezgodnosc);
//        niezgodnoscRepo.save(niezgodnosc);
//
//        cechaList=new ArrayList<>();
//        opisNiezgodnosciList= new ArrayList<>();
//
//        cechaAdd(cechaList,"kolorystyka VIS");
//        cechaAdd(cechaList,"kolorystyka UV");
//        cechaAdd(cechaList,"wygląd/odwzorowanie");
//        cechaAdd(cechaList,"algorytm numeracji");
//        cechaAdd(cechaList,"magnetyzm");
//        cechaAdd(cechaList,"fluoresciencja");
//        cechaAdd(cechaList,"Zgodność numeracji");
//
//        opisAdd(opisNiezgodnosciList,"brak elementu");
//        opisAdd(opisNiezgodnosciList,"niezgodność z wzorcem");
//        opisAdd(opisNiezgodnosciList,"niezgodność z ST");
//        opisAdd(opisNiezgodnosciList,"ubytki");
//        opisAdd(opisNiezgodnosciList,"zabrudzenia z farby");
//        opisAdd(opisNiezgodnosciList,"pozycjonowanie poza tolerancją");
//        opisAdd(opisNiezgodnosciList,"odciągnięcia");
//
//
//        niezgodnosc= new Niezgodnosc("typografia",cechaList,opisNiezgodnosciList);
//        niezList.add(niezgodnosc);
//        niezgodnoscRepo.save(niezgodnosc);
//
//        cechaList=new ArrayList<>();
//        opisNiezgodnosciList= new ArrayList<>();
//
//        cechaAdd(cechaList,"kolorystyka VIS");
//        cechaAdd(cechaList,"kolorystyka UV");
//        cechaAdd(cechaList,"widok w IR");
//        cechaAdd(cechaList,"kod");
//        cechaAdd(cechaList,"zgodność kodu z numeracją");
//        cechaAdd(cechaList,"algorytm");
//        cechaAdd(cechaList,"wygląd/odwzorowanie");
//        cechaAdd(cechaList,"pozycjonowanie");
//
//
//        opisAdd(opisNiezgodnosciList,"brak elementu");
//        opisAdd(opisNiezgodnosciList,"niezgodność z wzorcem");
//        opisAdd(opisNiezgodnosciList,"niezgodność z ST");
//        opisAdd(opisNiezgodnosciList,"ubytki");
//        opisAdd(opisNiezgodnosciList,"zabrudzenia z farby");
//        opisAdd(opisNiezgodnosciList,"pozycjonowanie poza tolerancją");
//        opisAdd(opisNiezgodnosciList,"odciągnięcia");
//
//
//        niezgodnosc= new Niezgodnosc("injet",cechaList,opisNiezgodnosciList);
//        niezList.add(niezgodnosc);
//        niezgodnoscRepo.save(niezgodnosc);
//
//        cechaList=new ArrayList<>();
//        opisNiezgodnosciList= new ArrayList<>();
//
//        cechaAdd(cechaList,"połysk");
//        cechaAdd(cechaList,"gramatura");
//        cechaAdd(cechaList,"utrawalenie");
//        cechaAdd(cechaList,"wygląd/odwzorowanie");
//        cechaAdd(cechaList,"pozycjonowanie");
//
//        opisAdd(opisNiezgodnosciList,"brak elementu");
//        opisAdd(opisNiezgodnosciList,"niezgodność z wzorcem");
//        opisAdd(opisNiezgodnosciList,"niezgodność z ST");
//        opisAdd(opisNiezgodnosciList,"pozycjonowanie poza tolerancją");
//
//
//        niezgodnosc= new Niezgodnosc("lakierowanie",cechaList,opisNiezgodnosciList);
//        niezList.add(niezgodnosc);
//        niezgodnoscRepo.save(niezgodnosc);
//
//        cechaList=new ArrayList<>();
//        opisNiezgodnosciList= new ArrayList<>();
//
//
//        cechaAdd(cechaList,"wygląd/odwzorowanie");
//        cechaAdd(cechaList,"jakość krawędzi");
//        cechaAdd(cechaList,"przyczepność");
//        cechaAdd(cechaList,"własciwości UV");
//        cechaAdd(cechaList,"widok IR");
//        cechaAdd(cechaList,"pozycjonowanie");
//        cechaAdd(cechaList,"up-convertering");
//
//
//        opisAdd(opisNiezgodnosciList,"brak elementu/zabezpieczenia");
//        opisAdd(opisNiezgodnosciList,"niezgodność z wzorcem");
//        opisAdd(opisNiezgodnosciList,"niezgodność z ST");
//        opisAdd(opisNiezgodnosciList,"pozycjonowanie poza tolerancją");
//
//
//        niezgodnosc= new Niezgodnosc("hot stamping",cechaList,opisNiezgodnosciList);
//        niezList.add(niezgodnosc);
//        niezgodnoscRepo.save(niezgodnosc);
//
//        cechaList=new ArrayList<>();
//        opisNiezgodnosciList= new ArrayList<>();
//
//
//        cechaAdd(cechaList,"format");
//        cechaAdd(cechaList,"ostrość krawędzi");
//        cechaAdd(cechaList,"pozycjonowanie");
//
//
//        opisAdd(opisNiezgodnosciList,"niezgodność z wzorcem");
//        opisAdd(opisNiezgodnosciList,"niezgodność z ST");
//
//
//        niezgodnosc= new Niezgodnosc("krojenie",cechaList,opisNiezgodnosciList);
//        niezList.add(niezgodnosc);
//        niezgodnoscRepo.save(niezgodnosc);
//
//        cechaList=new ArrayList<>();
//        opisNiezgodnosciList= new ArrayList<>();
//
//        cechaAdd(cechaList,"kolejność stron");
//        cechaAdd(cechaList,"prostokątność złamu");
//
//
//        opisAdd(opisNiezgodnosciList,"nieprawidłowa kolejność stron");
//        opisAdd(opisNiezgodnosciList,"skosy w złamie");
//
//
//        niezgodnosc= new Niezgodnosc("złamywanie",cechaList,opisNiezgodnosciList);
//        niezList.add(niezgodnosc);
//        niezgodnoscRepo.save(niezgodnosc);
//
//        cechaList=new ArrayList<>();
//        opisNiezgodnosciList= new ArrayList<>();
//
//        cechaAdd(cechaList,"kolorystyka VIS");
//        cechaAdd(cechaList,"kolorystyka UV");
//        cechaAdd(cechaList,"up-convertering");
//        cechaAdd(cechaList,"zgodność ściegu");
//        cechaAdd(cechaList,"ciągłość ściegu");
//        cechaAdd(cechaList,"wygląd ścieg");
//
//
//        opisAdd(opisNiezgodnosciList,"brak elementu/zabezpieczenia");
//        opisAdd(opisNiezgodnosciList,"niezgodność z wzorcem");
//        opisAdd(opisNiezgodnosciList,"niezgodność z ST");
//
//
//        niezgodnosc= new Niezgodnosc("szycie",cechaList,opisNiezgodnosciList);
//        niezList.add(niezgodnosc);
//        niezgodnoscRepo.save(niezgodnosc);
//
//        cechaList=new ArrayList<>();
//        opisNiezgodnosciList= new ArrayList<>();
//
//        cechaAdd(cechaList,"format");
//        cechaAdd(cechaList,"prostokątność");
//        cechaAdd(cechaList,"ostrość krawędzi");
//        cechaAdd(cechaList,"pozycjonowanie");
//
//
//        opisAdd(opisNiezgodnosciList,"brak elementu/zabezpieczenia");
//        opisAdd(opisNiezgodnosciList,"niezgodność z wzorcem");
//        opisAdd(opisNiezgodnosciList,"niezgodność z ST");
//        opisAdd(opisNiezgodnosciList,"pozycjonowanie poza tolerancją");
//
//
//
//        niezgodnosc= new Niezgodnosc("wykrawanie",cechaList,opisNiezgodnosciList);
//        niezList.add(niezgodnosc);
//        niezgodnoscRepo.save(niezgodnosc);
//
//        cechaList=new ArrayList<>();
//        opisNiezgodnosciList= new ArrayList<>();
//
//        cechaAdd(cechaList,"format");
//        cechaAdd(cechaList,"pozycjonowanie");
//        cechaAdd(cechaList,"wygląd");
//        cechaAdd(cechaList,"głębokość");
//        cechaAdd(cechaList,"funkcjonalność");
//
//
//        opisAdd(opisNiezgodnosciList,"brak elementu/zabezpieczenia");
//        opisAdd(opisNiezgodnosciList,"niezgodność z wzorcem");
//        opisAdd(opisNiezgodnosciList,"niezgodność z ST");
//        opisAdd(opisNiezgodnosciList,"pozycjonowanie poza tolerancją");
//
//
//        niezgodnosc= new Niezgodnosc("nacinanie",cechaList,opisNiezgodnosciList);
//        niezList.add(niezgodnosc);
//        niezgodnoscRepo.save(niezgodnosc);
//
//        cechaList=new ArrayList<>();
//        opisNiezgodnosciList= new ArrayList<>();
//
//        cechaAdd(cechaList,"powierzchnia laminatu");
//        cechaAdd(cechaList,"tłoczenia");
//        cechaAdd(cechaList,"MLI/CLI");
//        cechaAdd(cechaList,"pozycjonowanie");
//
//
//        opisAdd(opisNiezgodnosciList,"brak elementu");
//        opisAdd(opisNiezgodnosciList,"niezgodność z wzorcem");
//        opisAdd(opisNiezgodnosciList,"pozycjonowanie poza tolerancją");
//
//
//        niezgodnosc= new Niezgodnosc("laminowanie",cechaList,opisNiezgodnosciList);
//        niezList.add(niezgodnosc);
//        niezgodnoscRepo.save(niezgodnosc);
//
//        cechaList=new ArrayList<>();
//        opisNiezgodnosciList= new ArrayList<>();
//
//        cechaAdd(cechaList,"żywotność");
//        cechaAdd(cechaList,"odczyt danych");
//
//
//        opisAdd(opisNiezgodnosciList,"Nie odpowiada ATR");
//        opisAdd(opisNiezgodnosciList,"Nie odpowiada ATS");
//        opisAdd(opisNiezgodnosciList,"brak odczytu danych");
//
//
//        niezgodnosc= new Niezgodnosc("chip/mikroprocesor",cechaList,opisNiezgodnosciList);
//        niezList.add(niezgodnosc);
//        niezgodnoscRepo.save(niezgodnosc);
//
//        cechaList=new ArrayList<>();
//        opisNiezgodnosciList= new ArrayList<>();
//
//        cechaAdd(cechaList,"zgodność numeracji");
//        cechaAdd(cechaList,"zgodność ilościowa");
//        cechaAdd(cechaList,"zgodność etykiety z zawartością");
//
//
//        opisAdd(opisNiezgodnosciList,"błędna numeracja");
//        opisAdd(opisNiezgodnosciList,"niezgodność ilościowa");
//        opisAdd(opisNiezgodnosciList,"etykieta niezgodna z zawartością");
//
//
//        niezgodnosc= new Niezgodnosc("pakowanie",cechaList,opisNiezgodnosciList);
//        niezList.add(niezgodnosc);
//        niezgodnoscRepo.save(niezgodnosc);
//
//        cechaList=new ArrayList<>();
//        opisNiezgodnosciList= new ArrayList<>();
//
//        cechaAdd(cechaList,"cechy wizualne");
//        cechaAdd(cechaList,"cechy mierzalne");
//
//
//        opisAdd(opisNiezgodnosciList,"niezgodność z wzorcem");
//        opisAdd(opisNiezgodnosciList,"niezgodność z ST");
//
//
//        niezgodnosc= new Niezgodnosc("sortowanie manualne",cechaList,opisNiezgodnosciList);
//        niezList.add(niezgodnosc);
//        niezgodnoscRepo.save(niezgodnosc);
//
//        cechaList=new ArrayList<>();
//        opisNiezgodnosciList= new ArrayList<>();
//
//        cechaAdd(cechaList,"cechy wizualne");
//        cechaAdd(cechaList,"cechy mierzalne");
//
//
//        opisAdd(opisNiezgodnosciList,"niezgodność z wzorcem");
//        opisAdd(opisNiezgodnosciList,"niezgodność z ST");
//
//
//        niezgodnosc= new Niezgodnosc("sortowanie maszynowe",cechaList,opisNiezgodnosciList);
//        niezList.add(niezgodnosc);
//        niezgodnoscRepo.save(niezgodnosc);
//
//        cechaList=new ArrayList<>();
//        opisNiezgodnosciList= new ArrayList<>();
//
//        cechaAdd(cechaList,"zgodność numeracji");
//
//
//        opisAdd(opisNiezgodnosciList,"błędna numeracja");
//
//
//        niezgodnosc= new Niezgodnosc("wymiana dobitek",cechaList,opisNiezgodnosciList);
//        niezList.add(niezgodnosc);
//        niezgodnoscRepo.save(niezgodnosc);
//
//        cechaList=new ArrayList<>();
//        opisNiezgodnosciList= new ArrayList<>();
//
//        List<Cecha> tempC;
//        tempC= (List<Cecha>) cechaRepo.findAll();
//
//        List<OpisNiezgodnosci> tempO;
//        tempO= (List<OpisNiezgodnosci>) opisNiezgodnosciRepo.findAll();
//
//        boolean is;
//
//        for (Cecha cecha1:tempC){
//            is=false;
//            for (Cecha cecha2:cechaList){
//                if (cecha2.getCechaNaz().equals(cecha1.getCechaNaz())){
//                    is=true;
//                    break;
//                }
//            }
//
//            if (!is&&cecha1.getCechaNaz()!=null){
//                cechaList.add(new Cecha(cecha1.getCechaNaz()));
//            }
//        }
//
//
//        for (OpisNiezgodnosci opisNiezgodnosci1:tempO){
//            is=false;
//            for (OpisNiezgodnosci opis:opisNiezgodnosciList){
//                if (opis.getOpis().equals(opisNiezgodnosci1.getOpis())){
//                    is=true;
//                    break;
//                }
//            }
//
//            if (!is){
//                opisNiezgodnosciList.add(new OpisNiezgodnosci(opisNiezgodnosci1.getOpis()));
//            }
//        }
//
//
//
//        niezgodnosc= new Niezgodnosc("nieokreślone",cechaList,opisNiezgodnosciList);
//        niezList.add(niezgodnosc);
//        niezgodnoscRepo.save(niezgodnosc);
//
//
//
//
//    }
//    private void cechaAdd(List<Cecha> cechaList, String tekst){
//        Cecha cecha= new Cecha();
//        cecha.setCechaNaz(tekst);
//        cechaList.add(cecha);
//
//    }
//
//    private void opisAdd(List<OpisNiezgodnosci> opisNiezgodnosciList, String tekst) {
//
//        OpisNiezgodnosci opisNiezgodnosci= new OpisNiezgodnosci();
//        opisNiezgodnosci.setOpis(tekst);
//        opisNiezgodnosciList.add(opisNiezgodnosci);
//
//
//    }
//
//    }
