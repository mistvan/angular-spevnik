(function() {
    'use strict';

    angular
        .module('spevnikApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('playlist', {
            parent: 'entity',
            url: '/playlist',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'spevnikApp.playlist.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/playlist/playlists.html',
                    controller: 'PlaylistController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('playlist');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('playlist-detail', {
            parent: 'entity',
            url: '/playlist/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'spevnikApp.playlist.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/playlist/playlist-detail.html',
                    controller: 'PlaylistDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('playlist');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Playlist', function($stateParams, Playlist) {
                    return Playlist.get({id : $stateParams.id});
                }]
            }
        })
        .state('playlist.new', {
            parent: 'playlist',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/playlist/playlist-dialog.html',
                    controller: 'PlaylistDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                slug: null,
                                link: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('playlist', null, { reload: true });
                }, function() {
                    $state.go('playlist');
                });
            }]
        })
        .state('playlist.edit', {
            parent: 'playlist',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/playlist/playlist-dialog.html',
                    controller: 'PlaylistDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Playlist', function(Playlist) {
                            return Playlist.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('playlist', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('playlist.delete', {
            parent: 'playlist',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/playlist/playlist-delete-dialog.html',
                    controller: 'PlaylistDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Playlist', function(Playlist) {
                            return Playlist.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('playlist', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
