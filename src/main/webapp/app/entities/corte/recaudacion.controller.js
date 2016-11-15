(function() {
    'use strict';

    angular
        .module('peluqueriaApp')
        .controller('RecaudacionController', RecaudacionController);

    RecaudacionController.$inject = ['$scope', '$state', 'Corte', 'CorteSearch', 'ParseLinks', 'AlertService', 'searchParams', 'paginationConstants','$http'];

    function RecaudacionController ($scope, $state, Corte, CorteSearch, ParseLinks, AlertService, searchParams, paginationConstants,$http) {
        var vm = this;

        vm.search = search;
        vm.getCortes = getCortes;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.fecha_desde = searchParams.fecha_desde;
        vm.fecha_hasta = searchParams.fecha_hasta;
        vm.tipo_corte = searchParams.tipo_corte;
        
        getCortes();

        function getCortes () {
        	
        	
        	$http({
				method : 'GET',
				url : "/api/recaudacion/",
				headers : {
					'Content-Type' : "application/json"
				},
				params: {
					fechaDesde : vm.fecha_desde.getTime(),
					fechaHasta : vm.fecha_hasta.getTime(),
					tipoCorte : vm.tipo_corte 
				},
				data: null
				
			}).then(function(result) {
				vm.cortes = result.data;
			}, function(reason) {
				AlertService.error(reason);
			});
        }
        
        function search (fechaDesde,fechaHasta,tipoCorte) {
            vm.fecha_desde = fechaDesde;
            vm.fecha_hasta = fechaHasta;
            vm.tipo_corte = tipoCorte;
            getCortes();
        }
        
        vm.datePickerOpenStatus.fecha_desde = false;
        vm.datePickerOpenStatus.fecha_hasta = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
