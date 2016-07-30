(function () {
    'use strict';

    angular
        .module('app')
        .controller('CategoryController', CategoryController);

    CategoryController.$inject = ['CategoryService', '$location', 'FlashService'];
    function CategoryController(CategoryService, $location, FlashService) {

        var vm = this;

        vm.allCategory = [];
        vm.deleteCategory = deleteCategory;
        //vm.subCategories = subCategories;

        initController();

        function initController() {
            loadAllCategories();
        }

        function loadAllCategories() {
            CategoryService.GetAll()
                .then(function (categories) {
                    vm.allCategories = categories;
                });
        }

        function deleteCategory(category) {
            CategoryService.Delete(category)
                .then(function (response) {
                    if (response==="") {
                        FlashService.Success('Categoría eliminada.', true);
                        loadAllCategories();
                    } else {
                        FlashService.Error(response.message);
                        vm.dataLoading = false;
                    }
                    $location.path('/category');
                });
        }

        //function subCategories(subCategories) {
        //    var subCategoriesStrings = "";
        //    subCategories.forEach(function (subCategory) {
        //        subCategoriesStrings += subCategory.name + '  ';
        //    });
        //    return subCategoriesStrings;
        //}

    }

})();