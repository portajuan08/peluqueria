(function() {
    'use strict';
    angular
        .module('peluqueriaApp')
        .factory('Corte', Corte);

    Corte.$inject = ['$resource', 'DateUtils'];

    function Corte ($resource, DateUtils) {
        var resourceUrl =  'api/cortes/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.fecha = DateUtils.convertDateTimeFromServer(data.fecha);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
