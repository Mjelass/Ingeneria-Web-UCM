-- insert admin (username a, password aa)
INSERT INTO IWUser (id, enabled, roles, username, password)
VALUES (1, TRUE, 'ADMIN,USER', 'a',
    '{bcrypt}$2a$10$2BpNTbrsarbHjNsUWgzfNubJqBRf.0Vz9924nRSHBqlbPKerkgX.W');
INSERT INTO IWUser (id, enabled, roles, username, password)
VALUES (2, TRUE, 'USER', 'b',
    '{bcrypt}$2a$10$2BpNTbrsarbHjNsUWgzfNubJqBRf.0Vz9924nRSHBqlbPKerkgX.W');

-- insert remained user:
insert into IWUSER (id, birthdate, first_name, last_name, email, username, password, description, enabled, languages, level, location, rating, roles, status) values (3, PARSEDATETIME('21/12/1970', 'dd/MM/yyyy'), 'Idelle', 'Goford', 'igoford0@hud.gov', 'igoford0', '{bcrypt}$2a$10$2BpNTbrsarbHjNsUWgzfNubJqBRf.0Vz9924nRSHBqlbPKerkgX.W', 'Suspendisse potenti. In eleifend quam a odio. In hac habitasse platea dictumst.', TRUE, 'Hebrew', 'NONE', 'Vol’no-Nadezhdinskoye', 0, 'USER', 'ACTIVE');
insert into IWUSER (id, birthdate, first_name, last_name, email, username, password, description, enabled, languages, level, location, rating, roles, status) values (4, PARSEDATETIME('09/02/1989', 'dd/MM/yyyy'), 'Adore', 'Nation', 'anation1@senate.gov', 'anation1', '{bcrypt}$2a$10$2BpNTbrsarbHjNsUWgzfNubJqBRf.0Vz9924nRSHBqlbPKerkgX.W', 'Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Vivamus vestibulum sagittis sapien. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus.', TRUE, 'Yiddish', 'NONE', 'Yunlong', 0, 'USER', 'ACTIVE');
insert into IWUSER (id, birthdate, first_name, last_name, email, username, password, description, enabled, languages, level, location, rating, roles, status) values (5, PARSEDATETIME('26/10/1974', 'dd/MM/yyyy'), 'Levon', 'Mumford', 'lmumford2@opera.com', 'lmumford2', '{bcrypt}$2a$10$2BpNTbrsarbHjNsUWgzfNubJqBRf.0Vz9924nRSHBqlbPKerkgX.W', 'Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Proin risus. Praesent lectus.', TRUE, 'Belarusian', 'NONE', 'Blois', 0, 'USER', 'ACTIVE');
insert into IWUSER (id, birthdate, first_name, last_name, email, username, password, description, enabled, languages, level, location, rating, roles, status) values (6, PARSEDATETIME('14/02/1989', 'dd/MM/yyyy'), 'Kaitlyn', 'Brodest', 'kbrodest3@usnews.com', 'kbrodest3', '{bcrypt}$2a$10$2BpNTbrsarbHjNsUWgzfNubJqBRf.0Vz9924nRSHBqlbPKerkgX.W', 'In hac habitasse platea dictumst. Morbi vestibulum, velit id pretium iaculis, diam erat fermentum justo, nec condimentum neque sapien placerat ante. Nulla justo.', TRUE, 'Spanish', 'NONE', 'Fratar', 0, 'USER', 'ACTIVE');
insert into IWUSER (id, birthdate, first_name, last_name, email, username, password, description, enabled, languages, level, location, rating, roles, status) values (7, PARSEDATETIME('22/08/1992', 'dd/MM/yyyy'), 'Andy', 'McIlriach', 'amcilriach4@google.cn', 'amcilriach4', '{bcrypt}$2a$10$2BpNTbrsarbHjNsUWgzfNubJqBRf.0Vz9924nRSHBqlbPKerkgX.W', 'Nullam porttitor lacus at turpis. Donec posuere metus vitae ipsum. Aliquam non mauris.', TRUE, 'Italian', 'NONE', 'Valle de Ángeles', 0, 'USER', 'ACTIVE');
insert into IWUSER (id, birthdate, first_name, last_name, email, username, password, description, enabled, languages, level, location, rating, roles, status) values (8, PARSEDATETIME('20/04/1971', 'dd/MM/yyyy'), 'Catie', 'O''Feeney', 'cofeeney5@ft.com', 'cofeeney5', '{bcrypt}$2a$10$2BpNTbrsarbHjNsUWgzfNubJqBRf.0Vz9924nRSHBqlbPKerkgX.W', 'Quisque id justo sit amet sapien dignissim vestibulum. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Nulla dapibus dolor vel est. Donec odio justo, sollicitudin ut, suscipit a, feugiat et, eros.', TRUE, 'Swahili', 'NONE', 'Ngala', 0, 'USER', 'ACTIVE');
insert into IWUSER (id, birthdate, first_name, last_name, email, username, password, description, enabled, languages, level, location, rating, roles, status) values (9, PARSEDATETIME('08/03/1973', 'dd/MM/yyyy'), 'Nedda', 'Springall', 'nspringall6@dmoz.org', 'nspringall6', '{bcrypt}$2a$10$2BpNTbrsarbHjNsUWgzfNubJqBRf.0Vz9924nRSHBqlbPKerkgX.W', 'Morbi non lectus. Aliquam sit amet diam in magna bibendum imperdiet. Nullam orci pede, venenatis non, sodales sed, tincidunt eu, felis.', TRUE, 'Malagasy', 'NONE', 'Boulaouane', 0, 'USER', 'ACTIVE');
insert into IWUSER (id, birthdate, first_name, last_name, email, username, password, description, enabled, languages, level, location, rating, roles, status) values (10, PARSEDATETIME('19/04/1971', 'dd/MM/yyyy'), 'Iorgo', 'Grindrod', 'igrindrod7@utexas.edu', 'igrindrod7', '{bcrypt}$2a$10$2BpNTbrsarbHjNsUWgzfNubJqBRf.0Vz9924nRSHBqlbPKerkgX.W', 'Duis aliquam convallis nunc. Proin at turpis a pede posuere nonummy. Integer non velit.', TRUE, 'Hiri Motu', 'NONE', 'Katoro', 0, 'USER', 'BLACK_LISTED');
insert into IWUSER (id, birthdate, first_name, last_name, email, username, password, description, enabled, languages, level, location, rating, roles, status) values (11, PARSEDATETIME('12/04/1990', 'dd/MM/yyyy'), 'Tyrone', 'Bernt', 'tbernt8@theguardian.com', 'tbernt8', '{bcrypt}$2a$10$2BpNTbrsarbHjNsUWgzfNubJqBRf.0Vz9924nRSHBqlbPKerkgX.W', 'Donec diam neque, vestibulum eget, vulputate ut, ultrices vel, augue. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Donec pharetra, magna vestibulum aliquet ultrices, erat tortor sollicitudin mi, sit amet lobortis sapien sapien non mi. Integer ac neque.', TRUE, 'Aymara', 'NONE', 'Xichangmen', 0, 'USER', 'ACTIVE');
insert into IWUSER (id, birthdate, first_name, last_name, email, username, password, description, enabled, languages, level, location, rating, roles, status) values (12, PARSEDATETIME('01/04/1990', 'dd/MM/yyyy'), 'Cesya', 'Gave', 'cgave9@cdc.gov', 'cgave9', '{bcrypt}$2a$10$2BpNTbrsarbHjNsUWgzfNubJqBRf.0Vz9924nRSHBqlbPKerkgX.W', 'Proin eu mi. Nulla ac enim. In tempor, turpis nec euismod scelerisque, quam turpis adipiscing lorem, vitae mattis nibh ligula nec sem.', TRUE, 'Khmer', 'NONE', 'Jadranovo', 0, 'USER', 'BLACK_LISTED');

--insert events:
insert into EVENT (id, title, capacity, finish_date, init_date, destination, notes, description, occupied, price, type, user_owner_id, status, transport, reunion_point) values (1, 'Colombia', 2, PARSEDATETIME('02/07/2022', 'dd/MM/yyyy'), PARSEDATETIME('02/02/2022', 'dd/MM/yyyy'), 'Spain', 'NO_KIDS', 'Vestibulum quam sapien, varius ut, blandit non, interdum in, ante. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Duis faucibus accumsan odio. Curabitur convallis.', 1, 133, 'MUSEUM', 6, 'OPEN', 'CAR', '12400 Moulton Terrace');
insert into EVENT (id, title, capacity, finish_date, init_date, destination, notes, description, occupied, price, type, user_owner_id, status, transport, reunion_point) values (2, 'Colombia', 3, PARSEDATETIME('08/09/2022', 'dd/MM/yyyy'), PARSEDATETIME('03/03/2022', 'dd/MM/yyyy'), 'Brazil', 'NO_ANIMALS', 'Get ready to embark on an unforgettable journey to the vibrant city of Madrid, the cultural and artistic capital of Spain. From the moment you arrive, youll be swept up in the energy and excitement of this bustling metropolis. Stroll along the picturesque streets and admire the stunning architecture, indulge in the delicious local cuisine, and experience the passionate flamenco dancing that is synonymous with Madrids rich cultural heritage. Visit world-renowned museums, such as the Prado Museum and the Reina Sofia Museum, and marvel at the works of some of Spains most famous artists, including Goya and Picasso. Whether youre a history buff, a foodie, or a lover of the arts, Madrid has something for everyone. Dont wait any longer to book your trip to this vibrant and unforgettable city!', 1, 264, 'CAMPING', 3, 'FINISH', 'CAR', '16 Hallows Hill');
insert into EVENT (id, title, capacity, finish_date, init_date, destination, notes, description, occupied, price, type, user_owner_id, status, transport, reunion_point) values (3, 'Angola', 10, PARSEDATETIME('29/01/2023', 'dd/MM/yyyy'), PARSEDATETIME('18/09/2022', 'dd/MM/yyyy'), 'Ecuador', 'NO_ANIMALS', 'Proin eu mi. Nulla ac enim. In tempor, turpis nec euismod scelerisque, quam turpis adipiscing lorem, vitae mattis nibh ligula nec sem.', 1, 43, 'CAMPING', 6, 'OPEN', 'SHIP', '56019 Buell Trail');
insert into EVENT (id, title, capacity, finish_date, init_date, destination, notes, description, occupied, price, type, user_owner_id, status, transport, reunion_point) values (4, 'Indonesia', 8, PARSEDATETIME('19/11/2022', 'dd/MM/yyyy'), PARSEDATETIME('08/12/2022', 'dd/MM/yyyy'), 'Russia', 'NO_KIDS', 'Sed sagittis. Nam congue, risus semper porta volutpat, quam pede lobortis ligula, sit amet eleifend pede libero quis orci. Nullam molestie nibh in lectus.', 1, 12, 'MUSEUM', 12, 'OPEN', 'BUS', '56145 Washington Road');
insert into EVENT (id, title, capacity, finish_date, init_date, destination, notes, description, occupied, price, type, user_owner_id, status, transport, reunion_point) values (5, 'Portugal', 8, PARSEDATETIME('29/08/2022', 'dd/MM/yyyy'), PARSEDATETIME('09/02/2023', 'dd/MM/yyyy'), 'Thailand', 'NO_ANIMALS', 'Vestibulum quam sapien, varius ut, blandit non, interdum in, ante. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Duis faucibus accumsan odio. Curabitur convallis.', 1, 232, 'CAMPING', 7, 'OPEN', 'CAR', '832 Dahle Crossing');
insert into EVENT (id, title, capacity, finish_date, init_date, destination, notes, description, occupied, price, type, user_owner_id, status, transport, reunion_point) values (6, 'Colombia', 9, PARSEDATETIME('29/06/2022', 'dd/MM/yyyy'), PARSEDATETIME('01/06/2022', 'dd/MM/yyyy'), 'China', 'NO_ANIMALS', 'Duis consequat dui nec nisi volutpat eleifend. Donec ut dolor. Morbi vel lectus in quam fringilla rhoncus.', 1, 13, 'CONCERT', 10, 'OPEN', 'BUS', '52 Rigney Pass');
insert into EVENT (id, title, capacity, finish_date, init_date, destination, notes, description, occupied, price, type, user_owner_id, status, transport, reunion_point) values (7, 'Brazil', 9, PARSEDATETIME('18/10/2022', 'dd/MM/yyyy'), PARSEDATETIME('24/10/2022', 'dd/MM/yyyy'), 'Guadeloupe', 'NO_ANIMALS,NO_KIDS', 'Nullam sit amet turpis elementum ligula vehicula consequat. Morbi a ipsum. Integer a nibh.', 1, 130, 'EVENT', 11, 'OPEN', 'SHIP', '806 Banding Way');
insert into EVENT (id, title, capacity, finish_date, init_date, destination, notes, description, occupied, price, type, user_owner_id, status, transport, reunion_point) values (8, 'Indonesia', 8, PARSEDATETIME('17/07/2022', 'dd/MM/yyyy'), PARSEDATETIME('16/03/2023', 'dd/MM/yyyy'), 'Greece', 'NO_KIDS', 'Cras non velit nec nisi vulputate nonummy. Maecenas tincidunt lacus at velit. Vivamus vel nulla eget eros elementum pellentesque.', 1, 214, 'EVENT', 8, 'OPEN', 'PLANE', '5404 International Hill');
insert into EVENT (id, title, capacity, finish_date, init_date, destination, notes, description, occupied, price, type, user_owner_id, status, transport, reunion_point) values (9, 'Russia', 3, PARSEDATETIME('06/09/2022', 'dd/MM/yyyy'), PARSEDATETIME('28/05/2022', 'dd/MM/yyyy'), 'Russia', 'NO_KIDS', 'Maecenas tristique, est et tempus semper, est quam pharetra magna, ac consequat metus sapien ut nunc. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Mauris viverra diam vitae quam. Suspendisse potenti.', 1, 262, 'EVENT', 4, 'OPEN', 'SHIP', '4 Pond Point');
insert into EVENT (id, title, capacity, finish_date, init_date, destination, notes, description, occupied, price, type, user_owner_id, status, transport, reunion_point) values (10, 'Thailand', 4, PARSEDATETIME('14/02/2023', 'dd/MM/yyyy'), PARSEDATETIME('04/07/2022', 'dd/MM/yyyy'), 'Pakistan', 'NO_ANIMALS,NO_KIDS', 'Curabitur in libero ut massa volutpat convallis. Morbi odio odio, elementum eu, interdum eu, tincidunt in, leo. Maecenas pulvinar lobortis est.', 1, 98, 'EVENT', 11, 'OPEN', 'PLANE', '7 Anhalt Circle');
insert into EVENT (id, title, capacity, finish_date, init_date, destination, notes, description, occupied, price, type, user_owner_id, status, transport, reunion_point) values (11, 'Madrid', 4, PARSEDATETIME('14/02/2023', 'dd/MM/yyyy'), PARSEDATETIME('04/07/2022', 'dd/MM/yyyy'), 'Spain', 'NO_ANIMALS,NO_KIDS', 'Curabitur in libero ut massa volutpat convallis. Morbi odio odio, elementum eu, interdum eu, tincidunt in, leo. Maecenas pulvinar lobortis est.', 1, 98, 'EVENT', 3, 'OPEN', 'PLANE', '7 Anhalt Circle');

--insert user_event:
INSERT INTO USER_EVENT (user_id, event_id, joined, fav, rol) VALUES (6, 1, TRUE, TRUE, 'HOST');
INSERT INTO USER_EVENT (user_id, event_id, joined, fav, rol) VALUES (3, 2, TRUE, TRUE, 'HOST');
INSERT INTO USER_EVENT (user_id, event_id, joined, fav, rol) VALUES (6, 3, TRUE, TRUE, 'HOST');
INSERT INTO USER_EVENT (user_id, event_id, joined, fav, rol) VALUES (12, 4, TRUE, TRUE, 'HOST');
INSERT INTO USER_EVENT (user_id, event_id, joined, fav, rol) VALUES (7, 5, TRUE, TRUE, 'HOST');
INSERT INTO USER_EVENT (user_id, event_id, joined, fav, rol) VALUES (10, 6, TRUE, TRUE, 'HOST');
INSERT INTO USER_EVENT (user_id, event_id, joined, fav, rol) VALUES (11, 7, TRUE, TRUE, 'HOST');
INSERT INTO USER_EVENT (user_id, event_id, joined, fav, rol) VALUES (8, 8, TRUE, TRUE, 'HOST');
INSERT INTO USER_EVENT (user_id, event_id, joined, fav, rol) VALUES (4, 9, TRUE, TRUE, 'HOST');
INSERT INTO USER_EVENT (user_id, event_id, joined, fav, rol) VALUES (11, 10, TRUE, TRUE, 'HOST');
INSERT INTO USER_EVENT (user_id, event_id, joined, fav, rol) VALUES (3, 11, TRUE, TRUE, 'HOST');
INSERT INTO USER_EVENT (user_id, event_id, joined, fav, rol) VALUES (3, 7, TRUE, TRUE, 'AAAAA');

-- insert report:
insert into REPORT (id, user_source, user_target, description) values (1, 12, 11, 'porttitor lacus at turpis donec posuere metus vitae ipsum aliquam non mauris morbi non lectus');
insert into REPORT (id, user_source, user_target, description) values (2, 6, 9, 'at feugiat non pretium quis lectus suspendisse potenti in eleifend quam a odio in');
insert into REPORT (id, user_source, user_target, description) values (3, 7, 9, 'facilisi cras non velit nec nisi vulputate nonummy maecenas tincidunt lacus');
insert into REPORT (id, user_source, user_target, description) values (4, 11, 3, 'sit amet justo morbi ut odio cras mi pede malesuada in imperdiet et commodo vulputate justo in blandit ultrices');
insert into REPORT (id, user_source, user_target, description) values (5, 3, 6, 'felis eu sapien cursus vestibulum proin eu mi nulla ac enim in tempor turpis nec');


-- start id numbering from a value that is larger than any assigned above
ALTER SEQUENCE "PUBLIC"."GEN" RESTART WITH 1024;
