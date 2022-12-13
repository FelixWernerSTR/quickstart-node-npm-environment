<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="retrospectiveManagerApp.retrospectiveType.home.createOrEditLabel"
          data-cy="RetrospectiveTypeCreateUpdateHeading"
          v-text="$t('retrospectiveManagerApp.retrospectiveType.home.createOrEditLabel')"
        >
          Create or edit a RetrospectiveType
        </h2>
        <div>
          <div class="form-group" v-if="retrospectiveType.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="retrospectiveType.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('retrospectiveManagerApp.retrospectiveType.name')" for="retrospective-type-name"
              >Name</label
            >
            <input
              type="text"
              class="form-control"
              name="name"
              id="retrospective-type-name"
              data-cy="name"
              :class="{ valid: !$v.retrospectiveType.name.$invalid, invalid: $v.retrospectiveType.name.$invalid }"
              v-model="$v.retrospectiveType.name.$model"
              required
            />
            <div v-if="$v.retrospectiveType.name.$anyDirty && $v.retrospectiveType.name.$invalid">
              <small class="form-text text-danger" v-if="!$v.retrospectiveType.name.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
              <small
                class="form-text text-danger"
                v-if="!$v.retrospectiveType.name.maxLength"
                v-text="$t('entity.validation.maxlength', { max: 50 })"
              >
                This field cannot be longer than 50 characters.
              </small>
            </div>
          </div>
        </div>
        <div>
          <button type="button" id="cancel-save" data-cy="entityCreateCancelButton" class="btn btn-secondary" v-on:click="previousState()">
            <font-awesome-icon icon="ban"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.cancel')">Cancel</span>
          </button>
          <button
            type="submit"
            id="save-entity"
            data-cy="entityCreateSaveButton"
            :disabled="$v.retrospectiveType.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./retrospective-type-update.component.ts"></script>
