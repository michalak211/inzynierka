package pl.mrozek.inzynierka.Config;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.mrozek.inzynierka.Repo.UserRepo;

@Service
public class UserDetailServiceB implements UserDetailsService {

    private final UserRepo userRepo;

    public UserDetailServiceB(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findAllByUsername(username);
    }
}
