package functions;

public class LinkedListTabulatedFunction implements TabulatedFunction{

    private static final double EPSILON = 1e-10;


    private static class FunctionNode {
        FunctionPoint point;
        FunctionNode prev;
        FunctionNode next;

        FunctionNode(FunctionPoint point) {
            this.point = point;
        }
    }

    private FunctionNode head;
    private int pointsCount;
    private FunctionNode lastAccessed;
    private int lastAccessedIndex;

    public LinkedListTabulatedFunction() {
        head = new FunctionNode(null);
        head.prev = head;
        head.next = head;
        pointsCount = 0;
        lastAccessed = head;
        lastAccessedIndex = -1;
    }

    public LinkedListTabulatedFunction(double leftX, double rightX, int pointsCount) {
        this();

        if (leftX >= rightX) {
            throw new IllegalArgumentException("Левая граница должна быть меньше правой: " + leftX + " >= " + rightX);
        }
        if (pointsCount < 2) {
            throw new IllegalArgumentException("Количество точек должно быть не менее 2: " + pointsCount);
        }

        double step = (rightX - leftX) / (pointsCount - 1);
        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + i * step;
            FunctionNode newNode = addNodeToTail();
            newNode.point = new FunctionPoint(x, 0);
        }
    }

    public LinkedListTabulatedFunction(double leftX, double rightX, double[] values) {
        this();

        if (leftX >= rightX) {
            throw new IllegalArgumentException("Левая граница должна быть меньше правой: " + leftX + " >= " + rightX);
        }
        if (values.length < 2) {
            throw new IllegalArgumentException("Количество точек должно быть не менее 2: " + values.length);
        }


        double step = (rightX - leftX) / (values.length - 1);
        for (int i = 0; i < values.length; i++) {
            double x = leftX + i * step;
            FunctionNode newNode = addNodeToTail();
            newNode.point = new FunctionPoint(x, values[i]);
        }
    }

    public LinkedListTabulatedFunction(FunctionPoint[] points) {
        this();

        if (points == null) {
            throw new IllegalArgumentException("Массив точек не может быть null");
        }
        if (points.length < 2) {
            throw new IllegalArgumentException("Количество точек должно быть не менее 2: " + points.length);
        }

        for (int i = 0; i < points.length - 1; i++) {
            if (points[i].getX() >= points[i + 1].getX() - EPSILON) {
                throw new IllegalArgumentException("Точки не упорядочены по X или содержат дубликаты");
            }
        }

        for (FunctionPoint point : points) {
            FunctionNode newNode = addNodeToTail();
            newNode.point = new FunctionPoint(point); // Создаем копию
        }
    }

    private FunctionNode getNodeByIndex(int index) {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException(index, pointsCount);
        }

        FunctionNode current;
        int startIndex;

        if (lastAccessedIndex != -1 && Math.abs(index - lastAccessedIndex) < Math.abs(index)) {
            current = lastAccessed;
            startIndex = lastAccessedIndex;
        } else {
            current = head.next;
            startIndex = 0;
        }

        if (index >= startIndex) {
            for (int i = startIndex; i < index; i++) {
                current = current.next;
            }
        } else {
            for (int i = startIndex; i > index; i--) {
                current = current.prev;
            }
        }

        lastAccessed = current;
        lastAccessedIndex = index;

        return current;
    }

    private FunctionNode addNodeToTail() {
        FunctionNode newNode = new FunctionNode(new FunctionPoint(0, 0));

        if (pointsCount == 0) {
            newNode.prev = head;
            newNode.next = head;
            head.next = newNode;
            head.prev = newNode;
        } else {
            newNode.prev = head.prev;
            newNode.next = head;
            head.prev.next = newNode;
            head.prev = newNode;
        }

        pointsCount++;
        lastAccessed = newNode;
        lastAccessedIndex = pointsCount - 1;

        return newNode;
    }

    private FunctionNode addNodeByIndex(int index) {
        if (index < 0 || index > pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException(index, pointsCount);
        }

        FunctionNode newNode = new FunctionNode(new FunctionPoint(0, 0));

        if (pointsCount == 0) {
            newNode.prev = head;
            newNode.next = head;
            head.next = newNode;
            head.prev = newNode;
        } else {
            FunctionNode targetNode;
            if (index == pointsCount) {
                targetNode = head.prev;
            } else {
                targetNode = getNodeByIndex(index);
            }

            newNode.prev = targetNode.prev;
            newNode.next = targetNode;
            targetNode.prev.next = newNode;
            targetNode.prev = newNode;
        }

        pointsCount++;
        lastAccessed = newNode;
        lastAccessedIndex = index;

        return newNode;
    }

    private FunctionNode deleteNodeByIndex(int index) {
        if (pointsCount <= 2) {
            throw new IllegalStateException("Невозможно удалить точку: должно остаться минимум 2 точки");
        }
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException(index, pointsCount);
        }

        FunctionNode nodeToDelete = getNodeByIndex(index);

        nodeToDelete.prev.next = nodeToDelete.next;
        nodeToDelete.next.prev = nodeToDelete.prev;

        pointsCount--;

        if (lastAccessedIndex == index) {
            lastAccessed = head.next;
            lastAccessedIndex = 0;
        } else if (lastAccessedIndex > index) {
            lastAccessedIndex--;
        }

        return nodeToDelete;
    }

    public double getLeftDomainBorder() {
        if (pointsCount == 0) {
            throw new IllegalStateException("Функция не содержит точек");
        }
        return head.next.point.getX();
    }

    public double getRightDomainBorder() {
        if (pointsCount == 0) {
            throw new IllegalStateException("Функция не содержит точек");
        }
        return head.prev.point.getX();
    }

    public double getFunctionValue(double x) {
        if (pointsCount == 0) return Double.NaN;

        double leftBorder = getLeftDomainBorder();
        double rightBorder = getRightDomainBorder();

        if (x < leftBorder - EPSILON || x > rightBorder + EPSILON) {
            return Double.NaN;
        }

        FunctionNode current = head.next;
        for (int i = 0; i < pointsCount; i++) {
            if (Math.abs(x - current.point.getX()) < EPSILON) {
                return current.point.getY();
            }
            current = current.next;
        }

        current = head.next;
        for (int i = 0; i < pointsCount - 1; i++) {
            double x1 = current.point.getX();
            double x2 = current.next.point.getX();

            if (x >= x1 - EPSILON && x <= x2 + EPSILON) {
                double y1 = current.point.getY();
                double y2 = current.next.point.getY();
                return y1 + (y2 - y1) * (x - x1) / (x2 - x1);
            }
            current = current.next;
        }

        return Double.NaN;
    }


    public int getPointsCount() {
        return pointsCount;
    }

    public FunctionPoint getPoint(int index) {
        FunctionNode node = getNodeByIndex(index);
        return new FunctionPoint(node.point);
    }

    public void setPoint(int index, FunctionPoint point) {
        FunctionNode node = getNodeByIndex(index);

        if (index > 0) {
            FunctionNode prevNode = node.prev;
            if (point.getX() <= prevNode.point.getX()) {
                throw new InappropriateFunctionPointException(
                        "X координата " + point.getX() + " должна быть больше предыдущей " + prevNode.point.getX());
            }
        }
        if (index < pointsCount - 1) {
            FunctionNode nextNode = node.next;
            if (point.getX() >= nextNode.point.getX()) {
                throw new InappropriateFunctionPointException(
                        "X координата " + point.getX() + " должна быть меньше следующей " + nextNode.point.getX());
            }
        }

        node.point = new FunctionPoint(point);
    }

    public double getPointX(int index) {
        FunctionNode node = getNodeByIndex(index);
        return node.point.getX();
    }

    public void setPointX(int index, double x) {
        FunctionNode node = getNodeByIndex(index);

        if (index > 0) {
            FunctionNode prevNode = node.prev;
            if (x <= prevNode.point.getX()) {
                throw new InappropriateFunctionPointException(
                        "X координата " + x + " должна быть больше предыдущей " + prevNode.point.getX());
            }
        }
        if (index < pointsCount - 1) {
            FunctionNode nextNode = node.next;
            if (x >= nextNode.point.getX()) {
                throw new InappropriateFunctionPointException(
                        "X координата " + x + " должна быть меньше следующей " + nextNode.point.getX());
            }
        }

        node.point.setX(x);
    }

    public double getPointY(int index) {
        FunctionNode node = getNodeByIndex(index);
        return node.point.getY();
    }

    public void setPointY(int index, double y) {
        FunctionNode node = getNodeByIndex(index);
        node.point.setY(y);
    }

    public void deletePoint(int index) {
        deleteNodeByIndex(index);
    }


    public void addPoint(FunctionPoint point) {
        int insertIndex = 0;
        FunctionNode current = head.next;

        while (insertIndex < pointsCount && current.point.getX() < point.getX() - EPSILON) {
            current = current.next;
            insertIndex++;
        }

        if (insertIndex < pointsCount && Math.abs(current.point.getX() - point.getX()) < EPSILON) {
            throw new InappropriateFunctionPointException("Точка с X=" + point.getX() + " уже существует");
        }

        FunctionNode newNode = addNodeByIndex(insertIndex);
        newNode.point = new FunctionPoint(point);
    }
}