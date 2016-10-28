(function() {
    'use strict';

    angular
        .module('peluqueriaApp')
        .controller('CorteDeleteController',CorteDeleteController);

    CorteDeleteController.$inject = ['$uibModalInstance', 'entity', 'Corte'];

    function CorteDeleteController($uibModalInstance, entity, Corte) {
        var vm = this;

        vm.corte = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Corte.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
