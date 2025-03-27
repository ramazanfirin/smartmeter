(function() {
    'use strict';

    angular
        .module('smartmeterApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('vibration-pro-message', {
            parent: 'entity',
            url: '/vibration-pro-message?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'smartmeterApp.vibrationProMessage.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/vibration-pro-message/vibration-pro-messages.html',
                    controller: 'VibrationProMessageController',
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
                    $translatePartialLoader.addPart('vibrationProMessage');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('vibration-pro-message-detail', {
            parent: 'vibration-pro-message',
            url: '/vibration-pro-message/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'smartmeterApp.vibrationProMessage.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/vibration-pro-message/vibration-pro-message-detail.html',
                    controller: 'VibrationProMessageDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('vibrationProMessage');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'VibrationProMessage', function($stateParams, VibrationProMessage) {
                    return VibrationProMessage.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'vibration-pro-message',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('vibration-pro-message-detail.edit', {
            parent: 'vibration-pro-message-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/vibration-pro-message/vibration-pro-message-dialog.html',
                    controller: 'VibrationProMessageDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['VibrationProMessage', function(VibrationProMessage) {
                            return VibrationProMessage.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('vibration-pro-message.new', {
            parent: 'vibration-pro-message',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/vibration-pro-message/vibration-pro-message-dialog.html',
                    controller: 'VibrationProMessageDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                batteryValue: null,
                                temperature: null,
                                xVelocity: null,
                                xAcceleration: null,
                                yVelocity: null,
                                yAcceleration: null,
                                zVelocity: null,
                                zAcceleration: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('vibration-pro-message', null, { reload: 'vibration-pro-message' });
                }, function() {
                    $state.go('vibration-pro-message');
                });
            }]
        })
        .state('vibration-pro-message.edit', {
            parent: 'vibration-pro-message',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/vibration-pro-message/vibration-pro-message-dialog.html',
                    controller: 'VibrationProMessageDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['VibrationProMessage', function(VibrationProMessage) {
                            return VibrationProMessage.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('vibration-pro-message', null, { reload: 'vibration-pro-message' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('vibration-pro-message.delete', {
            parent: 'vibration-pro-message',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/vibration-pro-message/vibration-pro-message-delete-dialog.html',
                    controller: 'VibrationProMessageDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['VibrationProMessage', function(VibrationProMessage) {
                            return VibrationProMessage.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('vibration-pro-message', null, { reload: 'vibration-pro-message' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
