package functions;

public final class MathUtil {

    public static final double EPS = 1e-10;

    private MathUtil() {}

    public static boolean equals(double a, double b) {
        if (Double.isNaN(a) || Double.isNaN(b)) return false;
        if (a == b) return true; 

        double diff = Math.abs(a - b);
        if (diff <= EPS) return true; 

        double max = Math.max(Math.abs(a), Math.abs(b));
        
        return diff <= Math.max(EPS, max * 1e-12);
    }

    public static boolean less(double a, double b) {
        return a < b && !equals(a, b);
    }

    public static boolean lessOrEquals(double a, double b) {
        return a < b || equals(a, b);
    }

    public static boolean greater(double a, double b) {
        return a > b && !equals(a, b);
    }

    public static boolean greaterOrEquals(double a, double b) {
        return a > b || equals(a, b);
    }

    public static boolean isZero(double a) {
        return equals(a, 0.0);
    }
}
