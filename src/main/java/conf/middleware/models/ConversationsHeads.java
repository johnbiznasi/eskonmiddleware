package conf.middleware.models;

public class ConversationsHeads {
    String  teacherName;
    String  teacherKey;
    String startTime;
    String devotee;

    public ConversationsHeads(String teacherName, String teacherKey, String startTime, String devotee) {
        this.teacherName = teacherName;
        this.teacherKey = teacherKey;
        this.startTime = startTime;
        this.devotee = devotee;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public String getTeacherKey() {
        return teacherKey;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getDevotee() {
        return devotee;
    }
}


