(function() {
    'use strict';

    angular
        .module('spevnikApp')
        .controller('SongDetailController', SongDetailController);

    SongDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Song'];

    function SongDetailController($scope, $rootScope, $stateParams, entity, Song) {
        var vm = this;
        vm.song = entity;
        vm.load = function (id) {
            Song.get({id: id}, function(result) {
                vm.song = result;
            });
        };
        var unsubscribe = $rootScope.$on('spevnikApp:songUpdate', function(event, result) {
            vm.song = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
