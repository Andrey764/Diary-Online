package Models;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class WorkInFiles {

    public static String WriteContext(Context context){
        String message = "";
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("Library.dat")))
        {
            oos.writeObject(context);
            message = "data saved successfully";
        }
        catch(Exception ex){
            message = ex.getMessage();
        }
        finally {
            return message;
        }
    }

    public static Context ReadContext(){
        Context context = null;
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream("Library.dat")))
        {
            context = (Context)ois.readObject();
        }
        catch(Exception ex){
            //context = new Context();
        }
        finally {
            return context;
        }
    }
}
