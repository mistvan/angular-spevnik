(function() {
    'use strict';

    angular
        .module('spevnikApp')
        .controller('SongDialogController', SongDialogController);

    SongDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Song'];

    function SongDialogController ($scope, $stateParams, $uibModalInstance, entity, Song) {
        var vm = this;
        vm.song = entity;
        vm.load = function(id) {
            Song.get({id : id}, function(result) {
                vm.song = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('spevnikApp:songUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.song.id !== null) {
                Song.update(vm.song, onSaveSuccess, onSaveError);
            } else {
                Song.save(vm.song, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
