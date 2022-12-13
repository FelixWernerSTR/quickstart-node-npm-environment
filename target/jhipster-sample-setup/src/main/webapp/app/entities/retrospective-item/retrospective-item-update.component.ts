import { Component, Inject } from 'vue-property-decorator';

import { mixins } from 'vue-class-component';
import JhiDataUtils from '@/shared/data/data-utils.service';

import { maxLength } from 'vuelidate/lib/validators';

import AlertService from '@/shared/alert/alert.service';

import RetrospectiveTypeService from '@/entities/retrospective-type/retrospective-type.service';
import { IRetrospectiveType } from '@/shared/model/retrospective-type.model';

import { IRetrospectiveItem, RetrospectiveItem } from '@/shared/model/retrospective-item.model';
import RetrospectiveItemService from './retrospective-item.service';

const validations: any = {
  retrospectiveItem: {
    content: {},
    file: {},
    titel: {
      maxLength: maxLength(70),
    },
  },
};

@Component({
  validations,
})
export default class RetrospectiveItemUpdate extends mixins(JhiDataUtils) {
  @Inject('retrospectiveItemService') private retrospectiveItemService: () => RetrospectiveItemService;
  @Inject('alertService') private alertService: () => AlertService;

  public retrospectiveItem: IRetrospectiveItem = new RetrospectiveItem();

  @Inject('retrospectiveTypeService') private retrospectiveTypeService: () => RetrospectiveTypeService;

  public retrospectiveTypes: IRetrospectiveType[] = [];
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.retrospectiveItemId) {
        vm.retrieveRetrospectiveItem(to.params.retrospectiveItemId);
      }
      vm.initRelationships();
    });
  }

  created(): void {
    this.currentLanguage = this.$store.getters.currentLanguage;
    this.$store.watch(
      () => this.$store.getters.currentLanguage,
      () => {
        this.currentLanguage = this.$store.getters.currentLanguage;
      }
    );
  }

  public save(): void {
    this.isSaving = true;
    if (this.retrospectiveItem.id) {
      this.retrospectiveItemService()
        .update(this.retrospectiveItem)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('retrospectiveManagerApp.retrospectiveItem.updated', { param: param.id });
          return (this.$root as any).$bvToast.toast(message.toString(), {
            toaster: 'b-toaster-top-center',
            title: 'Info',
            variant: 'info',
            solid: true,
            autoHideDelay: 5000,
          });
        })
        .catch(error => {
          this.isSaving = false;
          this.alertService().showHttpError(this, error.response);
        });
    } else {
      this.retrospectiveItemService()
        .create(this.retrospectiveItem)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('retrospectiveManagerApp.retrospectiveItem.created', { param: param.id });
          (this.$root as any).$bvToast.toast(message.toString(), {
            toaster: 'b-toaster-top-center',
            title: 'Success',
            variant: 'success',
            solid: true,
            autoHideDelay: 5000,
          });
        })
        .catch(error => {
          this.isSaving = false;
          this.alertService().showHttpError(this, error.response);
        });
    }
  }

  public retrieveRetrospectiveItem(retrospectiveItemId): void {
    this.retrospectiveItemService()
      .find(retrospectiveItemId)
      .then(res => {
        this.retrospectiveItem = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.retrospectiveTypeService()
      .retrieve()
      .then(res => {
        this.retrospectiveTypes = res.data;
      });
  }
}
