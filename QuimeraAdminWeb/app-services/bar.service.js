(function () {
    'use strict';

    angular
        .module('app')
        .factory('BarService', BarService);

    BarService.$inject = ['$http', '$rootScope'];
    function BarService($http, $rootScope) {
        var service = {};
        var url = $rootScope.url;

        service.GetAll = GetAll;
        service.GetById = GetById;
        service.Create = Create;
        service.Update = Update;
        service.Delete = Delete;

        return service;

        function GetAll() {
            return $http.get(url + '/bar').then(handleSuccess, handleError('Error getting all bars'));
        }

        function GetById(id) {
            return $http.get(url + '/bar', {params: {id: id}}).then(handleSuccess, handleError('Error getting bar by id'));
        }

        function Create(bar) {
            return $http.post(url + '/bar', bar).then(handleSuccess, handleError('Error creating bar'));
        }

        function Update(bar) {
            return $http.put(url + '/bar', bar).then(handleSuccess, handleError('Error updating bar'));
        }

        function Delete(id) {
            return $http.delete(url + '/bar', {params: {id: id}}).then(handleSuccess, handleError('Error deleting bar'));
        }

        // private functions

        function handleSuccess(res) {
            return res.data;
        }

        function handleError(error) {
            return function () {
                return { success: false, message: error };
            };
        }
    }

})();
