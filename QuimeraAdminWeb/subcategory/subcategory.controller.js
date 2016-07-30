(function () {
    'use strict';

    angular
        .module('app')
        .controller('SubcategoryController', SubcategoryController);

    SubcategoryController.$inject = ['SubcategoryService', '$location', 'FlashService'];
    function SubcategoryController(SubcategoryService, $location, FlashService) {

        var vm = this;
        vm.allSubcategory = [];
        vm.deleteSubcategory = deleteSubcategory;
       
        initController();

        function initController() {
            loadAllSubcategories();
        }

        function loadAllSubcategories() {
            SubcategoryService.GetAll()
                .then(function (subcategories) {
                    vm.allSubcategories = subcategories;
                });
        }

        function deleteSubcategory(subcategory) {
            SubcategoryService.Delete(subcategory)
                .then(function (response) {
                    if (response==="") {
                        FlashService.Success('Subcategoría eliminada.', true);
                        loadAllSubcategories();
                    } else {
                        FlashService.Error(response.message);
                        vm.dataLoading = false;
                    }
                    $location.path('/subcategory');
                });
        }

    }

})();