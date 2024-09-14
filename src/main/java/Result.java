public class Result {
    private double result;
    private double e;
    private int n;

    public double getResult() {
        return result;
    }

    public double getE() {
        return e;
    }

    public int getN() {
        return n;
    }

    public Result(double res, double e, int n) {
        this.result = res;
        this.e = e;
        this.n = n;
    }

    @Override
    public String toString() {
        return "Result: " + result + "\ne: " + e + "\nn: " + n;
    }
}
