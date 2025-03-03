package models;

public record EmployeeWorkLongestPair(int employeeId1, int employeeId2, int daysWorked) {

    public int getMinEmployeeId() {
        return Math.min(this.employeeId1, this.employeeId2);
    }

    public int getMaxEmployeeId() {
        return Math.max(this.employeeId1, this.employeeId2);
    }
}
