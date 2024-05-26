package org.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBPokemons {
    private static final Logger logger = LoggerFactory.getLogger(DBPokemons.class);

    public void printAllPokemons() {
        final String query = "SELECT * FROM pokemons";

        try (Connection connection = HikariDBSource.getConnection()) {
            prepareGetStatement(query, connection);
        } catch (Exception e) {
            logger.error("Error while printing Pokemons", e);
            e.printStackTrace();
        }
    }

    public void printUncaughtPokemons() {
        final String query = "SELECT * FROM pokemons WHERE trainer_id IS NULL";

        try (Connection connection = HikariDBSource.getConnection()) {
            prepareGetStatement(query, connection);
        } catch (Exception e) {
            logger.error("Error while printing uncaught Pokemons", e);
            e.printStackTrace();
        }
    }

    private void prepareGetStatement(String query, Connection connection) throws SQLException {
        final PreparedStatement statement = connection.prepareStatement(query);
        final ResultSet resultSet = statement.executeQuery();

        boolean hasData = false;
        while (resultSet.next()) {
            hasData = true;
            final int id = resultSet.getInt("id");
            final String name = resultSet.getString("name");
            final String type = resultSet.getString("type");
            final int trainerId = resultSet.getInt("trainer_id");

            System.out.println("Pokemon id: " + id + ", name: " + name + ", type: " + type + ", trainer id: " + trainerId);
        }
        if (!hasData) {
            System.out.println("No data found.");
        }
    }

    public void addPokemon(String name, String type) {
        final String insert = "INSERT INTO pokemons (name, type) VALUES (?, ?)";

        try (Connection connection = HikariDBSource.getConnection()) {
            final PreparedStatement statement = connection.prepareStatement(insert);
            statement.setString(1, name);
            statement.setString(2, type);
            statement.executeUpdate();
        } catch (Exception e) {
            logger.error("Error while inserting a Pokemon", e);
            e.printStackTrace();
        }
    }

    public void getPokemonsByTrainer(String trainerName) {
        String query = "SELECT p.name, p.type, t.name as trainer_name FROM pokemons p JOIN trainers t ON t.id = p.trainer_id WHERE t.name = ?";

        try (Connection connection = HikariDBSource.getConnection()) {
            final PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, trainerName);
            final ResultSet resultSet = statement.executeQuery();

            boolean hasData = false;
            while (resultSet.next()) {
                hasData = true;
                final String name = resultSet.getString("name");
                final String type = resultSet.getString("type");
                final String trainerNameInDb = resultSet.getString("trainer_name");

                System.out.println("Pokemon name: " + name + ", type: " + type + ", trainer name: " + trainerNameInDb);
            }
            if (!hasData) {
                System.out.println("No data found.");
            }
        } catch (Exception e) {
            logger.error("Error while getting Pokemons by trainer", e);
            e.printStackTrace();
        }
    }

    public void getUncaughtPokemons() {
        String sql = "SELECT * FROM pokemons WHERE trainer_id IS NULL";

        try (Connection connection = HikariDBSource.getConnection()) {
            final PreparedStatement statement = connection.prepareStatement(sql);
            final ResultSet resultSet = statement.executeQuery();

            boolean hasData = false;
            while (resultSet.next()) {
                hasData = true;
                final String name = resultSet.getString("name");
                final String type = resultSet.getString("type");

                System.out.println("Pokemon name: " + name + ", type: " + type);
            }
            if (!hasData) {
                System.out.println("No data found.");
            }
        } catch (Exception e) {
            logger.error("Error while getting uncaught Pokemons", e);
            e.printStackTrace();
        }
    }

    public void catchPokemon(int pokemonId, int trainerId) {
        final String update = "UPDATE pokemons SET trainer_id = ? WHERE id = ? AND trainer_id IS NULL";

        try (Connection connection = HikariDBSource.getConnection()) {
            final PreparedStatement statement = connection.prepareStatement(update);
            statement.setInt(1, trainerId);
            statement.setInt(2, pokemonId);
            statement.executeUpdate();
        } catch (Exception e) {
            logger.error("Error while catching a Pokemon", e);
            e.printStackTrace();
        }
    }

    public void deletePokemon(int id) {
        final String delete = "DELETE FROM pokemons WHERE id = ?";

        try (Connection connection = HikariDBSource.getConnection()) {
            final PreparedStatement statement = connection.prepareStatement(delete);
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (Exception e) {
            logger.error("Error while deleting a Pokemon", e);
            e.printStackTrace();
        }
    }
}