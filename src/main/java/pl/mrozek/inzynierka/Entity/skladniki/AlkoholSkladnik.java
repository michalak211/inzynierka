package pl.mrozek.inzynierka.Entity.skladniki;

import com.sun.istack.Nullable;
import lombok.*;
import pl.mrozek.inzynierka.Repo.SkladnikRepo;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString

@Entity

public class AlkoholSkladnik extends Skladnik {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String nazwa;

    private int typID;


}
