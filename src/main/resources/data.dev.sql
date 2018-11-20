REPLACE INTO role (role_id, role) VALUES (1, 'ADMIN');
REPLACE INTO role (role_id, role) VALUES (2, 'USER');

REPLACE INTO user (user_id, active, email, first_name, last_name, password) VALUES (1, 1, 'ithinkisam@gmail.com', 'Sam', 'Butler', '$2a$10$Ahl5DP1VseUvPywlpWa5delXOQf8YOSndTQem1kHFp.jdGwbeOq9C');
REPLACE INTO user_role (user_id, role_id) VALUES (1, 1);
REPLACE INTO user_role (user_id, role_id) VALUES (1, 2);
REPLACE INTO user_profile (user_id, profile_key, profile_value) VALUES (1, 'color', 'blue');

REPLACE INTO user (user_id, active, email, first_name, last_name, password) VALUES (2, 1, 'jennica.butler@gmail.com', 'Jennica', 'Butler', '$2a$10$Ahl5DP1VseUvPywlpWa5delXOQf8YOSndTQem1kHFp.jdGwbeOq9C');
REPLACE INTO user_role (user_id, role_id) VALUES (2, 2);
REPLACE INTO user_profile (user_id, profile_key, profile_value) VALUES (2, 'color', 'purple');

REPLACE INTO user (user_id, active, email, first_name, last_name, password) VALUES (3, 1, 'ellis.butler@gmail.com', 'Ellis', 'Butler', '$2a$10$Ahl5DP1VseUvPywlpWa5delXOQf8YOSndTQem1kHFp.jdGwbeOq9C');
REPLACE INTO user_role (user_id, role_id) VALUES (3, 2);
REPLACE INTO user_profile (user_id, profile_key, profile_value) VALUES (3, 'color', 'orange');

REPLACE INTO user (user_id, active, email, first_name, last_name, password) VALUES (4, 1, 'duane_butler@comcast.net', 'Duane', 'Butler', '$2a$10$Ahl5DP1VseUvPywlpWa5delXOQf8YOSndTQem1kHFp.jdGwbeOq9C');
REPLACE INTO user (user_id, active, email, first_name, last_name, password) VALUES (5, 1, 'rachel.humnick@gmail.com', 'Rachel', 'Humnick', '$2a$10$Ahl5DP1VseUvPywlpWa5delXOQf8YOSndTQem1kHFp.jdGwbeOq9C');
REPLACE INTO user (user_id, active, email, first_name, last_name, password) VALUES (6, 1, 'humnick.jake@gmail.com', 'Jake', 'Humnick', '$2a$10$Ahl5DP1VseUvPywlpWa5delXOQf8YOSndTQem1kHFp.jdGwbeOq9C');
REPLACE INTO user (user_id, active, email, first_name, last_name, password) VALUES (7, 1, 'annaraebutler@gmail.com', 'Anna', 'Butler', '$2a$10$Ahl5DP1VseUvPywlpWa5delXOQf8YOSndTQem1kHFp.jdGwbeOq9C');

REPLACE INTO user_role (user_id, role_id) VALUES (4, 2);
REPLACE INTO user_role (user_id, role_id) VALUES (5, 2);
REPLACE INTO user_role (user_id, role_id) VALUES (6, 2);
REPLACE INTO user_role (user_id, role_id) VALUES (7, 2);

REPLACE INTO wish (wish_id, description, price_minimum, price_maximum, user_id, quantity, purchased) VALUES (1, 'Test wish with a much longer name', 18, 25, 1, 1, false);
REPLACE INTO wish (wish_id, description, price_minimum, price_maximum, user_id, quantity, purchased) VALUES (2, 'Suzie Talks-alot', 29.99, 29.99, 1, 1, false);
REPLACE INTO wish (wish_id, description, price_minimum, price_maximum, user_id, quantity, purchased, purchaser_id) VALUES (3, 'A google of my very own', 99.99, 99.99, 1, 1, true, 2);
REPLACE INTO wish (wish_id, description, price_minimum, price_maximum, user_id, quantity, purchased, purchaser_id) VALUES (4, 'Something special', 49.98, 49.98, 1, 1, true, 3);

REPLACE INTO wish_reference (reference_id, wish_id, url) VALUES (1, 1, 'https://www.amazon.com');
REPLACE INTO wish_reference (reference_id, wish_id, url) VALUES (2, 1, 'https://www.google.com');
REPLACE INTO wish_reference (reference_id, wish_id, url) VALUES (3, 2, 'https://www.getbootstrap.com');

REPLACE INTO event (event_id, title, description, date, location) VALUES (1, 'Butler Christmas 2018', 'The coolest getogether ever', '2018-12-22 14:00:00', 'Gma & Gpa Butler''s House');
REPLACE INTO event (event_id, title, description, date, location) VALUES (2, 'LaPlaca Christmas 2018', 'The lit-est getogether ever', '2018-12-25 10:00:00', 'Nona and Grampa''s House');
REPLACE INTO event (event_id, title, description, date) VALUES (3, 'Ellis'' First Christmas!', 'An event of straight fire', '2018-12-25 07:30:00');
REPLACE INTO event (event_id, title, description, date, location) VALUES (4, 'Lynette Christmas', 'Hangin'' with mom', '2018-12-24 16:00:00', 'Grandma''s house');
REPLACE INTO event (event_id, title, description, date, location) VALUES (5, 'Duane Christmas', 'Hangin'' with pops', '2018-12-24 10:30:00', 'Papa''s house');
REPLACE INTO event (event_id, title, description, date, location) VALUES (6, 'Humnick Christmas', 'This is gonna get weird...', '2018-12-25 14:23:00', 'The place');
REPLACE INTO event_members (event_event_id, members_user_id) VALUES (1, 1);
REPLACE INTO event_members (event_event_id, members_user_id) VALUES (2, 1);
REPLACE INTO event_members (event_event_id, members_user_id) VALUES (3, 1);
REPLACE INTO event_members (event_event_id, members_user_id) VALUES (4, 1);
REPLACE INTO event_members (event_event_id, members_user_id) VALUES (5, 1);
REPLACE INTO event_members (event_event_id, members_user_id) VALUES (1, 2);
REPLACE INTO event_members (event_event_id, members_user_id) VALUES (2, 2);
REPLACE INTO event_members (event_event_id, members_user_id) VALUES (3, 2);
REPLACE INTO event_members (event_event_id, members_user_id) VALUES (4, 2);
REPLACE INTO event_members (event_event_id, members_user_id) VALUES (5, 2);
REPLACE INTO event_members (event_event_id, members_user_id) VALUES (1, 3);
REPLACE INTO event_members (event_event_id, members_user_id) VALUES (2, 3);
REPLACE INTO event_members (event_event_id, members_user_id) VALUES (3, 3);
REPLACE INTO event_members (event_event_id, members_user_id) VALUES (4, 3);
REPLACE INTO event_members (event_event_id, members_user_id) VALUES (5, 3);
REPLACE INTO event_members (event_event_id, members_user_id) VALUES (1, 4);
REPLACE INTO event_members (event_event_id, members_user_id) VALUES (1, 5);
REPLACE INTO event_members (event_event_id, members_user_id) VALUES (1, 6);
REPLACE INTO event_members (event_event_id, members_user_id) VALUES (1, 7);
REPLACE INTO event_admins (event_event_id, admins_user_id) VALUES (1, 1);
REPLACE INTO event_admins (event_event_id, admins_user_id) VALUES (2, 2);
REPLACE INTO event_admins (event_event_id, admins_user_id) VALUES (3, 2);
REPLACE INTO event_admins (event_event_id, admins_user_id) VALUES (4, 1);
REPLACE INTO event_admins (event_event_id, admins_user_id) VALUES (5, 1);
REPLACE INTO event_admins (event_event_id, admins_user_id) VALUES (6, 1);
