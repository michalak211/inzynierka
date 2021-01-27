package pl.mrozek.inzynierka.Entity.przepis;

import com.sun.istack.Nullable;
import lombok.*;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import pl.mrozek.inzynierka.Entity.User;

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
public class Koktail {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String nazwa;

    @OneToMany (cascade = CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<SkladnikB> skladnikBList=new ArrayList<>();

    @Nullable
    private String klasa;

//    @OneToOne
//    private Skladnik skladnikGlowny;
    @Nullable
    private int ocena;
    @Nullable
    private String szklo;

    private boolean Vegan;

    @Nullable
    private String opisPrzyzadzenia;

    @Nullable
    private String zdobienie;


    @Lob
    private byte[] zdjecie;

    @OneToOne
    private User creator;

    @Nullable
    private int cena;


}
