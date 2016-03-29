(function() {
    'use strict';

    angular
        .module('spevnikApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('song', {
            parent: 'entity',
            url: '/song',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'spevnikApp.song.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/song/songs.html',
                    controller: 'SongController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('song');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('song-detail', {
            parent: 'entity',
            url: '/song/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'spevnikApp.song.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/song/song-detail.html',
                    controller: 'SongDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('song');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Song', function($stateParams, Song) {
                    return Song.get({id : $stateParams.id});
                }]
            }
        })
        .state('song.new', {
            parent: 'song',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/song/song-dialog.html',
                    controller: 'SongDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                songText: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('song', null, { reload: true });
                }, function() {
                    $state.go('song');
                });
            }]
        })
        .state('song.edit', {
            parent: 'song',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/song/song-dialog.html',
                    controller: 'SongDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Song', function(Song) {
                            return Song.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('song', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('song.delete', {
            parent: 'song',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/song/song-delete-dialog.html',
                    controller: 'SongDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Song', function(Song) {
                            return Song.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('song', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
