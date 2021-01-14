//package pl.mrozek.inzynierka.temp;
//
//import lombok.*;
//
//import javax.persistence.*;
//import java.util.ArrayList;
//import java.util.List;
//
//@AllArgsConstructor
//@NoArgsConstructor
//@Getter
//@Setter
//@ToString
//@Entity
//public class Niezgodnosc {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    private Long id;
//
//    @Column(name = "niezgodnoscNaz")
//    private String niezgodnoscNaz;
//
//    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private List<Cecha> cechaList = new ArrayList<>();
//
//    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private List<OpisNiezgodnosci> opisList = new ArrayList<>();
//
//
//    public Niezgodnosc(String niezgodnoscNaz) {
//        this.niezgodnoscNaz = niezgodnoscNaz;
//    }
//
//    public Niezgodnosc(String niezgodnoscNaz, List<Cecha> cechaList, List<OpisNiezgodnosci> opisList) {
//        this.niezgodnoscNaz = niezgodnoscNaz;
//        this.cechaList = cechaList;
//        this.opisList = opisList;
//    }
//
//    public void addToCechyList(Cecha newCecha) {
//        this.cechaList.add(newCecha);
//    }
//
//    public void addToOpisniezgodnosciList(OpisNiezgodnosci newOpis) {
//        this.opisList.add(newOpis);
//    }
//}
