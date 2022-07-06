package controllers;

import Models.Store;
import Models.Stores;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/store")
public class StoresController {
    Stores stores;
    public StoresController() {
        this.stores = new Stores();
        stores.AddStore(new Store("Rozetka","ул. Лермонтова, 22В",
                "По техническим причинам линии поддержки не работают. Заказывайте онлайн.",
                "cab@rozetka.com.ua","https://rozetka.com.ua","universal",
                "ROZETKA – самый большой онлайн-ритейлер в стране. С 2005 года мы воплощаем " +
                "маленькие мечты и грандиозные планы миллионов людей. У нас можно найти буквально все. " +
                "Мы продаем по справедливой цене и предоставляем гарантию, так как считаем, что " +
                "онлайн-шопинг должен быть максимально удобным и безопасным. И каждый раз, когда" +
                " кто-то нажимает «Купить», мы понимаем, что делаем нужное дело."));
        stores.AddStore(new Store("Comfy","пр-т Гагаріна, 54в", "0-800-303-505",
                "info@comfy.ua","https://comfy.ua","home appliances",
                "Comfy - сеть магазинов бытовой техники и электроники"));
    }
    @GetMapping("/main")
    public String Main(Model model){
        model.addAttribute("stores", stores.GetStores());
        return "index";
    }

    @PostMapping("/search")
    public String Main(Model model, @RequestParam("searchStore") String searchStore,
        @RequestParam("typeStore") String typeStore){
        model.addAttribute("stores", stores.GetStores(searchStore, typeStore));
        return "index";
    }

    @GetMapping("/delete")
    public String DeleteHuman(Model model, @RequestParam("name") String name){
        stores.DeleteStore(name);
        return Main(model);
    }

    @GetMapping("add")
    public String AddHuman(){
        return "addStore";
    }

    @GetMapping("edit")
    public String EditHuman(Model model, @RequestParam("name") String name){
        model.addAttribute("store", stores.GetStore(name));
        return "editStore";
    }

    @GetMapping("show")
    public String ShowHuman(Model model, @RequestParam("name") String name){
        model.addAttribute("store", stores.GetStore(name));
        return "showStore";
    }

    @PostMapping("/editSave")
    public String SaveHuman(Model model, @RequestParam("oldTitle") String oldTitle,
    @RequestParam("title") String title, @RequestParam("address") String address,
    @RequestParam("phone") String phone,@RequestParam("email") String email,
    @RequestParam("link") String link,@RequestParam("category") String category,
                            @RequestParam("description") String description){
        stores.ReplaceStore(oldTitle, new Store(title,address,phone,email,link,category,description));
        model.addAttribute("stores", stores.GetStores());
        return "index";
    }

    @PostMapping("/save")
    public String EditSaveHuman(Model model, @RequestParam("title")
    String title, @RequestParam("address") String address, @RequestParam("phone")
    String phone,@RequestParam("email") String email, @RequestParam("link") String link,
    @RequestParam("category") String category, @RequestParam("description") String description){
        stores.AddStore(new Store(title,address,phone,email,link,category,description));
        model.addAttribute("stores", stores.GetStores());
        return "index";
    }
}
