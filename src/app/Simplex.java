package app;

public class Simplex {

    private double[][] tableaux; // симплекс-таблица
    private int numberOfConstraints; // количество ограничений
    private int numberOfOriginalVariables; // число основных переменных
    private boolean maximizeOrMinimize; // поиск максимума или минимума
    private int[] basis; // basis[i] - базисная переменная, соответствующая строке i
    private String solution;

    public Simplex(double[][] tableaux, int numberOfConstraint, int numberOfOriginalVariable, boolean maximizeOrMinimize) {
        this.maximizeOrMinimize = maximizeOrMinimize;
        this.numberOfConstraints = numberOfConstraint;
        this.numberOfOriginalVariables = numberOfOriginalVariable;
        this.tableaux = tableaux;
        this.solution = "";

        basis = new int[numberOfConstraints];
        for (int i = 0; i < numberOfConstraints; i++) {
            basis[i] = numberOfOriginalVariables + i;
        }

        solution += "Количество ограничений = " + numberOfConstraints + "\n";
        solution += "Количество основных переменных = " + numberOfOriginalVariables + "\n\n";

        solve();
    }

    // основной цикл решения
    private void solve() {
        while (true) {
            record();
            int q = 0;

            // поиск разрешающего столбца q
            if (maximizeOrMinimize) {
                q = dantzig();
            } else {
                q = dantzigNegative();
            }
            if (q == -1)
                break; // найдено оптимальное решение

            // поиск разрешающей строки p
            int p = minRatioRule(q);
            if (p == -1) {
                solution += "Данная задача не ограничена. \n\n";
                break;
            }

            // перерасчёт симплекс-таблицы
            pivot(p, q);

            // обновление базиса
            basis[p] = q;
        }
    }

    // определение индекса небазисного столбца с наибольшей положительной стоимостью
    private int dantzig() {
        int q = 0;
        for (int j = 1; j < numberOfConstraints + numberOfOriginalVariables; j++) {
            if (tableaux[numberOfConstraints][j] > tableaux[numberOfConstraints][q])
                q = j;
        }

        if (tableaux[numberOfConstraints][q] <= 0)
            return -1; // найдено оптимальное решение
        else
            return q;
    }

    // определение индекса небазисного столбца с наибольшей отрицательной стоимостью
    private int dantzigNegative() {
        int q = 0;
        for (int j = 1; j < numberOfConstraints + numberOfOriginalVariables; j++) {
            if (tableaux[numberOfConstraints][j] < tableaux[numberOfConstraints][q])
                q = j;
        }

        if (tableaux[numberOfConstraints][q] >= 0)
            return -1; // найдено оптимальное решение
        else
            return q;
    }

    // поиск разрешающей строки p
    private int minRatioRule(int q) {
        int p = -1;
        for (int i = 0; i < numberOfConstraints; i++) {
            if (tableaux[i][q] <= 0)
                continue;
            else if (p == -1)
                p = i;
            else if ((tableaux[i][numberOfConstraints + numberOfOriginalVariables] / tableaux[i][q]) < (tableaux[p][numberOfConstraints + numberOfOriginalVariables] / tableaux[p][q]))
                p = i;
        }
        return p;
    }

    // перерасчёт симплекс-таблицы методом Жордана-Гаусса
    private void pivot(int p, int q) {
        // перерасчёт всего, кроме строки p и столбца q
        for (int i = 0; i <= numberOfConstraints; i++) {
            for (int j = 0; j <= numberOfConstraints + numberOfOriginalVariables; j++) {
                if (i != p && j != q)
                    tableaux[i][j] -= tableaux[p][j] * tableaux[i][q] / tableaux[p][q];
            }
        }

        // обнуление столбца q
        for (int i = 0; i <= numberOfConstraints; i++) {
            if (i != p)
                tableaux[i][q] = 0.0;
        }

        // расчёт строки p
        for (int j = 0; j <= numberOfConstraints + numberOfOriginalVariables; j++) {
            if (j != q)
                tableaux[p][j] /= tableaux[p][q];
        }
        tableaux[p][q] = 1.0;
    }

    // оптимальное значение
    public double value() {
        return -tableaux[numberOfConstraints][numberOfConstraints + numberOfOriginalVariables];
    }

    // вектор решения
    public double[] primal() {
        double[] x = new double[numberOfOriginalVariables];
        for (int i = 0; i < numberOfConstraints; i++) {
            if (basis[i] < numberOfOriginalVariables)
                x[basis[i]] = tableaux[i][numberOfConstraints + numberOfOriginalVariables];
        }
        return x;
    }

    // вывод симплекс-таблицы
    public void record() {
        for (int i = 0; i <= numberOfConstraints; i++) {
            for (int j = 0; j <= numberOfConstraints + numberOfOriginalVariables; j++) {
                solution += String.format("%7.2f ", tableaux[i][j]);
            }
            solution += "\n";
        }
        solution += "\nЗначение целевой функции = " + value() + "\nБазис: \n";
        for (int i = 0; i < numberOfConstraints; i++) {
            solution += "x[" + (basis[i]+1) + "] = " + tableaux[i][numberOfConstraints + numberOfOriginalVariables] + "\n";
        }
        solution += "\n";
    }

    public String getSolution() {
        return solution;
    }
}
