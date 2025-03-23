(function() {
    'use strict';

    angular
        .module('smartmeterApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('vibration-eco-message', {
            parent: 'entity',
            url: '/vibration-eco-message?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'smartmeterApp.vibrationEcoMessage.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/vibration-eco-message/vibration-eco-messages.html',
                    controller: 'VibrationEcoMessageController',
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
                    $translatePartialLoader.addPart('vibrationEcoMessage');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('vibration-eco-message-detail', {
            parent: 'vibration-eco-message',
            url: '/vibration-eco-message/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'smartmeterApp.vibrationEcoMessage.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/vibration-eco-message/vibration-eco-message-detail.html',
                    controller: 'VibrationEcoMessageDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('vibrationEcoMessage');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'VibrationEcoMessage', function($stateParams, VibrationEcoMessage) {
                    return VibrationEcoMessage.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'vibration-eco-message',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('vibration-eco-message-detail.edit', {
            parent: 'vibration-eco-message-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/vibration-eco-message/vibration-eco-message-dialog.html',
                    controller: 'VibrationEcoMessageDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['VibrationEcoMessage', function(VibrationEcoMessage) {
                            return VibrationEcoMessage.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('vibration-eco-message.new', {
            parent: 'vibration-eco-message',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/vibration-eco-message/vibration-eco-message-dialog.html',
                    controller: 'VibrationEcoMessageDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                batteryValue: null,
                                xAxisValue: null,
                                yAxisValue: null,
                                zAxisValue: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('vibration-eco-message', null, { reload: 'vibration-eco-message' });
                }, function() {
                    $state.go('vibration-eco-message');
                });
            }]
        })
        .state('vibration-eco-message.edit', {
            parent: 'vibration-eco-message',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/vibration-eco-message/vibration-eco-message-dialog.html',
                    controller: 'VibrationEcoMessageDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['VibrationEcoMessage', function(VibrationEcoMessage) {
                            return VibrationEcoMessage.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('vibration-eco-message', null, { reload: 'vibration-eco-message' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('vibration-eco-message.delete', {
            parent: 'vibration-eco-message',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/vibration-eco-message/vibration-eco-message-delete-dialog.html',
                    controller: 'VibrationEcoMessageDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['VibrationEcoMessage', function(VibrationEcoMessage) {
                            return VibrationEcoMessage.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('vibration-eco-message', null, { reload: 'vibration-eco-message' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
