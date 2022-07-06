package Models;

import java.util.ArrayList;
import java.util.List;

public class Stores {
    private List<Store> stores;

    public Stores() {
        stores = new ArrayList<>();
    }

    public Store GetStore(String titleStore) {
        for (int i = 0; i < stores.size(); i++) {
            Store store = stores.get(i);
            if (store.Title.equals(titleStore))
                return store;
        }
        return null;
    }

    public List<Store> GetStores() {
        return stores;
    }

    public List<Store> GetStores(String searchParam, String typeStore) {
        List<Store> buffer = new ArrayList<>();
        for (Store store : stores)
            if (store.Title.contains(searchParam) && store.Category.equals(typeStore))
                buffer.add(store);
        return buffer;
    }

    public void AddStore(Store store) {
        stores.add(store);
    }

    public void DeleteStore(String nameStore) {
        for (int i = 0; i < stores.size(); i++) {
            Store store = stores.get(i);
            if (store.Title.equals(nameStore))
                stores.remove(store);
        }
    }

    public void ReplaceStore(String nameStore, Store newStore) {
        for (int i = 0; i < stores.size(); i++) {
            Store store = stores.get(i);
            if (store.Title.equals(nameStore))
                store.Copy(newStore);
        }
    }

    public List<String> GetListStoreType() {
        List<String> buffer = new ArrayList<>();
        for (Store store : stores)
            if (!buffer.contains(store.Category))
                buffer.add(store.Category);
        return buffer;
    }
}

