package services;

import models.EmployeeWorkLongestPair;
import models.WorkAssignment;
import utils.WorkAssignmentUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LongestWorkPairService {

    public EmployeeWorkLongestPair findPairWithLongestOverlap(List<WorkAssignment> workAssignments) {
        Map<Integer, List<WorkAssignment>> projectAssignments = WorkAssignmentUtils.groupByProjectId(workAssignments);

        Map<EmployeePair, Integer> pairOverlapMap = this.computeCommonEmployeesTotalWorkingDays(projectAssignments);

        return this.findMaxOverlapPair(pairOverlapMap);
    }

    private Map<EmployeePair, Integer> computeCommonEmployeesTotalWorkingDays(Map<Integer, List<WorkAssignment>> projectAssignments) {
        Map<EmployeePair, Integer> pairOverlapMap = new HashMap<>();

        for (List<WorkAssignment> assignmentsInProject : projectAssignments.values()) {

            for (int i = 0; i < assignmentsInProject.size(); i++) {
                for (int j = i + 1; j < assignmentsInProject.size(); j++) {

                    WorkAssignment workAssignment1 = assignmentsInProject.get(i);
                    WorkAssignment workAssignment2 = assignmentsInProject.get(j);

                    if (workAssignment1.employee().id() == workAssignment2.employee().id())
                        continue;

                    if (workAssignment1.workPeriod().overlapsWith(workAssignment2.workPeriod())) {
                        int overlapDays = WorkAssignmentUtils.getWorkDaysOverlap(workAssignment1, workAssignment2);

                        EmployeePair employeePair = this.createOrderedPair(workAssignment1.employee().id(), workAssignment2.employee().id());

                        int currentOverlap = pairOverlapMap.getOrDefault(employeePair, 0);
                        int newTotal = currentOverlap + overlapDays;

                        pairOverlapMap.put(employeePair, newTotal);
                    }
                }
            }
        }

        return pairOverlapMap;
    }

    private EmployeeWorkLongestPair findMaxOverlapPair(Map<EmployeePair, Integer> commonEmployeesTotalWorkingDays) {

        if (commonEmployeesTotalWorkingDays.isEmpty())
            return null;

        int maxCommonWorkingDays = 0;
        EmployeePair bestEmployeePair = null;

        for (Map.Entry<EmployeePair, Integer> entry : commonEmployeesTotalWorkingDays.entrySet()) {

            int commonWorkingDays = entry.getValue();

            if (commonWorkingDays > maxCommonWorkingDays) {
                maxCommonWorkingDays = commonWorkingDays;
                bestEmployeePair = entry.getKey();
            }

        }

        if (bestEmployeePair == null)
            return null;

        return new EmployeeWorkLongestPair(bestEmployeePair.empId1, bestEmployeePair.empId2, maxCommonWorkingDays);
    }

    private EmployeePair createOrderedPair(int employeeId1, int employeeId2) {
        int minEmployeeId = Math.min(employeeId1, employeeId2);
        int maxEmployeeId = Math.max(employeeId1, employeeId2);
        return new EmployeePair(minEmployeeId, maxEmployeeId);
    }

    private record EmployeePair(int empId1, int empId2) {
    }
}
