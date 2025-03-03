package services;

import models.Employee;
import models.LocalDateRange;
import models.Project;
import models.WorkAssignment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class WorkAssignmentCSVReader {

    private static final DateTimeFormatter[] SUPPORTED_FORMATS = {
            DateTimeFormatter.ofPattern("yyyy-MM-dd"),
            DateTimeFormatter.ofPattern("dd/MM/yyyy"),
            DateTimeFormatter.ofPattern("MM/dd/yyyy"),
            DateTimeFormatter.ofPattern("dd-MM-yyyy"),
            DateTimeFormatter.ofPattern("MM-dd-yyyy"),
    };

    private static final int EXPECTED_ARGUMENTS_CSV_LENGTH = 4;

    private static final ZoneId DATE_ZONE = ZoneId.of("UTC");


    public List<WorkAssignment> read(File csvFile) {
        this.validateFile(csvFile);

        List<WorkAssignment> assignments = new ArrayList<>();

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(csvFile))) {
            String line;
            boolean skipHeader = true;

            while ((line = bufferedReader.readLine()) != null) {

                if (skipHeader) {
                    skipHeader = false;
                    continue;
                }

                WorkAssignment workAssignment = this.parseWorkAssignmentFromCSVLine(line);
                assignments.add(workAssignment);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to read CSV file: " + csvFile.getAbsolutePath(), e);
        }

        return assignments;
    }

    private void validateFile(File file) {
        Objects.requireNonNull(file, "File cannot be null");

        if (!file.exists())
            throw new IllegalArgumentException("File does not exist: " + file.getAbsolutePath());

        if (!file.isFile())
            throw new IllegalArgumentException("Not a file: " + file.getAbsolutePath());

        if (!file.getName().toLowerCase().endsWith(".csv")) {
            throw new IllegalArgumentException("File is not a CSV file: " + file.getAbsolutePath());
        }
    }

    private WorkAssignment parseWorkAssignmentFromCSVLine(String csvLine) {

        if (csvLine == null || csvLine.isBlank())
            throw new IllegalArgumentException("CSV line is null or empty");

        String[] columns = csvLine.split(",");

        if (columns.length < EXPECTED_ARGUMENTS_CSV_LENGTH)
            throw new IllegalArgumentException("Invalid CSV line, expected " + EXPECTED_ARGUMENTS_CSV_LENGTH + " columns but got " + columns.length);

        int employeeId = this.parseEmployeeId(columns[0]);
        int projectId = this.parseProjectId(columns[1]);

        LocalDate dateFrom = this.parseDate(columns[2]);
        LocalDate dateTo = this.parseDate(columns[3]);

        LocalDateRange range = new LocalDateRange(dateFrom, dateTo);

        Employee employee = new Employee(employeeId);
        Project project = new Project(projectId);
        return new WorkAssignment(employee, project, range);
    }


    private int parseEmployeeId(String employeeId) {
        Objects.requireNonNull(employeeId, "employeeId must not be null");

        employeeId = employeeId.trim();
        try {
            return Integer.parseInt(employeeId);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid employee id: " + employeeId, e);
        }
    }

    private int parseProjectId(String projectId) {
        Objects.requireNonNull(projectId, "employeeId must not be null");

        projectId = projectId.trim();
        try {
            return Integer.parseInt(projectId);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid project id: " + projectId, e);
        }
    }

    private LocalDate parseDate(String date) {

        if (date == null || date.isBlank())
            throw new IllegalArgumentException("date must not be null or empty");

        String dateTrimmed = date.trim();

        if (dateTrimmed.equalsIgnoreCase("NULL")) {
            return LocalDate.now(DATE_ZONE);
        }

        for (DateTimeFormatter dateTimeFormatter : SUPPORTED_FORMATS) {
            try {
                return LocalDate.parse(dateTrimmed, dateTimeFormatter);
            } catch (DateTimeParseException dateTimeParseException) {
            }
        }

        throw new IllegalArgumentException("Unsupported date format for: " + dateTrimmed);
    }
}
