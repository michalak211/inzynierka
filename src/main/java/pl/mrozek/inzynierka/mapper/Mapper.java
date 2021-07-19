package pl.mrozek.inzynierka.mapper;

import org.springframework.stereotype.Component;
import pl.mrozek.inzynierka.Dto.KoktajlForm;
import pl.mrozek.inzynierka.Dto.SkladnikP;
import pl.mrozek.inzynierka.Entity.bar.Butelka;
import pl.mrozek.inzynierka.Entity.przepis.Alkohol;
import pl.mrozek.inzynierka.Entity.przepis.Koktajl;
import pl.mrozek.inzynierka.Entity.przepis.SkladnikB;
import pl.mrozek.inzynierka.Entity.skladniki.*;
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


    public List<KoktajlForm> toForms(List<Koktajl> koktajlList){
        List<KoktajlForm> koktajlFormList= new ArrayList<>();

        for (Koktajl koktajl:koktajlList){
            koktajlFormList.add(toKoktajlForm(koktajl));
        }

        return koktajlFormList;
    }



    public Koktajl toKoktajl(Koktajl koktajl, KoktajlForm koktajlForm) {

        koktajl.setNazwa(koktajlForm.getNazwa());
        if (koktajlForm.getKlasa() != null) {
            koktajl.setKlasa(koktajlForm.getKlasa());
        }
        if (koktajlForm.getKlasa() != null) {
            koktajl.setKlasa(koktajlForm.getKlasa());
        }
        if (koktajlForm.getSzklo() != null) {
            koktajl.setSzklo(koktajlForm.getSzklo());
        }

        koktajl.setVegan(koktajlForm.getVegan().equals("tak"));

        if (koktajlForm.getZdobienie() != null) {
            koktajl.setZdobienie(koktajlForm.getZdobienie());
        }


        if (koktajlForm.getOpisPrzyrzadzenia() != null) {
            koktajl.setOpisPrzyzadzenia(koktajlForm.getOpisPrzyrzadzenia());
        }
        if (koktajlForm.getOcena() != null) {
            koktajl.setOpisPrzyzadzenia(koktajlForm.getOpisPrzyrzadzenia());
        }

//        if (koktajl.getId()!=null) {
//            koktajlForm.setId(koktajl.getId());
//        }else {
//            koktajlForm.setId(0);
//        }


        koktajl.setSkladnikBList(new ArrayList<>());
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
                                skladnikB.setSkladnikId(newTyp.getId());

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

                                skladnikB.setSkladnikId(newTyp.getId());

                            } else {
                                Typ typ = typRepo.findByNazwaEquals(skladnikP.getTyp());
                                skladnikB.setSkladnikId(typ.getId());
                            }

                            break;
                        case 2:
                            if (skladnikP.isNowy()) {
                                Sok sok;
                                if (sokRepo.findByNazwaEquals(skladnikP.getNazwa()) == null) {
                                    sok = new Sok();
                                    sok.setNazwa(skladnikP.getNazwa());
                                    sokRepo.save(sok);
                                } else {
                                    sok = sokRepo.findByNazwaEquals(skladnikP.getNazwa());
                                }
                                skladnikB.setSkladnikId(sok.getId());
                            } else {
                                skladnikB.setSkladnikId(sokRepo.findByNazwaEquals(skladnikP.getNazwa()).getId());
                            }
                            break;
                        case 3:
                            if (skladnikP.isNowy()) {
                                Syrop syrop;
                                if (sokRepo.findByNazwaEquals(skladnikP.getNazwa()) == null) {
                                    syrop = new Syrop();
                                    syrop.setNazwa(skladnikP.getNazwa());
                                    syropRepo.save(syrop);
                                } else {
                                    syrop = syropRepo.findByNazwaEquals(skladnikP.getNazwa());
                                }
                                skladnikB.setSkladnikId(syrop.getId());
                            } else {
                                skladnikB.setSkladnikId(syropRepo.findByNazwaEquals(skladnikP.getNazwa()).getId());

                            }
                            break;
                        case 4:
                            if (skladnikP.isNowy()) {

                                Inny inny;
                                if (innyRepo.findByNazwaEquals(skladnikP.getNazwa()) == null) {
                                    inny = new Inny();
                                    inny.setNazwa(skladnikP.getNazwa());
                                    innyRepo.save(inny);
                                } else {
                                    inny = innyRepo.findByNazwaEquals(skladnikP.getNazwa());
                                }
                                skladnikB.setSkladnikId(inny.getId());
                            } else {
                                skladnikB.setSkladnikId(innyRepo.findByNazwaEquals(skladnikP.getNazwa()).getId());
                            }
                            break;
                    }
                    skladnikBRepo.save(skladnikB);
                    koktajl.getSkladnikBList().add(skladnikB);
                }

            }
        }

        return koktajl;
    }

    public void setBaseInForm(KoktajlForm koktajlForm, Koktajl koktajl) {

        koktajlForm.setNazwa(koktajl.getNazwa());

        if (koktajl.getKlasa() != null) {
            koktajlForm.setKlasa(koktajl.getKlasa());
        }
        if (koktajl.getSzklo() != null) {
            koktajlForm.setSzklo(koktajl.getSzklo());
        }
        if (koktajl.getOpisPrzyzadzenia() != null) {
            koktajlForm.setOpisPrzyrzadzenia(koktajl.getOpisPrzyzadzenia());
        }
        if (koktajl.getZdobienie() != null) {
            koktajlForm.setZdobienie(koktajl.getZdobienie());
        }
        koktajlForm.setOcena(koktajl.getOcena());

        if (koktajl.isVegan()) {
            koktajlForm.setVegan("tak");
        } else {
            koktajlForm.setVegan("nie");
        }

        if (koktajl.getId() != null) {
            koktajlForm.setId(koktajl.getId());
        } else {
            koktajlForm.setId(0);
        }
    }


    public SkladnikP skladnikBToSkladnikP(SkladnikB skladnikB) {
        SkladnikP skladnikP = toSkladnikP(skladnikB.getSkladnikId());

        skladnikP.setId(skladnikB.getSkladnikId());
        skladnikP.setIloscML(skladnikB.getIlosc());
        skladnikP.setOpisDodatkowy(skladnikB.getOpisDodatkowy());


        return skladnikP;
    }


    public KoktajlForm toKoktajlForm(Koktajl koktajl) {
        KoktajlForm koktajlForm = new KoktajlForm();

        setBaseInForm(koktajlForm, koktajl);

        List<SkladnikP> skladnikPList = new ArrayList<>();

        for (SkladnikB skladnikB : koktajl.getSkladnikBList()) {
            if (!skladnikRepo.findById(skladnikB.getSkladnikId()).isPresent()) continue;

            skladnikPList.add(skladnikBToSkladnikP(skladnikB));

        }

        koktajlForm.setListaSkladnikow(skladnikPList);


        return koktajlForm;
    }


    public Butelka butlaToForm(Butelka butelka) {

        if (alkoholRepo.findById(butelka.getAlkoholId()).isPresent()) {
            Alkohol alkohol = alkoholRepo.findById(butelka.getAlkoholId()).get();
            butelka.setAlkoholNazwa(alkohol.getNazwa());
        }
        if (typRepo.findById(butelka.getTypId()).isPresent()) {
            Typ typ = typRepo.findById(butelka.getTypId()).get();
            butelka.setTypNazwa(typ.getNazwa());
        }
        return butelka;
    }

    public SkladnikP toSkladnikP(Long id) {
        SkladnikP skladnikP = new SkladnikP();


        if (skladnikRepo.findById(id).isPresent()) {
            Skladnik skladnik = skladnikRepo.findById(id).get();
            skladnikP.setId(skladnik.getId());
            skladnikP.setNazwa(skladnik.getNazwa());

            if (skladnik instanceof Typ) {
                skladnikP.setRodzaj(1);
                skladnikP.setTyp(skladnik.getNazwa());
                if (alkoholRepo.findById(((Typ) skladnik).getAlkoholID()).isPresent()) {
                    Alkohol alkohol = alkoholRepo.findById(((Typ) skladnik).getAlkoholID()).get();
                    skladnikP.setNazwa(alkohol.getNazwa());
                }
            }


            if (skladnik instanceof Sok) {
                skladnikP.setRodzaj(2);
                skladnikP.setIloscML(((Sok) skladnik).getCenaZaLitr());
            }
            if (skladnik instanceof Syrop) {
                skladnikP.setRodzaj(3);
                skladnikP.setIloscML(((Syrop) skladnik).getCenaZaLitr());
                skladnikP.setOpisDodatkowy(((Syrop) skladnik).getPrzepis());
            }
            if (skladnik instanceof Inny) {
                skladnikP.setRodzaj(4);
                skladnikP.setIloscML(((Inny) skladnik).getCenaZaJednostke());
            }
        }

        return skladnikP;
    }

}
