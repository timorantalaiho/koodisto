"use strict";
app.factory('OrganisaatioTreeModel', function(OrganizationChildrenByOid) {

    return (function() {
        var instance = {};
        instance.model = {};
        instance.searchStr = "";

        instance.init = function(organizations) {                       
            instance.model = {};
            instance.model.organisaatiot = [];
            instance.model.numHits = 0;
            
            organizations.forEach(function(organization) {
        	OrganizationChildrenByOid.get({oid: organization}, function(result) { 
        	    result.organisaatiot.forEach(function(org) {
        		instance.model.organisaatiot.push(org);
        	    });
        	    instance.model.numHits += result.numHits;
        	});
            });
        };
        
        instance.resetSearch = function() {
            if (instance.model.originalOrganizations){
        	instance.model.organisaatiot =  instance.model.originalOrganizations;
            }
        }

        instance.search = function(searchStr) {
            
            var matchingOrgs = new Array();
            
            function matchesSearch(organization) {
        	function matchesTranslation(organization, language) {
        	    return organization.nimi[language] && organization.nimi[language].toLowerCase().indexOf(searchStr) > -1;
        	}
        	return matchesTranslation(organization, 'fi') || matchesTranslation(organization, 'sv') || matchesTranslation(organization, 'en');
            }
            
            function recursivelyAddMatchingOrganizations(organization) { 
        	if (matchesSearch(organization)) {
        	    matchingOrgs.push(organization);
        	} else {
        	    organization.children.forEach(function(child) {        	    
        		recursivelyAddMatchingOrganizations(child);
        	    });
        	}
            }
            
            searchStr = searchStr.toLowerCase();

            if (!instance.model.originalOrganizations) {
        	instance.model.originalOrganizations = instance.model.organisaatiot;
            }

            instance.model.originalOrganizations.forEach(function(organization) {
        	recursivelyAddMatchingOrganizations(organization);
            });

            instance.model.organisaatiot = matchingOrgs;
            instance.model.numHits = matchingOrgs.length;
            
            if(instance.model.organisaatiot.length < 4) {
        	instance.model.organisaatiot.forEach(function(data){
        	    instance.openChildren(data);
        	});
            }
        }

        instance.openChildren = function(data) {
            data.open = !data.open;
            if(data.open) {

                var iter = function(children){
                    if(children) {
                        children.forEach(function(child){
                            child.open = true;
                            iter(child.children);
                        });
                    }
                }

                iter(data.children);
            }
        };

        return instance;
    })();

});

app.factory('OrganisaatioOPHTreeModel', function(Organizations, OrganizationByOid) {

    return (function() {
        var instance = {};
        instance.model = {};
        instance.searchStr = "";

        instance.init = function() {                       
            instance.model = {};
            instance.model.organisaatiot = [];
            instance.model.numHits = 0;
            
            OrganizationByOid.get({oid: OPH_ORG}, function(result) {
        	instance.model.organisaatiot.push(result);
        	instance.model.numHits += 1;
            });
        };
        
        instance.resetSearch = function() {
            OrganizationByOid.get({oid: OPH_ORG}, function(result) {
        	instance.model.organisaatiot = [result];
        	instance.model.numHits = 1;
            });
        }

        instance.search = function(searchStr) {            
            Organizations.get({"searchStr": searchStr}, function(result){
        	instance.model = result;
        	if(instance.model.organisaatiot.length < 4) {
        	    instance.model.organisaatiot.forEach(function(data){
        		instance.openChildren(data);
        	    });
        	}
            });            
        }

        instance.openChildren = function(data) {
            data.open = !data.open;
            if(data.open) {

                var iter = function(children){
                    if(children) {
                        children.forEach(function(child){
                            child.open = true;
                            iter(child.children);
                        });
                    }
                }

                iter(data.children);
            }
        };

        return instance;
    })();

});

function OrganisaatioTreeController($scope, AuthService, OrganisaatioTreeModel, OrganisaatioOPHTreeModel) {
    if (!$scope.orgTree) {
	AuthService.updateOph(SERVICE_NAME).then(function() {
	    $scope.orgTree = OrganisaatioOPHTreeModel;
	}, function() {
	    $scope.orgTree = OrganisaatioTreeModel;
	});
	
	AuthService.getOrganizations(SERVICE_NAME).then(function(organizations) {
	    $scope.orgTree.init(organizations);
	});
	
	$scope.$watch('orgTree.searchStr', function() {
	        if($scope.orgTree.searchStr.length > 2) {
	            $scope.orgTree.search($scope.orgTree.searchStr);
	        } else if ($scope.orgTree.searchStr.length < 1){
	            $scope.orgTree.resetSearch();
	        }            
	});
    }
    

    function debounce(fn, delay) {
        var timer = null;
        return function () {
            var context = this, args = arguments;
            clearTimeout(timer);
            timer = setTimeout(function () {
                fn.apply(context, args);
            }, delay);
        };
    }

    $scope.openChildren = function(data) {
	$scope.orgTree.openChildren(data);
    }

    $scope.clear = function(){
	$scope.orgTree.searchStr = '';
	$scope.orgTree.resetSearch();
    }


}


var ModalInstanceCtrl = function ($scope, $modalInstance) {

    $scope.cancel = function () {
        $modalInstance.dismiss('cancel');
    };


    $scope.organisaatioSelector = function(data) {
        $modalInstance.close(data);
    };
};