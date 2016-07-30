(function () {
    'use strict';

    angular
        .module('app')
        .controller('ModifySubcategoryController', ModifySubcategoryController);

    ModifySubcategoryController.$inject = ['SubcategoryService', 'CategoryService', '$location', '$routeParams', 'FlashService', '$scope'];
    function ModifySubcategoryController(SubcategoryService, CategoryService, $location, $routeParams, FlashService, $scope) {
        var vm = this;
        vm.subcategory = null;
        vm.allCategories = [];
        vm.subCategorySelected = null;
        vm.modifySubcategory = modifySubcategory;

        initController();

        function initController() {
            loadCurrentSubcategory();
            loadAllCategories();
        }

        function loadCurrentSubcategory() {
            SubcategoryService.GetById($routeParams.id)
                .then(function (subcategory) {
                    vm.subcategory = subcategory;
                    vm.categorySelected = vm.subcategory.category;
                });
        }

        function modifySubcategory() {
            vm.dataLoading = true;
            SubcategoryService.Update(vm.subcategory)
                .then(function (response) {
                    if (response === "") {
                        FlashService.Success('Subcategoría modificada.', false);
                        $location.path('/subcategory');
                    } else {
                        FlashService.Error(response.message);
                        vm.dataLoading = false;
                    }
                });
        }
        function loadAllCategories() {
            CategoryService.GetAll()
                .then(function (categories) {
                    vm.allCategories = categories;
                });
        }
    }

})();