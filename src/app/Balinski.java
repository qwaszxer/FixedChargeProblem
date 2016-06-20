package app;

import java.io.File;

public class Balinski {

    private static final boolean MAXIMIZE = true;
    private static final boolean MINIMIZE = false;

    public String solve(String choice, File selectedDirectory) {
        Data data = new Data();
        String solution = data.getData(choice, selectedDirectory);
        if (!("").equals(solution))
            return solution;

        double[] C = data.getC();
        double[] D = data.getD();
        double[] M = new double[D.length];
        double[][] constraintLeftSide = data.getConstraintLeftSide();
        Data.Constraint[] constraintOperator = data.getConstraintOperator();
        double[] constraintRightSide = data.getConstraintRightSide();
        if (("Из файла").equals(choice)) {
             M = data.getM();
        }
        else {
            for (int i = 0; i < D.length; i++) {
                double[] objectiveFunc = new double[C.length];
                objectiveFunc[i] = -1;
                for (int j = 0; j < C.length; j++) {
                    if (j != i)
                        objectiveFunc[j] = 0;
                }

                solution += "Вычисление М[" + (i+1) + "]. Решается следующая задача: \n";
                solution += "Найти минимум целевой функции: ";
                for (int j = 0; j < objectiveFunc.length; j++) {
                    if ((objectiveFunc[j] >= 0) && (j != 0))
                        solution += "+";
                    solution += objectiveFunc[j] + "x[" + (j+1) + "] ";
                }
                solution += "\nОграничения: \n";
                for (int k = 0; k < constraintRightSide.length; k++) {
                    for (int j = 0; j < C.length; j++) {
                        if ((constraintLeftSide[k][j] >= 0) && (j != 0))
                            solution += "+";
                        solution += constraintLeftSide[k][j] + "x[" + (j+1) + "] ";
                    }
                    switch (constraintOperator[k]) {
                        case greaterThan:
                            solution += ">= ";
                            break;
                        case lessThan:
                            solution += "<= ";
                            break;
                        default:
                            solution += "= ";
                    }
                    solution += constraintRightSide[k] + "\n";
                }
                solution += "\nРешение симплекс методом: \n";

                Modeler model = new Modeler(constraintLeftSide, constraintRightSide, constraintOperator, objectiveFunc);
                Simplex simplex = new Simplex(model.getTableaux(), model.getNumberOfConstraint(), model.getNumberOfOriginalVariable(), MINIMIZE);
                solution += simplex.getSolution();

                solution += "Решение задачи: \n";
                double[] x = simplex.primal();
                int[] y = new int[x.length];
                for (int j = 0; j < x.length; j++) {
                    solution += "x[" + (j+1) + "] = " + x[j] +"\n";
                }
                solution += "Значение целевой функции = " + simplex.value() + "\n\n";

                M[i] = x[i];
                solution += "M[" + (i+1) + "] = " + M[i] + "\n\n";
            }
        }

        solution += "Решается следующая задача с фиксированными доплатами: \n";
        solution += "Найти минимум целевой функции: ";
        for (int i = 0; i < C.length; i++) {
            if ((C[i] >= 0) && (i != 0))
                solution += "+";
            solution += C[i] + "x[" + (i+1) + "] ";
        }
        for (int i = 0; i < D.length; i++) {
            if (D[i] >= 0)
                solution += "+";
            solution += D[i] + "y[" + (i+1) + "] ";
        }
        solution += "\nЗначения М: \n";
        for (int i = 0; i < M.length; i++) {
            solution += "M[" + (i+1) + "] = " + M[i] + "\n";
        }
        solution += "Ограничения: \n";
        for (int i = 0; i < constraintRightSide.length; i++) {
            for (int j = 0; j < C.length; j++) {
                if ((constraintLeftSide[i][j] >= 0) && (j != 0))
                    solution += "+";
                solution += constraintLeftSide[i][j] + "x[" + (j+1) + "] ";
            }
            switch (constraintOperator[i]) {
                case greaterThan:
                    solution += ">= ";
                    break;
                case lessThan:
                    solution += "<= ";
                    break;
                default:
                    solution += "= ";
            }
            solution += constraintRightSide[i] + "\n";
        }

        double[] objectiveFunc = new double[C.length];

        for (int i = 0; i < D.length; i++) {
            objectiveFunc[i] = C[i] + D[i] / M[i];
        }
        for (int i = D.length; i < C.length; i++) {
            objectiveFunc[i] = C[i];
        }

        solution += "Преобразованная по методу Балинского целевая функция: ";
        for (int i = 0; i < objectiveFunc.length; i++) {
            if ((objectiveFunc[i] > 0) && (i != 0))
                solution += "+";
            solution += objectiveFunc[i] + "x[" + (i+1) + "] ";
        }
        solution += "\n\nРешение симплекс методом: \n";

        Modeler model = new Modeler(data.getConstraintLeftSide(), data.getConstraintRightSide(), data.getConstraintOperator(), objectiveFunc);
        Simplex simplex = new Simplex(model.getTableaux(), model.getNumberOfConstraint(), model.getNumberOfOriginalVariable(), MINIMIZE);
        solution += simplex.getSolution();

        solution += "Решение задачи с фиксированными доплатами: \n";
        double[] x = simplex.primal();
        int[] y = new int[x.length];
        for (int i = 0; i < D.length; i++) {
            if(x[i] == 0)
                y[i] = 0;
            else
                y[i] = 1;
        }
        for (int i = 0; i < x.length; i++) {
            solution += "x[" + (i+1) + "] = " + x[i];
            if(i < D.length)
                solution += "   y[" + (i+1) + "] = " + y[i];
            solution += "\n";
        }

        solution += "Значение целевой функции = " + simplex.value() + "\n\n";

        return solution;
    }
}
