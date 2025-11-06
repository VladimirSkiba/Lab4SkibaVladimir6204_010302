package functions.meta;

import functions.Function;

public class Shift implements Function {
    private final Function base;
    private final double shiftX;
    private final double shiftY;

    public Shift(Function base, double shiftX, double shiftY) {
        this.base = base;
        this.shiftX = shiftX;
        this.shiftY = shiftY;
    }

    @Override
    public double getLeftDomainBorder() {
        return base.getLeftDomainBorder() + shiftX;
    }

    @Override
    public double getRightDomainBorder() {
        return base.getRightDomainBorder() + shiftX;
    }

    @Override
    public double getFunctionValue(double x) {
        return base.getFunctionValue(x - shiftX) + shiftY;
    }
}
