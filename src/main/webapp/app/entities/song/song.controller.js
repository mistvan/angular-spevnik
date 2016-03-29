(function() {
    'use strict';

    angular
        .module('spevnikApp')
        .controller('SongController', SongController);

    SongController.$inject = ['$scope', '$state', 'Song'];

    function SongController ($scope, $state, Song) {
        var vm = this;
        vm.songs = [];
        vm.loadAll = function() {
            Song.query(function(result) {
                vm.songs = result;
            });
        };

        vm.loadAll();
        
    }
})();
