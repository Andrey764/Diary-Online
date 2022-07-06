package Models;

import java.util.ArrayList;
import java.util.List;

public class AddressBook {
    private List<Human> humans;
    public AddressBook(){
        humans = new ArrayList<>();
    }
    public Human GetHuman(String nameHuman){
        for (int i = 0; i < humans.size(); i++) {
            Human human = humans.get(i);
            if(human.Name.equals(nameHuman))
                return human;
        }
        return null;
    }
    public List<Human> GetHumans(){
        return humans;
    }
    public List<Human> GetHumans(String searchName){
        List<Human> buffer = new ArrayList<>();
        for (Human human: humans)
            if (human.Name.contains(searchName))
                buffer.add(human);
        return buffer;
    }
    public void AddHuman(Human human){
        humans.add(human);
    }
    public void DeleteHuman(String nameHuman){
        for (int i = 0; i < humans.size(); i++) {
            Human human = humans.get(i);
            if(human.Name.equals(nameHuman))
                humans.remove(human);
        }
    }
    public void ReplaceHuman(String nameHuman, Human newHuman){
        for (int i = 0; i < humans.size(); i++) {
            Human human = humans.get(i);
            if(human.Name.equals(nameHuman))
                human.Copy(newHuman);
        }
    }
}
