<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="retrospectiveManagerApp.retrospectiveItem.home.createOrEditLabel"
          data-cy="RetrospectiveItemCreateUpdateHeading"
          v-text="$t('retrospectiveManagerApp.retrospectiveItem.home.createOrEditLabel')"
        >
          Create or edit a RetrospectiveItem
        </h2>
        <div>
          <div class="form-group" v-if="retrospectiveItem.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="retrospectiveItem.id" readonly />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="$t('retrospectiveManagerApp.retrospectiveItem.content')"
              for="retrospective-item-content"
              >Content</label
            >
            <textarea
              class="form-control"
              name="content"
              id="retrospective-item-content"
              data-cy="content"
              :class="{ valid: !$v.retrospectiveItem.content.$invalid, invalid: $v.retrospectiveItem.content.$invalid }"
              v-model="$v.retrospectiveItem.content.$model"
            ></textarea>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('retrospectiveManagerApp.retrospectiveItem.file')" for="retrospective-item-file"
              >File</label
            >
            <div>
              <div v-if="retrospectiveItem.file" class="form-text text-danger clearfix">
                <a
                  class="pull-left"
                  v-on:click="openFile(retrospectiveItem.fileContentType, retrospectiveItem.file)"
                  v-text="$t('entity.action.open')"
                  >open</a
                ><br />
                <span class="pull-left">{{ retrospectiveItem.fileContentType }}, {{ byteSize(retrospectiveItem.file) }}</span>
                <button
                  type="button"
                  v-on:click="
                    retrospectiveItem.file = null;
                    retrospectiveItem.fileContentType = null;
                  "
                  class="btn btn-secondary btn-xs pull-right"
                >
                  <font-awesome-icon icon="times"></font-awesome-icon>
                </button>
              </div>
              <input
                type="file"
                ref="file_file"
                id="file_file"
                data-cy="file"
                v-on:change="setFileData($event, retrospectiveItem, 'file', false)"
                v-text="$t('entity.action.addblob')"
              />
            </div>
            <input
              type="hidden"
              class="form-control"
              name="file"
              id="retrospective-item-file"
              data-cy="file"
              :class="{ valid: !$v.retrospectiveItem.file.$invalid, invalid: $v.retrospectiveItem.file.$invalid }"
              v-model="$v.retrospectiveItem.file.$model"
            />
            <input
              type="hidden"
              class="form-control"
              name="fileContentType"
              id="retrospective-item-fileContentType"
              v-model="retrospectiveItem.fileContentType"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('retrospectiveManagerApp.retrospectiveItem.titel')" for="retrospective-item-titel"
              >Titel</label
            >
            <input
              type="text"
              class="form-control"
              name="titel"
              id="retrospective-item-titel"
              data-cy="titel"
              :class="{ valid: !$v.retrospectiveItem.titel.$invalid, invalid: $v.retrospectiveItem.titel.$invalid }"
              v-model="$v.retrospectiveItem.titel.$model"
            />
            <div v-if="$v.retrospectiveItem.titel.$anyDirty && $v.retrospectiveItem.titel.$invalid">
              <small
                class="form-text text-danger"
                v-if="!$v.retrospectiveItem.titel.maxLength"
                v-text="$t('entity.validation.maxlength', { max: 70 })"
              >
                This field cannot be longer than 70 characters.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="$t('retrospectiveManagerApp.retrospectiveItem.retrospectiveType')"
              for="retrospective-item-retrospectiveType"
              >Retrospective Type</label
            >
            <select
              class="form-control"
              id="retrospective-item-retrospectiveType"
              data-cy="retrospectiveType"
              name="retrospectiveType"
              v-model="retrospectiveItem.retrospectiveType"
            >
              <option v-bind:value="null"></option>
              <option
                v-bind:value="
                  retrospectiveItem.retrospectiveType && retrospectiveTypeOption.id === retrospectiveItem.retrospectiveType.id
                    ? retrospectiveItem.retrospectiveType
                    : retrospectiveTypeOption
                "
                v-for="retrospectiveTypeOption in retrospectiveTypes"
                :key="retrospectiveTypeOption.id"
              >
                {{ retrospectiveTypeOption.name }}
              </option>
            </select>
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
            :disabled="$v.retrospectiveItem.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./retrospective-item-update.component.ts"></script>
