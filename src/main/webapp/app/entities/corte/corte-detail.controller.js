(function() {
    'use strict';

    angular
        .module('peluqueriaApp')
        .controller('CorteDetailController', CorteDetailController);

    CorteDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Corte', 'Cliente'];

    function CorteDetailController($scope, $rootScope, $stateParams, previousState, entity, Corte, Cliente) {
        var vm = this;

        vm.corte = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('peluqueriaApp:corteUpdate', function(event, result) {
            vm.corte = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
