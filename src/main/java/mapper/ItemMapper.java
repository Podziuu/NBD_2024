package mapper;

import com.datastax.oss.driver.api.mapper.annotations.DaoFactory;
import com.datastax.oss.driver.api.mapper.annotations.DaoKeyspace;
import com.datastax.oss.driver.api.mapper.annotations.DaoTable;
import com.datastax.oss.driver.api.mapper.annotations.Mapper;
import dao.ItemDao;

@Mapper
public interface ItemMapper {
    @DaoFactory
    ItemDao itemDao(@DaoKeyspace String keyspace, @DaoTable String table);
    @DaoFactory
    ItemDao itemDao();
}
