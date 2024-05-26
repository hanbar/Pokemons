package org.example;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        final DBPokemons dbPokemons = new DBPokemons();
        final DBTrainers dbTrainers = new DBTrainers();

        printPokemons(dbPokemons);
        printTrainers(dbTrainers);
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("1. Create Trainer");
            System.out.println("2. Create Pokemon");
            System.out.println("3. Delete Trainer");
            System.out.println("4. Delete Pokemon");
            System.out.println("5. Print Pokemons belonging to a trainer");
            System.out.println("6. Print trainers sorted according to number of Pokemons");
            System.out.println("7. Print uncaught Pokemons");
            System.out.println("8. Catch a Pokemon - check a trainer and an uncaught Pokemon");

            int choice = getValidInt(scanner);
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Enter a new trainer name: ");
                    String tName = scanner.nextLine();
                    dbTrainers.addTrainer(tName);
                    printTrainers(dbTrainers);
                    break;
                case 2:
                    System.out.print("Enter a new Pokemon name: ");
                    String pName = scanner.nextLine();
                    System.out.print("Enter a new Pokemon type: ");
                    String pType = scanner.nextLine();
                    dbPokemons.addPokemon(pName, pType);
                    printPokemons(dbPokemons);
                    break;
                case 3:
                    printTrainers(dbTrainers);
                    System.out.print("Enter id of the trainer to delete: ");
                    int tId = getValidInt(scanner);
                    dbTrainers.deleteTrainer(tId);
                    printTrainers(dbTrainers);
                    break;
                case 4:
                    printPokemons(dbPokemons);
                    System.out.print("Enter id of the pokemon to delete: ");
                    int pId = getValidInt(scanner);
                    dbPokemons.deletePokemon(pId);
                    printPokemons(dbPokemons);
                    break;
                case 5:
                    System.out.print("Enter trainer name: ");
                    String trainerName = scanner.nextLine();
                    dbPokemons.getPokemonsByTrainer(trainerName);
                    break;
                case 6:
                    dbTrainers.getTrainersSortedByPokemonCount();
                    break;
                case 7:
                    dbPokemons.getUncaughtPokemons();
                    break;
                case 8:
                    printTrainers(dbTrainers);
                    System.out.print("Enter a trainer id, who catches: ");
                    int trainerId = getValidInt(scanner);
                    printUncaughtPokemons(dbPokemons);
                    System.out.print("Enter a Pokemon id: ");
                    int pokemonId = getValidInt(scanner);
                    dbPokemons.catchPokemon(pokemonId, trainerId);
                    printPokemons(dbPokemons);
                    break;
                default:
                    System.out.println("Invalid choice");
                    break;
            }
        }
    }

    public static void printPokemons(DBPokemons dbPokemons) {
        System.out.println("PRINTING ALL POKEMONS");
        dbPokemons.printAllPokemons();
        System.out.println();
    }

    public static void printUncaughtPokemons(DBPokemons dbPokemons) {
        System.out.println("PRINTING UNCAUGHT POKEMONS");
        dbPokemons.printUncaughtPokemons();
        System.out.println();
    }

    public static void printTrainers(DBTrainers dbTrainers) {
        System.out.println("PRINTING ALL TRAINERS");
        dbTrainers.printAllTrainers();
        System.out.println();
    }

    private static int getValidInt(Scanner scanner) {
        int choice;
        while (true) {
            try {
                choice = scanner.nextInt();
                break;
            } catch (Exception e) {
                System.out.println("Invalid input");
                scanner.nextLine();
            }
        }
        return choice;
    }
}