(function() {
    'use strict';

    angular
        .module('spevnikApp')
        .controller('PlaylistDialogController', PlaylistDialogController);

    PlaylistDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Playlist', 'Song', 'AuthServerProvider'];

    function PlaylistDialogController ($scope, $stateParams, $uibModalInstance, entity, Playlist, Song, AuthServerProvider) {
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

        var saveWithOauthToken = function (response) {
            vm.accessToken = response.access_token;

            vm.playlist.status = 'publish';
            
            if (vm.playlist.id !== null) {
                Playlist.update({id:vm.playlist.id, access_token:vm.accessToken},vm.playlist, onSaveSuccess, onSaveError);
            } else {
                Playlist.save({access_token:vm.accessToken},vm.playlist, onSaveSuccess, onSaveError);
            }

        }

        vm.save = function () {
            vm.isSaving = true;
            AuthServerProvider.getOauthToken().success(saveWithOauthToken);
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
