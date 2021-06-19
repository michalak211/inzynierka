package pl.mrozek.inzynierka.Entity.user;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString

@Entity
public class Authoritiy implements GrantedAuthority {

    @Id
    private UUID id;

    private String authority;

    @ManyToOne
    private BarUser barUser;


}
