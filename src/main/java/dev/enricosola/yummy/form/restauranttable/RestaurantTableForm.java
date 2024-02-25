package dev.enricosola.yummy.form.restauranttable;

public interface RestaurantTableForm {
    void setAvailableSeats(int availableSeats);

    int getAvailableSeats();

    void setJoinable(boolean joinable);

    boolean getJoinable();
}
