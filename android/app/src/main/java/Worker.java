public class Worker extends User {
    private String companyName;
    private String jobTitle;

    public Worker() {
        super(User.TYPE_WORKER);
    }

    public Worker(String firstName, String lastName, String companyName, String jobTitle) {
        super(User.TYPE_WORKER, firstName, lastName);
        this.companyName = companyName;
        this.jobTitle = jobTitle;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }
}
