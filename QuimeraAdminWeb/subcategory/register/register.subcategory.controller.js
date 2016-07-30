(function () {
    'use strict';

    angular
        .module('app')
        .controller('SubcategoryRegisterController', SubcategoryRegisterController);

    SubcategoryRegisterController.$inject = ['SubcategoryService', 'CategoryService', '$location', 'FlashService', '$scope'];
    function SubcategoryRegisterController(SubcategoryService, CategoryService, $location, FlashService, $scope) {
        var vm = this;
        vm.subcategory = null;
        vm.allCategories = [];
        vm.allSubcategories = [];
        vm.categorySelected = null;
        vm.subCategorySelected = null;
        vm.registerSubcategory = registerSubcategory;
        vm.loadAllCategories = loadAllCategories;

        initController();

        function initController() {
            loadAllCategories();
        }

        function registerSubcategory() {
            vm.dataLoading = true;

            vm.subcategory.category = vm.categorySelected;

            SubcategoryService.Create(vm.subcategory)
                .then(function (response) {
                    if (response === "") {
                        FlashService.Success('Subcategoría registrada.', false);
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
                    vm.categorySelected = categories[0];
                });
        }


    }

})();