(function() {
    'use strict';

    angular
        .module('peluqueriaApp')
        .factory('CorteSearch', CorteSearch);

    CorteSearch.$inject = ['$resource'];

    function CorteSearch($resource) {
        var resourceUrl =  'api/_search/cortes/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
