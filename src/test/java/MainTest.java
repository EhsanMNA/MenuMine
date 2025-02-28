import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

public class MainTest {

    public static void main(String[] args){
        //String actionString = "CHANGE-[ITEM, ITEM]STONE";
        //if (!actionString.contains("-")){
        //    System.out.println("Only action name entered!");
        //    return;
        //}
        //String actionId = actionString.split("-")[0];
        //String actionArguments = actionString.split("-")[1];
        //String actionInputs = actionArguments.replaceAll("[A-z]+]","").replace("[","");
        //String actionDetails = actionArguments.split("]")[1];
        //System.out.println("Action: "+actionString);
        //System.out.println("Id: "+actionId);
        //System.out.println("Arguments: "+actionArguments);
        //System.out.println("Inputs: "+actionInputs);
        //System.out.println("Details: "+actionDetails);
        //System.out.println("A: "+actionDetails.replace("[","").replaceAll("]",""));
//
        //String str = "Hello dear <Item> cobblestone!";
        //System.out.println(str.replaceAll("<"+"Item"+">","Block of"));
//
        //Date date = new Date();
        //System.out.println(date);
//
        //System.out.println(date.getDate());
        long startTime = System.currentTimeMillis();
        HashMap<UUID,String> sss = new HashMap<>();
        int i = 0;
        while (i < 10000000){
            i++;
            UUID uuid = UUID.randomUUID();
            sss.put(uuid,uuid.toString());
            System.out.println("["+i+"] "+uuid);
        }
        long secondTime = System.currentTimeMillis();
        System.out.println(System.currentTimeMillis()-startTime + " is the time per ms and the map size is = "+sss.size());
        System.out.println(sss.entrySet().stream().toArray()[1000]);
        System.out.println(System.currentTimeMillis()-secondTime + "is the time");
    }

}
