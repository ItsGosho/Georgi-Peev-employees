package models;

import java.util.Objects;

public class EmployeeCommonWorkPair {
    private int employeeId1;
    private int employeeId2;
    private int projectId;
    private int daysWorked;

    public EmployeeCommonWorkPair(int employeeId1, int employeeId2, int projectId, int daysWorked) {
        this.employeeId1 = employeeId1;
        this.employeeId2 = employeeId2;
        this.projectId = projectId;
        this.daysWorked = daysWorked;
    }

    @Override
    public boolean equals(Object object) {

        if (this == object)
            return true;

        if (!(object instanceof EmployeeCommonWorkPair other))
            return false;

        return this.projectId == other.projectId &&
                this.employeeId1 == other.employeeId1 &&
                this.employeeId2 == other.employeeId2;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.employeeId1, this.employeeId2, this.projectId);
    }

    public int getEmployeeId1() {
        return this.employeeId1;
    }

    public void setEmployeeId1(int employeeId1) {
        this.employeeId1 = employeeId1;
    }

    public int getEmployeeId2() {
        return this.employeeId2;
    }

    public void setEmployeeId2(int employeeId2) {
        this.employeeId2 = employeeId2;
    }

    public int getProjectId() {
        return this.projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public int getDaysWorked() {
        return this.daysWorked;
    }

    public void setDaysWorked(int daysWorked) {
        this.daysWorked = daysWorked;
    }

    public void increaseDaysWorked(int days) {
        this.daysWorked++;
    }

    @Override
    public String toString() {
        return "EmployeeCommonWorkPair[" +
                "employeeId1=" + this.employeeId1 + ", " +
                "employeeId2=" + this.employeeId2 + ", " +
                "projectId=" + this.projectId + ", " +
                "daysWorked=" + this.daysWorked + ']';
    }

}
