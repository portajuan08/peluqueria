(function() {
    'use strict';

    angular
        .module('peluqueriaApp')
        .controller('CorteController', CorteController);

    CorteController.$inject = ['$scope', '$state', 'Corte', 'CorteSearch', 'ParseLinks', 'AlertService', 'pagingParams', 'paginationConstants'];

    function CorteController ($scope, $state, Corte, CorteSearch, ParseLinks, AlertService, pagingParams, paginationConstants) {
    	var date = new Date();
        var d = date.getDate();
        var m = date.getMonth();
        var y = date.getFullYear();

        $scope.cortes = [];
        
        /* alert on eventClick */
        $scope.clickCorte = function( date, jsEvent, view){
        	console.log("clickeo corte");
        	$state.go('corte.edit', {'id': date.id});
        };
        
        $scope.loadAll =  function(view, element) {
            Corte.query({
                page: 0,
                size: 100
            }, onSuccess, onError);
            
            function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== 'id') {
                    result.push('id');
                }
                return result;
            }
            function onSuccess(cortes, headers) {
				$scope.clearCortes();
				for(var i=0; i< cortes.length; i++){
					var corte = cortes[i];
					var fecha_ini = new Date(corte.fecha);
					var corteJson = { title: corte.cliente.nombre + " " + corte.cliente.apellido,
						id: corte.id, 
						start: fecha_ini,
						end:  new Date(fecha_ini.getTime() + 20*60000)
					};
					$scope.addCorte(corteJson);
				}
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        }
		
    	$scope.uiConfig = {
  		      calendar:{
  		        defaultView: 'agendaDay',
  		        allDaySlot : false,
  		        //height: 450,
  		        editable: false,
  		        customButtons: {
  		            agregarCorte: {
  		                text: 'Agregar Nuevo Corte',
  		                click: function() {
  		                    $state.go('corte.new');
  		                }
  		            }
  		        },
  		        header:{
  		          left: 'agregarCorte',
  		          center: 'title',
  		          right: 'today prev,next'
  		        },
  				viewRender: $scope.loadAll,
  		        eventClick: $scope.clickCorte
  		      }
  	    };
    	/* borramos todos los cortes*/
		$scope.clearCortes = function(){
			$scope.cortes.splice(0,$scope.cortes.length);
		};
    	
		/* add custom event*/
		$scope.addCorte = function(corte) {
			$scope.cortes.push(corte);
		};
		
	   	/* event sources array*/
	   	$scope.eventSources = [$scope.cortes];
    }
})();
