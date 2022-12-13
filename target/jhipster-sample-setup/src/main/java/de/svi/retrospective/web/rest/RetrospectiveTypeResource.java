package de.svi.retrospective.web.rest;

import de.svi.retrospective.domain.RetrospectiveType;
import de.svi.retrospective.repository.RetrospectiveTypeRepository;
import de.svi.retrospective.service.RetrospectiveTypeService;
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
 * REST controller for managing {@link de.svi.retrospective.domain.RetrospectiveType}.
 */
@RestController
@RequestMapping("/api")
public class RetrospectiveTypeResource {

    private final Logger log = LoggerFactory.getLogger(RetrospectiveTypeResource.class);

    private static final String ENTITY_NAME = "retrospectiveType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RetrospectiveTypeService retrospectiveTypeService;

    private final RetrospectiveTypeRepository retrospectiveTypeRepository;

    public RetrospectiveTypeResource(
        RetrospectiveTypeService retrospectiveTypeService,
        RetrospectiveTypeRepository retrospectiveTypeRepository
    ) {
        this.retrospectiveTypeService = retrospectiveTypeService;
        this.retrospectiveTypeRepository = retrospectiveTypeRepository;
    }

    /**
     * {@code POST  /retrospective-types} : Create a new retrospectiveType.
     *
     * @param retrospectiveType the retrospectiveType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new retrospectiveType, or with status {@code 400 (Bad Request)} if the retrospectiveType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/retrospective-types")
    public ResponseEntity<RetrospectiveType> createRetrospectiveType(@Valid @RequestBody RetrospectiveType retrospectiveType)
        throws URISyntaxException {
        log.debug("REST request to save RetrospectiveType : {}", retrospectiveType);
        if (retrospectiveType.getId() != null) {
            throw new BadRequestAlertException("A new retrospectiveType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RetrospectiveType result = retrospectiveTypeService.save(retrospectiveType);
        return ResponseEntity
            .created(new URI("/api/retrospective-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /retrospective-types/:id} : Updates an existing retrospectiveType.
     *
     * @param id the id of the retrospectiveType to save.
     * @param retrospectiveType the retrospectiveType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated retrospectiveType,
     * or with status {@code 400 (Bad Request)} if the retrospectiveType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the retrospectiveType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/retrospective-types/{id}")
    public ResponseEntity<RetrospectiveType> updateRetrospectiveType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody RetrospectiveType retrospectiveType
    ) throws URISyntaxException {
        log.debug("REST request to update RetrospectiveType : {}, {}", id, retrospectiveType);
        if (retrospectiveType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, retrospectiveType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!retrospectiveTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        RetrospectiveType result = retrospectiveTypeService.update(retrospectiveType);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, retrospectiveType.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /retrospective-types/:id} : Partial updates given fields of an existing retrospectiveType, field will ignore if it is null
     *
     * @param id the id of the retrospectiveType to save.
     * @param retrospectiveType the retrospectiveType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated retrospectiveType,
     * or with status {@code 400 (Bad Request)} if the retrospectiveType is not valid,
     * or with status {@code 404 (Not Found)} if the retrospectiveType is not found,
     * or with status {@code 500 (Internal Server Error)} if the retrospectiveType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/retrospective-types/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RetrospectiveType> partialUpdateRetrospectiveType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody RetrospectiveType retrospectiveType
    ) throws URISyntaxException {
        log.debug("REST request to partial update RetrospectiveType partially : {}, {}", id, retrospectiveType);
        if (retrospectiveType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, retrospectiveType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!retrospectiveTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RetrospectiveType> result = retrospectiveTypeService.partialUpdate(retrospectiveType);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, retrospectiveType.getId().toString())
        );
    }

    /**
     * {@code GET  /retrospective-types} : get all the retrospectiveTypes.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of retrospectiveTypes in body.
     */
    @GetMapping("/retrospective-types")
    public ResponseEntity<List<RetrospectiveType>> getAllRetrospectiveTypes(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of RetrospectiveTypes");
        Page<RetrospectiveType> page = retrospectiveTypeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /retrospective-types/:id} : get the "id" retrospectiveType.
     *
     * @param id the id of the retrospectiveType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the retrospectiveType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/retrospective-types/{id}")
    public ResponseEntity<RetrospectiveType> getRetrospectiveType(@PathVariable Long id) {
        log.debug("REST request to get RetrospectiveType : {}", id);
        Optional<RetrospectiveType> retrospectiveType = retrospectiveTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(retrospectiveType);
    }

    /**
     * {@code DELETE  /retrospective-types/:id} : delete the "id" retrospectiveType.
     *
     * @param id the id of the retrospectiveType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/retrospective-types/{id}")
    public ResponseEntity<Void> deleteRetrospectiveType(@PathVariable Long id) {
        log.debug("REST request to delete RetrospectiveType : {}", id);
        retrospectiveTypeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
