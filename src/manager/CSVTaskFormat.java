package manager;

import tasks.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static tasks.TaskType.*;

public class CSVTaskFormat {

    /**
     * id,type,name,status,description,epicId
     *1,TASK,Task1,NEW,Description,task1,
     *2,EPIC,Epic1,NEW,Description ep1,
     *3,SUBTASK,SubTask1,NEW,Description,task1,1
     */


    public static String toString(Task task) {
            return task.getId() + "," + task.getType() + "," + task.getName() + "," + task.getStatus() + "," + task.getDescription() + "," +
                    (task instanceof SubTask ? ((SubTask) task).getEpicId() : "") + "\n";
    }


    public static Task taskFromString(String value) {
        final String[] values = value.split(",");
        final int id = Integer.parseInt(values[0]);
        final TaskType type = TaskType.valueOf(values[1]);
        final String name = values[2];
        final TaskStatus status = TaskStatus.valueOf(values[3]);
        final String description = values[4];
        Task task = null;
        if(type.equals(SUBTASK)) {
            final Integer epicId = Integer.parseInt(values[5]);
             task = new SubTask(name, description, id, status, epicId);

        } else if (type.equals(EPIC)) {
            task = new Epic(name, description, id, status);
        } else if (type.equals(TASK)) {
            task = new Task(name, description, status, id);
        }

        return task;
    }

    public static String historyToString(HistoryManager historyManager) {
        StringBuilder sb = new StringBuilder();
        for(Task task : historyManager.getHistory()) {
            sb.append(task.getId());
            sb.append(",");
        }
        return sb.toString();
    }

    public static List<Integer> historyFromString(String value) {
        String[] temp = value.split(",");
        List<Integer> historyIds = new ArrayList<>();
       for(int i = 0; i < temp.length; i++) {
           historyIds.add(Integer.parseInt(temp[i]));
       }
        return historyIds;
    }

}
