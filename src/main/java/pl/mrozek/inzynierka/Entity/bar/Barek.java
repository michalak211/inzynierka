package pl.mrozek.inzynierka.Entity.bar;

import lombok.*;
import pl.mrozek.inzynierka.Entity.user.BarUser;
import pl.mrozek.inzynierka.Entity.skladniki.Inny;
import pl.mrozek.inzynierka.Entity.skladniki.Sok;
import pl.mrozek.inzynierka.Entity.skladniki.Syrop;

import javax.persistence.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString

@Entity
public class Barek {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    private BarUser barUser;

    private String nazwa;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Butelka> butelkaList;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Sok> listSok;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Syrop> listSyrop;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Inny> listInny;

}
