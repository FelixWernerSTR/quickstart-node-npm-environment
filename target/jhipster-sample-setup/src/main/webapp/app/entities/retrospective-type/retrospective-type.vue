<template>
  <div>
    <h2 id="page-heading" data-cy="RetrospectiveTypeHeading">
      <span v-text="$t('retrospectiveManagerApp.retrospectiveType.home.title')" id="retrospective-type-heading">Retrospective Types</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="$t('retrospectiveManagerApp.retrospectiveType.home.refreshListLabel')">Refresh List</span>
        </button>
        <router-link :to="{ name: 'RetrospectiveTypeCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-retrospective-type"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="$t('retrospectiveManagerApp.retrospectiveType.home.createLabel')"> Create a new Retrospective Type </span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && retrospectiveTypes && retrospectiveTypes.length === 0">
      <span v-text="$t('retrospectiveManagerApp.retrospectiveType.home.notFound')">No retrospectiveTypes found</span>
    </div>
    <div class="table-responsive" v-if="retrospectiveTypes && retrospectiveTypes.length > 0">
      <table class="table table-striped" aria-describedby="retrospectiveTypes">
        <thead>
          <tr>
            <th scope="row" v-on:click="changeOrder('id')">
              <span v-text="$t('global.field.id')">ID</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('name')">
              <span v-text="$t('retrospectiveManagerApp.retrospectiveType.name')">Name</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'name'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="retrospectiveType in retrospectiveTypes" :key="retrospectiveType.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'RetrospectiveTypeView', params: { retrospectiveTypeId: retrospectiveType.id } }">{{
                retrospectiveType.id
              }}</router-link>
            </td>
            <td>{{ retrospectiveType.name }}</td>
            <td class="text-right">
              <div class="btn-group">
                <router-link
                  :to="{ name: 'RetrospectiveTypeView', params: { retrospectiveTypeId: retrospectiveType.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.view')">View</span>
                  </button>
                </router-link>
                <router-link
                  :to="{ name: 'RetrospectiveTypeEdit', params: { retrospectiveTypeId: retrospectiveType.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.edit')">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(retrospectiveType)"
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
          id="retrospectiveManagerApp.retrospectiveType.delete.question"
          data-cy="retrospectiveTypeDeleteDialogHeading"
          v-text="$t('entity.delete.title')"
          >Confirm delete operation</span
        ></span
      >
      <div class="modal-body">
        <p
          id="svi-delete-retrospectiveType-heading"
          v-text="$t('retrospectiveManagerApp.retrospectiveType.delete.question', { id: removeId })"
        >
          Are you sure you want to delete this Retrospective Type?
        </p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-text="$t('entity.action.cancel')" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="svi-confirm-delete-retrospectiveType"
          data-cy="entityConfirmDeleteButton"
          v-text="$t('entity.action.delete')"
          v-on:click="removeRetrospectiveType()"
        >
          Delete
        </button>
      </div>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./retrospective-type.component.ts"></script>
