package pl.mrozek.inzynierka.Config;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.mrozek.inzynierka.Repo.BarUserRepo;

@Service
public class UserDetailServiceB implements UserDetailsService {

    private final BarUserRepo barUserRepo;

    public UserDetailServiceB(BarUserRepo barUserRepo) {
        this.barUserRepo = barUserRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return barUserRepo.findAllByUsername(username);
    }
}
