package functions;

import java.io.Serializable;

public class TabulatedFunctionData implements Serializable {
    private static final long serialVersionUID = 1L;

    private final FunctionPoint[] points;

    public TabulatedFunctionData(FunctionPoint[] points) {
        this.points = new FunctionPoint[points.length];
        for (int i = 0; i < points.length; i++) this.points[i] = new FunctionPoint(points[i]);
    }

    public int getPointsCount() { return points.length; }
    public FunctionPoint getPoint(int i) { return new FunctionPoint(points[i]); }
}
