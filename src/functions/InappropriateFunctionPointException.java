package functions;

public class InappropriateFunctionPointException extends RuntimeException {
    public InappropriateFunctionPointException() {
        super();
    }

    public InappropriateFunctionPointException(String message) {
        super(message);
    }

    public InappropriateFunctionPointException(double x, double existingX) {
        super("Point with x=" + x + " cannot be added. Point with x=" + existingX + " already exists");
    }

    public InappropriateFunctionPointException(double x, double leftX, double rightX) {
        super("Point with x=" + x + " would break ordering. Must be between " + leftX + " and " + rightX);
    }
}