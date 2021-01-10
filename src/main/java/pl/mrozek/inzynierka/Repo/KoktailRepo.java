package pl.mrozek.inzynierka.Repo;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.mrozek.inzynierka.Entity.przepis.Koktail;

import java.util.List;

@Repository
public interface KoktailRepo extends CrudRepository<Koktail, Long> {
}
