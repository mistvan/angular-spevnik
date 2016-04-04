(function() {
    'use strict';

    angular
        .module('spevnikApp')
        .controller('PlaylistDetailController', PlaylistDetailController);

    PlaylistDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Playlist', 'Song'];

    function PlaylistDetailController($scope, $rootScope, $stateParams, entity, Playlist, Song) {
        var vm = this;
        vm.playlist = entity;
        vm.load = function (id) {
            Playlist.get({id: id}, function(result) {
                vm.playlist = result;
            });
        };
        var unsubscribe = $rootScope.$on('spevnikApp:playlistUpdate', function(event, result) {
            vm.playlist = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
