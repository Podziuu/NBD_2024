
package repos;

import exceptions.LogicException;
import models.Item;

import java.util.ArrayList;
import java.util.List;

public class ItemRepository {
    private List<Item> itemRepositoryList;

    public ItemRepository() {
        itemRepositoryList = new ArrayList<>();
    }

    public Item find(String id) {
        for (Item item : itemRepositoryList) {
            if (item.getItemId().equals(id)) {
                return item;
            }
        }
        return null;
    }

    public Item get(int i) {
        if (i >= itemRepositoryList.size()) {
            return null;
        }
        return itemRepositoryList.get(i);
    }

    public void add(Item item) throws LogicException {
        if (item != null) {
            if (find(String.valueOf(item.getItemId())) != null) {
                throw new LogicException("Item with this ID already exists in repository");
            }
            itemRepositoryList.add(item);
        }
    }

    public void remove(String item) {
        if (item != null) {
            itemRepositoryList.removeIf(i -> i.equals(item));
        }
    }

    public String report() {
        StringBuilder report = new StringBuilder();
        for (Item item : itemRepositoryList) {
            report.append(item.getItemInfo());
        }
        return report.toString();
    }

    public int getSize() {
        return itemRepositoryList.size();
    }

    public List<Item> getAll() {
        return new ArrayList<>(itemRepositoryList);
    }
}
