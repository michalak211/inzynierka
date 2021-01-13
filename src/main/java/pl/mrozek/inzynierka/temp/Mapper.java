//package pl.mrozek.inzynierka.temp;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Component;
//import pl.pwpw.demo.KWN.Entity.KWN;
//import pl.pwpw.demo.KWN.Entity.Task;
//import pl.pwpw.demo.KWN.Repo.TaskRepo;
//import pl.pwpw.demo.KWN.Service.TaskService;
//import pl.pwpw.demo.ogolne.Dto.EmployeeDto;
//import pl.pwpw.demo.ogolne.Dto.NoteDto;
//import pl.pwpw.demo.ogolne.Dto.TablePCDto;
//import pl.pwpw.demo.ogolne.Dto.TableTPDto;
//import pl.pwpw.demo.ogolne.Entity.*;
//import pl.pwpw.demo.ogolne.Repo.EmployeeRepo;
//import pl.pwpw.demo.ogolne.Repo.SettingsRepo;
//import pl.pwpw.demo.ogolne.Service.EmployeeService;
//import pl.pwpw.demo.ogolne.Service.SimpleEmailService;
//
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
////import pl.pwpw.demo.ogolne.Config.SpringSecurityConfig;
//
//@Component
//public class Mapper {
//
//
//    @Autowired
//    PasswordEncoder passwordEncoder;
//
//    @Autowired
//    EmployeeRepo employeeRepo;
//
//    @Autowired
//    TaskRepo taskRepo;
//
//    @Autowired
//    TaskService taskService;
//
//    @Autowired
//    EmployeeService employeeService;
//
//    @Autowired
//    SimpleEmailService simpleEmailService;
//
//    @Autowired
//    SettingsRepo settingsRepo;
//
//    public KWNDto toFormKWN(KWN kwn) {
//
//        String pattern = "yyyy-MM-dd";
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
//
//
//        KWNDto KWNDto = new KWNDto();
//        KWNDto.setNrKwn(kwn.getNrKwn());
//        KWNDto.setNazwaWyrobu(kwn.getNazwaWyrobu());
//        KWNDto.setIlosc(kwn.getIlosc());
//        KWNDto.setObszar(kwn.getObszar());
//        KWNDto.setTypilosci(kwn.getTypilosci());
//        KWNDto.setZglaszajacyNiezgodnosc(kwn.getZglaszajacyNiezgodnosc());
//        KWNDto.setKomOrg(kwn.getKomOrg());
//        KWNDto.setDataZgloszenia(simpleDateFormat.format(kwn.getDataZgloszenia()));
//        String zlecenia = "";
//        for (Zlecenia z : kwn.getNrZlecenia()) {
//            if (zlecenia == "") {
//                zlecenia = z.getNrZlecenia();
//            } else {
//                zlecenia = zlecenia + ", " + z.getNrZlecenia();
//            }
//        }
//
//        KWNDto.setNrZlecenia(zlecenia);
//        KWNDto.setDzialWydzial(kwn.getDzialWydzial());
//        KWNDto.setWykryteNaEtapie(kwn.getWykryteNaEtapie());
//        KWNDto.setNrPtotOdb(kwn.getNrPtotOdb());
//
//        KWNDto.setOpisNiezgodnosci(kwn.getOpisNiezgodnosci());
//        KWNDto.setPotencjalnaPrzyczyna(kwn.getPotencjalnaPrzyczyna());
//        KWNDto.setCecha(kwn.getCecha());
//        KWNDto.setDodatkowyOpisNiezgodnosci(kwn.getDodatkowyOpisNiezgodnosci());
//
//
//        KWNDto.setDataSpotkaniaZ(simpleDateFormat.format(kwn.getDataSpotkaniaZ()));
//
//        KWNDto.setModerator(kwn.getModerator().getEmployee().getId());
//        KWNDto.setOsobaOdp(kwn.getOsobaOdp().getId());
//        KWNDto.setNiezgodnoscDotyczy(kwn.getNiezgodnoscDotyczy());
//        KWNDto.setNiezgodnoscDotyczy(kwn.getNiezgodnoscDotyczy());
//        KWNDto.setRodzajPrzyczyny(kwn.getRodzajPrzyczyny());
//        KWNDto.setDecyzjaZKWN(kwn.getDecyzjaZKWN());
//        KWNDto.setUzasadnienie(kwn.getUzasadnienie());
//        KWNDto.setZzreklamowac(kwn.getZzreklamowac());
//        KWNDto.setZzuruchomic(kwn.getZzuruchomic());
//        KWNDto.setZzotworzyc(kwn.getZzotworzyc());
//        KWNDto.setUwagi(kwn.getUwagi());
//        List<TableTPDto> tableTPDtos = new ArrayList<>();
//        if (kwn.getTaskList() != null && kwn.getTaskList().size() > 0) {
//            for (Task task : kwn.getTaskList()) {
//                TableTPDto tableTPDto = new TableTPDto();
//                tableTPDto.setEmployee(task.getEmployee().getId());
//                if (task.getDeadLine() != null) {
//                    tableTPDto.setTerminRealizacji(simpleDateFormat.format(task.getDeadLine()));
//                }
//                tableTPDto.setTrybPostepowania(task.getContent());
//                tableTPDtos.add(tableTPDto);
//            }
//        }
//        KWNDto.setTableTPDtos(tableTPDtos);
//
//        List<TablePCDto> tablePCDtos = new ArrayList<>();
//        if (kwn.getPcList() != null && kwn.getPcList().size() > 0) {
//            for (PC pc : kwn.getPcList()) {
//                TablePCDto tablePCDto = new TablePCDto();
//                tablePCDto.setImieNazwisko(pc.getImieNazwisko());
//                tablePCDto.setSymbolDzialu(pc.getSymbolDzialu());
//                tablePCDto.setEmployee(pc.getEmployee().getId());
//                tablePCDtos.add(tablePCDto);
//            }
//        }
//        KWNDto.setTablePCDtos(tablePCDtos);
//
//        return KWNDto;
//    }
//
//    public void addKWNtoTask(KWN kwn) {
//        for (Task task : kwn.getTaskList()) {
//            task.setKwnId(kwn.getId());
//            task.setKwnName(kwn.getNrKwn());
//            taskRepo.save(task);
//        }
//    }
//
//    public Note toNote(NoteDto noteDto) {
//
//        Note note = new Note();
//        note.setContent(noteDto.getContent());
//        note.setDate(new Date());
//        note.setEmployee(employeeService.getCurrentEmployee());
//        return note;
//    }
//
//
//    public KWN toKWN(KWNDto KWNDto, KWN kwn) {
//
//        kwn.setNrKwn(KWNDto.getNrKwn());
//        kwn.setNazwaWyrobu(KWNDto.getNazwaWyrobu());
//        kwn.setIlosc(KWNDto.getIlosc());
//        kwn.setObszar(KWNDto.getObszar());
//        kwn.setZglaszajacyNiezgodnosc(KWNDto.getZglaszajacyNiezgodnosc());
//        kwn.setKomOrg(KWNDto.getKomOrg());
//        try {
//            kwn.setDataZgloszenia(new SimpleDateFormat("yyyy-MM-dd").parse(KWNDto.getDataZgloszenia()));
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//
//        kwn.setNiezgodnoscDotyczy(KWNDto.getNiezgodnoscDotyczy());
//        kwn.setCecha(KWNDto.getCecha());
//        kwn.setDodatkowyOpisNiezgodnosci(KWNDto.getDodatkowyOpisNiezgodnosci());
//        kwn.setRodzajPrzyczyny(KWNDto.getRodzajPrzyczyny());
//        Employee employee = employeeRepo.findOne(KWNDto.getModerator());
//        Moderator moderator = new Moderator();
//        moderator.setDzial(employee.getDepartment());
//        moderator.setImieiNazwisko(employee.getFirstAndLastName());
//        moderator.setEmployee(employee);
//        kwn.setModerator(moderator);
//        if (KWNDto.getZzuruchomic().equals("tak")) {
//            Employee osobaOdp = employeeRepo.findOne(KWNDto.getOsobaOdp());
//            kwn.setOsobaOdp(osobaOdp);
//        } else {
//            Employee osobaOdp = employeeService.getAllEmployee().get(0);
//            kwn.setOsobaOdp(osobaOdp);
//        }
//
//        kwn.setNrZlecenia(splitText(KWNDto.getNrZlecenia(), ","));
//
//        kwn.setTypilosci(KWNDto.getTypilosci());
//        kwn.setDzialWydzial(KWNDto.getDzialWydzial());
//        kwn.setWykryteNaEtapie(KWNDto.getWykryteNaEtapie());
//        kwn.setNrPtotOdb(KWNDto.getNrPtotOdb());
//        kwn.setOpisNiezgodnosci(KWNDto.getOpisNiezgodnosci());
//        kwn.setPotencjalnaPrzyczyna(KWNDto.getPotencjalnaPrzyczyna());
//        try {
//            kwn.setDataSpotkaniaZ(new SimpleDateFormat("yyyy-MM-dd").parse(KWNDto.getDataSpotkaniaZ()));
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        kwn.setDecyzjaZKWN(KWNDto.getDecyzjaZKWN());
//        kwn.setUzasadnienie(KWNDto.getUzasadnienie());
//        kwn.setZzreklamowac(KWNDto.getZzreklamowac());
//        kwn.setZzuruchomic(KWNDto.getZzuruchomic());
//        kwn.setZzotworzyc(KWNDto.getZzotworzyc());
//        kwn.setUwagi(KWNDto.getUwagi());
//        kwn.setTypilosci(KWNDto.getTypilosci());
//        if (kwn.getTaskList().size()==0) {
//            List<Task> taskList = new ArrayList<>();
//            if (KWNDto.getTableTPDtos() != null && KWNDto.getTableTPDtos().size() > 0) {
//                for (TableTPDto tableTPDto : KWNDto.getTableTPDtos()) {
//                    if (tableTPDto.getEmployee() != null) {
//                        Task task = new Task();
//                        try {
//                            Employee employee1 = employeeRepo.findOne(tableTPDto.getEmployee());
//                            task.setEmployee(employee1);
//
//                            if (employee1.getFirstAndLastName().equals(" ------ ")){
//                                task.setStatus(Status.WYSLANY);
//                            }else {
//                                task.setStatus(Status.AKTYWNY);
//                            }
//
//
//                            if (!tableTPDto.getTerminRealizacji().equals("")) {
//                                task.setDeadLine(new SimpleDateFormat("yyyy-MM-dd").parse(tableTPDto.getTerminRealizacji()));
//                            } else {
//                                task.setDeadLine(null);
//                                task.setStatus(Status.ZAKONCZONY);
//                            }
//                            task.setContent(tableTPDto.getTrybPostepowania());
//
//                            // wysyłanie e-maili
//                            Mail mail = new Mail();
//                            mail.setEmployee(employee1);
//                            System.out.println("======================= wysyłanie wiadomości " + employee1.getFirstAndLastName());
//                            System.out.println("Wysyłanie wiadomości e-mail do: " + task.getEmployee().getFirstAndLastName());
//                            mail.setSubject("Masz nowe zadanie do wykonania");
//                            mail.setTask(task);
//                            simpleEmailService.send(mail, 1, null);
//                            //            tpRepo.save(tp);
//                            taskList.add(task);
//                        } catch (ParseException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            }
//            kwn.setTaskList(taskList);
//        }
//
//
//        List<PC> pcList = new ArrayList<>();
//        if (KWNDto.getTablePCDtos() != null && KWNDto.getTablePCDtos().size() > 0) {
//            for (TablePCDto tablePCDto : KWNDto.getTablePCDtos()) {
//                if(tablePCDto.getEmployee() != null){
//
//                    PC pc = new PC();
//                    Employee employee1 = employeeRepo.findOne(tablePCDto.getEmployee());
//                    pc.setImieNazwisko(employee1.getFirstAndLastName());
//                    pc.setSymbolDzialu(employee1.getDepartment());
//                    pc.setEmployee(employee1);
//                    pcList.add(pc);
//                }
//            }
//        }
//
//        kwn.setPcList(pcList);
//        return kwn;
//    }
//
//
//    public List<Zlecenia> splitText(String textToSplit, String x) {
//        textToSplit = textToSplit.replaceAll("\\s", "");
//        String[] stringList = textToSplit.split(x);
//        List<Zlecenia> zlecenia = new ArrayList<>();
//        for (String s : stringList) {
//            zlecenia.add(new Zlecenia(s));
//        }
//        return zlecenia;
//    }
//
//    public static String joinList(List<String> listToJoin, String x) {
//        return String.join(x, listToJoin);
//    }
//
//
//    public Employee mapToEmployee(EmployeeDto employeeDto) {
//        Employee employee = new Employee();
//        employee.setUserName(employeeDto.getUserName());
//        employee.setFirstAndLastName(employeeDto.getFirstAndLastName());
//        employee.setDepartment(employeeDto.getDepartment());
//        employee.setEmail(employeeDto.getEmail());
//
//        return employee;
//    }
//
//
//    public EmployeeDto mapToEmployeeDto(Employee employee) {
//        EmployeeDto employeeDto = new EmployeeDto();
//        employeeDto.setUserName(employee.getUserName());
//        employeeDto.setFirstAndLastName(employee.getFirstAndLastName());
//        employeeDto.setDepartment(employee.getDepartment());
//        employeeDto.setEmail(employee.getEmail());
//        return employeeDto;
//    }
//
//}
