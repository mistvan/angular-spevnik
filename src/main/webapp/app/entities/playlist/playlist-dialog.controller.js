(function() {
    'use strict';

    angular
        .module('spevnikApp')
        .controller('PlaylistDialogController', PlaylistDialogController);

    PlaylistDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Playlist', 'Song'];

    function PlaylistDialogController ($scope, $stateParams, $uibModalInstance, entity, Playlist, Song) {
        var vm = this;
        vm.playlist = entity;
        vm.songs = Song.query();
        vm.load = function(id) {
            Playlist.get({id : id}, function(result) {
                vm.playlist = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('spevnikApp:playlistUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.playlist.id !== null) {
                Playlist.update(vm.playlist, onSaveSuccess, onSaveError);
            } else {
                Playlist.save(vm.playlist, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
