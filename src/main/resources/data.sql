REPLACE INTO role (role_id, role) VALUES (1, 'ADMIN');
REPLACE INTO role (role_id, role) VALUES (2, 'USER');

--INSERT INTO user (user_id, active, email, first_name, last_name, password) VALUES (1, 1, 'ithinkisam@gmail.com', 'Sam', 'Butler', '$2a$10$Ahl5DP1VseUvPywlpWa5delXOQf8YOSndTQem1kHFp.jdGwbeOq9C') where not exists (select email from user where email = 'ithinkisam@gmail.com');
--REPLACE INTO user_role (user_id, role_id) VALUES (1, 1);
--REPLACE INTO user_role (user_id, role_id) VALUES (1, 2);
