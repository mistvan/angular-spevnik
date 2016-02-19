'use strict';

angular.module('spevnikApp')
    .controller('SongDetailController', function ($scope, $rootScope, $stateParams, entity, Song) {
        $scope.song = entity;
        $scope.load = function (id) {
            Song.get({id: id}, function(result) {
                $scope.song = result;
            });
        };
        var unsubscribe = $rootScope.$on('App:songUpdate', function(event, result) {
            $scope.song = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
