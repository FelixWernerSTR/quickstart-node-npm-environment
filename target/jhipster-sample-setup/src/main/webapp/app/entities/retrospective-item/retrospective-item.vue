<template>
  <div>
    <h2 id="page-heading" data-cy="RetrospectiveItemHeading">
      <span v-text="$t('retrospectiveManagerApp.retrospectiveItem.home.title')" id="retrospective-item-heading">Retrospective Items</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="$t('retrospectiveManagerApp.retrospectiveItem.home.refreshListLabel')">Refresh List</span>
        </button>
        <router-link :to="{ name: 'RetrospectiveItemCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-retrospective-item"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="$t('retrospectiveManagerApp.retrospectiveItem.home.createLabel')"> Create a new Retrospective Item </span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && retrospectiveItems && retrospectiveItems.length === 0">
      <span v-text="$t('retrospectiveManagerApp.retrospectiveItem.home.notFound')">No retrospectiveItems found</span>
    </div>
    <div class="table-responsive" v-if="retrospectiveItems && retrospectiveItems.length > 0">
      <table class="table table-striped" aria-describedby="retrospectiveItems">
        <thead>
          <tr>
            <th scope="row" v-on:click="changeOrder('id')">
              <span v-text="$t('global.field.id')">ID</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('content')">
              <span v-text="$t('retrospectiveManagerApp.retrospectiveItem.content')">Content</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'content'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('file')">
              <span v-text="$t('retrospectiveManagerApp.retrospectiveItem.file')">File</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'file'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('titel')">
              <span v-text="$t('retrospectiveManagerApp.retrospectiveItem.titel')">Titel</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'titel'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('retrospectiveType.name')">
              <span v-text="$t('retrospectiveManagerApp.retrospectiveItem.retrospectiveType')">Retrospective Type</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'retrospectiveType.name'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="retrospectiveItem in retrospectiveItems" :key="retrospectiveItem.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'RetrospectiveItemView', params: { retrospectiveItemId: retrospectiveItem.id } }">{{
                retrospectiveItem.id
              }}</router-link>
            </td>
            <td>{{ retrospectiveItem.content }}</td>
            <td>
              <a
                v-if="retrospectiveItem.file"
                v-on:click="openFile(retrospectiveItem.fileContentType, retrospectiveItem.file)"
                v-text="$t('entity.action.open')"
                >open</a
              >
              <span v-if="retrospectiveItem.file">{{ retrospectiveItem.fileContentType }}, {{ byteSize(retrospectiveItem.file) }}</span>
            </td>
            <td>{{ retrospectiveItem.titel }}</td>
            <td>
              <div v-if="retrospectiveItem.retrospectiveType">
                <router-link
                  :to="{ name: 'RetrospectiveTypeView', params: { retrospectiveTypeId: retrospectiveItem.retrospectiveType.id } }"
                  >{{ retrospectiveItem.retrospectiveType.name }}</router-link
                >
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link
                  :to="{ name: 'RetrospectiveItemView', params: { retrospectiveItemId: retrospectiveItem.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.view')">View</span>
                  </button>
                </router-link>
                <router-link
                  :to="{ name: 'RetrospectiveItemEdit', params: { retrospectiveItemId: retrospectiveItem.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.edit')">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(retrospectiveItem)"
                  variant="danger"
                  class="btn btn-sm"
                  data-cy="entityDeleteButton"
                  v-b-modal.removeEntity
                >
                  <font-awesome-icon icon="times"></font-awesome-icon>
                  <span class="d-none d-md-inline" v-text="$t('entity.action.delete')">Delete</span>
                </b-button>
              </div>
            </td>
          </tr>
        </tbody>
        <infinite-loading
          ref="infiniteLoading"
          v-if="totalItems > itemsPerPage"
          :identifier="infiniteId"
          slot="append"
          @infinite="loadMore"
          force-use-infinite-wrapper=".el-table__body-wrapper"
          :distance="20"
        >
        </infinite-loading>
      </table>
    </div>
    <b-modal ref="removeEntity" id="removeEntity">
      <span slot="modal-title"
        ><span
          id="retrospectiveManagerApp.retrospectiveItem.delete.question"
          data-cy="retrospectiveItemDeleteDialogHeading"
          v-text="$t('entity.delete.title')"
          >Confirm delete operation</span
        ></span
      >
      <div class="modal-body">
        <p
          id="svi-delete-retrospectiveItem-heading"
          v-text="$t('retrospectiveManagerApp.retrospectiveItem.delete.question', { id: removeId })"
        >
          Are you sure you want to delete this Retrospective Item?
        </p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-text="$t('entity.action.cancel')" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="svi-confirm-delete-retrospectiveItem"
          data-cy="entityConfirmDeleteButton"
          v-text="$t('entity.action.delete')"
          v-on:click="removeRetrospectiveItem()"
        >
          Delete
        </button>
      </div>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./retrospective-item.component.ts"></script>
