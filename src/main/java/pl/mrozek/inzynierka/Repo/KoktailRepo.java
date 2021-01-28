package pl.mrozek.inzynierka.Repo;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.mrozek.inzynierka.Entity.przepis.Koktajl;

@Repository
public interface KoktailRepo extends CrudRepository<Koktajl, Long> {
}
