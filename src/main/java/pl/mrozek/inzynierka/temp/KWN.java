//package pl.mrozek.inzynierka.temp;
//
//import lombok.*;
//import org.hibernate.annotations.LazyCollection;
//import org.hibernate.annotations.LazyCollectionOption;
//import org.springframework.format.annotation.DateTimeFormat;
//import pl.pwpw.demo.ogolne.Entity.*;
//
//import javax.persistence.*;
//import java.io.Serializable;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
//@AllArgsConstructor
//@NoArgsConstructor
//@Getter
//@Setter
//@Entity
//@EqualsAndHashCode
//@ToString
//public class KWN implements Cloneable , Serializable, Comparable<KWN>{
//
//    @Id
//    @Column(name = "id")
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    private Long id;
//
//    @Column(name = "nrKwn")
//    private String nrKwn;
//
//    @Column(name = "nazwaWyrobu")
//    private String nazwaWyrobu;
//
//    @Column(name = "ilosc")
//    private String ilosc;
//
//    @Column(name = "obszar")
//    private String obszar;
//
//    @Column(name = "typilosci")
//    private String typilosci;
//
//    @Column(name = "zglaszajacyNiezgodnosc")
//    private String zglaszajacyNiezgodnosc;
//
//    @Column(name = "komOrg")
//    private String komOrg;
//
//    @Column(name = "dataZgloszenia")
//    @DateTimeFormat(pattern = "yyyy-MM-dd")
//    private Date dataZgloszenia;
//
//    @Column(name = "nrZlecenia")
//    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private List<Zlecenia> nrZlecenia = new ArrayList<>();
//
//    @Column(name = "dzialWydzial")
//    private String dzialWydzial;
//
//    @Column(name = "wykryteNaEtapie")
//    private String wykryteNaEtapie;
//
//    @Column(name = "nrPtotOdb")
//    private String nrPtotOdb;
//
//    @Column(name = "niezgodnoscDotyczy")
//    private String niezgodnoscDotyczy;
//
//    @Column(name = "Cecha")
//    private String cecha;
//
//    @Column(name = "dodatkowyOpisNiezgodnosci")
//    private String dodatkowyOpisNiezgodnosci;
//
//    @Column(name = "opisNiezgodnosci")
//    private String opisNiezgodnosci;
//
//    @Column(name = "rodzajPrzyczyny")
//    private String rodzajPrzyczyny;
//
//    @Column(name = "potencjalnaPrzyczyna")
//    private String potencjalnaPrzyczyna;
//
//    @DateTimeFormat(pattern = "yyyy-MM-dd")
//    @Column(name = "dataSpotkaniaZ")
//    private Date dataSpotkaniaZ;
//
//    @Column(name = "decyzjaZKWN")
//    private String decyzjaZKWN;
//
//    @Column(name = "uzasadnienie")
//    private String uzasadnienie;
//
//    @Column(name = "zzreklamowac")
//    private String zzreklamowac;
//
//    @Column(name = "zzuruchomic")
//    private String zzuruchomic;
//
//    @Column(name = "zzotworzyc")
//    private String zzotworzyc;
//
//    @Column(name = "uwagi")
//    private String uwagi;
//
//    @OneToOne(cascade = CascadeType.ALL)
//    private Employee osobaOdp;
//
//    @Lob
//    @Column(name = "plik")
//    private byte[] plik;
//
////    @Lob
////    @OneToMany(cascade = CascadeType.ALL)
////    private List<byte[]> files = new ArrayList<>();
//
//
//    @OneToMany(cascade = CascadeType.ALL)
//    @LazyCollection(LazyCollectionOption.FALSE)
//    private List<KwnMeetingFiles> fileList = new ArrayList<>();
//
//    @OneToOne(cascade = CascadeType.ALL)
//    private Moderator moderator;
//
//
//    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private List<Task> taskList = new ArrayList<>();
//
//    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private List<PC> pcList = new ArrayList<>();
//
//    @Column(name = "status")
//    private Status status;
//
//
//    public Object clone()throws CloneNotSupportedException{
//        return super.clone();
//    }
//
//    @Override
//    public int compareTo(KWN kwn) {
//        if (getId() == null || kwn.getId() == null) {
//            return 0;
//        }
//        return getId().compareTo(kwn.getId());
//    }
//
//
//
//
//}
