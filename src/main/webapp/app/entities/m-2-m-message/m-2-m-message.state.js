(function() {
    'use strict';

    angular
        .module('smartmeterApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('m-2-m-message', {
            parent: 'entity',
            url: '/m-2-m-message?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'smartmeterApp.m2mMessage.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/m-2-m-message/m-2-m-messages.html',
                    controller: 'M2mMessageController',
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
                    $translatePartialLoader.addPart('m2mMessage');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('m-2-m-message-detail', {
            parent: 'm-2-m-message',
            url: '/m-2-m-message/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'smartmeterApp.m2mMessage.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/m-2-m-message/m-2-m-message-detail.html',
                    controller: 'M2mMessageDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('m2mMessage');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'M2mMessage', function($stateParams, M2mMessage) {
                    return M2mMessage.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'm-2-m-message',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('m-2-m-message-detail.edit', {
            parent: 'm-2-m-message-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/m-2-m-message/m-2-m-message-dialog.html',
                    controller: 'M2mMessageDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['M2mMessage', function(M2mMessage) {
                            return M2mMessage.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('m-2-m-message.new', {
            parent: 'm-2-m-message',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/m-2-m-message/m-2-m-message-dialog.html',
                    controller: 'M2mMessageDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                base64Message: null,
                                hexMessage: null,
                                index: null,
                                totalMessageCount: null,
                                insertDate: null,
                                image: null,
                                imageContentType: null,
                                batteryValue: null,
                                sensorValue: null,
                                port: null,
                                imageData: null,
                                validImage: null,
                                ip: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('m-2-m-message', null, { reload: 'm-2-m-message' });
                }, function() {
                    $state.go('m-2-m-message');
                });
            }]
        })
        .state('m-2-m-message.edit', {
            parent: 'm-2-m-message',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/m-2-m-message/m-2-m-message-dialog.html',
                    controller: 'M2mMessageDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['M2mMessage', function(M2mMessage) {
                            return M2mMessage.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('m-2-m-message', null, { reload: 'm-2-m-message' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('m-2-m-message.delete', {
            parent: 'm-2-m-message',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/m-2-m-message/m-2-m-message-delete-dialog.html',
                    controller: 'M2mMessageDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['M2mMessage', function(M2mMessage) {
                            return M2mMessage.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('m-2-m-message', null, { reload: 'm-2-m-message' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
