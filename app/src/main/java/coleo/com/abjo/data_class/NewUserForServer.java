package coleo.com.abjo.data_class;

public class NewUserForServer extends User {

    private String introduceCode;
    private String studentId;

    public NewUserForServer(String firstName, String lastName, String number, boolean isWoman
            , String introduceCode, String studentId) {
        super(firstName, lastName, number, isWoman);
        this.introduceCode = introduceCode;
        this.studentId = studentId;
    }

    public void setIntroduceCode(String introduceCode) {
        this.introduceCode = introduceCode;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getIntroduceCode() {
        return introduceCode;
    }

    public String getStudentId() {
        return studentId;
    }
}
