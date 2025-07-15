import java.util.HashSet;
import java.util.Set;

public class Fine {
    private double amount;
    private int scoring;
    private Set<Photo> photos; // no duplicates
    private EventGeolocation eventGeolocation;
    private InfractionType infractionType;

    public Fine(double amount, int scoring, EventGeolocation eventGeolocation, InfractionType infractionType) {
        this.amount = amount;
        this.scoring = scoring;
        this.photos = new HashSet<>();
        this.eventGeolocation = eventGeolocation;
        this.infractionType = infractionType;
    }

    public void addPhoto(Photo photo) {
        photos.add(photo);
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getScoring() {
        return scoring;
    }

    public void setScoring(int scoring) {
        this.scoring = scoring;
    }

    public Set<Photo> getPhotos() {
        return photos;
    }

    public EventGeolocation getEventGeolocation() {
        return eventGeolocation;
    }

    public void setEventGeolocation(EventGeolocation eventGeolocation) {
        this.eventGeolocation = eventGeolocation;
    }

    public InfractionType getInfractionType() {
        return infractionType;
    }

    public void setInfractionType(InfractionType infractionType) {
        this.infractionType = infractionType;
    }

    @Override
    public String toString() {
        return "Fine [amount=" + amount + ", scoring=" + scoring + "]";
    }
}
