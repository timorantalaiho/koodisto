"use strict";

var app = angular.module('koodisto', ['ngResource', 'loading', 'ngRoute', 'ngAnimate', 'localization','ui.bootstrap']);
//
// i18n toteutus kopioitu osittain http://jsfiddle.net/4tRBY/41/
//


angular.module('localization', [])
    .filter('i18n', ['$rootScope', '$locale', function ($rootScope, $locale) {
        var localeMapping = {"en-us": "en_US", "fi-fi": "fi_FI", "sv-se": "sv-SE"};

        jQuery.i18n.properties({
            name: 'messages',
            path: '../i18n/',
            mode: 'map',
            language: localeMapping[$locale.id],
            callback: function () {
            }
        });

        return function (text) {
            return jQuery.i18n.prop(text);
        };
    }]);


var SERVICE_URL_BASE = KOODISTO_URL_BASE;
var ORGANIZATION_SERVICE_URL_BASE = ORGANIZATION_SERVICE_URL_BASE || "/organisaatio-service/";
var TEMPLATE_URL_BASE = TEMPLATE_URL_BASE || "";
var CAS_URL = CAS_URL || "/cas/myroles";

//Route configuration
app.config(function ($routeProvider) {
    $routeProvider.
        //front page
        when('/etusivu', {controller: KoodistoTreeController, templateUrl: TEMPLATE_URL_BASE + 'codesmainpage.html'}).
        when('/lisaaKoodisto', {controller: CodesCreatorController, templateUrl:TEMPLATE_URL_BASE + 'createcodes.html'}).
        when('/muokkaaKoodisto/:codesUri', {controller: CodesEditorController, templateUrl:TEMPLATE_URL_BASE + 'editcodes.html'}).
        when('/koodisto/:codesUri/:codesVersion', {controller: ViewCodesController, templateUrl:TEMPLATE_URL_BASE + 'viewcodes.html'}).
        when('/koodi/:codeElementUri/:codeElementVersion', {controller: ViewCodeElementController, templateUrl:TEMPLATE_URL_BASE + 'viewcodeelement.html'}).
        when('/lisaaKoodi/:codesUri/:codesVersion', {controller: CodeElementCreatorController, templateUrl:TEMPLATE_URL_BASE + 'createcodeelement.html'}).
        when('/muokkaaKoodi/:codeElementUri/:codeElementVersion', {controller: CodeElementEditorController, templateUrl:TEMPLATE_URL_BASE + 'editcodeelement.html'}).
        //else
        otherwise({redirectTo: '/etusivu'});
});


//rest resources


//Koodistot
app.factory('RootCodes', function ($resource) {
    return $resource(SERVICE_URL_BASE + "codes", {}, {
        get: {method: "GET", isArray: true,
            params: {
            }
        }
    });
});

app.factory('NewCodes', function($resource) {
    return $resource(SERVICE_URL_BASE + "codes", {}, {
        put: {method: "PUT"}
    });
});

app.factory('UpdateCodes', function($resource) {
    return $resource(SERVICE_URL_BASE + "codes", {}, {
        put: {method: "POST"}
    });
});

app.factory('CodesByUri', function($resource) {
    return $resource(SERVICE_URL_BASE + "codes/:codesUri", {codesUri: "@codesUri"}, {
        get: {method: "GET"}
    });
});

app.factory('CodesByUriAndVersion', function($resource) {
    return $resource(SERVICE_URL_BASE + "codes/:codesUri/:codesVersion", {codesUri: "@codesUri",codesVersion: "@codesVersion"}, {
        get: {method: "GET"}
    });
});

app.factory('AllCodes', function ($resource) {
    return $resource(SERVICE_URL_BASE + "codes/all", {}, {
        get: {method: "GET", isArray: true,
            params: {
            }
        }
    });
});


app.factory('MyRoles', function($resource) {
    return $resource(CAS_URL, {}, {
        get: {method: "GET", isArray: true}
    });
});


app.factory('CodeElementsByCodesUriAndVersion', function($resource) {
    return $resource(SERVICE_URL_BASE + "codeelement/codes/:codesUri/:codesVersion", {codesUri: "@codesUri", codesVersion: "@codesVersion"}, {
        get: {method: "GET", isArray: true,
            params: {
            }}
    });
});

app.factory('CodeElementByUriAndVersion', function($resource) {
    return $resource(SERVICE_URL_BASE + "codeelement/:codeElementUri/:codeElementVersion", {codeElementUri: "@codeElementUri", codeElementVersion: "@codeElementVersion"}, {
        get: {method: "GET", isArray: false,
            params: {
            }}
    });
});

app.factory('CodeElementByCodeElementUri', function($resource) {
    return $resource(SERVICE_URL_BASE + "codeelement/:codesUri/:codesVersion/:codeElementUri", {codesUri: "@codesUri", codesVersion: "@codesVersion", codeElementUri: "@codeElementUri"}, {
        get: {method: "GET", isArray: false}
    });
});

app.factory('CodeElementVersionsByCodeElementUri', function($resource) {
    return $resource(SERVICE_URL_BASE + "codeelement/:codeElementUri", {codeElementUri: "@codeElementUri"}, {
        get: {method: "GET", isArray: true}
    });
});

app.factory('LatestCodeElementVersionsByCodeElementUri', function($resource) {
    return $resource(SERVICE_URL_BASE + "codeelement/latest/:codeElementUri", {codeElementUri: "@codeElementUri"}, {
        get: {method: "GET", isArray: false}
    });
});

app.factory('NewCodeElement', function($resource) {
    return $resource(SERVICE_URL_BASE + "codeelement/:codesUri", {}, {
        put: {method: "PUT"}
    });
});

app.factory('DeleteCodeElement', function($resource) {
    return $resource(SERVICE_URL_BASE + "codeelement/delete/:codeElementUri/:codeElementVersion",
        {codeElementUri: "@codeElementUri",codeElementVersion: "@codeElementVersion"}, {
            put: {method: "POST"}
        });
});

app.factory('RemoveRelationCodeElement', function($resource) {
    return $resource(SERVICE_URL_BASE + "codeelement/removerelation/:codeElementUri/:codeElementUriToRemove/:relationType",
        {codeElementUri: "@codeElementUri",codeElementUriToRemove: "@codeElementUriToRemove",relationType: "@relationType"}, {
        put: {method: "POST"}
    });
});

app.factory('AddRelationCodeElement', function($resource) {
    return $resource(SERVICE_URL_BASE + "codeelement/addrelation/:codeElementUri/:codeElementUriToAdd/:relationType",
        {codeElementUri: "@codeElementUri",codeElementUriToAdd: "@codeElementUriToAdd",relationType: "@relationType"}, {
            put: {method: "POST"}
        });
});

app.factory('UpdateCodeElement', function($resource) {
    return $resource(SERVICE_URL_BASE + "codeelement", {}, {
        put: {method: "POST"}
    });
});

app.factory('Organizations', function ($resource) {
    return $resource(ORGANIZATION_SERVICE_URL_BASE + "rest/organisaatio/hae", {}, {
        get: {method: "GET"}
    });
});

app.factory('OrganizationByOid', function ($resource) {
    return $resource(ORGANIZATION_SERVICE_URL_BASE + "rest/organisaatio/:oid", {oid: "@oid"}, {
        get: {method: "GET"}
    });
});

function getLanguageSpecificValue(fieldArray,fieldName,language) {
    var returnStr = "";
    if (fieldArray) {
        for (var i = 0; i < fieldArray.length; i++) {
            if (fieldArray[i].kieli === language) {
                var fieldName = "fieldArray[i]."+fieldName;
                return eval(fieldName);
            }
        }
    }
    return returnStr;
}

