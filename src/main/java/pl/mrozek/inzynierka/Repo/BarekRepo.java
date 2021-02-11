package pl.mrozek.inzynierka.Repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.mrozek.inzynierka.Entity.bar.Barek;

@Repository
public interface BarekRepo extends CrudRepository<Barek,Long> {
}
