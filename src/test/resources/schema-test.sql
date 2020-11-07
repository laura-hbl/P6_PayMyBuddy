DROP DATABASE IF EXISTS paymybuddy_test;

CREATE DATABASE paymybuddy_test CHARACTER SET utf8mb4;

USE paymybuddy_test;

CREATE TABLE user (
                id BIGINT AUTO_INCREMENT NOT NULL,
                first_name VARCHAR(70) NOT NULL,
                last_name VARCHAR(70) NOT NULL,
                email VARCHAR(70) NOT NULL,
                password VARCHAR(100) NOT NULL,
                phone VARCHAR(20) NOT NULL,
                PRIMARY KEY (id)
);


CREATE TABLE buddy_account (
                id BIGINT AUTO_INCREMENT NOT NULL,
                user_id BIGINT NOT NULL,
                balance DECIMAL(7,2) NOT NULL,
                PRIMARY KEY (id)
);


CREATE TABLE bank_account (
                id BIGINT AUTO_INCREMENT NOT NULL,
                user_id BIGINT NOT NULL,
                iban VARCHAR(34),
                bic VARCHAR(11),
                PRIMARY KEY (id)
);


CREATE TABLE transaction (
                id BIGINT AUTO_INCREMENT NOT NULL,
                type VARCHAR(9) NOT NULL,
                buddy_account_owner_id BIGINT NOT NULL,
                buddy_account_receiver_id BIGINT,
                bank_account_id BIGINT,
                date DATE NOT NULL,
                description VARCHAR(100) NOT NULL,
                amount DECIMAL(5,2) NOT NULL,
                fee DECIMAL(3,2) NOT NULL,
                PRIMARY KEY (id)
);

CREATE TABLE connect (
                owner_id BIGINT NOT NULL,
                buddy_id BIGINT NOT NULL,
                PRIMARY KEY (owner_id, buddy_id)
);


ALTER TABLE connect ADD CONSTRAINT user_transaction_fk
FOREIGN KEY (buddy_id)
REFERENCES user (id)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE connect ADD CONSTRAINT user_transaction_fk1
FOREIGN KEY (owner_id)
REFERENCES user (id)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE bank_account ADD CONSTRAINT user_bank_account_fk
FOREIGN KEY (user_id)
REFERENCES user (id)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE buddy_account ADD CONSTRAINT user_buddy_account_fk
FOREIGN KEY (user_id)
REFERENCES user (id)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE transaction ADD CONSTRAINT buddy_account_transaction_fk2
FOREIGN KEY (buddy_account_owner_id)
REFERENCES buddy_account (id)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE transaction ADD CONSTRAINT buddy_account_transaction_fk3
FOREIGN KEY (buddy_account_receiver_id)
REFERENCES buddy_account (id)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE transaction ADD CONSTRAINT bank_account_transaction_fk
FOREIGN KEY (bank_account_id)
REFERENCES bank_account (id)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

INSERT INTO user
VALUES
(1, "Brad", "Pitt", "brad@gmail.com", "$2a$10$Ua2VaNYHIQvHTgnO5K5hbOFP", "111-111-111"),
(2, "Tom", "Cruise", "tom@gmail.com", "$2a$10$hb87OnOgcsOP2sCSbPaADxRh6P", "222-222-222"),
(3, "Leonardo", "Dicaprio", "leonardo@gmail.com", "$2a$10$SqRkrwsADxx0i15BZ5JkyOY", "333-333-333"),
(4, "Johnny", "Depp", "johnny@gmail.com", "$5a$33$SqRkhfdAfr0i176FHJkyO9", "444-444-444"),
(5, "Will", "Smith", "will@gmail.com", "HG%hgfdEdAfrvfd6GvDDd4ff", "555-555-555");

INSERT INTO buddy_account
VALUES
(1, 1, 200),
(2, 2, 800),
(3, 3, 15),
(4, 4, 0);

INSERT INTO bank_account
VALUES
(1, 1, "FR10 1000 1000 1000 1000 1000 J01", "CRLYGTED"),
(2, 2, "FR20 2000 2000 2000 2000 2000 J02", "CRLYPLVR"),
(3, 3, "FR30 3000 3000 3000 3000 3000 J03", "CRLYAEZS");

INSERT INTO transaction
VALUES
(1, "TRANSFER", 1, null, 1, '2020-11-03', "transfer", 100, 0.5),
(2, "PAYMENT", 2, 1, null, '2020-11-03', "pay beer", 5.5, 0.03),
(3, "RECHARGE", 3, null, 3, '2020-11-03', "recharge", 140, 0.7);

INSERT INTO connect
VALUES
(1, 2),
(2, 1);