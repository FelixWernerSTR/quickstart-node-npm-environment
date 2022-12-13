package de.svi.retrospective.web.rest;

import de.svi.retrospective.domain.RetrospectiveItem;
import de.svi.retrospective.repository.RetrospectiveItemRepository;
import de.svi.retrospective.service.RetrospectiveItemService;
import de.svi.retrospective.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link de.svi.retrospective.domain.RetrospectiveItem}.
 */
@RestController
@RequestMapping("/api")
public class RetrospectiveItemResource {

    private final Logger log = LoggerFactory.getLogger(RetrospectiveItemResource.class);

    private static final String ENTITY_NAME = "retrospectiveItem";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RetrospectiveItemService retrospectiveItemService;

    private final RetrospectiveItemRepository retrospectiveItemRepository;

    public RetrospectiveItemResource(
        RetrospectiveItemService retrospectiveItemService,
        RetrospectiveItemRepository retrospectiveItemRepository
    ) {
        this.retrospectiveItemService = retrospectiveItemService;
        this.retrospectiveItemRepository = retrospectiveItemRepository;
    }

    /**
     * {@code POST  /retrospective-items} : Create a new retrospectiveItem.
     *
     * @param retrospectiveItem the retrospectiveItem to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new retrospectiveItem, or with status {@code 400 (Bad Request)} if the retrospectiveItem has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/retrospective-items")
    public ResponseEntity<RetrospectiveItem> createRetrospectiveItem(@Valid @RequestBody RetrospectiveItem retrospectiveItem)
        throws URISyntaxException {
        log.debug("REST request to save RetrospectiveItem : {}", retrospectiveItem);
        if (retrospectiveItem.getId() != null) {
            throw new BadRequestAlertException("A new retrospectiveItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RetrospectiveItem result = retrospectiveItemService.save(retrospectiveItem);
        return ResponseEntity
            .created(new URI("/api/retrospective-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /retrospective-items/:id} : Updates an existing retrospectiveItem.
     *
     * @param id the id of the retrospectiveItem to save.
     * @param retrospectiveItem the retrospectiveItem to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated retrospectiveItem,
     * or with status {@code 400 (Bad Request)} if the retrospectiveItem is not valid,
     * or with status {@code 500 (Internal Server Error)} if the retrospectiveItem couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/retrospective-items/{id}")
    public ResponseEntity<RetrospectiveItem> updateRetrospectiveItem(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody RetrospectiveItem retrospectiveItem
    ) throws URISyntaxException {
        log.debug("REST request to update RetrospectiveItem : {}, {}", id, retrospectiveItem);
        if (retrospectiveItem.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, retrospectiveItem.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!retrospectiveItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        RetrospectiveItem result = retrospectiveItemService.update(retrospectiveItem);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, retrospectiveItem.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /retrospective-items/:id} : Partial updates given fields of an existing retrospectiveItem, field will ignore if it is null
     *
     * @param id the id of the retrospectiveItem to save.
     * @param retrospectiveItem the retrospectiveItem to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated retrospectiveItem,
     * or with status {@code 400 (Bad Request)} if the retrospectiveItem is not valid,
     * or with status {@code 404 (Not Found)} if the retrospectiveItem is not found,
     * or with status {@code 500 (Internal Server Error)} if the retrospectiveItem couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/retrospective-items/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RetrospectiveItem> partialUpdateRetrospectiveItem(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody RetrospectiveItem retrospectiveItem
    ) throws URISyntaxException {
        log.debug("REST request to partial update RetrospectiveItem partially : {}, {}", id, retrospectiveItem);
        if (retrospectiveItem.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, retrospectiveItem.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!retrospectiveItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RetrospectiveItem> result = retrospectiveItemService.partialUpdate(retrospectiveItem);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, retrospectiveItem.getId().toString())
        );
    }

    /**
     * {@code GET  /retrospective-items} : get all the retrospectiveItems.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of retrospectiveItems in body.
     */
    @GetMapping("/retrospective-items")
    public ResponseEntity<List<RetrospectiveItem>> getAllRetrospectiveItems(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "false") boolean eagerload
    ) {
        log.debug("REST request to get a page of RetrospectiveItems");
        Page<RetrospectiveItem> page;
        if (eagerload) {
            page = retrospectiveItemService.findAllWithEagerRelationships(pageable);
        } else {
            page = retrospectiveItemService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /retrospective-items/:id} : get the "id" retrospectiveItem.
     *
     * @param id the id of the retrospectiveItem to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the retrospectiveItem, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/retrospective-items/{id}")
    public ResponseEntity<RetrospectiveItem> getRetrospectiveItem(@PathVariable Long id) {
        log.debug("REST request to get RetrospectiveItem : {}", id);
        Optional<RetrospectiveItem> retrospectiveItem = retrospectiveItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(retrospectiveItem);
    }

    /**
     * {@code DELETE  /retrospective-items/:id} : delete the "id" retrospectiveItem.
     *
     * @param id the id of the retrospectiveItem to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/retrospective-items/{id}")
    public ResponseEntity<Void> deleteRetrospectiveItem(@PathVariable Long id) {
        log.debug("REST request to delete RetrospectiveItem : {}", id);
        retrospectiveItemService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
