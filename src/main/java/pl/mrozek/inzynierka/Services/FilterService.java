package pl.mrozek.inzynierka.Services;


import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import pl.mrozek.inzynierka.Dto.KoktajlForm;
import pl.mrozek.inzynierka.Dto.SkladnikP;
import pl.mrozek.inzynierka.Entity.bar.Barek;
import pl.mrozek.inzynierka.Entity.bar.Butelka;
import pl.mrozek.inzynierka.Entity.przepis.Alkohol;
import pl.mrozek.inzynierka.Entity.przepis.Koktajl;
import pl.mrozek.inzynierka.Entity.przepis.SkladnikB;
import pl.mrozek.inzynierka.Entity.skladniki.Inny;
import pl.mrozek.inzynierka.Entity.skladniki.Sok;
import pl.mrozek.inzynierka.Entity.skladniki.Syrop;
import pl.mrozek.inzynierka.Entity.skladniki.Typ;
import pl.mrozek.inzynierka.Repo.*;
import pl.mrozek.inzynierka.mapper.Mapper;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
public class FilterService {


    private final KoktailRepo koktailRepo;
    private final Mapper mapper;
    private final BarekRepo barekRepo;
    private final AlkoholRepo alkoholRepo;
    private final SokRepo sokRepo;
    private final SyropRepo syropRepo;
    private final InnyRepo innyRepo;
    private final TypRepo typRepo;
    private final UserService userService;

    public FilterService(KoktailRepo koktailRepo, Mapper mapper, BarekRepo barekRepo, AlkoholRepo alkoholRepo, SokRepo sokRepo, SyropRepo syropRepo, InnyRepo innyRepo, TypRepo typRepo, UserService userService) {
        this.koktailRepo = koktailRepo;
        this.mapper = mapper;
        this.barekRepo = barekRepo;
        this.alkoholRepo = alkoholRepo;
        this.sokRepo = sokRepo;
        this.syropRepo = syropRepo;
        this.innyRepo = innyRepo;
        this.typRepo = typRepo;
        this.userService = userService;
    }

    public List<KoktajlForm> completeFilters(KoktajlForm filterSet) {
        List<Koktajl> koktajlList = filterCoctails(filterSet);


        Barek barek;
        if (userService.isAdmin()) {
            barek = barekRepo.findById(Long.valueOf(filterSet.getOpisPrzyrzadzenia())).orElse(null);
        } else {
            barek = barekRepo.findByNazwaEquals("barek mieszkanie");
        }
        List<KoktajlForm> koktajlFormList;
        if (barek != null) {
            if (filterSet.isPoBarku()) koktajlList = filterByBar(koktajlList, barek.getId());
            koktajlFormList = mapper.koktajlListToForm(koktajlList);
            koktajlFormList = checkSkladnikAccesability(barek.getId(), koktajlFormList);
            return koktajlFormList;
        }
        return mapper.koktajlListToForm(koktajlList);
    }


    public List<KoktajlForm> checkSkladnikAccesability(Long barId, List<KoktajlForm> koktajlFormList) {
        Barek barek = barekRepo.findById(barId).orElse(null);
        if (barek == null) return null;
        HashSet<Long> typList = getBarekTypesId(barek);
        typList.addAll(addDowolnyId(barek, typList));

        for (Sok sok : barek.getListSok()) typList.add(sok.getId());
        for (Syrop syrop : barek.getListSyrop()) typList.add(syrop.getId());
        for (Inny inny : barek.getListInny()) typList.add(inny.getId());

        for (KoktajlForm koktajlForm : koktajlFormList) {
            for (SkladnikP skladnikP : koktajlForm.getListaSkladnikow()) {
                skladnikP.setPresent(setSkladnikPresence(skladnikP, typList));
            }
        }
        return koktajlFormList;
    }


    private boolean setSkladnikPresence(SkladnikP skladnikP, HashSet<Long> typList) {
        return typList.contains(skladnikP.getId());
    }

    private boolean checkKoktailPossibility(Barek barek, Koktajl koktajl) {

        HashSet<Long> typList = getBarekTypesId(barek);
        typList.addAll(addDowolnyId(barek, typList));
        for (Sok sok : barek.getListSok()) typList.add(sok.getId());
        for (Syrop syrop : barek.getListSyrop()) typList.add(syrop.getId());
        for (Inny inny : barek.getListInny()) typList.add(inny.getId());

        for (SkladnikB skladnikB : koktajl.getSkladnikBList()) {
            if (typList.contains(skladnikB.getSkladnikId())) continue;
            return false;
        }
        return true;
    }


    private HashSet<Long> getBarekTypesId(Barek barek) {
        HashSet<Long> typList = new HashSet<>();

        for (Butelka butelka : barek.getButelkaList()) {
            typList.add(butelka.getTypId());
        }
        return typList;
    }

    private HashSet<Long> addDowolnyId(Barek barek, HashSet<Long> alkoList) {

        HashSet<Long> checkedAlko = new HashSet<>();

        for (Butelka butelka : barek.getButelkaList()) {
            if (checkedAlko.contains(butelka.getAlkoholId())) continue;
            Alkohol alkohol = alkoholRepo.findById(butelka.getAlkoholId()).
                    orElse(new Alkohol());

            for (Typ typ : alkohol.getTypList()) {
                if (typ.getNazwa().equals("Dowolny")) {
                    if (!alkoList.contains(typ.getId())) {
                        alkoList.add(typ.getId());
                        checkedAlko.add(typ.getAlkoholID());
                    }
                    break;
                }
            }
        }

        return alkoList;
    }

    // wtf co to jest?!
    public List<SkladnikP> getSkladniksForFilters() {
        List<SkladnikP> skladnikPList = new ArrayList<>();

        for (Sok sok : sokRepo.findAll()) {
            SkladnikP skladnikP = new SkladnikP();
            skladnikP.setId(sok.getId());
            skladnikP.setNazwa(sok.getNazwa());
            skladnikPList.add(skladnikP);
        }


        return skladnikPList;
    }

    public List<Koktajl> filterCoctails(KoktajlForm koktajlForm) {
        Koktajl koktajl = new Koktajl();
        if (koktajlForm.getNazwa() != null) koktajl.setNazwa(koktajlForm.getNazwa());
        if (koktajlForm.getKlasa() != null) koktajl.setKlasa(koktajlForm.getKlasa());
        if (koktajlForm.getSzklo() != null) koktajl.setSzklo(koktajlForm.getSzklo());
        if (koktajlForm.getZdobienie() != null) koktajl.setZdobienie(koktajlForm.getZdobienie());
        if (koktajlForm.getVegan().equals("TAK")) {
            koktajl.setVegan(true);
        } else {
            koktajl.setVegan(false);
        }
        List<Koktajl> koktajlList = findBySet(koktajl);
        koktajlList = filterBySkladnik(koktajlList, koktajlForm);

        return koktajlList;
    }

    public List<Koktajl> filterByBar(List<Koktajl> koktajlList, long barId) {
        Barek barek = barekRepo.findById(barId).orElse(null);
        if (barek == null) return koktajlList;
        List<Koktajl> filtered = new ArrayList<>();
        for (Koktajl koktajl1 : koktajlList) {
            if (checkKoktailPossibility(barek, koktajl1)) filtered.add(koktajl1);
        }
        return filtered;
    }

    private List<Koktajl> filterBySkladnik(List<Koktajl> koktajlList, KoktajlForm koktajlForm) {

        if (koktajlForm.getListaSkladnikow()== null) return koktajlList;
        for (SkladnikP skladnikP : koktajlForm.getListaSkladnikow()) {
            if (skladnikP.getRodzaj() == 0) continue;
            if (skladnikP.getRodzaj() == 1 && skladnikP.getTyp() == null) {
                koktajlList = filterByDowolny(koktajlList, Long.parseLong(skladnikP.getNazwa()));
                continue;
            }
            if (skladnikP.getTyp() != null) {
                if (checkForDowolny(Long.parseLong(skladnikP.getTyp()))) {
                    koktajlList = filterByDowolny(koktajlList, Long.parseLong(skladnikP.getNazwa()));
                    continue;
                }

                koktajlList = removeKoktailIfWrong(koktajlList, Long.parseLong(skladnikP.getTyp()));
                continue;
            }
            koktajlList = removeKoktailIfWrong(koktajlList, Long.parseLong(skladnikP.getNazwa()));
        }
        return koktajlList;
    }

    private boolean checkForDowolny(long id) {
        Typ typ = typRepo.findById(id).orElse(null);
        if (typ == null) return false;
        return typ.getNazwa().equals("Dowolny");
    }


    private List<Koktajl> filterByDowolny(List<Koktajl> koktajlList, long alkoId) {
        List<Koktajl> filtered = new ArrayList<>();
        Alkohol alkohol = alkoholRepo.findById(alkoId).orElse(null);
        if (alkohol == null) return koktajlList;
        List<Long> idList = new ArrayList<>();
        for (Typ typ : alkohol.getTypList()) idList.add(typ.getId());

        for (Koktajl koktajl : koktajlList) {
            boolean match = koktajl.getSkladnikBList().stream().anyMatch(o ->
                    idList.stream().anyMatch(o2 ->
                            o2 == o.getSkladnikId()));
            if (match) filtered.add(koktajl);
        }
        return filtered;
    }


    private List<Koktajl> removeKoktailIfWrong(List<Koktajl> koktajlList, long id) {
        List<Koktajl> filtered = new ArrayList<>();
        for (Koktajl koktajl : koktajlList) {
            boolean match = koktajl.getSkladnikBList().stream().anyMatch(o -> o.getSkladnikId() == id);
            if (match) filtered.add(koktajl);
        }
        return filtered;
    }


    private List<Koktajl> findBySet(Koktajl koktajl) {

        return koktailRepo.findAll((Specification<Koktajl>) (root, query, criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (koktajl.getNazwa() != null) {
                predicates.add(criteriaBuilder.and(criteriaBuilder.like(root.get("nazwa"), "%" + koktajl.getNazwa() + "%")));
            }
            if (koktajl.getKlasa() != null) {
                predicates.add(criteriaBuilder.and(criteriaBuilder.like(root.get("klasa"), "%" + koktajl.getKlasa() + "%")));
            }
            if (koktajl.getOcena() != 0) {
                predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("ocena"), koktajl.getOcena())));
            }
            if (koktajl.getSzklo() != null) {
                predicates.add(criteriaBuilder.and(criteriaBuilder.like(root.get("szklo"), "%" + koktajl.getSzklo() + "%")));
            }
            if (koktajl.isVegan()) {
                predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("vegan"), koktajl.isVegan())));
            }
            if (koktajl.getZdobienie() != null) {
                predicates.add(criteriaBuilder.and(criteriaBuilder.like(root.get("zdobienie"), "%" + koktajl.getZdobienie() + "%")));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
        });
    }


}
