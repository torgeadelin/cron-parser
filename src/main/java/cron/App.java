package cron;

import exceptions.InvalidInputException;
import exceptions.ParsingException;
import java.util.*;

class Field {
    public int min;
    public int max;
    public String name;
}

class Minute extends Field {
    public Minute() {
        name = "minute";
        min = 0;
        max = 59;
    }
}

class Hour extends Field {
    public Hour() {
        name = "hour";
        min = 0;
        max = 23;
    }
}

class DayMonth extends Field {
    public DayMonth() {
        name = "dayMonth";
        min = 1;
        max = 31;
    }
}

class Month extends Field {
    public Month() {
        name = "month";
        min = 1;
        max = 12;
    }
}

class DayWeek extends Field {
    public DayWeek() {
        name = "dayWeek";
        min = 0;
        max = 6;
    }
}

/**
 * The Cron program implements an application that simply parses a cron input
 * and returns a table with minute, hour, day(month) month, day(week) and
 * command.
 *
 * @author Catalin Adelin Torge
 * @version 1.0
 * @since 2020-01-29
 */
public class App {
    private String minute;
    private String hour;
    // Stores the day of month
    private String dayM;
    private String month;
    // Stores the day of week
    private String dayW;

    // Table containing all mentioned fields.
    private HashMap<String, String> table = new HashMap<>();

    // Parses both parameters of the program
    // First parameter is the time input, and the second one is the command
    public void parse(String[] timeInput, String command) throws ParsingException {
        if (timeInput.length < 5 || command.isEmpty()) {
            throw new ParsingException("Invalid number of time fields");
        }
        this.minute = timeInput[0];
        this.hour = timeInput[1];
        this.dayM = timeInput[2];
        this.month = timeInput[3];
        this.dayW = timeInput[4];
        this.table.put("command", command);

        try {
            String minuteOutput = parseField(new Minute(), this.minute);
            this.table.put("minute", minuteOutput);

            String hourOutput = parseField(new Hour(), this.hour);
            this.table.put("hour", hourOutput);

            String dayMOutput = parseField(new DayMonth(), this.dayM);
            this.table.put("dayM", dayMOutput);

            String monthOutput = parseField(new Month(), this.month);
            this.table.put("month", monthOutput);

            String dayWOutput = parseField(new DayWeek(), this.dayW);
            this.table.put("dayW", dayWOutput);

        } catch (InvalidInputException e) {
            System.out.println(e);
            System.exit(0);
        }
    }

    // Parses a specific field in the timeInput part.
    // This funciton takes a field type which can be minute, hour, etc and the
    // actual input field.
    // It returns the generated intervals, as mentioend in the brief.

    // Given the standard definition of cron, there are 5 possible cases:
    // "-" refers to a range of numbers
    // "/*" refers to a step function
    // "," refers to a sequence
    // "*" refers to any number
    // "n" refers to a single number n

    // All rules must follow each type's min and maximum values.
    // Ex 1-5 for minute means 1 2 3 4 5, but 1-60 would throw an exception as 60 is
    // > Minute.max
    public String parseField(Field field, String input) throws InvalidInputException {
        StringBuilder minuteOutput = new StringBuilder();
        // Range
        if (input.contains("-")) {
            String[] range = input.split("-");
            try {
                int start = Integer.parseInt(range[0]);
                int end = Integer.parseInt(range[1]);
                if (!(start >= field.min && end <= field.max && start < end)) {
                    throw new InvalidInputException("Invalid " + field.name + " range format");
                }
                for (int i = start; i <= end - 1; i++) {
                    minuteOutput.append(i + " ");
                }
                minuteOutput.append(end);
            } catch (NumberFormatException e) {
                System.out.println(e);
                System.exit(0);
            }
            // Step values
        } else if (input.contains("*/")) {
            try {
                int step = Integer.parseInt(input.substring(2, input.length()));
                if (!(step >= field.min && step <= field.max)) {
                    throw new InvalidInputException("Invalid step " + field.name + " format");
                }
                for (int i = field.min; i < field.max; i += step) {
                    minuteOutput.append(i + " ");
                }
                minuteOutput.deleteCharAt(minuteOutput.length() - 1);
            } catch (NumberFormatException e) {
                System.out.println(e);
                System.exit(0);
            }
            // Sequence
        } else if (input.contains(",")) {
            try {
                String[] sequence = input.split(",");
                for (String s : sequence) {
                    int i = Integer.parseInt(s);
                    if (!(i >= field.min && i <= field.max))
                        throw new InvalidInputException("Invalid " + field.name + " format");
                }

                minuteOutput = new StringBuilder(String.join(" ", sequence));
            } catch (NumberFormatException e) {
                System.out.println(e);
                System.exit(0);
            }
            // Any
        } else if (input.equals("*")) {
            for (int i = field.min; i < field.max - 1; i++) {
                minuteOutput.append(i + " ");
            }
            minuteOutput.append(field.max);
            // Single value
        } else {
            try {
                int i = Integer.parseInt(input);
                if (!(i >= field.min && i <= field.max))
                    throw new InvalidInputException("Invalid " + field.name + " format");
                minuteOutput.append(input);
            } catch (NumberFormatException e) {
                System.out.println(e);
                System.exit(0);
            }
        }
        return minuteOutput.toString();
    }

    public void printHashTable() {
        System.out.println();
        System.out.println(table.toString());
    }

    // Prints a formatted table with all time fields and the command
    public void printOutput() {
        System.out.println();
        for (int i = 0; i < 64; i++)
            System.out.print("-");
        System.out.println();
        for (String k : this.table.keySet()) {
            System.out.print(k);
            System.out.print("\t");
            System.out.print("\t");
            System.out.println(this.table.get(k));
        }
        for (int i = 0; i < 64; i++)
            System.out.print("-");
        System.out.println();
        System.out.println();
    }

    public static void main(String[] args) {
        App cron = new App();

        if (args.length > 1) {
            String cronField = args[0];
            String command = args[1];
            try {
                cron.parse(cronField.split(" "), command);
                cron.printOutput();
            } catch (ParsingException e) {
                System.out.println(e);
            }
        } else {
            System.out.println("Please provide a valid input...");
        }

    }
}