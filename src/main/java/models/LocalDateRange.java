package models;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public record LocalDateRange(LocalDate start, LocalDate end) {

    public LocalDateRange {
        Objects.requireNonNull(start, "start range must not be null");
        Objects.requireNonNull(end, "end range must not be null");

        if (end.isBefore(start))
            throw new IllegalArgumentException("end date must be on or after start date");

    }

    public boolean overlapsWith(LocalDateRange other) {
        Objects.requireNonNull(other, "other range must not be null");

        return !(this.end().isBefore(other.start()) || other.end().isBefore(this.start()));
    }

    public long getOverlapInTimeUnit(LocalDateRange other, ChronoUnit timeUnit) {
        Objects.requireNonNull(other, "other range must not be null");
        Objects.requireNonNull(other, "timeUnit must not be null");

        if (!this.overlapsWith(other)) {
            return 0L;
        }

        LocalDate overlapStart = (this.start().isAfter(other.start())) ? this.start() : other.start();
        LocalDate overlapEnd = (this.end().isBefore(other.end())) ? this.end() : other.end();

        return timeUnit.between(overlapStart, overlapEnd);
    }
}
