package pl.mrozek.inzynierka.Entity.przepis;

import com.sun.istack.Nullable;
import lombok.*;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import pl.mrozek.inzynierka.Entity.Uzytkownik;

import javax.persistence.*;
import java.util.ArrayList;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString

@Entity
public class Koktajl {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String nazwa;

//    @OneToMany (cascade = CascadeType.ALL)
////    @LazyCollection(LazyCollectionOption.FALSE)
//    private List<SkladnikB> skladnikBList;

    @Nullable
    private String klasa;

    @Nullable
    private long ocena;
    @Nullable
    private String szklo;
//
//    private boolean vegan;
//
//    @Nullable
//    @Column(columnDefinition = "LONGTEXT")
//    private String opisPrzyzadzenia;
//
//    @Nullable
//    private String zdobienie;
//
//    @Lob
//    private byte[] zdjecie;
//
//    @OneToOne
//    private Uzytkownik uzytkownik;
//
//    @Nullable
//    private double cena;
//
//    private int origin=0;


}
