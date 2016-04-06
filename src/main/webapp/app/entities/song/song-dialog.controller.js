(function() {
    'use strict';

    angular
        .module('spevnikApp')
        .controller('SongDialogController', SongDialogController);

    SongDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Song', 'Playlist', 'AuthServerProvider'];

    function SongDialogController ($scope, $stateParams, $uibModalInstance, entity, Song, Playlist, AuthServerProvider) {
        var vm = this;
        vm.song = entity;
        vm.playlists = Playlist.query();
        vm.load = function(id) {
            Song.get({id : id}, function(result) {
                vm.song = result;
            });
        };
        vm.accessToken = '';

        var onSaveSuccess = function (result) {
            $scope.$emit('spevnikApp:songUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        var saveWithOauthToken = function (response) {
            vm.accessToken = response.access_token;

            if (vm.song.id !== null) {
                Song.update(vm.song, onSaveSuccess, onSaveError);
            } else {
                Song.save({access_token:vm.accessToken}, vm.song, onSaveSuccess, onSaveError);
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
