package functions;

public class FunctionPointIndexOutOfBoundsException extends IndexOutOfBoundsException {
    public FunctionPointIndexOutOfBoundsException() {
        super();
    }

    public FunctionPointIndexOutOfBoundsException(String message) {
        super(message);
    }

    public FunctionPointIndexOutOfBoundsException(int index) {
        super("Index out of bounds: " + index);
    }

    public FunctionPointIndexOutOfBoundsException(int index, int size) {
        super("Index " + index + " out of bounds for size " + size);
    }
}