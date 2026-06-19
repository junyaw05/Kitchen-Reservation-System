package utility;

import java.util.*;

public class ConsoleUtil {	
    public static void pause() {
        System.out.println("Press Enter to continue...");
        Scanner input = new Scanner(System.in);
        input.nextLine();
    }
}
