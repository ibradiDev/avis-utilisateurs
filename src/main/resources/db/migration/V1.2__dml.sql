insert into utilisateur (nom) values ('John');

insert into avis (message, utilisateur_id) values ('Formation Spring', (select id from utilisateur where nom='John'));



insert into utilisateur (nom) values ('Ibradi');

insert into avis (message, utilisateur_id) values ('Formation FullStack', (select id from utilisateur where nom='Ibradi'));