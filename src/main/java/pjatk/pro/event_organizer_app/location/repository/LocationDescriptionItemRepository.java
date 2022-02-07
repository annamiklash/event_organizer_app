package pjatk.pro.event_organizer_app.location.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pjatk.pro.event_organizer_app.location.model.LocationDescriptionItem;

import java.util.List;

@Repository
public interface LocationDescriptionItemRepository extends JpaRepository<LocationDescriptionItem, String> {

     @Query("select di from description_item di where di.id = :name ")
     LocationDescriptionItem getLocationDescriptionItemByName(@Param("name") String name);

     @Query("Select distinct di from description_item di")
     List<LocationDescriptionItem> findAllDistinct();


}
