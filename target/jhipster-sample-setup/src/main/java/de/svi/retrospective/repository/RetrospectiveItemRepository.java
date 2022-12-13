package de.svi.retrospective.repository;

import de.svi.retrospective.domain.RetrospectiveItem;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the RetrospectiveItem entity.
 */
@Repository
public interface RetrospectiveItemRepository extends JpaRepository<RetrospectiveItem, Long> {
    default Optional<RetrospectiveItem> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<RetrospectiveItem> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<RetrospectiveItem> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct retrospectiveItem from RetrospectiveItem retrospectiveItem left join fetch retrospectiveItem.retrospectiveType",
        countQuery = "select count(distinct retrospectiveItem) from RetrospectiveItem retrospectiveItem"
    )
    Page<RetrospectiveItem> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct retrospectiveItem from RetrospectiveItem retrospectiveItem left join fetch retrospectiveItem.retrospectiveType")
    List<RetrospectiveItem> findAllWithToOneRelationships();

    @Query(
        "select retrospectiveItem from RetrospectiveItem retrospectiveItem left join fetch retrospectiveItem.retrospectiveType where retrospectiveItem.id =:id"
    )
    Optional<RetrospectiveItem> findOneWithToOneRelationships(@Param("id") Long id);
}
