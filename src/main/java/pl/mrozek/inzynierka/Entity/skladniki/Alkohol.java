package pl.mrozek.inzynierka.Entity.skladniki;


import com.sun.istack.Nullable;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString

@Entity
public class Alkohol extends Skladnik {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

//    private String nazwa;
//    private int procenty;
//    private String marka;

//    @Nullable
//    @OneToOne (cascade = CascadeType.ALL)
//    private  Typ typ;


    @OneToMany (cascade = CascadeType.ALL)
    private List<Typ> typList;


//    @Nullable
//    private String podTyp;




}
