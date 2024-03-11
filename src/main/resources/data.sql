INSERT INTO product (id, name, code, description, price)
VALUES (nextval('product_seq'),
		'Morra',
		'morra1',
        'Morra Origini Brasilia Santos',
        78.39),
       (nextval('product_seq'),
		'Lavazza',
		'lavazza1',
        'Cafeaua Lavazza Super Crema Espresso',
        58.30),
       (nextval('product_seq'),
		'Pellini',
		'pellini1',
        'Pellini Top 100% Arabica',
        97.00),
       (nextval('product_seq'),
		'Eilles',
		'eilles1',
        'Eilles Tee Teebeutel Assam Special',
        13.90),
       (nextval('product_seq'),
		'Espressor',
		'espressor1',
		'Epressor-automat-cafea-studio-casa-diva-de-luxe',
		1279.90);

INSERT INTO technical_details (id, name, value, product_id)
VALUES (nextval('technical_details_seq'), 'Brand', 'Morra Origini', 1),
       (nextval('technical_details_seq'), 'Gramaj', '1 kg', 1),
       (nextval('technical_details_seq'), 'Continut', 'Cafea', 1),
       (nextval('technical_details_seq'), 'Tip', 'Cafea boabe', 1),
       (nextval('technical_details_seq'), 'Brand', 'Lavazza', 2),
       (nextval('technical_details_seq'), 'Gramaj', '1 kg', 2),
       (nextval('technical_details_seq'), 'Brand', 'Pellini', 2),
       (nextval('technical_details_seq'), 'Gramaj', '1 kg', 2),
       (nextval('technical_details_seq'), 'Tip', 'Cafea boabe', 2),
       (nextval('technical_details_seq'), 'Brand', 'Eilles', 3),
       (nextval('technical_details_seq'), 'Brand', 'Illy', 4),
       (nextval('technical_details_seq'), 'Alimentare', 'capsule IPERESPRESSO', 5),
       (nextval('technical_details_seq'), 'Presiune', '15 Bar', 5),
       (nextval('technical_details_seq'), 'Dimensiune', '260 x 250 x 330 mm', 5),
       (nextval('technical_details_seq'), 'Putere de consum', '1050 W', 5);

INSERT INTO roles (id, name)
VALUES (nextval('roles_seq'), 'ADMIN'),
		(nextval('roles_seq'), 'USER');

INSERT INTO users (id, email, first_name, last_name, password)
VALUES (nextval('users_seq'), 'adminEmail', 'Admin', 'Admin', '$2a$10$qBbKAklFkIgpdYkGzClmne1r9ko8vN20xVnvUg6uGrZ08WT2syIVW');

INSERT INTO users_roles (role_id, user_id)
VALUES (1,1);

INSERT INTO inventories (id, code, quantity)
VALUES (nextval('inventories_seq'), 'morra1', 100),
       (nextval('inventories_seq'), 'lavazza1', 200),
       (nextval('inventories_seq'), 'eilles1', 5),
       (nextval('inventories_seq'), 'espressor1', 10);
