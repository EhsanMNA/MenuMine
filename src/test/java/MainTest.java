import java.util.Date;

public class MainTest {

    public static void main(String[] args){
        String actionString = "CHANGE-[ITEM, ITEM]STONE";
        if (!actionString.contains("-")){
            System.out.println("Only action name entered!");
            return;
        }
        String actionId = actionString.split("-")[0];
        String actionArguments = actionString.split("-")[1];
        String actionInputs = actionArguments.replaceAll("[A-z]+]","").replace("[","");
        String actionDetails = actionArguments.split("]")[1];
        System.out.println("Action: "+actionString);
        System.out.println("Id: "+actionId);
        System.out.println("Arguments: "+actionArguments);
        System.out.println("Inputs: "+actionInputs);
        System.out.println("Details: "+actionDetails);
        System.out.println("A: "+actionDetails.replace("[","").replaceAll("]",""));

        String str = "Hello dear <Item> cobblestone!";
        System.out.println(str.replaceAll("<"+"Item"+">","Block of"));

        Date date = new Date();
        System.out.println(date);

        System.out.println(date.getDate());
    }

}
