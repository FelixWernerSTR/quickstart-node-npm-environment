/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import RetrospectiveItemDetailComponent from '@/entities/retrospective-item/retrospective-item-details.vue';
import RetrospectiveItemClass from '@/entities/retrospective-item/retrospective-item-details.component';
import RetrospectiveItemService from '@/entities/retrospective-item/retrospective-item.service';
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
  describe('RetrospectiveItem Management Detail Component', () => {
    let wrapper: Wrapper<RetrospectiveItemClass>;
    let comp: RetrospectiveItemClass;
    let retrospectiveItemServiceStub: SinonStubbedInstance<RetrospectiveItemService>;

    beforeEach(() => {
      retrospectiveItemServiceStub = sinon.createStubInstance<RetrospectiveItemService>(RetrospectiveItemService);

      wrapper = shallowMount<RetrospectiveItemClass>(RetrospectiveItemDetailComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: { retrospectiveItemService: () => retrospectiveItemServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundRetrospectiveItem = { id: 123 };
        retrospectiveItemServiceStub.find.resolves(foundRetrospectiveItem);

        // WHEN
        comp.retrieveRetrospectiveItem(123);
        await comp.$nextTick();

        // THEN
        expect(comp.retrospectiveItem).toBe(foundRetrospectiveItem);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundRetrospectiveItem = { id: 123 };
        retrospectiveItemServiceStub.find.resolves(foundRetrospectiveItem);

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
