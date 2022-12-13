import { Component, Inject } from 'vue-property-decorator';

import { mixins } from 'vue-class-component';
import JhiDataUtils from '@/shared/data/data-utils.service';

import { IRetrospectiveItem } from '@/shared/model/retrospective-item.model';
import RetrospectiveItemService from './retrospective-item.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class RetrospectiveItemDetails extends mixins(JhiDataUtils) {
  @Inject('retrospectiveItemService') private retrospectiveItemService: () => RetrospectiveItemService;
  @Inject('alertService') private alertService: () => AlertService;

  public retrospectiveItem: IRetrospectiveItem = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.retrospectiveItemId) {
        vm.retrieveRetrospectiveItem(to.params.retrospectiveItemId);
      }
    });
  }

  public retrieveRetrospectiveItem(retrospectiveItemId) {
    this.retrospectiveItemService()
      .find(retrospectiveItemId)
      .then(res => {
        this.retrospectiveItem = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
