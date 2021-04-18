package pl.mrozek.inzynierka.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.mrozek.inzynierka.Entity.user.VerificationToken;

public interface VerificationTokenRepo extends JpaRepository<VerificationToken,Long> {

    VerificationToken findByValue(String value);


}
