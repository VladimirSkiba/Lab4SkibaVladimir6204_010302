package functions.meta;

import functions.Function;

public class Power implements Function {
    private final Function baseFunction;
    private final double power;

    public Power(Function baseFunction, double power) {
        this.baseFunction = baseFunction;
        this.power = power;
    }

    @Override
    public double getLeftDomainBorder() {
        return baseFunction.getLeftDomainBorder();
    }

    @Override
    public double getRightDomainBorder() {
        return baseFunction.getRightDomainBorder();
    }

    @Override
    public double getFunctionValue(double x) {
        return Math.pow(baseFunction.getFunctionValue(x), power);
    }
}
