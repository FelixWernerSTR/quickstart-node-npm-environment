import { Component, Vue, Inject } from 'vue-property-decorator';

import { required, maxLength } from 'vuelidate/lib/validators';

import AlertService from '@/shared/alert/alert.service';

import { IRetrospectiveType, RetrospectiveType } from '@/shared/model/retrospective-type.model';
import RetrospectiveTypeService from './retrospective-type.service';

const validations: any = {
  retrospectiveType: {
    name: {
      required,
      maxLength: maxLength(50),
    },
  },
};

@Component({
  validations,
})
export default class RetrospectiveTypeUpdate extends Vue {
  @Inject('retrospectiveTypeService') private retrospectiveTypeService: () => RetrospectiveTypeService;
  @Inject('alertService') private alertService: () => AlertService;

  public retrospectiveType: IRetrospectiveType = new RetrospectiveType();
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.retrospectiveTypeId) {
        vm.retrieveRetrospectiveType(to.params.retrospectiveTypeId);
      }
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
    if (this.retrospectiveType.id) {
      this.retrospectiveTypeService()
        .update(this.retrospectiveType)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('retrospectiveManagerApp.retrospectiveType.updated', { param: param.id });
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
      this.retrospectiveTypeService()
        .create(this.retrospectiveType)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('retrospectiveManagerApp.retrospectiveType.created', { param: param.id });
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

  public retrieveRetrospectiveType(retrospectiveTypeId): void {
    this.retrospectiveTypeService()
      .find(retrospectiveTypeId)
      .then(res => {
        this.retrospectiveType = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {}
}
