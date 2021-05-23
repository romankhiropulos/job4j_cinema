DELETE FROM ticket;
DELETE FROM account;

ALTER SEQUENCE global_seq RESTART WITH 1000;

INSERT INTO account (id, username, email, phone) VALUES
(2000, 'Admin', 'admin@gmail.com', '89997776655');

INSERT INTO ticket (session_id, row, cell, account_id)
VALUES (1, 1, 1, 2000),
       (1, 1, 2, 2000),
       (1, 1, 3, 2000),
       (1, 2, 1, 2000),
       (1, 2, 2, 2000),
       (1, 2, 3, 2000),
       (1, 3, 1, 2000),
       (1, 3, 2, 2000),
       (1, 3, 3, 2000),
       (1, 4, 1, 2000),
       (1, 4, 2, 2000),
       (1, 4, 3, 2000);