'use strict';

angular.module('spevnikApp')
    .controller('SongController', function ($scope, $state, Song) {

        $scope.songs = [];
        $scope.loadAll = function() {
            Song.query(function(result) {
               $scope.songs = result;
            });
        };
        $scope.loadAll();


        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.song = {
                songText: null,
                id: null
            };
        };
    });
