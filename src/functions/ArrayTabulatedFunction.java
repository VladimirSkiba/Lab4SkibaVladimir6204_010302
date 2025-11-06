package functions;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Arrays;

public class ArrayTabulatedFunction implements TabulatedFunction, Externalizable {
    private FunctionPoint[] points;
    private int pointsCount;

    public ArrayTabulatedFunction(double leftX, double rightX, int pointsCount) {
        if (leftX >= rightX) {
            throw new IllegalArgumentException("Левая граница должна быть меньше правой: " + leftX + " >= " + rightX);
        }
        if (pointsCount < 2) {
            throw new IllegalArgumentException("Количество точек должно быть не менее 2: " + pointsCount);
        }

        this.pointsCount = pointsCount;
        this.points = new FunctionPoint[pointsCount + 5];

        double step = (rightX - leftX) / (pointsCount - 1);
        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + i * step;
            points[i] = new FunctionPoint(x, 0);
        }
    }

    public ArrayTabulatedFunction(double leftX, double rightX, double[] values) {
        if (leftX >= rightX) {
            throw new IllegalArgumentException("Левая граница должна быть меньше правой: " + leftX + " >= " + rightX);
        }
        if (values.length < 2) {
            throw new IllegalArgumentException("Количество точек должно быть не менее 2: " + values.length);
        }

        this.pointsCount = values.length;
        this.points = new FunctionPoint[pointsCount + 5];

        double step = (rightX - leftX) / (pointsCount - 1);
        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + i * step;
            points[i] = new FunctionPoint(x, values[i]);
        }
    }

    public ArrayTabulatedFunction(FunctionPoint[] points) {
        if (points == null) {
            throw new IllegalArgumentException("Массив точек не может быть null");
        }
        if (points.length < 2) {
            throw new IllegalArgumentException("Количество точек должно быть не менее 2: " + points.length);
        }

        for (int i = 0; i < points.length - 1; i++) {
            if (!MathUtil.less(points[i].getX(), points[i + 1].getX())) {
                throw new IllegalArgumentException("Точки не упорядочены по X или содержат дубликаты");
            }
        }

        this.pointsCount = points.length;
        this.points = new FunctionPoint[pointsCount + 5];

        for (int i = 0; i < pointsCount; i++) {
            this.points[i] = new FunctionPoint(points[i]);
        }
        // ensure trailing nulls for consistent state
        if (this.pointsCount < this.points.length) {
            Arrays.fill(this.points, this.pointsCount, this.points.length, null);
        }
    }

    public double getLeftDomainBorder() {
        return points[0].getX();
    }

    public double getRightDomainBorder() {
        return points[pointsCount - 1].getX();
    }

    public double getFunctionValue(double x) {
        if (MathUtil.less(x, getLeftDomainBorder()) || MathUtil.greater(x, getRightDomainBorder())) {
            return Double.NaN;
        }

        for (int i = 0; i < pointsCount; i++) {
            if (MathUtil.equals(x, points[i].getX())) {
                return points[i].getY();
            }
        }

        for (int i = 0; i < pointsCount - 1; i++) {
            double x1 = points[i].getX();
            double x2 = points[i + 1].getX();

            if (MathUtil.greaterOrEquals(x, x1) && MathUtil.lessOrEquals(x, x2)) {
                double y1 = points[i].getY();
                double y2 = points[i + 1].getY();
                return y1 + (y2 - y1) * (x - x1) / (x2 - x1);
            }
        }

        return Double.NaN;
    }

    public int getPointsCount() {
        return pointsCount;
    }

    public FunctionPoint getPoint(int index) {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс " + index + " вне диапазона [0, " + (pointsCount - 1) + "]");
        }
        return new FunctionPoint(points[index]);
    }

    public void setPoint(int index, FunctionPoint point) {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException(index, pointsCount);
        }

    if (index > 0 && !MathUtil.greater(point.getX(), points[index - 1].getX())) {
            throw new InappropriateFunctionPointException(
                    "X координата " + point.getX() + " должна быть больше предыдущей " + points[index - 1].getX());
        }
    if (index < pointsCount - 1 && !MathUtil.less(point.getX(), points[index + 1].getX())) {
            throw new InappropriateFunctionPointException(
                    "X координата " + point.getX() + " должна быть меньше следующей " + points[index + 1].getX());
        }

        points[index] = new FunctionPoint(point);
    }

    public double getPointX(int index) {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс " + index + " вне диапазона [0, " + (pointsCount - 1) + "]");
        }
        return points[index].getX();
    }

    public void setPointX(int index, double x) {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException(index, pointsCount);
        }
    if (index > 0 && !MathUtil.greater(x, points[index - 1].getX())) {
            throw new InappropriateFunctionPointException(
                    "X координата " + x + " должна быть больше предыдущей " + points[index - 1].getX());
        }
    if (index < pointsCount - 1 && !MathUtil.less(x, points[index + 1].getX())) {
            throw new InappropriateFunctionPointException(
                    "X координата " + x + " должна быть меньше следующей " + points[index + 1].getX());
        }

        points[index].setX(x);
    }

    public double getPointY(int index) {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс " + index + " вне диапазона [0, " + (pointsCount - 1) + "]");
        }
        return points[index].getY();
    }

    public void setPointY(int index, double y) {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс " + index + " вне диапазона [0, " + (pointsCount - 1) + "]");
        }
        points[index].setY(y);
    }

    public void deletePoint(int index) {
        if (pointsCount <= 2) {
            throw new IllegalStateException("Невозможно удалить точку: должно остаться минимум 2 точки");
        }

        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс " + index + " вне диапазона [0, " + (pointsCount - 1) + "]");
        }

        System.arraycopy(points, index + 1, points, index, pointsCount - index - 1);
        pointsCount--;
        points[pointsCount] = null;
    }

    public void addPoint(FunctionPoint point) {
        int insertIndex = 0;
        while (insertIndex < pointsCount && MathUtil.less(points[insertIndex].getX(), point.getX())) {
            insertIndex++;
        }

        if (insertIndex < pointsCount && MathUtil.equals(points[insertIndex].getX(), point.getX())) {
            throw new InappropriateFunctionPointException("Точка с X=" + point.getX() + " уже существует");
        }

        if (pointsCount == points.length) {
            int newCapacity = points.length * 3 / 2 + 1;
            FunctionPoint[] newPoints = new FunctionPoint[newCapacity];
            System.arraycopy(points, 0, newPoints, 0, pointsCount);
            points = newPoints;
        }

        if (insertIndex < pointsCount) {
            System.arraycopy(points, insertIndex, points, insertIndex + 1, pointsCount - insertIndex);
        }

        points[insertIndex] = new FunctionPoint(point);
        pointsCount++;
    }

    // Required no-arg constructor for Externalizable
    public ArrayTabulatedFunction() {
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeInt(pointsCount);
        for (int i = 0; i < pointsCount; i++) {
            out.writeDouble(points[i].getX());
            out.writeDouble(points[i].getY());
        }
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        int n = in.readInt();
        this.pointsCount = n;
        this.points = new FunctionPoint[n + 5];
        for (int i = 0; i < n; i++) {
            double x = in.readDouble();
            double y = in.readDouble();
            this.points[i] = new FunctionPoint(x, y);
        }
        if (this.pointsCount < this.points.length) {
            Arrays.fill(this.points, this.pointsCount, this.points.length, null);
        }
    }
}