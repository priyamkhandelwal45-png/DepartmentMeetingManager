public class Meeting {
    private String id;
    private String title;
    private String department;
    private String date;
    private String time;
    private String room;

    public Meeting(String id, String title, String department, String date, String time, String room) {
        this.id = id;
        this.title = title;
        this.department = department;
        this.date = date;
        this.time = time;
        this.room = room;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }
    public String getRoom() { return room; }
    public void setRoom(String room) { this.room = room; }
}
