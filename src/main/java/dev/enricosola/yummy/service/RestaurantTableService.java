package dev.enricosola.yummy.service;

import dev.enricosola.yummy.form.restauranttable.RestaurantTableCreateForm;
import dev.enricosola.yummy.form.restauranttable.RestaurantTableEditForm;
import dev.enricosola.yummy.repository.RestaurantTableRepository;
import org.springframework.transaction.annotation.Transactional;
import dev.enricosola.yummy.entity.RestaurantTable;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class RestaurantTableService {
    private final RestaurantTableRepository restaurantTableRepository;
    private RestaurantTable restaurantTable;

    public RestaurantTableService(RestaurantTableRepository restaurantTableRepository){
        this.restaurantTableRepository = restaurantTableRepository;
    }

    public void setRestaurantTable(RestaurantTable restaurantTable){
        this.restaurantTable = restaurantTable;
    }

    public RestaurantTable getRestaurantTable(){
        return this.restaurantTable;
    }

    public RestaurantTable getById(int id){
        return this.restaurantTable = this.restaurantTableRepository.findOne(id);
    }

    public List<RestaurantTable> getMultiById(List<Integer> IDList){
        return this.restaurantTableRepository.findMultiById(IDList);
    }

    public List<RestaurantTable> getAll(){
        return this.restaurantTableRepository.findAll();
    }

    public List<RestaurantTable> getAvailableList(){
        return this.restaurantTableRepository.findAvailable();
    }

    public List<RestaurantTable> getJoinableTables(){
        return this.restaurantTableRepository.findJoinable();
    }

    public boolean checkGroupJoinability(List<RestaurantTable> restaurantTableList){
        boolean hasJoinableTables = false, valid = true;
        if ( restaurantTableList != null ){
            int i = 0;
            while ( !hasJoinableTables && i < restaurantTableList.size() ){
                if ( !restaurantTableList.get(i).getJoinable() ){
                    hasJoinableTables = true;
                }
                i++;
            }
            valid = !hasJoinableTables || restaurantTableList.size() == 1;
        }
        return valid;
    }

    public RestaurantTable createFromForm(RestaurantTableCreateForm restaurantTableCreateForm){
        return this.create(restaurantTableCreateForm.getAvailableSeats(), restaurantTableCreateForm.getJoinable());
    }

    public RestaurantTable updateFromForm(RestaurantTableEditForm restaurantTableEditForm){
        return this.update(restaurantTableEditForm.getAvailableSeats(), restaurantTableEditForm.getJoinable());
    }

    public RestaurantTable create(int availableSeats, boolean joinable){
        RestaurantTable restaurantTable = new RestaurantTable();
        restaurantTable.setAvailableSeats(availableSeats);
        restaurantTable.setCreatedAt(new Date());
        restaurantTable.setUpdatedAt(new Date());
        restaurantTable.setJoinable(joinable);
        return this.restaurantTable = this.restaurantTableRepository.store(restaurantTable);
    }

    public RestaurantTable update(int availableSeats, boolean joinable){
        this.restaurantTable.setAvailableSeats(availableSeats);
        this.restaurantTable.setUpdatedAt(new Date());
        this.restaurantTable.setJoinable(joinable);
        return this.restaurantTableRepository.update(this.restaurantTable);
    }

    public void delete(){
        this.restaurantTableRepository.delete(this.restaurantTable);
        this.restaurantTable = null;
    }
}
