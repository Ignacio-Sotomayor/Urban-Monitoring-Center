package model.Fines;

public class EventGeolocation {
 private String id;
 private String dateHour;
 private String address;

 public EventGeolocation(String id, String dateHour, String address) {
  this.id = id;
  this.dateHour = dateHour;
  this.address = address;
 }

 public String getId() {
  return id;
 }

 public void setId(String id) {
  this.id = id;
 }

 public String getDateHour() {
  return dateHour;
 }

 public void setDateHour(String dateHour) {
  this.dateHour = dateHour;
 }

 public String getAddress() {
  return address;
 }

 public void setAddress(String address) {
  this.address = address;
 }

 @Override
 public String toString() {
  return "EventGeolocation [id=" + id + ", dateHour=" + dateHour + ", address=" + address + "]";
 }
}