'use strict';

angular.module('spevnikApp').controller('SongDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Song',
        function($scope, $stateParams, $uibModalInstance, entity, Song) {

        $scope.song = entity;
        $scope.load = function(id) {
            Song.get({id : id}, function(result) {
                $scope.song = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('App:songUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.song.id != null) {
                Song.update($scope.song, onSaveSuccess, onSaveError);
            } else {
                Song.save($scope.song, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
