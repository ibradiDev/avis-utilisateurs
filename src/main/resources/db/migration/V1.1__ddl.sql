-- Créer la table utilisateur
create table utilisateur (
  id serial primary key,
  nom varchar(100)
);

-- Créer la table avis
create table avis (
  id serial primary key,
  message text,
  creation timestamp,
  utilisateur_id int,
  foreign key (utilisateur_id) references utilisateur(id)
);