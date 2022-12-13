<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="retrospectiveItem">
        <h2 class="jh-entity-heading" data-cy="retrospectiveItemDetailsHeading">
          <span v-text="$t('retrospectiveManagerApp.retrospectiveItem.detail.title')">RetrospectiveItem</span> {{ retrospectiveItem.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('retrospectiveManagerApp.retrospectiveItem.content')">Content</span>
          </dt>
          <dd>
            <span>{{ retrospectiveItem.content }}</span>
          </dd>
          <dt>
            <span v-text="$t('retrospectiveManagerApp.retrospectiveItem.file')">File</span>
          </dt>
          <dd>
            <div v-if="retrospectiveItem.file">
              <a v-on:click="openFile(retrospectiveItem.fileContentType, retrospectiveItem.file)" v-text="$t('entity.action.open')">open</a>
              {{ retrospectiveItem.fileContentType }}, {{ byteSize(retrospectiveItem.file) }}
            </div>
          </dd>
          <dt>
            <span v-text="$t('retrospectiveManagerApp.retrospectiveItem.titel')">Titel</span>
          </dt>
          <dd>
            <span>{{ retrospectiveItem.titel }}</span>
          </dd>
          <dt>
            <span v-text="$t('retrospectiveManagerApp.retrospectiveItem.retrospectiveType')">Retrospective Type</span>
          </dt>
          <dd>
            <div v-if="retrospectiveItem.retrospectiveType">
              <router-link
                :to="{ name: 'RetrospectiveTypeView', params: { retrospectiveTypeId: retrospectiveItem.retrospectiveType.id } }"
                >{{ retrospectiveItem.retrospectiveType.name }}</router-link
              >
            </div>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link
          v-if="retrospectiveItem.id"
          :to="{ name: 'RetrospectiveItemEdit', params: { retrospectiveItemId: retrospectiveItem.id } }"
          custom
          v-slot="{ navigate }"
        >
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.edit')"> Edit</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./retrospective-item-details.component.ts"></script>
