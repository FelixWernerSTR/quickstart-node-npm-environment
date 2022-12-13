/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import RetrospectiveTypeDetailComponent from '@/entities/retrospective-type/retrospective-type-details.vue';
import RetrospectiveTypeClass from '@/entities/retrospective-type/retrospective-type-details.component';
import RetrospectiveTypeService from '@/entities/retrospective-type/retrospective-type.service';
import router from '@/router';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();
localVue.use(VueRouter);

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('RetrospectiveType Management Detail Component', () => {
    let wrapper: Wrapper<RetrospectiveTypeClass>;
    let comp: RetrospectiveTypeClass;
    let retrospectiveTypeServiceStub: SinonStubbedInstance<RetrospectiveTypeService>;

    beforeEach(() => {
      retrospectiveTypeServiceStub = sinon.createStubInstance<RetrospectiveTypeService>(RetrospectiveTypeService);

      wrapper = shallowMount<RetrospectiveTypeClass>(RetrospectiveTypeDetailComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: { retrospectiveTypeService: () => retrospectiveTypeServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundRetrospectiveType = { id: 123 };
        retrospectiveTypeServiceStub.find.resolves(foundRetrospectiveType);

        // WHEN
        comp.retrieveRetrospectiveType(123);
        await comp.$nextTick();

        // THEN
        expect(comp.retrospectiveType).toBe(foundRetrospectiveType);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundRetrospectiveType = { id: 123 };
        retrospectiveTypeServiceStub.find.resolves(foundRetrospectiveType);

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
