(function() {
    'use strict';

    angular
        .module('peluqueriaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('corte', {
            parent: 'entity',
            url: '/corte',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'peluqueriaApp.corte.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/corte/cortes.html',
                    controller: 'CorteController',
                    controllerAs: 'vm'
                }
            },
            params: { },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('corte');
                    $translatePartialLoader.addPart('tipo_corte');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('recaudacion', {
            parent: 'entity',
            url: '/recaudacion',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'peluqueriaApp.corte.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/corte/recaudacion.html',
                    controller: 'RecaudacionController',
                    controllerAs: 'vm'
                }
            },
            params: {
                fecha_desde : new Date(),
                fecha_hasta : new Date(),
                tipo_corte : "Todos"
            },
            resolve: {
                searchParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        tipo_corte : $stateParams.tipo_corte,
                        fecha_desde : $stateParams.fecha_desde,
                        fecha_hasta : $stateParams.fecha_hasta
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('corte');
                    $translatePartialLoader.addPart('tipo_corte');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('corte-detail', {
            parent: 'entity',
            url: '/corte/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'peluqueriaApp.corte.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/corte/corte-detail.html',
                    controller: 'CorteDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('corte');
                    $translatePartialLoader.addPart('tipo_corte');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Corte', function($stateParams, Corte) {
                    return Corte.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'corte',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('corte-detail.edit', {
            parent: 'corte-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/corte/corte-dialog.html',
                    controller: 'CorteDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Corte', function(Corte) {
                            return Corte.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('corte.new', {
            parent: 'corte',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/corte/corte-new-dialog.html',
                    controller: 'CorteNewDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                fecha: $stateParams.fecha,
                                precio: null,
                                tipo_corte: null,
                                detalle: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('corte', null, { reload: 'corte' });
                }, function() {
                    $state.go('corte');
                });
            }]
        })
        .state('corte.edit', {
            parent: 'corte',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/corte/corte-dialog.html',
                    controller: 'CorteDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Corte', function(Corte) {
                            return Corte.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('corte', null, { reload: 'corte' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('corte.delete', {
            parent: 'corte',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/corte/corte-delete-dialog.html',
                    controller: 'CorteDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Corte', function(Corte) {
                            return Corte.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('corte', null, { reload: 'corte' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
