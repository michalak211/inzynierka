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

    public FilterService(KoktailRepo koktailRepo, Mapper mapper, BarekRepo barekRepo, AlkoholRepo alkoholRepo, SokRepo sokRepo, SyropRepo syropRepo, InnyRepo innyRepo) {
        this.koktailRepo = koktailRepo;
        this.mapper = mapper;
        this.barekRepo = barekRepo;
        this.alkoholRepo = alkoholRepo;
        this.sokRepo = sokRepo;
        this.syropRepo = syropRepo;
        this.innyRepo = innyRepo;
    }


    public List<KoktajlForm> checkSkladnikAccesability(Long barId,List<KoktajlForm> koktajlFormList) {
//        List<KoktajlForm> koktajlFormList = getAllKoktajlForms();


        Barek barek = barekRepo.findById(barId).orElse(null);
        if (barek == null) return null;
        List<Long> typList = getBarekTypesId(barek);
        typList.addAll(addDowolnyId(barek, typList));

        List<Long> sokIds = new ArrayList<>();
        for (Sok sok : barek.getListSok()) {
            sokIds.add(sok.getId());
        }

        List<Long> syropIds = new ArrayList<>();
        for (Syrop syrop : barek.getListSyrop()) {
            syropIds.add(syrop.getId());
        }

        List<Long> innyIds = new ArrayList<>();
        for (Inny inny : barek.getListInny()) {
            innyIds.add(inny.getId());
        }

        for (KoktajlForm koktajlForm : koktajlFormList) {
            for (SkladnikP skladnikP : koktajlForm.getListaSkladnikow()) {
                skladnikP.setPresent(setSkladnikPresence(skladnikP, typList, sokIds, syropIds, innyIds));
            }
        }
        return koktajlFormList;
    }


    private boolean setSkladnikPresence(SkladnikP skladnikP, List<Long> typList, List<Long> sokIds,
                                        List<Long> syropIds, List<Long> innyIds) {
        if (typList.contains(skladnikP.getId())) return true;
        if (sokIds.contains(skladnikP.getId())) return true;
        if (syropIds.contains(skladnikP.getId())) return true;
        return innyIds.contains(skladnikP.getId());
    }

    private boolean checkKoktailPossibility(Barek barek, Koktajl koktajl) {

        List<Long> typList = getBarekTypesId(barek);
        typList.addAll(addDowolnyId(barek, typList));
        List<Long> sokIds = new ArrayList<>();
        List<Long> syropIds = new ArrayList<>();
        List<Long> innyIds = new ArrayList<>();

        for (Sok sok : barek.getListSok()) {
            sokIds.add(sok.getId());
        }
        for (Syrop syrop : barek.getListSyrop()) {
            syropIds.add(syrop.getId());
        }
        for (Inny inny : barek.getListInny()) {
            innyIds.add(inny.getId());
        }

        for (SkladnikB skladnikB : koktajl.getSkladnikBList()) {
            if (sokIds.contains(skladnikB.getSkladnikId())) continue;
            if (syropIds.contains(skladnikB.getSkladnikId())) continue;
            if (innyIds.contains(skladnikB.getSkladnikId())) continue;
            if (typList.contains(skladnikB.getSkladnikId())) continue;
            return false;
        }
        return true;
    }


    private List<Long> getBarekTypesId(Barek barek) {
        List<Long> typList = new ArrayList<>();

        for (Butelka butelka : barek.getButelkaList()) {
            if (!typList.contains(butelka.getTypId())) {
                typList.add(butelka.getTypId());
            }
        }
        return typList;
    }

    private List<Long> addDowolnyId(Barek barek, List<Long> alkoList) {

        List<Long> checkedAlko = new ArrayList<>();

        for (Butelka butelka : barek.getButelkaList()) {
            if (checkedAlko.contains(butelka.getAlkoholId())) continue;
            Alkohol alkohol = alkoholRepo.findById(butelka.getAlkoholId()).orElse(new Alkohol());

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

    public List<SkladnikP> getSkladniksForFilters(){
        List<SkladnikP> skladnikPList= new ArrayList<>();

        for (Sok sok: sokRepo.findAll()){
            SkladnikP skladnikP= new SkladnikP();
            skladnikP.setId(sok.getId());
            skladnikP.setNazwa(sok.getNazwa());
            skladnikPList.add(skladnikP);
        }





         return skladnikPList;
    }


    public List<Koktajl> findBySet(Koktajl koktajl) {

        return koktailRepo.findAll(new Specification<Koktajl>() {
            @Override
            public Predicate toPredicate(Root<Koktajl> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {

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
            }
        });
    }









}
