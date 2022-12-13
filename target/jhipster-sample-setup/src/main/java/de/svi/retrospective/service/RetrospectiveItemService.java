package de.svi.retrospective.service;

import de.svi.retrospective.domain.RetrospectiveItem;
import de.svi.retrospective.repository.RetrospectiveItemRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link RetrospectiveItem}.
 */
@Service
@Transactional
public class RetrospectiveItemService {

    private final Logger log = LoggerFactory.getLogger(RetrospectiveItemService.class);

    private final RetrospectiveItemRepository retrospectiveItemRepository;

    public RetrospectiveItemService(RetrospectiveItemRepository retrospectiveItemRepository) {
        this.retrospectiveItemRepository = retrospectiveItemRepository;
    }

    /**
     * Save a retrospectiveItem.
     *
     * @param retrospectiveItem the entity to save.
     * @return the persisted entity.
     */
    public RetrospectiveItem save(RetrospectiveItem retrospectiveItem) {
        log.debug("Request to save RetrospectiveItem : {}", retrospectiveItem);
        return retrospectiveItemRepository.save(retrospectiveItem);
    }

    /**
     * Update a retrospectiveItem.
     *
     * @param retrospectiveItem the entity to save.
     * @return the persisted entity.
     */
    public RetrospectiveItem update(RetrospectiveItem retrospectiveItem) {
        log.debug("Request to update RetrospectiveItem : {}", retrospectiveItem);
        return retrospectiveItemRepository.save(retrospectiveItem);
    }

    /**
     * Partially update a retrospectiveItem.
     *
     * @param retrospectiveItem the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<RetrospectiveItem> partialUpdate(RetrospectiveItem retrospectiveItem) {
        log.debug("Request to partially update RetrospectiveItem : {}", retrospectiveItem);

        return retrospectiveItemRepository
            .findById(retrospectiveItem.getId())
            .map(existingRetrospectiveItem -> {
                if (retrospectiveItem.getContent() != null) {
                    existingRetrospectiveItem.setContent(retrospectiveItem.getContent());
                }
                if (retrospectiveItem.getFile() != null) {
                    existingRetrospectiveItem.setFile(retrospectiveItem.getFile());
                }
                if (retrospectiveItem.getFileContentType() != null) {
                    existingRetrospectiveItem.setFileContentType(retrospectiveItem.getFileContentType());
                }
                if (retrospectiveItem.getTitel() != null) {
                    existingRetrospectiveItem.setTitel(retrospectiveItem.getTitel());
                }

                return existingRetrospectiveItem;
            })
            .map(retrospectiveItemRepository::save);
    }

    /**
     * Get all the retrospectiveItems.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<RetrospectiveItem> findAll(Pageable pageable) {
        log.debug("Request to get all RetrospectiveItems");
        return retrospectiveItemRepository.findAll(pageable);
    }

    /**
     * Get all the retrospectiveItems with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<RetrospectiveItem> findAllWithEagerRelationships(Pageable pageable) {
        return retrospectiveItemRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one retrospectiveItem by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<RetrospectiveItem> findOne(Long id) {
        log.debug("Request to get RetrospectiveItem : {}", id);
        return retrospectiveItemRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the retrospectiveItem by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete RetrospectiveItem : {}", id);
        retrospectiveItemRepository.deleteById(id);
    }
}
