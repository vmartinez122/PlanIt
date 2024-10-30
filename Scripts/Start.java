//importamos la carpeta PlanIt para poder utilizar los otros archivos
package PlanIt;

import java.util.Scanner;

public class Start {
    public static void main(String[] args) {
        PlanFunctions calendar = new PlanFunctions(); // Crea una instancia de la clase que contiene las demás funciones
        // Este Scanner hace que "read" funcione
        Scanner read = new Scanner(System.in); // Inicializa un scanner
        // Hacemos uso de un while para regresar al menú tras finalizar una tarea
        System.out.println("***************************************************\r\n" + //
                "\r\n" + //
                "   ██████╗ ██╗      █████╗ ███╗   ██╗██╗████████╗\r\n" + //
                "   ██╔══██╗██║     ██╔══██╗████╗  ██║██║╚══██╔══╝\r\n" + //
                "   ██████╔╝██║     ███████║██╔██╗ ██║██║   ██║\r\n" + //
                "   ██╔═══╝ ██║     ██╔══██║██║╚██╗██║██║   ██║\r\n" + //
                "   ██║     ███████╗██║  ██║██║ ╚████║██║   ██║\r\n" + //
                "   ╚═╝     ╚══════╝╚═╝  ╚═╝╚═╝  ╚═══╝╚═╝   ╚═╝\r\n" + //
                "\r\n" + //
                "***************************************************");
        calendar.load();// Método load, lee el documento de guardado
        while (true) {
            System.out.println("\n1. New event");
            System.out.println("2. Edit event");
            System.out.println("3. Delete event");
            System.out.println("4. Show events");
            System.out.println("5. Exit\n");

            // Hacemos uso de switch case para las opciones del menú
            // Como esperamos un carácter en el input del usuario, no necesitamos hacer
            // parse
            String option = read.nextLine();
            switch (option) {
                case "1":
                    System.out.println("You chose 1. New Event\n");
                    calendar.create(option);
                    calendar.save();
                    break;

                case "2":
                    System.out.println("You chose 2. Edit event\n");
                    calendar.show();
                    calendar.edit(option);
                    calendar.save();
                    break;
                case "3":
                    System.out.println("You chose 3. Delete\n");
                    calendar.show();
                    calendar.delete(option);
                    break;
                case "4":
                    System.out.println("You chose 4. Show\n");
                    calendar.show();
                    break;
                case "5":
                    System.out.println("Exiting program...\n");
                    calendar.save();
                    read.close();
                    System.exit(0);
                    break;
                // importante para evitar un error
                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 5.");
            }
        }
    }
}
