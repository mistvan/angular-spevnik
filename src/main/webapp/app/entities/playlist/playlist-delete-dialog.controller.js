(function() {
    'use strict';

    angular
        .module('spevnikApp')
        .controller('PlaylistDeleteController',PlaylistDeleteController);

    PlaylistDeleteController.$inject = ['$uibModalInstance', 'entity', 'Playlist'];

    function PlaylistDeleteController($uibModalInstance, entity, Playlist) {
        var vm = this;
        vm.playlist = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Playlist.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
