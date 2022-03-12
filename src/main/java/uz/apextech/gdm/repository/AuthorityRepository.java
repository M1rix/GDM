package uz.apextech.gdm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.apextech.gdm.domain.Authority;

/**
 * Spring Data JPA repository for the {@link Authority} entity.
 */
public interface AuthorityRepository extends JpaRepository<Authority, String> {}
