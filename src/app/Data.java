package app;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Data {

    public enum Constraint {
        lessThan, equal, greaterThan
    }
    private double[] C;
    private double[] D;
    private double[] M;
    private double[][] constraintLeftSide;
    private double[] constraintRightSide;
    private Constraint[] constraintOperator;

    public double[] getC() {
        return C;
    }

    public double[] getD() {
        return D;
    }

    public double[] getM() {
        return M;
    }

    public double[][] getConstraintLeftSide() {
        return constraintLeftSide;
    }

    public Constraint[] getConstraintOperator() {
        return constraintOperator;
    }

    public double[] getConstraintRightSide() {
        return constraintRightSide;
    }

    public String getData(String choice, File selectedDirectory) {
        try {
            String data = "";
            Scanner input = new Scanner(selectedDirectory);
            while (input.hasNext()) {
                data += input.nextLine();
            }
            input.close();

            JSONObject object = new JSONObject(data);

            JSONArray array1 = object.optJSONArray("C");
            C = new double[array1.length()];
            for (int i = 0; i < array1.length(); i++) {
                C[i] = array1.optDouble(i);
            }

            JSONArray array2 = object.optJSONArray("D");
            if (array2.length() > array1.length())
                throw new Exception();
            D = new double[array2.length()];
            for (int i = 0; i < array2.length(); i++) {
                D[i] = array2.optDouble(i);
            }

            if (("Из файла").equals(choice)) {
                JSONArray array3 = object.optJSONArray("M");
                if (array3.length() != array2.length())
                    throw new Exception();
                M = new double[array3.length()];
                for (int i = 0; i < array3.length(); i++) {
                    M[i] = array3.optDouble(i);
                }
            }

            JSONArray array4 = object.optJSONArray("constraintLeftSide");
            constraintLeftSide = new double[array4.length()][array4.optJSONArray(0).length()];
            for (int i = 0; i < array4.length(); i++) {
                JSONArray temp = array4.optJSONArray(i);
                if (array1.length() != temp.length())
                    throw new Exception();
                for (int j = 0; j < temp.length(); j++) {
                    constraintLeftSide[i][j] = temp.optDouble(j);
                }
            }

            JSONArray array5 = object.optJSONArray("constraintOperator");
            if (array5.length() != array4.length())
                throw new Exception();
            constraintOperator = new Constraint[array5.length()];
            for (int i = 0; i < array5.length(); i++) {
                switch (array5.optString(i)) {
                    case "lessThan" :
                        constraintOperator[i] = Constraint.lessThan;
                        break;
                    case "greaterThan" :
                        constraintOperator[i] = Constraint.greaterThan;
                        break;
                    case "equal" :
                        constraintOperator[i] = Constraint.equal;
                        break;
                    default:
                }
            }

            JSONArray array6 = object.optJSONArray("constraintRightSide");
            if (array6.length() != array4.length())
                throw new Exception();
            constraintRightSide = new double[array6.length()];
            for (int i = 0; i < array6.length(); i++) {
                constraintRightSide[i] = array6.optDouble(i);
            }

            return "";

        } catch (FileNotFoundException e) {
            return "Файл не найден. \n\n";
        } catch (JSONException e) {
            return "Исходные данные не соответствуют нужному формату. \n\n";
        } catch (NullPointerException e) {
            return "Некоторые компоненты исходных данных отсутствуют. \n\n";
        } catch (Exception e) {
            return "Размерность некоторых компонентов исходных данных не соответствует. \n\n";
        }
    }
}
