(function () {
    'use strict';

    angular
        .module('app')
        .factory('CategoryService', CategoryService);

    CategoryService.$inject = ['$http', '$rootScope'];
    function CategoryService($http, $rootScope) {
        var service = {};
        var url = $rootScope.url;
        service.GetAll = GetAll;
        service.GetById = GetById;
        service.Create = Create;
        service.Update = Update;
        service.Delete = Delete;

        return service;

        function GetAll() {
            return $http.get(url + '/category').then(handleSuccess, handleError('Error getting all categories'));
        }

        function GetById(id) {
            return $http.get(url + '/category', {params: {id: id}}).then(handleSuccess, handleError('Error getting category by id'));
        }

        function Create(category) {
            return $http.post(url + '/category', category).then(handleSuccess, handleError('Error creating category'));
        }

        function Update(category) {
            return $http.put(url + '/category', category).then(handleSuccess, handleError('Error updating category'));
        }

        function Delete(id) {
            return $http.delete(url + '/category', {params: {id: id}}).then(handleSuccess, handleError('Error deleting category'));
        }

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
