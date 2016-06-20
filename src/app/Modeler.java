package app;

public class Modeler {

    private double[][] a; // таблица
    private int numberOfConstraints; // число ограничений
    private int numberOfOriginalVariables; // число оригинальных переменных

    public Modeler(double[][] constraintLeftSide, double[] constraintRightSide, Data.Constraint[] constraintOperator, double[] objectiveFunction) {
        numberOfConstraints = constraintRightSide.length;
        numberOfOriginalVariables = objectiveFunction.length;
        a = new double[numberOfConstraints + 1][numberOfOriginalVariables + numberOfConstraints + 1];

        // инициализация ограничений
        for (int i = 0; i < numberOfConstraints; i++) {
            for (int j = 0; j < numberOfOriginalVariables; j++) {
                a[i][j] = constraintLeftSide[i][j];
            }
        }

        for (int i = 0; i < numberOfConstraints; i++) {
            a[i][numberOfConstraints + numberOfOriginalVariables] = constraintRightSide[i];
        }

        // инициализация слабых переменных
        for (int i = 0; i < numberOfConstraints; i++) {
            int slack = 0;
            switch (constraintOperator[i]) {
                case greaterThan:
                    slack = -1;
                    break;
                case lessThan:
                    slack = 1;
                    break;
                default:
            }
            a[i][numberOfOriginalVariables + i] = slack;
        }

        // инициализация целевой функции
        for (int j = 0; j < numberOfOriginalVariables; j++) {
            a[numberOfConstraints][j] = objectiveFunction[j];
        }
    }

    public double[][] getTableaux() {
        return a;
    }

    public int getNumberOfConstraint() {
        return numberOfConstraints;
    }

    public int getNumberOfOriginalVariable() {
        return numberOfOriginalVariables;
    }
}
