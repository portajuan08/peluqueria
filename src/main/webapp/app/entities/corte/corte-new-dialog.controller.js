(function() {
    'use strict';

    angular
        .module('peluqueriaApp')
        .controller('CorteNewDialogController', CorteNewDialogController);

    CorteNewDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Corte', 'Cliente'];

    function CorteNewDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Corte, Cliente) {
        var vm = this;

        vm.corte = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.clientes = Cliente.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.corte.id !== null) {
                Corte.update(vm.corte, onSaveSuccess, onSaveError);
            } else {
                Corte.save(vm.corte, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('peluqueriaApp:corteUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.fecha = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();