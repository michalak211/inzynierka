package pl.mrozek.inzynierka.Entity.skladniki;


import lombok.*;
import net.bytebuddy.dynamic.loading.InjectionClassLoader;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
//@DiscriminatorColumn(name = "rodzaj" ,discriminatorType = DiscriminatorType.INTEGER)

public class Skaldnik {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    private String nazwa;
}
