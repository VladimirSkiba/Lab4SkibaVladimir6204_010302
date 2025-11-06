package functions.meta;

import functions.Function;

public class Scale implements Function {
    private final Function base;
    private final double scaleX;
    private final double scaleY;

    public Scale(Function base, double scaleX, double scaleY) {
        if (Math.abs(scaleX) < 1e-15) throw new IllegalArgumentException("scaleX не может быть 0");
        this.base = base;
        this.scaleX = scaleX;
        this.scaleY = scaleY;
    }

    @Override
    public double getLeftDomainBorder() {
        return base.getLeftDomainBorder() * scaleX;
    }

    @Override
    public double getRightDomainBorder() {
        return base.getRightDomainBorder() * scaleX;
    }

    @Override
    public double getFunctionValue(double x) {
        return scaleY * base.getFunctionValue(x / scaleX);
    }
}
