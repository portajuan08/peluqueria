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
            url: '/corte?page&sort&search',
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
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
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
                                fecha: null,
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
