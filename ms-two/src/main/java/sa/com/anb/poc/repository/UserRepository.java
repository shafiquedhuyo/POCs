package sa.com.anb.poc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sa.com.anb.poc.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

}
