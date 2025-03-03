package utils;

import models.WorkAssignment;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class WorkAssignmentUtils {

    public static Map<Integer, List<WorkAssignment>> groupByProjectId(List<WorkAssignment> workAssignments) {
        Map<Integer, List<WorkAssignment>> projectAssignments = new HashMap<>();

        for (WorkAssignment workAssignment : workAssignments) {
            int projectId = workAssignment.project().id();

            if (!projectAssignments.containsKey(projectId)) {
                projectAssignments.put(projectId, new ArrayList<>());
            }

            projectAssignments.get(projectId).add(workAssignment);
        }

        return projectAssignments;
    }

    public static int getMinEmployeeId(WorkAssignment workAssignment1, WorkAssignment workAssignment2) {
        return Math.min(workAssignment1.employee().id(), workAssignment2.employee().id());
    }

    public static int getMaxEmployeeId(WorkAssignment workAssignment1, WorkAssignment workAssignment2) {
        return Math.max(workAssignment1.employee().id(), workAssignment2.employee().id());
    }


    public static int getWorkDaysOverlap(WorkAssignment workAssignment1, WorkAssignment workAssignment2) {
        return (int) (workAssignment1.workPeriod().getOverlapInTimeUnit(workAssignment2.workPeriod(), ChronoUnit.DAYS)) + 1;
    }
}
