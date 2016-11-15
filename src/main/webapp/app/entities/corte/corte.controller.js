(function() {
    'use strict';

    angular
        .module('peluqueriaApp')
        .controller('CorteController', CorteController);

    CorteController.$inject = ['$scope', '$state', 'Corte', 'CorteSearch', 'ParseLinks', 'AlertService', 'paginationConstants','$http'];

    function CorteController ($scope, $state, Corte, CorteSearch, ParseLinks, AlertService, paginationConstants,$http) {
    	var date = new Date();
        var d = date.getDate();
        var m = date.getMonth();
        var y = date.getFullYear();
        

        $scope.cortes = [];
        
        /* alert on eventClick */
        $scope.clickCorte = function( date, jsEvent, view){
        	$state.go('corte.edit', {'id': date.id});
        };

        $scope.loadAll =  function(view, element) {
        	var moment = $('#calendar').fullCalendar('getDate');
        	$http({
				method : 'GET',
				url : "/api/cortes/",
				headers : {
					'Content-Type' : "application/json"
				},
				params: {
					fecha : moment.toDate().getTime()
				},
				data: null
				
			}).then(function(result) {
				$scope.clearCortes();
				var cortes = result.data;
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
			}, function(reason) {
				AlertService.error(reason);
			});
        }
		
    	$scope.uiConfig = {
  		      calendar:{
  		    	//contentHeight: 450,
  		    	//aspectRatio: 3,
  		        defaultView: 'agendaDay',
  		        allDaySlot : false,
  		        //height: 450,
  		        editable: false,
  		        customButtons: {
  		            agregarCorte: {
  		                text: 'Agregar nuevo Servicio',
  		                click: function() {
  		                	var fecha = $('#calendar').fullCalendar('getDate');
  		                    $state.go('corte.new',{'fecha': fecha.format()});
  		                }
  		            }
  		        },
  		        header:{
  		          left: 'agregarCorte',
  		          center: 'title',
  		          right: 'today month day prev,next',
  		          today : 'Hoy'
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
