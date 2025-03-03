package models;

import java.util.Objects;

public record WorkAssignment(Employee employee, Project project, LocalDateRange workPeriod) {

    public WorkAssignment {
        Objects.requireNonNull(employee, "employee must not be null");
        Objects.requireNonNull(project, "project must not be null");
        Objects.requireNonNull(workPeriod, "workPeriod must not be null");
    }
}
