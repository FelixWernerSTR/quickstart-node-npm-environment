/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import RetrospectiveItemComponent from '@/entities/retrospective-item/retrospective-item.vue';
import RetrospectiveItemClass from '@/entities/retrospective-item/retrospective-item.component';
import RetrospectiveItemService from '@/entities/retrospective-item/retrospective-item.service';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();
localVue.use(ToastPlugin);

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('b-badge', {});
localVue.component('jhi-sort-indicator', {});
localVue.directive('b-modal', {});
localVue.component('b-button', {});
localVue.component('router-link', {});

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  describe('RetrospectiveItem Management Component', () => {
    let wrapper: Wrapper<RetrospectiveItemClass>;
    let comp: RetrospectiveItemClass;
    let retrospectiveItemServiceStub: SinonStubbedInstance<RetrospectiveItemService>;

    beforeEach(() => {
      retrospectiveItemServiceStub = sinon.createStubInstance<RetrospectiveItemService>(RetrospectiveItemService);
      retrospectiveItemServiceStub.retrieve.resolves({ headers: {} });

      wrapper = shallowMount<RetrospectiveItemClass>(RetrospectiveItemComponent, {
        store,
        i18n,
        localVue,
        stubs: { jhiItemCount: true, bPagination: true, bModal: bModalStub as any },
        provide: {
          retrospectiveItemService: () => retrospectiveItemServiceStub,
          alertService: () => new AlertService(),
        },
      });
      comp = wrapper.vm;
    });

    it('Should call load all on init', async () => {
      // GIVEN
      retrospectiveItemServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

      // WHEN
      comp.retrieveAllRetrospectiveItems();
      await comp.$nextTick();

      // THEN
      expect(retrospectiveItemServiceStub.retrieve.called).toBeTruthy();
      expect(comp.retrospectiveItems[0]).toEqual(expect.objectContaining({ id: 123 }));
    });

    it('should load a page', async () => {
      // GIVEN
      retrospectiveItemServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });
      comp.previousPage = 1;

      // WHEN
      comp.loadPage(2);
      await comp.$nextTick();

      // THEN
      expect(retrospectiveItemServiceStub.retrieve.called).toBeTruthy();
      expect(comp.retrospectiveItems[0]).toEqual(expect.objectContaining({ id: 123 }));
    });

    it('should re-initialize the page', async () => {
      // GIVEN
      retrospectiveItemServiceStub.retrieve.reset();
      retrospectiveItemServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

      // WHEN
      comp.loadPage(2);
      await comp.$nextTick();
      comp.clear();
      await comp.$nextTick();

      // THEN
      expect(retrospectiveItemServiceStub.retrieve.callCount).toEqual(2);
      expect(comp.page).toEqual(1);
      expect(comp.retrospectiveItems[0]).toEqual(expect.objectContaining({ id: 123 }));
    });

    it('should calculate the sort attribute for an id', () => {
      // WHEN
      const result = comp.sort();

      // THEN
      expect(result).toEqual(['id,asc']);
    });

    it('should calculate the sort attribute for a non-id attribute', () => {
      // GIVEN
      comp.propOrder = 'name';

      // WHEN
      const result = comp.sort();

      // THEN
      expect(result).toEqual(['name,asc', 'id']);
    });
    it('Should call delete service on confirmDelete', async () => {
      // GIVEN
      retrospectiveItemServiceStub.delete.resolves({});

      // WHEN
      comp.prepareRemove({ id: 123 });
      expect(retrospectiveItemServiceStub.retrieve.callCount).toEqual(1);

      comp.removeRetrospectiveItem();
      await comp.$nextTick();

      // THEN
      expect(retrospectiveItemServiceStub.delete.called).toBeTruthy();
      expect(retrospectiveItemServiceStub.retrieve.callCount).toEqual(2);
    });
  });
});
