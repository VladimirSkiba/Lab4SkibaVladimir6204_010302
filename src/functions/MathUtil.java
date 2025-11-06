package functions;

public final class MathUtil {
    /**
     * Absolute epsilon for near-zero comparisons. Kept small but not too small to avoid
     * underflow issues for typical tabulation ranges.
     */
    public static final double EPS = 1e-10;

    private MathUtil() {}

    /**
     * Robust equality check for doubles: combines an absolute tolerance with a
     * tiny relative tolerance based on magnitude.
     */
    public static boolean equals(double a, double b) {
        if (Double.isNaN(a) || Double.isNaN(b)) return false;
        if (a == b) return true; // handles infinities and exact equality

        double diff = Math.abs(a - b);
        if (diff <= EPS) return true; // absolute tolerance

        double max = Math.max(Math.abs(a), Math.abs(b));
        // relative tolerance: allow differences on the order of 1e-12 of the magnitude
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
