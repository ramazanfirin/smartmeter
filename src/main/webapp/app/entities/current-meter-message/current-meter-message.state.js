(function() {
    'use strict';

    angular
        .module('smartmeterApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('current-meter-message', {
            parent: 'entity',
            url: '/current-meter-message?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'smartmeterApp.currentMeterMessage.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/current-meter-message/current-meter-messages.html',
                    controller: 'CurrentMeterMessageController',
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
                    $translatePartialLoader.addPart('currentMeterMessage');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('current-meter-message-detail', {
            parent: 'current-meter-message',
            url: '/current-meter-message/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'smartmeterApp.currentMeterMessage.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/current-meter-message/current-meter-message-detail.html',
                    controller: 'CurrentMeterMessageDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('currentMeterMessage');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'CurrentMeterMessage', function($stateParams, CurrentMeterMessage) {
                    return CurrentMeterMessage.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'current-meter-message',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('current-meter-message-detail.edit', {
            parent: 'current-meter-message-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/current-meter-message/current-meter-message-dialog.html',
                    controller: 'CurrentMeterMessageDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['CurrentMeterMessage', function(CurrentMeterMessage) {
                            return CurrentMeterMessage.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('current-meter-message.new', {
            parent: 'current-meter-message',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/current-meter-message/current-meter-message-dialog.html',
                    controller: 'CurrentMeterMessageDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                batteryValue: null,
                                current: null,
                                totalEnergy: null,
                                reason: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('current-meter-message', null, { reload: 'current-meter-message' });
                }, function() {
                    $state.go('current-meter-message');
                });
            }]
        })
        .state('current-meter-message.edit', {
            parent: 'current-meter-message',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/current-meter-message/current-meter-message-dialog.html',
                    controller: 'CurrentMeterMessageDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['CurrentMeterMessage', function(CurrentMeterMessage) {
                            return CurrentMeterMessage.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('current-meter-message', null, { reload: 'current-meter-message' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('current-meter-message.delete', {
            parent: 'current-meter-message',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/current-meter-message/current-meter-message-delete-dialog.html',
                    controller: 'CurrentMeterMessageDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['CurrentMeterMessage', function(CurrentMeterMessage) {
                            return CurrentMeterMessage.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('current-meter-message', null, { reload: 'current-meter-message' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
