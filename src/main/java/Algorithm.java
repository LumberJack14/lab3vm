public class Algorithm {
    private static int selectedFunction = 0;
    private static int selectedMethod = 0;

    public static void setSelectedFunction(int selectedFunction) {
        Algorithm.selectedFunction = selectedFunction;
    }

    public static int getSelectedFunction() {
        return selectedFunction;
    }

    public static void setSelectedMethod(int selectedMethod) {
        Algorithm.selectedMethod = selectedMethod;
    }

    public static int getSelectedMethod() {
        return selectedMethod;
    }

    public static Result Integrate(double a, double b, int n) throws
            InvalidIntervalException,
            BreakPointException,
            FunctionNotDefinedException
    {

        if (a > b) {
            throw new InvalidIntervalException("b should be bigger than a");
        }

        // hardcoded shenanigans
        if (selectedFunction == 0) {
            if (a == 0 || b == 0) {
                throw new BreakPointException("unable to calculate the integral with the second-type break point as an interval's edge point");
            }

            if (a * b < 0) {
                if (b > Math.abs(a)) {
                    a *= -1;
                } else {
                    b *= -1;
                }
            }
        }

        if (selectedFunction == 1) {
            if (a < 0) {
                throw new FunctionNotDefinedException("function is not defined on a part of that interval");
            }
        }

        if (selectedFunction == 3) {
            if (a < 2 && b > 2) {
                Result res1 = Integrate(a, 2, n);
                Result res2 = Integrate(2, b, n);
                return new Result(res1.getResult() + res2.getResult(), res1.getE() + res2.getE(), n);
            }
        }

        double normalRes = 0;
        double twiceRes = 0;
        switch (selectedMethod) {
            case 0:
                normalRes = leftRect(a, b, n);
                twiceRes = leftRect(a, b, 2 * n);
                break;
            case 1:
                normalRes = midRect(a, b, n);
                twiceRes = midRect(a, b, 2 * n);
                break;
            case 2:
                normalRes = rightRect(a, b, n);
                twiceRes = rightRect(a, b, 2 * n);
                break;
            case 3:
                normalRes = trapezoid(a, b, n);
                twiceRes = trapezoid(a, b, 2 * n);
                break;
            case 4:
                normalRes = simpson(a, b, n);
                twiceRes = simpson(a, b, 2 * n);
                break;
        }

        return new Result(normalRes, calculateRunge(normalRes, twiceRes), n);
    }

    private static double calculateRunge(double normalRes, double twiceRes) {
        switch (selectedMethod) {
            case 4:
                return Math.abs((twiceRes - normalRes) / 15);
            default:
                return Math.abs((twiceRes - normalRes) / 3);

        }
    }

    private static double func0(double x) {
        return 1 / x;
    }

    private static double func1(double x) {
        return Math.sqrt(x);
    }

    private static double func2(double x) {
        return Math.pow(x, 3) + 2 * Math.pow(x, 2) - 3 * x;
    }

    private static double func3(double x) {
        return 1 / (0.3 + Math.exp(1 / (x - 2)));
    }

    private static double function(double x) {
        switch (selectedFunction) {
            case 0:
                return func0(x);
            case 1:
                return func1(x);
            case 2:
                return func2(x);
            case 3:
                return func3(x);
            default:
                return 0;
        }
    }


                    /********************
                     *                  *
                     *      METHODS     *
                     *                  *
                     *******************/

    public static double leftRect(double a, double b, int n) {
        double width = (b - a) / n;
        double sum = 0.0;

        for (int i = 0; i < n; i++) {
            double x = a + i * width;
            sum += function(x) * width;
        }

        return sum;
    }

    public static double rightRect(double a, double b, int n) {
        double width = (b - a) / n;
        double sum = 0.0;

        for (int i = 1; i <= n; i++) {
            double x = a + i * width;
            sum += function(x) * width;
        }

        return sum;
    }


    public static double midRect(double a, double b, int n) {
        double width = (b - a) / n;
        double sum = 0.0;

        for (int i = 0; i < n; i++) {
            double x = a + (i + 0.5) * width;
            sum += function(x) * width;
        }

        return sum;
    }

    public static double trapezoid(double a, double b, int n) {
        double width = (b - a) / n;
        double sum = 0.0;

        for (int i = 1; i < n; i++) {
            double x = a + i * width;
            sum += function(x);
        }

        sum += (function(a) + function(b)) / 2.0;

        return sum * width;
    }

    public static double simpson(double a, double b, int n) {
        if (n % 2 != 0) {
            n++;
        }

        double width = (b - a) / n;
        double sum = function(a) + function(b);

        for (int i = 1; i < n; i++) {
            double x = a + i * width;

            if (i % 2 == 0) {
                sum += 2 * function(x);
            } else {
                sum += 4 * function(x);
            }
        }

        return (sum * width) / 3.0;
    }
}
