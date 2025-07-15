public class Person {
    private String fullName;
    private String id;
    private String address;

    public Person(String fullName, String id, String address) {
        this.fullName = fullName;
        this.id = id;
        this.address = address;
    }

    public String getFullName() {
        return fullName;
    }

    public String getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }

    @Override
    public String toString() {
        return fullName + " (ID: " + id + ")";
    }
}