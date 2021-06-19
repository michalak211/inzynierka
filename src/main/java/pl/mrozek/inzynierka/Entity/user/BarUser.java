package pl.mrozek.inzynierka.Entity.user;


import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString

@Entity
public class BarUser implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String username;
    private String password;
    private boolean isEnabled;
    private String mail;


    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Authoritiy> authorities;

//    @OneToOne(mappedBy = "user")
//    private VerificationToken verificationToken;

    @Enumerated(value = EnumType.STRING)
    @Transient
    private Role role;



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<SimpleGrantedAuthority> simpleGrantedAuthorities = new HashSet<>();
        for (Authoritiy a : this.authorities) {
            simpleGrantedAuthorities.add(new SimpleGrantedAuthority(a.getAuthority()));
        }
        return simpleGrantedAuthorities;
    }


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
