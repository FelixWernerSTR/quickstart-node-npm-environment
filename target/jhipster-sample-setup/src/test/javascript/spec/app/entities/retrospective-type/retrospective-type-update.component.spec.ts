/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import RetrospectiveTypeUpdateComponent from '@/entities/retrospective-type/retrospective-type-update.vue';
import RetrospectiveTypeClass from '@/entities/retrospective-type/retrospective-type-update.component';
import RetrospectiveTypeService from '@/entities/retrospective-type/retrospective-type.service';

import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
const router = new Router();
localVue.use(Router);
localVue.use(ToastPlugin);
localVue.component('font-awesome-icon', {});
localVue.component('b-input-group', {});
localVue.component('b-input-group-prepend', {});
localVue.component('b-form-datepicker', {});
localVue.component('b-form-input', {});

describe('Component Tests', () => {
  describe('RetrospectiveType Management Update Component', () => {
    let wrapper: Wrapper<RetrospectiveTypeClass>;
    let comp: RetrospectiveTypeClass;
    let retrospectiveTypeServiceStub: SinonStubbedInstance<RetrospectiveTypeService>;

    beforeEach(() => {
      retrospectiveTypeServiceStub = sinon.createStubInstance<RetrospectiveTypeService>(RetrospectiveTypeService);

      wrapper = shallowMount<RetrospectiveTypeClass>(RetrospectiveTypeUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          retrospectiveTypeService: () => retrospectiveTypeServiceStub,
          alertService: () => new AlertService(),
        },
      });
      comp = wrapper.vm;
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const entity = { id: 123 };
        comp.retrospectiveType = entity;
        retrospectiveTypeServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(retrospectiveTypeServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.retrospectiveType = entity;
        retrospectiveTypeServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(retrospectiveTypeServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundRetrospectiveType = { id: 123 };
        retrospectiveTypeServiceStub.find.resolves(foundRetrospectiveType);
        retrospectiveTypeServiceStub.retrieve.resolves([foundRetrospectiveType]);

        // WHEN
        comp.beforeRouteEnter({ params: { retrospectiveTypeId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.retrospectiveType).toBe(foundRetrospectiveType);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        comp.previousState();
        await comp.$nextTick();

        expect(comp.$router.currentRoute.fullPath).toContain('/');
      });
    });
  });
});
