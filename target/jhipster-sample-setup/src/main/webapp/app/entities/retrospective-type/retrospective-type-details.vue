<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="retrospectiveType">
        <h2 class="jh-entity-heading" data-cy="retrospectiveTypeDetailsHeading">
          <span v-text="$t('retrospectiveManagerApp.retrospectiveType.detail.title')">RetrospectiveType</span> {{ retrospectiveType.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('retrospectiveManagerApp.retrospectiveType.name')">Name</span>
          </dt>
          <dd>
            <span>{{ retrospectiveType.name }}</span>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link
          v-if="retrospectiveType.id"
          :to="{ name: 'RetrospectiveTypeEdit', params: { retrospectiveTypeId: retrospectiveType.id } }"
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

<script lang="ts" src="./retrospective-type-details.component.ts"></script>
