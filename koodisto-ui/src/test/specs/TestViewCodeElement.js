describe("Code Element View test", function() {

    var model, scope, mockBackend;

    var codesResponse = {
        "koodistoUri" : "versiointitesti",
        "resourceUri" : "http://koodistopalvelu.opintopolku.fi/versiointitesti",
        "omistaja" : null,
        "organisaatioOid" : "1.2.246.562.10.00000000001",
        "lukittu" : null,
        "latestKoodistoVersio" : {
            "versio" : 5,
            "paivitysPvm" : 1398346711637,
            "voimassaAlkuPvm" : "2014-04-24",
            "voimassaLoppuPvm" : null,
            "tila" : "HYVAKSYTTY",
            "version" : 2,
            "metadata" : [ {
                "kieli" : "FI",
                "nimi" : "Versiointitesti",
                "kuvaus" : "Testataan versiointia",
                "kayttoohje" : null,
                "kasite" : null,
                "kohdealue" : null,
                "sitovuustaso" : null,
                "kohdealueenOsaAlue" : null,
                "toimintaymparisto" : null,
                "tarkentaaKoodistoa" : null,
                "huomioitavaKoodisto" : null,
                "koodistonLahde" : null
            } ]
        }
    };

    beforeEach(module("koodisto", function($provide) {
        $provide.value('NoCacheInterceptor', {});
    }));

    beforeEach(inject(function($controller, $injector, $rootScope, $routeParams, ViewCodeElementModel) {
        scope = $rootScope.$new();
        model = ViewCodeElementModel;
        $routeParams.codeElementUri = "versiointitesti_uudi";
        $routeParams.codeElementVersion = 3;
        controller = $controller("ViewCodeElementController", {
            $scope : scope,
            ViewCodeElementModel : model
        });
        angular.mock.inject(function($injector) {
            mockBackend = $injector.get('$httpBackend');
        });
        mockBackend.whenGET(SERVICE_URL_BASE + "session/maxinactiveinterval").respond(1);
    }));

    it("ViewCodeElementModel is defined and it is in scope", function() {
        expect(model).toBeDefined();
        expect(scope.model).toEqual(model);
    });

    describe("Versioning", function() {

        givenResponseCodeElement = function(old) {
            mockBackend.expectGET(SERVICE_URL_BASE + "codes/versiointitesti").respond(codesResponse);
            return {
                "koodiUri" : "versiointitesti_uudi",
                "resourceUri" : "http://koodistopalvelu.opintopolku.fi/versiointitesti/koodi/versiointitesti_uudi",
                "version" : 1,
                "versio" : (old ? 2 : 3),
                "koodisto" : {
                    "koodistoUri" : "versiointitesti",
                    "organisaatioOid" : "1.2.246.562.10.00000000001",
                    "koodistoVersios" : [ (old ? 4 : 5) ]
                },
                "koodiArvo" : "uudi",
                "paivitysPvm" : 1398346711458,
                "voimassaAlkuPvm" : "2014-04-14",
                "voimassaLoppuPvm" : null,
                "tila" : "HYVAKSYTTY",
                "metadata" : [ {
                    "nimi" : "Uusinkoodi",
                    "kuvaus" : "fawegag",
                    "lyhytNimi" : "fweafaw",
                    "kayttoohje" : null,
                    "kasite" : null,
                    "sisaltaaMerkityksen" : null,
                    "eiSisallaMerkitysta" : null,
                    "huomioitavaKoodi" : null,
                    "sisaltaaKoodiston" : null,
                    "kieli" : "FI"
                } ],
                "withinCodeElements" : [],
                "includesCodeElements" : [],
                "levelsWithCodeElements" : []
            };
        };

        beforeEach(function() {
            // in order to get rid of controller's initialization
            mockBackend.expectGET(SERVICE_URL_BASE + "codeelement/versiointitesti_uudi/3").respond(givenResponseCodeElement());
            mockBackend.flush();
        });

        it("Editing old version of code element is prevented", function() {
            mockBackend.expectGET(SERVICE_URL_BASE + "codeelement/versiointitesti_uudi/2").respond(givenResponseCodeElement(true));
            scope.model.init(scope, "versiointitesti_uudi", 2);
            mockBackend.flush();
            expect(scope.model.editState).toEqual("disabled");
        });

        it("Editing latest version of code element is permitted", function() {
            expect(scope.model.editState).toEqual("");
        });
    });

    describe("Relations", function() {

        givenResponseCodeElementWithRelation = function() {
            mockBackend.expectGET(SERVICE_URL_BASE + "codes/versiointitesti").respond(codesResponse);
            return {
                "koodiUri" : "versiointitesti_uudi",
                "resourceUri" : "http://koodistopalvelu.opintopolku.fi/versiointitesti/koodi/versiointitesti_uudi",
                "version" : 1,
                "versio" : 3,
                "koodisto" : {
                    "koodistoUri" : "versiointitesti",
                    "organisaatioOid" : "1.2.246.562.10.00000000001",
                    "koodistoVersios" : [ 3 ]
                },
                "koodiArvo" : "uudi",
                "paivitysPvm" : 1398346711458,
                "voimassaAlkuPvm" : "2014-04-14",
                "voimassaLoppuPvm" : null,
                "tila" : "HYVAKSYTTY",
                "metadata" : [ {
                    "nimi" : "Uusinkoodi",
                    "kuvaus" : "fawegag",
                    "lyhytNimi" : "fweafaw",
                    "kayttoohje" : null,
                    "kasite" : null,
                    "sisaltaaMerkityksen" : null,
                    "eiSisallaMerkitysta" : null,
                    "huomioitavaKoodi" : null,
                    "sisaltaaKoodiston" : null,
                    "kieli" : "FI"
                } ],
                "withinCodeElements" : [ {
                    "codeElementUri" : "hep2_2",
                    "codeElementVersion" : 2,
                    "relationMetadata" : [ {
                      "nimi" : "2",
                      "kuvaus" : "2",
                      "lyhytNimi" : "2",
                      "kayttoohje" : null,
                      "kasite" : null,
                      "sisaltaaMerkityksen" : null,
                      "eiSisallaMerkitysta" : null,
                      "huomioitavaKoodi" : null,
                      "sisaltaaKoodiston" : null,
                      "kieli" : "FI"
                    } ],
                    "parentMetadata" : [ {
                      "kieli" : "FI",
                      "nimi" : "hep2",
                      "kuvaus" : "hep2",
                      "kayttoohje" : null,
                      "kasite" : null,
                      "kohdealue" : null,
                      "sitovuustaso" : null,
                      "kohdealueenOsaAlue" : null,
                      "toimintaymparisto" : null,
                      "tarkentaaKoodistoa" : null,
                      "huomioitavaKoodisto" : null,
                      "koodistonLahde" : null
                    } ]
                  } ],
                "includesCodeElements" : [],
                "levelsWithCodeElements" : []
            };
        };

        beforeEach(function() {
            mockBackend.expectGET(SERVICE_URL_BASE + "codeelement/versiointitesti_uudi/3").respond(givenResponseCodeElementWithRelation());
            mockBackend.flush();
        });

        it("Should contain version number of code element relation references", function() {
            expect(model.withinCodeElements.length).toEqual(1);
            expect(model.withinCodeElements[0].versio).toEqual(2);
            expect(model.withinCodeElements[0].name).toEqual("2");
        });
    });

    describe("Caching", function() {

        givenResponseCodeElementCaching = function() {
            mockBackend.expectGET(SERVICE_URL_BASE + "codes/versiointitesti").respond(codesResponse);
            return {
                "koodiUri" : "versiointitesti_uudi",
                "resourceUri" : "http://koodistopalvelu.opintopolku.fi/versiointitesti/koodi/versiointitesti_uudi",
                "version" : 1,
                "versio" : 3,
                "koodisto" : {
                    "koodistoUri" : "versiointitesti",
                    "organisaatioOid" : "1.2.246.562.10.00000000001",
                    "koodistoVersios" : [ 5 ]
                },
                "koodiArvo" : "uudi",
                "paivitysPvm" : 1398346711458,
                "voimassaAlkuPvm" : "2014-04-14",
                "voimassaLoppuPvm" : null,
                "tila" : "HYVAKSYTTY",
                "metadata" : [ {
                    "nimi" : "Uusinkoodi",
                    "kuvaus" : "fawegag",
                    "lyhytNimi" : "fweafaw",
                    "kayttoohje" : null,
                    "kasite" : null,
                    "sisaltaaMerkityksen" : null,
                    "eiSisallaMerkitysta" : null,
                    "huomioitavaKoodi" : null,
                    "sisaltaaKoodiston" : null,
                    "kieli" : "FI"
                } ],
                "withinCodeElements" : [],
                "includesCodeElements" : [],
                "levelsWithCodeElements" : []
            };
        };

        beforeEach(function() {
            // in order to get rid of controller's initialization
            mockBackend.expectGET(SERVICE_URL_BASE + "codeelement/versiointitesti_uudi/3").respond(givenResponseCodeElementCaching());
            mockBackend.flush();
        });

        it("Calling subsequent inits should make no calls to backend", function() {
            scope.model.init(scope, "versiointitesti_uudi", 3);
            scope.model.init(scope, "versiointitesti_uudi", 3);
            scope.model.init(scope, "versiointitesti_uudi", 3);
        });

        it("Calling init after forcerefresh should load eveything", function() {
            scope.model.init(scope, "versiointitesti_uudi", 3);

            model.forceRefresh = true; // simulates clicking the edit link
            mockBackend.expectGET(SERVICE_URL_BASE + "codeelement/versiointitesti_uudi/3").respond(givenResponseCodeElementCaching());
            scope.model.init(scope, "versiointitesti_uudi", 3);
            mockBackend.flush();
        });

    });

});