package pl.mrozek.inzynierka.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.mrozek.inzynierka.Entity.user.BarUser;

@Repository
public interface BarUserRepo extends CrudRepository<BarUser,Long>, JpaRepository<BarUser,Long> {
    BarUser findAllByUsername(String username);
}
