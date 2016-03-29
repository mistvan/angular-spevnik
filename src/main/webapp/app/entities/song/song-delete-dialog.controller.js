(function() {
    'use strict';

    angular
        .module('spevnikApp')
        .controller('SongDeleteController',SongDeleteController);

    SongDeleteController.$inject = ['$uibModalInstance', 'entity', 'Song'];

    function SongDeleteController($uibModalInstance, entity, Song) {
        var vm = this;
        vm.song = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Song.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
