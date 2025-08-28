package com.model.Fines;

public class Photo {
    private String path;

    public Photo(String path) {
        this.path = path;
    }

    //getters
    public String getPath() {
        return path;
    }

    //setters
    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Photo other = (Photo) obj;
        return this.path.equals(other.path);
    }

    @Override
    public int hashCode() {
        return path.hashCode();
    }

    @Override
    public String toString() {
        return "Photo [path=" + path + "]";
    }
}