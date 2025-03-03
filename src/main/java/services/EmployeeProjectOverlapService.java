package services;

import models.EmployeeCommonWorkPair;
import models.EmployeeWorkLongestPair;
import models.WorkAssignment;
import utils.WorkAssignmentUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class EmployeeProjectOverlapService {


    public Set<EmployeeCommonWorkPair> findAllProjectOverlapsForPair(List<WorkAssignment> workAssignments, EmployeeWorkLongestPair employeeWorkLongestPair) {
        Map<Integer, List<WorkAssignment>> projectAssignments = WorkAssignmentUtils.groupByProjectId(workAssignments);

        Map<EmployeeCommonWorkPair, EmployeeCommonWorkPair> commonWorkPairs = new HashMap();

        for (List<WorkAssignment> workAssignment : projectAssignments.values()) {

            for (int i = 0; i < workAssignment.size(); i++) {
                for (int j = i + 1; j < workAssignment.size(); j++) {

                    WorkAssignment workAssignment1 = workAssignment.get(i);
                    WorkAssignment workAssignment2 = workAssignment.get(j);

                    if (workAssignment1.employee().id() == workAssignment2.employee().id())
                        continue;

                    if (employeeWorkLongestPair.getMinEmployeeId() == WorkAssignmentUtils.getMinEmployeeId(workAssignment1, workAssignment2) &&
                            employeeWorkLongestPair.getMaxEmployeeId() == WorkAssignmentUtils.getMaxEmployeeId(workAssignment1, workAssignment2)) {
                        int overlappingWorkDays = WorkAssignmentUtils.getWorkDaysOverlap(workAssignment1, workAssignment2);

                        EmployeeCommonWorkPair employeeCommonWorkPair = new EmployeeCommonWorkPair(
                                employeeWorkLongestPair.getMinEmployeeId(),
                                employeeWorkLongestPair.getMaxEmployeeId(),
                                workAssignment1.project().id(),
                                overlappingWorkDays);

                        if (commonWorkPairs.containsKey(employeeCommonWorkPair)) {
                            commonWorkPairs.get(employeeCommonWorkPair).increaseDaysWorked(overlappingWorkDays);
                        } else {
                            commonWorkPairs.put(employeeCommonWorkPair, employeeCommonWorkPair);
                        }
                    }
                }
            }
        }

        return commonWorkPairs.keySet();
    }

}
