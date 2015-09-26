INSERT INTO "ORGANIZATION"
	("ID", "NAME")
VALUES
	(1, 'Organization');

INSERT INTO "DIVISION" 
	("ID", "NAME", "ORGANIZATION")
VALUES
	(1, 'Division', 1);	
	
INSERT INTO "USER" 
	("ID", "FIRSTNAME", "LASTNAME", "PASSWORD", "DIVISION") 
VALUES 
	('test1', 'Test', 'User1', 'blub', 1);
	
INSERT INTO "USER" 
	("ID", "FIRSTNAME", "LASTNAME", "PASSWORD", "DIVISION") 
VALUES 
	('test2', 'Test', 'User2', 'blub', 1);
	
INSERT INTO "ESTIMATE" 
	("ID", "NAME", "CREATIONDATE", "MODIFYDATE", "ESTIMATOR", "LASTEDITOR", "DIVISION") 
VALUES 
	(1, 'Estimate1', '2011-01-01 00:00:00', '2011-01-01 00:00:01', 'test1', 'test1', 1);
	
INSERT INTO "ESTIMATE" 
	("ID", "NAME", "CREATIONDATE", "MODIFYDATE", "ESTIMATOR", "LASTEDITOR", "DIVISION") 
VALUES 
	(2, 'Estimate2', '2011-01-01 00:00:00', '2011-01-01 00:00:01', 'test2', 'test2', 1);
	
INSERT INTO "SOLUTION"
	("ID","NAME", "ESTIMATE", "PI", "GRANULARITY", "STARTDATE", "CREATIONDATE", "MODIFYDATE", "LASTEDITOR", "GEARINGFACTOR")
VALUES
	(1,'Solution1', 1, 5, 1, '2011-01-01 00:00:00', '2011-01-01 00:00:00', '2011-01-01 00:00:01', 'test1',125);
	
INSERT INTO "SOLUTION"
	("ID","NAME", "ESTIMATE", "PI", "GRANULARITY", "STARTDATE", "CREATIONDATE", "MODIFYDATE", "LASTEDITOR", "GEARINGFACTOR")
VALUES
	(2,'Solution2', 1, 5, 1, '2011-01-01 00:00:00', '2011-01-01 00:00:00', '2011-01-01 00:00:02', 'test1',125);
	
INSERT INTO "SOLUTION"
	("ID","NAME", "ESTIMATE", "PI", "GRANULARITY", "PROJECTSTARTDATE", "CREATIONDATE", "MODIFYDATE", "LASTEDITOR", "GEARINGFACTOR")
VALUES
	(3,'Solution3', 1, 5, 1, '2011-01-01 00:00:00', '2011-01-01 00:00:00', '2011-01-01 00:00:03', 'test1',125);
	
INSERT INTO "SOLUTION"
	("ID","NAME", "ESTIMATE", "PI", "GRANULARITY", "PROJECTSTARTDATE", "CREATIONDATE", "MODIFYDATE", "LASTEDITOR", "GEARINGFACTOR")
VALUES
	(4, 'Solution4',2, 5, 1, '2011-01-01 00:00:00', '2011-01-01 00:00:00', '2011-01-01 00:00:04', 'test1',125);

ALTER TABLE SOLUTION ALTER COLUMN id RESTART WITH 5;
ALTER TABLE ORGANIZATION ALTER COLUMN id RESTART WITH 2;
ALTER TABLE DIVISION ALTER COLUMN id RESTART WITH 2;
ALTER TABLE ESTIMATE ALTER COLUMN id RESTART WITH 3;
