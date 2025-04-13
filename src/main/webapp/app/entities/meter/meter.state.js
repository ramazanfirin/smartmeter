(function() {
    'use strict';

    angular
        .module('smartmeterApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('meter', {
            parent: 'entity',
            url: '/meter?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'smartmeterApp.meter.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/meter/meters.html',
                    controller: 'MeterController',
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
                    $translatePartialLoader.addPart('meter');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('meter-detail', {
            parent: 'meter',
            url: '/meter/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'smartmeterApp.meter.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/meter/meter-detail.html',
                    controller: 'MeterDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('meter');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Meter', function($stateParams, Meter) {
                    return Meter.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'meter',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('meter-detail.edit', {
            parent: 'meter-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/meter/meter-dialog.html',
                    controller: 'MeterDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Meter', function(Meter) {
                            return Meter.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('meter.new', {
            parent: 'meter',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/meter/meter-dialog.html',
                    controller: 'MeterDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                meterNo: null,
                                customerNo: null,
                                address: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('meter', null, { reload: 'meter' });
                }, function() {
                    $state.go('meter');
                });
            }]
        })
        .state('meter.edit', {
            parent: 'meter',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/meter/meter-dialog.html',
                    controller: 'MeterDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Meter', function(Meter) {
                            return Meter.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('meter', null, { reload: 'meter' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('meter.delete', {
            parent: 'meter',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/meter/meter-delete-dialog.html',
                    controller: 'MeterDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Meter', function(Meter) {
                            return Meter.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('meter', null, { reload: 'meter' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
