DELETE FROM STUDENT_DETAILS;
DELETE FROM STUDENT_ACCOUNT;
INSERT INTO STUDENT_ACCOUNT VALUES(1,'new@example.com','password',1);
INSERT INTO STUDENT_ACCOUNT VALUES(2,'student@example.com','password',10);
INSERT INTO STUDENT_ACCOUNT VALUES(3,'premium@example.com','password',50);
INSERT INTO STUDENT_ACCOUNT VALUES(4,'moderator@example.com','password',100);
INSERT INTO STUDENT_ACCOUNT VALUES(5,'admin@example.com','password',200);
SELECT * FROM STUDENT_ACCOUNT;