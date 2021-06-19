package pl.mrozek.inzynierka.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.mrozek.inzynierka.Entity.user.Authoritiy;

public interface AuthoritiyRepo extends JpaRepository<Authoritiy, Long> {
}
