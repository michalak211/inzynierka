package pl.mrozek.inzynierka.mapper;

import org.springframework.stereotype.Component;
import pl.mrozek.inzynierka.Dto.KoktajlForm;
import pl.mrozek.inzynierka.Dto.SkladnikP;
import pl.mrozek.inzynierka.Entity.bar.Butelka;
import pl.mrozek.inzynierka.Entity.przepis.Alkohol;
import pl.mrozek.inzynierka.Entity.przepis.Koktajl;
import pl.mrozek.inzynierka.Entity.przepis.SkladnikB;
import pl.mrozek.inzynierka.Entity.skladniki.Inny;
import pl.mrozek.inzynierka.Entity.skladniki.Sok;
import pl.mrozek.inzynierka.Entity.skladniki.Syrop;
import pl.mrozek.inzynierka.Entity.skladniki.Typ;
import pl.mrozek.inzynierka.Repo.*;

import java.util.ArrayList;
import java.util.List;

@Component
public class Mapper {
    private final
    AlkoholRepo alkoholRepo;
    private final
    SkladnikRepo skladnikRepo;
    private final
    SokRepo sokRepo;
    private final
    SyropRepo syropRepo;
    private final TypRepo typRepo;
    private final SkladnikBRepo skladnikBRepo;
    private final InnyRepo innyRepo;
    private final ButelkaRepo butelkaRepo;


    public Mapper(AlkoholRepo alkoholRepo, SkladnikRepo skladnikRepo, SokRepo sokRepo, SyropRepo syropRepo, TypRepo typRepo, SkladnikBRepo skladnikBRepo, InnyRepo innyRepo, ButelkaRepo butelkaRepo) {
        this.alkoholRepo = alkoholRepo;
        this.skladnikRepo = skladnikRepo;
        this.sokRepo = sokRepo;
        this.syropRepo = syropRepo;
        this.typRepo = typRepo;
        this.skladnikBRepo = skladnikBRepo;
        this.innyRepo = innyRepo;
        this.butelkaRepo = butelkaRepo;
    }


    public Koktajl toKoktajl(Koktajl koktajl, KoktajlForm koktajlForm) {

        koktajl.setNazwa(koktajlForm.getNazwa());
//        if (koktajlForm.getKlasa() != null) {
//            koktajl.setKlasa(koktajlForm.getKlasa());
//        }
//        if (koktajlForm.getKlasa() != null) {
//            koktajl.setKlasa(koktajlForm.getKlasa());
//        }
//        if (koktajlForm.getSzklo() != null) {
//            koktajl.setSzklo(koktajlForm.getSzklo());
//        }
//
//        koktajl.setVegan(koktajlForm.getVegan().equals("tak"));
//
//        if (koktajlForm.getZdobienie() != null) {
//            koktajl.setZdobienie(koktajlForm.getZdobienie());
//        }
//
//
//        if (koktajlForm.getOpisPrzyrzadzenia() != null) {
//            koktajl.setOpisPrzyzadzenia(koktajlForm.getOpisPrzyrzadzenia());
//        }
//        if (koktajlForm.getOcena() != null) {
//            koktajl.setOpisPrzyzadzenia(koktajlForm.getOpisPrzyrzadzenia());
//        }

//        if (koktajl.getId()!=null) {
//            koktajlForm.setId(koktajl.getId());
//        }else {
//            koktajlForm.setId(0);
//        }


//        koktajl.setSkladnikBList(new ArrayList<>());
        if (koktajlForm.getListaSkladnikow() != null) {
            for (SkladnikP skladnikP : koktajlForm.getListaSkladnikow()) {

                if (skladnikP.getRodzaj() > 0) {
                    SkladnikB skladnikB = new SkladnikB();
                    skladnikB.setIlosc(skladnikP.getIloscML());
                    skladnikB.setOpisDodatkowy(skladnikP.getOpisDodatkowy());

                    switch (skladnikP.getRodzaj()) {
                        case 1:

                            if (skladnikP.isNowy()) {
                                Alkohol alkohol = alkoholRepo.findByNazwaEquals(skladnikP.getNazwa());
                                Typ newTyp;

                                if (typRepo.findByNazwaEquals(skladnikP.getTyp()) == null) {
                                    newTyp = new Typ();
                                    newTyp.setNazwa(skladnikP.getTyp());
                                    newTyp.setAlkoholID(alkohol.getId());
                                    typRepo.save(newTyp);
                                    alkohol.getTypList().add(newTyp);
                                    alkoholRepo.save(alkohol);
                                } else {
                                    newTyp = typRepo.findByNazwaEquals(skladnikP.getTyp());
                                }
                                skladnikB.setSkladnik(newTyp);

                            } else if (skladnikP.isNowyAlko() && alkoholRepo.findByNazwaEquals(skladnikP.getNazwa()) == null) {

                                Alkohol alkohol = new Alkohol();
                                alkohol.setNazwa(skladnikP.getNazwa());
                                Typ newTyp = new Typ();
                                newTyp.setNazwa(skladnikP.getTyp());
                                alkoholRepo.save(alkohol);
                                newTyp.setAlkoholID(alkohol.getId());
                                typRepo.save(newTyp);

                                List<Typ> alkolist = new ArrayList<>();
                                alkolist.add(newTyp);
                                alkohol.setTypList(alkolist);
                                alkoholRepo.save(alkohol);

                                skladnikB.setSkladnik(newTyp);

                            } else {
                                Typ typ = typRepo.findByNazwaEquals(skladnikP.getTyp());
                                skladnikB.setSkladnik(typ);
                            }

                            break;
                        case 2:
                            if (skladnikP.isNowy()) {
                                Sok sok;
                                if (sokRepo.findByNazwaEquals(skladnikP.getNazwa()) != null) {
                                    sok = new Sok();
                                    sok.setNazwa(skladnikP.getNazwa());
                                    sokRepo.save(sok);
                                } else {
                                    sok = sokRepo.findByNazwaEquals(skladnikP.getNazwa());
                                }
                                skladnikB.setSkladnik(sok);
                            } else {
                                skladnikB.setSkladnik(sokRepo.findByNazwaEquals(skladnikP.getNazwa()));
                            }
                            break;
                        case 3:
                            if (skladnikP.isNowy()) {
                                Syrop syrop;
                                if (sokRepo.findByNazwaEquals(skladnikP.getNazwa()) != null) {
                                    syrop = new Syrop();
                                    syrop.setNazwa(skladnikP.getNazwa());
                                    syropRepo.save(syrop);
                                } else {
                                    syrop = syropRepo.findByNazwaEquals(skladnikP.getNazwa());
                                }
                                skladnikB.setSkladnik(syrop);
                            } else {
                                skladnikB.setSkladnik(syropRepo.findByNazwaEquals(skladnikP.getNazwa()));

                            }
                            break;
                        case 4:
                            if (skladnikP.isNowy()) {

                                Inny inny;
                                if (innyRepo.findByNazwaEquals(skladnikP.getNazwa()) != null) {
                                    inny = new Inny();
                                    inny.setNazwa(skladnikP.getNazwa());
                                    innyRepo.save(inny);
                                } else {
                                    inny = innyRepo.findByNazwaEquals(skladnikP.getNazwa());
                                }
                                skladnikB.setSkladnik(inny);
                            } else {
                                skladnikB.setSkladnik(innyRepo.findByNazwaEquals(skladnikP.getNazwa()));
                            }
                            break;
                    }
                    skladnikBRepo.save(skladnikB);
//                    koktajl.getSkladnikBList().add(skladnikB);
                }

            }
        }

        return koktajl;
    }

    public KoktajlForm toKoktajlForm(Koktajl koktajl) {
        KoktajlForm koktajlForm = new KoktajlForm();

        koktajlForm.setNazwa(koktajl.getNazwa());

//        if (koktajl.getKlasa() != null) {
//            koktajlForm.setKlasa(koktajl.getKlasa());
//        }
//        if (koktajl.getSzklo() != null) {
//            koktajlForm.setSzklo(koktajl.getSzklo());
//        }
//        if (koktajl.getOpisPrzyzadzenia() != null) {
//            koktajlForm.setOpisPrzyrzadzenia(koktajl.getOpisPrzyzadzenia());
//        }
//        if (koktajl.getZdobienie() != null) {
//            koktajlForm.setZdobienie(koktajl.getZdobienie());
//        }
//        koktajlForm.setOcena(koktajl.getOcena());
//
//        if (koktajl.isVegan()) {
//            koktajlForm.setVegan("tak");
//        } else {
//            koktajlForm.setVegan("nie");
//        }
//
//        if (koktajl.getId() != null) {
//            koktajlForm.setId(koktajl.getId());
//        } else {
//            koktajlForm.setId(0);
//        }


        List<SkladnikP> skladnikPList = new ArrayList<>();

//        for (SkladnikB skladnikB : koktajl.getSkladnikBList()) {
//            SkladnikP skladnikP = new SkladnikP();
//            skladnikP.setIloscML(skladnikB.getIlosc());
//            skladnikP.setOpisDodatkowy(skladnikB.getOpisDodatkowy());
//
//            if (skladnikB.getSkladnik() instanceof Typ) {
//                skladnikP.setRodzaj(1);
//                skladnikP.setTyp(skladnikB.getSkladnik().getNazwa());
//                if (alkoholRepo.findById(((Typ) skladnikB.getSkladnik()).getAlkoholID()).isPresent()) {
//                    Alkohol alkohol = alkoholRepo.findById(((Typ) skladnikB.getSkladnik()).getAlkoholID()).get();
//                    skladnikP.setNazwa(alkohol.getNazwa());
//                }
//            } else if (skladnikB.getSkladnik() instanceof Sok) {
//                skladnikP.setRodzaj(2);
//                skladnikP.setNazwa(skladnikB.getSkladnik().getNazwa());
//            } else if (skladnikB.getSkladnik() instanceof Syrop) {
//                skladnikP.setRodzaj(3);
//                skladnikP.setNazwa(skladnikB.getSkladnik().getNazwa());
//            } else if (skladnikB.getSkladnik() instanceof Inny) {
//                skladnikP.setRodzaj(4);
//                skladnikP.setNazwa(skladnikB.getSkladnik().getNazwa());
//            } else {
//                continue;
//            }
//
//            skladnikPList.add(skladnikP);
//        }

        koktajlForm.setListaSkladnikow(skladnikPList);


        return koktajlForm;
    }

    public Butelka butlaToBase(Butelka butelka){


        if (butelka.isNewAlko()) {
            boolean repeatsAlko=alkoholRepo.findByNazwaEquals(butelka.getAlkoholNazwa())!=null;

            if (repeatsAlko){
                butelka.setAlkoholId(alkoholRepo.findByNazwaEquals(butelka.getAlkoholNazwa()).getId());
            } else {
                Alkohol alkohol = new Alkohol();
                alkohol.setNazwa(butelka.getAlkoholNazwa());
                alkoholRepo.save(alkohol);

                List<Typ> typList= new ArrayList<>();
                Typ typ = new Typ();
                typ.setAlkoholID(alkohol.getId());
                typ.setNazwa(butelka.getTypNazwa());
                typRepo.save(typ);
                typList.add(typ);

                alkohol.setTypList(typList);
                alkoholRepo.save(alkohol);

                butelka.setAlkoholId(alkohol.getId());
                butelka.setTypId(typ.getId());
            }

        }else if (butelka.isNewTyp()){
            Alkohol alkohol=alkoholRepo.findByNazwaEquals(butelka.getAlkoholNazwa());
            butelka.setAlkoholId(alkohol.getId());

            Typ typ= new Typ();
            typ.setAlkoholID(alkohol.getId());
            typ.setNazwa(butelka.getTypNazwa());
            typRepo.save(typ);

            alkohol.getTypList().add(typ);
            alkoholRepo.save(alkohol);
            butelka.setTypId(typ.getId());

        }else {
            butelka.setAlkoholId(alkoholRepo.findByNazwaEquals(butelka.getAlkoholNazwa()).getId());
            butelka.setTypId(typRepo.findByNazwaEquals(butelka.getTypNazwa()).getId());
        }

        return butelka;
    }
    public Butelka butlaToForm(Butelka butelka){

        if (alkoholRepo.findById(butelka.getAlkoholId()).isPresent()){
            Alkohol alkohol=alkoholRepo.findById(butelka.getAlkoholId()).get();
            butelka.setAlkoholNazwa(alkohol.getNazwa());
        }
        if (typRepo.findById(butelka.getTypId()).isPresent()){
            Typ typ=typRepo.findById(butelka.getTypId()).get();
            butelka.setTypNazwa(typ.getNazwa());
        }
        return butelka;
    }

}
