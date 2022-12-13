import { Authority } from '@/shared/security/authority';
/* tslint:disable */
// prettier-ignore
const Entities = () => import('@/entities/entities.vue');

const RetrospectiveType = () => import('@/entities/retrospective-type/retrospective-type.vue');
const RetrospectiveTypeUpdate = () => import('@/entities/retrospective-type/retrospective-type-update.vue');
const RetrospectiveTypeDetails = () => import('@/entities/retrospective-type/retrospective-type-details.vue');

const RetrospectiveItem = () => import('@/entities/retrospective-item/retrospective-item.vue');
const RetrospectiveItemUpdate = () => import('@/entities/retrospective-item/retrospective-item-update.vue');
const RetrospectiveItemDetails = () => import('@/entities/retrospective-item/retrospective-item-details.vue');

// jhipster-needle-add-entity-to-router-import - JHipster will import entities to the router here

export default {
  path: '/',
  component: Entities,
  children: [
    {
      path: 'retrospective-type',
      name: 'RetrospectiveType',
      component: RetrospectiveType,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'retrospective-type/new',
      name: 'RetrospectiveTypeCreate',
      component: RetrospectiveTypeUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'retrospective-type/:retrospectiveTypeId/edit',
      name: 'RetrospectiveTypeEdit',
      component: RetrospectiveTypeUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'retrospective-type/:retrospectiveTypeId/view',
      name: 'RetrospectiveTypeView',
      component: RetrospectiveTypeDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'retrospective-item',
      name: 'RetrospectiveItem',
      component: RetrospectiveItem,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'retrospective-item/new',
      name: 'RetrospectiveItemCreate',
      component: RetrospectiveItemUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'retrospective-item/:retrospectiveItemId/edit',
      name: 'RetrospectiveItemEdit',
      component: RetrospectiveItemUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'retrospective-item/:retrospectiveItemId/view',
      name: 'RetrospectiveItemView',
      component: RetrospectiveItemDetails,
      meta: { authorities: [Authority.USER] },
    },
    // jhipster-needle-add-entity-to-router - JHipster will add entities to the router here
  ],
};
