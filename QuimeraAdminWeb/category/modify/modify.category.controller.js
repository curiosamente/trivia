(function () {
    'use strict';

    angular
        .module('app')
        .controller('ModifyCategoryController', ModifyCategoryController);

    ModifyCategoryController.$inject = ['CategoryService', '$location', '$routeParams', 'FlashService', '$scope'];
    function ModifyCategoryController(CategoryService, $location, $routeParams, FlashService, $scope) {
        var vm = this;
        vm.category = null;

        vm.modifyCategory = modifyCategory;

        initController();

        function initController() {
            loadCurrentCategory();
        }

        function loadCurrentCategory() {
            CategoryService.GetById($routeParams.id)
                .then(function (category) {
                    vm.category = category;
                });
        }

        function modifyCategory() {
            vm.dataLoading = true;
            CategoryService.Update(vm.category)
                .then(function (response) {
                    if (response === "") {
                        FlashService.Success('Categoría modificada.', false);
                        $location.path('/category');
                    } else {
                        FlashService.Error(response.message);
                        vm.dataLoading = false;
                    }
                });
        }

        //$scope.addItem = function (item) {
        //    $scope.vm.category.subCategories.push(item);
        //    $scope.newItem = null;
        //}

    }


})();