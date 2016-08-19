(function () {
    'use strict';

    angular
        .module('app')
        .controller('RegisterBannerController', RegisterBannerController);

    RegisterBannerController.$inject = ['BannerService', '$location', 'FlashService', '$scope'];
    function RegisterBannerController(BannerService, $location, FlashService, $scope) {
        var vm = this;
        var url = 'http://curiosamente-banners.s3.amazonaws.com/';

        vm.registerBanner = registerBanner;
        $scope.progress = 0;
        $scope.url = null;

        vm.banner = {};

        $scope.sizeLimit      = 10585760; // 10MB in Bytes
        $scope.uploadProgress = 0;

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

        $scope.creds = {
            bucket: 'curiosamente-banners',
            access_key: 'AKIAIRP72Z4G36I3BK2A',
            secret_key: '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'
        };

        $scope.upload = function() {
            // Configure The S3 Object
            AWS.config.update({ accessKeyId: $scope.creds.access_key, secretAccessKey: $scope.creds.secret_key });
            AWS.config.region = 'sa-east-1';
            var bucket = new AWS.S3({ params: { Bucket: $scope.creds.bucket } });

            $scope.file = document.getElementById('file').files[0];

            if($scope.file) {
                var params = { Key: $scope.file.name, ContentType: $scope.file.type, Body: $scope.file, ServerSideEncryption: 'AES256' };

                bucket.putObject(params, function(err, data) {
                    if(err) {
                        // There Was An Error With Your S3 Config
                        alert(err.message);
                        return false;
                    }
                    else {
                        // Success!
                        uploadComplete();
                    }
                })
                    .on('httpUploadProgress',function(progress) {
                        $scope.$apply(function () {
                            if (progress.lengthComputable) {
                                $scope.progress = Math.round(progress.loaded * 100 / progress.total);
                                $scope.$digest();
                            } else {
                                $scope.progress = 'unable to compute'
                            }
                        });
                    });
            }
            else {
                // No File Selected
                alert('Seleccione un archivo');
            }
        };

        function uploadComplete() {
            /* This event is raised when the server send back a response */
            $scope.$apply(function () {
                vm.banner.url = $scope.url = url + $scope.file.name;
            });
            (function initController() {
                document.getElementById("myBtn").click();
            })();

        }

    }

})();