-- INSERT USE CASE COMPLEXITIES --

INSERT INTO UseCaseComplexity (id, name, factor) VALUES ('H', 'High', 10.08);
INSERT INTO UseCaseComplexity (id, name, factor) VALUES ('HR', 'High Reuse', 3.6);
INSERT INTO UseCaseComplexity (id, name, factor) VALUES ('L', 'Low', 2.88);
INSERT INTO UseCaseComplexity (id, name, factor) VALUES ('LR', 'Low Reuse', 0.72);
INSERT INTO UseCaseComplexity (id, name, factor) VALUES ('M', 'Medium', 5.76);
INSERT INTO UseCaseComplexity (id, name, factor) VALUES ('MR', 'Medium Reuse', 2.16);
INSERT INTO UseCaseComplexity (id, name, factor) VALUES ('VL', 'Very Low', 0.576);
INSERT INTO UseCaseComplexity (id, name, factor) VALUES ('VLR', 'Very Low Reuse', 0.144);


-- INSERT CERTAINTIES --

INSERT INTO Certainty (id, name, factor) VALUES ('H', 'High', 1);
INSERT INTO Certainty (id, name, factor) VALUES ('L', 'Low', 0.4);
INSERT INTO Certainty (id, name, factor) VALUES ('M', 'Medium', 0.8);


-- INSERT GRANULARITY LEVELS --

INSERT INTO GranularityLevel (id, name, "VALUE") VALUES (1, 'Decomposed', 1);
INSERT INTO GranularityLevel (id, name, "VALUE") VALUES (2, 'Medium', 2);
INSERT INTO GranularityLevel (id, name, "VALUE") VALUES (3, 'Essential', 4);



-- INSERT GRANULARITY QUESTIONS --

INSERT INTO GranularityQuestion (level, factorName, factor, question) VALUES (1, 'basic_flows', 0.2, 'All use case''s basic flows complete a major interaction or transaction with the system.');
INSERT INTO GranularityQuestion (level, factorName, factor, question) VALUES (2, 'basic_flows', 0.2, 'Some use case''s basic flows complete a major interaction or transaction with the system; most interactions are broken up over more than one use case.');
INSERT INTO GranularityQuestion (level, factorName, factor, question) VALUES (3, 'basic_flows', 0.2, 'No use case''s basic flows complete a major interaction or transaction, but instead describe functions such as Search for a Client.');
INSERT INTO GranularityQuestion (level, factorName, factor, question) VALUES (1, 'arch_express', 0.1, 'Use cases are not associated with system or architectural components.');
INSERT INTO GranularityQuestion (level, factorName, factor, question) VALUES (2, 'arch_express', 0.1, 'Use case occasionally span system or architectural components.');
INSERT INTO GranularityQuestion (level, factorName, factor, question) VALUES (3, 'arch_express', 0.1, 'Use cases are completely locked to specific architectural or system components.');
INSERT INTO GranularityQuestion (level, factorName, factor, question) VALUES (1, 'actor_gen', 0.05, 'Actors in the use case model have been well generalized.');
INSERT INTO GranularityQuestion (level, factorName, factor, question) VALUES (2, 'actor_gen', 0.05, 'Actors in the use case model are mostly generalized, but some concrete users may remain.');
INSERT INTO GranularityQuestion (level, factorName, factor, question) VALUES (3, 'actor_gen', 0.05, 'Use cases are tied to specific concrete users, not abstract actors.');
INSERT INTO GranularityQuestion (level, factorName, factor, question) VALUES (1, 'indep_ui', 0.1, 'Use cases are completely independent of user interface implementations.');
INSERT INTO GranularityQuestion (level, factorName, factor, question) VALUES (2, 'indep_ui', 0.1, 'Use cases assume specific user interface paradigms or implementations.');
INSERT INTO GranularityQuestion (level, factorName, factor, question) VALUES (3, 'indep_ui', 0.1, 'Use cases are tied to specific screens or users functions, and contain specific UI design statements.');
INSERT INTO GranularityQuestion (level, factorName, factor, question) VALUES (1, 'ext_excl', 0.05, 'Use of extends and includes statements is 20% or less of the use case model.');
INSERT INTO GranularityQuestion (level, factorName, factor, question) VALUES (2, 'ext_excl', 0.05, '20-50% of the use case model are extensions or inclusions.');
INSERT INTO GranularityQuestion (level, factorName, factor, question) VALUES (3, 'ext_excl', 0.05, 'More than half of the use cases are extensions or inclusions.');
INSERT INTO GranularityQuestion (level, factorName, factor, question) VALUES (1, 'altern_flows', 0.1, 'Use cases contain many alternate flows.');
INSERT INTO GranularityQuestion (level, factorName, factor, question) VALUES (2, 'altern_flows', 0.1, 'Use cases contain few alternate flows.');
INSERT INTO GranularityQuestion (level, factorName, factor, question) VALUES (3, 'altern_flows', 0.1, 'Use cases usually contain 0-2 alternate flows.');
INSERT INTO GranularityQuestion (level, factorName, factor, question) VALUES (1, 'crud', 0.1, 'CRUD functionality is a portion of an use case.');
INSERT INTO GranularityQuestion (level, factorName, factor, question) VALUES (2, 'crud', 0.1, 'CRUD functionality is the totality of an use case, and sometimes two use cases.');
INSERT INTO GranularityQuestion (level, factorName, factor, question) VALUES (3, 'crud', 0.1, 'Each element of CRUD is dealt with in a separate use case.');
INSERT INTO GranularityQuestion (level, factorName, factor, question) VALUES (1, 'scenarios', 0.2, 'Use cases represent many concrete examples of interaction, e.g. scenarios.');
INSERT INTO GranularityQuestion (level, factorName, factor, question) VALUES (2, 'scenarios', 0.2, 'Use cases represent few scenarios or multiple use cases must be invoked to make a single scenario.');
INSERT INTO GranularityQuestion (level, factorName, factor, question) VALUES (3, 'scenarios', 0.2, 'Scenarios almost always have to invoke several use cases to complete a useful example.');
INSERT INTO GranularityQuestion (level, factorName, factor, question) VALUES (1, 'testing_appl', 0.1, 'Use cases are useful for usability and system testing.');
INSERT INTO GranularityQuestion (level, factorName, factor, question) VALUES (2, 'testing_appl', 0.1, 'Use cases are useful for system and unit testing.');
INSERT INTO GranularityQuestion (level, factorName, factor, question) VALUES (3, 'testing_appl', 0.1, 'Use cases are useful for unit testing.');

