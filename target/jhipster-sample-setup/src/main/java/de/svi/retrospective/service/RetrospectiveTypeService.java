package de.svi.retrospective.service;

import de.svi.retrospective.domain.RetrospectiveType;
import de.svi.retrospective.repository.RetrospectiveTypeRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link RetrospectiveType}.
 */
@Service
@Transactional
public class RetrospectiveTypeService {

    private final Logger log = LoggerFactory.getLogger(RetrospectiveTypeService.class);

    private final RetrospectiveTypeRepository retrospectiveTypeRepository;

    public RetrospectiveTypeService(RetrospectiveTypeRepository retrospectiveTypeRepository) {
        this.retrospectiveTypeRepository = retrospectiveTypeRepository;
    }

    /**
     * Save a retrospectiveType.
     *
     * @param retrospectiveType the entity to save.
     * @return the persisted entity.
     */
    public RetrospectiveType save(RetrospectiveType retrospectiveType) {
        log.debug("Request to save RetrospectiveType : {}", retrospectiveType);
        return retrospectiveTypeRepository.save(retrospectiveType);
    }

    /**
     * Update a retrospectiveType.
     *
     * @param retrospectiveType the entity to save.
     * @return the persisted entity.
     */
    public RetrospectiveType update(RetrospectiveType retrospectiveType) {
        log.debug("Request to update RetrospectiveType : {}", retrospectiveType);
        return retrospectiveTypeRepository.save(retrospectiveType);
    }

    /**
     * Partially update a retrospectiveType.
     *
     * @param retrospectiveType the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<RetrospectiveType> partialUpdate(RetrospectiveType retrospectiveType) {
        log.debug("Request to partially update RetrospectiveType : {}", retrospectiveType);

        return retrospectiveTypeRepository
            .findById(retrospectiveType.getId())
            .map(existingRetrospectiveType -> {
                if (retrospectiveType.getName() != null) {
                    existingRetrospectiveType.setName(retrospectiveType.getName());
                }

                return existingRetrospectiveType;
            })
            .map(retrospectiveTypeRepository::save);
    }

    /**
     * Get all the retrospectiveTypes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<RetrospectiveType> findAll(Pageable pageable) {
        log.debug("Request to get all RetrospectiveTypes");
        return retrospectiveTypeRepository.findAll(pageable);
    }

    /**
     * Get one retrospectiveType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<RetrospectiveType> findOne(Long id) {
        log.debug("Request to get RetrospectiveType : {}", id);
        return retrospectiveTypeRepository.findById(id);
    }

    /**
     * Delete the retrospectiveType by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete RetrospectiveType : {}", id);
        retrospectiveTypeRepository.deleteById(id);
    }
}
