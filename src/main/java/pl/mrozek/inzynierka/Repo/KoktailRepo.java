package pl.mrozek.inzynierka.Repo;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.mrozek.inzynierka.Entity.Koktail;

@Repository
public interface KoktailRepo extends CrudRepository<Koktail, Long> {
}
