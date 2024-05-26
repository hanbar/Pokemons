package org.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DBTrainers {
    private static final Logger logger = LoggerFactory.getLogger(DBTrainers.class);

    public void printAllTrainers() {
        final String query = "SELECT * FROM trainers";

        try (Connection connection = HikariDBSource.getConnection()) {
            final PreparedStatement statement = connection.prepareStatement(query);
            final ResultSet resultSet = statement.executeQuery();

            boolean hasData = false;
            while (resultSet.next()) {
                hasData = true;
                final int id = resultSet.getInt("id");
                final String name = resultSet.getString("name");

                System.out.println("Trainer id: " + id + ", name: " + name);
            }
            if (!hasData) {
                System.out.println("No data found.");
            }
        } catch (Exception e) {
            logger.error("Error while printing trainers", e);
            e.printStackTrace();
        }
    }

    public void addTrainer(String name) {
        final String insert = "INSERT INTO trainers (name) VALUES (?)";

        try (Connection connection = HikariDBSource.getConnection()) {
            final PreparedStatement statement = connection.prepareStatement(insert);
            statement.setString(1, name);
            statement.executeUpdate();
        } catch (Exception e) {
            logger.error("Error while inserting trainer", e);
            e.printStackTrace();
        }
    }

    public void getTrainersSortedByPokemonCount() {
        final String sql = "SELECT trainers.*, COUNT(pokemons.id) AS pokemon_count " +
                "FROM trainers " +
                "LEFT JOIN pokemons ON trainers.id = pokemons.trainer_id " +
                "GROUP BY trainers.id " +
                "ORDER BY pokemon_count DESC";
        try (Connection connection = HikariDBSource.getConnection()) {
            final PreparedStatement statement = connection.prepareStatement(sql);
            final ResultSet resultSet = statement.executeQuery();

            boolean hasData = false;
            while (resultSet.next()) {
                hasData = true;
                final String name = resultSet.getString("name");
                final int count = resultSet.getInt("pokemon_count");

                System.out.println("Trainer name: " + name + ", Pokemon count: " + count);
            }
            if (!hasData) {
                System.out.println("No data found.");
            }
        } catch (Exception e) {
            logger.error("Error while getting trainers by Pokemon count", e);
            e.printStackTrace();
        }
    }

    public void deleteTrainer(int id) {
        final String delete = "DELETE FROM trainers WHERE id = ?";

        try (Connection connection = HikariDBSource.getConnection()) {
            final PreparedStatement statement = connection.prepareStatement(delete);
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (Exception e) {
            logger.error("Error while deleting trainer", e);
            e.printStackTrace();
        }
    }
}
