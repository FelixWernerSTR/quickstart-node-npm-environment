import { Component, Vue, Inject } from 'vue-property-decorator';

import { IRetrospectiveType } from '@/shared/model/retrospective-type.model';
import RetrospectiveTypeService from './retrospective-type.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class RetrospectiveTypeDetails extends Vue {
  @Inject('retrospectiveTypeService') private retrospectiveTypeService: () => RetrospectiveTypeService;
  @Inject('alertService') private alertService: () => AlertService;

  public retrospectiveType: IRetrospectiveType = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.retrospectiveTypeId) {
        vm.retrieveRetrospectiveType(to.params.retrospectiveTypeId);
      }
    });
  }

  public retrieveRetrospectiveType(retrospectiveTypeId) {
    this.retrospectiveTypeService()
      .find(retrospectiveTypeId)
      .then(res => {
        this.retrospectiveType = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
