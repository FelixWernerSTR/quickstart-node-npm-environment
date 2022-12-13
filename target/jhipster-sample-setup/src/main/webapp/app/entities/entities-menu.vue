<template>
  <div>
    <b-dropdown-item to="/retrospective-type">
      <font-awesome-icon icon="asterisk" />
      <span v-text="$t('global.menu.entities.retrospectiveType')">Retrospective Type</span>
    </b-dropdown-item>
    <b-dropdown-item to="/retrospective-item">
      <font-awesome-icon icon="asterisk" />
      <span v-text="$t('global.menu.entities.retrospectiveItem')">Retrospective Item</span>
    </b-dropdown-item>
    <!-- jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here -->
  </div>
</template>

<script lang="ts" src="./entities-menu.component.ts"></script>
