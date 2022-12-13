import { Component, Provide, Vue } from 'vue-property-decorator';

import RetrospectiveTypeService from './retrospective-type/retrospective-type.service';
import RetrospectiveItemService from './retrospective-item/retrospective-item.service';
// jhipster-needle-add-entity-service-to-entities-component-import - JHipster will import entities services here

@Component
export default class Entities extends Vue {
  @Provide('retrospectiveTypeService') private retrospectiveTypeService = () => new RetrospectiveTypeService();
  @Provide('retrospectiveItemService') private retrospectiveItemService = () => new RetrospectiveItemService();
  // jhipster-needle-add-entity-service-to-entities-component - JHipster will import entities services here
}
