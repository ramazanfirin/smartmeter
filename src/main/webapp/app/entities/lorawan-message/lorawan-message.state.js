(function() {
    'use strict';

    angular
        .module('smartmeterApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('lorawan-message', {
            parent: 'entity',
            url: '/lorawan-message?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'smartmeterApp.lorawanMessage.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/lorawan-message/lorawan-messages.html',
                    controller: 'LorawanMessageController',
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
                    $translatePartialLoader.addPart('lorawanMessage');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('lorawan-message-detail', {
            parent: 'lorawan-message',
            url: '/lorawan-message/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'smartmeterApp.lorawanMessage.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/lorawan-message/lorawan-message-detail.html',
                    controller: 'LorawanMessageDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('lorawanMessage');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'LorawanMessage', function($stateParams, LorawanMessage) {
                    return LorawanMessage.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'lorawan-message',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('lorawan-message-detail.edit', {
            parent: 'lorawan-message-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/lorawan-message/lorawan-message-dialog.html',
                    controller: 'LorawanMessageDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['LorawanMessage', function(LorawanMessage) {
                            return LorawanMessage.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('lorawan-message.new', {
            parent: 'lorawan-message',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/lorawan-message/lorawan-message-dialog.html',
                    controller: 'LorawanMessageDialogController',
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
                                fPort: null,
                                fCnt: null,
                                imageId: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('lorawan-message', null, { reload: 'lorawan-message' });
                }, function() {
                    $state.go('lorawan-message');
                });
            }]
        })
        .state('lorawan-message.edit', {
            parent: 'lorawan-message',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/lorawan-message/lorawan-message-dialog.html',
                    controller: 'LorawanMessageDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['LorawanMessage', function(LorawanMessage) {
                            return LorawanMessage.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('lorawan-message', null, { reload: 'lorawan-message' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('lorawan-message.delete', {
            parent: 'lorawan-message',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/lorawan-message/lorawan-message-delete-dialog.html',
                    controller: 'LorawanMessageDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['LorawanMessage', function(LorawanMessage) {
                            return LorawanMessage.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('lorawan-message', null, { reload: 'lorawan-message' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
