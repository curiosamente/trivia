(function () {
    'use strict';

    angular
        .module('app')
        .controller('RegisterBannerController', RegisterBannerController);

    RegisterBannerController.$inject = ['BannerService','$location', 'FlashService', '$scope'];
    function RegisterBannerController(BannerService, $location, FlashService, $scope) {
        var vm = this;
        var url = 'https://quimera-web-admin-banners.s3.amazonaws.com/';
        var filePath;
        vm.uploadFile = uploadFile;
        vm.registerBanner = registerBanner;
        $scope.progress = 0;
        $scope.url = null;

        vm.banner = {};

        function registerBanner() {
            vm.dataLoading = true;
            BannerService.Create(vm.banner)
                .then(function (response) {
                    if (response === "") {
                        FlashService.Success('Banner registrado.', false);
                        $location.path('/banner');
                    } else {
                        FlashService.Error(response.message);
                        vm.dataLoading = false;
                    }
                });
        }

        function uploadFile() {

            var policy = 'eyJleHBpcmF0aW9uIjogIjIwMjAtMDEtMDFUMDA6MDA6MDBaIiwKICAiY29uZGl0aW9ucyI6IFsgCiAgICB7ImJ1Y2tldCI6ICJxdWltZXJhLXdlYi1hZG1pbi1iYW5uZXJzIn0sIAogICAgWyJzdGFydHMtd2l0aCIsICIka2V5IiwgInVwbG9hZHMvIl0sCiAgICB7ImFjbCI6ICJwdWJsaWMtcmVhZCJ9LAogICAgWyJzdGFydHMtd2l0aCIsICIkQ29udGVudC1UeXBlIiwgIiJdLAogICAgWyJjb250ZW50LWxlbmd0aC1yYW5nZSIsIDAsIDEwNDg1NzZdCiAgXQp9';
            var signature = 'ZuahwGSsbubTpKG3jYVOAEjTZc8=';

            var file = document.getElementById('file').files[0];
            var formData = new FormData();

            var key = "uploads/" + file.name;

            filePath = key;

            formData.append('key', key);
            formData.append('acl', 'public-read');
            formData.append('Content-Type', file.type);
            formData.append('AWSAccessKeyId', 'AKIAIU37DXC4MKK2BGOQ');
            formData.append('policy', policy);
            formData.append('signature', signature);

            formData.append("file", file);

            var xhr = new XMLHttpRequest();

            xhr.upload.addEventListener("progress", uploadProgress, false);
            xhr.addEventListener("load", uploadComplete, false);
            xhr.addEventListener("error", uploadFailed, false);
            xhr.addEventListener("abort", uploadCanceled, false);

            xhr.open('POST', url, true);

            xhr.send(formData);
        }

        function uploadProgress(evt) {

            $scope.$apply(function () {
                if (evt.lengthComputable) {
                    $scope.progress = Math.round(evt.loaded * 100 / evt.total)
                } else {
                    $scope.progress = 'unable to compute'
                }
            })

        }

        function uploadComplete(evt) {
            /* This event is raised when the server send back a response */
            $scope.$apply(function () {
                $scope.url = url + filePath;
                vm.banner.url = url + filePath;
            });
            (function initController() {
                document.getElementById("myBtn").click();
            })();

        }

        function uploadFailed(evt) {
            alert("There was an error attempting to upload the file." + evt);
        }

        function uploadCanceled(evt) {
            alert("The upload has been canceled by the user or the browser dropped the connection.");
        }

    }

})();