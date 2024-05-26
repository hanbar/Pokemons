USE pokemons;

CREATE TABLE IF NOT EXISTS trainers (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS pokemons (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    type VARCHAR(100) NOT NULL,
    trainer_id INT,
    FOREIGN KEY (trainer_id) REFERENCES trainers(id) ON DELETE SET NULL
);

INSERT INTO trainers (name) VALUES ('John Doe'), ('Jane Doe');

INSERT INTO pokemons (name, type, trainer_id) VALUES ('Pikachu', 'electric', 1);
INSERT INTO pokemons (name, type) VALUES ('Charmander', 'fire'), ('Diglett', 'ground');