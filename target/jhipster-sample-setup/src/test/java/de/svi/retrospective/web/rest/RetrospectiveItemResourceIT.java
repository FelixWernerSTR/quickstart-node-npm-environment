package de.svi.retrospective.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import de.svi.retrospective.IntegrationTest;
import de.svi.retrospective.domain.RetrospectiveItem;
import de.svi.retrospective.repository.RetrospectiveItemRepository;
import de.svi.retrospective.service.RetrospectiveItemService;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link RetrospectiveItemResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class RetrospectiveItemResourceIT {

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    private static final byte[] DEFAULT_FILE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_FILE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_FILE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_FILE_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_TITEL = "AAAAAAAAAA";
    private static final String UPDATED_TITEL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/retrospective-items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RetrospectiveItemRepository retrospectiveItemRepository;

    @Mock
    private RetrospectiveItemRepository retrospectiveItemRepositoryMock;

    @Mock
    private RetrospectiveItemService retrospectiveItemServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRetrospectiveItemMockMvc;

    private RetrospectiveItem retrospectiveItem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RetrospectiveItem createEntity(EntityManager em) {
        RetrospectiveItem retrospectiveItem = new RetrospectiveItem()
            .content(DEFAULT_CONTENT)
            .file(DEFAULT_FILE)
            .fileContentType(DEFAULT_FILE_CONTENT_TYPE)
            .titel(DEFAULT_TITEL);
        return retrospectiveItem;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RetrospectiveItem createUpdatedEntity(EntityManager em) {
        RetrospectiveItem retrospectiveItem = new RetrospectiveItem()
            .content(UPDATED_CONTENT)
            .file(UPDATED_FILE)
            .fileContentType(UPDATED_FILE_CONTENT_TYPE)
            .titel(UPDATED_TITEL);
        return retrospectiveItem;
    }

    @BeforeEach
    public void initTest() {
        retrospectiveItem = createEntity(em);
    }

    @Test
    @Transactional
    void createRetrospectiveItem() throws Exception {
        int databaseSizeBeforeCreate = retrospectiveItemRepository.findAll().size();
        // Create the RetrospectiveItem
        restRetrospectiveItemMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(retrospectiveItem))
            )
            .andExpect(status().isCreated());

        // Validate the RetrospectiveItem in the database
        List<RetrospectiveItem> retrospectiveItemList = retrospectiveItemRepository.findAll();
        assertThat(retrospectiveItemList).hasSize(databaseSizeBeforeCreate + 1);
        RetrospectiveItem testRetrospectiveItem = retrospectiveItemList.get(retrospectiveItemList.size() - 1);
        assertThat(testRetrospectiveItem.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testRetrospectiveItem.getFile()).isEqualTo(DEFAULT_FILE);
        assertThat(testRetrospectiveItem.getFileContentType()).isEqualTo(DEFAULT_FILE_CONTENT_TYPE);
        assertThat(testRetrospectiveItem.getTitel()).isEqualTo(DEFAULT_TITEL);
    }

    @Test
    @Transactional
    void createRetrospectiveItemWithExistingId() throws Exception {
        // Create the RetrospectiveItem with an existing ID
        retrospectiveItem.setId(1L);

        int databaseSizeBeforeCreate = retrospectiveItemRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRetrospectiveItemMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(retrospectiveItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the RetrospectiveItem in the database
        List<RetrospectiveItem> retrospectiveItemList = retrospectiveItemRepository.findAll();
        assertThat(retrospectiveItemList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllRetrospectiveItems() throws Exception {
        // Initialize the database
        retrospectiveItemRepository.saveAndFlush(retrospectiveItem);

        // Get all the retrospectiveItemList
        restRetrospectiveItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(retrospectiveItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())))
            .andExpect(jsonPath("$.[*].fileContentType").value(hasItem(DEFAULT_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].file").value(hasItem(Base64Utils.encodeToString(DEFAULT_FILE))))
            .andExpect(jsonPath("$.[*].titel").value(hasItem(DEFAULT_TITEL)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllRetrospectiveItemsWithEagerRelationshipsIsEnabled() throws Exception {
        when(retrospectiveItemServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restRetrospectiveItemMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(retrospectiveItemServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllRetrospectiveItemsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(retrospectiveItemServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restRetrospectiveItemMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(retrospectiveItemRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getRetrospectiveItem() throws Exception {
        // Initialize the database
        retrospectiveItemRepository.saveAndFlush(retrospectiveItem);

        // Get the retrospectiveItem
        restRetrospectiveItemMockMvc
            .perform(get(ENTITY_API_URL_ID, retrospectiveItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(retrospectiveItem.getId().intValue()))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT.toString()))
            .andExpect(jsonPath("$.fileContentType").value(DEFAULT_FILE_CONTENT_TYPE))
            .andExpect(jsonPath("$.file").value(Base64Utils.encodeToString(DEFAULT_FILE)))
            .andExpect(jsonPath("$.titel").value(DEFAULT_TITEL));
    }

    @Test
    @Transactional
    void getNonExistingRetrospectiveItem() throws Exception {
        // Get the retrospectiveItem
        restRetrospectiveItemMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingRetrospectiveItem() throws Exception {
        // Initialize the database
        retrospectiveItemRepository.saveAndFlush(retrospectiveItem);

        int databaseSizeBeforeUpdate = retrospectiveItemRepository.findAll().size();

        // Update the retrospectiveItem
        RetrospectiveItem updatedRetrospectiveItem = retrospectiveItemRepository.findById(retrospectiveItem.getId()).get();
        // Disconnect from session so that the updates on updatedRetrospectiveItem are not directly saved in db
        em.detach(updatedRetrospectiveItem);
        updatedRetrospectiveItem
            .content(UPDATED_CONTENT)
            .file(UPDATED_FILE)
            .fileContentType(UPDATED_FILE_CONTENT_TYPE)
            .titel(UPDATED_TITEL);

        restRetrospectiveItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedRetrospectiveItem.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedRetrospectiveItem))
            )
            .andExpect(status().isOk());

        // Validate the RetrospectiveItem in the database
        List<RetrospectiveItem> retrospectiveItemList = retrospectiveItemRepository.findAll();
        assertThat(retrospectiveItemList).hasSize(databaseSizeBeforeUpdate);
        RetrospectiveItem testRetrospectiveItem = retrospectiveItemList.get(retrospectiveItemList.size() - 1);
        assertThat(testRetrospectiveItem.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testRetrospectiveItem.getFile()).isEqualTo(UPDATED_FILE);
        assertThat(testRetrospectiveItem.getFileContentType()).isEqualTo(UPDATED_FILE_CONTENT_TYPE);
        assertThat(testRetrospectiveItem.getTitel()).isEqualTo(UPDATED_TITEL);
    }

    @Test
    @Transactional
    void putNonExistingRetrospectiveItem() throws Exception {
        int databaseSizeBeforeUpdate = retrospectiveItemRepository.findAll().size();
        retrospectiveItem.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRetrospectiveItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, retrospectiveItem.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(retrospectiveItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the RetrospectiveItem in the database
        List<RetrospectiveItem> retrospectiveItemList = retrospectiveItemRepository.findAll();
        assertThat(retrospectiveItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRetrospectiveItem() throws Exception {
        int databaseSizeBeforeUpdate = retrospectiveItemRepository.findAll().size();
        retrospectiveItem.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRetrospectiveItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(retrospectiveItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the RetrospectiveItem in the database
        List<RetrospectiveItem> retrospectiveItemList = retrospectiveItemRepository.findAll();
        assertThat(retrospectiveItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRetrospectiveItem() throws Exception {
        int databaseSizeBeforeUpdate = retrospectiveItemRepository.findAll().size();
        retrospectiveItem.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRetrospectiveItemMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(retrospectiveItem))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RetrospectiveItem in the database
        List<RetrospectiveItem> retrospectiveItemList = retrospectiveItemRepository.findAll();
        assertThat(retrospectiveItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRetrospectiveItemWithPatch() throws Exception {
        // Initialize the database
        retrospectiveItemRepository.saveAndFlush(retrospectiveItem);

        int databaseSizeBeforeUpdate = retrospectiveItemRepository.findAll().size();

        // Update the retrospectiveItem using partial update
        RetrospectiveItem partialUpdatedRetrospectiveItem = new RetrospectiveItem();
        partialUpdatedRetrospectiveItem.setId(retrospectiveItem.getId());

        partialUpdatedRetrospectiveItem.content(UPDATED_CONTENT).file(UPDATED_FILE).fileContentType(UPDATED_FILE_CONTENT_TYPE);

        restRetrospectiveItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRetrospectiveItem.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRetrospectiveItem))
            )
            .andExpect(status().isOk());

        // Validate the RetrospectiveItem in the database
        List<RetrospectiveItem> retrospectiveItemList = retrospectiveItemRepository.findAll();
        assertThat(retrospectiveItemList).hasSize(databaseSizeBeforeUpdate);
        RetrospectiveItem testRetrospectiveItem = retrospectiveItemList.get(retrospectiveItemList.size() - 1);
        assertThat(testRetrospectiveItem.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testRetrospectiveItem.getFile()).isEqualTo(UPDATED_FILE);
        assertThat(testRetrospectiveItem.getFileContentType()).isEqualTo(UPDATED_FILE_CONTENT_TYPE);
        assertThat(testRetrospectiveItem.getTitel()).isEqualTo(DEFAULT_TITEL);
    }

    @Test
    @Transactional
    void fullUpdateRetrospectiveItemWithPatch() throws Exception {
        // Initialize the database
        retrospectiveItemRepository.saveAndFlush(retrospectiveItem);

        int databaseSizeBeforeUpdate = retrospectiveItemRepository.findAll().size();

        // Update the retrospectiveItem using partial update
        RetrospectiveItem partialUpdatedRetrospectiveItem = new RetrospectiveItem();
        partialUpdatedRetrospectiveItem.setId(retrospectiveItem.getId());

        partialUpdatedRetrospectiveItem
            .content(UPDATED_CONTENT)
            .file(UPDATED_FILE)
            .fileContentType(UPDATED_FILE_CONTENT_TYPE)
            .titel(UPDATED_TITEL);

        restRetrospectiveItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRetrospectiveItem.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRetrospectiveItem))
            )
            .andExpect(status().isOk());

        // Validate the RetrospectiveItem in the database
        List<RetrospectiveItem> retrospectiveItemList = retrospectiveItemRepository.findAll();
        assertThat(retrospectiveItemList).hasSize(databaseSizeBeforeUpdate);
        RetrospectiveItem testRetrospectiveItem = retrospectiveItemList.get(retrospectiveItemList.size() - 1);
        assertThat(testRetrospectiveItem.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testRetrospectiveItem.getFile()).isEqualTo(UPDATED_FILE);
        assertThat(testRetrospectiveItem.getFileContentType()).isEqualTo(UPDATED_FILE_CONTENT_TYPE);
        assertThat(testRetrospectiveItem.getTitel()).isEqualTo(UPDATED_TITEL);
    }

    @Test
    @Transactional
    void patchNonExistingRetrospectiveItem() throws Exception {
        int databaseSizeBeforeUpdate = retrospectiveItemRepository.findAll().size();
        retrospectiveItem.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRetrospectiveItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, retrospectiveItem.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(retrospectiveItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the RetrospectiveItem in the database
        List<RetrospectiveItem> retrospectiveItemList = retrospectiveItemRepository.findAll();
        assertThat(retrospectiveItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRetrospectiveItem() throws Exception {
        int databaseSizeBeforeUpdate = retrospectiveItemRepository.findAll().size();
        retrospectiveItem.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRetrospectiveItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(retrospectiveItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the RetrospectiveItem in the database
        List<RetrospectiveItem> retrospectiveItemList = retrospectiveItemRepository.findAll();
        assertThat(retrospectiveItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRetrospectiveItem() throws Exception {
        int databaseSizeBeforeUpdate = retrospectiveItemRepository.findAll().size();
        retrospectiveItem.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRetrospectiveItemMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(retrospectiveItem))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RetrospectiveItem in the database
        List<RetrospectiveItem> retrospectiveItemList = retrospectiveItemRepository.findAll();
        assertThat(retrospectiveItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRetrospectiveItem() throws Exception {
        // Initialize the database
        retrospectiveItemRepository.saveAndFlush(retrospectiveItem);

        int databaseSizeBeforeDelete = retrospectiveItemRepository.findAll().size();

        // Delete the retrospectiveItem
        restRetrospectiveItemMockMvc
            .perform(delete(ENTITY_API_URL_ID, retrospectiveItem.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<RetrospectiveItem> retrospectiveItemList = retrospectiveItemRepository.findAll();
        assertThat(retrospectiveItemList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
