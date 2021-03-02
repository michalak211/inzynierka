package pl.mrozek.inzynierka.Entity.przepis;


import com.sun.istack.Nullable;
import lombok.*;
import pl.mrozek.inzynierka.Entity.skladniki.Typ;

import javax.persistence.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString

@Entity
public class Alkohol  {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column (unique = true)
    private String nazwa;


    @OneToMany (cascade = CascadeType.ALL)
    private List<Typ> typList;

}
