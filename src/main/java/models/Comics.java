package models;

import com.datastax.oss.driver.api.mapper.annotations.*;
import java.util.UUID;

@Entity
@CqlName("items_by_id")
public class Comics extends Item {
    @CqlName("page_number")
    private int pageNumber;

    public Comics(UUID id, int basePrice, String itemName, int pageNumber) {
        super(id, basePrice, itemName);
        this.itemType = "comics";
        this.pageNumber = pageNumber;
    }

    public Comics() {}

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }
}