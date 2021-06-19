package pl.mrozek.inzynierka.Entity.skladniki;

import com.sun.istack.Nullable;
import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString

@Entity
public class Typ extends Skladnik{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    private Long butelkaId;

    private Long alkoholID;

}
