(function() {
    'use strict';
    angular
        .module('peluqueriaApp')
        .factory('Cliente', Cliente);

    Cliente.$inject = ['$resource', 'DateUtils'];

    function Cliente ($resource, DateUtils) {
        var resourceUrl =  'api/clientes/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.fecha_nacimiento = DateUtils.convertLocalDateFromServer(data.fecha_nacimiento);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.fecha_nacimiento = DateUtils.convertLocalDateToServer(copy.fecha_nacimiento);
                    return angular.toJson(copy);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.fecha_nacimiento = DateUtils.convertLocalDateToServer(copy.fecha_nacimiento);
                    return angular.toJson(copy);
                }
            }
        });
    }
})();
