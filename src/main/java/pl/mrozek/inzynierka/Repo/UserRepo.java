package pl.mrozek.inzynierka.Repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.mrozek.inzynierka.Entity.Uzytkownik;

@Repository
public interface UserRepo extends CrudRepository<Uzytkownik,Long> {
}
