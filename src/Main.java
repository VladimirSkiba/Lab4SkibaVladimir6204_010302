import functions.*;
import functions.basic.*;
import java.io.*;
import java.util.Locale;

public class Main {
    public static void main(String[] args) {
        Locale.setDefault(Locale.US);
        System.out.println("=== Лабораторная работа №4 (сокращённый вывод) ===");

        // Короткие проверки работоспособности
        testNewConstructorsShort();
        testFunctionInterfaceShort();

        // Короткая демонстрация ЛР4
        System.out.println("\n--- Демонстрация: табулирование, IO и сериализация (сравнение) ---");
        try {
            demoLab4Compact();
        } catch (Exception e) {
            System.out.println("Ошибка демонстрации: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void printFileHex(File f, int maxBytes) {
        try (InputStream is = new FileInputStream(f)) {
            byte[] buf = new byte[maxBytes];
            int r = is.read(buf);
            if (r <= 0) {
                System.out.println("(empty)");
                return;
            }
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < r; i++) {
                sb.append(String.format("%02X ", buf[i]));
                if ((i + 1) % 16 == 0) sb.append('\n');
            }
            System.out.println(sb.toString());
        } catch (IOException e) {
            System.out.println("Ошибка чтения файла: " + e.getMessage());
        }
    }

    // Тестирование новых конструкторов (Задание 1)
    private static void testNewConstructorsShort() {
        FunctionPoint[] points = { new FunctionPoint(0,0), new FunctionPoint(1,1), new FunctionPoint(2,4) };
        TabulatedFunction a = new ArrayTabulatedFunction(points);
        TabulatedFunction l = new LinkedListTabulatedFunction(points);
        System.out.println("Constructors test: Array pts=" + a.getPointsCount() + ", Linked pts=" + l.getPointsCount());
    }

    // Тестирование интерфейса Function (Задание 2)
    private static void testFunctionInterfaceShort() {
        Function f = new ArrayTabulatedFunction(0,2,new double[]{0,1,4});
        System.out.println("Function test: domain=["+f.getLeftDomainBorder()+","+f.getRightDomainBorder()+"] f(1.5)="+f.getFunctionValue(1.5));
    }

    // Демонстрация из задания 8 и 9 (компактная реализация, вызывается из main)
    private static void demoLab4Compact() throws IOException, ClassNotFoundException {
        System.out.println("\nЗадание 8: Sin и Cos и табулирование");

        Function sin = new Sin();
        Function cos = new Cos();

        System.out.println("Значения sin и cos на [0, pi] с шагом 0.1:");
        for (double x = 0.0; x <= Math.PI + 1e-9; x += 0.1) {
            System.out.printf("x=%.2f sin=%.6f cos=%.6f%n", x, sin.getFunctionValue(x), cos.getFunctionValue(x));
        }

        // Табулирование с 10 точками
        TabulatedFunction tabSin = TabulatedFunctions.tabulate(sin, 0.0, Math.PI, 10);
        TabulatedFunction tabCos = TabulatedFunctions.tabulate(cos, 0.0, Math.PI, 10);

        System.out.println("\nСравнение табулированных и аналитических значений (шаг 0.1):");
        for (double x = 0.0; x <= Math.PI + 1e-9; x += 0.1) {
            double sTrue = sin.getFunctionValue(x);
            double cTrue = cos.getFunctionValue(x);
            double sTab = tabSin.getFunctionValue(x);
            double cTab = tabCos.getFunctionValue(x);
            System.out.printf("x=%.2f sin=%.6f tabSin=%.6f | cos=%.6f tabCos=%.6f%n", x, sTrue, sTab, cTrue, cTab);
        }

        // Сумма квадратов табулированных функций
        Function sumSquares = Functions.sum(Functions.power(tabSin, 2.0), Functions.power(tabCos, 2.0));
        System.out.println("\nСумма квадратов табулированных аналогов (на [0, pi]):");
        for (double x = 0.0; x <= Math.PI + 1e-9; x += 0.1) {
            System.out.printf("x=%.2f val=%.6f%n", x, sumSquares.getFunctionValue(x));
        }

        // Табулирование экспоненты и запись/чтение в текстовый файл
        TabulatedFunction tabExp = TabulatedFunctions.tabulate(new Exp(), 0.0, 10.0, 11);
        File expText = new File("exp_tab.txt");
        try (Writer w = new FileWriter(expText)) {
            TabulatedFunctions.writeTabulatedFunction(tabExp, w);
        }
        TabulatedFunction readExp;
        try (Reader r = new FileReader(expText)) {
            readExp = TabulatedFunctions.readTabulatedFunction(r);
        }

        System.out.println("\nСравнение экспоненты и считанной из текстового файла (шаг 1):");
        for (double x = 0.0; x <= 10.0 + 1e-9; x += 1.0) {
            System.out.printf("x=%.0f orig=%.6f read=%.6f%n", x, Math.exp(x), readExp.getFunctionValue(x));
        }

        // Табулирование логарифма и бинарная запись/чтение
        TabulatedFunction tabLog = TabulatedFunctions.tabulate(new Log(Math.E), 0.0, 10.0, 11);
        File logBin = new File("log_tab.bin");
        try (OutputStream os = new FileOutputStream(logBin)) {
            TabulatedFunctions.outputTabulatedFunction(tabLog, os);
        }
        TabulatedFunction readLog;
        try (InputStream is = new FileInputStream(logBin)) {
            readLog = TabulatedFunctions.inputTabulatedFunction(is);
        }

        System.out.println("\nСравнение логарифма и считанного из бинарного файла (шаг 1):");
        for (double x = 0.0; x <= 10.0 + 1e-9; x += 1.0) {
            System.out.printf("x=%.0f orig=%.6f read=%.6f%n", x, new Log(Math.E).getFunctionValue(x), readLog.getFunctionValue(x));
        }

        // Сериализация табулированной функции
        TabulatedFunction tabLogForSer = TabulatedFunctions.tabulate(Functions.composition(new Log(Math.E), new Exp()), 0.0, 10.0, 11);
        File serFile = new File("tabfunc_serialized.obj");
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(serFile))) {
            oos.writeObject(tabLogForSer);
        }
        TabulatedFunction deserialized;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(serFile))) {
            deserialized = (TabulatedFunction) ois.readObject();
        }

        System.out.println("\nСравнение сериализованной и десериализованной функции (шаг 1):");
        for (double x = 0.0; x <= 10.0 + 1e-9; x += 1.0) {
            System.out.printf("x=%.0f orig=%.6f deser=%.6f%n", x, tabLogForSer.getFunctionValue(x), deserialized.getFunctionValue(x));
        }

    // Сравнение Serializable (POJO) vs Externalizable (класс)
    System.out.println("\nСравнение Serializable vs Externalizable: сериализация в файлы и сравнение размеров");
        // Создадим табулированную функцию для сравнения
        TabulatedFunction tf = TabulatedFunctions.tabulate(new Exp(), 0.0, 10.0, 101);

        // Сериализация Externalizable (объект tf, который у нас - ArrayTabulatedFunction implements Externalizable)
        File extFile = new File("tf_externalizable.obj");
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(extFile))) {
            oos.writeObject(tf);
        }

        // Сериализация как Serializable: упакуем данные в POJO TabulatedFunctionData
        FunctionPoint[] pts = new FunctionPoint[tf.getPointsCount()];
        for (int i = 0; i < tf.getPointsCount(); i++) pts[i] = tf.getPoint(i);
        TabulatedFunctionData pojo = new TabulatedFunctionData(pts);
        File serPojoFile = new File("tf_serializable_pojo.obj");
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(serPojoFile))) {
            oos.writeObject(pojo);
        }

        long extSize = extFile.length();
        long pojoSize = serPojoFile.length();
        System.out.println("Externalizable file: " + extFile.getName() + " size=" + extSize + " bytes");
        System.out.println("Serializable(POJO) file: " + serPojoFile.getName() + " size=" + pojoSize + " bytes");

        // Покажем первые 64 байта обоих файлов в hex (для простого сравнения содержимого)
        System.out.println("\nПервые 64 байта externalizable (hex):");
        printFileHex(extFile, 64);
        System.out.println("\nПервые 64 байта serializable POJO (hex):");
        printFileHex(serPojoFile, 64);
    }
}