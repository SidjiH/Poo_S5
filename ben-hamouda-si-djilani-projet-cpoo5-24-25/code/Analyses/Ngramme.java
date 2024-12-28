package Analyses;

public final class Ngramme {
    private final String seq;
    private final int occur;

    public Ngramme(String sequence, int occurence) {
        this.seq = sequence;
        this.occur = occurence;
    }

    public String getSequence() {
        return seq;
    }

    public int getOccurence() {
        return occur;
    }

    @Override
    public String toString() {
        return "NGramme{" +
               "sequence='" + seq + '\'' +
               ", occurence=" + occur +
               '}';
    }
}
