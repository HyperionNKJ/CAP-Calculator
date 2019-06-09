import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Stack;
import java.util.InputMismatchException;

public class CapCalculator {
    public static void main(String[] args) {
        Stack<CAP> capStack = new Stack();
        String command = "start";
        Scanner s = new Scanner(System.in);
        float cap = 0.0F;
		System.out.println("\nWelcome to NKJ's CapCalculator. Type \"help\" for more information.");

        while (!command.equals("quit")) {
            System.out.println("\nPlease input command: ");
            command = s.nextLine();
            String[] splitted = command.split(" "); // e.g. "undo 4" or "add CS2103T 4 A+"
            switch (splitted[0]) {
            case "undo":
            	int undoCount = splitted.length == 1 ? 1 : Integer.parseInt(splitted[1]);
            	System.out.println();
            	for (int i = 0; i < undoCount; i++) {
            		if (capStack.isEmpty()) {
                    	System.out.println("Nothing to undo");
                    	break;
                	}
                    CAP removed = capStack.pop();
	                System.out.println("Removed " + removed.getModuleCode());
            	}
                cap = computeCap(capStack);
                System.out.println("\nCAP: " + String.valueOf((cap == -1) ? 0 : cap));
                break;
            case "add":
                String moduleCode = splitted[1];
                int numOfMCs = Integer.parseInt(splitted[2]);
                String grade = splitted[3];
                try {
                	capStack.push(new CAP(moduleCode, numOfMCs, grade));
                } catch (InputMismatchException e) {
                	System.out.println("\nInvalid grade entered");
                	continue;
                }
                cap = computeCap(capStack);
                System.out.println("\nAdded " + moduleCode);
                System.out.println("CAP: " + String.valueOf((cap == -1) ? 0 : cap));
                break;
            case "list":
                System.out.print(printStack(capStack));
                break;
            case "clear":
                capStack.clear();
                System.out.println("List has been cleared");
                break;
            case "help":
                System.out.println("To add module: add <Module Code> <# of MCs> <Grade obtained>\nTo remove latest module: undo\nTo list modules: list\nTo clear list: clear\nTo quit app: quit");
                break;
            case "quit":
                break;
            default:
                System.out.println("Invalid Command");
            }
        }
    }

    private static String printStack(Stack<CAP> capStack) {
        if (capStack.isEmpty()) {
            return "No entries\n";
        }
        StringBuilder sb = new StringBuilder();
        for (CAP s : capStack) {
            sb.append(s.toString()).append("\n");
        }
        return sb.toString();
    }

    private static float computeCap(Stack<CAP> capStack) {
        float nominator = 0;
        float denominator = 0;

        for (CAP c : capStack) {
            if (c.isSU()) {
                continue;
            }
            nominator += (c.getGradePoint() * c.getNumOfMCs());
            denominator += c.getNumOfMCs();
        }

        if (denominator == 0) {
            return -1;
        } else {
            return nominator / denominator;
        }
    }
}

class CAP {
    private String moduleCode;
    private int numOfMCs;
    private String grade;
    private static final Map<String, Float> scoreSystem = new HashMap<>() { {
        put("A+", 5.0F);
        put("A", 5.0F);
        put("A-", 4.5F);
        put("B+", 4.0F);
        put("B", 3.5F);
        put("B-", 3F);
        put("C+", 2.5F);
        put("C", 2.0F);
        put("D+", 1.5F);
        put("D", 1.0F);
        put("F", 0F);
        put("SU", 0F); } };

    CAP(String moduleCode, int numOfMCs, String grade) {
    	if (!scoreSystem.containsKey(grade)) {
    		throw new InputMismatchException();
    	}
        this.moduleCode = moduleCode;
        this.numOfMCs = numOfMCs;
        this.grade = grade;
    }

	public String getModuleCode() {
        return moduleCode;
    }

    public int getNumOfMCs() {
        return numOfMCs;
    }

    public float getGradePoint() {
        return scoreSystem.get(grade);
    }

    public boolean isSU() {
        return grade.equals("SU");
    }

    @Override
    public String toString() {
        return moduleCode + " " + String.valueOf(numOfMCs) + " " + grade;
    }
}
