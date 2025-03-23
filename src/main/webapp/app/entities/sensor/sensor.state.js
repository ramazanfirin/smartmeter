(function() {
    'use strict';

    angular
        .module('smartmeterApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('sensor', {
            parent: 'entity',
            url: '/sensor?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'smartmeterApp.sensor.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/sensor/sensors.html',
                    controller: 'SensorController',
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
                    $translatePartialLoader.addPart('sensor');
                    $translatePartialLoader.addPart('connectionType');
                    $translatePartialLoader.addPart('type');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('sensor-detail', {
            parent: 'sensor',
            url: '/sensor/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'smartmeterApp.sensor.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/sensor/sensor-detail.html',
                    controller: 'SensorDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('sensor');
                    $translatePartialLoader.addPart('connectionType');
                    $translatePartialLoader.addPart('type');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Sensor', function($stateParams, Sensor) {
                    return Sensor.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'sensor',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('sensor-detail.edit', {
            parent: 'sensor-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/sensor/sensor-dialog.html',
                    controller: 'SensorDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Sensor', function(Sensor) {
                            return Sensor.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('sensor.new', {
            parent: 'sensor',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/sensor/sensor-dialog.html',
                    controller: 'SensorDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                devEui: null,
                                appEui: null,
                                appKey: null,
                                imei: null,
                                connectionType: null,
                                type: null,
                                thingsBoardDeviceId: null,
                                lastSeenDate: null,
                                lastMessage: null,
                                lastImage: null,
                                lastImageContentType: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('sensor', null, { reload: 'sensor' });
                }, function() {
                    $state.go('sensor');
                });
            }]
        })
        .state('sensor.edit', {
            parent: 'sensor',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/sensor/sensor-dialog.html',
                    controller: 'SensorDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Sensor', function(Sensor) {
                            return Sensor.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('sensor', null, { reload: 'sensor' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('sensor.delete', {
            parent: 'sensor',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/sensor/sensor-delete-dialog.html',
                    controller: 'SensorDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Sensor', function(Sensor) {
                            return Sensor.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('sensor', null, { reload: 'sensor' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
