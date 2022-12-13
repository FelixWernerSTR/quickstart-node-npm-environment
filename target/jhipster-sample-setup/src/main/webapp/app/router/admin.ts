import { Authority } from '@/shared/security/authority';

const SviDocsComponent = () => import('@/admin/docs/docs.vue');
const SviConfigurationComponent = () => import('@/admin/configuration/configuration.vue');
const SviHealthComponent = () => import('@/admin/health/health.vue');
const SviLogsComponent = () => import('@/admin/logs/logs.vue');
const SviMetricsComponent = () => import('@/admin/metrics/metrics.vue');

export default [
  {
    path: '/admin/docs',
    name: 'SviDocsComponent',
    component: SviDocsComponent,
    meta: { authorities: [Authority.ADMIN] },
  },
  {
    path: '/admin/health',
    name: 'SviHealthComponent',
    component: SviHealthComponent,
    meta: { authorities: [Authority.ADMIN] },
  },
  {
    path: '/admin/logs',
    name: 'SviLogsComponent',
    component: SviLogsComponent,
    meta: { authorities: [Authority.ADMIN] },
  },
  {
    path: '/admin/metrics',
    name: 'SviMetricsComponent',
    component: SviMetricsComponent,
    meta: { authorities: [Authority.ADMIN] },
  },
  {
    path: '/admin/configuration',
    name: 'SviConfigurationComponent',
    component: SviConfigurationComponent,
    meta: { authorities: [Authority.ADMIN] },
  },
];
