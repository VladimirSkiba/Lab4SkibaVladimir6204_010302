import functions.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Лабораторная работа №3 ===");

        // Тестирование ArrayTabulatedFunction
        System.out.println("\n1. Тестирование ArrayTabulatedFunction:");
        testTabulatedFunction(new ArrayTabulatedFunction(0, 4, new double[]{0, 1, 4, 9, 16}), "Array");

        // Тестирование LinkedListTabulatedFunction
        System.out.println("\n2. Тестирование LinkedListTabulatedFunction:");
        testTabulatedFunction(new LinkedListTabulatedFunction(0, 4, new double[]{0, 1, 4, 9, 16}), "Linked List");

        // Тестирование исключений
        System.out.println("\n3. Тестирование исключений:");
        testExceptions();
    }

    private static void testTabulatedFunction(TabulatedFunction func, String type) {
        System.out.println("   Тип: " + type);
        System.out.println("   Границы: [" + func.getLeftDomainBorder() + ", " + func.getRightDomainBorder() + "]");
        System.out.println("   Количество точек: " + func.getPointsCount());

        // Вывод всех точек ДО операций
        System.out.println("   Исходные точки:");
        for (int i = 0; i < func.getPointsCount(); i++) {
            System.out.println("     [" + i + "] (" + func.getPointX(i) + ", " + func.getPointY(i) + ")");
        }

        // Тестирование вычислений
        System.out.println("   Тестирование вычислений:");
        double[] testPoints = {-1, 0, 0.5, 1, 1.5, 2, 2.5, 3, 3.5, 4, 5};
        for (double x : testPoints) {
            double y = func.getFunctionValue(x);
            System.out.println("     f(" + x + ") = " + y);
        }

        // Тестирование операций
        System.out.println("   Тестирование операций с точками:");
        try {
            func.setPointY(2, 5);
            System.out.println("     setPointY(2, 5) - успешно");
            System.out.println("     Результат: (" + func.getPointX(2) + ", " + func.getPointY(2) + ")");

            func.addPoint(new FunctionPoint(1.5, 2.25));
            System.out.println("     addPoint(1.5, 2.25) - успешно, точек: " + func.getPointsCount());

            func.deletePoint(1);
            System.out.println("     deletePoint(1) - успешно, точек: " + func.getPointsCount());

            // Вывод точек ПОСЛЕ операций
            System.out.println("     Точки после операций:");
            for (int i = 0; i < func.getPointsCount(); i++) {
                System.out.println("       [" + i + "] (" + func.getPointX(i) + ", " + func.getPointY(i) + ")");
            }

        } catch (Exception e) {
            System.out.println("     Ошибка: " + e.getMessage());
        }
    }

    private static void testExceptions() {
        // Тестирование исключений конструкторов
        System.out.println("   Тестирование конструкторов:");

        try {
            TabulatedFunction badFunc1 = new ArrayTabulatedFunction(5, 0, 3);
            System.out.println("     Array: левая граница > правой - ОШИБКА: должно быть исключение");
        } catch (IllegalArgumentException e) {
            System.out.println("     Array: левая граница > правой - корректно: " + e.getMessage());
        }

        try {
            TabulatedFunction badFunc2 = new LinkedListTabulatedFunction(0, 4, 1);
            System.out.println("     LinkedList: точек < 2 - ОШИБКА: должно быть исключение");
        } catch (IllegalArgumentException e) {
            System.out.println("     LinkedList: точек < 2 - корректно: " + e.getMessage());
        }

        // Тестирование исключений индексов
        System.out.println("   Тестирование исключений индексов:");
        TabulatedFunction func = new ArrayTabulatedFunction(0, 2, 3);

        try {
            func.getPoint(10);
            System.out.println("     getPoint(10) - ОШИБКА: должно быть исключение");
        } catch (FunctionPointIndexOutOfBoundsException e) {
            System.out.println("     getPoint(10) - корректно: " + e.getMessage());
        }

        // Тестирование исключений порядка точек
        System.out.println("   Тестирование исключений порядка точек:");

        try {
            func.setPointX(1, 0); // Попытка установить X меньше предыдущего
            System.out.println("     setPointX(1, 0) - ОШИБКА: должно быть исключение");
        } catch (InappropriateFunctionPointException e) {
            System.out.println("     setPointX(1, 0) - корректно: " + e.getMessage());
        }

        try {
            func.addPoint(new FunctionPoint(1.0, 1.0)); // Попытка добавить точку с существующим X
            System.out.println("     addPoint(1.0, 1.0) - ОШИБКА: должно быть исключение");
        } catch (InappropriateFunctionPointException e) {
            System.out.println("     addPoint(1.0, 1.0) - корректно: " + e.getMessage());
        }

        // Тестирование исключения состояния
        System.out.println("   Тестирование исключения состояния:");
        TabulatedFunction smallFunc = new ArrayTabulatedFunction(0, 1, 2);

        try {
            smallFunc.deletePoint(0); // Попытка удалить при 2 точках
            System.out.println("     deletePoint(0) при 2 точках - ОШИБКА: должно быть исключение");
        } catch (IllegalStateException e) {
            System.out.println("     deletePoint(0) при 2 точках - корректно: " + e.getMessage());
        }

        // Демонстрация полиморфизма
        System.out.println("\n4. Демонстрация полиморфизма:");
        demonstratePolymorphism();
    }

    private static void demonstratePolymorphism() {
        // Работа через интерфейс с разными реализациями
        TabulatedFunction[] functions = {
                new ArrayTabulatedFunction(0, 2, new double[]{0, 1, 4}),
                new LinkedListTabulatedFunction(0, 2, new double[]{0, 1, 4})
        };

        String[] types = {"Array", "Linked List"};

        for (int i = 0; i < functions.length; i++) {
            System.out.println("   " + types[i] + " реализация:");
            System.out.println("     getPointsCount(): " + functions[i].getPointsCount());
            System.out.println("     getFunctionValue(1.5): " + functions[i].getFunctionValue(1.5));

            // Проверка, что возвращаются копии точек (инкапсуляция)
            FunctionPoint point = functions[i].getPoint(1);
            point.setX(999); // Изменение копии не должно влиять на оригинал
            System.out.println("     После изменения копии, оригинал: " + functions[i].getPointX(1));
        }
    }
    private static void testNewKONSTRUKTORS(){
        // Тестирование новых конструкторов
        System.out.println("\nТестирование конструкторов с массивом точек:");

        // Корректный массив точек
        FunctionPoint[] points = {
                new FunctionPoint(0, 0),
                new FunctionPoint(1, 1),
                new FunctionPoint(2, 4),
                new FunctionPoint(3, 9)
        };

        try {
            TabulatedFunction arrayFromPoints = new ArrayTabulatedFunction(points);
            TabulatedFunction listFromPoints = new LinkedListTabulatedFunction(points);
            System.out.println("Конструкторы с массивом точек работают корректно");
            System.out.println("Array точек: " + arrayFromPoints.getPointsCount());
            System.out.println("LinkedList точек: " + listFromPoints.getPointsCount());
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }

        // Тестирование исключений
        try {
            FunctionPoint[] invalidPoints = {new FunctionPoint(0, 0)}; // Только 1 точка
            new ArrayTabulatedFunction(invalidPoints);
            System.out.println("ОШИБКА: Должно быть исключение для 1 точки");
        } catch (IllegalArgumentException e) {
            System.out.println("Корректно: " + e.getMessage());
        }

        try {
            FunctionPoint[] unorderedPoints = {
                    new FunctionPoint(2, 4),
                    new FunctionPoint(1, 1), // Неупорядочено
                    new FunctionPoint(3, 9)
            };
            new LinkedListTabulatedFunction(unorderedPoints);
            System.out.println("ОШИБКА: Должно быть исключение для неупорядоченных точек");
        } catch (IllegalArgumentException e) {
            System.out.println("Корректно: " + e.getMessage());
        }
    }
}