/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import RetrospectiveItemUpdateComponent from '@/entities/retrospective-item/retrospective-item-update.vue';
import RetrospectiveItemClass from '@/entities/retrospective-item/retrospective-item-update.component';
import RetrospectiveItemService from '@/entities/retrospective-item/retrospective-item.service';

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
  describe('RetrospectiveItem Management Update Component', () => {
    let wrapper: Wrapper<RetrospectiveItemClass>;
    let comp: RetrospectiveItemClass;
    let retrospectiveItemServiceStub: SinonStubbedInstance<RetrospectiveItemService>;

    beforeEach(() => {
      retrospectiveItemServiceStub = sinon.createStubInstance<RetrospectiveItemService>(RetrospectiveItemService);

      wrapper = shallowMount<RetrospectiveItemClass>(RetrospectiveItemUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          retrospectiveItemService: () => retrospectiveItemServiceStub,
          alertService: () => new AlertService(),

          retrospectiveTypeService: () =>
            sinon.createStubInstance<RetrospectiveTypeService>(RetrospectiveTypeService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
        },
      });
      comp = wrapper.vm;
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const entity = { id: 123 };
        comp.retrospectiveItem = entity;
        retrospectiveItemServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(retrospectiveItemServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.retrospectiveItem = entity;
        retrospectiveItemServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(retrospectiveItemServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundRetrospectiveItem = { id: 123 };
        retrospectiveItemServiceStub.find.resolves(foundRetrospectiveItem);
        retrospectiveItemServiceStub.retrieve.resolves([foundRetrospectiveItem]);

        // WHEN
        comp.beforeRouteEnter({ params: { retrospectiveItemId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.retrospectiveItem).toBe(foundRetrospectiveItem);
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
