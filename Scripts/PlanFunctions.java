//importamos la carpeta PlanIt para poder utilizar los otros archivos
package PlanIt;

//Funciones de java que utiliza el programa:
import java.util.ArrayList;
import java.util.Scanner;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;

public class PlanFunctions {
    // Constructor de la clase PlanFunctions, con este podemos crear una instancia
    // de
    // la clase PlanFunctions desde el menú principal
    public PlanFunctions() {
    }

    Scanner read = new Scanner(System.in); // Inicializamos un scanner
    boolean stop = false; // Variable que utilizamos para salir fuera de algunos métodos
    private static int index; // Variable que indica el índice dentro de arraylist, necesita ser estática

    // Creamos el arraylist donde se almacenan los eventos
    ArrayList<Event> eventList = new ArrayList<Event>();

    class Event { // Clase que almacena los eventos
        // Cada evento esta formado de estos 3 datos:
        private String date;
        private String name;
        private String description;

        // Constructor para eventos nuevos
        public Event(String date, String name, String description) {
            // Este constructor, genera una instancia de Event con los 3 campos que le
            // introducimos
            this.date = date;
            this.name = name;
            this.description = description;
        }

        // Métodos para devolver los datos en cada instancia de Event:
        String getDate() {
            return date;
        }

        String getName() {
            return name;
        }

        String getDescription() {
            return description;
        }
    }

    Event newEvent; // Inicializamos una variable tipo Event, para almacenar los eventos nuevos

    class Date { // Clase para formato de fechas
        // Variables de la clase:
        private String day;
        private String month;
        private String year;
        private String date; // fecha completa

        public Date(String day, String month, String year) { // Constructor de la clase
            this.day = day;
            this.month = month;
            this.year = year;
        }

        String buildDate() { // Devuelve la fecha con el formato que queremos
            date = day + "/" + month + "/" + year;
            return date;
        }
    }

    Date newDate; // Variable donde almacenaremos date

    public void create(String option) { // Método creación de eventos
        String name = inputEvent(); // Llamamos a la función donde el usuario define un nuevo evento
        // InputEvent devuelve "name", como el nombre del evento a crear, que utilizamos
        // para realizar una confirmación.
        System.out.println(
                "Do you want to add: [" + name + "] (y/n)?"); // Confirmación con el nombre del evento
        CheckConfirm(option); // Función de confirmación, utiliza option para saber desde donde se ha llamado
    }

    private String inputEvent() { // Función que recibe input y se asegura de que los eventos tengan el formato
                                  // correcto

        // Introducir fecha
        System.out.println("Date (dd/mm/yyyy):");
        // Para introducir los datos de la fecha, utilizamos la función checkDate, que
        // verifica el formato de los datos
        String day = checkDate("Day");
        String month = checkDate("Month");
        String year = checkDate("Year");
        newDate = new Date(day, month, year); // Creamos una instancia de Date con estos datos

        // Introducir nombre
        System.out.println("Name of the event:");
        String name;
        do { // Comprobación para evitar nombres vacíos o ";" (Rompería archivo de guardado)
            name = read.nextLine();
            if (name.isBlank()) {
                System.out.println("Cannot be blank" + "\nName of the event:");
            } else if (name.contains(";")) {
                System.out.println("Cannot contain \";\" character" + "\nName of the event:");
            }
        } while (name.isBlank() || name.contains(";")); // El usuario tendrá que escribir un nombre para el evento

        // Introducir descripción
        System.out.println("Description:");
        String description;
        do { //Comprueba si hay ";", pueden haber descripciones vacías
            description = read.nextLine();
            if (description.contains(";")) {
                System.out.println("Cannot contain \";\" character" + "\nDescription:");
            }
        } while (description.contains(";"));

        // Creamos un evento con la fecha construida y los demás datos:
        newEvent = new Event(newDate.buildDate(), name, description);
        return name; // Devuelve el nombre del evento para indicarlo en las confirmaciones
    }

    String checkDate(String type) { // Método para introducir un número de la fecha y comprobarlo
        String input;
        do { // Mientras el número sea válido, el programa sigue preguntando
            System.out.print(type + ": "); // type se declara cuando llamamos a la función, sirve para que el usuario
                                           // sepa lo que ha de escribir
            input = read.nextLine();
        } while (!validateDateType(input, type)); // Solamente sale del bucle cuando el formato del número sea válido
        return input; // Devuelve el número cuando es correcto
    }

    Boolean validateDateType(String inputString, String type) {// Método que devuelve true si el número es válido
        // dependiendo del tipo
        Integer input;
        Boolean test;
        try {
            input = Integer.parseInt(inputString); // Pasamos el valor a integer, para poder comprobarlo
        } catch (NumberFormatException e) { // La catch box, captura errores, en caso de que se introduzcan caracteres
                                            // no numéricos
            System.out.println("Invalid input, try again:");
            return false;
        }

        switch (type) { // Como sabemos el tipo de dato que se está introduciendo, lo pasamos por una
                        // condición lógica, para saber si es válido
            case "Day":
                test = input >= 1 && input <= 31;// La bool es verdadera si el input está dentro de este rango
                if (!test) {
                    System.out.println("Input a valid day");
                }
                return test;

            case "Month":
                test = input >= 1 && input <= 12;
                if (!test) {
                    System.out.println("Input a valid month");
                }
                return test;

            case "Year":
                test = input >= 1000 && input <= 9999;
                if (!test) {
                    System.out.println("Input a valid year");
                }
                return test;

            default:
                System.out.println("Error: wrong type"); // Caso por defecto para evitar errores, devuelve true para
                                                         // que no se cree un bucle
                return true;
        }
    }

    // Edit, permite editar/reemplazar los eventos ya creados
    public void edit(String option) {
        if (eventList.isEmpty()) { // Comprobación para evitar que el método se ejecute si no hay eventos
            System.out.println("No events detected, create a new event to use this function");
            return;
        } else {
            stop = false; // Variable para que la función se repita
            while (!stop) { // Bucle que se repite hasta que stop sea true
                try {
                    System.out.println("Event to replace:");
                    String input = read.nextLine(); // Usaremos la variable input para realizar varias comprobaciones
                    while (input.isEmpty()) { // Comprobación para que el input no sea vacío
                        System.out.println("Input cannot be empty");
                        input = read.nextLine();
                    }
                    index = Integer.parseInt(input); // Pasamos el número introducido a integer
                    // Si index no puede ser convertido, será atrapado por NumberFormatException, lo
                    // que causará a la función que se repita

                    index--;// Como los arrays empiezan por la posición 0, le restaremos 1 al índice para
                            // que el número insertado corresponda a su posición real
                    if (index >= 0 && index < eventList.size()) {// Condicional para comprobar que index esté dentro del
                                                                 // rango
                        Event event = eventList.get(index);// Evento que vamos a editar
                        // Hacemos que el usuario introduzca los datos que vamos a cambiar
                        inputEvent(); // Función para introducir eventos con el formato correcto
                        System.out.println(
                                "Do you want to replace " + (1 + eventList.indexOf(event)) + ":[" + event.getName()
                                        + "]? (y/n)"); // Pide confirmación al usuario
                        CheckConfirm(option); // Función de confirmación
                        stop = true; // Detiene la función cuando se ha realizado correctamente
                    } else {
                        System.out.println("The event does not exist. Enter a valid event.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid Input: Please enter a valid number.");
                } catch (Exception e) {
                    System.out.println("The event does not exist. Enter a valid event.");
                }
            }
        }
    }

    // delete permite eliminar eventos existentes
    public void delete(String option) {
        if (eventList.isEmpty()) { // Comprobación para evitar que el método se ejecute si no hay eventos
            System.out.println("No events detected, create a new event to use this function");
            return;
        } else {
            stop = false; // Variable para el bucle while
            while (!stop) { // Bucle para inputs incorrectos
                try {
                    System.out.println("Event to delete:");
                    String input = read.nextLine();
                    while (input.isEmpty()) { // Comprobación para que el input no sea vacío
                        System.out.println("Input cannot be empty");
                        input = read.nextLine();
                    }
                    index = Integer.parseInt(input); // Pasamos el input a integer
                    index--; // Restamos 1 al input para que corresponda con su posición en el array
                    if (index >= 0 && index < eventList.size()) {// Condicional para comprobar que index esté dentro del
                                                                 // rango
                        Event event = eventList.get(index);// Evento que vamos a eliminar
                        System.out.println(
                                "Do you want to remove " + (1 + eventList.indexOf(event)) + ":[" + event.getName()
                                        + "]? (y/n)"); // Pide confirmación al usuario
                        CheckConfirm(option); // Función de confirmación
                        stop = true;
                    } else {
                        System.out.println("The event does not exist. Enter a valid event.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid Input: Please enter a valid number.");
                } catch (Exception e) {
                    System.out.println("Invalid Input");
                    e.printStackTrace();
                }
            }
        }
    }

    // Show utiliza los métodos de la clase Event para mostrar el contenido de cada
    // Event almacenado en el array
    public void show() {
        if (eventList.isEmpty()) { // Comprobación para evitar que el método se ejecute si no hay eventos
            System.out.println("No events detected, create a new event to use this function");
            return;
        } else {
            for (Event event : eventList) {// Realiza un bucle que se repite por cada Event en el arraylist (eventList)
                System.out.print(1 + eventList.indexOf(event) + ". "); // Muestra el número de posición del evento + 1
                                                                       // ya que en arraylist se empieza a numerar por 0
                if (event.getDescription().isEmpty()) { // Los eventos sin descripción, no tienen ":" después del nombre
                    System.out.println(event.getDate() + " - " + event.getName() + "\n");
                } else { // Imprime eventos con descripción
                    System.out.println(event.getDate() + " - " + event.getName() + ": "
                            + event.getDescription() + "\n");
                }
            }
        }
    }

    public void save() {
        try { // utilizamos try para poder usar una catch box, esto permite detectar errores
              // en la lectura y escritura
            BufferedWriter writer = new BufferedWriter(new FileWriter("PlanIt/savefile.txt"));
            // buffered writer puede escribir muchas cosas, hemos de especificarle que
            // escriba un archivo, junto con el nombre del archivo
            String events;
            for (Event event : eventList) {// bucle que se repetirá tantas veces como eventos almacenados en el array
                // utilizando las funciones get... como hacemos en show, creamos un string que
                // el writer pueda escribir en el documento
                events = (event.getDate() + ";" + event.getName() + ";" + event.getDescription() + ";\n");
                writer.write(events);
            }

            writer.close();
        } catch (IOException e) { // catch box para errores
            System.out.println("Error: ");
            e.printStackTrace();
        }
    }

    public void load() {
        try {
            File saveFile = new File("PlanIt/savefile.txt");
            if (!saveFile.exists()) { // Si savefile no existe, no intenta cargarlo.
                System.out.println("No savefile found. Starting with an empty calendar.");
                return; // El programa no intentará cargar el archivo (no ejecuta el siguiente código)
            }
            BufferedReader reader = new BufferedReader(new FileReader(saveFile));
            String line;
            while ((line = reader.readLine()) != null) {// utilizamos un bucle while para indicar al reader que lea
                // hasta que el documento no sea nulo, es decir, todo el texto
                String[] parts = line.split(";");// utilizamos la función split, sobre el string line para separar cada
                // línea de texto en distintos strings.
                // Estos strings se almacenan en un array, desde donde los asignaremos a las
                // variables date, name y description, para poder añadirlas al array principal.
                if (parts.length >= 2) {
                    String date = parts[0];
                    String name = parts[1];
                    String description = "";
                    if (parts.length > 2) { // Comprobamos si evento tiene descripción, sinó quedará en blanco.
                        description = parts[2];
                    }
                    // Introducimos las variables al array
                    Event newEvent = new Event(date, name, description);
                    eventList.add(newEvent);
                } else {
                    System.out.println("Invalid entry found in savefile: " + line);
                }
            }
            reader.close();

        } catch (IOException e) { // catch box para errores
            e.printStackTrace();
        }
    }

    // CheckConfirm sirve como una confirmación de los cambios a realizar, actuando
    // de una manera u otra dependiendo del método desde donde se llama
    void CheckConfirm(String option) {
        try {
            String input = read.nextLine();
            if (input.isEmpty()) { // Para excepciones donde no se da ningún input
                // Hacemos la comprobación de carácteres vacíos ahora, ya que no queremos darle
                // un valor vacío a la función de convertir a Carácter
                System.out.println("Operation cancelled");
            } else {
                Character answ = input.charAt(0); // Convertimos el primer valor introducido a carácter, ya que el
                                                  // programa leía enter
                if (answ == 'y' || answ == 'Y') {
                    // Utilizamos el string option que hemos declarado desde el menú para saber qué
                    // acción realizar
                    switch (option) {
                        case "1":
                            eventList.add(newEvent);
                            System.out.println("Event added");
                            break;

                        case "2":
                            eventList.set(index, newEvent);
                            System.out.println("Event modified");
                            break;

                        case "3":
                            eventList.remove(index);
                            System.out.println("Event deleted");
                            break;
                    }
                } else {
                    System.out.println("Operation cancelled");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}