(function() {
    'use strict';

    angular
        .module('spevnikApp')
        .controller('PlaylistController', PlaylistController);

    PlaylistController.$inject = ['$scope', '$state', 'Playlist'];

    function PlaylistController ($scope, $state, Playlist) {
        var vm = this;
        vm.playlists = [];
        vm.loadAll = function() {
            Playlist.query(function(result) {
                vm.playlists = result;
            });
        };

        vm.loadAll();
        
    }
})();
