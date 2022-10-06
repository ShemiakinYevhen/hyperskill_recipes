import java.util.Objects;

class ComplexNumber {

    private final double re;
    private final double im;

    public ComplexNumber(double re, double im) {
        this.re = re;
        this.im = im;
    }

    public double getRe() {
        return re;
    }

    public double getIm() {
        return im;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;

        if (!(obj instanceof ComplexNumber)) return false;

        ComplexNumber toCompare = (ComplexNumber) obj;

        return Objects.equals(re, toCompare.getRe()) && Objects.equals(im, toCompare.getIm());
    }

    @Override
    public int hashCode() {
        return Objects.hash(re, im);
    }
}