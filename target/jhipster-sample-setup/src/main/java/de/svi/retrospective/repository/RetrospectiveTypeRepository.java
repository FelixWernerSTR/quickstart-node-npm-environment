package de.svi.retrospective.repository;

import de.svi.retrospective.domain.RetrospectiveType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the RetrospectiveType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RetrospectiveTypeRepository extends JpaRepository<RetrospectiveType, Long> {}
