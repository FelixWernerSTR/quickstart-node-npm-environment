package de.svi.retrospective.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import de.svi.retrospective.IntegrationTest;
import de.svi.retrospective.domain.RetrospectiveType;
import de.svi.retrospective.repository.RetrospectiveTypeRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link RetrospectiveTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RetrospectiveTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/retrospective-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RetrospectiveTypeRepository retrospectiveTypeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRetrospectiveTypeMockMvc;

    private RetrospectiveType retrospectiveType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RetrospectiveType createEntity(EntityManager em) {
        RetrospectiveType retrospectiveType = new RetrospectiveType().name(DEFAULT_NAME);
        return retrospectiveType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RetrospectiveType createUpdatedEntity(EntityManager em) {
        RetrospectiveType retrospectiveType = new RetrospectiveType().name(UPDATED_NAME);
        return retrospectiveType;
    }

    @BeforeEach
    public void initTest() {
        retrospectiveType = createEntity(em);
    }

    @Test
    @Transactional
    void createRetrospectiveType() throws Exception {
        int databaseSizeBeforeCreate = retrospectiveTypeRepository.findAll().size();
        // Create the RetrospectiveType
        restRetrospectiveTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(retrospectiveType))
            )
            .andExpect(status().isCreated());

        // Validate the RetrospectiveType in the database
        List<RetrospectiveType> retrospectiveTypeList = retrospectiveTypeRepository.findAll();
        assertThat(retrospectiveTypeList).hasSize(databaseSizeBeforeCreate + 1);
        RetrospectiveType testRetrospectiveType = retrospectiveTypeList.get(retrospectiveTypeList.size() - 1);
        assertThat(testRetrospectiveType.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createRetrospectiveTypeWithExistingId() throws Exception {
        // Create the RetrospectiveType with an existing ID
        retrospectiveType.setId(1L);

        int databaseSizeBeforeCreate = retrospectiveTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRetrospectiveTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(retrospectiveType))
            )
            .andExpect(status().isBadRequest());

        // Validate the RetrospectiveType in the database
        List<RetrospectiveType> retrospectiveTypeList = retrospectiveTypeRepository.findAll();
        assertThat(retrospectiveTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = retrospectiveTypeRepository.findAll().size();
        // set the field null
        retrospectiveType.setName(null);

        // Create the RetrospectiveType, which fails.

        restRetrospectiveTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(retrospectiveType))
            )
            .andExpect(status().isBadRequest());

        List<RetrospectiveType> retrospectiveTypeList = retrospectiveTypeRepository.findAll();
        assertThat(retrospectiveTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRetrospectiveTypes() throws Exception {
        // Initialize the database
        retrospectiveTypeRepository.saveAndFlush(retrospectiveType);

        // Get all the retrospectiveTypeList
        restRetrospectiveTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(retrospectiveType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getRetrospectiveType() throws Exception {
        // Initialize the database
        retrospectiveTypeRepository.saveAndFlush(retrospectiveType);

        // Get the retrospectiveType
        restRetrospectiveTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, retrospectiveType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(retrospectiveType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingRetrospectiveType() throws Exception {
        // Get the retrospectiveType
        restRetrospectiveTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingRetrospectiveType() throws Exception {
        // Initialize the database
        retrospectiveTypeRepository.saveAndFlush(retrospectiveType);

        int databaseSizeBeforeUpdate = retrospectiveTypeRepository.findAll().size();

        // Update the retrospectiveType
        RetrospectiveType updatedRetrospectiveType = retrospectiveTypeRepository.findById(retrospectiveType.getId()).get();
        // Disconnect from session so that the updates on updatedRetrospectiveType are not directly saved in db
        em.detach(updatedRetrospectiveType);
        updatedRetrospectiveType.name(UPDATED_NAME);

        restRetrospectiveTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedRetrospectiveType.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedRetrospectiveType))
            )
            .andExpect(status().isOk());

        // Validate the RetrospectiveType in the database
        List<RetrospectiveType> retrospectiveTypeList = retrospectiveTypeRepository.findAll();
        assertThat(retrospectiveTypeList).hasSize(databaseSizeBeforeUpdate);
        RetrospectiveType testRetrospectiveType = retrospectiveTypeList.get(retrospectiveTypeList.size() - 1);
        assertThat(testRetrospectiveType.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingRetrospectiveType() throws Exception {
        int databaseSizeBeforeUpdate = retrospectiveTypeRepository.findAll().size();
        retrospectiveType.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRetrospectiveTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, retrospectiveType.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(retrospectiveType))
            )
            .andExpect(status().isBadRequest());

        // Validate the RetrospectiveType in the database
        List<RetrospectiveType> retrospectiveTypeList = retrospectiveTypeRepository.findAll();
        assertThat(retrospectiveTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRetrospectiveType() throws Exception {
        int databaseSizeBeforeUpdate = retrospectiveTypeRepository.findAll().size();
        retrospectiveType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRetrospectiveTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(retrospectiveType))
            )
            .andExpect(status().isBadRequest());

        // Validate the RetrospectiveType in the database
        List<RetrospectiveType> retrospectiveTypeList = retrospectiveTypeRepository.findAll();
        assertThat(retrospectiveTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRetrospectiveType() throws Exception {
        int databaseSizeBeforeUpdate = retrospectiveTypeRepository.findAll().size();
        retrospectiveType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRetrospectiveTypeMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(retrospectiveType))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RetrospectiveType in the database
        List<RetrospectiveType> retrospectiveTypeList = retrospectiveTypeRepository.findAll();
        assertThat(retrospectiveTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRetrospectiveTypeWithPatch() throws Exception {
        // Initialize the database
        retrospectiveTypeRepository.saveAndFlush(retrospectiveType);

        int databaseSizeBeforeUpdate = retrospectiveTypeRepository.findAll().size();

        // Update the retrospectiveType using partial update
        RetrospectiveType partialUpdatedRetrospectiveType = new RetrospectiveType();
        partialUpdatedRetrospectiveType.setId(retrospectiveType.getId());

        partialUpdatedRetrospectiveType.name(UPDATED_NAME);

        restRetrospectiveTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRetrospectiveType.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRetrospectiveType))
            )
            .andExpect(status().isOk());

        // Validate the RetrospectiveType in the database
        List<RetrospectiveType> retrospectiveTypeList = retrospectiveTypeRepository.findAll();
        assertThat(retrospectiveTypeList).hasSize(databaseSizeBeforeUpdate);
        RetrospectiveType testRetrospectiveType = retrospectiveTypeList.get(retrospectiveTypeList.size() - 1);
        assertThat(testRetrospectiveType.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void fullUpdateRetrospectiveTypeWithPatch() throws Exception {
        // Initialize the database
        retrospectiveTypeRepository.saveAndFlush(retrospectiveType);

        int databaseSizeBeforeUpdate = retrospectiveTypeRepository.findAll().size();

        // Update the retrospectiveType using partial update
        RetrospectiveType partialUpdatedRetrospectiveType = new RetrospectiveType();
        partialUpdatedRetrospectiveType.setId(retrospectiveType.getId());

        partialUpdatedRetrospectiveType.name(UPDATED_NAME);

        restRetrospectiveTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRetrospectiveType.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRetrospectiveType))
            )
            .andExpect(status().isOk());

        // Validate the RetrospectiveType in the database
        List<RetrospectiveType> retrospectiveTypeList = retrospectiveTypeRepository.findAll();
        assertThat(retrospectiveTypeList).hasSize(databaseSizeBeforeUpdate);
        RetrospectiveType testRetrospectiveType = retrospectiveTypeList.get(retrospectiveTypeList.size() - 1);
        assertThat(testRetrospectiveType.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingRetrospectiveType() throws Exception {
        int databaseSizeBeforeUpdate = retrospectiveTypeRepository.findAll().size();
        retrospectiveType.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRetrospectiveTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, retrospectiveType.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(retrospectiveType))
            )
            .andExpect(status().isBadRequest());

        // Validate the RetrospectiveType in the database
        List<RetrospectiveType> retrospectiveTypeList = retrospectiveTypeRepository.findAll();
        assertThat(retrospectiveTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRetrospectiveType() throws Exception {
        int databaseSizeBeforeUpdate = retrospectiveTypeRepository.findAll().size();
        retrospectiveType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRetrospectiveTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(retrospectiveType))
            )
            .andExpect(status().isBadRequest());

        // Validate the RetrospectiveType in the database
        List<RetrospectiveType> retrospectiveTypeList = retrospectiveTypeRepository.findAll();
        assertThat(retrospectiveTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRetrospectiveType() throws Exception {
        int databaseSizeBeforeUpdate = retrospectiveTypeRepository.findAll().size();
        retrospectiveType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRetrospectiveTypeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(retrospectiveType))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RetrospectiveType in the database
        List<RetrospectiveType> retrospectiveTypeList = retrospectiveTypeRepository.findAll();
        assertThat(retrospectiveTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRetrospectiveType() throws Exception {
        // Initialize the database
        retrospectiveTypeRepository.saveAndFlush(retrospectiveType);

        int databaseSizeBeforeDelete = retrospectiveTypeRepository.findAll().size();

        // Delete the retrospectiveType
        restRetrospectiveTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, retrospectiveType.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<RetrospectiveType> retrospectiveTypeList = retrospectiveTypeRepository.findAll();
        assertThat(retrospectiveTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
